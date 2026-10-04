import { Injectable, NgZone, OnDestroy, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Observable } from 'rxjs';

interface SsePayload {
  recipientId: string;
  eventType: string;
  aggregateId: string;
  message: string;
  occurredAt: Date;
}

@Injectable({ providedIn: 'root' })
export class NotificationSseService implements OnDestroy {
  private eventSource: EventSource | null = null;
  private readonly notificationsUrl = 'http://localhost:8085/api/notifications/stream';
  private readonly isBrowser: boolean;

  constructor(
    private ngZone: NgZone,
    @Inject(PLATFORM_ID) platformId: object,
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  /**
   * EventSource se reconecta automáticamente si la conexión se corta — no
   * hay que implementar reintentos a mano. zone.run(...) es necesario porque
   * EventSource dispara sus eventos FUERA de la zona de Angular; sin esto,
   * la UI no se actualizaría hasta el próximo cambio detectado por otro medio.
   */
  connect(recipientId: string): Observable<SsePayload> {
    return new Observable<SsePayload>((subscriber) => {
      // En el servidor (SSR) no existe EventSource — simplemente no nos
      // conectamos ahí. La conexión real solo se abre cuando el navegador
      // hidrata la aplicación y este código corre otra vez, ya en el cliente.
      if (!this.isBrowser) {
        return;
      }

      this.eventSource = new EventSource(`${this.notificationsUrl}/${recipientId}`);

      this.eventSource.addEventListener('notification', (event: MessageEvent) => {
        const payload = JSON.parse(event.data) as SsePayload;
        this.ngZone.run(() => subscriber.next(payload));
      });

      this.eventSource.onerror = () => {
        // EventSource reintenta solo; aquí solo se notifica que hubo un corte,
        // sin cerrar el Observable — la conexión se recupera sola.
        this.ngZone.run(() => console.warn('Conexión SSE interrumpida, reintentando...'));
      };

      return () => this.disconnect();
    });
  }

  disconnect(): void {
    this.eventSource?.close();
    this.eventSource = null;
  }

  ngOnDestroy(): void {
    this.disconnect();
  }
}
