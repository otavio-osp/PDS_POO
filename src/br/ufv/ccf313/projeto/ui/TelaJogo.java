package br.ufv.ccf313.projeto.ui;

import br.ufv.ccf313.projeto.model.Jogador;
import br.ufv.ccf313.projeto.model.mapa.Celula;
import br.ufv.ccf313.projeto.model.mapa.Mapa;
import br.ufv.ccf313.projeto.service.GerenciadorRanking;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// MARINA + CAROL: tela principal do jogo onde o mapa e exibido.
// MARINA: responsavel por renderizar a grade do mapa, os inimigos, as tartarugas
//         e as animacoes (tartaruga aumenta ao atacar, lixo encolhe ao tomar dano).
// CAROL: responsavel pelos eventos de clique nas celulas (modal de compra/upgrade)
//         e por detectar condicao de game over para chamar salvarPartida().
public class TelaJogo extends JFrame {

    private Mapa mapa;
    private Jogador jogador;
    private JLabel labelVida;
    private JLabel labelDinheiro;
    private JLabel labelWave;

    // CAROL + MARINA: inicializar o mapa e o jogador e montar o layout.
    public TelaJogo(Mapa mapa, Jogador jogador) {
        this.mapa = mapa;
        this.jogador = jogador;
        configurarJanela();
        criarHUD();
        configurarCliqueMapa();
    }

    // MARINA: configurar tamanho da janela e titulo.
    private void configurarJanela() {
        setTitle("Tartarugas vs. Lixo Marinho - Jogo");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // MARINA: criar o painel de HUD que mostra vida, dinheiro e wave atual.
    // Deve ser atualizado a cada ciclo do jogo via atualizarHUD().
    private void criarHUD() {
        JPanel hud = new JPanel();
        labelVida = new JLabel("Vida: " + jogador.getVida());
        labelDinheiro = new JLabel("Dinheiro: " + jogador.getDinheiro());
        labelWave = new JLabel("Wave: " + jogador.getWaveAtual());
        hud.add(labelVida);
        hud.add(labelDinheiro);
        hud.add(labelWave);
        add(hud);
    }

    // MARINA: chamar este metodo a cada ciclo para atualizar os valores do HUD.
    public void atualizarHUD() {
        labelVida.setText("Vida: " + jogador.getVida());
        labelDinheiro.setText("Dinheiro: " + jogador.getDinheiro());
        labelWave.setText("Wave: " + jogador.getWaveAtual());
    }

    // CAROL: registrar o evento de clique nas celulas da grade.
    // Celula LIVRE → abrir modal de escolha de tartaruga
    // Celula OCUPADA → abrir modal de upgrade da tartaruga
    // CAROL: verificar se o jogador tem dinheiro suficiente antes de confirmar a compra.
    //        Se nao tiver, exibir aviso de "Dinheiro insuficiente".
    private void configurarCliqueMapa() {
        // CAROL: identificar qual celula foi clicada com base nas coordenadas do clique
        // e chamar abrirModalCompra() ou abrirModalUpgrade() conforme o tipo da celula
    }

    // CAROL: exibir modal com as 3 opcoes de tartaruga para o jogador escolher.
    // Apos escolha, verificar jogador.gastarDinheiro(custo) antes de colocar no mapa.
    // Se gastarDinheiro retornar false, chamar mostrarAvisoSemDinheiro().
    public void abrirModalCompra(Celula celula) {
        // CAROL: implementar o modal de selecao de tartaruga
        // Opcoes: TartarugaDeCouro (200), TartarugaDePente (100), TartarugaVerde (50)
    }

    // CAROL: exibir modal de upgrade ao clicar em uma celula ja ocupada por tartaruga.
    // TOM: definir quais upgrades ficam disponiveis para cada tipo de tartaruga e o custo.
    public void abrirModalUpgrade(Celula celula) {
        // CAROL: implementar o modal de upgrade
    }

    // CAROL: exibir aviso de dinheiro insuficiente.
    public void mostrarAvisoSemDinheiro() {
        JOptionPane.showMessageDialog(this, "Dinheiro insuficiente!", "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    // CAROL: chamar quando o jogador perder (vida <= 0).
    // Salva a partida no ranking e exibe a tela de game over.
    // PEDRO: GerenciadorRanking.salvarPartida() e chamado aqui.
    public void mostrarGameOver() {
        GerenciadorRanking ranking = new GerenciadorRanking();
        ranking.salvarPartida(jogador.getNome(), jogador.getScore(), jogador.getWaveAtual());
        JOptionPane.showMessageDialog(this,
            "Game Over!\nScore: " + jogador.getScore() + "\nWave: " + jogador.getWaveAtual(),
            "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
        // CAROL: voltar para TelaPrincipal apos fechar o dialogo
    }

    // CAROL: chamar quando o jogador vencer todas as waves.
    // Salva a partida no ranking com vitoria.
    public void mostrarVitoria() {
        GerenciadorRanking ranking = new GerenciadorRanking();
        ranking.salvarPartida(jogador.getNome(), jogador.getScore(), jogador.getWaveAtual());
        JOptionPane.showMessageDialog(this,
            "Voce venceu!\nScore: " + jogador.getScore() + "\nWave: " + jogador.getWaveAtual(),
            "Vitoria!", JOptionPane.INFORMATION_MESSAGE);
        // CAROL: voltar para TelaPrincipal apos fechar o dialogo
    }
}
