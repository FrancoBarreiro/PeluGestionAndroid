import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultsJobsComponent } from './results-jobs.component';

describe('ResultsJobsComponent', () => {
  let component: ResultsJobsComponent;
  let fixture: ComponentFixture<ResultsJobsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ResultsJobsComponent]
    });
    fixture = TestBed.createComponent(ResultsJobsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
