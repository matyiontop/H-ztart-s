import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HaztartasApiService } from '../../core/services/haztartas-api.service';
import { HufCurrencyPipe } from '../../shared/pipes/huf-currency.pipe';
import { Expense, Category, Priority } from '../../core/models/haztartas.models';

@Component({
  selector: 'app-expenses',
  standalone: true,
  imports: [CommonModule, FormsModule, HufCurrencyPipe],
  templateUrl: './expenses.component.html',
  styleUrls: ['./expenses.component.css']
})
export class ExpensesComponent implements OnInit {
  public apiService = inject(HaztartasApiService);

  public filterType = signal<'ALL' | 'FIXED' | 'VARIABLE'>('ALL');
  public isModalOpen = signal<boolean>(false);

  public currentExpense: Expense = {
    title: '',
    amount: 0,
    fixed: true,
    categoryId: undefined,
    expenseDate: new Date().toISOString().substring(0, 10),
    dueDayOfMonth: 10,
    priority: 'NEEDS',
    recurring: true
  };

  ngOnInit(): void {
    this.apiService.getExpenses().subscribe();
    this.apiService.getCategories().subscribe();
  }

  get filteredExpenses(): Expense[] {
    const list = this.apiService.expenses();
    if (this.filterType() === 'FIXED') {
      return list.filter(e => e.fixed);
    }
    if (this.filterType() === 'VARIABLE') {
      return list.filter(e => !e.fixed);
    }
    return list;
  }

  get totalFixedAmount(): number {
    return this.apiService.expenses().filter(e => e.fixed).reduce((sum, e) => sum + e.amount, 0);
  }

  get totalVariableAmount(): number {
    return this.apiService.expenses().filter(e => !e.fixed).reduce((sum, e) => sum + e.amount, 0);
  }

  get totalExpenseAmount(): number {
    return this.apiService.expenses().reduce((sum, e) => sum + e.amount, 0);
  }

  setFilter(filter: 'ALL' | 'FIXED' | 'VARIABLE'): void {
    this.filterType.set(filter);
  }

  openNewModal(): void {
    const cats = this.apiService.categories().filter(c => c.type === 'EXPENSE');
    this.currentExpense = {
      title: '',
      amount: 0,
      fixed: true,
      categoryId: cats.length > 0 ? cats[0].id : undefined,
      expenseDate: new Date().toISOString().substring(0, 10),
      dueDayOfMonth: 10,
      priority: 'NEEDS',
      recurring: true
    };
    this.isModalOpen.set(true);
  }

  closeModal(): void {
    this.isModalOpen.set(false);
  }

  saveExpense(): void {
    if (!this.currentExpense.title || this.currentExpense.amount <= 0) return;
    this.apiService.createExpense(this.currentExpense).subscribe(() => {
      this.closeModal();
    });
  }

  deleteExpense(id?: number): void {
    if (id && confirm('Biztosan törlöd ezt a kiadást?')) {
      this.apiService.deleteExpense(id).subscribe();
    }
  }
}
