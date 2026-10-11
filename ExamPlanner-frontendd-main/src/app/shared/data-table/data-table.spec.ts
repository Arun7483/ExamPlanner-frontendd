import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DataTableComponent } from './data-table';

describe('DataTable', () => {
  let component: DataTableComponent;
  let fixture: ComponentFixture<DataTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DataTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DataTableComponentImport { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DataService } from '../../../data';
import { NgChartsModule } from 'ng2-charts';
import { ChartConfiguration, ChartType } from 'chart.js';

@Component({
  selector: 'app-admin-home',
  standalone: true,
  imports: [RouterModule, CommonModule, NgChartsModule],
  templateUrl: './admin-home-page.html',
  styleUrl: './admin-home-page.css',
})
export class AdminHomePage {

  // ===== EXISTING DASHBOARD COUNTS =====
  get totalStudents() {
    return this.dataService.getStudentCount();
  }

  get upcomingExams() {
    return this.dataService.getExamCount();
  }

  get totalInvigilators() {
    return this.dataService.getInvigilatorCount();
  }

  get hallsInUse() {
    return this.dataService.getHallCount();
  }

  constructor(
    private router: Router,
    private dataService: DataService
  ) {}

  navigateTo(path: string) {
    this.router.navigate([path]);
  }

  // ===== PIE CHART: STUDENTS BY DEPARTMENT =====
  pieChartType: ChartType = 'pie';

  pieChartData: ChartConfiguration<'pie'>['data'] = {
    labels: ['Science', 'Commerce', 'Arts', 'Others'],
    datasets: [
      {
        data: [35, 25, 20, 20],
      },
    ],
  };

  // ===== BAR CHART: UPCOMING EXAMS =====
  barChartType: ChartType = 'bar';

  barChartData: ChartConfiguration<'bar'>['data'] = {
    labels: ['Science', 'Commerce', 'Arts', 'Others'],
    datasets: [
      {
        label: 'Upcoming Exams',
        data: [7, 4, 2, 2],
      },
    ],
  };

  // ===== DOUGHNUT CHART: INVIGILATORS =====
  doughnutChartType: ChartType = 'doughnut';

  doughnutChartData: ChartConfiguration<'doughnut'>['data'] = {
    labels: ['Available', 'Assigned'],
    datasets: [
      {
        data: [75, 25],
      },
    ],
  };

  // ===== HORIZONTAL BAR: HALL STATUS =====
  hallChartType: ChartType = 'bar';

  hallChartData: ChartConfiguration<'bar'>['data'] = {
    labels: ['In Use', 'Available'],
    datasets: [
      {
        label: 'Halls',
        data: [12, 8],
      },
    ],
  };

  hallChartOptions: ChartConfiguration<'bar'>['options'] = {
    indexAxis: 'y',
  };
});
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
