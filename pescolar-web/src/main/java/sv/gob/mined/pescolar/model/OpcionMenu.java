/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author misanchez
 */
@Entity
@Table(name = "OPCION_MENU")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpcionMenu.findAll", query = "SELECT o FROM OpcionMenu o"),
    @NamedQuery(name = "OpcionMenu.findByIdOpcMenu", query = "SELECT o FROM OpcionMenu o WHERE o.idOpcMenu = :idOpcMenu"),
    @NamedQuery(name = "OpcionMenu.findByNombreOpcion", query = "SELECT o FROM OpcionMenu o WHERE o.nombreOpcion = :nombreOpcion"),
    @NamedQuery(name = "OpcionMenu.findByNombrePanel", query = "SELECT o FROM OpcionMenu o WHERE o.nombrePanel = :nombrePanel"),
    @NamedQuery(name = "OpcionMenu.findByIcono", query = "SELECT o FROM OpcionMenu o WHERE o.icono = :icono"),
    @NamedQuery(name = "OpcionMenu.findByOrden", query = "SELECT o FROM OpcionMenu o WHERE o.orden = :orden"),
    @NamedQuery(name = "OpcionMenu.findByCodPantalla", query = "SELECT o FROM OpcionMenu o WHERE o.codPantalla = :codPantalla")})
public class OpcionMenu implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_OPC_MENU")
    private Long idOpcMenu;
    @Size(max = 100)
    @Column(name = "NOMBRE_OPCION")
    private String nombreOpcion;
    @Size(max = 100)
    @Column(name = "NOMBRE_PANEL")
    private String nombrePanel;
    @Size(max = 100)
    @Column(name = "ICONO")
    private String icono;
    @Column(name = "ORDEN")
    private Integer orden;
    @Size(max = 50)
    @Column(name = "COD_PANTALLA")
    private String codPantalla;
    @JoinColumn(name = "APP", referencedColumnName = "ID_MODULO")
    @ManyToOne(fetch = FetchType.LAZY)
    private Modulo app;
    @OneToMany(mappedBy = "opcIdOpcMenu", fetch = FetchType.LAZY)
    private List<OpcionMenu> opcionMenuList;
    @JoinColumn(name = "OPC_ID_OPC_MENU", referencedColumnName = "ID_OPC_MENU")
    @ManyToOne(fetch = FetchType.LAZY)
    private OpcionMenu opcIdOpcMenu;

    public OpcionMenu() {
    }

    public OpcionMenu(Long idOpcMenu) {
        this.idOpcMenu = idOpcMenu;
    }

    public Long getIdOpcMenu() {
        return idOpcMenu;
    }

    public void setIdOpcMenu(Long idOpcMenu) {
        this.idOpcMenu = idOpcMenu;
    }

    public String getNombreOpcion() {
        return nombreOpcion;
    }

    public void setNombreOpcion(String nombreOpcion) {
        this.nombreOpcion = nombreOpcion;
    }

    public String getNombrePanel() {
        return nombrePanel;
    }

    public void setNombrePanel(String nombrePanel) {
        this.nombrePanel = nombrePanel;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getCodPantalla() {
        return codPantalla;
    }

    public void setCodPantalla(String codPantalla) {
        this.codPantalla = codPantalla;
    }

    public Modulo getApp() {
        return app;
    }

    public void setApp(Modulo app) {
        this.app = app;
    }

    @XmlTransient
    public List<OpcionMenu> getOpcionMenuList() {
        return opcionMenuList;
    }

    public void setOpcionMenuList(List<OpcionMenu> opcionMenuList) {
        this.opcionMenuList = opcionMenuList;
    }

    public OpcionMenu getOpcIdOpcMenu() {
        return opcIdOpcMenu;
    }

    public void setOpcIdOpcMenu(OpcionMenu opcIdOpcMenu) {
        this.opcIdOpcMenu = opcIdOpcMenu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOpcMenu != null ? idOpcMenu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OpcionMenu)) {
            return false;
        }
        OpcionMenu other = (OpcionMenu) object;
        if ((this.idOpcMenu == null && other.idOpcMenu != null) || (this.idOpcMenu != null && !this.idOpcMenu.equals(other.idOpcMenu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.pescolar.model.OpcionMenu[ idOpcMenu=" + idOpcMenu + " ]";
    }
    
}
