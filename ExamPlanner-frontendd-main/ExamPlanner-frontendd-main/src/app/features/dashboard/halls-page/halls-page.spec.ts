import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HallsPage } from './halls-page';

describe('HallsPage', () => {
  let component: HallsPage;
  let fixture: ComponentFixture<HallsPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HallsPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HallsPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
