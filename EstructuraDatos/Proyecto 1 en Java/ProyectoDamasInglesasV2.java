import java.util.Scanner;
public class Main
{
    public static void limpiar() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private static final int TAMANO_TABLERO = 8;
    // 0 = vacío, 1 = Roja (R), 2 = Blanca (B)
    private static int[][] tablero = new int[TAMANO_TABLERO][TAMANO_TABLERO];
    private static int turnoActual = 1; // 1: Rojas (R), 2: Blancas (B)

    public static void reglamento(){
        System.out.println("");
        System.out.println("==========================================================================");
        System.out.println("                          MOVIMIENTOS BASICOS.");
        System.out.println("  -Peones: Se mueven una sola casilla a la vez en diagonal hacia ");
        System.out.println("  adelante, siempre a un espacio vacío. No pueden retroceder");
        System.out.println("  -Captura (comer): Se salta por encima de la ficha contraria adyacente");
        System.out.println("  hacia la casilla vacía que está detrás de ella. La ficha capturada");
        System.out.println("  se retira del tablero.");
        System.out.println("  -Capturas múltiples: Si tras un salto existe la opción de realizar");
        System.out.println("  otro salto con la misma pieza, se continúa comiendo en el mismo turno.");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("                        CORONACION Y DAMAS(reyes).");
        System.out.println("  -Coronar: Cuando un peón llega a la última fila del lado opuesto del");
        System.out.println("  tablero, se convierte en dama (o rey), generalmente colocando otra");
        System.out.println("  -Movimiento de la dama: En las damas inglesas, la dama se mueve también");
        System.out.println("  una sola casilla en diagonal, pero puede avanzar o retroceder (a");
        System.out.println("  diferencia del peón normal). (Nota: Algunas variantes permiten despla-");
        System.out.println("  zamiento largo, pero la regla estricta inglesa limita el rey a un paso");
        System.out.println("  en cualquier dirección diagonal).");
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
                if ((f + c) % 2 != 0) {
                    if (f < 3) tablero[f][c] = 2;      // Blancas arriba
                    else if (f > 4) tablero[f][c] = 1; // Rojas abajo
                }
            }
        }
    }

    private static void imprimirTablero() {
        limpiar();
        System.out.println("\n  0 1 2 3 4 5 6 7 (Cols)");
        for (int f = 0; f < TAMANO_TABLERO; f++) {
            System.out.print(f + " ");
            for (int c = 0; c < TAMANO_TABLERO; c++) {
                if (tablero[f][c] == 1) {
                    System.out.print("R ");
                } else if (tablero[f][c] == 2) {
                    System.out.print("B ");
                } else if ((f + c) % 2 != 0) {
                    System.out.print(". "); // Casilla jugable vacia
                } else {
                    System.out.print("  "); // Casilla no jugable
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static boolean realizarMovimiento(int fOrigen, int cOrigen, int fDestino, int cDestino) {
        // Validar rangos del tablero
        if (fOrigen < 0 || fOrigen >= 8 || cOrigen < 0 || cOrigen >= 8 ||
            fDestino < 0 || fDestino >= 8 || cDestino < 0 || cDestino >= 8) {
            return false;
        }

        // Debe haber una ficha propia en el origen y el destino debe estar vacio
        if (tablero[fOrigen][cOrigen] != turnoActual || tablero[fDestino][cDestino] != 0) {
            return false;
        }

        int direccion = (turnoActual == 1) ? -1 : 1; // Rojas suben (-1), Blancas bajan (+1)
        int distFila = fDestino - fOrigen;
        int distCol = Math.abs(cDestino - cOrigen);

        // Movimiento simple diagonal de 1 casilla
        if (distFila == direccion && distCol == 1) {
            tablero[fDestino][cDestino] = tablero[fOrigen][cOrigen];
            tablero[fOrigen][cOrigen] = 0;
            return true;
        }

        return false;
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
            	System.out.println("   3. Salir del juego.");
            	System.out.println("");
            	System.out.println("============================");
            	System.out.println("");
            	
                op = entrada.nextInt();
            	switch (op) {
            	    
            		case 1:
            		    limpiar();
                		reglamento();
            			break;
                	case 2:
                	    limpiar();
                		inicializarPiezas();
                		while (true) {
                            imprimirTablero();
                            String jugador = (turnoActual == 1) ? "Rojas (R)" : "Blancas (B)";
                            System.out.println("Turno de las " + jugador);
                            System.out.print("Fila y Columna de la ficha a mover (ej. 5 2): ");
                            int fOrigen = entrada.nextInt();
                            int cOrigen = entrada.nextInt();
                
                            System.out.print("Fila y Columna destino (ej. 4 3): ");
                            int fDestino = entrada.nextInt();
                            int cDestino = entrada.nextInt();
                
                            if (realizarMovimiento(fOrigen, cOrigen, fDestino, cDestino)) {
                                turnoActual = (turnoActual == 1) ? 2 : 1;
                            } else {
                                System.out.println("\n[!] Movimiento invalido. Intenta de nuevo.\n");
                            }
                        }
                	case 3: 
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
            } while (op != 3);
		}
	}