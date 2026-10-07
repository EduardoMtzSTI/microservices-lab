INSERT INTO customers (
    id,
    full_name,
    email,
    number,
    address,
    status,
    created_at
) VALUES
      (
          'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
          'Juan Pérez',
          'juan.perez@example.com',
          '4421234567',
          'Av. Universidad 123, Querétaro',
          'ACTIVE',
          CURRENT_TIMESTAMP
      ),
      (
          'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
          'María González',
          'maria.gonzalez@example.com',
          '4429876543',
          'Calle Hidalgo 456, Querétaro',
          'ACTIVE',
          CURRENT_TIMESTAMP
      );