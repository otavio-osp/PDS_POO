package br.ufv.ccf313.projeto.exception;

public class WaveInvalidaException extends Exception {

    public WaveInvalidaException() {
        super("Operacao invalida para a wave atual.");
    }

    public WaveInvalidaException(String mensagem) {
        super(mensagem);
    }

    public WaveInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
