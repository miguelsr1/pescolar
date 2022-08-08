package sv.gob.mined.pescolar.utils;

import javax.inject.Inject;


/**
 *
 * @author misanchez
 */
public class RecuperarProcesoUtil {

    @Inject
    private RecuperarProceso recuperarProceso;

    public RecuperarProceso getRecuperarProceso() {
        return recuperarProceso;
    }

    public void setRecuperarProceso(RecuperarProceso recuperarProceso) {
        this.recuperarProceso = recuperarProceso;
    }
}
