import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DataService } from '../../../data';

@Component({
  selector: 'app-admin-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-home-page.html',
  styleUrl: './admin-home-page.css'
})
export class AdminHomePage implements OnInit {
  studentCount = 2500;
  examCount = 15;
  invigilatorCount = 75;
  hallCount = 12;

  // Pie chart data
  studentsByDept = [
    { name: 'Science', value: 35, color: '#3498db' },
    { name: 'Commerce', value: 25, color: '#2ecc71' },
    { name: 'Arts', value: 20, color: '#f1c40f' },
    { name: 'Others', value: 20, color: '#e74c3c' }
  ];

  // Bar chart data (Values 0-8 to match the Y-axis in the image)
  examsByDept = [
    { name: 'Science', value: 7.5 },
    { name: 'Commerce', value: 5.5 },
    { name: 'Arts', value: 3.5 },
    { name: 'Others', value: 3.5 }
  ];

  constructor(private router: Router, private dataService: DataService) {}

  ngOnInit(): void {
    // Optionally fetch live data
    // this.studentCount = this.dataService.getStudentCount();
  }
}