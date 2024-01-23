import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentsessionComponent } from './currentsession.component';

describe('CurrentsessionComponent', () => {
  let component: CurrentsessionComponent;
  let fixture: ComponentFixture<CurrentsessionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CurrentsessionComponent]
    });
    fixture = TestBed.createComponent(CurrentsessionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
