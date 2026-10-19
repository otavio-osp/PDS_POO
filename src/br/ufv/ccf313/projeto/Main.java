package br.ufv.ccf313.projeto;

import br.ufv.ccf313.projeto.model.RegistroRanking;
import br.ufv.ccf313.projeto.model.TartarugaDeCouro;
import br.ufv.ccf313.projeto.model.TartarugaDePente;
import br.ufv.ccf313.projeto.model.TorreTartaruga;
import br.ufv.ccf313.projeto.service.GerenciadorRanking;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println(" CCF313 - Programacao Orientada a Objetos (UFV)");
        System.out.println(" Projeto: Tartarugas vs. Lixo Marinho");
        System.out.println(" Demonstracao de Execucao (Entrega Parcial)");
        System.out.println("==========================================================\n");

        // 1. Demonstracao de Instanciacao de Classes e Heranca
        System.out.println("--- 1. Instanciacao de Torres (Polimorfismo e Heranca) ---");
        TorreTartaruga torre1 = new TartarugaDeCouro(2, 3);
        TorreTartaruga torre2 = new TartarugaDePente(4, 5);

        System.out.println("Torre 1: " + torre1);
        System.out.println("Torre 2: " + torre2);
        System.out.println();

        // 2. Demonstracao da Funcionalidade Completa (CRUD de Ranking)
        System.out.println("--- 2. Funcionalidade Completa: CRUD de Ranking ---");
        GerenciadorRanking ranking = new GerenciadorRanking();

        // [C] CREATE - Insercao de registros
        System.out.println("\n[CREATE] Cadastrando pontuacoes no ranking...");
        ranking.adicionarRegistro("Lethicia", 1800, 6);
        ranking.adicionarRegistro("Tomas", 2900, 9);
        ranking.adicionarRegistro("Pedro", 1200, 4);
        ranking.adicionarRegistro("Caroline", 3200, 10);
        System.out.println("Total de registros cadastrados: " + ranking.totalRegistros());

        // [R] READ - Consulta dos top registros e busca individual
        System.out.println("\n[READ] Consultando Top 3 melhores pontuacoes:");
        List<RegistroRanking> top = ranking.getTopRegistros(3);
        for (int i = 0; i < top.size(); i++) {
            System.out.println("  " + (i + 1) + "# " + top.get(i));
        }

        System.out.println("\n[READ] Buscando registro individual de 'Tomas':");
        RegistroRanking busca = ranking.buscarRegistro("Tomas");
        if (busca != null) {
            System.out.println("  Encontrado: " + busca);
        }

        // [U] UPDATE - Atualizacao de registro existente
        System.out.println("\n[UPDATE] Atualizando pontuacao de 'Pedro' para novo recorde (3500 pontos, 10 ondas)...");
        ranking.atualizarRegistro("Pedro", 3500, 10);
        RegistroRanking pedroAtualizado = ranking.buscarRegistro("Pedro");
        System.out.println("  Novo registro de Pedro: " + pedroAtualizado);

        System.out.println("\n[READ] Novo Top 1 apos atualizacao:");
        List<RegistroRanking> novoTop = ranking.getTopRegistros(1);
        System.out.println("  1# " + novoTop.get(0));

        // [D] DELETE - Remocao de um registro
        System.out.println("\n[DELETE] Removendo registro de 'Lethicia'...");
        boolean removido = ranking.removerRegistro("Lethicia");
        System.out.println("  Removido com sucesso? " + removido);
        System.out.println("  Total de registros restante: " + ranking.totalRegistros());

        System.out.println("\n==========================================================");
        System.out.println(" Demonstracao concluida com sucesso!");
        System.out.println("==========================================================");
    }
}
