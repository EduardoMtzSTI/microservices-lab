export interface RestaurantData {
  name: string;
  address: string;
  phone: string;
  openingTime: string;
  closingTime: string;
}

export interface CreateRestaurant extends RestaurantData {}

export interface Restaurant extends RestaurantData {
  id: string;
}
