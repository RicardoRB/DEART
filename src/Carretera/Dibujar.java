package Carretera;

import javax.swing.JFrame;

/*
 * Clase encargada de hacer correr nuestro programa en la clase main
 * dibujando nuestra aplicacion
 */
public class Dibujar extends JFrame {

    private final static int ancho = 600;
    private final static int alto = 768;
        private static Thread hilo1;

    public Dibujar() {
                hilo1 = new Thread(javacvThread.getInstance());
        hilo1.start();
        //Aniade lo dibujado en la clase Carretera a una ventana
        add(new Carretera());
        //Funcion que hace cerrar el programa cuando le damos a la X
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Tamaño de la ventana
        setSize(Dibujar.getAncho(), Dibujar.getAlto());
        //Se coloca en el centro
        setLocation(766,0);
        //Titulo de la aplicacion
        setTitle("Carretera - Proyecto Zaragoza");
        //No se puede cambiar el tamaño de la aplicacion
        setResizable(false);
        //Muestra el programa por pantalla
        setVisible(true);
    }

    public static int getAncho() {
        return ancho;
    }

    public static int getAlto() {
        return alto;
    }
}
