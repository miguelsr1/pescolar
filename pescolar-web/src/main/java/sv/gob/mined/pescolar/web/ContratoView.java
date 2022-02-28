/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.pescolar.model.ContratosOrdenesCompra;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.ContratoRepo;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ContratoView implements Serializable {

    private String ciudadFirma;
    private ContratosOrdenesCompra contrato;

    private List<Integer> lstSelectDocumentosImp = new ArrayList();
    private List<SelectItem> lstDocumentosImp = new ArrayList();

    @Inject
    private ContratoRepo contratoRepo;
    @Inject
    private CatalogoRepo catalogoRepo;

    public ContratoView() {
    }

    @PostConstruct
    public void init() {
        contrato = contratoRepo.findContratoByIdResAdj(246831l);
        Long rubro = contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdRubroAdq().getId();
        
        lstDocumentosImp = catalogoRepo.getLstDocumentosImp(rubro.intValue()== 1 || rubro.intValue() == 4 || rubro.intValue() == 5, contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq().getIdAnho().getId().intValue(), lstDocumentosImp);
    }

    public ContratosOrdenesCompra getContrato() {
        return contrato;
    }

    public String getCiudadFirma() {
        return ciudadFirma;
    }

    public List<SelectItem> getLstDocumentosImp() {
        return lstDocumentosImp;
    }

    public void setLstDocumentosImp(List<SelectItem> lstDocumentosImp) {
        this.lstDocumentosImp = lstDocumentosImp;
    }

    public List<Integer> getLstSelectDocumentosImp() {
        return lstSelectDocumentosImp;
    }

    public void setLstSelectDocumentosImp(List<Integer> lstSelectDocumentosImp) {
        this.lstSelectDocumentosImp = lstSelectDocumentosImp;
    }

    public void imprimirAnalisisEconomico() {
    }
}
