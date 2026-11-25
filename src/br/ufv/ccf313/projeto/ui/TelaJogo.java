package br.ufv.ccf313.projeto.ui;

import br.ufv.ccf313.projeto.model.Jogador;
import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;
import br.ufv.ccf313.projeto.model.inimigo.LixoGarrafa;
import br.ufv.ccf313.projeto.model.inimigo.LixoPlastico;
import br.ufv.ccf313.projeto.model.inimigo.LixoRede;
import br.ufv.ccf313.projeto.model.mapa.Celula;
import br.ufv.ccf313.projeto.model.mapa.Mapa;
import br.ufv.ccf313.projeto.model.tartaruga.TartarugaDeCouro;
import br.ufv.ccf313.projeto.model.tartaruga.TartarugaDePente;
import br.ufv.ccf313.projeto.model.tartaruga.TartarugaVerde;
import br.ufv.ccf313.projeto.model.tartaruga.TorreTartaruga;
import br.ufv.ccf313.projeto.service.GerenciadorRanking;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TelaJogo extends JFrame {

    private static final int TAM_CELULA = 64;
    private static final int DURACAO_TURNO_MS = 1000; // 1 segundo por passo na grade

    private Mapa mapa;
    private Jogador jogador;
    private JLabel labelVida;
    private JLabel labelDinheiro;
    private JLabel labelWave;
    private PainelTabuleiro painelTabuleiro;

    // Estruturas da partida
    private Queue<InimigoLixo> filaSpawn;
    private List<InimigoLixo> inimigosAtivos;
    private Map<InimigoLixo, int[]> posAnteriorInimigo; // Guarda {colAnterior, linAnterior} para deslizar suave
    private Set<Celula> tartarugasAtacando;             // Guarda quais torres atacaram neste turno

    private Timer timerLogica;
    private Timer timerAnimacao;
    private long inicioTurnoMs;
    private int contadorTicks = 0;

    // Imagens
    private Image imgFundoMapa;
    private Image imgTartaruga;
    private Image imgPlastico;
    private Image imgGarrafa;
    private Image imgRede;

    public TelaJogo(Mapa mapa, Jogador jogador) {
        this.mapa = mapa;
        this.jogador = jogador;
        this.filaSpawn = new LinkedList<>();
        this.inimigosAtivos = new ArrayList<>();
        this.posAnteriorInimigo = new HashMap<>();
        this.tartarugasAtacando = new HashSet<>();

        carregarSprites();
        configurarJanela();
        criarHUD();
        criarTabuleiro();
        prepararWaveSimulada();
        iniciarLoops();
    }

    private void carregarSprites() {
        imgFundoMapa = carregarImagem("/br/ufv/ccf313/projeto/resource/imagens/mapa_fundo.jpg");
        imgTartaruga = carregarImagem("/br/ufv/ccf313/projeto/resource/imagens/north.png");
        imgPlastico = carregarImagem("/br/ufv/ccf313/projeto/resource/imagens/lixo_sacola_plastica_24x24.png");
        imgGarrafa = carregarImagem("/br/ufv/ccf313/projeto/resource/imagens/lixo_garrafa_plastica_24x24.png");
        imgRede = carregarImagem("/br/ufv/ccf313/projeto/resource/imagens/lixo_rede_pesca_24x24.png");
    }

    private Image carregarImagem(String caminho) {
        URL url = getClass().getResource(caminho);
        return (url != null) ? new ImageIcon(url).getImage() : null;
    }

    private void configurarJanela() {
        setTitle("Tartarugas vs. Lixo Marinho - Partida");
        setSize(Mapa.COLUNAS_PADRAO * TAM_CELULA + 20, Mapa.LINHAS_PADRAO * TAM_CELULA + 95);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void criarHUD() {
        JPanel hud = new JPanel();
        hud.setBackground(new Color(31, 61, 46));

        Font fonteHud = new Font("SansSerif", Font.BOLD, 16);
        labelVida = new JLabel("Vida: " + jogador.getVida() + "   |   ");
        labelDinheiro = new JLabel("Conchas: " + jogador.getDinheiro() + "   |   ");
        labelWave = new JLabel("Wave: " + jogador.getWaveAtual());

        labelVida.setForeground(Color.WHITE);
        labelDinheiro.setForeground(new Color(220, 200, 163));
        labelWave.setForeground(new Color(162, 181, 160));

        labelVida.setFont(fonteHud);
        labelDinheiro.setFont(fonteHud);
        labelWave.setFont(fonteHud);

        hud.add(labelVida);
        hud.add(labelDinheiro);
        hud.add(labelWave);
        add(hud, BorderLayout.NORTH);
    }

    private void criarTabuleiro() {
        painelTabuleiro = new PainelTabuleiro();
        painelTabuleiro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int coluna = e.getX() / TAM_CELULA;
                int linha = e.getY() / TAM_CELULA;
                Celula celula = mapa.getCelula(linha, coluna);

                if (celula == null) return;

                if (celula.estaLivre()) {
                    abrirModalCompra(celula);
                } else if (celula.estaOcupada()) {
                    JOptionPane.showMessageDialog(TelaJogo.this, "Este ponto ja possui uma tartaruga!");
                }
            }
        });
        add(painelTabuleiro, BorderLayout.CENTER);
    }

    private void prepararWaveSimulada() {
        InimigoLixo[] onda = {
            new LixoPlastico(0, 0),
            new LixoPlastico(0, 0),
            new LixoGarrafa(0, 0),
            new LixoPlastico(0, 0),
            new LixoGarrafa(0, 0),
            new LixoRede(0, 0),
            new LixoGarrafa(0, 0),
            new LixoRede(0, 0)
        };

        for (InimigoLixo lixo : onda) {
            lixo.definirCaminho(mapa.getCaminho());
            filaSpawn.add(lixo);
        }
    }

    private void iniciarLoops() {
        inicioTurnoMs = System.currentTimeMillis();

        // Loop 1: Logica de turnos (move na matriz e aplica dano a cada 1 segundo)
        timerLogica = new Timer(DURACAO_TURNO_MS, e -> executarTurno());
        timerLogica.setInitialDelay(3000);
        timerLogica.start();

        // Loop 2: Renderizacao suave a ~60 FPS (desliza os sprites entre as celulas)
        timerAnimacao = new Timer(16, e -> painelTabuleiro.repaint());
        timerAnimacao.start();
    }

    private void pararLoops() {
        if (timerLogica != null) timerLogica.stop();
        if (timerAnimacao != null) timerAnimacao.stop();
    }

    private void executarTurno() {
        contadorTicks++;
        inicioTurnoMs = System.currentTimeMillis();
        tartarugasAtacando.clear();

        // 1. Guarda a posicao atual antes de mover (para animar o deslizamento)
        for (InimigoLixo lixo : inimigosAtivos) {
            posAnteriorInimigo.put(lixo, new int[]{lixo.getPosX(), lixo.getPosY()});
        }

        // 2. Solta um novo lixo a cada 2 turnos
        if (contadorTicks % 2 == 1 && !filaSpawn.isEmpty()) {
            InimigoLixo novo = filaSpawn.poll();
            // Comeca visualmente 1 celula antes da entrada para entrar deslizando
            posAnteriorInimigo.put(novo, new int[]{novo.getPosX() - 1, novo.getPosY()});
            inimigosAtivos.add(novo);
        }

        // 3. Move os lixos ativos na grade
        for (InimigoLixo lixo : inimigosAtivos) {
            lixo.atualizar();
        }

        // 4. Tartarugas atacam diretamente os lixos no alcance
        for (Celula celula : mapa.getCelulasComTartaruga()) {
            int linTorre = celula.getLinha();
            int colTorre = celula.getColuna();

            int dano = 15;
            double alcance = 2.5;
            String nomeTorre = celula.getTartaruga().getClass().getSimpleName();
            if (nomeTorre.equals("TartarugaDePente")) {
                dano = 25;
                alcance = 3.0;
            } else if (nomeTorre.equals("TartarugaDeCouro")) {
                dano = 45;
                alcance = 3.5;
            }

            for (InimigoLixo lixo : inimigosAtivos) {
                if (!lixo.estaDestruido()) {
                    double dist = Math.hypot(lixo.getPosX() - colTorre, lixo.getPosY() - linTorre);
                    if (dist <= alcance) {
                        lixo.receberDano(dano);
                        tartarugasAtacando.add(celula); // Marca para animar o pulso da tartaruga
                        break;
                    }
                }
            }
        }

        // 5. Verifica lixos destruidos ou que chegaram ao fim
        Iterator<InimigoLixo> it = inimigosAtivos.iterator();
        while (it.hasNext()) {
            InimigoLixo lixo = it.next();
            if (lixo.estaDestruido()) {
                jogador.registrarInimigoDestruido(lixo.getRecompensa());
                posAnteriorInimigo.remove(lixo);
                it.remove();
            } else if (lixo.chegouAoFim()) {
                jogador.receberDano(lixo.getDano());
                posAnteriorInimigo.remove(lixo);
                it.remove();
            }
        }

        atualizarHUD();

        // 6. Verifica fim de jogo
        if (jogador.getVida() <= 0) {
            pararLoops();
            mostrarGameOver();
        } else if (filaSpawn.isEmpty() && inimigosAtivos.isEmpty()) {
            pararLoops();
            mostrarVitoria();
        }
    }

    public void atualizarHUD() {
        labelVida.setText("Vida: " + jogador.getVida() + "   |   ");
        labelDinheiro.setText("Conchas: " + jogador.getDinheiro() + "   |   ");
        labelWave.setText("Wave: " + jogador.getWaveAtual());
    }

    public void abrirModalCompra(Celula celula) {
        String[] opcoes = {
            "Tartaruga Verde (50 conchas)",
            "Tartaruga de Pente (100 conchas)",
            "Tartaruga de Couro (200 conchas)"
        };

        int escolha = JOptionPane.showOptionDialog(
            this,
            "Escolha a tartaruga para plantar neste ponto:",
            "Construir Defesa",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcoes,
            opcoes[0]
        );

        if (escolha == -1) return;

        int custo = (escolha == 0) ? 50 : (escolha == 1) ? 100 : 200;
        if (!jogador.gastarDinheiro(custo)) {
            mostrarAvisoSemDinheiro();
            return;
        }

        int lin = celula.getLinha();
        int col = celula.getColuna();
        TorreTartaruga torre;
        if (escolha == 0) {
            torre = new TartarugaVerde(col, lin);
        } else if (escolha == 1) {
            torre = new TartarugaDePente(col, lin);
        } else {
            torre = new TartarugaDeCouro(col, lin);
        }

        mapa.colocarTartaruga(lin, col, torre);
        atualizarHUD();
    }

    public void mostrarAvisoSemDinheiro() {
        JOptionPane.showMessageDialog(this, "Conchas insuficientes!", "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarGameOver() {
        GerenciadorRanking ranking = new GerenciadorRanking();
        ranking.salvarPartida(jogador.getNome(), jogador.getScore(), jogador.getWaveAtual());
        JOptionPane.showMessageDialog(this,
            "Game Over!\nScore: " + jogador.getScore() + "\nWave: " + jogador.getWaveAtual(),
            "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
        voltarAoMenu();
    }

    public void mostrarVitoria() {
        GerenciadorRanking ranking = new GerenciadorRanking();
        ranking.salvarPartida(jogador.getNome(), jogador.getScore(), jogador.getWaveAtual());
        JOptionPane.showMessageDialog(this,
            "Voce venceu a simulacao!\nScore: " + jogador.getScore() + "\nWave: " + jogador.getWaveAtual(),
            "Vitoria!", JOptionPane.INFORMATION_MESSAGE);
        voltarAoMenu();
    }

    private void voltarAoMenu() {
        new TelaPrincipal().setVisible(true);
        this.dispose();
    }

    private class PainelTabuleiro extends JPanel {

        public PainelTabuleiro() {
            setDoubleBuffered(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

            // Calcula quanto do turno atual ja passou (de 0.0 a 1.0)
            double progresso = Math.min(1.0, (System.currentTimeMillis() - inicioTurnoMs) / (double) DURACAO_TURNO_MS);

            // 1. Fundo do mapa
            if (imgFundoMapa != null) {
                g2.drawImage(imgFundoMapa, 0, 0, getWidth(), getHeight(), this);
            }

            // 2. Slots livres e tartarugas plantadas
            for (int lin = 0; lin < mapa.getLinhas(); lin++) {
                for (int col = 0; col < mapa.getColunas(); col++) {
                    Celula c = mapa.getCelula(lin, col);
                    int x = col * TAM_CELULA;
                    int y = lin * TAM_CELULA;

                    if (c.estaLivre()) {
                        g2.setColor(new Color(220, 200, 163, 95));
                        g2.fillRoundRect(x + 8, y + 8, TAM_CELULA - 16, TAM_CELULA - 16, 12, 12);
                        g2.setColor(new Color(31, 61, 46, 160));
                        g2.drawRoundRect(x + 8, y + 8, TAM_CELULA - 16, TAM_CELULA - 16, 12, 12);
                    }

                    if (c.estaOcupada()) {
                        // Se atacou neste turno, da um pulso (cresce no inicio e volta ao normal)
                        int bonusPulso = 0;
                        if (tartarugasAtacando.contains(c) && progresso < 0.4) {
                            bonusPulso = (int) (10 * Math.sin((progresso / 0.4) * Math.PI));
                        }

                        int tamTorre = (TAM_CELULA - 16) + bonusPulso;
                        int offTorre = (TAM_CELULA - tamTorre) / 2;

                        if (imgTartaruga != null) {
                            g2.drawImage(imgTartaruga, x + offTorre, y + offTorre, tamTorre, tamTorre, this);
                        } else {
                            g2.setColor(new Color(31, 61, 46));
                            g2.fillOval(x + offTorre, y + offTorre, tamTorre, tamTorre);
                        }

                        String sigla = c.getTartaruga().getClass().getSimpleName().replace("Tartaruga", "");
                        g2.setColor(Color.WHITE);
                        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                        g2.drawString(sigla, x + 12, y + TAM_CELULA - 6);
                    }
                }
            }

            // 3. Inimigos deslizando suavemente entre a celula anterior e a atual
            for (InimigoLixo lixo : inimigosAtivos) {
                int[] ant = posAnteriorInimigo.getOrDefault(lixo, new int[]{lixo.getPosX(), lixo.getPosY()});
                double colSuave = ant[0] + (lixo.getPosX() - ant[0]) * progresso;
                double linSuave = ant[1] + (lixo.getPosY() - ant[1]) * progresso;

                int x = (int) (colSuave * TAM_CELULA);
                int y = (int) (linSuave * TAM_CELULA);

                double proporcaoVida = lixo.getVidaAtual() / (double) lixo.getVidaMaxima();
                boolean tomouDano = lixo.getVidaAtual() < lixo.getVidaMaxima();

                // Encolhe conforme perde vida (de 42px ate 22px)
                int tamanho = (int) (22 + 20 * proporcaoVida);
                int offset = (TAM_CELULA - tamanho) / 2;

                Image sprite = imgPlastico;
                if (lixo instanceof LixoGarrafa) sprite = imgGarrafa;
                else if (lixo instanceof LixoRede) sprite = imgRede;

                if (sprite != null) {
                    g2.drawImage(sprite, x + offset, y + offset, tamanho, tamanho, this);
                } else {
                    g2.setColor(Color.RED);
                    g2.fillRect(x + offset, y + offset, tamanho, tamanho);
                }

                // Brilho vermelho ao redor se ja tomou dano
                if (tomouDano) {
                    g2.setColor(new Color(230, 57, 70, 110));
                    g2.fillOval(x + offset - 2, y + offset - 2, tamanho + 4, tamanho + 4);
                    g2.setColor(new Color(230, 57, 70));
                    g2.setStroke(new BasicStroke(2));
                    g2.drawOval(x + offset - 2, y + offset - 2, tamanho + 4, tamanho + 4);
                }

                // Barra de vida
                int larguraBarra = 44;
                int vidaAtual = (int) (proporcaoVida * larguraBarra);
                g2.setColor(Color.DARK_GRAY);
                g2.fillRect(x + 10, y + 4, larguraBarra, 6);
                g2.setColor(new Color(230, 57, 70));
                g2.fillRect(x + 10, y + 4, Math.max(0, vidaAtual), 6);
            }

            // Sincroniza o buffer grafico com o sistema operacional para evitar engasgos
            Toolkit.getDefaultToolkit().sync();
        }
    }
}
