package br.ufv.ccf313.projeto.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class TelaMenuView extends JFrame {

    // Proporção 4:3 compatível com o mapa base de 336x240 px (3x = 1008x720)
    private static final int LARGURA = 1008;
    private static final int ALTURA = 720;

    private JButton botaoJogar;
    private JButton botaoRanking;
    private JButton botaoSair;
    private Image imagemFundo;

    public TelaMenuView() {
        configurarJanela();
        carregarImagem();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("Tartarugas vs. Lixo Marinho");
        setSize(LARGURA, ALTURA);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void carregarImagem() {
        File arquivo = new File("assets/menu_fundo.png");
        if (arquivo.exists()) {
            imagemFundo = new ImageIcon(arquivo.getAbsolutePath()).getImage();
        }
    }

    private void inicializarComponentes() {
        JPanel painelMenu = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagemFundo != null) {
                    Graphics2D g2 = (Graphics2D) g;
                    // Mantém os pixels nítidos ao esticar para a janela
                    g2.setRenderingHint(
                            RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
                    );
                    g2.drawImage(imagemFundo, 0, 0, LARGURA, ALTURA, this);
                }
            }
        };
        painelMenu.setLayout(null);

        // Botões invisíveis sobre as áreas dos botões da imagem para a Carol ligar as ações
        int larguraBotao = 340;
        int alturaBotao = 86;
        int posX = LARGURA / 2 - larguraBotao / 2;

        botaoJogar = criarBotaoTransparente(posX, 270, larguraBotao, alturaBotao);
        botaoRanking = criarBotaoTransparente(posX, 382, larguraBotao, alturaBotao);
        botaoSair = criarBotaoTransparente(posX, 494, larguraBotao, alturaBotao);

        painelMenu.add(botaoJogar);
        painelMenu.add(botaoRanking);
        painelMenu.add(botaoSair);

        setContentPane(painelMenu);
    }

    private JButton criarBotaoTransparente(int x, int y, int largura, int altura) {
        JButton botao = new JButton();
        botao.setBounds(x, y, largura, altura);
        botao.setOpaque(false);
        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    // Métodos prontos para a Carol ligar ao GameController
    public void registrarAcaoJogar(ActionListener listener) {
        botaoJogar.addActionListener(listener);
    }

    public void registrarAcaoRanking(ActionListener listener) {
        botaoRanking.addActionListener(listener);
    }

    public void registrarAcaoSair(ActionListener listener) {
        botaoSair.addActionListener(listener);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaMenuView().setVisible(true));
    }
}
