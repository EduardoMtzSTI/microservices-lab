import { Product } from './Product';

export interface RestaurantMenu {
  id: string;
  name: string;
  openingTime: string;
  closingTime: string;
}

export interface ProductMenu extends Product {
  available: boolean;
  restaurantId: string;
}

export interface Menu {
  restaurant: RestaurantMenu;
  products: ProductMenu[];
}
