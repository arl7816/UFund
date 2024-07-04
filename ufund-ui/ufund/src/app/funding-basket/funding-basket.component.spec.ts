import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FundingBasketComponent } from './funding-basket.component';

describe('FundingBasketComponent', () => {
  let component: FundingBasketComponent;
  let fixture: ComponentFixture<FundingBasketComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FundingBasketComponent],
      declarations: [FundingBasketComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FundingBasketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
