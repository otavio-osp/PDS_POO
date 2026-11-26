package br.ufv.ccf313.projeto.ui;

import br.ufv.ccf313.projeto.model.Jogador;
import br.ufv.ccf313.projeto.model.mapa.Mapa;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TelaPrincipal extends JFrame {

    private JTextField campoNome;
    private JButton botaoJogar;
    private JButton botaoHistorico;
    private JButton botaoSair;
    private Image imagemFundo;

    public TelaPrincipal() {
        carregarImagemFundo();
        configurarJanela();
        criarComponentes();
        configurarBotoes();
    }

    private void carregarImagemFundo() {
        URL url = getClass().getResource("/br/ufv/ccf313/projeto/resource/imagens/menus/menu_fundo_2.png");
        if (url != null) {
            imagemFundo = new ImageIcon(url).getImage();
        } else {
            System.err.println("Aviso: menus/menu_fundo_2.png nao encontrada!");
        }
    }

    private void configurarJanela() {
        setTitle("Tartarugas vs. Lixo Marinho");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void criarComponentes() {
        JPanel painelFundo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagemFundo != null) {
                    g.drawImage(imagemFundo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        painelFundo.setLayout(null);

        campoNome = new JTextField();
        campoNome.setBounds(218, 224, 348, 28);
        campoNome.setOpaque(false);
        campoNome.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        campoNome.setFont(new Font("Monospaced", Font.BOLD, 16));
        campoNome.setForeground(Color.WHITE);
        campoNome.setCaretColor(Color.WHITE);

        botaoJogar = criarBotaoInvisivel(195, 312, 395, 65);
        botaoHistorico = criarBotaoInvisivel(195, 398, 190, 62);
        botaoSair = criarBotaoInvisivel(400, 398, 190, 62);

        painelFundo.add(campoNome);
        painelFundo.add(botaoJogar);
        painelFundo.add(botaoHistorico);
        painelFundo.add(botaoSair);

        add(painelFundo);
    }

    private JButton criarBotaoInvisivel(int x, int y, int largura, int altura) {
        JButton botao = new JButton();
        botao.setBounds(x, y, largura, altura);
        botao.setOpaque(false);
        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private void configurarBotoes() {
        botaoJogar.addActionListener(e -> {
            String nomeJogador = campoNome.getText().trim();

            if (nomeJogador.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Insira seu nome na placa acima antes de jogar!",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
                );
                campoNome.requestFocus();
                return;
            }

            Jogador jogador = new Jogador(nomeJogador);
            Mapa mapa = new Mapa();

            TelaJogo telaJogo = new TelaJogo(mapa, jogador);
            telaJogo.setVisible(true);
            this.dispose();
        });

        botaoHistorico.addActionListener(e -> {
            TelaHistorico telaHistorico = new TelaHistorico();
            telaHistorico.setVisible(true);
        });

        botaoSair.addActionListener(e -> System.exit(0));
    }
}