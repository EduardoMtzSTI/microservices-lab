import { Component, computed, effect, inject, input, PLATFORM_ID, signal } from '@angular/core';
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

interface OrderCardProps {
  order: Order;
  isProcessing: boolean;
}

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

  private readonly pendingConfirmations = new Map<string, ReturnType<typeof setTimeout>>();
  private readonly platformId = inject(PLATFORM_ID);
  private orderSound?: HTMLAudioElement;

  restaurant = input.required<Restaurant | null>();
  orderStatus = OrderStatus;
  orders = signal<OrderCardProps[]>([]);

  newOrders = computed(() =>
    this.orders().filter((orderProps) => orderProps.order.status === OrderStatus.PAYMENT_CONFIRMED),
  );

  acceptedOrders = computed(() =>
    this.orders().filter(
      (orderProps) => orderProps.order.status === OrderStatus.RESTAURANT_ACCEPTED,
    ),
  );

  readyOrders = computed(() =>
    this.orders().filter((orderProps) => orderProps.order.status === OrderStatus.READY_FOR_PICKUP),
  );

  constructor() {
    if (isPlatformBrowser(this.platformId)) {
      this.orderSound = new Audio('/assets/sounds/new-order-bell.wav');
      this.orderSound.preload = 'auto';
    }

    effect(() => {
      const restaurant = this.restaurant();
      if (!restaurant) return;
      this.loadOrders(restaurant.id);
      this.starToListenOrders(restaurant.id);
    });
  }

  loadOrders(restaurantId: string) {
    this.orderService.getTodayRestaurantOrders(restaurantId).subscribe({
      next: (orders) => {
        const orderCardProps = orders.map((order): OrderCardProps => ({
          order: order,
          isProcessing: false,
        }));
        this.orders.set(orderCardProps);
      },
    });
  }

  starToListenOrders(restaurantId: string) {
    this.sseService.connect(restaurantId).subscribe((payload) => {
      //cuadno ocurra un evento se consulta la orden
      this.orderService.getById(payload.aggregateId).subscribe({
        next: (order) => {
          if (this.isNewOrder(order)) {
            this.addNewOrder(order);
          }
          clearTimeout(this.pendingConfirmations.get(order.id));
          this.updateOrder(order);
        },
      });
    });
  }

  addNewOrder(order: Order) {
    this.orders.update((currentOrders) => [{ order, isProcessing: false }, ...currentOrders]);
    this.showOrderNotification();
    this.makeBellSound();
  }

  updateOrder(order: Order, isProcessing: boolean = false) {
    this.orders.update((currentOrdersProps) =>
      currentOrdersProps.map((cop) => {
        if (cop.order.id === order.id) return { order, isProcessing };
        return cop;
      }),
    );
  }

  isNewOrder(order: Order): boolean {
    return !this.orders().some((cop) => cop.order.id === order.id);
  }

  acceptOrder(orderId: string): void {
    const restaurant = this.restaurant();
    if (!restaurant) return;
    this.restaurantService.acceptOrder(restaurant.id, orderId).subscribe({
      next: () => this.waitForConfirmation(orderId, OrderStatus.PAYMENT_CONFIRMED),
      error: () => {
        this.updateOrderStatus(orderId, OrderStatus.PAYMENT_CONFIRMED);
        console.log('No se pudo enviar la aceptacion, intenta de nuevo');
      },
    });
  }

  markAsReadyForPickup(orderId: string): void {
    const restaurant = this.restaurant();
    if (!restaurant) return;
    this.restaurantService.markAsReadyForPickup(restaurant.id, orderId).subscribe({
      next: () => this.waitForConfirmation(orderId, OrderStatus.RESTAURANT_ACCEPTED),
      error: () => {
        this.updateOrderStatus(orderId, OrderStatus.RESTAURANT_ACCEPTED);
        console.log('No se pudo actualizar la orden como lista, intenta de nuevo');
      },
    });
  }

  private waitForConfirmation(orderId: string, previousStatus: OrderStatus): void {
    const timeout = setTimeout(() => {
      // Pasaron 10s sin confirmacion real -> probablemente el evento
      // termino en el DLT de order-service.
      //TODO: REGRESAR AL STATUS ANTERIOR
      this.setOrderAsProcessing(orderId, false);
      console.log(`El pedido ${orderId} no se confirmo, verifica el estado`);
    }, 10000);

    this.pendingConfirmations.set(orderId, timeout);
    this.setOrderAsProcessing(orderId, true);
  }

  setOrderAsProcessing(orderId: string, isProcessing: boolean) {
    this.orders.update((currentOrdersProps) =>
      currentOrdersProps.map((cop) => {
        if (cop.order.id === orderId) {
          return { order: cop.order, isProcessing };
        }
        return cop;
      }),
    );
  }

  updateOrderStatus(orderId: string, status: OrderStatus) {
    this.orders.update((orders) =>
      orders.map((poc) => {
        if (poc.order.id === orderId) {
          poc.order.status = status;
          return poc;
        }
        return poc;
      }),
    );
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
