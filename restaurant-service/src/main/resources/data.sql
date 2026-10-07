INSERT INTO restaurants (
    id,
    name,
    address,
    phone,
    status,
    open,
    opening_time,
    closing_time,
    created_at
) VALUES (
 'ea1f214e-166b-487f-9f0b-60821abc9319',
 'Fonda lulu',
 'Av. Hidalgo',
 '4424516784',
 'ACTIVE',
 true,
 '10:00:00',
 '17:30:00',
 CURRENT_TIMESTAMP
);

INSERT INTO products (
    id, restaurant_id, name, price, currency, description,
    category, available, created_at, idempotency_key
) VALUES
      (
          '11111111-1111-1111-1111-111111111111',
          'ea1f214e-166b-487f-9f0b-60821abc9319',
          'Hamburguesa Clásica',
          12000,
          'MXN',
          'Hamburguesa con carne de res, lechuga, tomate y queso',
          'COMIDA',
          true,
          CURRENT_TIMESTAMP,
          'seed-product-001'
      ),
      (
          '22222222-2222-2222-2222-222222222222',
          'ea1f214e-166b-487f-9f0b-60821abc9319',
          'Tacos al Pastor',
          8500,
          'MXN',
          'Orden de tacos al pastor con cebolla, cilantro y piña',
          'COMIDA',
          true,
          CURRENT_TIMESTAMP,
          'seed-product-002'
      ),
      (
          '33333333-3333-3333-3333-333333333333',
          'ea1f214e-166b-487f-9f0b-60821abc9319',
          'Refresco',
          3000,
          'MXN',
          'Refresco de 600 ml',
          'BEBIDA',
          true,
          CURRENT_TIMESTAMP,
          'seed-product-003'
      ),
      (
          '44444444-4444-4444-4444-444444444444',
          'ea1f214e-166b-487f-9f0b-60821abc9319',
          'Papas Fritas',
          5500,
          'MXN',
          'Papas fritas crujientes',
          'COMPLEMENTO',
          true,
          CURRENT_TIMESTAMP,
          'seed-product-004'
      );