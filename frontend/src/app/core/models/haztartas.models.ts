export type CategoryType = 'INCOME' | 'EXPENSE';
export type RecurrenceFrequency = 'MONTHLY' | 'ONETIME' | 'YEARLY' | 'BIWEEKLY';
export type Priority = 'NEEDS' | 'WANTS' | 'SAVINGS';

export interface Category {
  id?: number;
  name: string;
  type: CategoryType;
  fixed: boolean;
  icon?: string;
  color?: string;
  monthlyBudgetLimit?: number;
  priority?: Priority;
}

export interface Income {
  id?: number;
  title: string;
  amount: number;
  frequency: RecurrenceFrequency;
  categoryId?: number;
  categoryName?: string;
  categoryColor?: string;
  categoryIcon?: string;
  receivedDate: string;
  description?: string;
  recurring: boolean;
  durationMonths?: number; // 1 = egyszeri, 2, 3.. = X hónapig, undefined / null = állandó
}

export interface Expense {
  id?: number;
  title: string;
  amount: number;
  fixed: boolean;
  categoryId?: number;
  categoryName?: string;
  categoryColor?: string;
  categoryIcon?: string;
  expenseDate: string;
  dueDayOfMonth?: number;
  priority: Priority;
  description?: string;
  recurring: boolean;
}

export interface AccountBalance {
  id?: number;
  balance: number;
  bankAmount?: number;
  cashAmount?: number;
  updatedAt?: string;
  note?: string;
  projectedEndOfMonthBalance?: number;
}

export interface BalancePoint {
  label: string;
  date: string;
  actualBalance?: number;
  calculatedBalance: number;
  difference?: number;
}

export interface BalanceTrajectory {
  currentActualBalance: number;
  currentCalculatedBalance: number;
  difference: number;
  monthlyPoints: BalancePoint[];
  dailyPoints: BalancePoint[];
}

export interface SavingsGoal {
  id?: number;
  name: string;
  targetAmount: number;
  currentAmount: number;
  targetDate?: string;
  color?: string;
  icon?: string;
  notes?: string;
  progressPercentage?: number;
  remainingAmount?: number;
  remainingMonths?: number;
  requiredMonthlySavings?: number;
}

export interface CategoryBreakdown {
  categoryId: number;
  categoryName: string;
  color: string;
  icon: string;
  totalAmount: number;
  percentage: number;
  budgetLimit: number;
  overBudget: boolean;
}

export interface MonthlyTrend {
  monthName: string;
  year: number;
  month: number;
  totalIncome: number;
  totalFixedExpenses: number;
  totalVariableExpenses: number;
  totalExpenses: number;
  netSavings: number;
}

export interface CashFlowSummary {
  monthlyIncome: number;
  monthlyFixedExpenses: number;
  monthlyVariableExpenses: number;
  totalMonthlyExpenses: number;
  netMonthlySavings: number;
  savingsRatePercent: number;
  annualIncome: number;
  annualExpenses: number;
  annualSavings: number;
}

export interface FiftyThirtyTwenty {
  monthlyIncome: number;
  actualNeeds: number;
  actualNeedsPercent: number;
  targetNeeds: number;
  needsDifference: number;

  actualWants: number;
  actualWantsPercent: number;
  targetWants: number;
  wantsDifference: number;

  actualSavings: number;
  actualSavingsPercent: number;
  targetSavings: number;
  savingsDifference: number;

  evaluationSummary: string;
}

export interface EmergencyFund {
  monthlyFixedExpenses: number;
  monthlyEssentialExpenses: number;
  targetThreeMonths: number;
  targetSixMonths: number;
  targetTwelveMonths: number;
  currentSavedAmount: number;
  currentThreeMonthsProgress: number;
  currentSixMonthsProgress: number;
  currentTwelveMonthsProgress: number;
  monthlySavingsCapacity: number;
  monthsToReachThreeMonths: number;
  monthsToReachSixMonths: number;
  advice: string;
}

export interface SimulationRequest {
  incomeChangePercent: number;
  fixedExpenseChangePercent: number;
  variableExpenseChangePercent: number;
  additionalMonthlyExpense: number;
  additionalMonthlyIncome: number;
  oneTimeExpense: number;
}

export interface SimulationResult {
  baselineIncome: number;
  baselineExpenses: number;
  baselineSavings: number;
  baselineSavingsRate: number;

  simulatedIncome: number;
  simulatedExpenses: number;
  simulatedSavings: number;
  simulatedSavingsRate: number;

  incomeDifference: number;
  expenseDifference: number;
  savingsDifference: number;
  annualSavingsImpact: number;
  outcomeMessage: string;
  positiveImpact: boolean;
}

export interface DashboardSummary {
  currentBalance?: number;
  bankAmount?: number;
  cashAmount?: number;
  projectedEndOfMonthBalance?: number;
  totalMonthlyIncome: number;
  totalMonthlyFixedExpenses: number;
  totalMonthlyVariableExpenses: number;
  totalMonthlyExpenses: number;
  netMonthlySavings: number;
  savingsRatePercent: number;
  expenseCategories: CategoryBreakdown[];
  monthlyTrends: MonthlyTrend[];
  recentIncomes: Income[];
  recentExpenses: Expense[];
}
