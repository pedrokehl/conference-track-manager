package conferencetrackmanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Classe para objetos do tipo Dia, onde será contido o código, a data e uma lista de apresentações para aquele dia.
 * @author Pedro Affonso Kehl
 */
public class Dia {
    
    /** Código que será utilizado no arquivo de saída. */
    int codigo;
    
    /** Data do dia */
    public Calendar data;
    
    /** Lista das apresentações que estão agendadas para este dia */
    public ArrayList<Apresentacao> apresentacoesAgendadas = new ArrayList();
    
    /**
     * Construtor
     * @param codigo Código do dia que irá ser escrito no output.
     * @param data   Data que será inicializada com a horaInicial configurada na ConferenceTrackManager.
     */
    public Dia(int codigo, Calendar data){
        this.codigo = codigo;
        data.set(Calendar.HOUR_OF_DAY, ConferenceTrackManager.horaInicial);
        data.set(Calendar.MINUTE, 0);
        data.set(Calendar.SECOND, 0);
        data.set(Calendar.MILLISECOND, 0);
        this.data = data;
    }
    
    /** Retorna o objeto formatado em String
     * @return String - Formatação para escrever no arquivo.
     * Por exemplo, se o código for 1, irá retornar:
     * TRACK 1:
     */
    @Override
     public String toString() {         
         return "TRACK " + Integer.toString(this.codigo) + ":";
     }
    
    /** Método que preenche o dia com as apresentações que melhor se encaixam. */
    public void preencheDia(){        
        // Preenche manhã
        this.insereApresentacoes(ConferenceTrackManager.duracaoManha);
        
        if(!ConferenceTrackManager.apresentacoesPendentes.isEmpty()){
            // Preenche almoço.
            this.apresentacoesAgendadas.add(new Apresentacao("Lunch", ConferenceTrackManager.duracaoAlmoco, this.data));
            this.data.add(Calendar.MINUTE, ConferenceTrackManager.duracaoAlmoco);
            //Preenche tarde.
            this.insereApresentacoes(ConferenceTrackManager.duracaoTarde);
            // Preenche Networking.
            this.apresentacoesAgendadas.add(new Apresentacao("Networking Event", 0, this.data));
        }
    }
    
    /**
     * Preenche um turno do dia.
     * @param duracao Duração do turno que será preenchido
     */
    private void insereApresentacoes(int duracao){
        ArrayList<Apresentacao> listaInserirTurno = this.encontraMelhorListaApresentacoes(duracao);
                
        for (Apresentacao apresentacao : listaInserirTurno){
            apresentacao.setData(this.data);
            this.data.add(Calendar.MINUTE, apresentacao.duracao);
            this.apresentacoesAgendadas.add(apresentacao);
            ConferenceTrackManager.apresentacoesPendentes.remove(apresentacao);
        }
    }
    
    /**
     * Método que encontra a melhor lista de apresentações que podem preencher o turno do dia.
     *
            - A lista de apresentações pendentes está ordenada do maior para o menor, então se não encontramos
            nessa ordem apresentações que completem o turno, removemos o maior da lista de pendentes.
            - Realizamos novamente a busca, até encontrar uma lista que complete o turno, ou quando a lista
            de pendentes estiver vazia. Caso isso ocorra, retornará a lista de apresentações do turno que teve
            "melhor" duração.
     * @param duracaoTot int - Duração total do turno
     * @return ArrayList<Apresentacao> - A melhor lista de apresentações pendentes que pode preencher o turno.
     */
    private ArrayList<Apresentacao> encontraMelhorListaApresentacoes(int duracaoTot){
        
        ArrayList<Apresentacao> listaFull = (ArrayList<Apresentacao>) ConferenceTrackManager.apresentacoesPendentes.clone();
        ArrayList<Apresentacao> melhorLista = new ArrayList();
        int melhorDuracao = duracaoTot;
        
        // Enquanto não tentou todas possibilidades e a melhor duração não é 0.
        while(listaFull.size() >= melhorLista.size() && melhorDuracao > 0){
            
            ArrayList<Apresentacao> listaAux = new ArrayList();
            int duracaoAux = duracaoTot;
            
            Iterator<Apresentacao> it = listaFull.iterator();
            
            //nova tentativa de melhor duração.
            while (it.hasNext() && duracaoAux != 0){
                Apresentacao apresentacao = it.next();
                if (apresentacao.duracao <= duracaoAux){
                    listaAux.add(apresentacao);
                    duracaoAux -= apresentacao.duracao;
                }
            }
            
            if(duracaoAux < melhorDuracao){
                melhorDuracao = duracaoAux;
                melhorLista   = listaAux;
            }
            listaFull.remove(0);
        }
        return melhorLista;
    }
}
