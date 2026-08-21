import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HaztartasApiService } from '../../core/services/haztartas-api.service';
import { HufCurrencyPipe } from '../../shared/pipes/huf-currency.pipe';
import { CashFlowSummary } from '../../core/models/haztartas.models';

@Component({
  selector: 'app-calculator',
  standalone: true,
  imports: [CommonModule, FormsModule, HufCurrencyPipe],
  templateUrl: './calculator.component.html',
  styleUrls: ['./calculator.component.css']
})
export class CalculatorComponent implements OnInit {
  public apiService = inject(HaztartasApiService);

  public cashFlow = signal<CashFlowSummary | null>(null);

  // Napi keret kalkulátor számítások
  public remainingDaysInMonth: number = 15;
  public totalDaysInMonth: number = 30;
  public currentDayOfMonth: number = 15;

  // Egyéni gyors tervező mezők (egyetemista zsebpénz & költség kalkulációhoz)
  public plannedIncome: number = 200000;
  public plannedFixedCost: number = 205000;

  ngOnInit(): void {
    this.calculateMonthDays();
    this.loadCalculations();
  }

  calculateMonthDays(): void {
    const today = new Date();
    this.currentDayOfMonth = today.getDate();
    const lastDayOfMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0).getDate();
    this.totalDaysInMonth = lastDayOfMonth;
    this.remainingDaysInMonth = Math.max(1, lastDayOfMonth - today.getDate() + 1);
  }

  loadCalculations(): void {
    this.apiService.getAccountBalance().subscribe();
    this.apiService.getCashFlow().subscribe(res => {
      this.cashFlow.set(res);
      if (res.monthlyIncome > 0) {
        this.plannedIncome = res.monthlyIncome;
        this.plannedFixedCost = res.monthlyFixedExpenses;
      }
    });
  }

  // Tényleges megmaradó napi keret a jelenlegi valós vagyon alapján
  get dailyBudgetFromWealth(): number {
    const bal = this.apiService.accountBalance()?.balance ?? 0;
    if (bal <= 0) return 0;
    return Math.floor(bal / this.remainingDaysInMonth);
  }

  // Tényleges megmaradó napi keret a havi egyenleg alapján
  get dailyBudgetFromSavings(): number {
    const cf = this.cashFlow();
    if (!cf) return 0;
    const remainingMoney = cf.netMonthlySavings;
    if (remainingMoney <= 0) return 0;
    return Math.floor(remainingMoney / this.remainingDaysInMonth);
  }

  // Tervezett számítások
  get plannedDiscretionary(): number {
    return this.plannedIncome - this.plannedFixedCost;
  }

  get plannedDailyAllowance(): number {
    if (this.plannedDiscretionary <= 0) return 0;
    return Math.floor(this.plannedDiscretionary / this.totalDaysInMonth);
  }
}
