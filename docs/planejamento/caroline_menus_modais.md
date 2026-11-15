# 🖥️ Planejamento Individual: Caroline Melo Silva
## Módulo: Telas, Menus, Modais & Tratamento de Interações com o Usuário

---

### 1. Suas Responsabilidades Principais
1. **Navegação e Telas do Sistema:**
   - `TelaPrincipal.java`: Botão "Jogar" (inicia `TelaJogo`), Botão "Histórico" (abre `TelaHistorico`) e Botão "Sair".
   - `TelaHistorico.java`: Apresenta o ranking carregado do arquivo texto com scroll (`JScrollPane`) e aciona `ranking.limparHistorico()` no botão "Limpar Histórico".
   - `TelaJogo.java`: Gerencia os modais de compra e upgrade, alertas de saldo e finalização da partida (`mostrarGameOver` e `mostrarVitoria`).
2. **Modais e Interações com Células:**
   - **Clique em Célula LIVRE:** Abre o modal de compra de tartaruga (`abrirModalCompra`). Verifica `jogador.gastarDinheiro(custo)`. Se não houver saldo, dispara aviso "Dinheiro insuficiente!" (`mostrarAvisoSemDinheiro`).
   - **Clique em Célula OCUPADA:** Abre o modal de upgrade (`abrirModalUpgrade`) com base nos valores definidos pelo Tom.
3. **Fim de Jogo (Game Over / Vitória):**
   - Ao zerar vida ou vencer a última wave, invocar `ranking.salvarPartida(jogador.getNome(), jogador.getScore(), jogador.getWaveAtual())`.

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre os Modais de Compra e Upgrade:
1. **Formato do Modal de Compra:**  
   - Usar um `JOptionPane` com botões de opções das 3 tartarugas ou um `JDialog` customizado?
2. **Entrada do Nome do Jogador:**  
   - Solicitar via caixa de diálogo (`JOptionPane.showInputDialog`) logo ao clicar em "Jogar" no menu principal para já instanciar o `Jogador` com nome.
