/**
 *
 * @author Leonardo Flores Leyva
 */
public class AtributoFisicoDTO {
    
    private Integer id;    
    private String atributo;    
    private String tipo;

    public AtributoFisicoDTO() {}

    public AtributoFisicoDTO(String atributo, String tipo) {
        this.atributo = atributo;
        this.tipo = tipo;
    }

    public AtributoFisicoDTO(Integer id, String atributo, String tipo) {
        this.id = id;
        this.atributo = atributo;
        this.tipo = tipo;
    }

    public Integer getId() {return id;}

    public String getAtributo() {return atributo;}

    public String getTipo() {return tipo;}

    public void setId(Integer id) {this.id = id;}

    public void setAtributo(String atributo) {this.atributo = atributo;}

    public void setTipo(String tipo) {this.tipo = tipo;}
}