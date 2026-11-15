package br.ufv.ccf313.projeto.service;

import br.ufv.ccf313.projeto.model.RegistroRanking;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GerenciadorRanking {
    private static final String CAMINHO_ARQUIVO = "data/ranking.txt";
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

    // Salva uma partida no final do arquivo (formato: Nome;Score;Wave)
    // Chamada pela logica de Game Over ao fim de cada partida
    // Cria a pasta data/ e o arquivo automaticamente se nao existirem
    public void salvarPartida(String nome, int score, int wave) {
        File arquivo = new File(CAMINHO_ARQUIVO);
        arquivo.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
            writer.write(nome + ";" + score + ";" + wave);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar partida no arquivo: " + e.getMessage());
        }
    }

    // Le o arquivo ranking.txt e carrega todos os registros na lista em memoria
    // Retorna a lista ordenada por score (maior primeiro)
    // Se o arquivo nao existir, retorna lista vazia sem erro
    // Se qualquer linha estiver malformada, aborta a leitura toda e imprime o erro no console
    public List<RegistroRanking> carregarDoArquivo() {
        records.clear();
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                String nome = partes[0];
                int score = Integer.parseInt(partes[1]);
                int wave = Integer.parseInt(partes[2]);
                records.add(new RegistroRanking(nome, score, wave));
            }
            Collections.sort(records);
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Erro ao carregar ranking do arquivo: " + e.getMessage());
            records.clear();
        }
        return new ArrayList<>(records);
    }

    // Apaga todo o conteudo do arquivo de ranking
    // Chamada pelo botao "Limpar Historico" na tela de historico (Carol)
    public void limparHistorico() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, false))) {
            writer.write("");
        } catch (IOException e) {
            System.out.println("Erro ao limpar historico: " + e.getMessage());
        }
        records.clear();
    }
}
