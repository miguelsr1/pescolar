package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.pescolar.model.Anho;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.MunicipioAledanho;
import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;
import sv.gob.mined.pescolar.repository.CatalogoRepo;

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
    private List<VwCatalogoEntidadEducativa> lstEntidades;

    @Inject
    private CatalogoRepo catalogoRepo;

    @PostConstruct
    public void init() {
        lstAnhos = catalogoRepo.findListByParam(Anho.class, new ArrayList(), "id", false);
        lstMunicipioAledanho = catalogoRepo.findListByParam(MunicipioAledanho.class, new ArrayList());
        lstDepartamentos = catalogoRepo.findListByParam(Departamento.class, new ArrayList(), "id", true);

        lstEntidades = catalogoRepo.findAllEntidades();
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

    public List<VwCatalogoEntidadEducativa> findAllEntidadesByCodigoEntidad(String codigoEntidad) {
        return lstEntidades.stream().filter(entidad -> entidad.toString().startsWith(codigoEntidad)).collect(Collectors.toList());
    }

    public VwCatalogoEntidadEducativa findEntidadByCodigoEntidad(String codigoEntidad) {
        return lstEntidades.stream().filter(entidad -> entidad.getCodigoEntidad().compareTo(codigoEntidad) == 0).findAny().get();
    }
}
