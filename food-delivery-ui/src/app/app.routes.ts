import { Routes } from '@angular/router';
import { HomePageComponent } from './page/home/home-page.component';
import { CustomerPageComponent } from './page/customer/customer-page.component';
import { RestaurantPageComponent } from './page/restaurant/restaurant-page.component';
import { OrderPageComponent } from './page/order/order-page.component';

export const routes: Routes = [
  {
    path: '',
    component: HomePageComponent,
  },
  {
    path: 'customers',
    component: CustomerPageComponent,
  },
  {
    path: 'restaurants',
    component: RestaurantPageComponent,
  },
  {
    path: 'orders',
    component: OrderPageComponent,
  },
];
