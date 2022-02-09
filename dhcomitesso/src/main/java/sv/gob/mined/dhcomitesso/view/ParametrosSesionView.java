/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.dhcomitesso.view;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
    
    @PostConstruct
    public void init() {
        if (VarSession.isVariableSession("idEmpleado")) {
            empleado = loginRepo.findEmpleadoByPk(Integer.parseInt(VarSession.getVariableSession("idEmpleado").toString()));
            votoRealizado = candidatoRepo.isVotoRealizado(1, empleado.getId());
        }
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
