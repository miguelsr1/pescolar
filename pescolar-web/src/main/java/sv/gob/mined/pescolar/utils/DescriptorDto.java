package sv.gob.mined.pescolar.utils;

/**
 *
 * @author misanchez
 */
public class DescriptorDto {

    private String atributo;
    private String valor;

    public DescriptorDto() {
    }

    public DescriptorDto(int atributo, String valor) {
        this.atributo = String.valueOf(atributo);
        this.valor = valor;
    }

    public DescriptorDto(String atributo, String valor) {
        this.atributo = atributo;
        this.valor = valor;
    }

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return atributo;
    }
}
