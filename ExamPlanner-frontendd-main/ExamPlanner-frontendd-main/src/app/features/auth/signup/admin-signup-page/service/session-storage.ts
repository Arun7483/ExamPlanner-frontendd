import { Injectable,signal } from '@angular/core';


@Injectable({
  providedIn: 'root',
})
export class SessionStorageService {
  private profile = signal<any>(null);


  constructor(){
    if(this.isBrowser()) {
      const data = sessionStorage.getItem('profile');
      this.profile.set(data ? JSON.parse(data) : null);
    }
  }

  private isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof sessionStorage !== 'undefined';
    }
  setProfile(profile:any):void{
    if(this.isBrowser()) {
    sessionStorage.setItem('profile',JSON.stringify(profile));
    }
    this.profile.set(profile);
  }
  private loadProfile():any|null{
    if(!this.isBrowser()) return null;
    const data=sessionStorage.getItem('profile');
    return data?JSON.parse(data):null;
  }
  getProfile():any|null{
    return this.profile;
  }

  setToken(token:any):void {
    sessionStorage.setItem('token',token);
  }

  deleteToken():void {
    sessionStorage.removeItem('token');
  }

  getToken():any {
    return sessionStorage.getItem('token');
  }

  clearProfile(): void{
    if (this.isBrowser()) {
    sessionStorage.removeItem('profile');
    }
    this.profile.set(null);
  }
}
