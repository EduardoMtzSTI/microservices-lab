import { RestaurantMenu } from './Menu';
import { Product } from './Product';

export interface ProductCatalog extends Product {
  available: boolean;
  restaurant: RestaurantMenu;
}
