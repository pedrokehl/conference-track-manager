package conferencetrackmanager;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Classe para objetos do tipo Agenda, onde será contida uma lista de dias.
 * @author Pedro Affonso Kehl
 */
public class Agenda {
    
    /** diasAgendados - contêm os dias que já foram agendados */
    public ArrayList<Dia> diasAgendados = new ArrayList();
    
    /** Preenche os dias da Agenda com as apresentações pendentes. */
    public void adicionaApresentacoes(){
        
        int i = 1;
        Calendar dataDia = Calendar.getInstance(); // dia atual
        
        while(!ConferenceTrackManager.apresentacoesPendentes.isEmpty()){
            Dia dia = new Dia(i++, dataDia);
            dia.preencheDia(); // preenche o dia com as conferências pendentes
            diasAgendados.add(dia);            
            dataDia.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
}
