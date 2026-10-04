import {
  Component,
  computed,
  effect,
  inject,
  input,
  OnInit,
  PLATFORM_ID,
  signal,
} from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { NotificationSseService } from '../../services/notificationSse.service';
import { OrderList } from '../order/order-list/order-list.component';
import { OrderCardComponent } from '../order/order-card/order-card.component';
import { Order, OrderItem } from '../../models/Order';
import { Customer } from '../../models/Customer';
import { OrderStatus } from '../../models/OrderStatus';
import { Restaurant } from '../../models/Restaurant';
import { OrderService } from '../../services/order.service';
import { OrderNotificationComponent } from '../order-notification/order-notification.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'restaurant-dashboard',
  standalone: true,
  imports: [CommonModule, OrderList, OrderCardComponent],
  templateUrl: './restaurant-dashboard.component.html',
})
export class RestaurantDashboardComponent {
  private readonly orderService = inject(OrderService);
  private readonly snackBar = inject(MatSnackBar);
  private readonly sseService = inject(NotificationSseService);

  private readonly platformId = inject(PLATFORM_ID);
  private orderSound?: HTMLAudioElement;

  restaurant = input<Restaurant | null>(null);

  customer: Customer = {
    id: 'asf-213-dfs',
    fullName: 'Jose Perez',
    email: 'joz@mail.com',
    number: '4424516852',
    address: 'Corregidora 204',
  };

  items: OrderItem[] = [
    {
      id: '123-asd',
      productName: 'Gelatina de limon',
      quantity: 1,
      unitPrice: 15,
    },
    {
      id: '123-432',
      productName: 'Pay de queso',
      quantity: 2,
      unitPrice: 30,
    },
  ];

  private readonly newOrderExample: Order = {
    id: '654-sdd-6ad',
    customer: this.customer,
    currency: 'MXC',
    items: this.items,
    totalAmount: 45,
    status: OrderStatus.PAYMENT_CONFIRMED,
    createdAt: '12:22 pm',
    updatedAt: '12:15 pm',
  };

  newOrders = signal<Order[]>([]);
  acceptedOrders = signal<Order[]>([this.newOrderExample, this.newOrderExample]);
  readyOrders = signal<Order[]>([this.newOrderExample, this.newOrderExample]);

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

  acceptOrder(id: string): void {
    this.orderService.acceptOrder(id).subscribe({
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

  markAsReadyForPickup(id: string): void {
    this.orderService.markAsReadyForPickup(id).subscribe({
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
