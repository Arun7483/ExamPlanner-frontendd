import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { SessionStorageService } from '../auth/signup/admin-signup-page/service/session-storage';  
import { ProfileCard } from '../shared/profile-card/profile-card';
import { DrawerModule } from 'primeng/drawer';
@Component({
  selector: 'app-student-dashboard',
  standalone: true,
  imports: [CommonModule, ButtonModule, DrawerModule, ProfileCard],
  templateUrl: './student-dashboard.html',
  styleUrls: ['./student-dashboard.css']
})
export class StudentDashboard implements OnInit {
  profilePageVisible: boolean = false; // State for the drawer

  exams = [
    { code: 'CS101', name: 'Introduction to Programming', date: 'Jan 28, 2026', venue: 'Main Hall A', seat: 'A-15' },
    { code: 'MATH201', name: 'Calculus II', date: 'Jan 30, 2026', venue: 'Main Hall B', seat: 'C-10' },
    { code: 'PHY101', name: 'Physics I', date: 'Feb 02, 2026', venue: 'Science Block', seat: 'B-05' }
  ];

  constructor(
    private sessionService: SessionStorageService, 
    private router: Router
  ) {}

  ngOnInit() {}

  toggleProfilePage() {
    this.profilePageVisible = !this.profilePageVisible;
  }

  downloadAdmit() {
    alert('Generating Admit Card...');
  }

  logout() {
    this.sessionService.clearProfile();
    this.sessionService.deleteToken();
    this.router.navigate(['']);
  }
}