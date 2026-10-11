import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { SessionStorageService } from './features/auth/signup/admin-signup-page/service/session-storage';

export const routeGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const session = inject(SessionStorageService);
  const userProfile = session.getProfile()();
  
  if(userProfile == null){
    router.navigate(['']);
    return false;
  }else{
    // console.log('User profile :' + JSON.parse(userProfile));
    return true;
  }
};
