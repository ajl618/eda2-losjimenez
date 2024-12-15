package org.eda2.practica01;

import java.util.Arrays;
import java.util.Scanner;

/**
 * DivideAndConquer
 * 
 * Clase generada para la realizacion de la practica 01 donde se utilizaran
 * algoritmos de Divide y venceras para la realizacion
 * de la practica.
 * 
 * @author Adrian Jimenez Benitez
 * @author Antonio Jose Jimenez Luque
 * 
 */
public class DivideAndConquer {


    // Constantes para calcular el tamaño de la matriz en memoria
    private static final int INT_SIZE_BYTES = 4; // Tamaño en bytes de un entero en Java
    private static final int OVERHEAD_SIZE_BYTES = 32; // Estimación del overhead de memoria de la matriz

    private int[][] tabla;
    /**
     * Constructor vacío de la clase DivideAndConquer
     */
    public DivideAndConquer() {
    }

    /**
     * Constructor de la clase DivideAndConquer
     * 
     * Utilizado para generar la tabla para los enfrentamientos de los jugadores
     * 
     * @param numJugadores numero de jugadores
     */
    public DivideAndConquer(int numJugadores, int caso) {

        /*
         * Creamos la matriz con n jugadores y n-1 dias de enfrentamientos en el caso 1
         */

        if(caso == 1) this.tabla = new int[numJugadores][numJugadores - 1];

        /*
         * Creamos la matriz con n jugadores y n-1 dias de enfrentamientos en el caso 2 en el caso par
         * y n dias en el caso impar
         */

        if(caso == 2) 
        {
            if(numJugadores%2 == 1){
               this.tabla = new int[numJugadores][numJugadores];
            }else{
               this.tabla = new int[numJugadores][numJugadores - 1];
            }
        }

        /*
         * Creamos la matriz con n-1 días y n jugadores de enfrentamientos en el caso 1 parte 2
         */

        if(caso == 3) this.tabla = new int[numJugadores - 1][numJugadores];

    }

    /**
     * DivideAndConquerCaso1
     * 
     * Simplificacion
     * 
     * n es potencia de 2
     * 
     * @param n tamano de la matriz
     */
    public void DivideAndConquer1(int n) {

        /*
         * Caso base: solo hay dos jugadores, se deben de enfrentar entre ellos el
         * primer jugadores
         */
        if (n == 2) {
            /*
             * El primer jugador se enfrenta al segundo
             */
            this.tabla[0][0] = 2;
            this.tabla[1][0] = 1;
            return;
        }

        DivideAndConquer1(n / 2);

        /*
         * Solucion para los distintos cuadrantes
         */
        CuadranteInferiorIzquierdo(n);
        CuadranteSuperiorDerecho(n);
        CuadranteInferiorDerecho(n);

    }

    /**
     * CuadranteInferiorIzquierdo
     * 
     * 
     * @param n tamano de la matriz
     */
    public void CuadranteInferiorIzquierdo(int n) {

        int mitad = (n / 2);

        for (int jugadores = mitad; jugadores <= n - 1; jugadores++) {
            for (int dias = 0; dias < mitad - 1; dias++) {
                this.tabla[jugadores][dias] = this.tabla[jugadores - n / 2][dias] + n / 2;
            }
        }
    }

    /**
     * CuadranteSuperiorDerecho
     * 
     * @param n tamano de la matriz
     */
    public void CuadranteSuperiorDerecho(int n) {

        int mitad = (n / 2);

        for (int jugadores = 0; jugadores < mitad; jugadores++) {
            for (int dia = mitad - 1; dia < n - 1; dia++) {
                if ((jugadores + 1) + (dia + 1) > n) {
                    this.tabla[jugadores][dia] = ((jugadores + 1) + (dia + 1)) % n + mitad;
                }else{
                    this.tabla[jugadores][dia] = (jugadores + 1) + (dia + 1);
                }
            }
        }
    }

    /**
     * CuadranteInferiorDerecho
     * 
     * @param n tamano de la matriz
     */
    public void CuadranteInferiorDerecho(int n) {

        int mitad = n / 2;

        for (int jugadores = mitad; jugadores < n; jugadores++) {
            for (int dia = mitad - 1; dia < n - 1; dia++) {
                int oponente = (mitad - 1 + jugadores - dia) % mitad + mitad + 1;
                this.tabla[jugadores][dia] = oponente - mitad;

            }
        }
    }

    /**
     * 
     * DivideAndConquerCaso2ParImpar
     *
     * Algoritmo DyV para el caso 2 (n no es potencia de dos y puede ser par o impar)
     * Cada participante jugara un partido por dia durante n-1 dias en el caso par y en el caso impar jugará un partido en n días
     * descansando uno de los días.
     * 
     * Matriz bidimensional donde:
     *  - Filas: n jugadores
     *  - Columnas: n-1 (Caso par) o n (Caso impar) dias
     * 
     * @param n Numero de jugadores
     */
    public void DivideAndConquerCaso2(int n) {

     	/*
         * Caso base: solo hay dos jugadores, se deben de enfrentar entre ellos
         */
     	
        if (n == 2) {
            this.tabla[0][0] = 2;
            this.tabla[1][0] = 1;
        }else if (n % 2 != 0) { // n impar

        DivideAndConquerCaso2(n + 1); // llamada recursiva

        /*
         * Eliminamos el jugador n+1
         */
                 
        for (int jug = 0; jug < n; jug++) {
            for (int dia = 0; dia < n; dia++) { 
                if (this.tabla[jug][dia] == n+1) {
                    this.tabla[jug][dia] = 0;
                }
            }
        }

        }else{ // n par

            int m = n/2;

            DivideAndConquerCaso2(m);
                 
            if(m % 2 == 0){ //Caso PAR
                	
                /*
                 * Cuadrante inferior izquierdo
                 */

                for(int jugador = m; jugador < n; jugador++){
                   	if(jugador == this.tabla.length) continue;
                    for(int dia = 0; dia < m-1; dia++){
                        this.tabla[jugador][dia] = this.tabla[jugador - m][dia] + m;
                    }
                }

                /*
                 * Cuadrante superior derecho
                 */
                     
                for(int jugador = 0; jugador < m; jugador++){
                    for(int dia = m-1; dia < n-1; dia++){
                      	if ((jugador + 1) + (dia + 1) > n) {
                            this.tabla[jugador][dia] = ((jugador + 1) + (dia + 1)) % n + m;
                        } else {
                            this.tabla[jugador][dia] = (jugador + 1) + (dia + 1);
                        }
                    }
                }

                /*
                 * Cuadrante inferior derecho
                 */
                     
                for(int jugador = m; jugador < n; jugador++){
                   	if(jugador == this.tabla.length) continue;
                    for(int dia = m-1; dia < n-1; dia++){
                        if(jugador > dia){
                            this.tabla[jugador][dia] = jugador - dia;
                        }else{
                            this.tabla[jugador][dia] = (jugador + m) - dia;
                        }
                    }
                }
            }else{ //Caso IMPAR

             	/*
                 * Cuadrante inferior izquierdo
                 */
             	
                for(int jugador = m; jugador < n; jugador++){
                 	if(jugador == this.tabla.length) continue;
                    for(int dia = 0; dia < m; dia++){
                        if(this.tabla[jugador - m][dia] == 0)
                        {
                            this.tabla[jugador][dia] = 0;
                        }else{
                            this.tabla[jugador][dia] = this.tabla[jugador - m][dia] + m;
                        }
                    }
                }
                 
                /*
                 * Rellenar ceros de los cuadrantes izquierdos
                 */ 
                 
                for(int jugador = 0 ; jugador < m; jugador++){
                    for(int dia = 0; dia < m; dia++){
                    	if(jugador+m == this.tabla.length) continue;
                        if(this.tabla[jugador][dia] == 0){
                            this.tabla[jugador][dia] = jugador + m + 1;
                            this.tabla[jugador + m][dia] = jugador + 1;
                        }
                    }
                }

                /*
                 * Cuadrante superior derecho
                 */
                 
                for(int jugador = 0 ; jugador < m; jugador++){
                    for(int dia = m; dia < n-1; dia++){
                     	if ((jugador + 1) + (dia + 1) > n){
                            this.tabla[jugador][dia] = ((jugador + 1) + (dia + 1)) % n + m;
                        }else{
                            this.tabla[jugador][dia] = (jugador + 1) + (dia + 1);
                        }
                    }
                }

                /*
                 * Cuadrante inferior derecho
                 */
                 
                for(int jugador = m; jugador < n; jugador++){
                 	if(jugador == this.tabla.length) continue; 	
                     for(int dia = m; dia < n-1; dia++){
                        if(jugador > dia){
                            this.tabla[jugador][dia] = jugador - dia;
                        }else{
                            this.tabla[jugador][dia] = (jugador + m) - dia;
                        }
                    }
                }
            }
        }
    }

    /*
     * 
     * 
     * Caso 1 parte 2
     * 
     * 
     */


    /**
     * DividAndConquerCaso1b
     * 
     * Algoritmo DyV para el caso 1 (n es potencia de dos) y cada participante jugara un partido por dia durante n-1 dias
     * Primero dividimos a los jugadores en dos grupos iguales y que juegan dentr ode los grupos durante los primeros n/2-1 dias.
     * Luego, se programan los partidos entre los grupos para los otros n/2 dias.
     * 
     * Matriz bidimensional donde:
     *  - Filas: n - 1 dias
     *  - Columnas: n jugadores
     * 
     * n es potencia de 2
     * 
     * @param A matriz de enfrentamientos
     * @param jugadorInicial jugador inicial
     * @param jugadorFinal jugador final
     */

    public void DivideAndConquer1b(int jugadorInicial, int jugadorFinal) {

        /*
         * Caso base: solo hay dos jugadores, se deben de enfrentar entre ellos el
         * primer jugadores
         */

        if (jugadorFinal == jugadorInicial + 1) {

            /*
             * El primer jugador se enfrenta al segundo
             */

            this.tabla[0][jugadorInicial - 1] = jugadorFinal;
            this.tabla[0][jugadorFinal - 1] = jugadorInicial;

            return;
        }

        /*
         * k seria la mitad de los jugadores en cada llamada
         */

        int k = jugadorInicial + ((jugadorFinal - jugadorInicial + 1) / 2);
        int dias = jugadorFinal - jugadorInicial;
        int mitadDias = dias / 2;

        DivideAndConquer1b(jugadorInicial, k - 1); // Primera mitad
        DivideAndConquer1b(k, jugadorFinal); // Segunda mitad

        /*
         * Combinar los enfrentamientos de los jugadores
         */

        Combinar(jugadorInicial, jugadorFinal, mitadDias, dias);
    }

    /**
     * Combinar
     * 
     * Combinar los enfrentamientos de los jugadores
     * Esta funcion combina los enfrentamientos de los jugadores en la primera mitad
     * 
     * @param jugadorInicial
     * @param jugadorFinal
     * @param mitadDias
     * @param totalDias
     */

    public void Combinar(int jugadorInicial, int jugadorFinal, int mitadDias, int totalDias) {

        int numJugadores = jugadorFinal - jugadorInicial + 1;
        int medio = (jugadorInicial + jugadorFinal) / 2;
    
        // Cada día de la segunda mitad de los días del torneo
        for (int dia = 0; dia <= mitadDias; dia++) {
            // Cada jugador de la primera mitad
            for (int i = jugadorInicial - 1; i < medio; i++) {
                // Encuentra el índice del jugador en la segunda mitad para este día
                int jugador1 = i;
                int jugador2 = medio + (i + dia) % (numJugadores / 2);
    
                // Asigna los partidos para el día 'dia' más la mitad de los días del torneo
                this.tabla[dia + mitadDias][jugador1] = jugador2 + 1; // +1 para ajustar el índice base-0 a base-1
                this.tabla[dia + mitadDias][jugador2] = jugador1 + 1;
            }
        }
    }
    
    /**
     * imprimirTablaCaso1Recursivo
     * 
     * Imprime la tabla de enfrentamientos para el caso 1 parte 2,
     * donde n es potencia de 2 y cada participante jugara un partido por dia durante n-1 dias
     * 
     * @param tabla
     */

    public void imprimirTablaCaso1Recursivo(int[][] tabla) {
        System.out.println("             jugadores jugadores");
        for (int i = 0; i < tabla.length; i++) {
            System.out.print("dias: " + i + "|         ");
            for (int j = 0; j < tabla[i].length; j++) {
                System.out.print(tabla[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * getTabla
     * 
     * Devuelve la tabla de enfrentamientos
     * 
     * @return int[][]
     */

    public int[][] getTabla() {
        return tabla;
    }

    /**
     * esPotenciaDeDos
     * 
     * Verifica si un numero es potencia de dos
     * 
     * @param n
     * @return boolean
     */

    public static boolean esPotenciaDeDos(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    /**
     * calcularMedia
     * 
     * Calcula la media de un array de tiempos
     * 
     * @param tiempo
     * @return double media
     */
	public static double calcularMedia(long[] tiempo) {

		double suma = 0.0;
		
		for (int i = 0; i < tiempo.length-1; i++) {
			suma += tiempo[i];
		}
		
		double media = suma / (double)(tiempo.length-1);
		
		return media;
	}



    /**
     * imprimirMatrizEscalonada
     * 
     * Imprime una matriz de forma escalonada
     * 
     * @param matriz
     */
    public static void imprimirMatrizEscalonada(int[][] matriz) {
        // Encuentramos el número máximo en la matriz para determinar el espacio de impresión
        int maxNumero = encontrarMaximo(matriz);
        // Calculamos cuántos dígitos tiene el número máximo
        int maxDigitos = Integer.toString(maxNumero).length();

        // Imprime la línea superior de la tabla
        imprimirLinea(matriz[0].length, maxDigitos);

        for (int i = 0; i < matriz.length; i++) {
            System.out.print("|");
            for (int j = 0; j < matriz[i].length; j++) {
                // Ajusta el formato según el número máximo de dígitos y agrega bordes verticales
                System.out.printf(" %" + maxDigitos + "d |", matriz[i][j]);
            }
            System.out.println();
            // Imprime la línea divisoria entre filas
            imprimirLinea(matriz[i].length, maxDigitos);
        }
    }


    /**
     * encontrarMaximo
     * 
     * Encuentra el número máximo en una matriz para poder establecer el ancho de impresión
     * @param matriz
     * @return
     */
    public static int encontrarMaximo(int[][] matriz) {
        int maximo = matriz[0][0];
        for (int[] fila : matriz) {
            for (int numero : fila) {
                if (numero > maximo) {
                    maximo = numero;
                }
            }
        }
        return maximo;
    }

    /**
     * imprimirLinea
     * 
     * Imprime una línea divisoria entre filas
     * 
     * @param columnas
     * @param maxDigitos
     */
    public static void imprimirLinea(int columnas, int maxDigitos) {
        for (int i = 0; i < columnas; i++) {
            System.out.print("+");
            for (int j = 0; j < maxDigitos + 2; j++) { // +2 por los espacios extra alrededor del número
                System.out.print("-");
            }
        }
        System.out.println("+");
    }

    /**
     * Calcula el tamaño aproximado que ocupa la matriz en la memoria.
     * 
     * Este método estima el tamaño de la matriz multiplicando el número total de
     * elementos
     * (filas x columnas) por el tamaño ocupado por un entero (`int`) en Java, que
     * es de 4 bytes.
     * 
     * Además, se añade un valor de *overhead* para tener en cuenta el espacio
     * adicional
     * que la Java Virtual Machine (JVM) utiliza para gestionar la matriz. Este
     * espacio adicional
     * es necesario para almacenar metadatos sobre la matriz, como su tamaño, y para
     * otros
     * mecanismos internos de la JVM. Aunque este valor puede variar según la
     * implementación
     * específica de la JVM y la versión de Java, aquí utilizamos una estimación
     * fija de 32 bytes
     * para el cálculo.
     * 
     * La estimación fija de 32 bytes para el overhead se basa en la observación
     * general de que
     * la JVM necesita una cantidad mínima de memoria adicional para almacenar
     * información estructural
     * de la matriz, como la longitud de la matriz, referencia al objeto, y
     * posiblemente algún mecanismo
     * de alineación de memoria. Aunque este valor puede variar según la
     * implementación
     * específica de la JVM, la versión de Java, y la arquitectura de la máquina,
     * 32 bytes es un valor comúnmente aceptado como una aproximación razonable para
     * una amplia
     * gama de casos. Se debe tener en cuenta que para matrices más complejas o en
     * situaciones
     * específicas, el overhead real puede ser mayor.
     *
     * @param filas    Número de filas de la matriz, que representa una dimensión de
     *                 la matriz.
     * @param columnas Número de columnas de la matriz, que representa la otra
     *                 dimensión de la matriz.
     * @return El tamaño en bytes que se estima ocupa la matriz en memoria,
     *         incluyendo el overhead estimado.
     */
    private long calcularTamanoMatriz(int filas, int columnas) {
        // Multiplica el número total de elementos por el tamaño de un int, y añade el
        // overhead estimado
        return (long) filas * (long) columnas * INT_SIZE_BYTES + OVERHEAD_SIZE_BYTES;
    }

    /**
     * Main
     * 
     * Utilizado para crear el menu interactivo y realizar el estudio experimental.
     * 
     * @param args
     */

    public static void main(String[] args) {

        /**
         * Montaje de la shell interactiva
         * 
         * Donde:
         * - Se pueda ejecutar los distintos casos con opciones y parametros
         * - Se ejecuten los test y muestre los resultados en formato tabla
         * - Agregar la opcion para los desarrolladores hacer el estudio experimental
         * - 3 o 4 ejecuciones por n y obtener la mejugadores, si es muy dispar,
         * lanzarlo de nuevo
         * - Obtener los datos de memoria y tiempo
         */

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;
        int n = 0;

        // Tiempos
        long tiempoInicial, tiempoFinal;
        long[] tiempos1 = new long[10];
        double tiempoMedio = 0.0;


        // Tamaño
        //  long memoriaInicial = Runtime.getRuntime().freeMemory();
        double memoriaTotalUsada = 0.0;


        System.out.println("==============================================================");
        System.out.println(" ███████╗██████╗  █████╗");
        System.out.println(" ██╔════╝██╔══██╗██╔══██╗");
        System.out.println(" █████╗░░██║░░██║███████║");
        System.out.println(" ██╔══╝░░██║░░██║██╔══██║");
        System.out.println(" ███████╗██████╔╝██║░░██║");
        System.out.println(" ╚══════╝╚═════╝░╚═╝░░╚═╝");
        System.out.println("==============================================================");
        System.out.println("              BIENVENIDOS A LA PRÁCTICA 01 DE LOSJIMENEZ      ");
        System.out.println("==============================================================");

        while (opcion != 9) {
            System.out.println();
            System.out.println("Selecciona una opción:");
            System.out.println("1. Ejecutar el caso 1");
            System.out.println("2. Ejecutar el caso 2 Par");
            System.out.println("3. Ejecutar el caso 2 Impar");
            System.out.println("4. Ejecutar el caso 1 con 2 llamadas Recursivas");
            System.out.println("5. Estudio Experimental: caso 1");
            System.out.println("6. Estudio Experimental: caso 2 Par");
            System.out.println("7. Estudio Experimental: caso 2 Impar");
            System.out.println("8. Estudio Experimental: caso 1 2 llamadas Recursivas");

            System.out.println("9. Salir");
            System.out.print("Opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:

                    System.out.println("Caso 01 - n es potencia de 2");

                    do {
                        System.out.print("Por favor, introduce un número que sea potencia de 2: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Eso no parece ser un número válido. Intenta nuevamente.");
                            scanner.next();
                        }
                        n = scanner.nextInt();
                    } while (!esPotenciaDeDos(n));

                    DivideAndConquer dyv_1 = new DivideAndConquer(n, 1);
                    dyv_1.DivideAndConquer1(n);
                    imprimirMatrizEscalonada(dyv_1.getTabla());

                    break;
                case 2:

                    System.out.println("Caso 02 - Par");

                    do {
                        System.out.print("Por favor, introduce un número que sea par: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Eso no parece ser un número válido. Intenta nuevamente.");
                            scanner.next();
                        }
                        n = scanner.nextInt();
                    } while (n % 2 != 0);
                    
                    DivideAndConquer dyv_2_even = new DivideAndConquer(n, 2);
                    // Cambiar cuando se agregue el caso 2
                    dyv_2_even.DivideAndConquerCaso2(n);
                    imprimirMatrizEscalonada(dyv_2_even.getTabla());

                    break;
                case 3:


                    System.out.println("Caso 02 - Impar");

                    do {
                        System.out.print("Por favor, introduce un número que sea impar: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Eso no parece ser un número válido. Intenta nuevamente.");
                            scanner.next();
                        }

                        n = scanner.nextInt();
                    } while (n % 2 == 0);

                    DivideAndConquer dyv_2_odd = new DivideAndConquer(n, 2);
                    // Cambiar cuando se agregue el caso 2
                    dyv_2_odd.DivideAndConquerCaso2(n);
                    imprimirMatrizEscalonada(dyv_2_odd.getTabla());

                    break;
                case 4:

                    System.out.println("Caso 01 - Sin simplificación");

                    do {
                        System.out.print("Por favor, introduce un número que sea potencia de 2: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Eso no parece ser un número válido. Intenta nuevamente.");
                            scanner.next();
                        }
                        n = scanner.nextInt();
                    } while (!esPotenciaDeDos(n));
                    
                    DivideAndConquer dyv_3 = new DivideAndConquer(n, 3);
                    dyv_3.DivideAndConquer1b(1, n);
                    imprimirMatrizEscalonada(dyv_3.getTabla());
                    break;
                case 5:

                    System.out.println("Estudio Experimental Caso 1");

                    do {
                        System.out.print("Por favor, introduce un número que sea potencia de 2: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Eso no parece ser un número válido. Intenta nuevamente.");
                            scanner.next();
                        }
                        n = scanner.nextInt();
                    } while (!esPotenciaDeDos(n));

                    DivideAndConquer dyv = new DivideAndConquer(n, 1);

                    /*
                     * De esa manera suavizamos la curva
                     */
                    for (int i = 0; i < 10; i++) {
                        System.gc(); // Sugerencia para solicitar la ejecución del Garbage Collector

                        tiempoInicial = System.currentTimeMillis();
                        dyv.DivideAndConquer1(n);
                        tiempoFinal = System.currentTimeMillis();

                        tiempos1[i] = tiempoFinal - tiempoInicial;
                    }

                    // Calcula la media de tiempo y memoria
                    tiempoMedio = calcularMedia(tiempos1);
                    memoriaTotalUsada = dyv.calcularTamanoMatriz(n - 1, n);

                    // Imprime los resultados
                    System.out.println("\nValores asociados para n = " + n);
                    System.out.println("\nTiempo medio de ejecución: " + tiempoMedio + " milisegundos.");
                    System.out.println("Memoria media: " + memoriaTotalUsada + " bytes (" + (memoriaTotalUsada / 1024.0 / 1024.0) + " MB).");

                    break;
                case 6:

                    System.out.println("Estudio Experimental Caso 2 - Par");

                    //Solo permite introducir un número que sea par
                    do {
                        System.out.print("Por favor, introduce un número que sea par: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Eso no parece ser un número válido. Intenta nuevamente.");
                            scanner.next();
                        }
                        n = scanner.nextInt();
                    } while (n % 2 != 0);

                    DivideAndConquer dyv_caso2 = new DivideAndConquer(n, 2);

                    /*
                     * De esa manera suavizamos la curva
                     */
                    for (int i = 0; i < 10; i++) {
                        System.gc(); // Sugerencia para solicitar la ejecución del Garbage Collector

                        tiempoInicial = System.currentTimeMillis();
                        // cambiar a caso 2 cuando se haga el merge
                        dyv_caso2.DivideAndConquerCaso2(n);
                        tiempoFinal = System.currentTimeMillis();

                        tiempos1[i] = tiempoFinal - tiempoInicial;
                    }

                    // Calcula la media de tiempo y memoria
                    tiempoMedio = calcularMedia(tiempos1);
                    // Cambiar para caso 2
                    memoriaTotalUsada = dyv_caso2.calcularTamanoMatriz(n - 1, n);

                    // Imprime los resultados
                    System.out.println("\nValores asociados para n = " + n);
                    System.out.println("\nTiempo medio de ejecución: " + tiempoMedio + " milisegundos.");
                    System.out.println("Memoria media: " + memoriaTotalUsada + " bytes (" + (memoriaTotalUsada / 1024.0 / 1024.0) + " MB).");

                    break;
                
                case 7:

                    System.out.println("Estudio Experimental Caso 2 - Impar");

                    //Solo permite introducir un número que sea impar
                    do {
                        System.out.print("Por favor, introduce un número que sea impar: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Eso no parece ser un número válido. Intenta nuevamente.");
                            scanner.next();
                        }

                        n = scanner.nextInt();

                    } while (n % 2 == 0);

                    DivideAndConquer dyv_caso2_odd = new DivideAndConquer(n, 2);

                    /*
                     * De esa manera suavizamos la curva
                     */
                    for (int i = 0; i < 10; i++) {
                        System.gc(); // Sugerencia para solicitar la ejecución del Garbage Collector

                        tiempoInicial = System.currentTimeMillis();
                        // cambiar a caso 2 cuando se haga el merge
                        dyv_caso2_odd.DivideAndConquerCaso2(n);
                        tiempoFinal = System.currentTimeMillis();

                        tiempos1[i] = tiempoFinal - tiempoInicial;
                    }

                    // Calcula la media de tiempo y memoria
                    tiempoMedio = calcularMedia(tiempos1);
                    // Cambiar para caso 2
                    memoriaTotalUsada = dyv_caso2_odd.calcularTamanoMatriz(n - 1, n);

                    // Imprime los resultados
                    System.out.println("\nValores asociados para n = " + n);
                    System.out.println("\nTiempo medio de ejecución: " + tiempoMedio + " milisegundos.");
                    System.out.println("Memoria media: " + memoriaTotalUsada + " bytes (" + (memoriaTotalUsada / 1024.0 / 1024.0) + " MB).");

                    break;

                case 8:

                    System.out.println("Estudio Experimental Caso 1 parte 2");

                    //Solo permite introducir un número que sea potencia de 2
                    do {
                        System.out.print("Por favor, introduce un número que sea potencia de 2: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Eso no parece ser un número válido. Intenta nuevamente.");
                            scanner.next();
                        }
                        n = scanner.nextInt();
                    } while (!esPotenciaDeDos(n));

                    DivideAndConquer dyv_caso3 = new DivideAndConquer(n, 3);

                    /*
                     * De esa manera suavizamos la curva
                     */
                    for (int i = 0; i < 10; i++) {
                        System.gc(); // Sugerencia para solicitar la ejecución del Garbage Collector

                        tiempoInicial = System.currentTimeMillis();
                        dyv_caso3.DivideAndConquer1b(1, n);
                        tiempoFinal = System.currentTimeMillis();

                        tiempos1[i] = tiempoFinal - tiempoInicial;
                    }

                    // Calcula la media de tiempo y memoria
                    tiempoMedio = calcularMedia(tiempos1);
                    memoriaTotalUsada = dyv_caso3.calcularTamanoMatriz(n, n - 1);

                    // Imprime los resultados
                    System.out.println("\nValores asociados para n = " + n);
                    System.out.println("\nTiempo medio de ejecución: " + tiempoMedio + " milisegundos.");
                    System.out.println("Memoria media: " + memoriaTotalUsada + " bytes (" + (memoriaTotalUsada / 1024.0 / 1024.0) + " MB).");

                    break;
                case 9:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intenta de nuevo.");
            }
        }

        scanner.close();
        System.out.println("Programa finalizado.");
    }

}