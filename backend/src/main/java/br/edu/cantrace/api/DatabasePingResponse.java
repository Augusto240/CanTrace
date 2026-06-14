package br.edu.cantrace.api;

public record DatabasePingResponse(
    String status,
    String database,
    String databaseUser,
    String databaseTime,
    String apiTime
) {
}
