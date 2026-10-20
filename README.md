# Tartarugas vs. Lixo Marinho

Projeto de Desenvolvimento de Software para a disciplina **CCF313 - Programação Orientada a Objetos** da **Universidade Federal de Viçosa (UFV) - Campus Florestal**.

**Professor:** Philipe de Freitas Melo  
**Entrega Parcial:** 19/10/2026

---

## 1. Sobre o Projeto

O projeto consiste em um jogo de **Tower Defense** em 2D desenvolvido em Java, utilizando uma **interface gráfica simples** (GUI em JavaFX ou Swing). O tema central é a conscientização ecológica sobre a poluição marinha e o descarte de dejetos antrópicos nos oceanos.

* **Mecânica Principal:** O jogador deve proteger a costa litorânea posicionando diferentes espécies de tartarugas marinhas (que atuam como torres de defesa). As tartarugas atacam resíduos sólidos (garrafas plásticas, redes de pesca, microplásticos) que avançam em direção à praia.
* **Economia e Progresso:** Destruir dejetos gera conchas (moeda do jogo) para comprar novas unidades ou realizar upgrades. Ao final de cada partida, os recordes e estatísticas são gravados no sistema de ranking.

---

## 2. Divisão de Responsabilidades (8 Integrantes)

Para manter o desenvolvimento estruturado e garantir a colaboração ativa no GitHub, as responsabilidades foram divididas conforme o planejamento da equipe:

| # | Integrante | Módulo | Atribuições Principais |
| :---: | :--- | :--- | :--- |
| **1** | **Lethícia Gonçalves da Silva** | Base do Combate | Classe abstrata `Entidade.java`, posições e lógica matemática de colisões no mapa (`CollisionService.java`) |
| **2** | **Tomás Viana Resende** | Tartarugas / Torres | Classe abstrata `TorreTartaruga.java`, espécies (`TartarugaDeCouro`, `TartarugaDePente`, `TartarugaVerde`), `Projetil.java` e mecânica de upgrades |
| **3** | **Bruna Lima Ribeiro** | Lixos / Inimigos | Classe abstrata `InimigoLixo.java` e tipos de dejetos (`GarrafaPet`, `RedeFantasma`, `Microplastico`) com escalonamento por round |
| **4** | **Helena Santos Perez** | Motor do Jogo & Exceções | Loop principal (`GameLoop.java`), controle de ondas (`WaveService.java`) e exceções customizadas (`PosicaoInvalidaException`, `RecursosInsuficientesException`) |
| **5** | **Otávio Soares Pedroso** | Mapa & Jogador | Lógica do mapa/caminho (`Mapa.java`, `CelulaGrid.java`), gestão de moedas/vida (`Jogador.java`) e regras de compra (`LojaService.java`) |
| **6** | **Pedro Mendes Gonzaga** | Persistência & Arquivos | Classes Repository para salvar e carregar dados em arquivos de texto (saves de partidas, ranking e configurações) |
| **7** | **Marina Larissa Xavier Soares** | Interface da Partida & Controller | Tela gráfica principal do jogo (JavaFX/Swing) com renderização do mapa, animações e cliques do mouse (`GameController.java`, `TelaJogoView.java`) |
| **8** | **Caroline Melo Silva** | Menus & Testes Unitários | Telas gráficas de menu e ranking (`TelaMenuView.java`, `TelaRankingView.java`) e suíte de testes unitários com JUnit |

---

## 3. Planejamento das 5 Funcionalidades Completas (CRUD)

O sistema foi planejado para contemplar 5 funcionalidades completas com operações de Create, Read, Update e Delete:

1. **Gerenciamento de Ranking e Recordes** *(Implementada na entrega parcial)*: Cadastrar pontuação, consultar Top 10, atualizar pontuação de jogadores e excluir registros.
2. **Gerenciamento de Torres no Tabuleiro** *(Planejada)*: Comprar/posicionar tartarugas, consultar propriedades no grid, realizar upgrade e vender/remover unidades.
3. **Gerenciamento de Jogador e Perfis** *(Planejada)*: Cadastro de jogador, consulta de estatísticas, edição de preferências e exclusão de perfil.
4. **Gerenciamento de Ondas de Dejetos** *(Planejada)*: Criação e configuração de novas ondas, consulta de inimigos da onda, ajuste de dificuldade e remoção de ondas.
5. **Gerenciamento de Partidas Salvas (Save Game)** *(Planejada)*: Gravação do estado da partida em arquivo, listagem de saves, atualização/sobrescrita de progresso e exclusão de saves.

---

## 4. Como Executar o Protótipo (Entrega Parcial)

A entrega parcial conta com uma classe principal executável (`Main.java`) que instancia os modelos de domínio e executa o ciclo completo de testes do CRUD de Ranking no terminal.

### Opção 1: Pela IDE (VS Code / Antigravity / Eclipse)
1. Abra a pasta do projeto `PDS_POO` no editor.
2. Abra o arquivo `src/br/ufv/ccf313/projeto/Main.java`.
3. Clique no botão **Run** que aparece logo acima do método `public static void main(String[] args)`.

### Opção 2: Pelo Terminal (PowerShell / Bash)
Na raiz da pasta `PDS_POO`, compile e execute:

```powershell
# 1. Compilar as classes
javac -d target/classes src/br/ufv/ccf313/projeto/model/*.java src/br/ufv/ccf313/projeto/service/*.java src/br/ufv/ccf313/projeto/Main.java

# 2. Executar o protótipo
java -cp target/classes br.ufv.ccf313.projeto.Main
```

---

## 5. Como Executar os Testes Unitários (JUnit 5)

A suíte de testes automatizados da funcionalidade de Ranking está localizada em:  
`src/br/ufv/ccf313/projeto/test/GerenciadorRankingTest.java`

Os testes cobrem todas as operações do CRUD:
* `testAdicionarEOrdenarRanking()`: Inserção e ordenação automática decrescente por pontuação.
* `testBuscarRegistroExistenteENaoExistente()`: Consulta por nome de jogador cadastrado e tratamento de não cadastrado.
* `testAtualizarRegistro()`: Atualização de pontuação e ondas sobrevividas de um jogador existente.
* `testRemoverRegistro()`: Exclusão de registro e validação da redução do total cadastrado.
* `testNomeInvalido()`: Validação e tratamento de exceção (`IllegalArgumentException`) ao tentar cadastrar nomes vazios.

### Pela IDE:
Abra `GerenciadorRankingTest.java` e clique no botão verde **Run Test** acima do nome da classe para rodar todos os testes de uma vez.

---

## 6. Estrutura de Pacotes

```text
src/br/ufv/ccf313/projeto/
├── Main.java              # Ponto de entrada demonstrativo (Entrega Parcial)
├── model/                 # Classes de domínio e POJOs (Entidade, Torres, Inimigos, Ranking)
├── service/               # Regras de negócio (GerenciadorRanking, LojaService, WaveService)
├── repository/            # Persistência em arquivos (planejada)
├── controller/            # Mediação de comandos (GameController)
├── view/                  # Interface gráfica (JavaFX / Swing - planejada)
└── test/                  # Testes unitários (GerenciadorRankingTest)
```