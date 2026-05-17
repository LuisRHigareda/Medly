
/**
 *
 * @author Leonardo Flores Leyva
 */
public class AtributoFisicoExpedienteMedicoDTO {
    
    private Integer id;    
    private String especificacion;    
    private String nota;
    
    private AtributoFisicoDTO atributoFisico;
    
    private ExpedienteMedicoDTO expedienteMedico;

    public AtributoFisicoExpedienteMedicoDTO() {}

    public AtributoFisicoExpedienteMedicoDTO(
            String especificacion, 
            String nota, 
            AtributoFisicoDTO atributoFisico, 
            ExpedienteMedicoDTO expedienteMedico
    ) {
        this.especificacion = especificacion;
        this.nota = nota;
        this.atributoFisico = atributoFisico;
        this.expedienteMedico = expedienteMedico;
    }

    public AtributoFisicoExpedienteMedicoDTO(
            Integer id, 
            String especificacion, 
            String nota, 
            AtributoFisicoDTO atributoFisico, 
            ExpedienteMedicoDTO expedienteMedico
    ) {
        this.id = id;
        this.especificacion = especificacion;
        this.nota = nota;
        this.atributoFisico = atributoFisico;
        this.expedienteMedico = expedienteMedico;
    }

    public Integer getId() {return id;}

    public String getEspecificacion() {return especificacion;}

    public String getNota() {return nota;}

    public AtributoFisicoDTO getAtributoFisico() {return atributoFisico;}

    public ExpedienteMedicoDTO getExpedienteMedico() {return expedienteMedico;}

    public void setId(Integer id) {this.id = id;}

    public void setEspecificacion(String especificacion) {this.especificacion = especificacion;}

    public void setNota(String nota) {this.nota = nota;}

    public void setAtributoFisico(AtributoFisicoDTO atributoFisico) {this.atributoFisico = atributoFisico;}

    public void setExpedienteMedico(ExpedienteMedicoDTO expedienteMedico) {this.expedienteMedico = expedienteMedico;}
}