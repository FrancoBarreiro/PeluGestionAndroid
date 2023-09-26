import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultsExpensesComponent } from './results-expenses.component';

describe('ResultsExpensesComponent', () => {
  let component: ResultsExpensesComponent;
  let fixture: ComponentFixture<ResultsExpensesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ResultsExpensesComponent]
    });
    fixture = TestBed.createComponent(ResultsExpensesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
