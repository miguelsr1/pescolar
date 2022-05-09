/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.pescolar.model.Anho;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.MunicipioAledanho;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.utils.db.Filtro;

/**
 *
 * @author misanchez
 */
@SuppressWarnings("serial")
@ApplicationScoped
@Named
public class RepositorioAplicacionView implements Serializable {

    private List<Departamento> lstDepartamentos;
    private List<Anho> lstAnhos;
    private List<MunicipioAledanho> lstMunicipioAledanho;

    @Inject
    private CatalogoRepo catalogoRepo;

    @PostConstruct
    public void init() {
        lstAnhos = catalogoRepo.findListByParam(Anho.class, new ArrayList(), "id", false);
        lstMunicipioAledanho = catalogoRepo.findListByParam(MunicipioAledanho.class, new ArrayList());
        lstDepartamentos = catalogoRepo.findListByParam(Departamento.class, new ArrayList(), "id", true);
    }

    public List<Departamento> getLstDepartamentos() {
        return lstDepartamentos;
    }

    public List<Anho> getLstAnhos() {
        return lstAnhos;
    }

    public List<MunicipioAledanho> getLstMunicipioAledanho() {
        return lstMunicipioAledanho;
    }

    public MunicipioAledanho findIdMunicipios(Long idMunicipio) {
        Optional<MunicipioAledanho> municipio = getLstMunicipioAledanho().stream().parallel().
                filter(munA -> munA.getIdMunicipio().compareTo(idMunicipio) == 0).findAny();
        if (municipio.isPresent()) {
            return municipio.get();
        } else {
            return null;
        }
    }
}
