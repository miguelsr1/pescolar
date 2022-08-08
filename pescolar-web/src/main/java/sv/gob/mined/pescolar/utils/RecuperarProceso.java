/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.utils;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.pescolar.model.ProcesoAdquisicion;
import sv.gob.mined.pescolar.web.SessionView;
import sv.gob.mined.pescolar.web.proveedor.interno.ProveedorView;

/**
 *
 * @author misanchez
 */
@Named
@SessionScoped
public class RecuperarProceso implements Serializable {

    //private ProcesoAdquisicion procesoAdquisicion = new ProcesoAdquisicion();
    private String departamento;

    @Inject
    private ProveedorView proveedorView;
    @Inject
    private SessionView sessionView;

    @PostConstruct
    public void init1() {
        recuperarProcesoAdq();
    }

    public void recuperarProcesoAdq() {
        /*if (sessionView.isVariableSession("idEmpresa")) {

            procesoAdquisicion = proveedorView.getAnho().getProcesoAdquisicionList().get(0);
            sessionView.setProceso(procesoAdquisicion);

        } else {
            procesoAdquisicion = sessionView.getProceso();
        }

        if (procesoAdquisicion == null || procesoAdquisicion.getId() == null) {
            JsfUtil.mensajeAlerta("Debe de seleccionar un proceso de adquisición.");
        }*/

        departamento = sessionView.getCodigoDepartamento();
    }

    public ProcesoAdquisicion getProcesoAdquisicion() {
        return sessionView.getProceso();
         
    }

    public String getDepartamento() {
        departamento = sessionView.getCodigoDepartamento();
        return departamento;
    }
}
