package br.ufv.ccf313.projeto.model.mapa;

import br.ufv.ccf313.projeto.exception.PosicaoInvalidaException;
import br.ufv.ccf313.projeto.model.tartaruga.TorreTartaruga;
import java.util.ArrayList;
import java.util.List;

// Representa o mapa completo do jogo como uma grade de celulas.
// O caminho e uma lista ordenada de posicoes [linha, coluna] que os inimigos seguem do inicio ao fim.
public class Mapa {

    public static final int LINHAS_PADRAO = 10;
    public static final int COLUNAS_PADRAO = 14;

    private Celula[][] grade;
    private int linhas;
    private int colunas;

    // Sequencia ordenada de posicoes que os inimigos percorrem.
    // Cada elemento e um array {linha, coluna}.
    private List<int[]> caminho;

    private List<int[]> slotsFixos;

    public Mapa() {
        if (LINHAS_PADRAO <= 0 || COLUNAS_PADRAO <= 0) {
            throw new IllegalArgumentException("Dimensoes invalidas.");
        }
        this.linhas = LINHAS_PADRAO;
        this.colunas = COLUNAS_PADRAO;
        this.grade = new Celula[linhas][colunas];
        this.caminho = new ArrayList<>();
        this.slotsFixos = new ArrayList<>();

        inicializarGrade(TipoCelula.BLOQUEADA);
        inicializarCaminhoPadrao();
        inicializarSlotsFixosPadrao();
    }

    public Mapa(int linhas, int colunas) {
        if (linhas <= 0 || colunas <= 0) {
            throw new IllegalArgumentException("Dimensoes do mapa devem ser maiores que zero.");
        }
        this.linhas = linhas;
        this.colunas = colunas;
        this.grade = new Celula[linhas][colunas];
        this.caminho = new ArrayList<>();
        this.slotsFixos = new ArrayList<>();
        inicializarGrade(TipoCelula.LIVRE);
    }

    // Cria todas as celulas como LIVRE por padrao.
    private void inicializarGrade(TipoCelula tipoPadrao) {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                grade[i][j] = new Celula(i, j, tipoPadrao);
            }
        }
    }

    public void inicializarCaminhoPadrao() {
        for (int c = 0; c <= 6; c++) {
            adicionarCelulaCaminho(2, c);
        }
        for (int l = 3; l <= 5; l++) {
            adicionarCelulaCaminho(l, 6);
        }
        for (int c = 5; c >= 2; c--) {
            adicionarCelulaCaminho(5, c);
        }
        for (int l = 6; l <= 7; l++) {
            adicionarCelulaCaminho(l, 2);
        }
        for (int c = 3; c <= 13; c++) {
            adicionarCelulaCaminho(7, c);
        }
    }

    public void inicializarSlotsFixosPadrao() {
        adicionarSlotTorre(1, 1);
        adicionarSlotTorre(1, 3);
        adicionarSlotTorre(1, 5);
        adicionarSlotTorre(3, 1);
        adicionarSlotTorre(3, 3);
        adicionarSlotTorre(3, 5);

        adicionarSlotTorre(3, 7);
        adicionarSlotTorre(4, 7);

        adicionarSlotTorre(4, 2);
        adicionarSlotTorre(4, 4);

        adicionarSlotTorre(6, 1);
        adicionarSlotTorre(7, 1);

        adicionarSlotTorre(6, 4);
        adicionarSlotTorre(6, 6);
        adicionarSlotTorre(6, 8);
        adicionarSlotTorre(6, 10);
        adicionarSlotTorre(6, 12);

        adicionarSlotTorre(8, 3);
        adicionarSlotTorre(8, 5);
        adicionarSlotTorre(8, 7);
        adicionarSlotTorre(8, 9);
        adicionarSlotTorre(8, 11);
    }

    // Marca uma celula como parte do caminho dos inimigos e adiciona na lista ordenada.
    public void adicionarCelulaCaminho(int linha, int coluna) {
        if (linha < 0 || linha >= linhas || coluna < 0 || coluna >= colunas) {
            throw new IllegalArgumentException("Coordenada fora dos limites do mapa: (" + linha + ", " + coluna + ")");
        }
        grade[linha][coluna] = new Celula(linha, coluna, TipoCelula.CAMINHO);
        caminho.add(new int[]{linha, coluna});
    }

    public void adicionarSlotTorre(int linha, int coluna) {
        if (linha < 0 || linha >= linhas || coluna < 0 || coluna >= colunas) {
            throw new IllegalArgumentException("Coordenada de slot fora dos limites: (" + linha + ", " + coluna + ")");
        }
        if (grade[linha][coluna].eCaminho()) {
            throw new IllegalArgumentException("Nao e permitido criar um slot de tartaruga dentro do fluxo do rio.");
        }
        grade[linha][coluna] = new Celula(linha, coluna, TipoCelula.LIVRE);
        slotsFixos.add(new int[]{linha, coluna});
    }

    public void posicionarTartaruga(int linha, int coluna, TorreTartaruga torre) throws PosicaoInvalidaException {
        if (torre == null) {
            throw new IllegalArgumentException("TorreTartaruga nao pode ser nula.");
        }
        Celula celula = getCelula(linha, coluna);
        if (celula == null) {
            throw new PosicaoInvalidaException("Posicao fora dos limites do mapa: (" + linha + ", " + coluna + ")");
        }
        if (celula.eCaminho()) {
            throw new PosicaoInvalidaException("Posicao invalida: Nao e possivel posicionar uma tartaruga no fluxo do rio/caminho.");
        }
        if (celula.estaBloqueada()) {
            throw new PosicaoInvalidaException("Posicao invalida: Terreno de cenario bloqueado. As tartarugas so podem ser alocadas nos pontos fixos de defesa as margens do rio.");
        }
        if (celula.estaOcupada()) {
            throw new PosicaoInvalidaException("Posicao invalida: Ponto de defesa ja ocupado por outra tartaruga.");
        }
        celula.colocarTartaruga(torre);
    }

    // Tenta posicionar uma tartaruga em uma celula LIVRE.
    // Retorna true se conseguiu, false se a celula nao estiver disponivel.
    // Chamado por Carol (interface) apos verificar o dinheiro do jogador.
    public boolean colocarTartaruga(int linha, int coluna, TorreTartaruga torre) {
        Celula celula = getCelula(linha, coluna);
        if (celula == null) return false;
        return celula.colocarTartaruga(torre);
    }

    public boolean isPosicaoValidaParaTorre(int linha, int coluna) {
        Celula celula = getCelula(linha, coluna);
        return celula != null && celula.estaLivre();
    }

    // Remove a tartaruga de uma celula OCUPADA.
    public TorreTartaruga removerTartaruga(int linha, int coluna) {
        Celula celula = getCelula(linha, coluna);
        if (celula != null && celula.estaOcupada()) {
            return celula.removerTartaruga();
        }
        return null;
    }

    // Retorna a celula na posicao informada, ou null se fora dos limites.
    public Celula getCelula(int linha, int coluna) {
        if (linha < 0 || linha >= linhas || coluna < 0 || coluna >= colunas) return null;
        return grade[linha][coluna];
    }

    // Retorna todas as celulas que possuem uma tartaruga posicionada.
    // Usado pela logica de combate (Leticia) para saber quais tartarugas estao ativas.
    public List<Celula> getCelulasComTartaruga() {
        List<Celula> resultado = new ArrayList<>();
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (grade[i][j].estaOcupada()) {
                    resultado.add(grade[i][j]);
                }
            }
        }
        return resultado;
    }

    public List<Celula> getSlotsDisponiveis() {
        List<Celula> resultado = new ArrayList<>();
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (grade[i][j].estaLivre()) {
                    resultado.add(grade[i][j]);
                }
            }
        }
        return resultado;
    }

    // Retorna a sequencia de posicoes do caminho dos inimigos.
    // Usado pela logica de combate (Leticia) e pelos inimigos (Bruna) para movimentacao.
    public List<int[]> getCaminho() {
        return caminho;
    }

    public List<int[]> getSlotsFixos() {
        return new ArrayList<>(slotsFixos);
    }

    public int[] getPosicaoInicial() {
        if (caminho == null || caminho.isEmpty()) return null;
        return caminho.get(0);
    }

    // Retorna o indice da posicao final do caminho (onde o inimigo causa dano ao jogador).
    public int[] getPosicaoFinal() {
        if (caminho == null || caminho.isEmpty()) return null;
        return caminho.get(caminho.size() - 1);
    }

    public void reiniciarTorres() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (grade[i][j].estaOcupada()) {
                    grade[i][j].removerTartaruga();
                }
            }
        }
    }

    public int getLinhas() { return linhas; }
    public int getColunas() { return colunas; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mapa (").append(linhas).append("x").append(colunas).append("):\n");
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                switch (grade[i][j].getTipo()) {
                    case CAMINHO -> sb.append("[~]");
                    case LIVRE -> sb.append("[O]");
                    case OCUPADA -> sb.append("[T]");
                    case BLOQUEADA -> sb.append("[.]");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
