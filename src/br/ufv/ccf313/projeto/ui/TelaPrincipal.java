package br.ufv.ccf313.projeto.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

// CAROL + MARINA: tela inicial do jogo (menu principal).
// MARINA: responsavel pelo layout visual, fontes, cores e imagens de fundo.
// CAROL: responsavel pela logica dos botoes (o que acontece ao clicar em cada um).
public class TelaPrincipal extends JFrame {

    private JButton botaoJogar;
    private JButton botaoHistorico;
    private JButton botaoSair;

    // CAROL + MARINA: montar o layout e registrar os listeners dos botoes.
    public TelaPrincipal() {
        configurarJanela();
        criarComponentes();
        configurarBotoes();
    }

    // MARINA: definir tamanho, titulo, cor de fundo e posicao da janela.
    private void configurarJanela() {
        setTitle("Tartarugas vs. Lixo Marinho");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // MARINA: criar e posicionar os componentes visuais (botoes, titulo, imagens).
    private void criarComponentes() {
        JPanel painel = new JPanel();
        JLabel titulo = new JLabel("Tartarugas vs. Lixo Marinho");

        botaoJogar = new JButton("Jogar");
        botaoHistorico = new JButton("Historico");
        botaoSair = new JButton("Sair");

        painel.add(titulo);
        painel.add(botaoJogar);
        painel.add(botaoHistorico);
        painel.add(botaoSair);
        add(painel);
    }

    // CAROL: definir o que acontece ao clicar em cada botao.
    // Jogar     → abrir TelaJogo e fechar esta tela
    // Historico → abrir TelaHistorico
    // Sair      → encerrar o programa
    private void configurarBotoes() {
        botaoJogar.addActionListener(e -> {
            String nomeJogador = javax.swing.JOptionPane.showInputDialog(
                this,
                "Digite seu nome para iniciar:",
                "Novo Jogo",
                javax.swing.JOptionPane.QUESTION_MESSAGE
            );

            // Só avança se o jogador digitou um nome válido e não clicou em Cancelar
            if (nomeJogador != null && !nomeJogador.trim().isEmpty()) {
                // TODO: Quando a classe Jogador estiver integrada aqui, instanciar com nomeJogador.trim()
                
                // Descomente as 3 linhas abaixo assim que o arquivo TelaJogo.java existir no pacote ui:
                // TelaJogo telaJogo = new TelaJogo();
                // telaJogo.setVisible(true);
                // this.dispose();
                
                javax.swing.JOptionPane.showMessageDialog(this, "Iniciando partida para: " + nomeJogador.trim());
            } else if (nomeJogador != null) {
                javax.swing.JOptionPane.showMessageDialog(
                    this, 
                    "Por favor, digite um nome válido!", 
                    "Aviso", 
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
            }
        });

        botaoHistorico.addActionListener(e -> {
            TelaHistorico telaHistorico = new TelaHistorico();
            telaHistorico.setVisible(true);
            //javax.swing.JOptionPane.showMessageDialog(this, "Abrindo Histórico...");
        });

        botaoSair.addActionListener(e -> System.exit(0));
    }
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }
}
