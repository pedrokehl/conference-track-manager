package conferencetrackmanager;

import java.util.Calendar;

/**
 * Classe para objetos do tipo Apresentação, onde será contido a descrição, a duração, e a data/hora (caso já tenha sido agendada)
 * @author Pedro Affonso Kehl
 */
public class Apresentacao implements Comparable<Apresentacao> {
    
    /** Descrição da apresentação */
    String   descricao;
    
    /** Duração da apresentação em minutos */
    int      duracao;
    
    /** Data e hora da apresentação, caso já tenha sido agendada*/
    Calendar data;
    
    public Apresentacao(String descricao, int minutos, Calendar data){
        this.descricao = descricao;
        this.duracao   = minutos;
        if(data != null){
            this.data      = (Calendar) data.clone();
        }
    }
    
    public void setData(Calendar data){
        this.data = (Calendar) data.clone();
    }
    
    /** Retorna o objeto formatado em String
     * @return String - Formatação para escrever no arquivo.
     * Exemplo:
     * Descrição = Writing Fast Tests Against Enterprise Rails 60min
     * Data = 05/06/2016 - 15h
     * Retornará: 03:00PM Writing Fast Tests Against Enterprise Rails 60min
     */
     @Override
     public String toString() {
         String result;
         int horaAux;
         horaAux = (this.data.get(Calendar.HOUR));
         horaAux = (horaAux !=0 ? horaAux : 12);
         result  = String.format("%02d", horaAux);
         result += ":";
         result += String.format("%02d", (this.data.get(Calendar.MINUTE)));
         result += (this.data.get(Calendar.AM_PM) == Calendar.AM ? "AM " : "PM ");
         result += this.descricao;         
         return result;
     }
     
    @Override
    public int compareTo(Apresentacao other) {
        return ((Integer)other.duracao).compareTo(this.duracao);
    }
}
