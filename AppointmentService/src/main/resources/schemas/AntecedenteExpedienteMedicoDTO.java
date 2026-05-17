
/**
 *
 * @author Leonardo Flores Leyva
 */
public class AntecedenteExpedienteMedicoDTO {
    
    private Long id;    
    private String especificacion;    
    private AntecedenteDTO antecedente;    
    private ExpedienteMedicoDTO expedienteMedico;

    public AntecedenteExpedienteMedicoDTO() {}

    public AntecedenteExpedienteMedicoDTO(
            String especificacion, 
            AntecedenteDTO antecedente, 
            ExpedienteMedicoDTO expedienteMedico
    ) {
        this.especificacion = especificacion;
        this.antecedente = antecedente;
        this.expedienteMedico = expedienteMedico;
    }

    public AntecedenteExpedienteMedicoDTO(
            Long id, 
            String especificacion, 
            AntecedenteDTO antecedente, 
            ExpedienteMedicoDTO expedienteMedico
    ) {
        this.id = id;
        this.especificacion = especificacion;
        this.antecedente = antecedente;
        this.expedienteMedico = expedienteMedico;
    }

    public Long getId() {return id;}

    public String getEspecificacion() {return especificacion;}

    public AntecedenteDTO getAntecedente() {return antecedente;}

    public ExpedienteMedicoDTO getExpedienteMedico() {return expedienteMedico;}

    public void setId(Long id) {this.id = id;}

    public void setEspecificacion(String especificacion) {this.especificacion = especificacion;}

    public void setAntecedente(AntecedenteDTO antecedente) {this.antecedente = antecedente;}

    public void setExpedienteMedico(ExpedienteMedicoDTO expedienteMedico) {this.expedienteMedico = expedienteMedico;}
}