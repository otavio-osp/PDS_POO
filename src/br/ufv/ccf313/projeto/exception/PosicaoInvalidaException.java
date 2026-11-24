package br.ufv.ccf313.projeto.exception;

public class PosicaoInvalidaException extends Exception {

    public PosicaoInvalidaException() {
        super("Posicao invalida no mapa para alocar a tartaruga.");
    }

    public PosicaoInvalidaException(String mensagem) {
        super(mensagem);
    }

    public PosicaoInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
