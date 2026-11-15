package br.ufv.ccf313.projeto.model.mapa;

import br.ufv.ccf313.projeto.model.tartaruga.TorreTartaruga;
import java.util.ArrayList;
import java.util.List;

// Representa o mapa completo do jogo como uma grade de celulas.
// OTAVIO: definir o tamanho da grade e montar o caminho predeterminado dos inimigos.
// O caminho e uma lista ordenada de posicoes [linha, coluna] que os inimigos seguem do inicio ao fim.
public class Mapa {

    private Celula[][] grade;
    private int linhas;
    private int colunas;

    // Sequencia ordenada de posicoes que os inimigos percorrem.
    // Cada elemento e um array {linha, coluna}.
    // OTAVIO: preencher este caminho ao construir o mapa.
    private List<int[]> caminho;

    // OTAVIO: definir o tamanho da grade e chamar inicializarCaminho() no construtor.
    public Mapa(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.grade = new Celula[linhas][colunas];
        this.caminho = new ArrayList<>();
        inicializarGrade();
    }

    // Cria todas as celulas como LIVRE por padrao.
    // OTAVIO: apos chamar este metodo, marcar as celulas do caminho como CAMINHO.
    private void inicializarGrade() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                grade[i][j] = new Celula(i, j, TipoCelula.LIVRE);
            }
        }
    }

    // Marca uma celula como parte do caminho dos inimigos e adiciona na lista ordenada.
    // OTAVIO: chamar este metodo para cada celula do rio/mar, na ordem que os inimigos percorrem.
    public void adicionarCelulaCaminho(int linha, int coluna) {
        grade[linha][coluna] = new Celula(linha, coluna, TipoCelula.CAMINHO);
        caminho.add(new int[]{linha, coluna});
    }

    // Tenta posicionar uma tartaruga em uma celula LIVRE.
    // Retorna true se conseguiu, false se a celula nao estiver disponivel.
    // Chamado por Carol (interface) apos verificar o dinheiro do jogador.
    public boolean colocarTartaruga(int linha, int coluna, TorreTartaruga torre) {
        Celula celula = getCelula(linha, coluna);
        if (celula == null) return false;
        return celula.colocarTartaruga(torre);
    }

    // Remove a tartaruga de uma celula OCUPADA.
    // OTAVIO: definir se o jogador recebe algum reembolso ao remover.
    public void removerTartaruga(int linha, int coluna) {
        Celula celula = getCelula(linha, coluna);
        if (celula != null) celula.removerTartaruga();
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

    // Retorna a sequencia de posicoes do caminho dos inimigos.
    // Usado pela logica de combate (Leticia) e pelos inimigos (Bruna) para movimentacao.
    public List<int[]> getCaminho() {
        return caminho;
    }

    // Retorna o indice da posicao final do caminho (onde o inimigo causa dano ao jogador).
    public int[] getPosicaoFinal() {
        if (caminho.isEmpty()) return null;
        return caminho.get(caminho.size() - 1);
    }

    public int getLinhas() { return linhas; }
    public int getColunas() { return colunas; }
}
