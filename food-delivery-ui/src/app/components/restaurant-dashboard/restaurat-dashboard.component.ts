import { Component, effect, inject, input, PLATFORM_ID, signal } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { NotificationSseService } from '../../services/notificationSse.service';
import { OrderList } from '../order/order-list/order-list.component';
import { OrderCardComponent } from '../order/order-card/order-card.component';
import { Order } from '../../models/Order';
import { OrderStatus } from '../../models/OrderStatus';
import { Restaurant } from '../../models/Restaurant';
import { OrderService } from '../../services/order.service';
import { OrderNotificationComponent } from '../order-notification/order-notification.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { RestaurantService } from '../../services/restaura.service';

@Component({
  selector: 'restaurant-dashboard',
  standalone: true,
  imports: [CommonModule, OrderList, OrderCardComponent],
  templateUrl: './restaurant-dashboard.component.html',
})
export class RestaurantDashboardComponent {
  private readonly restaurantService = inject(RestaurantService);
  private readonly orderService = inject(OrderService);
  private readonly snackBar = inject(MatSnackBar);
  private readonly sseService = inject(NotificationSseService);

  private readonly platformId = inject(PLATFORM_ID);
  private orderSound?: HTMLAudioElement;

  restaurant = input.required<Restaurant | null>();

  newOrders = signal<Order[]>([]);
  acceptedOrders = signal<Order[]>([]);
  readyOrders = signal<Order[]>([]);

  constructor() {
    if (isPlatformBrowser(this.platformId)) {
      this.orderSound = new Audio('/assets/sounds/new-order-bell.wav');
      this.orderSound.preload = 'auto';
    }

    effect(() => {
      const restaurant = this.restaurant();
      if (!restaurant) return;
      this.loadOrders(restaurant.id);
      this.starToListenNewOrders(restaurant.id);
    });
  }

  starToListenNewOrders(restaurantId: string) {
    this.sseService.connect(restaurantId).subscribe((payload) => {
      this.orderService.getById(payload.aggregateId).subscribe({
        next: (newOrder) => {
          this.newOrders.update((currentOrders) => [newOrder, ...currentOrders]);
          this.showOrderNotification();
          this.makeBellSound();
        },
      });
    });
  }

  loadOrders(restaurantId: string) {
    this.orderService.getTodayRestaurantOrders(restaurantId).subscribe({
      next: (orders) => {
        const newOrders = this.filterOrdersByStatus(orders, OrderStatus.PAYMENT_CONFIRMED);
        this.newOrders.set(newOrders);

        const acceptedOrders = this.filterOrdersByStatus(orders, OrderStatus.RESTAURANT_ACCEPTED);
        this.acceptedOrders.set(acceptedOrders);

        const readyOrders = this.filterOrdersByStatus(orders, OrderStatus.READY_FOR_PICKUP);
        this.readyOrders.set(readyOrders);
      },
    });
  }

  filterOrdersByStatus(orders: Order[], status: OrderStatus): Order[] {
    return orders.filter((order) => order.status === status);
  }

  acceptOrder(orderId: string): void {
    const restaurant = this.restaurant();
    if (!restaurant) return;
    this.restaurantService.acceptOrder(restaurant.id, orderId).subscribe({
      next: (order) => {
        // quitar orden de lista de nuevas ordenes
        this.newOrders.update((orders) => orders.filter((o) => o.id !== order.id));
        // agregar orden a ordenes aceptadas
        this.acceptedOrders.update((orders) => {
          const updatedOrders = orders.filter((o) => o.id !== order.id);
          return [...updatedOrders, order];
        });
      },
    });
  }

  cancelOrder(id: string): void {}

  markAsReadyForPickup(orderId: string): void {
    const restaurant = this.restaurant();
    if (!restaurant) return;
    this.restaurantService.markAsReadyForPickup(restaurant.id, orderId).subscribe({
      next: (order) => {
        // quitar ordenes de lista de ordenes aceptadas
        this.acceptedOrders.update((orders) => orders.filter((o) => o.id !== order.id));
        // agregar orden a ordenes listas
        this.readyOrders.update((orders) => {
          const updatedOrders = orders.filter((o) => o.id !== order.id);
          return [...updatedOrders, order];
        });
      },
    });
  }

  showOrderNotification(): void {
    this.snackBar.openFromComponent(OrderNotificationComponent, {
      duration: 60000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: ['order-snackbar'],
    });
  }

  makeBellSound() {
    if (!this.orderSound) return;
    this.orderSound.currentTime = 0;
    this.orderSound.play().catch(() => {
      // El navegador puede bloquear autoplay si no hubo interacción del usuario.
    });
  }
}
