/* Estrella */
package mouserun.mouse;

import mouserun.game.Mouse;
import mouserun.game.Grid;
import mouserun.game.Cheese;
import java.util.*;

public class M16B11a extends Mouse {

    HashMap<Integer, Grid> mapa;
    Stack<Integer> memoria;
    private final HashMap<Integer, Grid> maparecorrido;
    private final LinkedList<Grid> mapaSinExplorar;
    private Nodo estrella;
    private final camino camino;
    boolean bombaCrack = false;
    boolean ultimaCasillaU;
    boolean ultimaCasillaD;
    boolean ultimaCasillaL;
    boolean ultimaCasillaR;
    boolean antibucle = false;
    boolean conectar = true;
    private Grid ultimaCasilla;
    private Grid penultimaCasilla;
    int oportunidades = 40;
    int bombas = 0;

    /*Constructor del raton y lo nombra*/
    public M16B11a() {
        super("TonyWheel");
        mapa = new HashMap();
        memoria = new Stack();
        Nodo.explorado = maparecorrido = new HashMap<Integer, Grid>();
        camino = new camino();
        ultimaCasilla = null;
        penultimaCasilla = null;
        estrella = null;
        mapaSinExplorar = new LinkedList<>();

    }

    @Override
    // Comprueba si se dan las condiciones establecidas para colocar una bomba
    public int move(Grid currentGrid, Cheese cheese) {

        penultimaCasilla = ultimaCasilla;
        ultimaCasilla = currentGrid;
        /*Si no conoce esta casilla, la guarda en la tabla*/
        if (mapa.get(clavemapa(currentGrid.getX(), currentGrid.getY())) == null) {
            mapa.put(clavemapa(currentGrid.getX(), currentGrid.getY()), currentGrid);
            if (conectar) {
                camino.add(ultimaCasilla, penultimaCasilla);
            } else {
                conectar = true;
            }
        }

        if (maparecorrido.get(clavemapa(currentGrid.getX(), currentGrid.getY())) == null) {
            incExploredGrids();
            maparecorrido.put(clavemapa(currentGrid.getX(), currentGrid.getY()), currentGrid);
        }

        /*if (bombaCrack && quesoExplosivo(currentGrid, cheese) && bombas < 5) {
            bombas++;
            return Mouse.BOMB;
        }*/
        // Secuencias que comprueban si el queso está alcanzable a una casilla para moverse directamente
        if ((currentGrid.getX() + 1 == cheese.getX()) && (currentGrid.getY() == cheese.getY()) && currentGrid.canGoRight()) {
            return RIGHT;
        }
        if ((currentGrid.getX() - 1 == cheese.getX()) && (currentGrid.getY() == cheese.getY()) && currentGrid.canGoLeft()) {
            return LEFT;
        }
        if ((currentGrid.getX() == cheese.getX()) && (currentGrid.getY() + 1 == cheese.getY()) && currentGrid.canGoUp()) {
            return UP;
        }
        if ((currentGrid.getX() == cheese.getX()) && (currentGrid.getY() - 1 == cheese.getY()) && currentGrid.canGoDown()) {
            return DOWN;
        }

        //para desactivar el antibucle
        if (oportunidades == 0) {
            oportunidades = 40;
            antibucle = false;

        }
        if (maparecorrido.get(clavemapa(cheese.getX(), cheese.getY())) == null)// Si no ha visitado la casilla donde está el queso, usará el método por defecto (no informado) para viajar (acercaQueso)
        {
            if (!antibucle) {
                return acercaQueso(currentGrid, cheese);
            } else {
                oportunidades--;
                return enProfundidad(currentGrid, cheese);
            }
        } else {
            return movimientoEstrella(currentGrid, cheese);
        }
    }
//clavemapa(currentGrid.getX(),currentGrid.getY())).conocida

    @Override
    public void newCheese() {
        antibucle = false;
        mapa = new HashMap<Integer, Grid>();
        memoria = new Stack<Integer>();
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
        boolean ultimaCasillaU = false;
        boolean ultimaCasillaD = false;
        boolean ultimaCasillaL = false;
        boolean ultimaCasillaR = false;
        ultimaCasilla = null;
        conectar = false;
    }

    /* métodos cuando el queso está en casilla desconocida */
    /**
     *
     * @param currentGrid casilla actual del ratón
     * @param cheese situación del queso
     * @return devuelve el valor de dirección en la que el ratón debe moverse
     * para acercarse al queso
     */
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

    // Pone una bomba si la próxima el queso está a una casilla accesible
    /**
     *
     * @param casillaActual casilla actual del ratón
     * @param quesito situación del queso
     * @return devuelve TRUE si el ratón está a una casilla alcanzable del queso
     * (pone bomba). FALSE en otro caso
     */
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

    /**
     *
     * @param currentGrid casilla actual del ratón
     * @param movimiento siguiente movimiento a realizar por el ratón
     * @note Ésta función cambia el valor "antibucle" que se activa a true si el
     * ratón va a recorrer de nuevo un camino de búsqueda que ya ha usado
     */
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

    /**
     *
     * @param currentGrid casilla actual del ratón
     * @param quesito situación actual del queso
     * @return devuelve el movimiento a realizar para una búsqueda en
     * profundidad (prioriza casillas cercanas al queso)
     */
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

    /**
     *
     * @param x situación en horizontal
     * @param y situación en vertical
     * @return genera una clave para una casilla concreta del mapa
     */
    private static int clavemapa(int x, int y) {
        return (x * 100 + y);
    }

    // Comprueba si la casilla x,y ya está explorada (true) o no (false)
    /**
     *
     * @param x situación en horizontal
     * @param y situación en vertical
     * @return devuelve TRUE si la casilla está explorada. FALSE en otro caso
     */
    public boolean profundMapa(int x, int y) {
        if ((mapa.get(clavemapa(x, y))) == null) {
            return true;
        } else {
            return false;
        }
    }

    // Diferentes cálculos de manhattan según datos

    /**
     *
     * @param xi situación horizontal de la primera casilla
     * @param xc situación horizontal de la segunda casilla
     * @param yi situación vertical de la primera casilla
     * @param yc situación vertical de la segunda casilla
     * @return valor después de calcular manhattam con los datos dados.
     */
    public static int manhattam(int xi, int xc, int yi, int yc) {
        return Math.abs(xi - xc) + Math.abs(yi - yc);
    }

    /**
     *
     * @param currentGrid situación de una casilla
     * @param queso situación del ratón
     * @return valor después de calcular manhattam con los datos dados.
     */
    public static int manhattamAlgQueso(Grid currentGrid, Cheese queso) {
        return manhattam(currentGrid.getX(), queso.getX(), currentGrid.getY(), queso.getY());
    }

    /**
     *
     * @param x situación horizontal de una casilla
     * @param y situación vertical de una casilla
     * @param queso situación del ratón
     * @return valor después de calcular manhattam con los datos dados.
     */
    public static int manhattamAlgQueso(int x, int y, Cheese queso) {
        return manhattam(x, queso.getX(), y, queso.getY());
    }

    /**
     *
     * @param currentGrid situación de una casilla
     * @param casilla situación de otra casilla
     * @return valor después de calcular manhattam con los datos dados.
     */
    public static int manhattamAlgCasilla(Grid currentGrid, Grid casilla) {
        return manhattam(currentGrid.getX(), casilla.getX(), currentGrid.getY(), casilla.getY());
    }

    private int movimientoEstrella(Grid currentGrid, Cheese queso) {

        if (estrella == null && maparecorrido.get(clavemapa(currentGrid.getX(), currentGrid.getY())) != null) {
            estrella = AEstrella(currentGrid, queso);
        }

        if (estrella != null && estrella.padre != null) {
            int d = estrella.Padre();
            estrella = estrella.padre;
            if (d == 0) {
                return enProfundidad(currentGrid, queso);
            }
            return d;
        } else {
        return enProfundidad(currentGrid, queso);
        }
    }

    private Nodo AEstrella(Grid currentGrid, Cheese queso) {
        int intentos = 0;
        Grid objetivo = maparecorrido.get(clavemapa(queso.getX(), queso.getY()));

        if (objetivo != null && camino.conectaCamino(objetivo, currentGrid)) {
            return AEstrella(objetivo, currentGrid);
        }

        LinkedList<Grid> Llist = new LinkedList<>();
        while ((objetivo = casillaCerca(queso, mapaSinExplorar, Llist)) != null && intentos < 400) {
            intentos++;
            if (camino.conectaCamino(objetivo, currentGrid)) {
                return AEstrella(objetivo, currentGrid);
            }
            Llist.add(objetivo);
        }
        return null;
    }

    private static Nodo AEstrella(Grid Inicio, Grid Fin) {
        Nodo inicio = new Nodo(null, Inicio, Fin, 0);
        if (inicio.casilla.getX() == Fin.getX() && inicio.casilla.getY() == Fin.getY()) {
            return inicio;
        }
        PriorityQueue<Nodo> lista = new PriorityQueue<>(512);
        HashMap<Integer, Nodo> busca = new HashMap<>(512);

        lista.add(inicio);
        busca.put(clavemapa(inicio.casilla.getX(), inicio.casilla.getY()), inicio);
        int intentos = 0;
        while (!lista.isEmpty() && intentos < 400) {
            intentos++;
            Nodo q = lista.poll();
            LinkedList<Nodo> hijos = q.crearHijos(Fin);
            for (Nodo hijo : hijos) {
                if ((hijo.casilla.getX() == Fin.getX()) && hijo.casilla.getY() == Fin.getY()) {
                    return hijo;
                }
                if (!mejoraCamino(busca, hijo)) {
                    lista.add(hijo);
                    busca.put(clavemapa(hijo.casilla.getX(), hijo.casilla.getY()), hijo);
                }
            }
        }
        return null;
    }

    private Grid casillaCerca(Cheese queso, LinkedList<Grid> a, LinkedList<Grid> b) {
        ListIterator<Grid> ListaHojas = a.listIterator();
        Grid hoja = null;
        int intentos = 0;
        while (ListaHojas.hasNext() && intentos < 100) {
            Grid g = ListaHojas.next();
            if (posiblesCaminos(g) > 0) {
                if (!b.contains(g)) {
                    hoja = g;
                    break;
                }
            } else {
                ListaHojas.remove();
            }
        }
        intentos = 0;
        while (ListaHojas.hasNext() && intentos < 100) {
            intentos++;
            Grid alternative = ListaHojas.next();
            if (posiblesCaminos(alternative) > 0) {
                if (!b.contains(alternative) && (manhattamAlgQueso(alternative, queso) < manhattamAlgQueso(hoja, queso))) {
                    hoja = alternative;
                }
            } else {
                ListaHojas.remove();
            }
        }
        return hoja;
    }

    private int posiblesCaminos(Grid casilla) {
        int n = 0;
        if (casilla.canGoUp() && maparecorrido.get(clavemapa(casilla.getX(), casilla.getY() + 1)) == null) {
            n++;
        }
        if (casilla.canGoDown() && maparecorrido.get(clavemapa(casilla.getX(), casilla.getY() - 1)) == null) {
            n++;
        }
        if (casilla.canGoLeft() && maparecorrido.get(clavemapa(casilla.getX() - 1, casilla.getY())) == null) {
            n++;
        }
        if (casilla.canGoRight() && maparecorrido.get(clavemapa(casilla.getX() + 1, casilla.getY())) == null) {
            n++;
        }

        return n;
    }

    //anula la memoria de la última casilla para recalcular direccion

    /**
     *
     * @param casillaActual situación del ratón
     * @param quesito situación del queso
     * @return devuelve un movimiento directo hacia el queso en caso de estar accesible
     */
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

    private static boolean mejoraCamino(HashMap<Integer, Nodo> hm, Nodo hijo) {
        Nodo n = hm.get(clavemapa(hijo.casilla.getX(), hijo.casilla.getY()));
        return (n != null && (n.AcCoste + n.coste) < (hijo.AcCoste + hijo.coste));
    }

    private static class Nodo implements Comparable<Nodo> {

        public static HashMap<Integer, Grid> explorado;
        private final Nodo padre;
        private final Grid casilla;
        private final int coste;
        private final int AcCoste;

        public Nodo(Nodo Padre, Grid Casilla, Grid Objetivo, int ACCoste) {
            padre = Padre;
            casilla = Casilla;
            AcCoste = ACCoste;
            coste = manhattam(casilla.getX(), casilla.getY(), Objetivo.getX(), Objetivo.getY());
        }

        @Override
        public int compareTo(Nodo n) {
            return (this.AcCoste + this.coste) - (n.AcCoste + n.coste);
        }

        public int Padre() {
            if (this.padre.casilla.getY() > this.casilla.getY()) {
                return UP;
            }
            if (this.padre.casilla.getY() < this.casilla.getY()) {
                return DOWN;
            }
            if (this.padre.casilla.getX() < this.casilla.getX()) {
                return LEFT;
            }
            if (this.padre.casilla.getX() > this.casilla.getX()) {
                return RIGHT;
            }
            return 0;
        }

        public LinkedList<Nodo> crearHijos(Grid objetivo) {

            LinkedList<Nodo> hijos = new LinkedList<>();
            if (casilla.canGoUp()) {
                Grid hijo = explorado.get(clavemapa(casilla.getX(), casilla.getY() + 1));
                if (hijo != null) {
                    hijos.add(new Nodo(this, hijo, objetivo, this.AcCoste + 1));
                }
            }
            if (casilla.canGoDown()) {
                Grid hijo = explorado.get(clavemapa(casilla.getX(), casilla.getY() - 1));
                if (hijo != null) {
                    hijos.add(new Nodo(this, hijo, objetivo, this.AcCoste + 1));
                }
            }
            if (casilla.canGoLeft()) {
                Grid hijo = explorado.get(clavemapa(casilla.getX() - 1, casilla.getY()));
                if (hijo != null) {
                    hijos.add(new Nodo(this, hijo, objetivo, this.AcCoste + 1));
                }
            }
            if (casilla.canGoRight()) {
                Grid hijo = explorado.get(clavemapa(casilla.getX() + 1, casilla.getY()));
                if (hijo != null) {
                    hijos.add(new Nodo(this, hijo, objetivo, this.AcCoste + 1));
                }

            }

            return hijos;
        }

    }

    private static class camino extends HashMap<Integer, Integer> {

        public camino() {
            super(2048);
        }

        public void add(Grid c1, Grid c2) {
            this.put(clavemapa(c1.getX(), c1.getY()), ((c2 == null) ? clavemapa(c1.getX(), c1.getY()) : clavemapa(c2.getX(), c2.getY())));

            int inicio = this.busqueda(c1);

            if (c1.canGoUp() && this.containsKey(clavemapa(c1.getX(), c1.getY() + 1)) && this.get(clavemapa(c1.getX(), c1.getY() + 1)) != inicio) {
                int cn = this.busqueda(clavemapa(c1.getX(), c1.getY() + 1));
                if (cn != inicio) {
                    this.put(cn, inicio);
                }
            }
            if (c1.canGoDown() && this.containsKey(clavemapa(c1.getX(), c1.getY() - 1)) && this.get(clavemapa(c1.getX(), c1.getY() - 1)) != inicio) {
                int cn = this.busqueda(clavemapa(c1.getX(), c1.getY() - 1));
                if (cn != inicio) {
                    this.put(cn, inicio);
                }
            }
            if (c1.canGoLeft() && this.containsKey(clavemapa(c1.getX() - 1, c1.getY())) && this.get(clavemapa(c1.getX() - 1, c1.getY())) != inicio) {
                int cn = this.busqueda(clavemapa(c1.getX() - 1, c1.getY()));
                if (cn != inicio) {
                    this.put(cn, inicio);
                }
            }
            if (c1.canGoRight() && this.containsKey(clavemapa(c1.getX() + 1, c1.getY())) && this.get(clavemapa(c1.getX() + 1, c1.getY())) != inicio) {
                int cn = this.busqueda(clavemapa(c1.getX() + 1, c1.getY()));
                if (cn != inicio) {
                    this.put(cn, inicio);
                }
            }

        }

        public boolean conectaCamino(Grid c1, Grid c2) {
            return (busqueda(c1) == busqueda(c2));
        }

        public int busqueda(Grid c) {
            return busqueda(clavemapa(c.getX(), c.getY()));
        }

        private int busqueda(int h) {
            int intentos = 0;
            int b, a = h;
            while (h != this.get(h) && intentos < 40) {
                h = this.get(h);
                intentos++;
            }
            intentos = 0;
            while (h != (b = this.get(a)) && intentos < 40) {
                this.put(a, h);
                a = b;
            }
            return h;
        }
    }
}