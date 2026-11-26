package br.ufv.ccf313.projeto.ui;

import br.ufv.ccf313.projeto.model.RegistroRanking;
import br.ufv.ccf313.projeto.service.GerenciadorRanking;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TelaHistorico extends JFrame {

    private Image imgFundoRanking;
    private JLabel[] labelsPosicoes;

    public TelaHistorico() {
        String base = "/br/ufv/ccf313/projeto/resource/imagens/";
        imgFundoRanking = carregarImagem(base + "menus/tela_ranking.jpeg");

        setTitle("Tartarugas vs. Lixo Marinho - Ranking");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imgFundoRanking != null) {
                    g.drawImage(imgFundoRanking, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(31, 61, 46));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        painel.setLayout(null);

        GerenciadorRanking gerenciador = new GerenciadorRanking();
        List<RegistroRanking> registros = gerenciador.carregarDoArquivo();

        // Posiciona os 10 textos exatamente dentro das 10 faixas da imagem (apos o icone da tartaruga)
        labelsPosicoes = new JLabel[10];
        Font fonteRanking = new Font("Monospaced", Font.BOLD, 15);
        int xTexto = 310;
        int larguraTexto = 310;
        int yInicial = 123;
        int alturaFaixa = 37;

        for (int i = 0; i < 10; i++) {
            String textoLinha = (registros != null && i < registros.size())
                    ? registros.get(i).toString()
                    : "---";
            JLabel lblPosicao = new JLabel(textoLinha);
            lblPosicao.setBounds(xTexto, yInicial + (i * alturaFaixa), larguraTexto, 30);
            lblPosicao.setFont(fonteRanking);
            lblPosicao.setForeground(i == 0 ? new Color(45, 30, 15) : Color.WHITE);
            labelsPosicoes[i] = lblPosicao;
            painel.add(lblPosicao);
        }

        // Botao VOLTAR na placa inferior
        JButton btnVoltar = criarBotaoInvisivel(296, 514, 208, 48);
        btnVoltar.addActionListener(e -> {
            new TelaPrincipal().setVisible(true);
            this.dispose();
        });
        painel.add(btnVoltar);

        // Botao discreto no canto superior direito para limpar o historico
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setBounds(685, 18, 90, 28);
        btnLimpar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnLimpar.setBackground(new Color(140, 45, 45));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpar.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(this,
                    "Deseja apagar todo o historico do ranking?",
                    "Limpar Historico",
                    JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                gerenciador.limparHistorico();
                for (JLabel lbl : labelsPosicoes) {
                    lbl.setText("---");
                }
            }
        });
        painel.add(btnLimpar);

        setContentPane(painel);
    }

    private JButton criarBotaoInvisivel(int x, int y, int largura, int altura) {
        JButton btn = new JButton();
        btn.setBounds(x, y, largura, altura);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private Image carregarImagem(String caminho) {
        URL url = getClass().getResource(caminho);
        return (url != null) ? new ImageIcon(url).getImage() : null;
    }
}
