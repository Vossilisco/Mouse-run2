package mouserun.mouse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import mouserun.game.Grid;
import mouserun.game.Mouse;
import mouserun.game.Cheese;

public class freeyuyee extends Mouse {

    HashMap<Integer, Grid> mapa;
    HashMap<Integer, Grid> maparecorrido;
    Stack<Integer> memoria;
    Stack<Integer> colaCamino;
    Queue<Grid> otraRuta;
    Grid siguienteCasilla;
    Grid casillaDer;
    Grid casillaIzq;
    Grid casillaArriba;
    Grid casillaAbajo;

    /// <summary>
    /// Constructor, para dar nombre al raton.
    /// </summary>
    public freeyuyee() {
        super("#FREEYUYEE");
        mapa = new HashMap<Integer, Grid>();
        maparecorrido = new HashMap<Integer, Grid>();
        memoria = new Stack<Integer>();
        colaCamino = new Stack<Integer>();
        otraRuta = new LinkedList<Grid>();
    }

    /// <summary>
    /// Devuelve un entero hash que nos ayudará a situar una casilla en nuestro HashMap.
    /// </summary>
    /// <param name="accepted">Dos enteros que.</param>
    private static int clavemapa(int x, int y) {
        return (x * 10000 + y);
    }

    /// <summary>
    /// Metodo principal del movimiento del raton, devolverá un entero que será el siguiente paso del raton..
    /// </summary>
    @Override
    public int move(Grid currentGrid, Cheese cheese) {

        Integer ratonX = currentGrid.getX();
        Integer ratonY = currentGrid.getY();
        ///Si no conoce la casilla en la que esta

        if (mapa.get(clavemapa(ratonX, ratonY)) == null) {
            mapa.put(clavemapa(ratonX, ratonY), currentGrid);
        }

        if (currentGrid.canGoUp() && mapa.get(clavemapa(ratonX, ratonY + 1)) == null) {
            Grid casilla = new Grid(ratonX, ratonY + 1);
            otraRuta.add(casilla);
            mapa.put(clavemapa(ratonX, ratonY + 1), casilla);
            System.out.println("hola A");
        }
        if (currentGrid.canGoRight() && mapa.get(clavemapa(ratonX + 1, ratonY)) == null) {
            Grid casilla = new Grid(ratonX + 1, ratonY);
            otraRuta.add(casilla);
            mapa.put(clavemapa(ratonX + 1, ratonY), casilla);
            System.out.println("hola D");
        }
        if (currentGrid.canGoDown() && mapa.get(clavemapa(ratonX, ratonY - 1)) == null) {
            Grid casilla = new Grid(ratonX, ratonY - 1);
            otraRuta.add(casilla);
            mapa.put(clavemapa(ratonX, ratonY - 1), casilla);
            System.out.println("hola AB");
        }
        if (currentGrid.canGoLeft() && mapa.get(clavemapa(ratonX - 1, ratonY)) == null) {
            Grid casilla = new Grid(ratonX - 1, ratonY);
            otraRuta.add(casilla);
            mapa.put(clavemapa(ratonX + 1, ratonY), casilla);
            System.out.println("hola I");

        }

        casillaArriba = new Grid(ratonX, ratonY + 1);
        casillaAbajo = new Grid(ratonX, ratonY - 1);
        casillaIzq = new Grid(ratonX - 1, ratonY);
        casillaDer = new Grid(ratonX + 1, ratonY);
        siguienteCasilla = otraRuta.element();
        System.out.println("casilla que tenemos que visitar: ");
        System.out.println(siguienteCasilla.getX() + " " + siguienteCasilla.getY());
        System.out.println("casilla que tenemos que tenemos ARRIBA: ");
        System.out.println(casillaArriba.getX() + " " + casillaArriba.getY());
        System.out.println("casilla que tenemos que tenemos ABAJO: ");
        System.out.println(casillaAbajo.getX() + " " + casillaAbajo.getY());
        System.out.println("casilla que tenemos que tenemos IZQ: ");
        System.out.println(casillaIzq.getX() + " " + casillaIzq.getY());
        System.out.println("casilla que tenemos que tenemos DER: ");
        System.out.println(casillaDer.getX() + " " + casillaDer.getY());

        if ((siguienteCasilla.getX() == casillaArriba.getX()) && (siguienteCasilla.getY() == casillaArriba.getY())) {
            System.out.println("Arriba tenemos una casilla nueva, vamos a sacarla x mis muerto!");
            colaCamino.push(DOWN);
            otraRuta.remove();
            return Mouse.UP;
        }
        if ((siguienteCasilla.getX() == casillaAbajo.getX()) && (siguienteCasilla.getY() == casillaAbajo.getY())) {
            System.out.println("Abajo tenemos una casilla nueva, vamos a sacarla x mis muerto!");
            colaCamino.push(UP);
            otraRuta.remove();
            return Mouse.DOWN;
        }
        if ((siguienteCasilla.getX() == casillaIzq.getX()) && (siguienteCasilla.getY() == casillaIzq.getY())) {
            System.out.println("A la izquierda tenemos una casilla nueva, vamos a sacarla x mis muerto!");
            colaCamino.push(RIGHT);
            otraRuta.remove();
            return Mouse.LEFT;
        }
        if ((siguienteCasilla.getX() == casillaDer.getX()) && (siguienteCasilla.getY() == casillaDer.getY())) {
            System.out.println("A la derecha tenemos una casilla nueva, vamos a sacarla x mis muerto!");
            colaCamino.push(LEFT);
            otraRuta.remove();
            return Mouse.RIGHT;
        }
    
        int haVolver=colaCamino.pop();
        colaCamino.clear();
        return haVolver;
    }

    @Override
    public void newCheese() {
    }

    public int contrario(int x){
        if (x==1) return 2;
        if (x==2) return 1;
        if (x==3) return 4;
        if (x==4) return 3; 
        return 5;
    }
    
    /// <summary>
    /// Metodo que se ejecutará cada vez que nuestro raton reaparezca.
    /// </summary>
    @Override
    public void respawned() {
        mapa = new HashMap<Integer, Grid>();
        memoria = new Stack<Integer>();
        colaCamino = new Stack<Integer>();
        otraRuta.clear();
    }
}
