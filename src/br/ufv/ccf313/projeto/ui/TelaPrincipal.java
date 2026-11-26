package br.ufv.ccf313.projeto.ui;

import br.ufv.ccf313.projeto.model.Jogador;
import br.ufv.ccf313.projeto.model.mapa.Mapa;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class TelaPrincipal extends JFrame {

    private Image imgMenuFundo;
    private Image imgInsiraNome;
    private JTextField campoNome;

    public TelaPrincipal() {
        String base = "/br/ufv/ccf313/projeto/resource/imagens/";
        imgMenuFundo = carregarImagem(base + "menus/menu_fundo_2_ofc.jpeg");
        if (imgMenuFundo == null) {
            imgMenuFundo = carregarImagem(base + "menus/menu_fundo_2.png");
        }
        imgInsiraNome = carregarImagem(base + "telas_auxiliares/tela_insira_nome_antes.png");

        setTitle("Tartarugas vs. Lixo Marinho");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imgMenuFundo != null) {
                    g.drawImage(imgMenuFundo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        painel.setLayout(null);

        // Campo de texto exatamente dentro da barra clara abaixo de "INSIRA SEU NOME"
        campoNome = new JTextField();
        campoNome.setBounds(216, 240, 368, 36);
        campoNome.setOpaque(false);
        campoNome.setBorder(null);
        campoNome.setHorizontalAlignment(SwingConstants.CENTER);
        campoNome.setFont(new Font("Monospaced", Font.BOLD, 18));
        campoNome.setForeground(Color.WHITE);
        campoNome.setCaretColor(Color.WHITE);
        painel.add(campoNome);

        // Botao JOGAR (placa central larga)
        JButton btnJogar = criarBotaoInvisivel(192, 332, 416, 78);
        btnJogar.addActionListener(e -> iniciarPartida());
        painel.add(btnJogar);

        // Botao VER RANKING (inferior esquerdo)
        JButton btnRanking = criarBotaoInvisivel(192, 444, 198, 72);
        btnRanking.addActionListener(e -> {
            new TelaHistorico().setVisible(true);
            this.dispose();
        });
        painel.add(btnRanking);

        // Botao SAIR (inferior direito)
        JButton btnSair = criarBotaoInvisivel(416, 444, 192, 72);
        btnSair.addActionListener(e -> System.exit(0));
        painel.add(btnSair);

        setContentPane(painel);
    }

    private void iniciarPartida() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            mostrarAvisoInsiraNome();
            return;
        }

        Jogador jogador = new Jogador(nome);
        Mapa mapa = new Mapa();
        new TelaJogo(mapa, jogador).setVisible(true);
        this.dispose();
    }

    private void mostrarAvisoInsiraNome() {
        if (imgInsiraNome == null) {
            JOptionPane.showMessageDialog(this, "Insira seu nome na placa antes de jogar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialogo = new JDialog(this, true);
        dialogo.setUndecorated(true);
        dialogo.setBackground(new Color(0, 0, 0, 0));

        JPanel painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imgInsiraNome, 0, 0, getWidth(), getHeight(), this);
            }
        };
        painel.setOpaque(false);
        painel.setLayout(null);

        // Botao X circular no canto superior direito da placa
        JButton btnFechar = criarBotaoInvisivel(450, 68, 46, 46);
        btnFechar.addActionListener(e -> {
            dialogo.dispose();
            campoNome.requestFocus();
        });

        // Botao OK na base da placa
        JButton btnOk = criarBotaoInvisivel(176, 162, 174, 38);
        btnOk.addActionListener(e -> {
            dialogo.dispose();
            campoNome.requestFocus();
        });

        painel.add(btnFechar);
        painel.add(btnOk);

        dialogo.setContentPane(painel);
        dialogo.setSize(560, 315);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
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
