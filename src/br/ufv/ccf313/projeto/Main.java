package br.ufv.ccf313.projeto;

import br.ufv.ccf313.projeto.ui.TelaPrincipal;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal menu = new TelaPrincipal();
            menu.setVisible(true);
        });
    }
}