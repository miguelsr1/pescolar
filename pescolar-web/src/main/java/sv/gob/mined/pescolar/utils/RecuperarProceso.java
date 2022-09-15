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

/**
 *
 * @author misanchez
 */
@Named
@SessionScoped
public class RecuperarProceso implements Serializable {

    private String departamento;

    @Inject
    private SessionView sessionView;

    @PostConstruct
    public void init1() {
        recuperarProcesoAdq();
    }

    public void recuperarProcesoAdq() {
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
