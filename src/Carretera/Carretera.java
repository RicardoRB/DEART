package Carretera;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Vector;
/*
 * Esta clase es la encargada de pintarnos los coches en nuestro programa
 * y hacer mover los coches
 */

public final class Carretera extends JPanel implements ActionListener {

    public static int getDistancia() {
        return distancia;
    }

    public static void setAbierto_arriba(boolean aAbierto_arriba) {
        abierto_arriba = aAbierto_arriba;
    }

    public static void setAbierto_abajo(boolean aAbierto_abajo) {
        abierto_abajo = aAbierto_abajo;
    }

    public static void setYa_cambio(boolean aYa_cambio) {
        ya_cambio = aYa_cambio;
    }

    public static void setYa_cambio_abajo(boolean aYa_cambio_abajo) {
        ya_cambio_abajo = aYa_cambio_abajo;
    }
    //Declarar la velocidad (tiempo de refresco)
    private Timer timer;
    //Declarar el numero de coches en cada carril
    private static int num_coches = 10;
    private String imagen_carretera = "../img/via.jpg";
    private String imagen_verde = "../img/verde.png";
    private String imagen_rojo = "../img/rojo.png";
    private String imagen_verde_up = "../img/verde1-1.png";
    private String imagen_verde_down = "../img/verde1-2.png";
    private String imagen_ambar = "../img/ambar.png";
    private static Vector lista_coches_arriba;
    private static Vector lista_coches_abajo;
    private static Vector lista_coches_medio;
    private static Vector lista_hilo_arriba;
    private static Vector lista_hilo_abajo;
    private static Vector lista_hilo_medio;
    private static int distancia;
    private static boolean abierto_arriba = false;
    private static boolean abierto_abajo = false;
    private static boolean ya_cambio = false;
    private static boolean ya_cambio_abajo = false;

    public Carretera() {
        //Comienza la enfocacion al componente
        setFocusable(true);
        /*Borrar el componente y dibujar la imágen encima directamente*/
        setDoubleBuffered(true);
        //Creamos la lista de coche
        lista_coches_arriba = new Vector();
        lista_coches_abajo = new Vector();
        lista_coches_medio = new Vector();
        //Creamos la lista de hilos
        lista_hilo_arriba = new Vector();
        lista_hilo_abajo = new Vector();
        lista_hilo_medio = new Vector();
        //Creamos los coches dentro de la lista
        crearcoches();
        //Velocidad en la que se avanza (tiempo de refresco)
        timer = new Timer(20, this);
        timer.start();
    }

    //Funcion que hace dibujar nuestro coche en la pantalla
    @Override
    public void paint(Graphics grafico) {
        super.paint(grafico);
        //Variable para dibujar los graficos en 2d
        Graphics2D g2d = (Graphics2D) grafico;
        //Imagen de fondo de la carretera
        ImageIcon ii = new ImageIcon(this.getClass().getResource(imagen_carretera));
        g2d.drawImage(ii.getImage(), 0, 0, this);
        //Imagen verde abajo para el semaforo
        ImageIcon ir = new ImageIcon(this.getClass().getResource(imagen_verde));
        g2d.drawImage(ir.getImage(), 550, 550, this);
        //Imagen verde arriba para el semaforo
        ImageIcon iv = new ImageIcon(this.getClass().getResource(imagen_verde));
        g2d.drawImage(iv.getImage(), 550, 185, this);
        if (javacvThread.getCambio_c1() > 1000 && !abierto_abajo) {
            ImageIcon im = new ImageIcon(this.getClass().getResource(imagen_verde_up));
            g2d.drawImage(im.getImage(), 560, 320, this);
            setAbierto_arriba(true);
        } else if (javacvThread.getCambio_c2() > 1000 && !abierto_arriba) {
            ImageIcon im = new ImageIcon(this.getClass().getResource(imagen_verde_down));
            g2d.drawImage(im.getImage(), 560, 420, this);
            setAbierto_abajo(true);
        } else if (abierto_arriba || abierto_abajo) {
            ImageIcon im = new ImageIcon(this.getClass().getResource(imagen_ambar));
            g2d.drawImage(im.getImage(), 560, 370, this);
        } else {
            ImageIcon im = new ImageIcon(this.getClass().getResource(imagen_rojo));
            g2d.drawImage(im.getImage(), 560, 370, this);
        }
        //Dibujar todos los coches
        for (int i = 0; i < lista_coches_arriba.size(); i++) {
            Coche c1 = (Coche) lista_coches_arriba.get(i);
            g2d.drawImage(c1.getImagen(), c1.getX(), c1.getY(), this);
        }
        for (int i = 0; i < lista_coches_abajo.size(); i++) {
            Coche c1 = (Coche) lista_coches_abajo.get(i);
            g2d.drawImage(c1.getImagen(), c1.getX(), c1.getY(), this);
        }
        for (int i = 0; i < lista_coches_medio.size(); i++) {
            Coche c1 = (Coche) lista_coches_medio.get(i);
            g2d.drawImage(c1.getImagen(), c1.getX(), c1.getY(), this);
        }
        //Sincronizar la pintura con Linux, para que vaya mas fluido
        Toolkit.getDefaultToolkit().sync();
        //Destructor de los graficos
        grafico.dispose();
    }

    //Funcion que mueve el coche y refresca la imagen
    public void actionPerformed(ActionEvent e) {
        lista_hilo_arriba.removeAllElements();
        for (int i = 0; i < lista_coches_arriba.size(); i++) {
            Coche c1 = (Coche) lista_coches_arriba.get(i);
            Thread hilo1 = new Thread(c1);
            c1.setId_coche(i);
            lista_hilo_arriba.add(hilo1);
            hilo1.start();
            distancia(c1);
            if (abierto_arriba && i % 2 == 0 && ya_cambio == false && !abierto_abajo) {
                c1.setLugar(1);
                lista_coches_medio.addElement(c1);
                eliminarcochearriba(i);
                if (getNum_coches() % 2 == 0) {
                    if (i == (getNum_coches() - 2)) {
                        setYa_cambio(true);
                        for(int j=1;j<getNum_coches()-1;j++){
                            if(j%2!=0){
                                Coche c = (Coche)lista_coches_arriba.get(j);
                                c.setId_coche(j/2);
                            }
                        }
                    }
                } else {
                    if (i == (getNum_coches() - 1)) {
                        setYa_cambio(true);
                    }
                }
            }
        }
        lista_hilo_abajo.removeAllElements();
        for (int i = 0; i < lista_coches_abajo.size(); i++) {
            Coche c1 = (Coche) lista_coches_abajo.get(i);
            Thread hilo1 = new Thread(c1);
            lista_hilo_abajo.add(hilo1);
            c1.setId_coche(i);
            hilo1.start();
            distancia(c1);
            if (abierto_abajo && i % 2 == 0 && ya_cambio_abajo == false && !abierto_arriba) {
                c1.setLugar(1);
                lista_coches_medio.addElement(c1);
                eliminarcocheabajo(i);
                if (getNum_coches() % 2 == 0) {
                    if (i == (getNum_coches() - 2)) {
                        setYa_cambio_abajo(true);
                    }
                } else {
                    if (i == (getNum_coches() - 1)) {
                        setYa_cambio_abajo(true);
                    }
                }
            }
        }
        lista_hilo_medio.removeAllElements();
        for (int i = 0; i < lista_coches_medio.size(); i++) {
            Coche c1 = (Coche) lista_coches_medio.get(i);
            Thread hilo1 = new Thread(c1);
            c1.setId_coche(i);
            lista_hilo_medio.add(hilo1);
            hilo1.start();
            distancia(c1);
        }
        repaint();
    }

    public static int getNum_coches() {
        return num_coches;
    }

    public static void setNum_coches(int num_coches) {
        Carretera.num_coches = num_coches;
    }

    //Funcion para crear los coches
    public static void crearcoches() {
        crearcochesarriba();
        crearcochesabajo();
    }

    public static void crearcochesmedio() {
        for (int i = 0; i < getNum_coches(); i++) {
            crearcoche(1, i);
        }
    }

    public static void crearcochesarriba() {
        for (int i = 0; i < getNum_coches(); i++) {
            crearcoche(0, i);
        }
    }

    public static void crearcochesabajo() {
        for (int i = 0; i < getNum_coches(); i++) {
            crearcoche(2, i);
        }
    }

    public static void crearcoche(int lugar, int id) {
        Coche coche = new Coche(lugar, id);
        if (lugar == 0) {
            lista_coches_arriba.addElement(coche);
            Thread hilo1 = new Thread(coche);
            lista_hilo_arriba.addElement(hilo1);
        } else if (lugar == 2) {
            lista_coches_abajo.addElement(coche);
            Thread hilo1 = new Thread(coche);
            lista_hilo_abajo.addElement(hilo1);
        } else {
            lista_coches_medio.addElement(coche);
            Thread hilo1 = new Thread(coche);
            lista_hilo_medio.addElement(hilo1);
        }
    }

    public static void eliminarcoches() {
        lista_coches_arriba.removeAllElements();
        lista_hilo_arriba.removeAllElements();
        lista_coches_abajo.removeAllElements();
        lista_hilo_abajo.removeAllElements();
        lista_coches_medio.removeAllElements();
        lista_hilo_medio.removeAllElements();
    }

    public static void pararcochearriba() {
        Coche coche;
        coche = (Coche) lista_coches_arriba.get(0);
        coche.setVelocidadx(0);
    }

    public static void pararcocheabajo() {
        Coche coche;
        coche = (Coche) lista_coches_abajo.get(0);
        coche.setVelocidadx(0);
    }

    public static void eliminarcochearriba(int i) {
        if (lista_coches_arriba.size() <= 0) {
            System.out.println("No hay coches para eliminar");
        } else {
            lista_hilo_arriba.removeElement(i);
            lista_coches_arriba.removeElement(i);
        }
    }

    public static void eliminarcochemedio(int i) {
        if (lista_coches_medio.size() <= 0) {
            System.out.println("No hay coches para eliminar");
        } else {
            lista_hilo_medio.removeElement(i);
            lista_coches_medio.removeElement(i);
        }
    }

    public static void eliminarcochemedio() {
        lista_hilo_medio.removeAllElements();
        lista_coches_medio.removeAllElements();
    }

    public static void eliminarcochearriba() {
        lista_hilo_arriba.removeAllElements();
        lista_coches_arriba.removeAllElements();
    }

    public static void eliminarcocheabajo(int i) {
        if (lista_coches_abajo.size() <= 0) {
            System.out.println("No hay coches para eliminar");
        } else {
            lista_coches_abajo.removeElement(i);
            lista_hilo_abajo.removeElement(i);
        }
    }

    public static void eliminarcocheabajo() {
        lista_coches_abajo.removeAllElements();
        lista_hilo_abajo.removeAllElements();
    }

    public static int getNum_coches_abajo() {
        return lista_coches_abajo.size();
    }

    public static int getNum_coches_arriba() {
        return lista_coches_arriba.size();
    }

    //Funcion para ver si hay coches delante
    public static void distancia(Coche c) {
        int velocidad_max = 2;
        int i;

        i = lista_coches_arriba.indexOf(c);
        if (i == -1) {
            i = lista_coches_abajo.indexOf(c);
            if (i == -1) {
                i = lista_coches_medio.indexOf(c);
            }
        }
        
        if (c.getLugar() == 0) {
            if (i != 0) {
                for (int j = 1; j < lista_coches_arriba.size(); j++) {
                    Coche c2 = (Coche) lista_coches_arriba.get(j);
                    if (j != i) {
                        distancia = c.getX() - c2.getX();
                        if (Math.abs(getDistancia()) < Coche.getDistancia_max()) {
                            if (getDistancia() < 0) {
                                c.setVelocidadx(0);
                            } else {
                                c2.setVelocidadx(0);
                            }
                        }
                    }
                }

                Coche c2 = (Coche) lista_coches_arriba.get(i - 1);
                distancia = c2.getX() - c.getX();
                if (getDistancia() <= Coche.getDistancia_max()) {
                    c.setVelocidadx(0);
                }
                if (((c.getVelocidadx() > c2.getVelocidadx()) && (getDistancia() <= Coche.getDistancia_max()))/*||(distancia <= Coche.getDistancia_max())*/) {
                    c.setVelocidadx(c2.getVelocidadx());
                } else {
                    if (c.getVelocidadx() < velocidad_max) {
                        c.setVelocidadx(c.getVelocidadx() + 1);
                    }
                }
            } else {
                for (int j = 1; j < lista_coches_arriba.size(); j += 1) {
                    Coche c2 = (Coche) lista_coches_arriba.get(j);
                    if (j != i) {
                        distancia = c.getX() - c2.getX();
                        if (Math.abs(getDistancia()) < Coche.getDistancia_max()) {
                            if (getDistancia() < 0) {
                                c.setVelocidadx(0);
                            } else {
                                c2.setVelocidadx(0);
                            }
                        }
                    }
                }
                if (lista_coches_arriba.size() > 1) {
                    Coche c2 = (Coche) lista_coches_arriba.get(lista_coches_arriba.size() - 1);
                    distancia = c2.getX() - c.getX();
                    if (getDistancia() <= Coche.getDistancia_max()) {
                        c.setVelocidadx(0);
                    }
                    if ((c.getVelocidadx() > c2.getVelocidadx()) && (c.getX() < c2.getX())) {
                        if (c2.getX() - c.getX() == Coche.getDistancia_max()) {
                            c.setVelocidadx(c2.getVelocidadx());
                        }
                    } else {
                        if (c.getVelocidadx() < velocidad_max) {
                            c.setVelocidadx(c.getVelocidadx() + 1);
                        }
                    }
                }
            }
        } else if (c.getLugar() == 2) {
            if (i != 0) {
                for (int j = 1; j < lista_coches_abajo.size(); j += 1) {
                    Coche c2 = (Coche) lista_coches_abajo.get(j);
                    if (j != i) {
                        distancia = c2.getX() - c.getX();
                        if (Math.abs(getDistancia()) < Coche.getDistancia_max()) {
                            if (getDistancia() < 0) {
                                c2.setVelocidadx(0);
                            } else {
                                c.setVelocidadx(0);
                            }
                        }
                    }
                }
                Coche c2 = (Coche) lista_coches_abajo.get(i - 1);
                distancia = c.getX() - c2.getX();
                if (getDistancia() < Coche.getDistancia_max()) {
                    c.setVelocidadx(0);
                }
                if ((c.getVelocidadx() < c2.getVelocidadx()) && (getDistancia() == Coche.getDistancia_max())) {
                    c.setVelocidadx(c2.getVelocidadx());
                } else {
                    if (Math.abs(c.getVelocidadx()) > velocidad_max) {
                        c.setVelocidadx(c.getVelocidadx() + 1);
                    }
                }
            } else {
                for (int j = 1; j < lista_coches_abajo.size(); j += 1) {
                    Coche c2 = (Coche) lista_coches_abajo.get(j);
                    if (j != i) {
                        distancia = c2.getX() - c.getX();
                        if (Math.abs(getDistancia()) < Coche.getDistancia_max()) {
                            if (getDistancia() < 0) {
                                c2.setVelocidadx(0);
                            } else {
                                c.setVelocidadx(0);
                            }
                        }
                    }
                }
                Coche c2 = (Coche) lista_coches_abajo.get(lista_coches_abajo.size() - 1);
                distancia = c.getX() - c2.getX();
                if (getDistancia() <= Coche.getDistancia_max()) {
                    c.setVelocidadx(0);
                }
                if ((c.getVelocidadx() < c2.getVelocidadx()) && (c.getX() < c2.getX())) {
                    if (c2.getX() - c.getX() == Coche.getDistancia_max()) {
                        c.setVelocidadx(c2.getVelocidadx());
                    }
                } else {
                    if (Math.abs(c.getVelocidadx()) > velocidad_max) {
                        c.setVelocidadx(c.getVelocidadx() + 1);
                    }
                }
            }
        } else {
            if (abierto_arriba) {
                if (i != 0) {
                    for (int j = 1; j < lista_coches_arriba.size(); j++) {
                        Coche c2 = (Coche) lista_coches_arriba.get(j);
                        if (j != i) {
                            distancia = c.getX() - c2.getX();
                            if (Math.abs(getDistancia()) < Coche.getDistancia_max()) {
                                if (getDistancia() < 0) {
                                    c.setVelocidadx(0);
                                } else {
                                    c2.setVelocidadx(0);
                                }
                            }
                        }
                    }
                    Coche c2 = (Coche) lista_coches_arriba.get(i - 1);
                    distancia = c2.getX() - c.getX();
                    if (getDistancia() <= Coche.getDistancia_max()) {
                        c.setVelocidadx(0);
                    }
                    if (((c.getVelocidadx() > c2.getVelocidadx()) && (getDistancia() <= Coche.getDistancia_max()))) {
                        c.setVelocidadx(c2.getVelocidadx());
                    } else {
                        if (c.getVelocidadx() < velocidad_max) {
                            c.setVelocidadx(c.getVelocidadx() + 1);
                        }
                    }
                } else {
                    for (int j = 1; j < lista_coches_arriba.size(); j += 1) {
                        Coche c2 = (Coche) lista_coches_arriba.get(j);
                        if (j != i) {
                            distancia = c.getX() - c2.getX();
                            if (Math.abs(getDistancia()) < Coche.getDistancia_max()) {
                                if (getDistancia() < 0) {
                                    c.setVelocidadx(0);
                                } else {
                                    c2.setVelocidadx(0);
                                }
                            }
                        }
                    }
                    if (lista_coches_arriba.size() > 1) {
                        Coche c2 = (Coche) lista_coches_arriba.get(lista_coches_arriba.size() - 1);
                        distancia = c2.getX() - c.getX();
                        if (getDistancia() <= Coche.getDistancia_max()) {
                            c.setVelocidadx(0);
                        }
                        if ((c.getVelocidadx() > c2.getVelocidadx()) && (c.getX() < c2.getX())) {
                            if (c2.getX() - c.getX() == Coche.getDistancia_max()) {
                                c.setVelocidadx(c2.getVelocidadx());
                            }
                        } else {
                            if (c.getVelocidadx() < velocidad_max) {
                                c.setVelocidadx(c.getVelocidadx() + 1);
                            }
                        }
                    }
                }
            } else if (abierto_abajo) {
                if (i != 0) {
                    for (int j = 1; j < lista_coches_abajo.size(); j += 1) {
                        Coche c2 = (Coche) lista_coches_abajo.get(j);
                        if (j != i) {
                            distancia = c2.getX() - c.getX();
                            if (Math.abs(getDistancia()) < Coche.getDistancia_max()) {
                                if (getDistancia() < 0) {
                                    c2.setVelocidadx(0);
                                } else {
                                    c.setVelocidadx(0);
                                }
                            }
                        }
                    }
                    Coche c2 = (Coche) lista_coches_abajo.get(i - 1);
                    distancia = c.getX() - c2.getX();
                    if (getDistancia() < Coche.getDistancia_max()) {
                        c.setVelocidadx(0);
                    }
                    if ((c.getVelocidadx() < c2.getVelocidadx()) && (getDistancia() == Coche.getDistancia_max())) {
                        c.setVelocidadx(c2.getVelocidadx());
                    } else {
                        if (Math.abs(c.getVelocidadx()) > velocidad_max) {
                            c.setVelocidadx(c.getVelocidadx() + 1);
                        }
                    }
                } else {
                    for (int j = 1; j < lista_coches_abajo.size(); j += 1) {
                        Coche c2 = (Coche) lista_coches_abajo.get(j);
                        if (j != i) {
                            distancia = c2.getX() - c.getX();
                            if (Math.abs(getDistancia()) < Coche.getDistancia_max()) {
                                if (getDistancia() < 0) {
                                    c2.setVelocidadx(0);
                                } else {
                                    c.setVelocidadx(0);
                                }
                            }
                        }
                    }
                    Coche c2 = (Coche) lista_coches_abajo.get(lista_coches_abajo.size() - 1);
                    distancia = c.getX() - c2.getX();
                    if (getDistancia() <= Coche.getDistancia_max()) {
                        c.setVelocidadx(0);
                    }
                    if ((c.getVelocidadx() < c2.getVelocidadx()) && (c.getX() < c2.getX())) {
                        if (c2.getX() - c.getX() == Coche.getDistancia_max()) {
                            c.setVelocidadx(c2.getVelocidadx());
                        }
                    } else {
                        if (Math.abs(c.getVelocidadx()) > velocidad_max) {
                            c.setVelocidadx(c.getVelocidadx() + 1);
                        }
                    }
                }
            }
        }
    }
}