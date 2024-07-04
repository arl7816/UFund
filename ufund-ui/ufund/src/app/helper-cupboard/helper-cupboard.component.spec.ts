import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HelperCupboardComponent } from './helper-cupboard.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('HelperCupboardComponent', () => {
  let component: HelperCupboardComponent;
  let fixture: ComponentFixture<HelperCupboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HelperCupboardComponent],
      imports: [HelperCupboardComponent]
      
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HelperCupboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
