// Codigo de cracks: M16B11
// UP: vale 1 |  DOWN: vale 2 | LEFT: vale 3 | RIGHT: vale 4 | BOMB: vale 5
package mouserun.mouse;

import mouserun.game.Mouse;
import mouserun.game.Grid;
import mouserun.game.Cheese;
import java.util.*;

public class M16B11estrella extends Mouse {

    HashMap<Integer, Grid> mapa;
    Stack<Integer> memoria;
    HashMap<Integer, Grid> maparecorrido;
    Vector<CasillaEstrella> nodos;
    Stack<Integer> vuelve;
    HashMap<Integer, Grid> exploraEstrella;
    boolean bombaCrack = false;
    boolean ultimaCasillaU;
    boolean ultimaCasillaD;
    boolean ultimaCasillaL;
    boolean ultimaCasillaR;
    boolean antibucle = false;
    int oportunidades = 40;
    int optimo = 400;

    /*Constructor del raton y lo nombra*/
    public M16B11estrella() {
        super("TonyWheel");
        mapa = new HashMap();
        memoria = new Stack();
        maparecorrido = new HashMap<Integer, Grid>();
        nodos = new Vector<CasillaEstrella>();
        vuelve = new Stack();
        exploraEstrella = new HashMap();
    }

    @Override
    // Comprueba si se dan las condiciones establecidas para colocar una bomba
    public int move(Grid currentGrid, Cheese cheese) {
        /*Si no conoce esta casilla, la guarda en la tabla*/
        if (mapa.get(clavemapa(currentGrid.getX(), currentGrid.getY())) == null) {
            mapa.put(clavemapa(currentGrid.getX(), currentGrid.getY()), currentGrid);
        }

        if (maparecorrido.get(clavemapa(currentGrid.getX(), currentGrid.getY())) == null) {
            incExploredGrids();
            maparecorrido.put(clavemapa(currentGrid.getX(), currentGrid.getY()), currentGrid);
        }

        if (bombaCrack && quesoExplosivo(currentGrid, cheese)) {
            return Mouse.BOMB;
        }
        //para desactivar el antibucle
        if (oportunidades == 0) {
            oportunidades = 40;
            antibucle = false;

        }
        if (maparecorrido.get(clavemapa(cheese.getX(), cheese.getY())) == null)// Si no ha visitado la casilla donde está el queso, usará el método por defecto (no informado) para viajar (acercaQueso)
        {
            if (!antibucle) {
                System.out.println("QUESO");
                return acercaQueso(currentGrid, cheese);
            } else {
                System.out.println("PROFUNDIDAD");
                oportunidades--;
                return enProfundidad(currentGrid, cheese);
            }
        } else {
            System.out.println("ESTRELLA");
            return AEstrella(currentGrid, cheese);
        }
    }
//clavemapa(currentGrid.getX(),currentGrid.getY())).conocida

    @Override
    public void newCheese() {
        antibucle = false;
        mapa = new HashMap<Integer, Grid>();
        memoria = new Stack<Integer>();
        nodos.clear();
        vuelve = new Stack();
        exploraEstrella = new HashMap();
        optimo = 400;
        boolean ultimaCasillaU = false;
        boolean ultimaCasillaD = false;
        boolean ultimaCasillaL = false;
        boolean ultimaCasillaR = false;
    }

    @Override
    public void respawned() {
        antibucle = false;
        mapa = new HashMap<Integer, Grid>();
        memoria = new Stack<Integer>();
        nodos.clear();
        vuelve = new Stack();
        exploraEstrella = new HashMap();
        optimo = 400;
        boolean ultimaCasillaU = false;
        boolean ultimaCasillaD = false;
        boolean ultimaCasillaL = false;
        boolean ultimaCasillaR = false;
    }

    /* métodos cuando el queso está en casilla desconocida */
    public int acercaQueso(Grid currentGrid, Cheese cheese) {

        if (cheese.getX() > currentGrid.getX() && currentGrid.canGoRight() && !ultimaCasillaR) {
            if (quesoCerca(currentGrid, cheese)) {
                memoria.push(LEFT);
                compruebaBucle(currentGrid, 4);
                return Mouse.RIGHT;
            }
            ultimaCasillaL = true; //Simboliza cual fue la última casilla que visitó para no hacer bucle infinito
            ultimaCasillaU = false;
            ultimaCasillaD = false;
            ultimaCasillaR = false;
            bombaCrack = true;
            memoria.push(LEFT);
            compruebaBucle(currentGrid, 4);
            return Mouse.RIGHT;
        }
        if (cheese.getY() > currentGrid.getY() && currentGrid.canGoUp() && !ultimaCasillaU) {
            if (quesoCerca(currentGrid, cheese)) {
                memoria.push(DOWN);
                compruebaBucle(currentGrid, 1);
                return Mouse.UP;
            }
            ultimaCasillaD = true;
            ultimaCasillaU = false;
            ultimaCasillaR = false;
            ultimaCasillaL = false;
            bombaCrack = true;
            memoria.push(DOWN);
            compruebaBucle(currentGrid, 1);
            return Mouse.UP;
        }
        if (cheese.getX() < currentGrid.getX() && currentGrid.canGoLeft() && !ultimaCasillaL) {
            if (quesoCerca(currentGrid, cheese)) {
                memoria.push(RIGHT);
                compruebaBucle(currentGrid, 3);
                return Mouse.LEFT;
            }
            ultimaCasillaR = true;
            ultimaCasillaD = false;
            ultimaCasillaU = false;
            ultimaCasillaL = false;
            bombaCrack = true;
            memoria.push(RIGHT);
            compruebaBucle(currentGrid, 3);
            return Mouse.LEFT;
        }
        if (cheese.getY() < currentGrid.getY() && currentGrid.canGoDown() && !ultimaCasillaD) {
            if (quesoCerca(currentGrid, cheese)) {
                memoria.push(UP);
                compruebaBucle(currentGrid, 2);
                return Mouse.DOWN;
            }
            ultimaCasillaU = true;
            ultimaCasillaD = false;
            ultimaCasillaL = false;
            ultimaCasillaR = false;
            bombaCrack = true;
            memoria.push(UP);
            compruebaBucle(currentGrid, 2);
            return Mouse.DOWN;
        }
        if (currentGrid.canGoDown() && !ultimaCasillaD) {
            if (quesoCerca(currentGrid, cheese)) {
                memoria.push(UP);
                compruebaBucle(currentGrid, 2);
                return Mouse.DOWN;
            }
            ultimaCasillaU = true;
            ultimaCasillaD = false;
            ultimaCasillaL = false;
            ultimaCasillaR = false;
            bombaCrack = true;
            memoria.push(UP);
            compruebaBucle(currentGrid, 2);
            return Mouse.DOWN;
        }
        if (currentGrid.canGoUp() && !ultimaCasillaU) {
            if (quesoCerca(currentGrid, cheese)) {
                memoria.push(DOWN);
                compruebaBucle(currentGrid, 1);
                return Mouse.UP;
            }
            ultimaCasillaD = true;
            ultimaCasillaU = false;
            ultimaCasillaR = false;
            ultimaCasillaL = false;
            bombaCrack = true;
            memoria.push(DOWN);
            compruebaBucle(currentGrid, 1);
            return Mouse.UP;
        }
        if (currentGrid.canGoRight() && !ultimaCasillaR) {
            if (quesoCerca(currentGrid, cheese)) {
                memoria.push(LEFT);
                compruebaBucle(currentGrid, 4);
                return Mouse.RIGHT;
            }
            ultimaCasillaL = true;
            ultimaCasillaU = false;
            ultimaCasillaD = false;
            ultimaCasillaR = false;
            bombaCrack = true;
            memoria.push(LEFT);
            compruebaBucle(currentGrid, 4);
            return Mouse.RIGHT;
        }
        if (currentGrid.canGoLeft() && !ultimaCasillaL) {
            if (quesoCerca(currentGrid, cheese)) {
                memoria.push(RIGHT);
                compruebaBucle(currentGrid, 3);
                return Mouse.LEFT;
            }
            ultimaCasillaR = true;
            ultimaCasillaD = false;
            ultimaCasillaU = false;
            ultimaCasillaL = false;
            bombaCrack = true;
            memoria.push(RIGHT);
            compruebaBucle(currentGrid, 3);
            return Mouse.LEFT;
        }

        if (ultimaCasillaL) {
            if (quesoCerca(currentGrid, cheese)) {
                memoria.push(RIGHT);
                compruebaBucle(currentGrid, 3);
                return Mouse.LEFT;
            }
            ultimaCasillaR = true;
            ultimaCasillaD = false;
            ultimaCasillaU = false;
            ultimaCasillaL = false;
            bombaCrack = true;
            memoria.push(RIGHT);
            compruebaBucle(currentGrid, 3);
            return Mouse.LEFT;
        }
        if (ultimaCasillaD) {
            if (quesoCerca(currentGrid, cheese)) {
                memoria.push(UP);
                compruebaBucle(currentGrid, 2);
                return Mouse.DOWN;
            }
            ultimaCasillaU = true;
            ultimaCasillaD = false;
            ultimaCasillaL = false;
            ultimaCasillaR = false;
            bombaCrack = true;
            memoria.push(UP);
            compruebaBucle(currentGrid, 2);
            return Mouse.DOWN;
        }
        if (ultimaCasillaR) {
            if (quesoCerca(currentGrid, cheese)) {
                memoria.push(LEFT);
                compruebaBucle(currentGrid, 4);
                return Mouse.RIGHT;
            }
            ultimaCasillaL = true;
            ultimaCasillaU = false;
            ultimaCasillaD = false;
            ultimaCasillaR = false;
            bombaCrack = true;
            memoria.push(LEFT);
            compruebaBucle(currentGrid, 4);
            return Mouse.RIGHT;
        }
        if (quesoCerca(currentGrid, cheese)) {
            memoria.push(DOWN);
            compruebaBucle(currentGrid, 1);
            return Mouse.UP;
        }
        ultimaCasillaD = true;
        ultimaCasillaU = false;
        ultimaCasillaR = false;
        ultimaCasillaL = false;
        bombaCrack = true;
        return Mouse.LEFT;
    }

    // Pone una bombica si la próxima el queso está a una casilla accesible
    public boolean quesoExplosivo(Grid casillaActual, Cheese quesito) {
        if (quesito.getY() == casillaActual.getY()) {
            if ((quesito.getX() == casillaActual.getX() + 1 && casillaActual.canGoRight()) || (quesito.getX() == casillaActual.getX() - 1 && casillaActual.canGoLeft())) {
                bombaCrack = false; // Evita que se quede estancado poniendo bombas
                oportunidades = 40;
                return true;
            }
        }
        if (quesito.getX() == casillaActual.getX()) {

            if ((quesito.getY() == casillaActual.getY() + 1 && casillaActual.canGoUp()) || (quesito.getY() == casillaActual.getY() - 1 && casillaActual.canGoDown())) {
                bombaCrack = false;
                oportunidades = 40;
                return true;
            }
        }
        return false;
    }

    public void compruebaBucle(Grid currentGrid, int movimiento) {
        if (movimiento == 1) {
            if ((mapa.get(clavemapa(currentGrid.getX(), currentGrid.getY() + 1)) != null) && currentGrid.canGoUp()) {
                antibucle = true;
            }
        }
        if (movimiento == 2) {
            if (!(mapa.get(clavemapa(currentGrid.getX(), currentGrid.getY() - 1)) == null) && currentGrid.canGoDown()) {
                antibucle = true;
            }
            if (movimiento == 3) {
                if (!(mapa.get(clavemapa(currentGrid.getX() - 1, currentGrid.getY())) == null) && currentGrid.canGoLeft()) {
                    antibucle = true;
                }
            }
            if (!(mapa.get(clavemapa(currentGrid.getX() + 1, currentGrid.getY())) == null) && currentGrid.canGoRight()) {
                antibucle = true;
            }
        }
    }

    public int enProfundidad(Grid currentGrid, Cheese quesito) {

        //Coge preferencia a tomar el camino que está más cerca del queso
        //PREFERENCIA ENFOCADA HACIA ABAJO
        if (currentGrid.getY() > quesito.getY()) {
            if (currentGrid.canGoDown() && profundMapa(currentGrid.getX(), currentGrid.getY() - 1)) {
                boolean ultimaCasillaU = true;
                boolean ultimaCasillaD = false;
                boolean ultimaCasillaL = false;
                boolean ultimaCasillaR = false;
                memoria.push(1);
                return Mouse.DOWN;
            }
            if (currentGrid.getX() < quesito.getX()) {
                if (currentGrid.canGoRight() && profundMapa(currentGrid.getX() + 1, currentGrid.getY())) {
                    boolean ultimaCasillaU = false;
                    boolean ultimaCasillaD = false;
                    boolean ultimaCasillaL = true;
                    boolean ultimaCasillaR = false;
                    memoria.push(3);
                    return Mouse.RIGHT;
                }
                if (currentGrid.canGoLeft() && profundMapa(currentGrid.getX() - 1, currentGrid.getY())) {
                    boolean ultimaCasillaU = false;
                    boolean ultimaCasillaD = false;
                    boolean ultimaCasillaL = false;
                    boolean ultimaCasillaR = true;
                    memoria.push(4);
                    return Mouse.LEFT;
                }
            } else {
                if (currentGrid.canGoLeft() && profundMapa(currentGrid.getX() - 1, currentGrid.getY())) {
                    boolean ultimaCasillaU = false;
                    boolean ultimaCasillaD = false;
                    boolean ultimaCasillaL = false;
                    boolean ultimaCasillaR = true;
                    memoria.push(4);
                    return Mouse.LEFT;
                }
                if (currentGrid.canGoRight() && profundMapa(currentGrid.getX() + 1, currentGrid.getY())) {
                    boolean ultimaCasillaU = false;
                    boolean ultimaCasillaD = false;
                    boolean ultimaCasillaL = false;
                    boolean ultimaCasillaR = true;
                    memoria.push(3);
                    return Mouse.RIGHT;
                }
            }
            if (currentGrid.canGoUp() && profundMapa(currentGrid.getX(), currentGrid.getY() + 1)) {
                boolean ultimaCasillaU = false;
                boolean ultimaCasillaD = true;
                boolean ultimaCasillaL = false;
                boolean ultimaCasillaR = false;
                memoria.push(2);
                return Mouse.UP;
            }
            if (!memoria.empty()) {
                return memoria.pop();
            } else {
                mapa = new HashMap<Integer, Grid>();
                return 0;
            }
            //PREFERENCIA ENFOCADA HACIA ARRIBA
        } else {
            if (currentGrid.canGoUp() && profundMapa(currentGrid.getX(), currentGrid.getY() + 1)) {
                boolean ultimaCasillaU = false;
                boolean ultimaCasillaD = true;
                boolean ultimaCasillaL = false;
                boolean ultimaCasillaR = false;
                memoria.push(2);
                return Mouse.UP;
            }
            if (currentGrid.getX() < quesito.getX()) {
                if (currentGrid.canGoRight() && profundMapa(currentGrid.getX() + 1, currentGrid.getY())) {
                    boolean ultimaCasillaU = false;
                    boolean ultimaCasillaD = false;
                    boolean ultimaCasillaL = true;
                    boolean ultimaCasillaR = false;
                    memoria.push(3);
                    return Mouse.RIGHT;
                }
                if (currentGrid.canGoLeft() && profundMapa(currentGrid.getX() - 1, currentGrid.getY())) {
                    boolean ultimaCasillaU = false;
                    boolean ultimaCasillaD = false;
                    boolean ultimaCasillaL = false;
                    boolean ultimaCasillaR = true;
                    memoria.push(4);
                    return Mouse.LEFT;
                }
            } else {
                if (currentGrid.canGoLeft() && profundMapa(currentGrid.getX() - 1, currentGrid.getY())) {
                    boolean ultimaCasillaU = false;
                    boolean ultimaCasillaD = false;
                    boolean ultimaCasillaL = false;
                    boolean ultimaCasillaR = true;
                    memoria.push(4);
                    return Mouse.LEFT;
                }
                if (currentGrid.canGoRight() && profundMapa(currentGrid.getX() + 1, currentGrid.getY())) {
                    boolean ultimaCasillaU = false;
                    boolean ultimaCasillaD = false;
                    boolean ultimaCasillaL = true;
                    boolean ultimaCasillaR = false;
                    memoria.push(3);
                    return Mouse.RIGHT;
                }
            }
            if (currentGrid.canGoDown() && profundMapa(currentGrid.getX(), currentGrid.getY() - 1)) {
                boolean ultimaCasillaU = true;
                boolean ultimaCasillaD = false;
                boolean ultimaCasillaL = false;
                boolean ultimaCasillaR = false;
                memoria.push(1);
                return Mouse.DOWN;
            }
            if (!memoria.empty()) {
                return memoria.pop();
            } else {
                mapa = new HashMap<Integer, Grid>();
            }
        }
        return 0;
    }

    private static int clavemapa(int x, int y) {
        return (x * 100 + y);
    }

    // Comprueba si la casilla x,y ya está explorada (true) o no (false)
    public boolean profundMapa(int x, int y) {
        if ((mapa.get(clavemapa(x, y))) == null) {
            return true;
        } else {
            return false;
        }
    }

    // Utilidades del algoritmo de manhattam
    public static int manhattam(int xi, int xc, int yi, int yc) {
        return Math.abs(xi - xc) + Math.abs(yi - yc);
    }

    public static int manhattamAlgQueso(Grid currentGrid, Cheese queso) {
        return manhattam(currentGrid.getX(), queso.getX(), currentGrid.getY(), queso.getY());
    }

    public static int manhattamAlgQueso(int x, int y, Cheese queso) {
        return manhattam(x, queso.getX(), y, queso.getY());
    }

    public static int manhattamAlgCasilla(Grid currentGrid, Grid casilla) {
        return manhattam(currentGrid.getX(), casilla.getX(), currentGrid.getY(), casilla.getY());
    }

    public int AEstrella(Grid currentGrid, Cheese queso) {
        if (currentGrid.canGoUp() && maparecorrido.get(clavemapa(currentGrid.getX(), currentGrid.getY() + 1)) != null && exploraEstrella.get(clavemapa(currentGrid.getX(), currentGrid.getY() + 1)) == null) {
            System.out.println("Añadiendo Arriba");
            CasillaEstrella ca = new CasillaEstrella(currentGrid.getX(), currentGrid.getY() + 1, queso);
            System.out.println("valor" + ca.mQueso);
            nodos.add(ca);
            exploraEstrella.put(clavemapa(currentGrid.getX(), currentGrid.getY() + 1), currentGrid);
        }
        if (currentGrid.canGoDown() && maparecorrido.get(clavemapa(currentGrid.getX(), currentGrid.getY() - 1)) != null && exploraEstrella.get(clavemapa(currentGrid.getX(), currentGrid.getY() - 1)) == null) {
            System.out.println("Añadiendo Abajo");
            CasillaEstrella ca = new CasillaEstrella(currentGrid.getX(), currentGrid.getY() - 1, queso);
            System.out.println("valor" + ca.mQueso);
            nodos.add(ca);
            exploraEstrella.put(clavemapa(currentGrid.getX(), currentGrid.getY() - 1), currentGrid);
        }
        if (currentGrid.canGoLeft() && maparecorrido.get(clavemapa(currentGrid.getX() - 1, currentGrid.getY())) != null && exploraEstrella.get(clavemapa(currentGrid.getX() - 1, currentGrid.getY())) == null) {
            System.out.println("Añadiendo Izquierda");
            CasillaEstrella ca = new CasillaEstrella(currentGrid.getX() - 1, currentGrid.getY(), queso);
            System.out.println("valor" + ca.mQueso);
            nodos.add(ca);
            exploraEstrella.put(clavemapa(currentGrid.getX() - 1, currentGrid.getY()), currentGrid);
        }
        if (currentGrid.canGoRight() && maparecorrido.get(clavemapa(currentGrid.getX() + 1, currentGrid.getY())) != null && exploraEstrella.get(clavemapa(currentGrid.getX() + 1, currentGrid.getY())) == null) {
            System.out.println("Añadiendo Derecha");
            CasillaEstrella ca = new CasillaEstrella(currentGrid.getX() + 1, currentGrid.getY(), queso);
            System.out.println("valor" + ca.mQueso);
            nodos.add(ca);
            exploraEstrella.put(clavemapa(currentGrid.getX() + 1, currentGrid.getY()), currentGrid);
        }
        int posiOptima = 0;
        for (int i = 0; i < nodos.size(); i++) {
            if (nodos.get(i).mQueso <= optimo) {
                posiOptima = i;
            }
        }
        if ((currentGrid.getY() + 1 == nodos.get(posiOptima).Y) && (currentGrid.getX() == nodos.get(posiOptima).X)) {
            System.out.println("EN A ESTRELLA ARRIBA");
            nodos.remove(posiOptima);
            vuelve.push(DOWN);
            return UP;
        }
        if ((currentGrid.getY() - 1 == nodos.get(posiOptima).Y) && (currentGrid.getX() == nodos.get(posiOptima).X)) {
            System.out.println("EN A ESTRELLA ABAJO");
            nodos.remove(posiOptima);
            vuelve.push(UP);
            return DOWN;
        }
        if ((currentGrid.getX() + 1 == nodos.get(posiOptima).X) && (currentGrid.getY() == nodos.get(posiOptima).Y)) {
            System.out.println("EN A ESTRELLA DERECHA");
            nodos.remove(posiOptima);
            vuelve.push(LEFT);
            return RIGHT;
        }
        if ((currentGrid.getX() - 1 == nodos.get(posiOptima).X) && (currentGrid.getY() == nodos.get(posiOptima).Y)) {
            System.out.println("EN A ESTRELLA IZQUIERDA");
            nodos.remove(posiOptima);
            vuelve.push(RIGHT);
            return LEFT;

        }
        if (!vuelve.empty()) {
            System.out.println("vuelve");
            return vuelve.pop();
        }
        if (!memoria.empty()){
            System.out.println("memoria");
            return memoria.pop();
        }
        return acercaQueso(currentGrid, queso);
    }
    //anula la memoria de la última casilla para recalcular direccion

    public boolean quesoCerca(Grid casillaActual, Cheese quesito) {
        if (quesito.getY() == casillaActual.getY()) {
            if ((quesito.getX() == casillaActual.getX() + 1 && casillaActual.canGoRight()) || (quesito.getX() == casillaActual.getX() - 1 && casillaActual.canGoLeft())) {
                ultimaCasillaU = false;
                ultimaCasillaD = false;
                ultimaCasillaL = false;
                ultimaCasillaR = false;
                return true;
            }
        }
        if (quesito.getX() == casillaActual.getX()) {
            if ((quesito.getY() == casillaActual.getY() + 1 && casillaActual.canGoUp()) || (quesito.getY() == casillaActual.getY() - 1 && casillaActual.canGoDown())) {
                ultimaCasillaU = false;
                ultimaCasillaD = false;
                ultimaCasillaL = false;
                ultimaCasillaR = false;
                return true;
            }
        }
        return false;
    }

    public class CasillaEstrella {

        public int X;
        public int Y;
        public int mQueso;

        public CasillaEstrella(Grid currentGrid, Cheese queso) {
            X = currentGrid.getX();
            Y = currentGrid.getY();
            mQueso = manhattamAlgQueso(currentGrid, queso);
        }

        public CasillaEstrella(int x, int y, Cheese queso) {
            X = x;
            Y = y;
            mQueso = manhattamAlgQueso(x, y, queso);
        }
    }
}
