import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { CreateRestaurant, Restaurant } from '../models/Restaurant';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RestaurantService {
  private readonly http = inject(HttpClient);
  private readonly testaurantsUrl = 'http://localhost:8082/api/restaurants';

  getRestaurantById(id: string): Observable<Restaurant | null> {
    return this.http.get<Restaurant | null>(this.testaurantsUrl);
  }

  getRestaurants(): Observable<Restaurant[]> {
    return this.http.get<Restaurant[]>(this.testaurantsUrl);
  }

  createRestaurant(restaurant: CreateRestaurant): Observable<Restaurant> {
    return this.http.post<Restaurant>(this.testaurantsUrl, restaurant);
  }
}
