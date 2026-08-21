import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, tap, catchError } from 'rxjs';
import {
  Category,
  Income,
  Expense,
  AccountBalance,
  BalancePoint,
  BalanceTrajectory,
  SavingsGoal,
  DashboardSummary,
  CashFlowSummary,
  FiftyThirtyTwenty,
  EmergencyFund,
  SimulationRequest,
  SimulationResult
} from '../models/haztartas.models';

@Injectable({
  providedIn: 'root'
})
export class HaztartasApiService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api';

  // Állapotkezelés Angular Signals-szel
  public isConnected = signal<boolean>(false);
  public isConnecting = signal<boolean>(false);
  public dashboardData = signal<DashboardSummary | null>(null);
  public accountBalance = signal<AccountBalance | null>(null);
  public trajectoryData = signal<BalanceTrajectory | null>(null);
  public categories = signal<Category[]>([]);
  public incomes = signal<Income[]>([]);
  public expenses = signal<Expense[]>([]);
  public goals = signal<SavingsGoal[]>([]);

  private fallbackBalance: AccountBalance = {
    id: 1,
    balance: 150000,
    bankAmount: 140000,
    cashAmount: 10000,
    note: 'Készpénz + Számla',
    updatedAt: new Date().toISOString()
  };

  // Alapértelmezett beépített minták (ha a backend még nincs elindítva)
  private fallbackCategories: Category[] = [
    { id: 1, name: 'Diákmunka', type: 'INCOME', fixed: false, icon: 'briefcase', color: '#10b981' },
    { id: 2, name: 'Egyetemi Ösztöndíj', type: 'INCOME', fixed: true, icon: 'book', color: '#3b82f6' },
    { id: 3, name: 'Családi támogatás / Zsebpénz', type: 'INCOME', fixed: true, icon: 'heart', color: '#06b6d4' },
    { id: 4, name: 'Egyéb Bevétel', type: 'INCOME', fixed: false, icon: 'plus-circle', color: '#8b5cf6' },
    { id: 5, name: 'Kollégium / Albérlet & Rezsi', type: 'EXPENSE', fixed: true, icon: 'home', color: '#ef4444', monthlyBudgetLimit: 65000 },
    { id: 6, name: 'Diákbérlet / Utazás (BKK, Vonat)', type: 'EXPENSE', fixed: true, icon: 'train', color: '#f97316', monthlyBudgetLimit: 12000 },
    { id: 7, name: 'Élelmiszer, Menza & Bevásárlás', type: 'EXPENSE', fixed: false, icon: 'shopping-cart', color: '#f59e0b', monthlyBudgetLimit: 60000 },
    { id: 8, name: 'Mobil & Internet előfizetés', type: 'EXPENSE', fixed: true, icon: 'wifi', color: '#6366f1', monthlyBudgetLimit: 8000 },
    { id: 9, name: 'Szórakozás, Kávé, Buli', type: 'EXPENSE', fixed: false, icon: 'coffee', color: '#a855f7', monthlyBudgetLimit: 25000 },
    { id: 10, name: 'Streaming (Spotify, Netflix)', type: 'EXPENSE', fixed: true, icon: 'tv', color: '#06b6d4', monthlyBudgetLimit: 5000 },
    { id: 11, name: 'Egyetemi jegyzetek & Eszközök', type: 'EXPENSE', fixed: false, icon: 'edit', color: '#ec4899', monthlyBudgetLimit: 10000 }
  ];

  private fallbackIncomes: Income[] = [
    { id: 1, title: 'Diákmunka fizetés', amount: 140000, frequency: 'MONTHLY', categoryId: 1, categoryName: 'Diákmunka', categoryColor: '#10b981', categoryIcon: 'briefcase', receivedDate: new Date().toISOString().substring(0, 10), recurring: true },
    { id: 2, title: 'Tanulmányi Ösztöndíj', amount: 35000, frequency: 'MONTHLY', categoryId: 2, categoryName: 'Egyetemi Ösztöndíj', categoryColor: '#3b82f6', categoryIcon: 'book', receivedDate: new Date().toISOString().substring(0, 10), recurring: true },
    { id: 3, title: 'Havi szülői támogatás', amount: 45000, frequency: 'MONTHLY', categoryId: 3, categoryName: 'Családi támogatás / Zsebpénz', categoryColor: '#06b6d4', categoryIcon: 'heart', receivedDate: new Date().toISOString().substring(0, 10), recurring: true }
  ];

  private fallbackExpenses: Expense[] = [
    { id: 1, title: 'Kollégiumi havidíj', amount: 35000, fixed: true, categoryId: 5, categoryName: 'Kollégium / Albérlet & Rezsi', categoryColor: '#ef4444', categoryIcon: 'home', expenseDate: new Date().toISOString().substring(0, 10), dueDayOfMonth: 10, priority: 'NEEDS', recurring: true },
    { id: 2, title: 'BKK országbérlet / diákbérlet', amount: 9450, fixed: true, categoryId: 6, categoryName: 'Diákbérlet / Utazás (BKK, Vonat)', categoryColor: '#f97316', categoryIcon: 'train', expenseDate: new Date().toISOString().substring(0, 10), dueDayOfMonth: 5, priority: 'NEEDS', recurring: true },
    { id: 3, title: 'Mobilnet előfizetés', amount: 4990, fixed: true, categoryId: 8, categoryName: 'Mobil & Internet előfizetés', categoryColor: '#6366f1', categoryIcon: 'wifi', expenseDate: new Date().toISOString().substring(0, 10), dueDayOfMonth: 15, priority: 'NEEDS', recurring: true },
    { id: 4, title: 'Spotify Diák előfizetés', amount: 1590, fixed: true, categoryId: 10, categoryName: 'Streaming (Spotify, Netflix)', categoryColor: '#06b6d4', categoryIcon: 'tv', expenseDate: new Date().toISOString().substring(0, 10), dueDayOfMonth: 8, priority: 'WANTS', recurring: true },
    { id: 5, title: 'Heti menza & bolti kaja', amount: 42000, fixed: false, categoryId: 7, categoryName: 'Élelmiszer, Menza & Bevásárlás', categoryColor: '#f59e0b', categoryIcon: 'shopping-cart', expenseDate: new Date().toISOString().substring(0, 10), dueDayOfMonth: 1, priority: 'NEEDS', recurring: true },
    { id: 6, title: 'Kávézás & egyetemi büfé', amount: 12000, fixed: false, categoryId: 9, categoryName: 'Szórakozás, Kávé, Buli', categoryColor: '#a855f7', categoryIcon: 'coffee', expenseDate: new Date().toISOString().substring(0, 10), dueDayOfMonth: 1, priority: 'WANTS', recurring: true }
  ];

  private fallbackGoals: SavingsGoal[] = [
    { id: 1, name: '6 Havi Vésztartalék Alap', targetAmount: 1800000, currentAmount: 750000, color: '#10b981', icon: 'shield', progressPercentage: 41.7, remainingAmount: 1050000, remainingMonths: 12, requiredMonthlySavings: 87500, notes: 'Biztonsági tartalék' },
    { id: 2, name: 'Nyári Utazás / Pihenés', targetAmount: 450000, currentAmount: 180000, color: '#3b82f6', icon: 'plane', progressPercentage: 40.0, remainingAmount: 270000, remainingMonths: 6, requiredMonthlySavings: 45000, notes: 'Nyaralási büdzsé' }
  ];

  constructor() {
    this.loadInitialData();
  }

  public loadInitialData(): void {
    this.isConnecting.set(true);
    this.getDashboardSummary().subscribe();
    this.getAccountBalance().subscribe();
    this.getBalanceTrajectory().subscribe();
    this.getCategories().subscribe();
    this.getIncomes().subscribe();
    this.getExpenses().subscribe();
  }

  // --- VAGYON / SZÁMLAEGYENLEG & IDŐVONAL ---
  getAccountBalance(): Observable<AccountBalance> {
    return this.http.get<AccountBalance>(`${this.baseUrl}/account/balance`).pipe(
      tap(data => this.accountBalance.set(data)),
      catchError(() => {
        const cashFlow = this.calculateLocalCashFlow();
        const projected = this.fallbackBalance.balance + cashFlow.netMonthlySavings;
        const res: AccountBalance = { ...this.fallbackBalance, projectedEndOfMonthBalance: projected };
        this.accountBalance.set(res);
        return of(res);
      })
    );
  }

  getBalanceTrajectory(): Observable<BalanceTrajectory> {
    return this.http.get<BalanceTrajectory>(`${this.baseUrl}/account/trajectory`).pipe(
      tap(data => this.trajectoryData.set(data)),
      catchError(() => {
        const cf = this.calculateLocalCashFlow();
        const bal = this.fallbackBalance.balance;
        const months = ['Már', 'Ápr', 'Máj', 'Jún', 'Júl', 'Aug'];
        const monthlyPoints: BalancePoint[] = months.map((m, idx) => ({
          label: m,
          date: `2026-0${idx + 3}-01`,
          actualBalance: bal - (5 - idx) * 15000 + (idx % 2 === 0 ? 5000 : -3000),
          calculatedBalance: bal - (5 - idx) * 20000
        }));
        const trajectory: BalanceTrajectory = {
          currentActualBalance: bal,
          currentCalculatedBalance: bal + cf.netMonthlySavings,
          difference: -cf.netMonthlySavings,
          monthlyPoints,
          dailyPoints: []
        };
        this.trajectoryData.set(trajectory);
        return of(trajectory);
      })
    );
  }

  updateAccountBalance(dto: AccountBalance): Observable<AccountBalance> {
    return this.http.put<AccountBalance>(`${this.baseUrl}/account/balance`, dto).pipe(
      tap(updated => {
        this.accountBalance.set(updated);
        this.getDashboardSummary().subscribe();
        this.getBalanceTrajectory().subscribe();
      }),
      catchError(() => {
        this.fallbackBalance = { ...this.fallbackBalance, ...dto, updatedAt: new Date().toISOString() };
        const cashFlow = this.calculateLocalCashFlow();
        const projected = this.fallbackBalance.balance + cashFlow.netMonthlySavings;
        const res: AccountBalance = { ...this.fallbackBalance, projectedEndOfMonthBalance: projected };
        this.accountBalance.set(res);
        this.refreshLocalData();
        return of(res);
      })
    );
  }

  // --- DASHBOARD ---
  getDashboardSummary(): Observable<DashboardSummary> {
    return this.http.get<DashboardSummary>(`${this.baseUrl}/dashboard`).pipe(
      tap(data => {
        this.isConnected.set(true);
        this.isConnecting.set(false);
        this.dashboardData.set(data);
      }),
      catchError(() => {
        this.isConnected.set(false);
        this.isConnecting.set(false);
        const fallback = this.calculateLocalDashboard();
        this.dashboardData.set(fallback);
        return of(fallback);
      })
    );
  }

  // --- KATEGÓRIÁK ---
  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.baseUrl}/categories`).pipe(
      tap(data => this.categories.set(data)),
      catchError(() => {
        this.categories.set(this.fallbackCategories);
        return of(this.fallbackCategories);
      })
    );
  }

  createCategory(category: Category): Observable<Category> {
    return this.http.post<Category>(`${this.baseUrl}/categories`, category).pipe(
      tap(() => this.loadInitialData()),
      catchError(() => {
        const newCat = { ...category, id: Date.now() };
        this.fallbackCategories.push(newCat);
        this.categories.set([...this.fallbackCategories]);
        this.refreshLocalData();
        return of(newCat);
      })
    );
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/categories/${id}`).pipe(
      tap(() => this.loadInitialData()),
      catchError(() => {
        this.fallbackCategories = this.fallbackCategories.filter(c => c.id !== id);
        this.categories.set([...this.fallbackCategories]);
        this.refreshLocalData();
        return of(void 0);
      })
    );
  }

  // --- BEVÉTELEK ---
  getIncomes(): Observable<Income[]> {
    return this.http.get<Income[]>(`${this.baseUrl}/incomes`).pipe(
      tap(data => this.incomes.set(data)),
      catchError(() => {
        this.incomes.set(this.fallbackIncomes);
        return of(this.fallbackIncomes);
      })
    );
  }

  createIncome(income: Income): Observable<Income> {
    return this.http.post<Income>(`${this.baseUrl}/incomes`, income).pipe(
      tap(() => this.loadInitialData()),
      catchError(() => {
        const cat = this.fallbackCategories.find(c => c.id === income.categoryId);
        const newInc: Income = {
          ...income,
          id: Date.now(),
          categoryName: cat?.name,
          categoryColor: cat?.color,
          categoryIcon: cat?.icon
        };
        this.fallbackIncomes.unshift(newInc);
        this.incomes.set([...this.fallbackIncomes]);
        this.refreshLocalData();
        return of(newInc);
      })
    );
  }

  deleteIncome(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/incomes/${id}`).pipe(
      tap(() => this.loadInitialData()),
      catchError(() => {
        this.fallbackIncomes = this.fallbackIncomes.filter(i => i.id !== id);
        this.incomes.set([...this.fallbackIncomes]);
        this.refreshLocalData();
        return of(void 0);
      })
    );
  }

  // --- KIADÁSOK ---
  getExpenses(): Observable<Expense[]> {
    return this.http.get<Expense[]>(`${this.baseUrl}/expenses`).pipe(
      tap(data => this.expenses.set(data)),
      catchError(() => {
        this.expenses.set(this.fallbackExpenses);
        return of(this.fallbackExpenses);
      })
    );
  }

  createExpense(expense: Expense): Observable<Expense> {
    return this.http.post<Expense>(`${this.baseUrl}/expenses`, expense).pipe(
      tap(() => this.loadInitialData()),
      catchError(() => {
        const cat = this.fallbackCategories.find(c => c.id === expense.categoryId);
        const newExp: Expense = {
          ...expense,
          id: Date.now(),
          categoryName: cat?.name,
          categoryColor: cat?.color,
          categoryIcon: cat?.icon
        };
        this.fallbackExpenses.unshift(newExp);
        this.expenses.set([...this.fallbackExpenses]);
        this.refreshLocalData();
        return of(newExp);
      })
    );
  }

  deleteExpense(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/expenses/${id}`).pipe(
      tap(() => this.loadInitialData()),
      catchError(() => {
        this.fallbackExpenses = this.fallbackExpenses.filter(e => e.id !== id);
        this.expenses.set([...this.fallbackExpenses]);
        this.refreshLocalData();
        return of(void 0);
      })
    );
  }

  // --- CÉLOK ---
  getGoals(): Observable<SavingsGoal[]> {
    return this.http.get<SavingsGoal[]>(`${this.baseUrl}/goals`).pipe(
      tap(data => this.goals.set(data)),
      catchError(() => {
        this.goals.set(this.fallbackGoals);
        return of(this.fallbackGoals);
      })
    );
  }

  createGoal(goal: SavingsGoal): Observable<SavingsGoal> {
    return this.http.post<SavingsGoal>(`${this.baseUrl}/goals`, goal).pipe(
      tap(() => this.loadInitialData()),
      catchError(() => {
        const progress = Math.min(100, Math.round(((goal.currentAmount || 0) / goal.targetAmount) * 100));
        const newGoal: SavingsGoal = {
          ...goal,
          id: Date.now(),
          progressPercentage: progress,
          remainingAmount: Math.max(0, goal.targetAmount - (goal.currentAmount || 0))
        };
        this.fallbackGoals.push(newGoal);
        this.goals.set([...this.fallbackGoals]);
        this.refreshLocalData();
        return of(newGoal);
      })
    );
  }

  addDeposit(id: number, amount: number): Observable<SavingsGoal> {
    return this.http.post<SavingsGoal>(`${this.baseUrl}/goals/${id}/deposit?amount=${amount}`, {}).pipe(
      tap(() => this.loadInitialData()),
      catchError(() => {
        const target = this.fallbackGoals.find(g => g.id === id);
        if (target) {
          target.currentAmount = (target.currentAmount || 0) + amount;
          target.progressPercentage = Math.min(100, Math.round((target.currentAmount / target.targetAmount) * 100));
          target.remainingAmount = Math.max(0, target.targetAmount - target.currentAmount);
        }
        this.goals.set([...this.fallbackGoals]);
        this.refreshLocalData();
        return of(target!);
      })
    );
  }

  deleteGoal(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/goals/${id}`).pipe(
      tap(() => this.loadInitialData()),
      catchError(() => {
        this.fallbackGoals = this.fallbackGoals.filter(g => g.id !== id);
        this.goals.set([...this.fallbackGoals]);
        this.refreshLocalData();
        return of(void 0);
      })
    );
  }

  // --- KALKULÁCIÓK ---
  getCashFlow(): Observable<CashFlowSummary> {
    return this.http.get<CashFlowSummary>(`${this.baseUrl}/calculations/cashflow`).pipe(
      catchError(() => of(this.calculateLocalCashFlow()))
    );
  }

  get503020(): Observable<FiftyThirtyTwenty> {
    return this.http.get<FiftyThirtyTwenty>(`${this.baseUrl}/calculations/50-30-20`).pipe(
      catchError(() => of(this.calculateLocal503020()))
    );
  }

  getEmergencyFund(): Observable<EmergencyFund> {
    return this.http.get<EmergencyFund>(`${this.baseUrl}/calculations/emergency-fund`).pipe(
      catchError(() => of(this.calculateLocalEmergencyFund()))
    );
  }

  runSimulation(req: SimulationRequest): Observable<SimulationResult> {
    return this.http.post<SimulationResult>(`${this.baseUrl}/calculations/simulate`, req).pipe(
      catchError(() => of(this.calculateLocalSimulation(req)))
    );
  }

  // --- HELYI KALKULÁCIÓS MOTOR (FALLBACK) ---
  private refreshLocalData(): void {
    const summary = this.calculateLocalDashboard();
    this.dashboardData.set(summary);
  }

  private calculateLocalCashFlow(): CashFlowSummary {
    const inc = this.fallbackIncomes.reduce((s, i) => s + i.amount, 0);
    const fix = this.fallbackExpenses.filter(e => e.fixed).reduce((s, e) => s + e.amount, 0);
    const varExp = this.fallbackExpenses.filter(e => !e.fixed).reduce((s, e) => s + e.amount, 0);
    const totalExp = fix + varExp;
    const net = inc - totalExp;
    const rate = inc > 0 ? Math.round((net / inc) * 1000) / 10 : 0;

    return {
      monthlyIncome: inc,
      monthlyFixedExpenses: fix,
      monthlyVariableExpenses: varExp,
      totalMonthlyExpenses: totalExp,
      netMonthlySavings: net,
      savingsRatePercent: rate,
      annualIncome: inc * 12,
      annualExpenses: totalExp * 12,
      annualSavings: net * 12
    };
  }

  private calculateLocal503020(): FiftyThirtyTwenty {
    const cf = this.calculateLocalCashFlow();
    const inc = cf.monthlyIncome;
    const needs = this.fallbackExpenses.filter(e => e.priority === 'NEEDS' || e.fixed).reduce((s, e) => s + e.amount, 0);
    const wants = this.fallbackExpenses.filter(e => e.priority === 'WANTS' && !e.fixed).reduce((s, e) => s + e.amount, 0);
    const savings = Math.max(0, cf.netMonthlySavings);

    const targetNeeds = Math.round(inc * 0.5);
    const targetWants = Math.round(inc * 0.3);
    const targetSavings = Math.round(inc * 0.2);

    return {
      monthlyIncome: inc,
      actualNeeds: needs,
      actualNeedsPercent: inc > 0 ? Math.round((needs / inc) * 1000) / 10 : 0,
      targetNeeds: targetNeeds,
      needsDifference: needs - targetNeeds,

      actualWants: wants,
      actualWantsPercent: inc > 0 ? Math.round((wants / inc) * 1000) / 10 : 0,
      targetWants: targetWants,
      wantsDifference: wants - targetWants,

      actualSavings: savings,
      actualSavingsPercent: inc > 0 ? Math.round((savings / inc) * 1000) / 10 : 0,
      targetSavings: targetSavings,
      savingsDifference: savings - targetSavings,

      evaluationSummary: 'Kiváló pénzügyi egyensúly! A háztartás stabilan működik, a megtakarítási ráta magas.'
    };
  }

  private calculateLocalEmergencyFund(): EmergencyFund {
    const cf = this.calculateLocalCashFlow();
    const essential = cf.monthlyFixedExpenses + (cf.monthlyVariableExpenses * 0.6);
    const saved = this.fallbackGoals.reduce((s, g) => s + (g.currentAmount || 0), 0);

    const t3 = essential * 3;
    const t6 = essential * 6;
    const t12 = essential * 12;

    const capacity = Math.max(0, cf.netMonthlySavings);

    return {
      monthlyFixedExpenses: cf.monthlyFixedExpenses,
      monthlyEssentialExpenses: essential,
      targetThreeMonths: t3,
      targetSixMonths: t6,
      targetTwelveMonths: t12,
      currentSavedAmount: saved,
      currentThreeMonthsProgress: t3 > 0 ? Math.min(100, Math.round((saved / t3) * 100)) : 0,
      currentSixMonthsProgress: t6 > 0 ? Math.min(100, Math.round((saved / t6) * 100)) : 0,
      currentTwelveMonthsProgress: t12 > 0 ? Math.min(100, Math.round((saved / t12) * 100)) : 0,
      monthlySavingsCapacity: capacity,
      monthsToReachThreeMonths: capacity > 0 ? Math.ceil(Math.max(0, t3 - saved) / capacity) : 0,
      monthsToReachSixMonths: capacity > 0 ? Math.ceil(Math.max(0, t6 - saved) / capacity) : 0,
      advice: 'Alapvető 3 havi vésztartalék meglévő, a cél a stabil 6 havi biztonsági sáv elérése.'
    };
  }

  private calculateLocalSimulation(req: SimulationRequest): SimulationResult {
    const cf = this.calculateLocalCashFlow();
    const baseInc = cf.monthlyIncome;
    const baseExp = cf.totalMonthlyExpenses;
    const baseSav = cf.netMonthlySavings;

    const simInc = (baseInc * (1 + req.incomeChangePercent / 100)) + (req.additionalMonthlyIncome || 0);
    const simFix = cf.monthlyFixedExpenses * (1 + req.fixedExpenseChangePercent / 100);
    const simVar = cf.monthlyVariableExpenses * (1 + req.variableExpenseChangePercent / 100);
    const simExp = simFix + simVar + (req.additionalMonthlyExpense || 0);
    const simSav = simInc - simExp;

    const simRate = simInc > 0 ? Math.round((simSav / simInc) * 1000) / 10 : 0;
    const diffSav = simSav - baseSav;
    const annualImpact = (diffSav * 12) - (req.oneTimeExpense || 0);

    const isPositive = annualImpact >= 0;

    return {
      baselineIncome: baseInc,
      baselineExpenses: baseExp,
      baselineSavings: baseSav,
      baselineSavingsRate: cf.savingsRatePercent,

      simulatedIncome: Math.round(simInc),
      simulatedExpenses: Math.round(simExp),
      simulatedSavings: Math.round(simSav),
      simulatedSavingsRate: simRate,

      incomeDifference: Math.round(simInc - baseInc),
      expenseDifference: Math.round(simExp - baseExp),
      savingsDifference: Math.round(diffSav),
      annualSavingsImpact: Math.round(annualImpact),
      outcomeMessage: isPositive
        ? `Pozitív hatás! Éves szinten +${annualImpact.toLocaleString('hu-HU')} Ft többlet megtakarítás érhető el.`
        : `Figyelem! A szimulált kiadásokkal éves szinten ${annualImpact.toLocaleString('hu-HU')} Ft-tal csökken a mérleg.`,
      positiveImpact: isPositive
    };
  }

  private calculateLocalDashboard(): DashboardSummary {
    const cf = this.calculateLocalCashFlow();
    const catMap = new Map<string, { id: number; color: string; icon: string; amount: number; limit: number }>();

    for (const exp of this.fallbackExpenses) {
      const cat = this.fallbackCategories.find(c => c.id === exp.categoryId);
      const name = cat?.name || 'Egyéb';
      const prev = catMap.get(name) || {
        id: exp.categoryId || 0,
        color: cat?.color || '#64748b',
        icon: cat?.icon || 'wallet',
        amount: 0,
        limit: cat?.monthlyBudgetLimit || 0
      };
      prev.amount += exp.amount;
      catMap.set(name, prev);
    }

    const categories: any[] = [];
    catMap.forEach((val, name) => {
      const pct = cf.totalMonthlyExpenses > 0 ? Math.round((val.amount / cf.totalMonthlyExpenses) * 1000) / 10 : 0;
      categories.push({
        categoryId: val.id,
        categoryName: name,
        color: val.color,
        icon: val.icon,
        totalAmount: val.amount,
        percentage: pct,
        budgetLimit: val.limit,
        overBudget: val.limit > 0 && val.amount > val.limit
      });
    });

    const months = ['Már', 'Ápr', 'Máj', 'Jún', 'Júl', 'Aug'];
    const trends: any[] = months.map((m, idx) => ({
      monthName: m,
      year: 2026,
      month: idx + 3,
      totalIncome: cf.monthlyIncome,
      totalFixedExpenses: cf.monthlyFixedExpenses,
      totalVariableExpenses: cf.monthlyVariableExpenses,
      totalExpenses: cf.totalMonthlyExpenses,
      netSavings: cf.netMonthlySavings
    }));

    return {
      currentBalance: this.fallbackBalance.balance,
      bankAmount: this.fallbackBalance.bankAmount,
      cashAmount: this.fallbackBalance.cashAmount,
      projectedEndOfMonthBalance: this.fallbackBalance.balance + cf.netMonthlySavings,
      totalMonthlyIncome: cf.monthlyIncome,
      totalMonthlyFixedExpenses: cf.monthlyFixedExpenses,
      totalMonthlyVariableExpenses: cf.monthlyVariableExpenses,
      totalMonthlyExpenses: cf.totalMonthlyExpenses,
      netMonthlySavings: cf.netMonthlySavings,
      savingsRatePercent: cf.savingsRatePercent,
      expenseCategories: categories,
      monthlyTrends: trends,
      recentIncomes: this.fallbackIncomes,
      recentExpenses: this.fallbackExpenses
    };
  }
}
