/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.dhcomitesso.view;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.dhcomitesso.model.dhcsso.Empleado;
import sv.gob.mined.dhcomitesso.model.dhcsso.ProcesoEleccion;
import sv.gob.mined.dhcomitesso.model.dhcsso.Users;
import sv.gob.mined.dhcomitesso.repository.CandidatoRepo;
import sv.gob.mined.dhcomitesso.repository.LoginRepo;
import sv.gob.mined.dhcomitesso.util.VarSession;

@Named
@SessionScoped
public class ParametrosSesionView implements Serializable {

    private Boolean votoRealizado = false;
    private Boolean admin = false;
    private Empleado empleado;
    private Boolean tiempoFinalizado = false;
    private Boolean iniciado = false;

    @Inject
    private LoginRepo loginRepo;
    @Inject
    private CandidatoRepo candidatoRepo;
    private ProcesoEleccion procesoEleccion;

    @PostConstruct
    public void init() {
        admin = ((Users) VarSession.getVariableSession("pUsuario")).getUsername().equals("NRA000000");
        empleado = loginRepo.findEmpleadoByCodigo(((Users) VarSession.getVariableSession("pUsuario")).getUsername());
        votoRealizado = candidatoRepo.isVotoRealizado(1, empleado.getId());
        procesoEleccion = candidatoRepo.findProcesoByPk();
//        if (procesoEleccion.getHabilitarProceso()) {
//            iniciado = procesoEleccion.getHabilitarProceso();
//        } else {
//            iniciado = false;
//        }
    }

    public Boolean getTiempoFinalizado() {
        return tiempoFinalizado;
    }

    public Boolean getIniciado() {
        return iniciado;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
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

    public long getTiempoRestante() {
        long tiempo = 0l;
        if (procesoEleccion.getFechaInsercion() != null && procesoEleccion.getHoras() != null && procesoEleccion.getHabilitarProceso()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(procesoEleccion.getFechaInicio());
            calendar.add(Calendar.HOUR_OF_DAY, procesoEleccion.getHoras());

            Date limite = calendar.getTime();

            tiempoFinalizado = (limite.getTime() < (new Date()).getTime());

            if (tiempoFinalizado) {
            } else {
                long diffInMillies = Math.abs(limite.getTime() - (new Date()).getTime());
                tiempo = TimeUnit.MILLISECONDS.toSeconds(diffInMillies);
            }
        }
        return tiempo;
    }

}
