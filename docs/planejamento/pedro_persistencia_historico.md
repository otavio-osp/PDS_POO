# 📜 Planejamento Individual: Pedro Mendes Gonzaga
## Módulo: Persistência em Arquivo (.txt) & Gerenciamento de Histórico/Ranking

---

### 1. Suas Responsabilidades Principais
1. **Gravação do Histórico em Arquivo Texto (`.txt`):**
   - Ao término de cada partida (vitória ou derrota/Game Over), salvar os dados da sessão em `data/ranking.txt`.
   - **Campos Salvos Acordados:**
     - Nome do Jogador (ex: "Pedro", "Otavio", etc.).
     - Pontuação Final (Score acumulado da soma de XP dos lixos abatidos).
     - Onda Alcançada (número inteiro da wave em que a partida encerrou).
     *(Nota de Alinhamento: Data e hora foram descartadas para simplificação do projeto).*
2. **Leitura e Ordenação de Ranking:**
   - Ler o arquivo `data/ranking.txt` via `GerenciadorRanking.carregarDoArquivo()`.
   - Processar os registros e ordená-los decrescentemente pelo Score (maior pontuação no topo).
   - Manter histórico cumulativo (duplicatas do mesmo nome são preservadas para acompanhar evolução).
   - Disponibilizar método `limparHistorico()` para zerar os registros caso o usuário clique no botão correspondente.

---

### 2. Definições Técnicas Fechadas e Decisões de Projeto

#### 🔹 Formato do Arquivo `.txt`:
- **Formato:** CSV delimitado por ponto e vírgula: `Nome;Score;Wave` (ex: `Pedro;1450;7`).
- **Localização:** `data/ranking.txt` relativo à raiz do projeto.
- **Criação Automática:** A pasta `data/` e o arquivo são criados automaticamente se não existirem no momento da primeira gravação.

#### 🔹 Tratamento de Erros e Exceções:
- Se o arquivo não existir na leitura, retorna lista vazia sem lançar exceção.
- Se uma linha do arquivo estiver malformada ou corrompida, a leitura é abortada e a mensagem de erro é exibida no console.
- Exceções de IO (`IOException`) são capturadas e registradas via console sem interromper o jogo.

#### 🔹 Ordenação e Exibição:
- **Ordenação:** Ordem decrescente de pontuação direta usando o `compareTo` de `RegistroRanking`. Em caso de empate, mantém a ordem de leitura.
- **Exibição:** Lista completa com barra de rolagem (`JScrollPane`) para visualização de todo o histórico.
- **Operação de Delete:** Implementada a limpeza geral do histórico (`limparHistorico()`), sem remoção de registros individuais.
