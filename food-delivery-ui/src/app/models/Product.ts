import { ProductCategory } from './ProductCategory';

export interface ProductData {
  name: string;
  price: string;
  currency: string;
  description: string;
  category: ProductCategory;
  restaurantId: string;
}

export interface CreateProduct extends ProductData {}

export interface Product extends ProductData {
  id: string;
}
