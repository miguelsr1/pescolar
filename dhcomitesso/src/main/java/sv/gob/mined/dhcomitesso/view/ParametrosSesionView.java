/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.dhcomitesso.view;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.SecurityContext;
import sv.gob.mined.dhcomitesso.model.dhcsso.Empleado;
import sv.gob.mined.dhcomitesso.repository.CandidatoRepo;
import sv.gob.mined.dhcomitesso.repository.LoginRepo;
import sv.gob.mined.dhcomitesso.util.VarSession;

@Named
@SessionScoped
public class ParametrosSesionView implements Serializable {

    private Boolean votoRealizado = false;
    private Empleado empleado;

    @Inject
    private LoginRepo loginRepo;
    @Inject
    private CandidatoRepo candidatoRepo;

    @Inject
    private SecurityContext ctx;

    @PostConstruct
    public void init() {
        empleado = loginRepo.findEmpleadoByCodigo(ctx.getCallerPrincipal().getName());
        votoRealizado = candidatoRepo.isVotoRealizado(1, empleado.getId());
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Boolean getVotoRealizado() {
        return votoRealizado;
    }

    public void setVotoRealizado(Boolean votoRealizado) {
        this.votoRealizado = votoRealizado;
    }

}
