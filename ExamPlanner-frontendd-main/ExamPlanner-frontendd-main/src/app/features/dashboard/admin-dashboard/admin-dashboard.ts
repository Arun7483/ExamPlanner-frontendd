import { NgTemplateOutlet } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { Router } from '@angular/router';
import {ButtonModule} from 'primeng/button';
import {DrawerModule} from 'primeng/drawer';
import { ProfileCard } from '../../shared/profile-card/profile-card';
import { SessionStorageService } from '../../auth/signup/admin-signup-page/service/session-storage';


@Component({
  selector: 'app-admin-dashboard',
  imports: [RouterOutlet,ButtonModule,DrawerModule,NgTemplateOutlet,RouterLink,ProfileCard],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css',
})
export class AdminDashboard implements OnInit{
  profile: any;

  constructor(private router: Router,
    private sessionStorage: SessionStorageService
  ) {}


  sidebarVisible: boolean = false;
  profilePageVisible: boolean = false;


  ngOnInit(){
    this.profile=this.sessionStorage.getProfile();
  }

  toggleSideBar() {
    this.sidebarVisible = !this.sidebarVisible;
  }

  toggleProfilePage() {
    this.profilePageVisible = !this.profilePageVisible
  }

   closeSideBar() {
    this.sidebarVisible = false;
  }

  AdminLogout() {
    this.sessionStorage.deleteToken();

    this.router.navigate([''])
    
  }
}
