/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Carlos Quintanilla
 */
@Table(name = "TIPO_USU_OPC_MENU")
@Entity
public class TipoUsuOpcMenu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_TIPO_OPC", nullable = false)
    @GeneratedValue(generator = "SEQ_TIPO_USU_OPC_MENU", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "SEQ_TIPO_USU_OPC_MENU", name = "SEQ_TIPO_USU_OPC_MENU", allocationSize = 1, initialValue = 1)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TIPO_USUARIO")
    private TipoUsuario idTipoUsuario;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_OPC_MENU")
    private OpcionMenu idOpcMenu;

    public TipoUsuOpcMenu() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoUsuario getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(TipoUsuario idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public OpcionMenu getIdOpcMenu() {
        return idOpcMenu;
    }

    public void setIdOpcMenu(OpcionMenu idOpcMenu) {
        this.idOpcMenu = idOpcMenu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoUsuOpcMenu)) {
            return false;
        }
        TipoUsuOpcMenu other = (TipoUsuOpcMenu) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "sv.gob.mined.pescolar.model.TipoUsuOpcMenu[ id=" + getId() + " ]";
    }

}
