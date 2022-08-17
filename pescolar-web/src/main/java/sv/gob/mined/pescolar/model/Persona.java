package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Table(name = "PERSONA")
@Entity
public class Persona implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPersona", fetch = FetchType.LAZY)
    private List<Empresa> empresaList;

    @Id
    @Column(name = "ID_PERSONA", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_GENERO")
    private Genero idGenero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_DOC_LEGAL")
    private TiposDocLegal idDocLegal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_MUNICIPIO")
    private Municipio idMunicipio;

    @Column(name = "PRIMER_APELLIDO", length = 150)
    private String primerApellido;

    @Column(name = "SEGUNDO_APELLIDO", length = 500)
    private String segundoApellido;

    @Column(name = "PRIMER_NOMBRE", length = 150)
    private String primerNombre;

    @Column(name = "SEGUNDO_NOMBRE", length = 150)
    private String segundoNombre;

    @Column(name = "FECHA_NACIMIENTO")
    private LocalDate fechaNacimiento;

    @Column(name = "NUMERO_DUI", length = 150)
    private String numeroDui;

    @Column(name = "NUMERO_NIT", length = 150)
    private String numeroNit;

    @Column(name = "NUMERO_TELEFONO", length = 150)
    private String numeroTelefono;

    @Column(name = "NUMERO_CELULAR", length = 150)
    private String numeroCelular;

    @Column(name = "DOMICILIO", length = 500)
    private String domicilio;

    @Column(name = "PROFESION", length = 150)
    private String profesion;

    @Column(name = "ACASADA", length = 150)
    private String acasada;

    @Column(name = "EMAIL", length = 200)
    private String email;

    @Column(name = "INACTIVO")
    private Long inactivo;

    @Column(name = "LECTURA_FIRMA", length = 254)
    private String lecturaFirma;

    @Column(name = "NUMERO_DOCUMENTO_LEGAL", length = 254)
    private String numeroDocumentoLegal;

    @Column(name = "USUARIO_CREACION", length = 25)
    private String usuarioCreacion;

    @Column(name = "USUARIO", length = 25)
    private String usuario;

    @Column(name = "CLAVE_ACCESO", length = 100)
    private String claveAcceso;

    @Column(name = "FECHA_INSERCION", nullable = false)
    private LocalDate fechaInsercion;

    @Column(name = "USUARIO_MODIFICACION", length = 25)
    private String usuarioModificacion;

    @Column(name = "FECHA_MODIFICACION")
    private LocalDate fechaModificacion;

    @Column(name = "FECHA_ELIMINACION")
    private LocalDate fechaEliminacion;

    @Column(name = "ESTADO_ELIMINACION", nullable = false)
    private Long estadoEliminacion;

    @Column(name = "URL_IMAGEN", length = 200)
    private String urlImagen;

    @Column(name = "URL_DUI", length = 200)
    private String urlDui;

    @Column(name = "URL_NIT", length = 200)
    private String urlNit;

    @Column(name = "URL_DECLARACION", length = 200)
    private String urlDeclaracion;

    @Column(name = "NUM_TELEFONO3", length = 9)
    private String numTelefono3;

    @Column(name = "NUM_TELEFONO2", length = 9)
    private String numTelefono2;

    @Column(name = "CODIGO_CANTON", length = 2)
    private String codigoCanton;

    @OneToMany(mappedBy = "idPersona", fetch = FetchType.LAZY)
    private List<Usuario> usuarioList;

    @Transient
    private String nombreCompleto;

    public String getCodigoCanton() {
        return codigoCanton;
    }

    public void setCodigoCanton(String codigoCanton) {
        this.codigoCanton = codigoCanton;
    }

    public String getNumTelefono2() {
        return numTelefono2;
    }

    public void setNumTelefono2(String numTelefono2) {
        this.numTelefono2 = numTelefono2;
    }

    public String getNumTelefono3() {
        return numTelefono3;
    }

    public void setNumTelefono3(String numTelefono3) {
        this.numTelefono3 = numTelefono3;
    }

    public String getUrlDeclaracion() {
        return urlDeclaracion;
    }

    public void setUrlDeclaracion(String urlDeclaracion) {
        this.urlDeclaracion = urlDeclaracion;
    }

    public String getUrlNit() {
        return urlNit;
    }

    public void setUrlNit(String urlNit) {
        this.urlNit = urlNit;
    }

    public String getUrlDui() {
        return urlDui;
    }

    public void setUrlDui(String urlDui) {
        this.urlDui = urlDui;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Long getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(Long estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    public LocalDate getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(LocalDate fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public LocalDate getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(LocalDate fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getNumeroDocumentoLegal() {
        return numeroDocumentoLegal;
    }

    public void setNumeroDocumentoLegal(String numeroDocumentoLegal) {
        this.numeroDocumentoLegal = numeroDocumentoLegal;
    }

    public String getLecturaFirma() {
        return lecturaFirma;
    }

    public void setLecturaFirma(String lecturaFirma) {
        this.lecturaFirma = lecturaFirma;
    }

    public Long getInactivo() {
        return inactivo;
    }

    public void setInactivo(Long inactivo) {
        this.inactivo = inactivo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAcasada() {
        return acasada;
    }

    public void setAcasada(String acasada) {
        this.acasada = acasada;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getNumeroNit() {
        return numeroNit;
    }

    public void setNumeroNit(String numeroNit) {
        this.numeroNit = numeroNit;
    }

    public String getNumeroDui() {
        return numeroDui;
    }

    public void setNumeroDui(String numeroDui) {
        this.numeroDui = numeroDui;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public Municipio getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Municipio idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public TiposDocLegal getIdDocLegal() {
        return idDocLegal;
    }

    public void setIdDocLegal(TiposDocLegal idDocLegal) {
        this.idDocLegal = idDocLegal;
    }

    public Genero getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(Genero idGenero) {
        this.idGenero = idGenero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String getApellidos() {
        if (acasada == null) {
            if (segundoApellido == null) {
                if (primerApellido == null) {
                    return "";
                } else {
                    return primerApellido;
                }
            } else {
                return primerApellido + " " + segundoApellido;
            }
        } else {
            return primerApellido + " " + acasada;
        }
    }

    private String getNombres() {
        if (segundoNombre == null) {
            if (primerNombre == null) {
                return "";
            } else {
                return primerNombre;
            }
        } else {
            return primerNombre + " " + segundoNombre;
        }
    }

    public String getNombreCompletoProveedor() {
        return getNombres() + " " + getApellidos();
    }

    public String getNombreCompleto() {
        if (getUsuario().isEmpty()) {
            return "";
        } else {
            return getNombres() + " " + getApellidos() + " (" + getUsuario() + ")";
        }
    }

    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public List<Empresa> getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List<Empresa> empresaList) {
        this.empresaList = empresaList;
    }
}
