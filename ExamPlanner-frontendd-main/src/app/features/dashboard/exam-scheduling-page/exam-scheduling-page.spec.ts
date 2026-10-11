import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamSchedulingPage } from './exam-scheduling-page';

describe('ExamSchedulingPage', () => {
  let component: ExamSchedulingPage;
  let fixture: ComponentFixture<ExamSchedulingPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExamSchedulingPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExamSchedulingPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
