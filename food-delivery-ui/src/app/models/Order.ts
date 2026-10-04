import { Customer } from './Customer';
import { OrderStatus } from './OrderStatus';

export interface NewOrder {
  customerId: string;
  restaurantId: string;
  items: NewOrderItem[];
}

export interface NewOrderItem {
  id: string;
  productName?: string;
  quantity: number;
  unitPrice?: number;
}

export interface Order {
  id: string;
  currency: string;
  items: OrderItem[];
  customer: Customer;
  totalAmount: Number;
  status: OrderStatus;
  createdAt: string;
  updatedAt: string;
}

export interface OrderItem {
  id: string;
  productName: string;
  quantity: number;
  unitPrice: number;
}
