import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProyectoDamasInglesasV7 {

    // Códigos ANSI para colores en consola
    private static final String RESET = "\u001B[0m";
    private static final String ROJO = "\u001B[31m";
    private static final String BLANCO = "\u001B[97m";

    public static void limpiar() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private static final int TAMANO_TABLERO = 8;
    // Convención de la matriz:
    //  0: Casilla vacía
    //  1 a 12: Ficha Roja
    // -1 a -12: Ficha Blanca
    // > 100: Dama Roja (ID original + 100)
    // < -100: Dama Blanca (ID original - 100)
    private static int[][] tablero = new int[TAMANO_TABLERO][TAMANO_TABLERO];
    private static int turnoActual = 2; // Inician las Blancas (2) siempre

    // Contadores de fichas comidas
    private static int comidasPorRojas = 0;
    private static int comidasPorBlancas = 0;

    // Registro histórico de los movimientos ejecutados en la partida
    private static List<String> historialMovimientos = new ArrayList<>();

    public static void reglamento(){
        System.out.println("");
        System.out.println("==========================================================================");
        System.out.println("                         MOVIMIENTOS BASICOS.");
        System.out.println("  -Peones: Se mueven una sola casilla a la vez en diagonal hacia ");
        System.out.println("  adelante, siempre a un espacio vacío. No pueden retroceder.");
        System.out.println("  -Captura (comer): Se salta por encima de la ficha contraria adyacente");
        System.out.println("  hacia la casilla vacía que está detrás de ella. La ficha capturada");
        System.out.println("  se retira del tablero.");
        System.out.println("  -Capturas múltiples: Si tras un salto existe la opción de realizar");
        System.out.println("  otro salto con la misma pieza, se continúa comiendo en el mismo turno.");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("                        CORONACION Y DAMAS(reyes).");
        System.out.println("  -Coronar: Cuando un peón llega a la última fila del lado opuesto del");
        System.out.println("  tablero, se convierte en dama.");
        System.out.println("  -Movimiento de la dama: Se mueve en diagonal un paso hacia adelante o atrás.");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("                             FIN DEL JUEGO.");
        System.out.println("  -Victoria: Gana el jugador que captura todas las piezas del rival o");
        System.out.println("  o deja al adversario sin movimientos válidos.");
        System.out.println("  -Empate: Se declara tablas si ambos jugadores acuerdan el empate o la");
        System.out.println("  posición no permite avanzar hacia la victoria.");
        System.out.println("==========================================================================");
        System.out.println("");
    }
    
    private static void inicializarPiezas() {
        for (int f = 0; f < TAMANO_TABLERO; f++) {
            for (int c = 0; c < TAMANO_TABLERO; c++) {
                tablero[f][c] = 0;
            }
        }

        int contadorRojas = 1;
        int contadorBlancas = 1;
        comidasPorRojas = 0;
        comidasPorBlancas = 0;
        historialMovimientos.clear();

        for (int f = 0; f < TAMANO_TABLERO; f++) {
            for (int c = 0; c < TAMANO_TABLERO; c++) {
                if ((f + c) % 2 != 0) {
                    if (f < 3) {
                        tablero[f][c] = contadorRojas++;   // Rojas (1 a 12)
                    } else if (f > 4) {
                        tablero[f][c] = -contadorBlancas++; // Blancas (-1 a -12)
                    }
                }
            }
        }
    }

    private static void imprimirTablero() {
        limpiar();
        System.out.println("\n TABLERO DE JUEGO                   MARCADOR DE CAPTURAS");
        System.out.println(" ------------------------           -------------------");
        for (int f = 0; f < TAMANO_TABLERO; f++) {
            System.out.print(" ");
            for (int c = 0; c < TAMANO_TABLERO; c++) {
                int val = tablero[f][c];
                if (val > 0) { // Pieza Roja
                    if (val > 100) {
                        System.out.printf(ROJO + "%-3s" + RESET, "D" + (val - 100));
                    } else {
                        System.out.printf(ROJO + "%-3d" + RESET, val);
                    }
                } else if (val < 0) { // Pieza Blanca
                    if (val < -100) {
                        System.out.printf(BLANCO + "%-3s" + RESET, "D" + Math.abs(val + 100));
                    } else {
                        System.out.printf(BLANCO + "%-3d" + RESET, Math.abs(val));
                    }
                } else if ((f + c) % 2 != 0) {
                    System.out.print(".  "); // Casilla jugable vacía
                } else {
                    System.out.print("   "); // Casilla no jugable
                }
            }

            // Marcador al costado derecho del tablero
            if (f == 2) {
                System.out.print("        " + ROJO + "Rojas comieron:" + RESET + " " + comidasPorRojas);
            } else if (f == 4) {
                System.out.print("        " + BLANCO + "Blancas comieron:" + RESET + " " + comidasPorBlancas);
            }

            System.out.println();
        }
        System.out.println(" ------------------------           -------------------");
        System.out.println();
    }

    private static void guardarPartida() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("partida_damas.txt"))) {
            writer.println(turnoActual);
            writer.println(comidasPorRojas);
            writer.println(comidasPorBlancas);
            
            // Matriz del tablero
            for (int f = 0; f < TAMANO_TABLERO; f++) {
                for (int c = 0; c < TAMANO_TABLERO; c++) {
                    writer.print(tablero[f][c] + " ");
                }
                writer.println();
            }

            // Guardar el registro numérico/textual de movimientos
            writer.println(historialMovimientos.size());
            for (String mov : historialMovimientos) {
                writer.println(mov);
            }

            System.out.println("\n[!] ¡Partida y movimientos guardados exitosamente en 'partida_damas.txt'!");
        } catch (IOException e) {
            System.out.println("\n[!] Error al guardar la partida: " + e.getMessage());
        }
    }

    private static boolean cargarPartida() {
        File archivo = new File("partida_damas.txt");
        if (!archivo.exists()) {
            return false;
        }

        try (Scanner reader = new Scanner(new FileReader(archivo))) {
            if (!reader.hasNextInt()) return false;
            turnoActual = reader.nextInt();
            comidasPorRojas = reader.nextInt();
            comidasPorBlancas = reader.nextInt();

            for (int f = 0; f < TAMANO_TABLERO; f++) {
                for (int c = 0; c < TAMANO_TABLERO; c++) {
                    tablero[f][c] = reader.nextInt();
                }
            }

            historialMovimientos.clear();
            if (reader.hasNextInt()) {
                int totalMovs = reader.nextInt();
                reader.nextLine(); // Limpieza del buffer
                for (int i = 0; i < totalMovs && reader.hasNextLine(); i++) {
                    historialMovimientos.add(reader.nextLine());
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("\n[!] Error al cargar el archivo de guardado.");
            return false;
        }
    }

    private static void eliminarPartidaGuardada() {
        File archivo = new File("partida_damas.txt");
        if (archivo.exists()) {
            if (archivo.delete()) {
                System.out.println("\n[!] La ultima partida guardada ha sido eliminada correctamente.");
            } else {
                System.out.println("\n[!] No se pudo eliminar el archivo de la partida guardada.");
            }
        } else {
            System.out.println("\n[!] No existe ninguna partida guardada para eliminar.");
        }
    }
    
    private static int[] buscarFicha(int idFicha) {
        int objetivo = (turnoActual == 1) ? idFicha : -idFicha;
        for (int f = 0; f < TAMANO_TABLERO; f++) {
            for (int c = 0; c < TAMANO_TABLERO; c++) {
                int val = tablero[f][c];
                if (val == objetivo || (turnoActual == 1 && val == objetivo + 100) || (turnoActual == 2 && val == objetivo - 100)) {
                    return new int[]{f, c};
                }
            }
        }
        return null;
    }

    private static boolean esPropia(int val) {
        if (turnoActual == 1) return val > 0;
        if (turnoActual == 2) return val < 0;
        return false;
    }

    private static boolean esEnemiga(int val) {
        if (val == 0) return false;
        return !esPropia(val);
    }

    private static void verificarCoronacion(int fila, int col) {
        int val = tablero[fila][col];
        if (val > 0 && val <= 12 && fila == 7) {
            tablero[fila][col] = val + 100; // Dama Roja
        } else if (val < 0 && val >= -12 && fila == 0) {
            tablero[fila][col] = val - 100; // Dama Blanca
        }
    }

    private static int[] obtenerDireccionesFila(int valPieza) {
        boolean esDama = Math.abs(valPieza) > 100;
        if (esDama) {
            return new int[]{1, -1};
        }
        return new int[]{(turnoActual == 1) ? 1 : -1};
    }

    private static boolean puedeComerFicha(int f, int c) {
        int val = tablero[f][c];
        if (!esPropia(val)) return false;

        int[] dFilas = obtenerDireccionesFila(val);
        int[] dCols = {-1, 1}; // Izquierda (-1) y Derecha (+1) en la pantalla

        for (int df : dFilas) {
            for (int dc : dCols) {
                int fDestino = f + (df * 2);
                int cDestino = c + (dc * 2);
                int fInter = f + df;
                int cInter = c + dc;

                if (fDestino >= 0 && fDestino < 8 && cDestino >= 0 && cDestino < 8) {
                    if (tablero[fDestino][cDestino] == 0 && esEnemiga(tablero[fInter][cInter])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean hayCapturasDisponibles() {
        for (int f = 0; f < TAMANO_TABLERO; f++) {
            for (int c = 0; c < TAMANO_TABLERO; c++) {
                if (puedeComerFicha(f, c)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int contarOpcionesCaptura(int f, int c) {
        int val = tablero[f][c];
        int opciones = 0;

        int[] dFilas = obtenerDireccionesFila(val);
        int[] dCols = {-1, 1};

        for (int df : dFilas) {
            for (int dc : dCols) {
                int fDestino = f + (df * 2);
                int cDestino = c + (dc * 2);
                int fInter = f + df;
                int cInter = c + dc;

                if (fDestino >= 0 && fDestino < 8 && cDestino >= 0 && cDestino < 8) {
                    if (tablero[fDestino][cDestino] == 0 && esEnemiga(tablero[fInter][cInter])) {
                        opciones++;
                    }
                }
            }
        }
        return opciones;
    }

    private static boolean intentarMover(int idFicha, Scanner sc) {
        int[] pos = buscarFicha(idFicha);
        if (pos == null) {
            System.out.println("\n[!] La ficha seleccionada no existe o no esta en el tablero.");
            return false;
        }

        int fOrigen = pos[0];
        int cOrigen = pos[1];
        int valPieza = tablero[fOrigen][cOrigen];
        boolean hayCapturasEnTablero = hayCapturasDisponibles();
        boolean estaFichaPuedeComer = puedeComerFicha(fOrigen, cOrigen);

        if (hayCapturasEnTablero && !estaFichaPuedeComer) {
            System.out.println("\n[!] Movimiento invalido: Tienes una captura obligatoria con OTRA ficha en el tablero.");
            return false;
        }

        String nomJugador = (turnoActual == 1) ? "Rojas" : "Blancas";

        // --- CASO 1: LA FICHA TIENE CAPTURA OBLIGATORIA ---
        if (estaFichaPuedeComer) {
            int cantidadOpciones = contarOpcionesCaptura(fOrigen, cOrigen);
            int opcionDireccion = 0;

            if (cantidadOpciones > 1) {
                System.out.println("Esta ficha tiene multiples opciones de captura:");
                System.out.println("  1. Izquierda");
                System.out.println("  2. Derecha");
                opcionDireccion = leerEntero(sc, "Selecciona direccion de salto: ");
            }

            int[] dFilas = obtenerDireccionesFila(valPieza);
            for (int deltaFila : dFilas) {
                int[] dCols = (opcionDireccion == 1) ? new int[]{-1} : (opcionDireccion == 2) ? new int[]{1} : new int[]{-1, 1};

                for (int deltaCol : dCols) {
                    int fDestinoSalto = fOrigen + (deltaFila * 2);
                    int cDestinoSalto = cOrigen + (deltaCol * 2);
                    int fInter = fOrigen + deltaFila;
                    int cInter = cOrigen + deltaCol;

                    if (fDestinoSalto >= 0 && fDestinoSalto < 8 && cDestinoSalto >= 0 && cDestinoSalto < 8) {
                        if (tablero[fDestinoSalto][cDestinoSalto] == 0 && esEnemiga(tablero[fInter][cInter])) {
                            tablero[fDestinoSalto][cDestinoSalto] = valPieza;
                            tablero[fOrigen][cOrigen] = 0;
                            tablero[fInter][cInter] = 0;
                            verificarCoronacion(fDestinoSalto, cDestinoSalto);
                            
                            // Actualizar conteo de capturas
                            if (turnoActual == 1) comidasPorRojas++;
                            else comidasPorBlancas++;

                            // Registrar movimiento en el historial
                            historialMovimientos.add("Jugador " + nomJugador + " [CAPTURA]: Ficha " + idFicha + " comio en (" + fDestinoSalto + "," + cDestinoSalto + ")");

                            System.out.println("\n[!] ¡Ficha capturada automaticamente!");

                            if (puedeComerFicha(fDestinoSalto, cDestinoSalto)) {
                                imprimirTablero();
                                System.out.println("[!] ¡CAPTURA MULTIPLE! La ficha " + idFicha + " continua comiendo...");
                                intentarMover(idFicha, sc);
                            }
                            return true;
                        }
                    }
                }
            }
        }

        // --- CASO 2: MOVIMIENTO SIMPLE ---
        System.out.println("¿Hacia donde mover?");
        System.out.println("  1. Izquierda ");
        System.out.println("  2. Derecha");
        int opcionDireccion = leerEntero(sc, "Selecciona direccion: ");

        if (opcionDireccion != 1 && opcionDireccion != 2) {
            System.out.println("\nDireccion no valida.");
            return false;
        }

        int deltaCol = (opcionDireccion == 1) ? -1 : 1;
        int[] dFilas = obtenerDireccionesFila(valPieza);

        for (int deltaFila : dFilas) {
            int fDestinoSimple = fOrigen + deltaFila;
            int cDestinoSimple = cOrigen + deltaCol;

            if (fDestinoSimple >= 0 && fDestinoSimple < 8 && cDestinoSimple >= 0 && cDestinoSimple < 8) {
                if (tablero[fDestinoSimple][cDestinoSimple] == 0) {
                    tablero[fDestinoSimple][cDestinoSimple] = valPieza;
                    tablero[fOrigen][cOrigen] = 0;
                    verificarCoronacion(fDestinoSimple, cDestinoSimple);

                    // Registrar movimiento simple en el historial
                    String dirTxt = (opcionDireccion == 1) ? "Izquierda" : "Derecha";
                    historialMovimientos.add("Jugador " + nomJugador + " [MOVIMIENTO]: Ficha " + idFicha + " hacia la " + dirTxt);

                    return true;
                }
            }
        }

        System.out.println("\n[!] Movimiento no permitido en esa direccion.");
        return false;
    }

    private static int leerEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (sc.hasNextInt()) {
                return sc.nextInt();
            } else {
                System.out.println("Debes ingresar solo numeros.");
                sc.next();
            }
        }
    }

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        int op;

        do {
            System.out.println("");
            System.out.println("============================");
            System.out.println("       DAMAS INGLESAS.");
            System.out.println("============================");
            System.out.println("");
            System.out.println("   1. Reglas del juego.");
            System.out.println("   2. Iniciar la partida.");
            System.out.println("   3. Opciones de Guardado.");
            System.out.println("   4. Salir del juego.");
            System.out.println("");
            System.out.println("============================");
            System.out.println("");

            op = leerEntero(entrada, "Selecciona una opcion: ");
            switch (op) {
                case 1:
                    limpiar();
                    reglamento();
                    break;
                case 2:
                    File archivoExiste = new File("partida_damas.txt");
                    boolean reanudar = false;

                    if (archivoExiste.exists()) {
                        System.out.println("\n[!] Se ha detectado una partida guardada previamente.");
                        System.out.println("   1. Reanudar partida guardada");
                        System.out.println("   2. Iniciar una nueva partida");
                        int eleccion = leerEntero(entrada, "Selecciona una opcion (1 o 2): ");
                        if (eleccion == 1) {
                            if (cargarPartida()) {
                                System.out.println("\n[!] Partida reanudada con exito.");
                                reanudar = true;
                            } else {
                                System.out.println("\n[!] No se pudo cargar la partida guardada. Se iniciara una nueva.");
                            }
                        }
                    } else {
                        System.out.println("\n[!] No existe ninguna partida guardada actualmente. Se creara una nueva partida.");
                    }

                    if (!reanudar) {
                        turnoActual = 2; // Garantiza que las Blancas inicien siempre
                        inicializarPiezas();
                    }

                    boolean enPartida = true;
                    while (enPartida) {
                        imprimirTablero();
                        String jugador = (turnoActual == 1) ? "Rojas (1 - 12)" : "Blancas (1 - 12)";
                        System.out.println("Turno de las " + jugador);
                        System.out.println("(Opciones avanzadas: Ingresa '0' para saltar turno, '-1' para salir al menu)");

                        if (hayCapturasDisponibles()) {
                            System.out.println(ROJO + "[!] ATENCION: ¡Existe una captura obligatoria en el tablero!" + RESET);
                        }

                        int idFicha = leerEntero(entrada, "Numero de ficha a mover (1-12): ");

                        // Opción para terminar la partida
                        if (idFicha == -1) {
                            limpiar();
                            System.out.println("\n[!] Partida terminada por el usuario.");
                            enPartida = false;
                            break;
                        }

                        // Opción para saltar turno
                        if (idFicha == 0) {
                            if (hayCapturasDisponibles()) {
                                System.out.println("\n[!] No puedes saltar turno cuando tienes capturas obligatorias disponibles.");
                            } else {
                                System.out.println("\n[!] Has saltado tu turno.");
                                String nomJugador = (turnoActual == 1) ? "Rojas" : "Blancas";
                                historialMovimientos.add("Jugador " + nomJugador + " [SALTO DE TURNO]");
                                turnoActual = (turnoActual == 1) ? 2 : 1;
                            }
                            continue;
                        }

                        if (intentarMover(idFicha, entrada)) {
                            turnoActual = (turnoActual == 1) ? 2 : 1;
                        } else {
                            System.out.println("Intenta de nuevo.\n");
                        }
                    }
                    break;
                case 3:
                    limpiar();
                    System.out.println("======================================");
                    System.out.println("        OPCIONES DE GUARDADO");
                    System.out.println("======================================");
                    System.out.println("  1. Guardar partida actual");
                    System.out.println("  2. Eliminar la ultima partida guardada");
                    System.out.println("======================================");
                    int opGuardado = leerEntero(entrada, "Selecciona una opcion (1 o 2): ");

                    if (opGuardado == 1) {
                        guardarPartida();
                    } else if (opGuardado == 2) {
                        System.out.println("\n¿Esta seguro de que desea eliminar la ultima partida guardada?");
                        System.out.println("  1. Si, eliminar");
                        System.out.println("  2. No, cancelar");
                        int confirmar = leerEntero(entrada, "Confirmacion (1 o 2): ");
                        if (confirmar == 1) {
                            eliminarPartidaGuardada();
                        } else {
                            System.out.println("\n[!] Operacion cancelada.");
                        }
                    } else {
                        System.out.println("\n[!] Opcion no valida.");
                    }
                    break;
                case 4:
                    limpiar();
                    System.out.println("");
                    System.out.println("=======================");
                    System.out.println(" Gracias por jugar :D.");
                    System.out.println("=======================");
                    System.out.println("");
                    System.exit(0);
                    break;
                default:
                    limpiar();
                    System.out.println("Operacion no valida.");
            }
        } while (op != 4);
    }
}