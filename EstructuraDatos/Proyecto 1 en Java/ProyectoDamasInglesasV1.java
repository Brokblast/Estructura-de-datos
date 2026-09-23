import java.util.Scanner;
public class Main
{
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
                		reglamento();
            			break;
                	case 2:
                		System.out.println("Martes");
                		break;
                	case 3: 
                	    System.out.println("=======================");
                	    System.out.println(" Gracias por jugar :D.");
                	    System.out.println("=======================");
                	    System.exit(0);
                	    break;
            		default:
            			System.out.println("Operacion no valida.");
            	}
            } while (op != 3);
		}
	}