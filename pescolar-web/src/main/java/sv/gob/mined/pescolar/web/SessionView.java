/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.SecurityContext;
import sv.gob.mined.pescolar.model.Persona;
import sv.gob.mined.pescolar.model.Usuario;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.utils.Filtro;

/**
 *
 * @author misanchez
 */
@SuppressWarnings("serial")
@SessionScoped
@Named
public class SessionView implements Serializable {

    private Usuario usuario;

    @Inject
    private SecurityContext securityContext;
    @Inject
    private CatalogoRepo catalogoRepo;

    @PostConstruct
    public void init() {
        List<Filtro> params = new ArrayList();
        params.add(new Filtro(Filtro.EQUALS, "idPersona,usuario", securityContext.getCallerPrincipal().getName()));
        usuario = ((Usuario) catalogoRepo.findListByParam(Usuario.class, params).get(0));
    }

    public Usuario getUsuario() {
        return usuario;
    }

}
