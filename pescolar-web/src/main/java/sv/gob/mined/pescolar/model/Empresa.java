package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Table(name = "EMPRESA")
@Entity
public class Empresa implements Serializable {

    @OneToMany(mappedBy = "idEmpresa", fetch = FetchType.LAZY)
    private List<PreciosRefRubroEmp> preciosRefRubroEmpList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresa", fetch = FetchType.LAZY)
    private List<DetRubroMuestraIntere> detRubroMuestraIntereList;
    @Id
    @Column(name = "ID_EMPRESA", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_PERSONA", nullable = false)
    private Persona idPersona;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TIPO_EMPRESA")
    private TipoEmpresa idTipoEmpresa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERSONERIA")
    private TipoPersoneria idPersoneria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ESTADO_REGISTRO")
    private EstadoRegistro idEstadoRegistro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_MUNICIPIO")
    private Municipio idMunicipio;

    @Column(name = "NOMBRE_COMERCIAL", length = 254)
    private String nombreComercial;

    @Column(name = "RAZON_SOCIAL", nullable = false, length = 254)
    private String razonSocial;

    @Column(name = "NUMERO_NIT", nullable = false, length = 150)
    private String numeroNit;

    @Column(name = "DIRECCION_COMPLETA", nullable = false, length = 500)
    private String direccionCompleta;

    @Column(name = "NUMERO_IVA", length = 20)
    private String numeroIva;

    @Column(name = "NUMERO_REG_COMERCIO", length = 75)
    private String numeroRegComercio;

    @Column(name = "CORREO_ELECTRONICO", length = 100)
    private String correoElectronico;

    @Column(name = "TELEFONOS", length = 75)
    private String telefonos;

    @Column(name = "NUMERO_CELULAR", length = 75)
    private String numeroCelular;

    @Column(name = "FAX", length = 75)
    private String fax;

    @Column(name = "ES_CONTRIBUYENTE")
    private Boolean esContribuyente;

    @Column(name = "USUARIO_INSERCION", nullable = false, length = 25)
    private String usuarioInsercion;

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

    @Column(name = "DISTRIBUIDOR")
    private Long distribuidor;

    @Column(name = "MOSTRAR_LEYENDA")
    private Long mostrarLeyenda;

    @Column(name = "NUMERO_CUENTA", length = 30)
    private String numeroCuenta;

    @Column(name = "CODIGO_CANTON", length = 2)
    private String codigoCanton;

    @Column(name = "DESEA_INSCRIBIRSE")
    private Boolean deseaInscribirse;

    public Boolean getDeseaInscribirse() {
        return deseaInscribirse;
    }

    public void setDeseaInscribirse(Boolean deseaInscribirse) {
        this.deseaInscribirse = deseaInscribirse;
    }

    public String getCodigoCanton() {
        return codigoCanton;
    }

    public void setCodigoCanton(String codigoCanton) {
        this.codigoCanton = codigoCanton;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Long getMostrarLeyenda() {
        return mostrarLeyenda;
    }

    public void setMostrarLeyenda(Long mostrarLeyenda) {
        this.mostrarLeyenda = mostrarLeyenda;
    }

    public Long getDistribuidor() {
        return distribuidor;
    }

    public void setDistribuidor(Long distribuidor) {
        this.distribuidor = distribuidor;
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

    public String getUsuarioInsercion() {
        return usuarioInsercion;
    }

    public void setUsuarioInsercion(String usuarioInsercion) {
        this.usuarioInsercion = usuarioInsercion;
    }

    public Boolean getEsContribuyente() {
        return esContribuyente;
    }

    public void setEsContribuyente(Boolean esContribuyente) {
        this.esContribuyente = esContribuyente;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNumeroRegComercio() {
        return numeroRegComercio;
    }

    public void setNumeroRegComercio(String numeroRegComercio) {
        this.numeroRegComercio = numeroRegComercio;
    }

    public String getNumeroIva() {
        return numeroIva;
    }

    public void setNumeroIva(String numeroIva) {
        this.numeroIva = numeroIva;
    }

    public String getDireccionCompleta() {
        return direccionCompleta;
    }

    public void setDireccionCompleta(String direccionCompleta) {
        this.direccionCompleta = direccionCompleta;
    }

    public String getNumeroNit() {
        return numeroNit;
    }

    public void setNumeroNit(String numeroNit) {
        this.numeroNit = numeroNit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public Municipio getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Municipio idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public EstadoRegistro getIdEstadoRegistro() {
        return idEstadoRegistro;
    }

    public void setIdEstadoRegistro(EstadoRegistro idEstadoRegistro) {
        this.idEstadoRegistro = idEstadoRegistro;
    }

    public TipoPersoneria getIdPersoneria() {
        return idPersoneria;
    }

    public void setIdPersoneria(TipoPersoneria idPersoneria) {
        this.idPersoneria = idPersoneria;
    }

    public TipoEmpresa getIdTipoEmpresa() {
        return idTipoEmpresa;
    }

    public void setIdTipoEmpresa(TipoEmpresa idTipoEmpresa) {
        this.idTipoEmpresa = idTipoEmpresa;
    }

    public Persona getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Persona idPersona) {
        this.idPersona = idPersona;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PreciosRefRubroEmp> getPreciosRefRubroEmpList() {
        return preciosRefRubroEmpList;
    }

    public void setPreciosRefRubroEmpList(List<PreciosRefRubroEmp> preciosRefRubroEmpList) {
        this.preciosRefRubroEmpList = preciosRefRubroEmpList;
    }

    public List<DetRubroMuestraIntere> getDetRubroMuestraIntereList() {
        return detRubroMuestraIntereList;
    }

    public void setDetRubroMuestraIntereList(List<DetRubroMuestraIntere> detRubroMuestraIntereList) {
        this.detRubroMuestraIntereList = detRubroMuestraIntereList;
    }

}
