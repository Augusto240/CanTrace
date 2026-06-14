CREATE TABLE lotes (
    id UUID PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nome_produto VARCHAR(200) NOT NULL,
    data_entrada DATE NOT NULL,
    data_validade DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'RASCUNHO',
    quantidade_inicial DECIMAL(10,2) NOT NULL,
    quantidade_atual DECIMAL(10,2) NOT NULL,
    responsavel VARCHAR(200) NOT NULL,
    notas TEXT,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP
);

CREATE TABLE movimentacoes_lote (
    id UUID PRIMARY KEY,
    lote_id UUID NOT NULL REFERENCES lotes(id),
    tipo VARCHAR(20) NOT NULL,
    data TIMESTAMP NOT NULL DEFAULT NOW(),
    quantidade DECIMAL(10,2) NOT NULL,
    responsavel VARCHAR(200) NOT NULL,
    destino VARCHAR(200),
    observacao TEXT
);

CREATE TABLE registros_auditoria (
    id UUID PRIMARY KEY,
    entidade VARCHAR(100) NOT NULL,
    entidade_id VARCHAR(100) NOT NULL,
    acao VARCHAR(20) NOT NULL,
    dados_anteriores JSONB,
    dados_novos JSONB,
    usuario VARCHAR(200) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT NOW(),
    ip_address VARCHAR(45),
    user_agent TEXT
);

CREATE INDEX idx_lotes_codigo ON lotes(codigo);
CREATE INDEX idx_lotes_status ON lotes(status);
CREATE INDEX idx_movimentacoes_lote_id ON movimentacoes_lote(lote_id);
CREATE INDEX idx_auditoria_entidade ON registros_auditoria(entidade, entidade_id);
CREATE INDEX idx_auditoria_timestamp ON registros_auditoria(timestamp);
