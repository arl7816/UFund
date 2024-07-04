
import {FundingBasket} from './FundingBasket';

export class User{
    id: number;
    username: string;
    password: string;
    isAdmin: boolean;
    basket: FundingBasket;
    wallet: number;
  
    constructor(){
      this.id = 0;
      this.username = '';
      this.password = '';
      this.isAdmin = false;
      this.basket = new FundingBasket;
      this.wallet = 0;
    }
  }