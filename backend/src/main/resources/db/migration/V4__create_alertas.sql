CREATE TABLE IF NOT EXISTS alertas_ambientais (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo VARCHAR(200) NOT NULL,
    mensagem TEXT NOT NULL,
    nivel VARCHAR(20) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    dispositivo_id UUID NOT NULL,
    leitura_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    resolvido_por VARCHAR(100),
    resolvido_em TIMESTAMP,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_alertas_ambientais_status ON alertas_ambientais(status);
CREATE INDEX idx_alertas_ambientais_nivel ON alertas_ambientais(nivel);
CREATE INDEX idx_alertas_ambientais_tipo ON alertas_ambientais(tipo);
CREATE INDEX idx_alertas_ambientais_dispositivo_id ON alertas_ambientais(dispositivo_id);
CREATE INDEX idx_alertas_ambientais_criado_em ON alertas_ambientais(criado_em);
