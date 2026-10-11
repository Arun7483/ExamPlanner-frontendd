import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSignupPage } from './admin-signup-page';

describe('AdminSignupPage', () => {
  let component: AdminSignupPage;
  let fixture: ComponentFixture<AdminSignupPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminSignupPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSignupPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
