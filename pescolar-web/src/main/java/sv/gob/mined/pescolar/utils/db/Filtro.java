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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Filtro filtro;

        private Builder() {
            filtro = new Filtro();
        }

        public Builder crearFiltro(TipoOperador tipo, String clave, Object valor) {
            filtro.setTipoOperacion(tipo);
            filtro.setClave(clave);
            filtro.setValor(valor);
            return this;
        }

        public Builder tipoOperador(TipoOperador tipo) {
            filtro.setTipoOperacion(tipo);
            return this;
        }

        public Builder clave(String clave) {
            filtro.setClave(clave);
            return this;
        }

        public Builder valor(Object valor) {
            filtro.setValor(valor);
            return this;
        }

        public Builder nombreClase(Class clazz) {
            filtro.setClazz(clazz);
            return this;
        }

        public Filtro build() {
            return filtro;
        }

    }
}
