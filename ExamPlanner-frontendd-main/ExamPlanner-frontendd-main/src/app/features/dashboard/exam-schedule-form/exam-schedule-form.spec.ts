import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamScheduleForm } from './exam-schedule-form';

describe('ExamScheduleForm', () => {
  let component: ExamScheduleForm;
  let fixture: ComponentFixture<ExamScheduleForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExamScheduleForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExamScheduleForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
