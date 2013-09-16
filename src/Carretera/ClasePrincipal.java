package Carretera;

public class ClasePrincipal {

     public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Panel().setVisible(true);
            }
        });
        new Dibujar();
    }
}
