import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HaztartasApiService } from '../../core/services/haztartas-api.service';
import { HufCurrencyPipe } from '../../shared/pipes/huf-currency.pipe';
import { Income, Category } from '../../core/models/haztartas.models';

@Component({
  selector: 'app-incomes',
  standalone: true,
  imports: [CommonModule, FormsModule, HufCurrencyPipe],
  templateUrl: './incomes.component.html',
  styleUrls: ['./incomes.component.css']
})
export class IncomesComponent implements OnInit {
  public apiService = inject(HaztartasApiService);

  public isModalOpen = signal<boolean>(false);
  public durationMode: 'ONETIME' | 'LIMITED' | 'PERMANENT' = 'PERMANENT';

  public currentIncome: Income = {
    title: '',
    amount: 0,
    frequency: 'MONTHLY',
    categoryId: undefined,
    receivedDate: new Date().toISOString().substring(0, 10),
    recurring: true,
    durationMonths: undefined
  };

  ngOnInit(): void {
    this.apiService.getIncomes().subscribe();
    this.apiService.getCategories().subscribe();
  }

  get totalIncomeAmount(): number {
    return this.apiService.incomes().reduce((sum, item) => sum + item.amount, 0);
  }

  openNewModal(): void {
    const cats = this.apiService.categories().filter(c => c.type === 'INCOME');
    this.durationMode = 'PERMANENT';
    this.currentIncome = {
      title: '',
      amount: 0,
      frequency: 'MONTHLY',
      categoryId: cats.length > 0 ? cats[0].id : undefined,
      receivedDate: new Date().toISOString().substring(0, 10),
      recurring: true,
      durationMonths: undefined
    };
    this.isModalOpen.set(true);
  }

  setDurationMode(mode: 'ONETIME' | 'LIMITED' | 'PERMANENT'): void {
    this.durationMode = mode;
    if (mode === 'ONETIME') {
      this.currentIncome.recurring = false;
      this.currentIncome.frequency = 'ONETIME';
      this.currentIncome.durationMonths = 1;
    } else if (mode === 'LIMITED') {
      this.currentIncome.recurring = true;
      this.currentIncome.frequency = 'MONTHLY';
      if (!this.currentIncome.durationMonths || this.currentIncome.durationMonths < 2) {
        this.currentIncome.durationMonths = 3; // alapértelmezett 3 hónap (pl. diákmunka / félév)
      }
    } else {
      this.currentIncome.recurring = true;
      this.currentIncome.frequency = 'MONTHLY';
      this.currentIncome.durationMonths = undefined;
    }
  }

  closeModal(): void {
    this.isModalOpen.set(false);
  }

  saveIncome(): void {
    if (!this.currentIncome.title || this.currentIncome.amount <= 0) return;

    if (this.durationMode === 'ONETIME') {
      this.currentIncome.recurring = false;
      this.currentIncome.frequency = 'ONETIME';
      this.currentIncome.durationMonths = 1;
    } else if (this.durationMode === 'LIMITED') {
      this.currentIncome.recurring = true;
      this.currentIncome.frequency = 'MONTHLY';
    } else {
      this.currentIncome.recurring = true;
      this.currentIncome.frequency = 'MONTHLY';
      this.currentIncome.durationMonths = undefined;
    }

    this.apiService.createIncome(this.currentIncome).subscribe(() => {
      this.closeModal();
    });
  }

  deleteIncome(id?: number): void {
    if (id && confirm('Biztosan törlöd ezt a bevételt?')) {
      this.apiService.deleteIncome(id).subscribe();
    }
  }
}
