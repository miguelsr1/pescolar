/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.pescolar.model.Anho;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.repository.CatalogoRepo;

/**
 *
 * @author misanchez
 */
@SuppressWarnings("serial")
@ApplicationScoped
@Named
public class RepositorioAplicacionView implements Serializable {

    @Inject
    private CatalogoRepo catalogoRepo;

    public List<Departamento> getLstDepartamentos() {
        return catalogoRepo.findListByParam(Departamento.class, new ArrayList(), "id", true);
    }

    public List<Anho> getLstAnhos() {
        return catalogoRepo.findListByParam(Anho.class, new ArrayList(), "id", false);
    }

}
