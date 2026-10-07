# microservices-lab

Aplicación de entrega de comida creada con microservicios con spring boot y UI con Angular

Idea general del dominio

Un pedido pasa por varios estados, y cada cambio de estado es un evento perfecto para Kafka:

Pedido creado → Pago confirmado → Restaurante acepta → En preparación → Listo → Repartidor asignado → En camino → Entregado

Microservicios sugeridos
order-service — crea y gestiona pedidos (API REST/gRPC)
payment-service — procesa pagos (simulado o con Stripe test mode)
restaurant-service — gestiona menú, disponibilidad, acepta/rechaza pedidos
delivery-service — asigna repartidores, trackea ubicación
notification-service — envía notificaciones (consumer puro, no expone API)
api-gateway — punto de entrada único (Spring Cloud Gateway, Kong, o Express Gateway)

Tópicos de Kafka
Topic                   Productor           Consumidores
order.created           order-service       payment-service, notification-service
payment.confirmed       payment-service     order-service, restaurant-service
payment.failed          payment-service     order-service, notification-service
order.accepted          restaurant-service  order-service, delivery-service
order.ready             restaurant-service  delivery-service
delivery.assigned       delivery-service    order-service, notification-service
delivery.status.updated delivery-service    order-service, notification-service
order.delivered         delivery-service    order-service, notification-service

Patrones interesantes para aplicar
Saga pattern (coreografiada): cada servicio reacciona a eventos y publica el siguiente, sin un orquestador central. Es el patrón más natural con Kafka.
Event sourcing ligero: guardar el historial de estados del pedido como secuencia de eventos.
CQRS simple: un servicio de "order-status" que solo lee eventos y mantiene una vista materializada para consultas rápidas.
Dead Letter Topic: para manejar fallos de procesamiento (ej. pago rechazado 3 veces).
Outbox pattern: evitar inconsistencias entre la base de datos y la publicación del evento en Kafka.
Stack técnico sugerido
Lenguaje: Java + Spring Boot (Spring Kafka) es lo más común en tutoriales, pero también funciona muy bien con Node.js (KafkaJS) o Python (confluent-kafka) si prefieres algo más liviano.
Kafka: Docker Compose con Kafka + Zookeeper (o KRaft mode, sin Zookeeper, más moderno) + Kafka UI para visualizar tópicos.
Bases de datos: una por servicio (Postgres o MongoDB), siguiendo "database per service".
Opcional avanzado: Schema Registry con Avro para versionar los esquemas de eventos.

1-Levantar la infraestructura base
Docker Compose con Kafka (modo KRaft, sin Zookeeper), Kafka UI (para ver tópicos y mensajes visualmente), y una base de datos Postgres por servicio. Verifica que puedes crear un tópico manualmente y publicar/consumir un mensaje de prueba con la consola de Kafka antes de escribir código.

2-Construir order-service y payment-service
Empieza con el flujo más simple: crear un pedido vía REST, publicar order.created, y que payment-service lo consuma, simule un pago (90% éxito, 10% falla) y publique payment.confirmed o payment.failed. Esto te da el esqueleto productor/consumidor funcionando end-to-end.

3-Agregar restaurant-service y delivery-service
Encadena la saga: restaurant-service escucha payment.confirmed, simula aceptación/rechazo, y publica restaurant.accepted y luego order.ready. delivery-service escucha order.ready, asigna un repartidor simulado y publica delivery.assigned y delivery.status.updated en intervalos.

4-Notification-service y vista de estado unificada
notification-service consume todos los tópicos relevantes y solo imprime/loguea notificaciones (simula email o push). Además, crea un endpoint GET /orders/{id}/status que consulte una vista materializada construida a partir de los eventos (patrón CQRS simple).

5-Manejo de fallos y resiliencia
Implementa reintentos con backoff, un Dead Letter Topic para pagos fallidos repetidamente, e idempotencia en los consumidores (evitar procesar el mismo evento dos veces usando el ID del pedido).

6-Observabilidad y pulido final
Agrega logs estructurados, métricas básicas (ej. cuántos pedidos por minuto), y opcionalmente traza distribuida con OpenTelemetry para ver el recorrido completo de un pedido a través de los servicios.
