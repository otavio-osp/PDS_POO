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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TelaJogo extends JFrame {

    private static final int TAM_CELULA = 64;
    private static final int DURACAO_TURNO_MS = 1000;

    private Mapa mapa;
    private Jogador jogador;
    private JLabel labelVida;
    private JLabel labelDinheiro;
    private JLabel labelWave;
    private PainelTabuleiro painelTabuleiro;

    // Estruturas da partida
    private Queue<InimigoLixo> filaSpawn;
    private List<InimigoLixo> inimigosAtivos;
    private Map<InimigoLixo, Double> progressoCaminhoAnterior; // Indice exato no caminho antes do turno
    private Map<InimigoLixo, Double> progressoCaminhoAtual;    // Indice exato no caminho ao fim do turno
    private Map<InimigoLixo, Integer> contadorPassosRede;      // Controla meio-passo continuo do LixoRede
    private Map<Celula, String> direcaoTartaruga;
    private Map<Celula, Double> inclinacaoAtaque;

    private Timer timerLogica;
    private Timer timerAnimacao;
    private long inicioTurnoMs;
    private int contadorTicks = 0;

    // Imagens do mapa e telas auxiliares
    private Image imgFundoMapa;
    private Image imgAvisoConchas;
    private Image imgConstruirDefesa;

    // Sprites dos lixos (normal e vermelho)
    private Image imgPlastico, imgPlasticoVermelha;
    private Image imgGarrafa, imgGarrafaVermelha;
    private Image imgRede, imgRedeVermelha;

    // Sprites direcionais das tartarugas
    private Map<String, Image> spritesTartarugas;

    public TelaJogo(Mapa mapa, Jogador jogador) {
        this.mapa = mapa;
        this.jogador = jogador;
        this.filaSpawn = new LinkedList<>();
        this.inimigosAtivos = new ArrayList<>();
        this.progressoCaminhoAnterior = new HashMap<>();
        this.progressoCaminhoAtual = new HashMap<>();
        this.contadorPassosRede = new HashMap<>();
        this.direcaoTartaruga = new HashMap<>();
        this.inclinacaoAtaque = new HashMap<>();
        this.spritesTartarugas = new HashMap<>();

        carregarSprites();
        configurarJanela();
        criarHUD();
        criarTabuleiro();
        prepararWaveSimulada();
        iniciarLoops();
    }

    private void carregarSprites() {
        String base = "/br/ufv/ccf313/projeto/resource/imagens/";

        imgFundoMapa = carregarImagem(base + "mapa_fundo.jpg");
        if (imgFundoMapa == null) {
            imgFundoMapa = carregarImagem(base + "menus/mapa_fundo.jpg");
        }

        imgAvisoConchas = carregarImagem(base + "telas_auxiliares/tela_conchas_insuficientes.png");
        imgConstruirDefesa = carregarImagem(base + "telas_auxiliares/tela_construir_defesa.png");

        imgPlastico = carregarImagem(base + "lixos/lixo_sacola_plastica_24x24.png");
        imgPlasticoVermelha = carregarImagem(base + "lixos/lixo_sacola_plastica_24x24_vermelha.png");

        imgGarrafa = carregarImagem(base + "lixos/lixo_garrafa_plastica_24x24.png");
        imgGarrafaVermelha = carregarImagem(base + "lixos/lixo_garrafa_plastica_24x24_vermelha.png");

        imgRede = carregarImagem(base + "lixos/lixo_rede_pesca_24x24.png");
        imgRedeVermelha = carregarImagem(base + "lixos/lixo_rede_pesca_24x24_vermelha.png");

        String[] dirs = {"north", "south", "east", "west"};
        for (String d : dirs) {
            spritesTartarugas.put("TartarugaVerde_" + d,
                carregarImagem(base + "tartarugas/tartaruga_verde/tartaruga_verde_" + d + ".png"));
            spritesTartarugas.put("TartarugaDePente_" + d,
                carregarImagem(base + "tartarugas/tartaruga_de_pente/tartaruga_de_pente_" + d + ".png"));
            spritesTartarugas.put("TartarugaDeCouro_" + d,
                carregarImagem(base + "tartarugas/tartaruga_de_couro/tartaruga_de_couro_" + d + ".png"));
        }
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

        timerLogica = new Timer(DURACAO_TURNO_MS, e -> executarTurno());
        timerLogica.setInitialDelay(3000);
        timerLogica.start();

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
        inclinacaoAtaque.clear();

        // 1. Atualiza o indice anterior de cada lixo ativo antes de mover
        for (InimigoLixo lixo : inimigosAtivos) {
            double atual = progressoCaminhoAtual.getOrDefault(lixo, 0.0);
            progressoCaminhoAnterior.put(lixo, atual);
        }

        // 2. Solta um novo lixo a cada 3 turnos (espacamento equilibrado)
        if (contadorTicks % 3 == 1 && !filaSpawn.isEmpty()) {
            InimigoLixo novo = filaSpawn.poll();
            progressoCaminhoAnterior.put(novo, -1.0); // Entra deslizando pela margem esquerda
            progressoCaminhoAtual.put(novo, 0.0);
            contadorPassosRede.put(novo, 0);
            inimigosAtivos.add(novo);
        }

        // 3. Move os lixos ativos na grade e calcula o progresso continuo no caminho
        int maxIndice = mapa.getCaminho().size() - 1;
        for (InimigoLixo lixo : inimigosAtivos) {
            double indAnt = progressoCaminhoAtual.getOrDefault(lixo, 0.0);

            // Executa a logica oficial do InimigoLixo
            lixo.atualizar();

            if (lixo.getVelocidade() == 0) {
                // LixoRede: desliza 0.5 celula no 1o turno e 0.5 celula no 2o turno (nunca para!)
                int ticksRede = contadorPassosRede.getOrDefault(lixo, 0) + 1;
                contadorPassosRede.put(lixo, ticksRede);
                double novoIndice = Math.min(maxIndice, ticksRede * 0.5);
                progressoCaminhoAtual.put(lixo, novoIndice);
            } else {
                // LixoPlastico (1 celula/turno) e LixoGarrafa (2 celulas/turno)
                double novoIndice = Math.min(maxIndice, indAnt + lixo.getVelocidade());
                progressoCaminhoAtual.put(lixo, novoIndice);
            }
        }

        // 4. Tartarugas viram para o inimigo, inclinam e atacam
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
                    int dx = lixo.getPosX() - colTorre;
                    int dy = lixo.getPosY() - linTorre;
                    double dist = Math.hypot(dx, dy);

                    if (dist <= alcance) {
                        lixo.receberDano(dano);

                        if (Math.abs(dx) >= Math.abs(dy)) {
                            direcaoTartaruga.put(celula, (dx >= 0) ? "east" : "west");
                        } else {
                            direcaoTartaruga.put(celula, (dy >= 0) ? "south" : "north");
                        }

                        double anguloInclinacao = (dx >= 0) ? Math.toRadians(18) : Math.toRadians(-18);
                        inclinacaoAtaque.put(celula, anguloInclinacao);
                        break;
                    }
                }
            }
        }

        // 5. Verifica lixos destruidos ou que chegaram ao final
        Iterator<InimigoLixo> it = inimigosAtivos.iterator();
        while (it.hasNext()) {
            InimigoLixo lixo = it.next();
            if (lixo.estaDestruido()) {
                jogador.registrarInimigoDestruido(lixo.getRecompensa());
                limparDadosInimigo(lixo);
                it.remove();
            } else if (lixo.chegouAoFim()) {
                jogador.receberDano(lixo.getDano());
                limparDadosInimigo(lixo);
                it.remove();
            }
        }

        atualizarHUD();

        // 6. Fim de jogo
        if (jogador.getVida() <= 0) {
            pararLoops();
            mostrarGameOver();
        } else if (filaSpawn.isEmpty() && inimigosAtivos.isEmpty()) {
            pararLoops();
            mostrarVitoria();
        }
    }

    private void limparDadosInimigo(InimigoLixo lixo) {
        progressoCaminhoAnterior.remove(lixo);
        progressoCaminhoAtual.remove(lixo);
        contadorPassosRede.remove(lixo);
    }

    public void atualizarHUD() {
        labelVida.setText("Vida: " + jogador.getVida() + "   |   ");
        labelDinheiro.setText("Conchas: " + jogador.getDinheiro() + "   |   ");
        labelWave.setText("Wave: " + jogador.getWaveAtual());
    }

    public void abrirModalCompra(Celula celula) {
        if (timerLogica != null) timerLogica.stop();

        if (imgConstruirDefesa == null) {
            String[] opcoes = {
                "Tartaruga Verde (50 conchas)",
                "Tartaruga de Pente (100 conchas)",
                "Tartaruga de Couro (200 conchas)"
            };
            int escolha = JOptionPane.showOptionDialog(this, "Escolha a tartaruga:", "Construir Defesa",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
            processarCompra(celula, escolha);
            inicioTurnoMs = System.currentTimeMillis();
            if (timerLogica != null) timerLogica.start();
            return;
        }

        JDialog dialogo = new JDialog(this, true);
        dialogo.setUndecorated(true);
        dialogo.setBackground(new Color(0, 0, 0, 0));

        JPanel painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imgConstruirDefesa, 0, 0, getWidth(), getHeight(), this);
            }
        };
        painel.setOpaque(false);
        painel.setLayout(null);

        JButton btnFechar = criarBotaoModal(560, 66, 32, 32);
        btnFechar.addActionListener(e -> dialogo.dispose());

        JButton btnVerde = criarBotaoModal(150, 134, 132, 168);
        btnVerde.addActionListener(e -> {
            dialogo.dispose();
            processarCompra(celula, 0);
        });

        JButton btnPente = criarBotaoModal(294, 134, 132, 168);
        btnPente.addActionListener(e -> {
            dialogo.dispose();
            processarCompra(celula, 1);
        });

        JButton btnCouro = criarBotaoModal(438, 134, 132, 168);
        btnCouro.addActionListener(e -> {
            dialogo.dispose();
            processarCompra(celula, 2);
        });

        painel.add(btnFechar);
        painel.add(btnVerde);
        painel.add(btnPente);
        painel.add(btnCouro);

        dialogo.setContentPane(painel);
        dialogo.setSize(720, 405);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);

        inicioTurnoMs = System.currentTimeMillis();
        if (timerLogica != null) timerLogica.start();
    }

    private JButton criarBotaoModal(int x, int y, int largura, int altura) {
        JButton btn = new JButton();
        btn.setBounds(x, y, largura, altura);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void processarCompra(Celula celula, int escolha) {
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
        direcaoTartaruga.put(celula, "south");
        atualizarHUD();
    }

    public void mostrarAvisoSemDinheiro() {
        if (imgAvisoConchas == null) {
            JOptionPane.showMessageDialog(this, "Conchas insuficientes!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialogo = new JDialog(this, true);
        dialogo.setUndecorated(true);
        dialogo.setBackground(new Color(0, 0, 0, 0));

        JPanel painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imgAvisoConchas, 0, 0, getWidth(), getHeight(), this);
            }
        };
        painel.setOpaque(false);
        painel.setLayout(null);

        JButton botaoOk = new JButton();
        botaoOk.setBounds(160, 152, 180, 42);
        botaoOk.setOpaque(false);
        botaoOk.setContentAreaFilled(false);
        botaoOk.setBorderPainted(false);
        botaoOk.setFocusPainted(false);
        botaoOk.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoOk.addActionListener(e -> dialogo.dispose());

        painel.add(botaoOk);
        dialogo.setContentPane(painel);
        dialogo.setSize(520, 290);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
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

        // Calcula a posicao {colSuave, linSuave} seguindo cada celula da curva do caminho
        private double[] calcularPosicaoNoCaminho(double indiceFracionario) {
            List<int[]> caminho = mapa.getCaminho();
            if (caminho.isEmpty()) return new double[]{0, 0};

            if (indiceFracionario < 0) {
                int[] inicio = caminho.get(0);
                return new double[]{inicio[1] + indiceFracionario, inicio[0]};
            }

            int idx1 = (int) Math.floor(indiceFracionario);
            if (idx1 >= caminho.size() - 1) {
                int[] fim = caminho.get(caminho.size() - 1);
                return new double[]{fim[1], fim[0]};
            }

            int idx2 = idx1 + 1;
            double frac = indiceFracionario - idx1;
            int[] p1 = caminho.get(idx1); // {linha, coluna}
            int[] p2 = caminho.get(idx2); // {linha, coluna}

            double col = p1[1] + (p2[1] - p1[1]) * frac;
            double lin = p1[0] + (p2[0] - p1[0]) * frac;
            return new double[]{col, lin};
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

            double progressoTurno = Math.min(1.0, (System.currentTimeMillis() - inicioTurnoMs) / (double) DURACAO_TURNO_MS);

            // 1. Fundo do mapa
            if (imgFundoMapa != null) {
                g2.drawImage(imgFundoMapa, 0, 0, getWidth(), getHeight(), this);
            } else {
                g2.setColor(new Color(107, 127, 78));
                g2.fillRect(0, 0, getWidth(), getHeight());
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
                        String classeTorre = c.getTartaruga().getClass().getSimpleName();
                        String dir = direcaoTartaruga.getOrDefault(c, "south");
                        Image spriteTorre = spritesTartarugas.get(classeTorre + "_" + dir);

                        double angulo = 0.0;
                        if (inclinacaoAtaque.containsKey(c) && progressoTurno < 0.5) {
                            double pulso = Math.sin((progressoTurno / 0.5) * Math.PI);
                            angulo = inclinacaoAtaque.get(c) * pulso;
                        }

                        AffineTransform original = g2.getTransform();
                        int centroX = x + TAM_CELULA / 2;
                        int centroY = y + TAM_CELULA / 2;
                        g2.rotate(angulo, centroX, centroY);

                        if (spriteTorre != null) {
                            g2.drawImage(spriteTorre, x + 6, y + 6, TAM_CELULA - 12, TAM_CELULA - 12, this);
                        } else {
                            g2.setColor(new Color(31, 61, 46));
                            g2.fillOval(x + 12, y + 12, TAM_CELULA - 24, TAM_CELULA - 24);
                        }

                        g2.setTransform(original);
                    }
                }
            }

            // 3. Inimigos deslizando continuamente ao longo da curva do caminho
            for (InimigoLixo lixo : inimigosAtivos) {
                double indAnt = progressoCaminhoAnterior.getOrDefault(lixo, 0.0);
                double indAtual = progressoCaminhoAtual.getOrDefault(lixo, 0.0);
                double indSuave = indAnt + (indAtual - indAnt) * progressoTurno;

                double[] posSuave = calcularPosicaoNoCaminho(indSuave);
                int x = (int) (posSuave[0] * TAM_CELULA);
                int y = (int) (posSuave[1] * TAM_CELULA);

                double proporcaoVida = lixo.getVidaAtual() / (double) lixo.getVidaMaxima();
                boolean tomouDano = lixo.getVidaAtual() < lixo.getVidaMaxima();

                int tamanho = (int) (24 + 20 * proporcaoVida);
                int offset = (TAM_CELULA - tamanho) / 2;

                Image sprite;
                if (lixo instanceof LixoGarrafa) {
                    sprite = tomouDano ? imgGarrafaVermelha : imgGarrafa;
                } else if (lixo instanceof LixoRede) {
                    sprite = tomouDano ? imgRedeVermelha : imgRede;
                } else {
                    sprite = tomouDano ? imgPlasticoVermelha : imgPlastico;
                }

                if (sprite != null) {
                    g2.drawImage(sprite, x + offset, y + offset, tamanho, tamanho, this);
                } else {
                    g2.setColor(tomouDano ? Color.RED : Color.ORANGE);
                    g2.fillRect(x + offset, y + offset, tamanho, tamanho);
                }

                // Barra de vida
                int larguraBarra = 44;
                int vidaAtual = (int) (proporcaoVida * larguraBarra);
                g2.setColor(Color.DARK_GRAY);
                g2.fillRect(x + 10, y + 4, larguraBarra, 6);
                g2.setColor(new Color(230, 57, 70));
                g2.fillRect(x + 10, y + 4, Math.max(0, vidaAtual), 6);
            }

            Toolkit.getDefaultToolkit().sync();
        }
    }
}