package sv.gob.mined.pescolar.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.pescolar.model.Empresa;

/**
 *
 * @author misanchez
 */
@Named
@ApplicationScoped
public class CatalogoGlobal implements Serializable {

    private Map<Long, Empresa> empresasAsMap;

    @Inject
    private EmpresaRepo emRepo;
    private List<Empresa> lstEmpresa;

    @PostConstruct
    public void init() {
        lstEmpresa = emRepo.findAll();
    }

    public List<Empresa> getLstEmpresa() {
        return lstEmpresa;
    }

    public Map<Long, Empresa> getEmpresasAsMap() {
        if (empresasAsMap == null) {
            empresasAsMap = getLstEmpresa().stream().collect(Collectors.toMap(Empresa::getId, empresa -> empresa));
        }
        return empresasAsMap;
    }
}
