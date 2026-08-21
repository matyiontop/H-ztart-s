import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HaztartasApiService } from '../../core/services/haztartas-api.service';
import { HufCurrencyPipe } from '../../shared/pipes/huf-currency.pipe';
import { SimulationRequest, SimulationResult } from '../../core/models/haztartas.models';

@Component({
  selector: 'app-simulator',
  standalone: true,
  imports: [CommonModule, FormsModule, HufCurrencyPipe],
  templateUrl: './simulator.component.html',
  styleUrls: ['./simulator.component.css']
})
export class SimulatorComponent implements OnInit {
  private apiService = inject(HaztartasApiService);

  public simulationRequest: SimulationRequest = {
    incomeChangePercent: 0,
    fixedExpenseChangePercent: 0,
    variableExpenseChangePercent: 0,
    additionalMonthlyExpense: 0,
    additionalMonthlyIncome: 0,
    oneTimeExpense: 0
  };

  public simulationResult = signal<SimulationResult | null>(null);

  // Előre beállított forgatókönyv sablonok
  public presets = [
    {
      name: '⚡ Rezsiár Emelkedés (+20%)',
      req: { incomeChangePercent: 0, fixedExpenseChangePercent: 20, variableExpenseChangePercent: 0, additionalMonthlyExpense: 0, additionalMonthlyIncome: 0, oneTimeExpense: 0 }
    },
    {
      name: '📈 Fizetésemelés (+15%)',
      req: { incomeChangePercent: 15, fixedExpenseChangePercent: 0, variableExpenseChangePercent: 0, additionalMonthlyExpense: 0, additionalMonthlyIncome: 0, oneTimeExpense: 0 }
    },
    {
      name: '🛒 Spórolás (-15% Változó Költség)',
      req: { incomeChangePercent: 0, fixedExpenseChangePercent: 0, variableExpenseChangePercent: -15, additionalMonthlyExpense: 0, additionalMonthlyIncome: 0, oneTimeExpense: 0 }
    },
    {
      name: '🚗 Új Autóhitel Törlesztő (60 000 Ft/hó)',
      req: { incomeChangePercent: 0, fixedExpenseChangePercent: 0, variableExpenseChangePercent: 0, additionalMonthlyExpense: 60000, additionalMonthlyIncome: 0, oneTimeExpense: 0 }
    },
    {
      name: '🔧 Váratlan Háztartási Kár (200 000 Ft egyszeri)',
      req: { incomeChangePercent: 0, fixedExpenseChangePercent: 0, variableExpenseChangePercent: 0, additionalMonthlyExpense: 0, additionalMonthlyIncome: 0, oneTimeExpense: 200000 }
    }
  ];

  ngOnInit(): void {
    this.recalculate();
  }

  applyPreset(presetReq: SimulationRequest): void {
    this.simulationRequest = { ...presetReq };
    this.recalculate();
  }

  resetSimulation(): void {
    this.simulationRequest = {
      incomeChangePercent: 0,
      fixedExpenseChangePercent: 0,
      variableExpenseChangePercent: 0,
      additionalMonthlyExpense: 0,
      additionalMonthlyIncome: 0,
      oneTimeExpense: 0
    };
    this.recalculate();
  }

  recalculate(): void {
    this.apiService.runSimulation(this.simulationRequest).subscribe(res => {
      this.simulationResult.set(res);
    });
  }
}
