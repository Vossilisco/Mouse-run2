// Codigo de grupo: M16B11
// UP: vale 1 |  DOWN: vale 2 | LEFT: vale 3 | RIGHT: vale 4 | BOMB: vale 5
package mouserun.mouse;

import java.util.HashMap;
import java.util.Stack;
import mouserun.game.Grid;
import mouserun.game.Mouse;
import mouserun.game.Cheese;

public class freeyuyee extends Mouse {

    HashMap<Integer, Grid> mapa;
    HashMap<Integer, Grid> maparecorrido;
    Stack<Integer> memoria;
    Stack<Integer> colaCamino;
    Stack<Grid> otraRuta;

    /// <summary>
    /// Constructor, para dar nombre al raton.
    /// </summary>
    public freeyuyee() {
        super("#FREEYUYEE");
        mapa = new HashMap<Integer, Grid>();
        maparecorrido = new HashMap<Integer, Grid>();
        memoria = new Stack<Integer>();
        colaCamino = new Stack<Integer>();
        otraRuta = new Stack<Grid>();
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

            if (currentGrid.canGoUp() && mapa.get(clavemapa(ratonX, ratonY + 1)) == null) {
                Grid casilla = new Grid(ratonX, ratonY + 1);
                otraRuta.push(casilla);
            }
            if (currentGrid.canGoRight() && mapa.get(clavemapa(ratonX + 1, ratonY)) == null) {
                Grid casilla = new Grid(ratonX + 1, ratonY);
                otraRuta.push(casilla);
            }
            if (currentGrid.canGoDown() && mapa.get(clavemapa(ratonX, ratonY - 1)) == null) {
                Grid casilla = new Grid(ratonX, ratonY - 1);
                otraRuta.push(casilla);
            }
            if (currentGrid.canGoLeft() && mapa.get(clavemapa(ratonX - 1, ratonY)) == null) {
                Grid casilla = new Grid(ratonX - 1, ratonY);
                otraRuta.push(casilla);

            }
        }

        Grid casillaArriba = new Grid(ratonX, ratonY + 1);
        Grid casillaAbajo = new Grid(ratonX, ratonY - 1);
        Grid casillaIzq = new Grid(ratonX - 1, ratonY);
        Grid casillaDer = new Grid(ratonX + 1, ratonY);
        Grid siguienteCasilla = otraRuta.firstElement();
        if (siguienteCasilla == casillaArriba) {
            colaCamino.push(DOWN);
            otraRuta.pop();
            return UP;
        }
        if (siguienteCasilla == casillaAbajo) {
            colaCamino.push(UP);
            otraRuta.pop();
            return DOWN;
        }
        if (siguienteCasilla == casillaIzq) {
            colaCamino.push(RIGHT);
            otraRuta.pop();
            return LEFT;
        }
        if (siguienteCasilla == casillaDer) {
            colaCamino.push(LEFT);
            otraRuta.pop();
            return RIGHT;
        }

        return colaCamino.pop();
        /**
         * if (otraRuta.empty()) { return colaCamino.pop(); } //DE LA COLA HAY
         * QUE TIRARR SI POSIBLES RUTAS ESTA VACIO else //if
         * (!otraRuta.empty()){ /** if (otraRuta.firstElement()==1)
         * colaCamino.push(2); if (otraRuta.firstElement()==2)
         * colaCamino.push(1); if (otraRuta.firstElement()==3)
         * colaCamino.push(4); if (otraRuta.firstElement()==4)
         * colaCamino.push(3);
         */

    }

    @Override
    public void newCheese() {
    }

    /// <summary>
    /// Metodo que se ejecutará cada vez que nuestro raton reaparezca.
    /// </summary>
    @Override
    public void respawned() {
        mapa = new HashMap<Integer, Grid>();
        memoria = new Stack<Integer>();
    }
}
