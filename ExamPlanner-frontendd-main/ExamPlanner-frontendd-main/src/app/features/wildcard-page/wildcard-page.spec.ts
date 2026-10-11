import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WildcardPage } from './wildcard-page';

describe('WildcardPage', () => {
  let component: WildcardPage;
  let fixture: ComponentFixture<WildcardPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WildcardPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WildcardPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
