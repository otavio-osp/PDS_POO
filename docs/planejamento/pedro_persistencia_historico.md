# 📜 Planejamento Individual: Pedro Mendes Gonzaga
## Módulo: Persistência em Arquivo (.txt) & Gerenciamento de Histórico/Ranking

---

### 1. Suas Responsabilidades Principais
1. **Gravação do Histórico em Arquivo Texto (`.txt`):**
   - Ao término de cada partida (seja por vitória após a 10ª wave ou por derrota com vida zerada), salvar os dados da sessão em um arquivo permanente (ex: `historico_partidas.txt` ou `ranking.txt`).
   - Dados obrigatórios a serem gravados por linha:
     - Nome do Jogador (ex: "Pedro", "Otavio", etc.).
     - Pontuação Final (Score acumulado pela soma dos XPs e bônus).
     - Onda Máxima Alcançada (ex: "Wave 8" ou "Wave 10 - Vitória").
     - Data e Hora da Partida (para auditoria e desempate).
2. **Leitura e Ordenação de Ranking:**
   - Ler o arquivo `.txt` ao iniciar o jogo ou quando o usuário clicar no botão "Histórico / Ranking".
   - Processar os registros e ordená-los decrescentemente pelo Score (quem fez mais pontos fica no topo).
   - Disponibilizar os dados prontos para a Caroline e a Marina exibirem na interface.

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre o Formato do Arquivo `.txt`:
1. **Qual formato delimitador será usado no arquivo?**  
   - *Opção CSV com ponto e vírgula:* `Pedro;1450;10;14/11/2026 16:30`
   - *Opção Chave-Valor:* `Nome=Pedro|Score=1450|Wave=10|Data=14/11/2026 16:30`  
   - *Dica:* A opção delimitada por ponto e vírgula (`;`) é a mais fácil e rápida de ler com `String.split(";")` e compatível com o `BufferedReader`.

2. **Onde o arquivo será salvo no projeto?**  
   - Recomenda-se uma pasta `data/` na raiz do projeto (ex: `PDS_POO/data/ranking.txt`).
   - Se o arquivo ainda não existir na primeira execução da máquina, o código deve criá-lo automaticamente sem quebrar (`IOException`)?

#### 🔹 Sobre Tratamento de Erros e Exceções:
3. **Quais exceções devem ser tratadas obrigatoriamente?**  
   - `FileNotFoundException`, `IOException` e `NumberFormatException` (caso alguém edite o arquivo manualmente e coloque letras no score).
   - O código deve emitir alerta para a interface gráfica (Caroline) se houver falha de permissão de escrita?

#### 🔹 Sobre Ordenação e Desempate:
4. **Critério de Desempate:**  
   - Se dois jogadores empatarem com a mesma pontuação (ex: 1200 pontos):
     - Critério 1: Quem alcançou a maior Wave ganha a posição?
     - Critério 2: Quem jogou primeiro (data mais antiga)?
5. **Limite de Exibição no Ranking:**  
   - O ranking exibirá apenas o Top 10 melhores pontuações ou a lista completa com scroll?
6. **Mapeamento para Objetos em Memória:**  
   - Como a classe `RegistroRanking` já foi criada na primeira entrega com a interface `Comparable<RegistroRanking>`, você usará diretamente `Collections.sort(lista)` para manter a ordenação automática garantida?

