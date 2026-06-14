package br.edu.cantrace.mqtt;

public record MQTTMessage(
    String codigoDispositivo,
    Double temperatura,
    Double umidade,
    Double luminosidade,
    String origem
) {}
