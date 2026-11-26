package br.ufv.ccf313.projeto.ui;

import br.ufv.ccf313.projeto.model.RegistroRanking;
import br.ufv.ccf313.projeto.service.GerenciadorRanking;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

// CAROL: tela de historico/ranking do jogo.
// Le o arquivo data/ranking.txt via GerenciadorRanking e exibe a lista ordenada por score.
// Possui scroll para listas longas e botao para limpar o historico.
public class TelaHistorico extends JFrame {

    private JTextArea areaTexto;
    private JButton botaoLimpar;
    private JButton botaoVoltar;
    private GerenciadorRanking ranking;

    public TelaHistorico() {
        this.ranking = new GerenciadorRanking();
        configurarJanela();
        criarComponentes();
        carregarRanking();
        configurarBotoes();
    }

    // MARINA: configurar tamanho e titulo da janela.
    private void configurarJanela() {
        setTitle("Historico de Partidas");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // MARINA: criar a area de texto com scroll e os botoes.
    private void criarComponentes() {
        JPanel painel = new JPanel();

        areaTexto = new JTextArea(20, 40);
        areaTexto.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaTexto);

        botaoLimpar = new JButton("Limpar Historico");
        botaoVoltar = new JButton("Voltar");

        painel.add(scroll);
        painel.add(botaoLimpar);
        painel.add(botaoVoltar);
        add(painel);
    }

    // Carrega o ranking do arquivo e exibe na area de texto, ordenado por score.
    // Chamado ao abrir a tela e apos limpar o historico.
    private void carregarRanking() {
        List<RegistroRanking> registros = ranking.carregarDoArquivo();
        areaTexto.setText("");

        if (registros.isEmpty()) {
            areaTexto.setText("Nenhuma partida registrada ainda.");
            return;
        }

        areaTexto.append(String.format("%-5s %-20s %-10s %-10s%n", "#", "Nome", "Score", "Wave"));
        areaTexto.append("-".repeat(50) + "\n");

        for (int i = 0; i < registros.size(); i++) {
            RegistroRanking r = registros.get(i);
            areaTexto.append(String.format("%-5d %-20s %-10d %-10d%n",
                i + 1, r.getNomeJogador(), r.getPontuacaoTotal(), r.getOndasSobrevividas()));
        }
    }

    // CAROL: configurar o que acontece ao clicar em cada botao.
    private void configurarBotoes() {
        // Limpar Historico → apaga o arquivo e recarrega a tela vazia
        botaoLimpar.addActionListener(e -> {
            int confirmacao = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja apagar todo o histórico de partidas?",
                "Confirmar Exclusão",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE
            );
            if (confirmacao == javax.swing.JOptionPane.YES_OPTION) {
                ranking.limparHistorico();
                carregarRanking();
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Histórico limpo com sucesso!",
                    "Aviso",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        // Voltar → fecha esta tela
        botaoVoltar.addActionListener(e -> dispose());
    }
}
