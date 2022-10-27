/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.model.dto.contratacion;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author oamartinez
 */
public class ReportPOIBean {
    private Integer numItem;
    private String item;  
    private Integer cantidadRequerida;
    private List<BeanProveedores> listadoProveedores = new LinkedList();
//    private List<Bean> listadoProveedores = new LinkedList<Bean>();

    public Integer getNumItem() {
        return numItem;
    }

    public void setNumItem(Integer numItem) {
        this.numItem = numItem;
    }    
    
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(Integer cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }

    public List<BeanProveedores> getListadoProveedores() {
        if(listadoProveedores == null){
            listadoProveedores = new ArrayList();
        }
        return listadoProveedores;
    }

    public void setListadoProveedores(List<BeanProveedores> listadoProveedores) {
        this.listadoProveedores = listadoProveedores;
    }
        
}
