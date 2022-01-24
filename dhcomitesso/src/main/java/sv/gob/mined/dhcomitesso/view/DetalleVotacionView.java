/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.dhcomitesso.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.CandidatoView;
import sv.gob.mined.dhcomitesso.repository.CandidatoRepo;

/**
 *
 * @author misanchez
 */
@SuppressWarnings("serial")
@Named
@ViewScoped
public class DetalleVotacionView implements Serializable {

    private String idCandidato;

    private List<sv.gob.mined.dhcomitesso.model.dhcsso.view.CandidatoView> lstCandidatos = new ArrayList();

    @Inject
    private CandidatoRepo candidatoRepo;

    @PostConstruct
    public void init() {
        lstCandidatos = candidatoRepo.findCandidatos();
    }

    public List<CandidatoView> getLstCandidatos() {
        return lstCandidatos;
    }

    public String getIdCandidato() {
        return idCandidato;
    }

    public void setIdCandidato(String idCandidato) {
        this.idCandidato = idCandidato;
    }

    public void guardar() {
        candidatoRepo.guardarVoto(1, idCandidato);
    }
}
