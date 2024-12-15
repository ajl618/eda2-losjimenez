package org.eda2.practica03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.eda2.practica03.DynamicProgramming.Objeto;

/**
 * MainDynamicProgramming
 * 
 * Clase generada para la realizacion del menu de la practica 03. Para la
 * realizacion de este menu se han utilizado los metodos de la clase
 * DynamicProgramming.
 * 
 * @author Adrian Jimenez Benitez
 * @author Antonio Jose Jimenez Luque
 */
public class MainDynamicProgramming {

    private DynamicProgramming dp;

	private String dataset = System.getProperty("user.dir") + File.separator + "docs" + File.separator + "practica03"
			+ File.separator + "dataset" + File.separator;
	
    private String carpeta_result = System.getProperty("user.dir") + File.separator + "docs" + File.separator + "practica03"
			+ File.separator + "resultados" + File.separator;


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        MainDynamicProgramming main = new MainDynamicProgramming();
        int opcion = 0;

        System.out.println("==============================================================");
        System.out.println(" ███████╗██████╗  █████╗");
        System.out.println(" ██╔════╝██╔══██╗██╔══██╗");
        System.out.println(" █████╗░░██║░░██║███████║");
        System.out.println(" ██╔══╝░░██║░░██║██╔══██║");
        System.out.println(" ███████╗██████╔╝██║░░██║");
        System.out.println(" ╚══════╝╚═════╝░╚═╝░░╚═╝");
        System.out.println("==============================================================");
        System.out.println("              BIENVENIDOS A LA PRÁCTICA 03 DE LOSJIMENEZ      ");
        System.out.println("==============================================================");

        while (opcion != 16) {
            System.out.println();
            System.out.println("Selecciona una opción:");
            System.out.println("1.  Cargar archivo");
            System.out.println("2.  Crear mochila manual");
            System.out.println("3.  Crear mochila aleatoria");
            System.out.println("4.  Ejecutar Camion Recursive");
            System.out.println("5.  Ejecutar Camion Table");
            System.out.println("6.  Ejecutar Camion DP");
            System.out.println("7.  Ejecutar Camion DP2");
            System.out.println("8.  Ejecutar Camion DP3");
            System.out.println("9.  Ejecutar Camion DPMD");
            System.out.println("10. Ejecutar Camion Greedy");
            System.out.println("11. Ejecutar Camion Fraccionado DP");
            System.out.println("12. Ejecutar pruebas de rendimiento P variable");
            System.out.println("13. Ejecutar pruebas de rendimiento N variable");
            System.out.println("14. Ejecutar pruebas de rendimiento con archivos");
            System.out.println("15. Ejecutar pruebas de rendimiento manual");
            System.out.println("16. Salir");
            System.out.print("Opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    main.cargarArchivo();
                    break;
                case 2:
                    main.generateBack();
                    break;
                case 3:

                    System.out.println("Introduzca el número de objetos");
                    int n = scanner.nextInt();

                    System.out.println("Introduzca la capacidad máxima de la mochila");
                    int P = scanner.nextInt();

                    main.generateRandomKnapsack(n, P);
                    System.out.println("Se ha generado correctamente la mochila");

                    break;
                case 4:
                    main.camionRecursive();
                    break;
                case 5:
                    main.camionTable();
                    break;
                case 6:
                    main.camionDP();
                    break;
                case 7:
                    System.out.println("Ejecutando Camion DP2");
                    main.camionDP2();
                    break;
                case 8:
                    System.out.println("Ejecutando Camion DP3");
                    main.camionDP3();
                    break;
                case 9:
                    System.out.println("Ejecutando Camion DPMD");
                    main.camionDPMP();
                    break;
                case 10:
                    System.out.println("Ejecutando Camion Greedy");
                    main.camionFraccionadoGreedy();
                    break;
                case 11:
                    System.out.println("Ejecutando Camion Fraccionado DP");
                    System.out.println("Introduzca el número de divisiones");
                    int divisiones = scanner.nextInt();
                    main.camionFraccionadoDP(divisiones);
                    break;
                case 12:
                    main.testRendimiento();
                    break;
                case 13:
                    main.testRendimientov2();
                    break;
                case 14:
                    main.testArchivosData();
                    break;
                case 15:
                    System.out.println("Prueba de Rendimiento Manual");
                    System.out.println("Introduzca el número de objetos");
                    int objetos = scanner.nextInt();
                    
                    System.out.println("Introduzca el peso");
                    int peso = scanner.nextInt();
                    
                    main.testRendimientov3(objetos, peso);
                    
                    break;
                case 16:
                    System.out.println("Fin");
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;

            }
        }
    }

    /**
     * Carga un archivo con los datos de la mochila.
     * 
     */
    private void cargarArchivo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el numero del dataset que quiere probar");
        System.out.println("p01, p02, p03, p04, p05, p06, p07, p08");
        String archivo = "";
        do {
            System.out.print("Introduzca un dataset válido: ");
            archivo = scanner.nextLine();
        } while (!archivo.matches("p[0-8]{2}"));

        dp = new DynamicProgramming(dataset + archivo);
        System.out.println("*** Archivo cargado correctamente ***");
    }

    /**
     * Genera una mochila manualmente.
     * 
     * @return una instancia de la clase Knapsack que representa la mochila generada
     *         manualmente.
     */
    private void generateBack() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==============================================================");
        System.out.println("Introduzca el número de objetos");
        int n = scanner.nextInt();

        System.out.println("Introduzca la capacidad máxima de la mochila");
        int P = scanner.nextInt();
        scanner.nextLine(); // Limpia el buffer del scanner después de leer un entero

        ArrayList<Integer> profit = new ArrayList<>();
        ArrayList<Double> weight = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.println("Introduzca el nombre del objeto " + (i + 1) + ": ");
            String name = scanner.nextLine();

            System.out.println("Introduzca el peso entero del objeto " + (i + 1) + ": ");
            while (!scanner.hasNextInt()) {
                System.out.println("Por favor, ingrese un número entero válido para el peso.");
                scanner.next(); // Descarta la entrada incorrecta
            }
            int peso = scanner.nextInt();
            scanner.nextLine(); // Limpia el buffer

            System.out.println("Introduzca el beneficio del objeto " + (i + 1) + ": ");
            while (!scanner.hasNextDouble()) {
                System.out.println("Por favor, ingrese un número válido para el beneficio.");
                scanner.next(); // Descarta la entrada incorrecta
            }
            double beneficio = scanner.nextDouble();
            scanner.nextLine(); // Limpia el buffer

            profit.add(peso);
            weight.add(beneficio);
        }

        dp = new DynamicProgramming();
        dp.setProfit(weight);
        dp.setWeight(profit);
        dp.setCapacidad(P);

        System.out.println("Se ha generado correctamente la mochila");
    }

    /**
     * Genera una mochila con objetos generados aleatoriamente.
     * Utilizada principalmente para los test de rendimiento.
     * 
     * @param n Número de objetos
     * @param P Capacidad máxima de la mochila
     */
    private DynamicProgramming generateRandomKnapsack(int n, int P)
    {
        ArrayList<Integer> weights = new ArrayList<>();
        ArrayList<Double> profits = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int weight = random.nextInt(P) + 1; // Peso entre 1 y P
            double profit = random.nextDouble() * 100; // Beneficio entre 0.0 y 100.0

            weights.add(weight);
            profits.add(profit);
        }

        dp = new DynamicProgramming();
        dp.setWeight(weights);
        dp.setProfit(profits);
        dp.setCapacidad(P);

        return dp;
    }

    private void camionRecursive() {
        if (check()) {

            int[] p = new int[dp.getProfit().size()];
            double[] w = new double[dp.getWeight().size()];

            for (int i = 0; i < dp.getWeight().size(); i++) {
                p[i] = dp.getWeight().get(i);
            }

            for (int i = 0; i < dp.getProfit().size(); i++) {
                w[i] = dp.getProfit().get(i);
            }

        System.out.println("Ejercicio 1: Camion Recursive");
		System.out.println("BENEFICIO TOTAL: " +  dp.camionRecursive(dp.getProfit().size(), dp.getCapacidad(), p, w));

        }
    }

    private void camionTable(){
        if (check()) {

            int[] p = new int[dp.getProfit().size()];
            double[] w = new double[dp.getWeight().size()];

            for (int i = 0; i < dp.getWeight().size(); i++) {
                p[i] = dp.getWeight().get(i);
            }

            for (int i = 0; i < dp.getProfit().size(); i++) {
                w[i] = dp.getProfit().get(i);
            }

            System.out.println();
            System.out.println("Ejercicio 2: Camion Table");
            System.out.println();
            System.out.println("BENEFICIO TOTAL: " + dp.camionTable(dp.getProfit().size(), dp.getCapacidad(), p, w));
            dp.printTabla();
        }
    }

    private void camionDP(){
        if (check()) {

            int[] p = new int[dp.getProfit().size()];
            double[] w = new double[dp.getWeight().size()];

            for (int i = 0; i < dp.getWeight().size(); i++) {
                p[i] = dp.getWeight().get(i);
            }

            for (int i = 0; i < dp.getProfit().size(); i++) {
                w[i] = dp.getProfit().get(i);
            }

            System.out.println("Ejercicio 3: Camion DP");
            dp.setSolucion(new int[dp.getProfit().size()]);
            System.out.println("BENEFICIO TOTAL: " + dp.camionDP(dp.getCapacidad(), p, w));
            dp.showSolution();
        }
    }

    private void camionDP2() {
        if (check()) {

            int[] p = new int[dp.getProfit().size()];
            double[] w = new double[dp.getWeight().size()];

            for (int i = 0; i < dp.getWeight().size(); i++) {
                p[i] = dp.getWeight().get(i);
            }

            for (int i = 0; i < dp.getProfit().size(); i++) {
                w[i] = dp.getProfit().get(i);
            }

        System.out.println("Ejercicio 5: Camion DP2");
		System.out.println("BENEFICIO TOTAL: " +  dp.camionDP2(dp.getCapacidad(), p, w, true));

        }
    }

    private void camionDP3() {
        if (check()) {

            int[] p = new int[dp.getProfit().size()];
            double[] w = new double[dp.getWeight().size()];

            for (int i = 0; i < dp.getWeight().size(); i++) {
                p[i] = dp.getWeight().get(i);
            }

            for (int i = 0; i < dp.getProfit().size(); i++) {
                w[i] = dp.getProfit().get(i);
            }

        System.out.println("Ejercicio 6: CamionDP3");
		System.out.println("BENEFICIO TOTAL: " +  dp.camionDP3(dp.getCapacidad(), p, w));

        }
    }

    private void camionDPMP() {
        if (check()) {

            int[] p = new int[dp.getProfit().size()];
            double[] w = new double[dp.getWeight().size()];

            for (int i = 0; i < dp.getWeight().size(); i++) {
                p[i] = dp.getWeight().get(i);
            }

            for (int i = 0; i < dp.getProfit().size(); i++) {
                w[i] = dp.getProfit().get(i);
            }

        System.out.println("Ejercicio 7: CamionDPMP");
		System.out.println("BENEFICIO TOTAL: " +  dp.camionDPMP( dp.getCapacidad(), p, w));

        }
    }

    private void camionFraccionadoGreedy() {
        if (check()) {

            int[] p = new int[dp.getProfit().size()];
            double[] w = new double[dp.getWeight().size()];

            for (int i = 0; i < dp.getWeight().size(); i++) {
                p[i] = dp.getWeight().get(i);
            }

            for (int i = 0; i < dp.getProfit().size(); i++) {
                w[i] = dp.getProfit().get(i);
            }

        //Creando un array de objetos tipo beneficio/peso
        Objeto[] arr = new Objeto[p.length];
        for(int i = 0; i < p.length; i++) {
            arr[i] = new Objeto((int)w[i], p[i]);
        }

        System.out.println("Ejercicio 8: Camion Fraccionado Greedy");
		System.out.println("BENEFICIO TOTAL: " +  dp.camionFraccionadoGreedy(arr, dp.getCapacidad()));

        }
    }

    private void camionFraccionadoDP(int divisiones) {
        if (check()) {

            int[] p = new int[dp.getProfit().size()];
            double[] w = new double[dp.getWeight().size()];

            for (int i = 0; i < dp.getWeight().size(); i++) {
                p[i] = dp.getWeight().get(i);
            }

            for (int i = 0; i < dp.getProfit().size(); i++) {
                w[i] = dp.getProfit().get(i);
            }

        System.out.println("Ejercicio 9: Camion Fraccionado DP");
		System.out.println("BENEFICIO TOTAL: " +  dp.camionFraccionadoDP(dp.getCapacidad(), p, w, divisiones));

        }
    }


    /**
     * testRendimiento
     * 
     * Metodo utilizado para realizar las pruebas de rendimiento sobre los distintos algoritmos de la mochila
     * La idea del metodo es que se ejecute todo y nos lo devuelva en un listado para poder hacer un analisis
     * de los resultados de una manera mas sencilla que en implementaciones anteriores.
	 */
    private void testRendimiento() {
        long start = 0;
        long end = 0;
    
        DynamicProgramming dp;
        int n = 100;
        
        PrintWriter csvWriter;
        
        String archivo = this.carpeta_result + "rendimientoV1";
    
        
        try {
            csvWriter = new PrintWriter(archivo);
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo crear el archivo.");
            return;
        }
        
        csvWriter.println("P,Rec,Rec2,DP,DP2,DP3,DPMD,Greedy,Fraccionado");
        
        System.out.println("Test de rendimiento para mochila de " + n + " objetos.");
        System.out.format("%-17s%-17s%-17s%-17s%-17s%-17s%-17s%-17s%-17s\n", "P", "Rec", "Rec2", "DP", "DP2", "DP3", "DPMD", "Greedy", "Fraccionado");
    
        for (int P = 1; P <= 400; P++) {
    
            dp = generateRandomKnapsack(n, P);
            dp.setSolucion(new int[dp.getProfit().size()]);
    
            int[] p = new int[dp.getProfit().size()];
            double[] w = new double[dp.getWeight().size()];
    
            if (check()) {
                for (int i = 0; i < n; i++) {
                    w[i] = dp.getProfit().get(i);
                    p[i] = dp.getWeight().get(i);
                }
            }
    
           System.out.print(P + "\t");
    
            long timeRec = 0, timeRec2 = 0, timeDP = 0, timeDP2 = 0, timeDP3 = 0, timeDPMD = 0, timeGreedy = 0, timeFraccionado = 0;
    
            for (int j = 0; j < 5; j++) { // Bucle para realizar tres ejecuciones
                start = System.nanoTime();
                dp.camionRecursive(dp.getProfit().size(), dp.getCapacidad(), p, w);
                end = System.nanoTime();
                timeRec += end - start;
    
                start = System.nanoTime();
                dp.camionTable(dp.getProfit().size(), dp.getCapacidad(), p, w);
                end = System.nanoTime();
                timeRec2 += end - start;
    
                start = System.nanoTime();
                dp.camionDP(dp.getCapacidad(), p, w);
                end = System.nanoTime();
                timeDP += end - start;

                start = System.nanoTime();
                dp.camionDP2(dp.getCapacidad(), p, w, false);
                end = System.nanoTime();
                timeDP2 += end - start;

                start = System.nanoTime();
                dp.camionDP3(dp.getCapacidad(), p, w);
                end = System.nanoTime();
                timeDP3 += end - start;

                start = System.nanoTime();
                dp.camionDPMP(dp.getCapacidad(), p, w);
                end = System.nanoTime();
                timeDPMD += end - start;

                Objeto[] arr = new Objeto[p.length];
                for(int i = 0; i < p.length; i++) {
                    arr[i] = new Objeto((int)w[i], p[i]);
                }
                start = System.nanoTime();
                dp.camionFraccionadoGreedy(arr, dp.getCapacidad());
                end = System.nanoTime();
                timeGreedy += end - start;

                start = System.nanoTime();
                dp.camionFraccionadoDP(dp.getCapacidad(), p, w, 10);
                end = System.nanoTime();
                timeFraccionado += end - start;

            }
    
            // Calcula la media de los tiempos
            timeRec /= 5;
            timeRec2 /= 5;
            timeDP /= 5;
            timeDP2 /= 5;
            timeDP3 /= 5;
            timeDPMD /= 5;
            timeGreedy /= 5;
            timeFraccionado /= 5;
    
            // Imprime los tiempos medios
            System.out.format("%15d\t%15d\t%15d\t%15d\t%15d\t%15d\t%15d\t%15d\n", timeRec, timeRec2, timeDP, timeDP2, timeDP3, timeDPMD, timeGreedy, timeFraccionado);
            // Escribe los tiempos medios en el archivo CSV
            csvWriter.printf("%d,%d,%d,%d,%d,%d,%d,%d,%d\n", P, timeRec, timeRec2, timeDP, timeDP2, timeDP3, timeDPMD, timeGreedy, timeFraccionado);
        }
        
        csvWriter.close();
    }
    

    private void testRendimientov2() {
        long start = 0;
        long end = 0;
        
        PrintWriter csvWriter;
        
        String archivo = this.carpeta_result + "rendimientoV3";
        
        try {
            csvWriter = new PrintWriter(archivo);
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo crear el archivo.");
            return;
        }

        DynamicProgramming dp;
        int P = 10;

        System.out.println("Test de rendimiento.");
        System.out.format("%-17s%-17s%-17s%-17s%-17s%-17s%-17s%-17s%-17s\n", "N", "Rec", "Rec2", "DP", "DP2", "DP3", "DPMD", "Greedy", "Fraccionado");
    
        csvWriter.println("N,Rec,Rec2,DP,DP2,DP3,DPMD,Greedy,Fraccionado");

        for (int n = 1; n <= 1000; n = n+50) {

            dp = generateRandomKnapsack(n, P);
            dp.setSolucion(new int[dp.getProfit().size()]);

            int[] p = new int[dp.getProfit().size()];
            double[] w = new double[dp.getWeight().size()];

            if (check()) {
                for (int i = 0; i < n; i++) {
                    w[i] = dp.getProfit().get(i);
                    p[i] = dp.getWeight().get(i);
                }
            }

            System.out.print(n + "\t");
            
            long timeRec = 0, timeRec2 = 0, timeDP = 0, timeDP2 = 0, timeDP3 = 0, timeDPMD = 0, timeGreedy = 0, timeFraccionado = 0;
    
            for (int j = 0; j < 10; j++) { // Bucle para realizar tres ejecuciones
            	
            	if(n < 350)
            	{
                    start = System.nanoTime();
                    dp.camionRecursive(dp.getProfit().size(), dp.getCapacidad(), p, w);
                    end = System.nanoTime();
                    timeRec += end - start;
            	}

  
                start = System.nanoTime();
                dp.camionTable(dp.getProfit().size(), dp.getCapacidad(), p, w);
                end = System.nanoTime();
                timeRec2 += end - start;
    
                start = System.nanoTime();
                dp.camionDP(dp.getCapacidad(), p, w);
                end = System.nanoTime();
                timeDP += end - start;

                start = System.nanoTime();
                dp.camionDP2(dp.getCapacidad(), p, w, false);
                end = System.nanoTime();
                timeDP2 += end - start;

                start = System.nanoTime();
                dp.camionDP3(dp.getCapacidad(), p, w);
                end = System.nanoTime();
                timeDP3 += end - start;

                start = System.nanoTime();
                dp.camionDPMP(dp.getCapacidad(), p, w);
                end = System.nanoTime();
                timeDPMD += end - start;

                Objeto[] arr = new Objeto[p.length];
                for(int i = 0; i < p.length; i++) {
                    arr[i] = new Objeto((int)w[i], p[i]);
                }
                start = System.nanoTime();
                dp.camionFraccionadoGreedy(arr, dp.getCapacidad());
                end = System.nanoTime();
                timeGreedy += end - start;

                start = System.nanoTime();
                dp.camionFraccionadoDP(dp.getCapacidad(), p, w, 10);
                end = System.nanoTime();
                timeFraccionado += end - start;

            }
    
//             Calcula la media de los tiempos
            timeRec /= 5;
            timeRec2 /= 5;
            timeDP /= 5;
            timeDP2 /= 5;
            timeDP3 /= 5;
            timeDPMD /= 5;
            timeGreedy /= 5;
            timeFraccionado /= 5;
    
            // Imprime los tiempos medios
            System.out.format("%15d\t%15d\t%15d\t%15d\t%15d\t%15d\t%15d\t%15d\n", timeRec, timeRec2, timeDP, timeDP2, timeDP3, timeDPMD, timeGreedy, timeFraccionado);
            // Escribe los tiempos medios en el archivo CSV
            csvWriter.printf("%d,%d,%d,%d,%d,%d,%d,%d,%d\n", n, timeRec, timeRec2, timeDP, timeDP2, timeDP3, timeDPMD, timeGreedy, timeFraccionado);
        }
        
        csvWriter.close();
        
    }
    
    private void testRendimientov3(int n, int P) {
        long start = 0;
        long end = 0;
        
        DynamicProgramming dp;

        System.out.println("Test de rendimiento.");
        System.out.format("%-17s%-17s%-17s%-17s%-17s%-17s%-17s%-17s%-17s\n", "N", "Rec", "Rec2", "DP", "DP2", "DP3", "DPMD", "Greedy", "Fraccionado");


            dp = generateRandomKnapsack(n, P);
            dp.setSolucion(new int[dp.getProfit().size()]);

            int[] p = new int[dp.getProfit().size()];
            double[] w = new double[dp.getWeight().size()];

            if (check()) {
                for (int i = 0; i < n; i++) {
                    w[i] = dp.getProfit().get(i);
                    p[i] = dp.getWeight().get(i);
                }
            }

            System.out.print(n + "\t");
            
            long timeRec = 0, timeRec2 = 0, timeDP = 0, timeDP2 = 0, timeDP3 = 0, timeDPMD = 0, timeGreedy = 0, timeFraccionado = 0;
    
            for (int j = 0; j < 10; j++) { // Bucle para realizar tres ejecuciones
            	
            	if(n < 500)
            	{
                    start = System.nanoTime();
                    dp.camionRecursive(dp.getProfit().size(), dp.getCapacidad(), p, w);
                    end = System.nanoTime();
                    timeRec += end - start;
            	}

  
                start = System.nanoTime();
                dp.camionTable(dp.getProfit().size(), dp.getCapacidad(), p, w);
                end = System.nanoTime();
                timeRec2 += end - start;
    
                start = System.nanoTime();
                dp.camionDP(dp.getCapacidad(), p, w);
                end = System.nanoTime();
                timeDP += end - start;

                start = System.nanoTime();
                dp.camionDP2(dp.getCapacidad(), p, w, false);
                end = System.nanoTime();
                timeDP2 += end - start;

                start = System.nanoTime();
                dp.camionDP3(dp.getCapacidad(), p, w);
                end = System.nanoTime();
                timeDP3 += end - start;

                start = System.nanoTime();
                dp.camionDPMP(dp.getCapacidad(), p, w);
                end = System.nanoTime();
                timeDPMD += end - start;

                Objeto[] arr = new Objeto[p.length];
                for(int i = 0; i < p.length; i++) {
                    arr[i] = new Objeto((int)w[i], p[i]);
                }
                start = System.nanoTime();
                dp.camionFraccionadoGreedy(arr, dp.getCapacidad());
                end = System.nanoTime();
                timeGreedy += end - start;

                start = System.nanoTime();
                dp.camionFraccionadoDP(dp.getCapacidad(), p, w, 10);
                end = System.nanoTime();
                timeFraccionado += end - start;

            }
    
//             Calcula la media de los tiempos
            timeRec /= 5;
            timeRec2 /= 5;
            timeDP /= 5;
            timeDP2 /= 5;
            timeDP3 /= 5;
            timeDPMD /= 5;
            timeGreedy /= 5;
            timeFraccionado /= 5;
    
            // Imprime los tiempos medios
            System.out.format("%15d\t%15d\t%15d\t%15d\t%15d\t%15d\t%15d\t%15d\n", timeRec, timeRec2, timeDP, timeDP2, timeDP3, timeDPMD, timeGreedy, timeFraccionado);
            // Escribe los tiempos medios en el archivo CSV
                  

        
    }

    private void testArchivosData() {
        long start = 0;
        long end = 0;

        PrintWriter csvWriter;
        
        String[] test = { "p01", "p02", "p03", "p04", "p05", "p06", "p07"};

        String archivo2 = this.carpeta_result + "rendimientoV4";
        
        try {
            csvWriter = new PrintWriter(archivo2);
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo crear el archivo.");
            return;
        }
        
        System.out.println("Test de rendimiento con los archivos de datos.");
        System.out.format("%-17s%-17s%-17s%-17s%-17s%-17s%-17s%-17s%-17s\n", "Archivo", "Rec", "Rec2", "DP", "DP2", "DP3",
                "DPMD", "Greedy", "Fraccionado");

        csvWriter.println("Archivo,Rec,Rec2,DP,DP2,DP3,DPMD,Greedy,Fraccionado");
        
        for (int i = 0; i < test.length; i++) {
            String archivo = this.dataset + test[i];
            DynamicProgramming d = new DynamicProgramming(archivo);

            int[] p = new int[d.getProfit().size()];
            double[] w = new double[d.getWeight().size()];

            for (int j = 0; j < d.getWeight().size(); j++) {
                p[j] = d.getWeight().get(j);
            }

            for (int t = 0; t < d.getProfit().size(); t++) {
                w[t] = d.getProfit().get(t);
            }

            long timeRec = 0, timeRec2 = 0, timeDP = 0, timeDP2 = 0, timeDP3 = 0, timeDPMD = 0, timeGreedy = 0,
                    timeFraccionado = 0;

            System.out.print(test[i] + "\t");

            for (int k = 0; k < 5; k++) {
                start = System.nanoTime();
                d.camionRecursive(d.getProfit().size(), d.getCapacidad(), p, w);
                end = System.nanoTime();
                timeRec += end - start;

                start = System.nanoTime();
                d.camionTable(d.getProfit().size(), d.getCapacidad(), p, w);
                end = System.nanoTime();
                timeRec2 += end - start;

                start = System.nanoTime();
                d.camionDP(d.getCapacidad(), p, w);
                end = System.nanoTime();
                timeDP += end - start;

                start = System.nanoTime();
                d.camionDP2(d.getCapacidad(), p, w, false);
                end = System.nanoTime();
                timeDP2 += end - start;

                start = System.nanoTime();
                d.camionDP3(d.getCapacidad(), p, w);
                end = System.nanoTime();
                timeDP3 += end - start;

                start = System.nanoTime();
                d.camionDPMP(d.getCapacidad(), p, w);
                end = System.nanoTime();
                timeDPMD += end - start;

                Objeto[] arr = new Objeto[p.length];
                for (int r = 0; r < p.length; r++) {
                    arr[r] = new Objeto((int) w[r], p[r]);
                }
                start = System.nanoTime();
                d.camionFraccionadoGreedy(arr, d.getCapacidad());
                end = System.nanoTime();
                timeGreedy += end - start;

                start = System.nanoTime();
                d.camionFraccionadoDP(d.getCapacidad(), p, w, 10);
                end = System.nanoTime();
                timeFraccionado += end - start;

            }

            // Calcula la media de los tiempos
            timeRec /= 5;
            timeRec2 /= 5;
            timeDP /= 5;
            timeDP2 /= 5;
            timeDP3 /= 5;
            timeDPMD /= 5;
            timeGreedy /= 5;
            timeFraccionado /= 5;

            // Imprime los tiempos medios
            System.out.format("%15d\t%15d\t%15d\t%15d\t%15d\t%15d\t%15d\t%15d\n", timeRec, timeRec2, timeDP, timeDP2,
                    timeDP3, timeDPMD, timeGreedy, timeFraccionado);
            csvWriter.printf("%s,%d,%d,%d,%d,%d,%d,%d,%d\n", test[i], timeRec, timeRec2, timeDP, timeDP2, timeDP3, timeDPMD, timeGreedy, timeFraccionado);
        }
        
        csvWriter.close();

    }



    /**
	 * Verificamos si existe una mochila cargada con objetos.
	 * 
	 * @return true, si existe uan mochila, false en caso contrario.
	 */
	private boolean check() {
		if(dp == null || this.dp.getProfit().size() == 0 || this.dp.getWeight().size() == 0){
			System.err.println("Carga un archivo de datos o genera un escenario previamente");
			return false;
		}
		return true;
	}

}
