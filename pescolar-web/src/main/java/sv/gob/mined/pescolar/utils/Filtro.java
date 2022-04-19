/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.utils;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author misanchez
 */
@Getter
@Setter
public class Filtro {

    public static final int EQUALS = 1;
    public static final int LIKE = 2;

    private int tipoOperacion;
    private String clave;
    private Object valor;

    public Filtro() {
    }

    public Filtro(int tipoOperacion, String clave, Object valor) {
        this.tipoOperacion = tipoOperacion;
        this.clave = clave;
        this.valor = valor;
    }

}
