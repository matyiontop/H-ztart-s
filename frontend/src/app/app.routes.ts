import { Routes } from '@angular/router';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { CalculatorComponent } from './features/calculator/calculator.component';
import { SimulatorComponent } from './features/simulator/simulator.component';
import { IncomesComponent } from './features/incomes/incomes.component';
import { ExpensesComponent } from './features/expenses/expenses.component';

export const routes: Routes = [
  { path: '', component: DashboardComponent, title: 'Háztartás - Áttekintés' },
  { path: 'calculator', component: CalculatorComponent, title: 'Háztartás - Havi & Napi Keret Kalkulátor' },
  { path: 'simulator', component: SimulatorComponent, title: 'Háztartás - "Mi lenne, ha?" Szimulátor' },
  { path: 'incomes', component: IncomesComponent, title: 'Háztartás - Bevételek' },
  { path: 'expenses', component: ExpensesComponent, title: 'Háztartás - Kiadások' },
  { path: '**', redirectTo: '' }
];
