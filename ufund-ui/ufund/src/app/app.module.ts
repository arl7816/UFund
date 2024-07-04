import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';


import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { CupboardComponent } from './cupboard/cupboard.component';
import { EditNeedComponent } from './edit-need/edit-need.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { AddNeedComponent } from './add-need/add-need.component';
import { FundingBasketComponent } from './funding-basket/funding-basket.component';
import { HelperCupboardComponent } from './helper-cupboard/helper-cupboard.component';
import { SignupPageComponent } from './signup-page/signup-page.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { AddBundleComponent } from './add-bundle/add-bundle.component';
import { EditBundleComponent } from './edit-bundle/edit-bundle.component';
import { HOMEComponent } from './home/home.component';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
  ],
  declarations: [
    AppComponent,
    CupboardComponent,
    EditNeedComponent,
    CheckoutComponent,
    AddNeedComponent,
    FundingBasketComponent,
    HelperCupboardComponent,
    SignupPageComponent,
    AddBundleComponent,
    EditBundleComponent,
    HOMEComponent,

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }