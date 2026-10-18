package br.ufv.ccf313.projeto.service;

import br.ufv.ccf313.projeto.model.RegistroRanking;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GerenciadorRanking {
    private List<RegistroRanking> records;

    public GerenciadorRanking() {
        this.records = new ArrayList<>();
    }

    // Método para ser chamado ao fim do GameLoop
    public void adicionarRegistro(String nome, int pontuacao, int ondas) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do jogador não pode ser vazio.");
        }
        records.add(new RegistroRanking(nome, pontuacao, ondas));
        Collections.sort(records); // Mantém a lista ordenada sempre que insere
    }

    public List<RegistroRanking> getTopRegistros(int quantidade) {
        if (records.isEmpty()) return new ArrayList<>();
        int limite = Math.min(quantidade, records.size());
        return new ArrayList<>(records.subList(0, limite));
    }
}
