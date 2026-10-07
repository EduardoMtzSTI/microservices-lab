import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { CreateRestaurant, Restaurant } from '../models/Restaurant';
import { Observable } from 'rxjs';
import { Order } from '../models/Order';

@Injectable({
  providedIn: 'root',
})
export class RestaurantService {
  private readonly http = inject(HttpClient);
  private readonly restaurantsUrl = 'http://localhost:8082/api/restaurants';

  getRestaurantById(id: string): Observable<Restaurant | null> {
    return this.http.get<Restaurant | null>(this.restaurantsUrl);
  }

  getRestaurants(): Observable<Restaurant[]> {
    return this.http.get<Restaurant[]>(this.restaurantsUrl);
  }

  createRestaurant(restaurant: CreateRestaurant): Observable<Restaurant> {
    return this.http.post<Restaurant>(this.restaurantsUrl, restaurant);
  }

  acceptOrder(restaurantId: string, orderId: string): Observable<Order> {
    return this.http.patch<Order>(
      this.restaurantsUrl + '/' + restaurantId + '/order/' + orderId + '/accept',
      null,
    );
  }

  markAsReadyForPickup(restaurantId: string, orderId: string): Observable<Order> {
    return this.http.patch<Order>(
      this.restaurantsUrl + '/' + restaurantId + '/order/' + orderId + '/ready',
      null,
    );
  }
}
