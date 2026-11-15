# 🐢 TARTARUGAS VS. LIXO MARINHO
## Documento Mestre de Planejamento e Arquitetura (Etapa 2 - Sprint Desenvolvimento)
**Disciplina:** CCF313 – Programação Orientada a Objetos (UFV - Campus Florestal)  
**Professor:** Philipe de Freitas Melo  
**Projeto:** Jogo *Tower Defense* 2D em Java

---

## 1. Visão Geral da Arquitetura do Jogo

O jogo funciona em uma **malha discreta (matriz $N \times M$)** baseada em turnos/ticks cíclicos.  
O fluxo central segue a seguinte dinâmica:

```
[Início da Wave] 
   └──> [Geração dos Inimigos no Rio/Mar]
           └──> [Fase de Ataque das Tartarugas (Alcance e Cadência)]
                   └──> [Fase de Movimentação dos Lixos na Matriz]
                           └──> [Verificação de Chegada ao Fim (Dano na Praia)]
                                   └──> [Fim da Wave / Loja / Próxima Wave]
```

### Principais Regras Unificadas:
1. **Malha do Mapa:** Matriz onde cada célula possui um tipo (`AGUA_RIO`, `TERRA_DISPONIVEL`, `PONTO_SPAWN`, `PONTO_DESTINO_PRAIA`).
2. **Defensores (Tartarugas):** Ficam alocadas em células específicas de terra. Atacam sempre o lixo mais avançado no caminho que esteja dentro do seu alcance ($Range$). Podem receber upgrades de nível.
3. **Invasores (Lixos):** Movem-se exclusivamente pela trilha de água. Caso dois lixos ocupem a mesma célula, ambos sofrem o dano da tartaruga (dano em área por célula). Ao morrerem, concedem conchas/moedas e XP. Ao chegarem ao ponto final, reduzem a integridade da praia (vida do jogador).
4. **Jogador / Economia:** Controla conchas (saldo), vida da praia (HP), wave atual e pontuação total (soma de XP).
5. **Persistência:** Ao final da partida (vitória ou derrota), os dados da sessão são registrados em um arquivo `.txt` e o ranking é atualizado e ordenado por pontuação.

---

## 2. Matriz de Responsabilidades da Equipe

| Integrante | Módulo Principal | Responsabilidade Chave |
| :--- | :--- | :--- |
| **Otávio** | **Mapa \& Jogador** | Implementar a malha matricial, caminho do rio, células clicáveis, estado do jogador (vida, conchas, wave, score). |
| **Tomás (Tom)** | **Tartarugas (Torres)** | Implementar as 3 espécies de tartarugas, sistema de custo, balanceamento de dano, range em células, velocidade de ataque e lógica de upgrades. |
| **Bruna** | **Inimigos (Lixo Marinho)** | Implementar os tipos de lixo, mecânica de velocidade/movimento na malha, pontos de vida, buffs por wave, dano à praia e drops (conchas e XP). |
| **Letícia** | **Sistema de Combate** | Controlar a integração entre Mapa, Tartarugas e Inimigos: detecção de alcance, seleção de alvo primário, aplicação de dano e acerto simultâneo na mesma célula. |
| **Helena** | **Motor de Ondas (WaveService)** | Controlar o ciclo de waves: quantidade e composição de inimigos por onda, temporização/intervalos de spawn, escalonamento de dificuldade e bônus de round. |
| **Pedro** | **Persistência \& Ranking** | Gravação e leitura de dados em arquivo `.txt`, formatação dos registros, ordenação decrescente do ranking por pontuação e exibição do histórico. |
| **Marina** | **Sprites \& Animações** | Criação e animação dos elementos visuais: animação de ataque da tartaruga (pulso de escala), hit no lixo (flash vermelho + encolhimento) e oscilação de flutuação no rio. |
| **Caroline** | **Interface, Menus \& Modais** | Telas de Menu Principal, Tela de Ranking, Tela de Jogo, botões de ação e modais interativos (compra de tartaruga, upgrades e alertas de saldo insuficiente). |

---

## 3. Contratos de Comunicação entre Módulos (Interfaces / Métodos)

Para que todos possam programar em paralelo sem quebrar o código dos colegas, definem-se os seguintes contratos:

### A. Otávio (Mapa) $\leftrightarrow$ Letícia (Combate)
* `Mapa.getPosicaoLixo(InimigoLixo lixo) -> Coordenada`
* `Mapa.getCelulasAoAlcance(Coordenada centro, int range) -> List<CelulaGrid>`
* `Mapa.getLixosNaCelula(int x, int y) -> List<InimigoLixo>`
* `Mapa.getCaminhoOrdenado() -> List<Coordenada>` (ordem do rio da nascente até a praia).

### B. Tomás (Tartarugas) $\leftrightarrow$ Otávio (Jogador / Loja)
* `Tartaruga.getCustoCompra() -> int`
* `Tartaruga.getCustoUpgrade() -> int`
* `Jogador.gastarConchas(int valor) -> boolean` (retorna false se não tiver saldo).
* `Tartaruga.evoluir() -> void` (aplica a melhoria de atributos).

### C. Bruna (Inimigos) $\leftrightarrow$ Letícia (Combate) $\leftrightarrow$ Otávio (Jogador)
* `InimigoLixo.receberDano(int dano) -> boolean` (retorna `true` se o inimigo morreu).
* `InimigoLixo.getValorRecompensa() -> int` (conchas transferidas para o Jogador).
* `InimigoLixo.getXpRecompensa() -> int` (adicionado ao score do Jogador).
* `Jogador.receberDano(int danoInimigo) -> void` (chamado quando o lixo chega ao destino).

### D. Helena (Ondas) $\leftrightarrow$ GameLoop / Interface
* `WaveService.iniciarProximaWave() -> List<InimigoLixo>`
* `WaveService.isWaveFinalizada() -> boolean`
* `WaveService.getNumeroWaveAtual() -> int`

### E. Pedro (Persistência) $\leftrightarrow$ Caroline (Interface)
* `GerenciadorPersistencia.salvarFimDePartida(RegistroPartida registro) -> void`
* `GerenciadorPersistencia.carregarRanking() -> List<RegistroPartida>`

---

## 4. Guia de Arquivos Individuais

Cada integrante possui um arquivo dedicado na pasta `docs/planejamento/` com o detalhamento de seu módulo e um questionário técnico de regras de negócio que deve ser respondido antes do início da codificação:

1. `otavio_mapa_jogador.md`
2. `tomas_tartarugas.md`
3. `bruna_inimigos_lixo.md`
4. `leticia_combate.md`
5. `helena_ondas.md`
6. `pedro_persistencia_historico.md`
7. `marina_visual_animacoes.md`
8. `caroline_menus_modais.md`

