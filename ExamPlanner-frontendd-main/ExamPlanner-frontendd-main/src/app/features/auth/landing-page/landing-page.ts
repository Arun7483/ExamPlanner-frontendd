import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-landing-page',
  imports: [ButtonModule,RouterLink],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.css',
})
export class LandingPage {
  projectTitle: string = "ExamPlanner";
  shortDescription: string = "Digital Exam Scheduling & Administration System";

}
