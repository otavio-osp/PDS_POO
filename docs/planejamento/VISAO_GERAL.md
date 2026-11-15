# Planejamento Geral — Tartarugas vs. Lixo Marinho
**CCF313 - POO | UFV-Florestal | Prof. Philipe de Freitas Melo**

---

## Estrutura de Pacotes

```
src/br/ufv/ccf313/projeto/
├── Main.java
├── model/
│   ├── Entidade.java              ← base abstrata de tudo
│   ├── Jogador.java               ← OTÁVIO
│   ├── RegistroRanking.java       ← PEDRO
│   ├── mapa/
│   │   ├── TipoCelula.java        ← OTÁVIO (enum: LIVRE, CAMINHO, OCUPADA)
│   │   ├── Celula.java            ← OTÁVIO
│   │   └── Mapa.java              ← OTÁVIO
│   ├── tartaruga/
│   │   ├── TorreTartaruga.java    ← pai abstrato (TOM)
│   │   ├── TartarugaDeCouro.java  ← TOM
│   │   ├── TartarugaDePente.java  ← TOM
│   │   └── TartarugaVerde.java    ← TOM
│   └── inimigo/
│       ├── InimigoLixo.java       ← pai abstrato (BRUNA)
│       ├── LixoPlastico.java      ← BRUNA
│       ├── LixoGarrafa.java       ← BRUNA
│       └── LixoRede.java          ← BRUNA
├── service/
│   ├── GerenciadorRanking.java    ← PEDRO
│   ├── GerenciadorCombate.java    ← LETICIA
│   └── GerenciadorOndas.java      ← HELENA
└── ui/
    ├── TelaPrincipal.java         ← CAROL + MARINA
    ├── TelaJogo.java              ← CAROL + MARINA
    └── TelaHistorico.java         ← CAROL
```

---

## Responsabilidades por Integrante

### 🗺️ Otávio — Mapa + Jogador
**Arquivos:** `Mapa.java`, `Celula.java`, `TipoCelula.java`, `Jogador.java`

- Montar a grade (matriz de `Celula`) com tamanho definido
- Definir o caminho predeterminado dos inimigos (lista ordenada de posições)
- Marcar células como `CAMINHO`, `LIVRE` ou `OCUPADA`
- Implementar os métodos de `Jogador`: vida, dinheiro, score, wave
- O `Jogador` é passado para `TelaJogo` e `GerenciadorCombate`

---

### 🐢 Tom — Tartarugas + Upgrades
**Arquivos:** `TorreTartaruga.java`, `TartarugaDeCouro.java`, `TartarugaDePente.java`, `TartarugaVerde.java`

- Balancear os valores de custo, dano, alcance e velocidade das 3 tartarugas
- Implementar o sistema de upgrades (métodos que alteram dano/alcance/velocidade)
- Definir quais upgrades ficam disponíveis para cada tipo de tartaruga e o custo de cada upgrade
- Os setters para upgrade já existem em `TorreTartaruga`: `setDano()`, `setAlcance()`, `setVelocidade()`

| Tartaruga | Custo | Dano | Alcance | Velocidade |
|---|---|---|---|---|
| TartarugaVerde | 50 | 15 | 2 | 2 |
| TartarugaDePente | 100 | 20 | 2 | 3 |
| TartarugaDeCouro | 200 | 60 | 4 | 1 |

---

### 🗑️ Bruna — Inimigos
**Arquivos:** `InimigoLixo.java`, `LixoPlastico.java`, `LixoGarrafa.java`, `LixoRede.java`

- Definir os valores finais de vida, velocidade, recompensa e dano dos 3 tipos de lixo
- Implementar a movimentação pelo caminho do mapa (andar célula a célula)
- Velocidade `0` = leva 2 rodadas para andar 1 célula; `1` = 1 célula/rodada; `2` = 2 células/rodada
- Coordenar com Helena o formato do bust de wave (`aplicarBustDeWave()` já existe em `InimigoLixo`)

| Inimigo | Vida | Velocidade | Recompensa | Dano ao Jogador |
|---|---|---|---|---|
| LixoPlastico | 50 | 1 | 10 | 1 |
| LixoGarrafa | 30 | 2 | 15 | 1 |
| LixoRede | 150 | 0 | 30 | 2 |

---

### 💾 Pedro — Persistência e Histórico
**Arquivos:** `GerenciadorRanking.java`, `RegistroRanking.java`

- **Já implementado:** CRUD em memória + leitura/escrita em `data/ranking.txt`
- `salvarPartida(nome, score, wave)` → grava ao final do Game Over (Carol chama)
- `carregarDoArquivo()` → retorna `List<RegistroRanking>` ordenada por score
- `limparHistorico()` → apaga o arquivo (Carol chama no botão da TelaHistorico)
- Formato do arquivo: `Nome;Score;Wave` (uma linha por partida)

---

### ⚔️ Leticia — Combate
**Arquivo:** `GerenciadorCombate.java`

- `processarCombate(mapa, inimigosAtivos, jogador)` → chamado a cada rodada
- Para cada tartaruga no mapa, encontrar o inimigo mais avançado no alcance
- Se 2 inimigos estão na mesma célula, ambos levam o ataque
- Ao matar inimigo, chamar `jogador.registrarInimigoDestruido(recompensa)`
- Verificar se inimigo chegou ao fim do caminho → chamar `jogador.receberDano(dano)`

---

### 🌊 Helena — Ondas
**Arquivo:** `GerenciadorOndas.java`

- `gerarInimigos(posX, posY)` → retorna a lista de inimigos da wave atual
- `aplicarBustDeWave()` → aumenta vida e velocidade conforme a wave
- `waveCompleta(inimigos)` → true quando todos morreram ou passaram
- `avancarWave()` → incrementa o contador e o jogador avança com `jogador.avancarWave()`
- Definir quantas waves no jogo (`TOTAL_WAVES`) e a fórmula de quantidade por wave

---

### 🎨 Marina — Visual + Sprites
**Arquivos:** `TelaPrincipal.java`, `TelaJogo.java`

- Layout visual das telas (cores, fontes, posicionamento)
- Renderizar a grade do mapa (cada célula como um retângulo/tile)
- **Animações:**
  - Tartaruga ataca → aumenta de tamanho e volta ao original
  - Lixo toma dano → encolhe, fica vermelho e volta ao original
  - Lixo se movendo → efeito de "boiando" (sobe/desce levemente alterando posX e posY)

---

### 🖱️ Carol — Telas + Botões + Modais
**Arquivos:** `TelaPrincipal.java`, `TelaJogo.java`, `TelaHistorico.java`

- Botão **Jogar** → abre `TelaJogo`, fecha `TelaPrincipal`
- Botão **Histórico** → abre `TelaHistorico`
- Clique em célula **LIVRE** → abre modal de compra de tartaruga
- Clique em célula **OCUPADA** → abre modal de upgrade
- Verificar `jogador.gastarDinheiro(custo)` antes de confirmar compra
- Se sem dinheiro → `mostrarAvisoSemDinheiro()`
- Game Over → chamar `mostrarGameOver()` que salva e exibe resultado
- Botão **Limpar Histórico** → `ranking.limparHistorico()`

---

## Fluxo do Jogo (Resumido)

```
TelaPrincipal
  → [Jogar] → TelaJogo

TelaJogo (loop por rodada):
  1. Helena: gerar inimigos da wave
  2. Bruna: mover inimigos pelo caminho
  3. Leticia: processar combate (tartarugas atacam)
     → inimigo morre: Jogador.registrarInimigoDestruido(recompensa)
     → inimigo passa: Jogador.receberDano(dano)
  4. Carol/Marina: atualizar tela
  5. Verificar fim de wave → Helena.avancarWave() + Jogador.avancarWave()
  6. Verificar derrota (Jogador.vida <= 0) → GameOver → Pedro salva ranking
  7. Verificar vitória (todas waves completas) → Vitória → Pedro salva ranking

TelaHistorico:
  → Pedro: carregarDoArquivo() → exibir lista ordenada
  → [Limpar] → Pedro: limparHistorico()
```
