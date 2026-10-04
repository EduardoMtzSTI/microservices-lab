import { Injectable, signal } from '@angular/core';
import { Product } from '../models/Product';
import { Validated } from '../models/Common';
import { NewOrderItem } from '../models/Order';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private readonly _cart = signal<Product[]>([]);
  readonly cart = this._cart.asReadonly();
  readonly restaurantId = signal<string | null>(null);
  customerId = signal<string | null>(null);

  add(product: Product) {
    if (this._cart().length === 0) this.setRestaurantId(product.restaurantId);
    this._cart.update((products) => [...products, product]);
  }

  remove(product: Product) {
    if (this._cart().length === 1) this.deleteRestaurantId();
    this._cart.update((products) => {
      return products.filter((item) => item.id !== product.id);
    });
  }

  private setRestaurantId(value: string) {
    this.restaurantId.set(value);
  }

  private deleteRestaurantId() {
    this.restaurantId.set(null);
  }

  clear() {
    this._cart.set([]);
  }

  validateAddToCart(product: Product): Validated {
    let validated: Validated = { isValid: true, messages: [] };
    if (!this.customerId()) {
      validated.messages = ['Selecciona un cliente para poder agregar productos al carrito'];
      validated.isValid = false;
    }
    if (this.exitsProduct(product.id)) {
      validated.isValid = false;
    }
    return validated;
  }

  exitsProduct(id: string): boolean {
    return this.cart().some((product) => product.id === id);
  }

  getOrderItems(): NewOrderItem[] {
    return this.cart().map((product: Product) => ({
      id: product.id,
      quantity: 1,
    }));
  }
}
