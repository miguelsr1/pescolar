package sv.gob.mined.pescolar.web.proveedor.interno;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import sv.gob.mined.pescolar.model.Anho;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.repository.AnhoRepo;
import sv.gob.mined.pescolar.repository.EmpresaRepo;
import sv.gob.mined.pescolar.web.SessionView;

/**
 *
 * @author MISanchez
 */
@Named
@SessionScoped
public class ProveedorView implements Serializable {

    private Long idAnho;
    private Anho anho = new Anho();
    private Empresa empresa = new Empresa();

    @Inject
    private AnhoRepo anhoRepo;
    @Inject
    private EmpresaRepo empresaRepo;
    @Inject
    private SessionView sessionView;

    @PostConstruct
    public void ini() {
        if (sessionView.isVariableSession("idEmpresa")) {
            anho = (Anho) anhoRepo.findListByParam(new ArrayList(), "id", Boolean.FALSE).get(0);
            idAnho = anho.getId();
        }
    }

    public Empresa getEmpresa() {
        if (sessionView.isVariableSession("idEmpresa")) {
            empresa = empresaRepo.findByPk((Long) sessionView.getVariableSession("idEmpresa"));
        }
        return empresa;
    }

    public Long getIdAnho() {
        return idAnho;
    }

    public void setIdAnho(Long idAnho) {
        this.idAnho = idAnho;
    }

    public Anho getAnho() {
        return anho;
    }

    public void actualizarVista() throws IOException {
        anho = anhoRepo.findByPk(idAnho);

        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = req.getRequestURL().toString();

        FacesContext.getCurrentInstance().getExternalContext().redirect(url.split("/")[(url.split("/").length - 1)]);
    }

}
