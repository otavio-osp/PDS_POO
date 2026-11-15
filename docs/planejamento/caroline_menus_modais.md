# 🖥️ Planejamento Individual: Caroline Melo Silva
## Módulo: Telas, Menus, Modais & Tratamento de Interações com o Usuário

---

### 1. Suas Responsabilidades Principais
1. **Navegação e Telas do Sistema:**
   - **Tela de Menu Principal:** Botão "Jogar / Iniciar Partida", Botão "Histórico / Ranking", Botão "Como Jogar / Regras" e Botão "Sair".
   - **Transição de Telas:** Ao clicar em "Iniciar", chaveia fluidamente para a Tela do Jogo; ao clicar em "Histórico", abre a tela com os dados carregados pelo Pedro.
   - **Tela de Game Over / Vitória:** Exibe o resultado final da partida, score alcançado e botão para retornar ao menu ou tentar novamente.
2. **Modais e Caixas de Diálogo Durante a Partida:**
   - **Modal de Compra de Tartaruga:** Quando o jogador clicar em uma célula vazia e válida de terra, abrir um pop-up/modal exibindo as 3 espécies disponíveis, seus custos em conchas, descrições e botão "Comprar".
   - **Modal de Upgrade de Tartaruga:** Quando clicar em uma célula que já tem uma tartaruga, exibir o nível atual dela, o custo do upgrade, o que vai melhorar (ex: $+5$ dano, $+1$ range) e botão "Evoluir".
   - **Alertas do Sistema:** Mensagem de aviso caso o jogador tente comprar sem saldo de conchas suficiente ("Conchas insuficientes para esta compra!").

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre a Arquitetura das Telas:
1. **Qual padrão de interface será adotado para a troca de telas?**  
   - Se for em Swing, será usado o `CardLayout` em um `JFrame` principal? (Essa é a melhor prática recomendada para alternar entre Menu, Jogo e Ranking sem abrir múltiplas janelas separadas).
2. **Entrada do Nome do Jogador:**  
   - Em que momento o jogador digita o seu nome?  
     - Antes de começar o jogo (uma caixinha de texto na tela inicial)? OU  
     - Somente no final da partida se ele entrar no ranking?  
     *(Dica: Pedir o nome logo no início evita perder o dado se o jogo for fechado abruptamente).*

#### 🔹 Sobre os Modais de Compra e Upgrade:
3. **Como os modais serão exibidos visualmente?**  
   - *Opção A (Pop-up flutuante / `JDialog`):* Abre uma caixinha suspensa sobre o jogo. O jogo pausa enquanto o modal estiver aberto.
   - *Opção B (Painel Lateral de Loja / HUD):* Uma barra lateral fixa sempre visível à direita do tabuleiro com os botões das tartarugas, onde clicar na célula seleciona a posição e na lateral escolhe a ação.  
   *Qual das duas abordagens você prefere implementar?*
4. **Tratamento de Saldo Insuficiente:**  
   - Quando o jogador não tiver conchas:
     - O botão de compra deve ficar desabilitado (cinza) com tooltip explicativo? OU
     - O botão continua clicável e ao clicar dispara uma mensagem vermelha na tela: *"Você precisa de X conchas a mais!"*?
5. **Confirmação de Cancelamento:**  
   - Se o jogador abrir o modal de uma célula por engano e desistir de comprar, como ele fecha o modal? (Botão "Fechar/Cancelar" ou clicando fora da caixa?).

#### 🔹 Sobre a Tela de Histórico / Ranking:
6. **Formato de Apresentação do Ranking:**  
   - Será exibido em uma tabela (`JTable`) com colunas estilizadas: `# Posição`, `Nome`, `Pontuação (Score)`, `Onda Alcançada` e `Data`?
   - Terá botão de "Limpar Histórico" ou apenas visualização?

