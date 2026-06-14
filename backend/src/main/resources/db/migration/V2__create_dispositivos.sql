-- V2: Create dispositivos table for IoT device management
CREATE TABLE dispositivos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    device_code VARCHAR(50) NOT NULL UNIQUE,
    nome VARCHAR(200) NOT NULL,
    area VARCHAR(100) NOT NULL,
    tipo_sensors JSONB NOT NULL DEFAULT '["TEMPERATURA"]',
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    data_instalacao TIMESTAMP NOT NULL DEFAULT NOW(),
    ultima_comunicacao TIMESTAMP,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP
);

CREATE INDEX idx_dispositivos_device_code ON dispositivos(device_code);
CREATE INDEX idx_dispositivos_status ON dispositivos(status);
CREATE INDEX idx_dispositivos_area ON dispositivos(area);
