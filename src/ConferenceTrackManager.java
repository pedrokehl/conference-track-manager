package conferencetrackmanager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/** Classe que lê um arquivo texto que contêm diversas apresentações e suas respectivas durações,
 * organiza as apresentações em uma agenda, separadas por dia e escreve esta agenda em outro arquivo texto chamado output.txt
 * @author Pedro Affonso Kehl
 */
public class ConferenceTrackManager {

    /**apresentacoesPendentes - contêm as apresentações pendentes para serem adicionadas na Agenda. */
    public static ArrayList<Apresentacao> apresentacoesPendentes = new ArrayList();
    
    /** Atributo estático com a duração em minutos de uma manhã */
    public static int duracaoManha  = 180;

    /** Atributo estático com a duração em minutos de um almoço */
    public static int duracaoAlmoco = 60;

    /** Atributo estático com a duração em minutos de uma tarde */
    public static int duracaoTarde  = 240;

    /** Atributo estático com a hora Inicial de um dia para a agenda */
    public static int horaInicial   = 9;

    /** Caminho do arquivo que será gerado o arquivo de saída. */
    public String filePath;
    
    /** Método principal do projeto. */
    public static void main(String[] args) {
        
        ConferenceTrackManager ctm = new ConferenceTrackManager();
        ctm.leArquivo();
        
        if (!ConferenceTrackManager.apresentacoesPendentes.isEmpty()) {
            // Ordena lista decrescentemente pelo tempo.
            Collections.sort(ConferenceTrackManager.apresentacoesPendentes);
            
            Agenda agenda = new Agenda();
            agenda.adicionaApresentacoes();
            ctm.escreveArquivo(agenda);
        } else{
            JOptionPane.showMessageDialog(null, "Arquivo inválido.");
        }
    }
    
    /** Oferece uma janela de seleção do arquivo desejado, e o lê. */
    private void leArquivo(){
        
        FileFilter filter = new FileNameExtensionFilter("Arquivo texto","txt");
        
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // filtra para mostrar apenas arquivos texto
        chooser.setFileFilter(filter);
        
        if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            File arquivo = chooser.getSelectedFile();
            
            // Salva o caminho do arquivo, para salvar por padrão o output.txt no mesmo lugar
            this.filePath = arquivo.getAbsolutePath();
            this.filePath = this.filePath.substring(0,this.filePath.lastIndexOf(File.separator));
            
            try {
                FileReader fr = new FileReader(arquivo);
                Scanner br = new Scanner(fr);

                while (br.hasNextLine()) {
                    String line = br.nextLine();
                    this.gravaApresentacaoPendente(line);
                }
                br.close();
                fr.close();
            }
            catch (IOException | NullPointerException e) {
                JOptionPane.showMessageDialog(null, "Erro na leitura do arquivo.");
            }
        }
        else{
            exit(0);
        }
    }
    
    /** Se combinar com a expressão regular, instancia um objeto Apresentacao e adiciona-o em uma lista de apresentações pendentes
 Pattern: Precisa terminar com lightning ou xxmin
       grupo 0 = Descrição
       grupo 1 = Apenas a descrição (alterar para utilizar este, ao invés do 0, caso deseja remover a duração de dentro da descrição)
       grupo 2 = lightning - 5 minutos (duração)
       grupo 4 = duração em minutos (duração)
     * @param Line String - Linha que será analisada.
     */
    private void gravaApresentacaoPendente(String line){
        Pattern p = Pattern.compile("(.*)\\s((([0-9]{1,2})min)|lightning)");
        Matcher m = p.matcher(line);
        if(m.matches()){
            int minutos;
            String descricao = m.group(0);
            if(m.group(2).equals("lightning")){
                minutos = 5;
            } else{
                minutos = Integer.parseInt(m.group(4));
            }
            ConferenceTrackManager.apresentacoesPendentes.add(new Apresentacao(descricao, minutos, null));
        }
    }
    
    /** Escreve a agenda em um arquivo texto chamado output.txt no mesmo caminho do arquivo lido inicialmente.
     * @param agenda Agenda - A agenda que será escrita no arquivo.
     */
    private void escreveArquivo(Agenda agenda){
        String nomeArquivoSaida = this.filePath + "\\output.txt";
        File arquivo = new File(nomeArquivoSaida);

        try {
            FileWriter fw = new FileWriter(arquivo);
            PrintWriter bw = new PrintWriter(fw);
            
            for(Dia dia : agenda.diasAgendados) {
                bw.println(dia.toString());
                for(Apresentacao apresentacao : dia.apresentacoesAgendadas){
                    bw.println(apresentacao.toString());
                }
                bw.println("\n");
            }
            
            bw.close();
            fw.close();
            JOptionPane.showMessageDialog(null, "O arquivo com sua agenda foi gerado em: \n" + nomeArquivoSaida);
            
        } catch (IOException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Erro na escrita do arquivo.");
        }
    }
}
