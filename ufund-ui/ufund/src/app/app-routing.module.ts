import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CupboardComponent } from './cupboard/cupboard.component';
import { EditNeedComponent } from './edit-need/edit-need.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { AddNeedComponent } from './add-need/add-need.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { FundingBasketComponent } from './funding-basket/funding-basket.component';
import { HelperCupboardComponent } from './helper-cupboard/helper-cupboard.component';
import { SignupPageComponent } from './signup-page/signup-page.component';
import { UserProfileComponent } from './user-profile/user-profile.component';

import { AddBundleComponent } from './add-bundle/add-bundle.component';
import { EditBundleComponent } from './edit-bundle/edit-bundle.component';
import { HOMEComponent } from './home/home.component';

const routes: Routes = [
  { path: '', component: HOMEComponent, pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  { path: 'cupboard', component: CupboardComponent },
  { path: 'editNeed/:id', component: EditNeedComponent },
  { path: 'checkout', component: CheckoutComponent},
  { path: 'addNeed', component: AddNeedComponent },
  { path: 'basket', component: FundingBasketComponent},
  { path: 'helper-cupboard', component: HelperCupboardComponent },
  { path: 'signup', component: SignupPageComponent},
  { path: 'user-profile', component: UserProfileComponent},
  { path: 'addBundle', component: AddBundleComponent},
  { path: 'editBundle/:id', component: EditBundleComponent},
  { path: 'home', component: HOMEComponent}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
