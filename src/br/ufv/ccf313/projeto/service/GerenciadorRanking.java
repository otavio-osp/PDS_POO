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

    // CREATE: adiciona um novo registro
    public void adicionarRegistro(String nome, int pontuacao, int ondas) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do jogador não pode ser vazio.");
        }
        records.add(new RegistroRanking(nome.trim(), pontuacao, ondas));
        Collections.sort(records);
    }

    // READ: busca registro pelo nome do jogador
    public RegistroRanking buscarRegistro(String nome) {
        if (nome == null) return null;
        for (RegistroRanking r : records) {
            if (r.getNomeJogador().equalsIgnoreCase(nome.trim())) {
                return r;
            }
        }
        return null;
    }

    // READ: retorna os top N registros
    public List<RegistroRanking> getTopRegistros(int quantidade) {
        if (records.isEmpty()) return new ArrayList<>();
        int limite = Math.min(quantidade, records.size());
        return new ArrayList<>(records.subList(0, limite));
    }

    // READ: retorna todos os registros
    public List<RegistroRanking> listarTodos() {
        return new ArrayList<>(records);
    }

    // UPDATE: atualiza a pontuação de um jogador existente
    public boolean atualizarRegistro(String nome, int novaPontuacao, int novasOndas) {
        RegistroRanking r = buscarRegistro(nome);
        if (r != null) {
            r.setPontuacaoTotal(novaPontuacao);
            r.setOndasSobrevividas(novasOndas);
            Collections.sort(records);
            return true;
        }
        return false;
    }

    // DELETE: remove um registro pelo nome
    public boolean removerRegistro(String nome) {
        RegistroRanking r = buscarRegistro(nome);
        if (r != null) {
            records.remove(r);
            return true;
        }
        return false;
    }

    public int totalRegistros() {
        return records.size();
    }
}
