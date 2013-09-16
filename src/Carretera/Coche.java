package Carretera;

import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
/*
 * Esta clase determina como va a ser nuestro coche con sus atributos, y
 * metodos.
 */

public class Coche implements Runnable {

    public static void setYa_arriba(boolean aYa) {
        ya_arriba = aYa;
    }

    public static void setComienzo_abajo(int aComienzo_abajo) {
        comienzo_abajo = aComienzo_abajo;
    }

    public static void setComienzo_arriba(int aComienzo_arriba) {
        comienzo_arriba = aComienzo_arriba;
    }

    public static void setYa_abajo(boolean aYa_abajo) {
        ya_abajo = aYa_abajo;
    }
    private String imagen_coche = "../img/coche.png";
    private int velocidadx;
    private int velocidady;
    private int x;
    private int y;
    private Image imagen;
    private int lugar;
    private int id_coche = 0;
    private final static int ANCHO = Dibujar.getAncho(), ALTO = Dibujar.getAlto();
    private static int distancia_max = 50;
    private static int comienzo_abajo = 0;
    private static int comienzo_arriba = -50;
    private static boolean parada_arriba = false;
    private static boolean parada_abajo = false;
    private static boolean ya_arriba = false;
    private static boolean ya_abajo = false;

    public Coche() {
    }

    public Coche(int lugar, int id) {
        //Que imagen vamos a querer del coche
        ImageIcon ii = new ImageIcon(this.getClass().getResource(imagen_coche));
        imagen = ii.getImage();
        //Posicion inicial del coche
        if (lugar == 0) {
            //La velocidad inicial tendra un valor inicial aleatorio
            velocidadx = (int) Math.floor(Math.random() * 3 + 1);
            x = comienzo_arriba;
            y = 180;
            //Con esto los coches salen en fila uno tras otro segun la matriz
            comienzo_arriba -= ((int) (Math.random() * 100) + distancia_max);
        } else if (lugar == 2) {
            //La velocidad inicial tendra un valor inicial aleatorio
            velocidadx = (int) (Math.floor(Math.random() * 3 + 1)) * -1;
            x = ANCHO + comienzo_abajo * -1;
            y = 560;
            //Con esto los coches salen en fila uno tras otro segun la matriz
            comienzo_abajo -= ((int) (Math.random() * 100) + distancia_max);
        }
        /*else {
        //La velocidad inicial tendra un valor inicial aleatorio
        velocidadx = (int) Math.floor(Math.random() * 3 + 1);
        x = comienzo_arriba;
        y = 357;
        //Con esto los coches salen en fila uno tras otro segun la matriz
        comienzo_arriba -= ((int) (Math.random() * 100) + distancia_max);
        }*/
        this.lugar = lugar;
        this.id_coche = id;
    }

    //Funcion que hace mover al coche
    public void mover() {
        //Condicion para coches de arriba
        if (getLugar() == 0) {
            if ((x >= 485 && x <= 515) && parada_arriba) {
                velocidadx = 0;
                setYa_arriba(true);
            } else {
                //Pondra al coche en el comienzo con una velocidad aleatoria
                if (x >= ANCHO) {
                    x = -200;
                    velocidadx = (int) Math.floor(Math.random() * 3 + 1);
                }
                if (ya_arriba == false || (ya_arriba == true && x <= (500 - (getId_coche() * 55)))) {
                    x += getVelocidadx() + 1;
                    setY(y + velocidady);
                }
            }
            //Condicion para coches de abajo
        } else if (getLugar() == 2) {
            if ((x >= 35 && x <= 55) && parada_abajo) {
                velocidadx = 0;
                setYa_abajo(true);
            } else {
                //Pondra al coche en el comienzo con una velocidad aleatoria
                if (x <= 0) {
                    x = ANCHO + 200;
                    velocidadx = (int) (Math.floor(Math.random() * 3 + 1)) * -1;
                }

                if (ya_abajo == false || (ya_abajo == true && x >= (50 + (getId_coche() * 55)))) {
                    x += getVelocidadx() - 1;
                    setY(y + velocidady);
                }
            }
        } else {
            if (y < 357) {
                //Pondra al coche en el comienzo con una velocidad aleatoria
                if (x >= ANCHO) {
                    if (id_coche != 0) {
                        if(Carretera.getNum_coches()%2==0){
                            if(id_coche == Carretera.getNum_coches()-2){
                                Carretera.setAbierto_arriba(false);
                                Carretera.eliminarcochemedio();
                            }
                        }else{
                            if(id_coche == Carretera.getNum_coches()-1){
                                Carretera.setAbierto_arriba(false);
                                Carretera.eliminarcochemedio();
                            }
                        }
                        Carretera.eliminarcochemedio(getId_coche() - 1);
                    } else {
                        Carretera.eliminarcochemedio(getId_coche());
                    }
                }
                x += getVelocidadx() + 1;
                if (y < 356) {
                    y += velocidady + 1;
                }
            }
            else{
                if (x <= -50) {
                    if (id_coche != 0) {
                        if(Carretera.getNum_coches()%2==0){
                            if(id_coche == Carretera.getNum_coches()-2){
                                Carretera.setAbierto_abajo(false);
                                Carretera.eliminarcochemedio();
                            }
                        }else{
                            if(id_coche == Carretera.getNum_coches()-1){
                                Carretera.setAbierto_abajo(false);
                                Carretera.eliminarcochemedio();
                            }
                        }
                        Carretera.eliminarcochemedio(getId_coche() - 1);
                    } else {
                        Carretera.eliminarcochemedio(getId_coche());
                    }
                }
                x += getVelocidadx() - 1;
                if (y > 358) {
                    y += velocidady - 1;
                }
            }
        }
    }

    //Declaro que quiero hacer con el hilo
    public void run() {
        mover();
    }

    public int getLugar() {
        return lugar;
    }

    public static int getDistancia_max() {
        return distancia_max;
    }

    public static void setDistancia_max(int aDistancia_max) {
        distancia_max = aDistancia_max;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public Image getImagen() {
        return imagen;
    }

    public int getVelocidadx() {
        return velocidadx;
    }

    public void setVelocidadx(int velocidadx) {
        this.velocidadx = velocidadx;
    }

    public static void setParada_arriba(boolean aParada) {
        parada_arriba = aParada;
    }

    public static void setParada_abajo(boolean aParada_abajo) {
        parada_abajo = aParada_abajo;
    }

    public void setLugar(int lugar) {
        this.lugar = lugar;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId_coche() {
        return id_coche;
    }

    public void setId_coche(int id_coche) {
        this.id_coche = id_coche;
    }
}
