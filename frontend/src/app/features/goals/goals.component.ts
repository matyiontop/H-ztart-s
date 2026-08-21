import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HaztartasApiService } from '../../core/services/haztartas-api.service';
import { HufCurrencyPipe } from '../../shared/pipes/huf-currency.pipe';
import { SavingsGoal } from '../../core/models/haztartas.models';

@Component({
  selector: 'app-goals',
  standalone: true,
  imports: [CommonModule, FormsModule, HufCurrencyPipe],
  templateUrl: './goals.component.html',
  styleUrls: ['./goals.component.css']
})
export class GoalsComponent implements OnInit {
  public apiService = inject(HaztartasApiService);

  public isGoalModalOpen = signal<boolean>(false);
  public isDepositModalOpen = signal<boolean>(false);

  public selectedGoalId: number | null = null;
  public depositAmount: number = 25000;

  public newGoal: SavingsGoal = {
    name: '',
    targetAmount: 0,
    currentAmount: 0,
    targetDate: '',
    color: '#10b981',
    icon: 'target',
    notes: ''
  };

  ngOnInit(): void {
    this.apiService.getGoals().subscribe();
  }

  get totalSaved(): number {
    return this.apiService.goals().reduce((sum, g) => sum + (g.currentAmount || 0), 0);
  }

  get totalTarget(): number {
    return this.apiService.goals().reduce((sum, g) => sum + (g.targetAmount || 0), 0);
  }

  get overallProgress(): number {
    if (this.totalTarget <= 0) return 0;
    return Math.min(100, Math.round((this.totalSaved / this.totalTarget) * 100));
  }

  openGoalModal(): void {
    this.newGoal = {
      name: '',
      targetAmount: 0,
      currentAmount: 0,
      targetDate: '',
      color: '#10b981',
      icon: 'target',
      notes: ''
    };
    this.isGoalModalOpen.set(true);
  }

  closeGoalModal(): void {
    this.isGoalModalOpen.set(false);
  }

  saveGoal(): void {
    if (!this.newGoal.name || this.newGoal.targetAmount <= 0) return;
    this.apiService.createGoal(this.newGoal).subscribe(() => {
      this.closeGoalModal();
    });
  }

  openDepositModal(goalId?: number): void {
    if (!goalId) return;
    this.selectedGoalId = goalId;
    this.depositAmount = 25000;
    this.isDepositModalOpen.set(true);
  }

  closeDepositModal(): void {
    this.isDepositModalOpen.set(false);
    this.selectedGoalId = null;
  }

  saveDeposit(): void {
    if (this.selectedGoalId && this.depositAmount > 0) {
      this.apiService.addDeposit(this.selectedGoalId, this.depositAmount).subscribe(() => {
        this.closeDepositModal();
      });
    }
  }

  deleteGoal(id?: number): void {
    if (id && confirm('Biztosan törlöd ezt a megtakarítási célt?')) {
      this.apiService.deleteGoal(id).subscribe();
    }
  }
}
