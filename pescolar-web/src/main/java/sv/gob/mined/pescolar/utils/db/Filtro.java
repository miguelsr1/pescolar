package sv.gob.mined.pescolar.utils.db;

import lombok.Getter;
import lombok.Setter;
import sv.gob.mined.pescolar.utils.enums.TipoOperador;

/**
 *
 * @author misanchez
 */
@Getter
@Setter
public class Filtro {

    private TipoOperador tipoOperacion;
    private String clave;
    private Object valor;
    private Class clazz;

    public Filtro() {
    }

    public Filtro(TipoOperador tipoOperacion, String clave, Object valor) {
        this.tipoOperacion = tipoOperacion;
        this.clave = clave;
        this.valor = valor;
    }

    public Filtro(TipoOperador tipoOperacion, String clave, Object valor, Class clazz) {
        this.tipoOperacion = tipoOperacion;
        this.clave = clave;
        this.valor = valor;
        this.clazz = clazz;
    }

}
