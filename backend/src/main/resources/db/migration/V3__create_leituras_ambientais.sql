-- V3: Create leituras_ambientais table for environmental telemetry
CREATE TABLE leituras_ambientais (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    dispositivo_id UUID NOT NULL REFERENCES dispositivos(id),
    temperatura DOUBLE PRECISION NOT NULL,
    umidade DOUBLE PRECISION NOT NULL,
    luminosidade DOUBLE PRECISION NOT NULL,
    origem VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_leituras_dispositivo_id ON leituras_ambientais(dispositivo_id);
CREATE INDEX idx_leituras_timestamp ON leituras_ambientais(timestamp);
CREATE INDEX idx_leituras_dispositivo_timestamp ON leituras_ambientais(dispositivo_id, timestamp);
