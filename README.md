# Tartarugas vs. Lixo Marinho (Nome provisório)

Projeto de Desenvolvimento de Software para a disciplina **CCF313 - Programação Orientada a Objetos** da **Universidade Federal de Viçosa (UFV)**.

---

## 1. Sobre o Projeto

O projeto consiste em um jogo de **Tower Defense** em 2D desenvolvido em Java. O tema central é a conscientização sobre o descarte inadequado de resíduos nos oceanos.

- **Objetivo do jogo:** O jogador deve defender a costa contra ondas de lixo marinho (garrafas plásticas, redes de pesca, microplásticos, etc.) posicionando diferentes espécies de tartarugas marinhas ao longo do mapa.
- **Mecânica:** Cada tartaruga funciona como uma torre de defesa com atributos próprios (alcance, dano, custo e velocidade de ataque). Destruir o lixo concede recursos (conchas) para comprar novas unidades ou melhorar as existentes. Ao final de cada partida, a pontuação é salva em um ranking local.

---

## 2. Conceitos de POO Aplicados

- **Abstração:** Classes abstratas base (`Entidade`, `TorreTartaruga`, `InimigoLixo`) que definem métodos e atributos comuns a todos os elementos móveis e estáticos do jogo.
- **Herança:** Reutilização de código através de subclasses concretas para cada espécie de tartaruga (`TartarugaVerde`, `TartarugaDeCouro`) e tipo de lixo (`GarrafaPet`, `RedeFantasma`).
- **Polimorfismo:** Métodos como `atacar()` e `atualizar()` têm implementações diferentes dependendo do tipo específico de tartaruga ou dejeto, permitindo comportamentos variados dentro do loop principal.
- **Encapsulamento:** Atributos de vida, dano, posição e recursos protegidos por modificadores de acesso (`private`/`protected`) e manipulados via métodos de acesso com validações.
- **Exceções:** Criação e captura de exceções personalizadas (`PosicaoInvalidaException`, `RecursosInsuficientesException`) para impedir jogadas ilegais, como posicionar torres fora do mapa ou tentar compras sem saldo.

---

## 3. Divisão de Responsabilidades (8 Integrantes)

Para manter o desenvolvimento organizado e garantir que todos do grupo tenham commits e arquivos próprios no repositório, dividimos as classes da seguinte forma:

| Integrante | Módulo | Classes / Arquivos Principais |
| :--- | :--- | :--- |
| **Lethícia** | Base de Torres | `Entidade.java`, `TorreTartaruga.java`, `TartarugaVerde.java` |
| **Tomás** | Torres Adicionais e Projéteis | `TartarugaDeCouro.java`, `TartarugaDePente.java`, `Projetil.java` |
| **Bruna** | Inimigos | `InimigoLixo.java`, `GarrafaPet.java`, `RedeFantasma.java`, `Microplastico.java` |
| **Helena** | Tabuleiro e Caminho | `Mapa.java`, `CelulaGrid.java`, `Caminho.java`, `PosicaoInvalidaException.java` |
| **Otávio** | Motor e Colisão | `GameLoopService.java`, `CollisionService.java`, `WaveService.java` |
| **Pedro** | Economia e Jogador | `Jogador.java`, `LojaService.java`, `RecursosInsuficientesException.java` |
| **Marina** | Persistência (Arquivos) | `IRepository.java`, `RankingRepository.java`, `JogadorRepository.java`, `ArquivoUtil.java` |
| **Caroline** | Interface Gráfica e Telas | `GameController.java`, `TelaJogoView.java`, `TelaMenuView.java`, `TelaRankingView.java` |

Obs: Arquivos sujeitos a alteração

---

O projeto pode sofrer alterações a qualquer momento sem aviso prévio