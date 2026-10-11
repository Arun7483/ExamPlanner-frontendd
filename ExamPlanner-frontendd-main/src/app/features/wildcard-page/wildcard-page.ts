import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-wildcard-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './wildcard-page.html',
  styleUrls: ['./wildcard-page.css'],
})
export class WildcardPage {
  constructor(private location: Location) {}

  goBack() {
    this.location.back();
  }
}
