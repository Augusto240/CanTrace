package br.edu.cantrace.alertas;

public record AlertaRegraResultado(
    TipoAlerta tipo,
    NivelAlerta nivel,
    String titulo,
    String mensagem
) {}
