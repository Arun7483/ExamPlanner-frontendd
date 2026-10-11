import { Component, OnInit } from '@angular/core';
import { SessionStorageService } from '../../auth/signup/admin-signup-page/service/session-storage';

@Component({
  selector: 'app-profile-card',
  imports: [],
  templateUrl: './profile-card.html',
  styleUrl: './profile-card.css',
})
export class ProfileCard implements OnInit{

  profile: any;

  constructor(private sessionStorage: SessionStorageService) {}

  ngOnInit(){
    this.profile=this.sessionStorage.getProfile();
  }
}
