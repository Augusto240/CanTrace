package br.edu.cantrace.dashboard;

import java.util.List;

public record DashboardDTO(
    int totalDispositivosAtivo,
    int totalDispositivosInativo,
    int totalDispositivosManutencao,
    int totalDispositivosOffline,
    long totalLeituras24h,
    long totalLeituras7d,
    Double mediaTemperatura24h,
    Double mediaUmidade24h,
    Double mediaLuminosidade24h,
    long totalAlertasAtivo,
    long totalAlertasResolvido,
    long totalAlertasIgnorado,
    long totalRegistrosAuditoria,
    List<DashboardLeituraResumoDTO> ultimasLeituras
) {}
