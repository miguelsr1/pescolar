/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.dhcomitesso.model.dhcsso.dto;

/**
 *
 * @author misanchez
 */
public class Postulante {

    private Integer correlativo;
    private String nombre;
    private Integer votos;
    
    public Postulante(Integer correlativo, String nombre, Integer votos) {
        this.correlativo = correlativo;
        this.nombre = nombre;
        this.votos = votos;
    }
    

    public Integer getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(Integer correlativo) {
        this.correlativo = correlativo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getVotos() {
        return votos;
    }

    public void setVotos(Integer votos) {
        this.votos = votos;
    }

}
