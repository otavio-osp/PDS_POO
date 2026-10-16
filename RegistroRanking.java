public class RegistroRanking implements Comparable<RegistroRanking> {
    private String nomeJogador;
    private int pontuacaoTotal;
    private int ondasSobrevividas;

    public RegistroRanking(String nomeJogador, int pontuacaoTotal, int ondasSobrevividas) {
        this.nomeJogador = nomeJogador;
        this.pontuacaoTotal = pontuacaoTotal;
        this.ondasSobrevividas = ondasSobrevividas;
    }

    public String getNomeJogador() { return nomeJogador; }
    public int getPontuacaoTotal() { return pontuacaoTotal; }
    public int getOndasSobrevividas() { return ondasSobrevividas; }

    // Ordena do maior para o menor (Ordem decrescente de pontuação)
    @Override
    public int compareTo(RegistroRanking outro) {
        return Integer.compare(outro.getPontuacaoTotal(), this.pontuacaoTotal);
    }

    @Override
    public String toString() {
        return nomeJogador + " - Pontos: " + pontuacaoTotal + " (Ondas: " + ondasSobrevividas + ")";
    }
}