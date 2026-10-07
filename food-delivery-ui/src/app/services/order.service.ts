import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { NewOrder, Order } from '../models/Order';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private readonly http = inject(HttpClient);
  private readonly orderUrl = 'http://localhost:8083/api/orders';

  create(order: NewOrder): Observable<Order> {
    return this.http.post<Order>(this.orderUrl, order);
  }

  getById(id: string): Observable<Order> {
    return this.http.get<Order>(this.orderUrl + '/' + id);
  }

  getTodayRestaurantOrders(restaurantId: string): Observable<Order[]> {
    return this.http.get<Order[]>(this.orderUrl + '/restaurant/' + restaurantId + '/order/today');
  }
}
