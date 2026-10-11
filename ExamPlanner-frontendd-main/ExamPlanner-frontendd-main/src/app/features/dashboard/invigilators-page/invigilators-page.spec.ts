import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvigilatorsPage } from './invigilators-page';

describe('InvigilatorsPage', () => {
  let component: InvigilatorsPage;
  let fixture: ComponentFixture<InvigilatorsPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InvigilatorsPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InvigilatorsPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
