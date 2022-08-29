package sv.gob.mined.pescolar.repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import sv.gob.mined.pescolar.model.Empresa;

/**
 *
 * @author misanchez
 */
@Named
@ApplicationScoped
public class CatalogoGlobal implements Serializable {

    private Map<Long, Empresa> empresasAsMap;
    private Map<String, String> parameters = new HashMap<>();

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Inject
    private EmpresaRepo emRepo;
    private List<Empresa> lstEmpresa;

    @PostConstruct
    public void init() {
        lstEmpresa = emRepo.findAll();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
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

    public String encriptar(String cadenaCaracteres) {
        passwordHash.initialize(parameters);
        return passwordHash.generate(cadenaCaracteres.toCharArray());
    }
}
