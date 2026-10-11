import { Component, OnInit, computed, signal, Signal } from '@angular/core';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ExamScheduleService, ExamSchedule } from '../../../services/exam-schedule-service';

@Component({
  selector: 'app-exam-scheduling-page',
  imports: [TableModule, ButtonModule, CommonModule],
  providers: [ExamScheduleService],
  templateUrl: './exam-scheduling-page.html',
  styleUrl: './exam-scheduling-page.css',
})
export class ExamSchedulingPage implements OnInit {
  schedules: Signal<ExamSchedule[]>;
  isLoading: Signal<boolean>;
  error: Signal<string | null>;

  constructor(private router: Router, private examScheduleService: ExamScheduleService) {
    this.schedules = this.examScheduleService.schedules;
    this.isLoading = this.examScheduleService.isLoading;
    this.error = this.examScheduleService.error;
  }

  ngOnInit(): void {
    this.loadSchedules();
  }

  loadSchedules(): void {
    this.examScheduleService.getAllSchedules().subscribe({
      next: () => {},
      error: () => {}
    });
  }

  deleteSchedule(id: number): void {
    if(confirm(`Are you sure you want to delete Schedule ID: ${id}?`)) {
      this.examScheduleService.deleteSchedule(id).subscribe({
        next: () => {
          console.log('Schedule deleted successfully');
        },
        error: (err: unknown) => {
          console.log('Delete failed: ', err);
        }
      });
    }
  }

  getStatusStyle(status: string) {
    if (status === 'Draft') {
      return { status_draft: true, status_live: false };
    } else {
      return { status_draft: false, status_live: true };
    }
  }

  navigateToExamScheduleForm() {
    this.router.navigate(['dashboard/schedules/create_schedule']);
  }
}
