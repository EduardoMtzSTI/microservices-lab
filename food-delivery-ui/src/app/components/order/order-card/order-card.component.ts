import { Component, computed, DestroyRef, inject, input, output, signal } from '@angular/core';
import { Order } from '../../../models/Order';
import { OrderStatus } from '../../../models/OrderStatus';
import { TimePipe } from '../../../shared/pipes/time.pipe';

@Component({
  selector: 'order-card',
  templateUrl: 'order-card.component.html',
  imports: [TimePipe],
})
export class OrderCardComponent {
  order = input.required<Order>();
  accept = output<string>();
  cancel = output<string>();
  readyForPickup = output<string>();

  protected readonly OrderStatus = OrderStatus;
  private readonly currentTime = signal(Date.now());
  private readonly destroyRef = inject(DestroyRef);

  readonly elapsedTime = computed(() => {
    const createdAt = new Date(this.order()?.createdAt).getTime();
    const now = this.currentTime();
    const seconds = Math.max(0, Math.floor((now - createdAt) / 1000));

    return this.formatElapsedTime(seconds);
  });

  constructor() {
    const interval = setInterval(() => {
      this.currentTime.set(Date.now());
    }, 1000);

    this.destroyRef.onDestroy(() => {
      clearInterval(interval);
    });
  }

  onAccept() {
    this.accept.emit(this.order().id);
  }

  onCancel() {
    this.cancel.emit(this.order().id);
  }

  onReadyForPickup() {
    this.readyForPickup.emit(this.order().id);
  }

  private formatElapsedTime(seconds: number): string {
    if (seconds < 60) {
      return `${seconds}s`;
    }

    const minutes = Math.floor(seconds / 60);

    if (minutes < 60) {
      return `${minutes}m`;
    }

    const hours = Math.floor(minutes / 60);

    if (hours < 24) {
      return `${hours}h`;
    }

    return `${Math.floor(hours / 24)}d`;
  }

  // acceptOrder(id: string) {
  //   this.orderService.acceptOrder(id).subscribe({
  //     next: (order) => console.log(order),
  //   });
  // }

  // markAsReadyForPickup(id: string) {
  //   this.orderService.markAsReadyForPickup(id).subscribe({
  //     next: (order) => console.log(order),
  //   });
  // }
}
