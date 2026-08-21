import { Component, OnInit, OnDestroy, ElementRef, ViewChild, inject, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { HaztartasApiService } from '../../core/services/haztartas-api.service';
import { HufCurrencyPipe } from '../../shared/pipes/huf-currency.pipe';
import { Income, Expense, Category, AccountBalance } from '../../core/models/haztartas.models';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, HufCurrencyPipe],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {
  public apiService = inject(HaztartasApiService);

  @ViewChild('donutCanvas') donutCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('trajectoryCanvas') trajectoryCanvas!: ElementRef<HTMLCanvasElement>;
  private donutChart?: Chart;
  private trajectoryChart?: Chart;

  // Modals state
  public isIncomeModalOpen = signal<boolean>(false);
  public isExpenseModalOpen = signal<boolean>(false);
  public isBalanceModalOpen = signal<boolean>(false);

  // Form models
  public newIncome: Income = {
    title: '',
    amount: 0,
    frequency: 'MONTHLY',
    categoryId: undefined,
    receivedDate: new Date().toISOString().substring(0, 10),
    recurring: true
  };

  public newExpense: Expense = {
    title: '',
    amount: 0,
    fixed: true,
    categoryId: undefined,
    expenseDate: new Date().toISOString().substring(0, 10),
    dueDayOfMonth: 10,
    priority: 'NEEDS',
    recurring: true
  };

  public editBalance: AccountBalance = {
    balance: 0,
    bankAmount: 0,
    cashAmount: 0,
    note: ''
  };

  constructor() {
    effect(() => {
      const data = this.apiService.dashboardData();
      const traj = this.apiService.trajectoryData();
      if (data || traj) {
        setTimeout(() => this.renderCharts(), 50);
      }
    });
  }

  ngOnInit(): void {
    this.apiService.loadInitialData();
  }

  ngOnDestroy(): void {
    this.donutChart?.destroy();
    this.trajectoryChart?.destroy();
  }

  renderCharts(): void {
    const data = this.apiService.dashboardData();
    const traj = this.apiService.trajectoryData();
    const isDark = document.documentElement.getAttribute('data-theme') !== 'light';

    // 1. Kördiagram (Kiadások megoszlása)
    if (data && this.donutCanvas && this.donutCanvas.nativeElement) {
      this.donutChart?.destroy();

      const labels = data.expenseCategories.map(c => c.categoryName);
      const amounts = data.expenseCategories.map(c => c.totalAmount);
      
      const darkPalette = ['#f5f5f5', '#a3a3a3', '#737373', '#525252', '#3a3a3a'];
      const lightPalette = ['#111111', '#444440', '#777770', '#aaaaa2', '#222222'];
      const palette = isDark ? darkPalette : lightPalette;
      const colors = data.expenseCategories.map((c, i) => palette[i % palette.length]);

      if (amounts.length > 0) {
        this.donutChart = new Chart(this.donutCanvas.nativeElement, {
          type: 'doughnut',
          data: {
            labels: labels,
            datasets: [{
              data: amounts,
              backgroundColor: colors,
              borderWidth: 2,
              borderColor: isDark ? '#141414' : '#ffffff',
              hoverOffset: 4
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '76%',
            plugins: {
              legend: {
                position: 'right',
                labels: {
                  color: isDark ? '#f5f5f5' : '#111111',
                  font: { family: 'Plus Jakarta Sans', size: 12, weight: 'bold' },
                  padding: 12,
                  boxWidth: 8,
                  boxHeight: 8,
                  usePointStyle: true
                }
              },
              tooltip: {
                backgroundColor: isDark ? '#1c1c1c' : '#111111',
                titleColor: '#ffffff',
                bodyColor: '#eaeae5',
                padding: 10,
                displayColors: false,
                callbacks: {
                  label: (context) => {
                    const val = context.parsed;
                    return ` ${context.label}: ${val.toLocaleString('hu-HU')} Ft`;
                  }
                }
              }
            }
          }
        });
      }
    }

    // 2. Kalkulált vs. Valós Egyenleg Idővonal Diagram (Görbe a felhasználó rajza alapján)
    if (this.trajectoryCanvas && this.trajectoryCanvas.nativeElement && traj && traj.monthlyPoints.length > 0) {
      this.trajectoryChart?.destroy();

      const labels = traj.monthlyPoints.map(p => p.label);
      const calculatedData: (number | null)[] = traj.monthlyPoints.map(p => p.calculatedBalance != null ? p.calculatedBalance : null);
      const actualData: (number | null)[] = traj.monthlyPoints.map(p => p.actualBalance != null ? p.actualBalance : null);

      this.trajectoryChart = new Chart(this.trajectoryCanvas.nativeElement, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [
            {
              label: 'Kalkulált egyenleg',
              data: calculatedData,
              borderColor: '#a855f7', // Lila görbe
              backgroundColor: 'rgba(168, 85, 247, 0.08)',
              borderWidth: 3,
              tension: 0.35,
              fill: false,
              spanGaps: false,
              pointBackgroundColor: '#a855f7',
              pointBorderColor: isDark ? '#141414' : '#ffffff',
              pointBorderWidth: 2,
              pointRadius: 4,
              pointHoverRadius: 6
            },
            {
              label: 'Valós egyenleg',
              data: actualData,
              borderColor: '#22c55e', // Zöld görbe
              backgroundColor: 'rgba(34, 197, 94, 0.1)',
              borderWidth: 3,
              tension: 0.35,
              fill: false,
              spanGaps: false,
              pointBackgroundColor: '#22c55e',
              pointBorderColor: isDark ? '#141414' : '#ffffff',
              pointBorderWidth: 2,
              pointRadius: 5,
              pointHoverRadius: 7
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          interaction: {
            mode: 'index',
            intersect: false
          },
          plugins: {
            legend: {
              position: 'top',
              align: 'end',
              labels: {
                color: isDark ? '#f5f5f5' : '#111111',
                font: { family: 'Plus Jakarta Sans', size: 12, weight: 'bold' },
                padding: 16,
                usePointStyle: true,
                boxWidth: 10,
                boxHeight: 10
              }
            },
            tooltip: {
              backgroundColor: isDark ? '#1a1a1a' : '#111111',
              titleColor: '#ffffff',
              bodyColor: '#eaeae5',
              padding: 12,
              cornerRadius: 4,
              borderColor: isDark ? '#333333' : '#444444',
              borderWidth: 1,
              callbacks: {
                label: (context) => {
                  const rawVal = context.raw;
                  if (rawVal === null || rawVal === undefined) {
                    return ` ${context.dataset.label}: Jövőbeli időpont (még nincs adat)`;
                  }
                  const val = Number(rawVal);
                  return ` ${context.dataset.label}: ${val.toLocaleString('hu-HU')} Ft`;
                },
                afterBody: (items) => {
                  const calcItem = items.find(i => i.datasetIndex === 0);
                  const actItem = items.find(i => i.datasetIndex === 1);
                  if (calcItem && actItem && actItem.raw !== null && actItem.raw !== undefined) {
                    const calc = Number(calcItem.raw);
                    const act = Number(actItem.raw);
                    const diff = act - calc;
                    const diffStr = diff >= 0 ? `+${diff.toLocaleString('hu-HU')} Ft` : `${diff.toLocaleString('hu-HU')} Ft`;
                    return [` Eltérés (Valós - Kalkulált): ${diffStr}`];
                  }
                  return [];
                }
              }
            }
          },
          scales: {
            x: {
              grid: {
                color: isDark ? 'rgba(255, 255, 255, 0.05)' : 'rgba(0, 0, 0, 0.05)'
              },
              ticks: {
                color: isDark ? '#a3a3a3' : '#555550',
                font: { family: 'Plus Jakarta Sans', size: 11, weight: 'bold' }
              },
              title: {
                display: true,
                text: 'Idő',
                color: isDark ? '#737373' : '#888882',
                font: { family: 'Plus Jakarta Sans', size: 11, weight: 'bold' }
              }
            },
            y: {
              grid: {
                color: isDark ? 'rgba(255, 255, 255, 0.05)' : 'rgba(0, 0, 0, 0.05)'
              },
              ticks: {
                color: isDark ? '#a3a3a3' : '#555550',
                font: { family: 'JetBrains Mono', size: 11 },
                callback: (val) => `${Number(val).toLocaleString('hu-HU')} Ft`
              },
              title: {
                display: true,
                text: 'Pénz (Ft)',
                color: isDark ? '#737373' : '#888882',
                font: { family: 'Plus Jakarta Sans', size: 11, weight: 'bold' }
              }
            }
          }
        }
      });
    }
  }

  // --- VAGYON / EGYENLEG MODAL ---
  openBalanceModal(): void {
    const acc = this.apiService.accountBalance();
    const dash = this.apiService.dashboardData();
    const currentTotal = acc?.balance ?? dash?.currentBalance ?? 0;
    const currentBank = acc?.bankAmount ?? dash?.bankAmount ?? currentTotal;
    const currentCash = acc?.cashAmount ?? dash?.cashAmount ?? 0;

    this.editBalance = {
      balance: currentTotal,
      bankAmount: currentBank,
      cashAmount: currentCash,
      note: 'Frissítve'
    };
    this.isBalanceModalOpen.set(true);
  }

  closeBalanceModal(): void {
    this.isBalanceModalOpen.set(false);
  }

  onSubBalanceChange(): void {
    this.editBalance.balance = (this.editBalance.bankAmount || 0) + (this.editBalance.cashAmount || 0);
  }

  saveBalance(): void {
    this.apiService.updateAccountBalance(this.editBalance).subscribe(() => {
      this.closeBalanceModal();
    });
  }

  // Income duration mode
  public incomeDurationMode: 'ONETIME' | 'LIMITED' | 'PERMANENT' = 'PERMANENT';

  // --- BEVÉTEL MODAL ---
  openIncomeModal(): void {
    const cats = this.apiService.categories().filter(c => c.type === 'INCOME');
    this.incomeDurationMode = 'PERMANENT';
    this.newIncome = {
      title: '',
      amount: 0,
      frequency: 'MONTHLY',
      categoryId: cats.length > 0 ? cats[0].id : undefined,
      receivedDate: new Date().toISOString().substring(0, 10),
      recurring: true,
      durationMonths: undefined
    };
    this.isIncomeModalOpen.set(true);
  }

  setIncomeDurationMode(mode: 'ONETIME' | 'LIMITED' | 'PERMANENT'): void {
    this.incomeDurationMode = mode;
    if (mode === 'ONETIME') {
      this.newIncome.recurring = false;
      this.newIncome.frequency = 'ONETIME';
      this.newIncome.durationMonths = 1;
    } else if (mode === 'LIMITED') {
      this.newIncome.recurring = true;
      this.newIncome.frequency = 'MONTHLY';
      if (!this.newIncome.durationMonths || this.newIncome.durationMonths < 2) {
        this.newIncome.durationMonths = 3;
      }
    } else {
      this.newIncome.recurring = true;
      this.newIncome.frequency = 'MONTHLY';
      this.newIncome.durationMonths = undefined;
    }
  }

  closeIncomeModal(): void {
    this.isIncomeModalOpen.set(false);
  }

  saveIncome(): void {
    if (!this.newIncome.title || this.newIncome.amount <= 0) return;

    if (this.incomeDurationMode === 'ONETIME') {
      this.newIncome.recurring = false;
      this.newIncome.frequency = 'ONETIME';
      this.newIncome.durationMonths = 1;
    } else if (this.incomeDurationMode === 'LIMITED') {
      this.newIncome.recurring = true;
      this.newIncome.frequency = 'MONTHLY';
    } else {
      this.newIncome.recurring = true;
      this.newIncome.frequency = 'MONTHLY';
      this.newIncome.durationMonths = undefined;
    }

    this.apiService.createIncome(this.newIncome).subscribe(() => {
      this.closeIncomeModal();
      this.apiService.getBalanceTrajectory().subscribe();
    });
  }

  // --- KIADÁS MODAL ---
  openExpenseModal(): void {
    const cats = this.apiService.categories().filter(c => c.type === 'EXPENSE');
    this.newExpense = {
      title: '',
      amount: 0,
      fixed: true,
      categoryId: cats.length > 0 ? cats[0].id : undefined,
      expenseDate: new Date().toISOString().substring(0, 10),
      dueDayOfMonth: 10,
      priority: 'NEEDS',
      recurring: true
    };
    this.isExpenseModalOpen.set(true);
  }

  closeExpenseModal(): void {
    this.isExpenseModalOpen.set(false);
  }

  saveExpense(): void {
    if (!this.newExpense.title || this.newExpense.amount <= 0) return;
    this.apiService.createExpense(this.newExpense).subscribe(() => {
      this.closeExpenseModal();
    });
  }

  deleteIncome(id?: number): void {
    if (id && confirm('Biztosan törlöd ezt a bevételt?')) {
      this.apiService.deleteIncome(id).subscribe();
    }
  }

  deleteExpense(id?: number): void {
    if (id && confirm('Biztosan törlöd ezt a kiadást?')) {
      this.apiService.deleteExpense(id).subscribe();
    }
  }
}
