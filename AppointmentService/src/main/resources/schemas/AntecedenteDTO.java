/**
 *
 * @author Leonardo Flores Leyva
 */
public class AntecedenteDTO {
    
    private Integer id;    
    private String antecedente;    
    private String tipo;

    public AntecedenteDTO() {}

    public AntecedenteDTO(String antecedente, String tipo) {
        this.antecedente = antecedente;
        this.tipo = tipo;
    }

    public AntecedenteDTO(Integer id, String antecedente, String tipo) {
        this.id = id;
        this.antecedente = antecedente;
        this.tipo = tipo;
    }

    public Integer getId() {return id;}

    public String getAntecedente() {return antecedente;}

    public String getTipo() {return tipo;}

    public void setId(Integer id) {this.id = id;}

    public void setAntecedente(String antecedente) {this.antecedente = antecedente;}

    public void setTipo(String tipo) {this.tipo = tipo;}
}