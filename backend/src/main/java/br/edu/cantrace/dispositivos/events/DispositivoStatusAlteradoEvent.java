package br.edu.cantrace.dispositivos.events;

import br.edu.cantrace.dispositivos.DispositivoStatus;

public class DispositivoStatusAlteradoEvent extends DispositivoEvent {
    private final DispositivoStatus statusAnterior;
    private final DispositivoStatus statusNovo;

    public DispositivoStatusAlteradoEvent(Object source, String dispositivoId, String deviceCode,
                                           DispositivoStatus statusAnterior, DispositivoStatus statusNovo) {
        super(source, dispositivoId, deviceCode);
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
    }

    public DispositivoStatus getStatusAnterior() { return statusAnterior; }
    public DispositivoStatus getStatusNovo() { return statusNovo; }
}
