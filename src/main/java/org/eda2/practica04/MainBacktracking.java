package org.eda2.practica04;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


public class MainBacktracking {
    
    private ArrayList<Integer> weight;
    private int[] pesos;
    private int capacidad;
    private String dataset = System.getProperty("user.dir") + File.separator + "docs" + File.separator + "practica04"
			+ File.separator + "dataset" + File.separator;
    

    private String carpeta_result = System.getProperty("user.dir") + File.separator + "docs" + File.separator + "practica04"
			+ File.separator + "resultados" + File.separator;


    GreedyContLoading gt = new GreedyContLoading();
    
    int result;


    /*
     * -------------------------- MENU --------------------------
     */

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        MainBacktracking main = new MainBacktracking();
        int opcion = 0;

        System.out.println("==============================================================");
        System.out.println(" ███████╗██████╗  █████╗");
        System.out.println(" ██╔════╝██╔══██╗██╔══██╗");
        System.out.println(" █████╗░░██║░░██║███████║");
        System.out.println(" ██╔══╝░░██║░░██║██╔══██║");
        System.out.println(" ███████╗██████╔╝██║░░██║");
        System.out.println(" ╚══════╝╚═════╝░╚═╝░░╚═╝");
        System.out.println("==============================================================");
        System.out.println("              BIENVENIDOS A LA PRÁCTICA 05 DE LOSJIMENEZ      ");
        System.out.println("==============================================================");

        while (opcion != 12) {
            System.out.println();
            System.out.println("Selecciona una opción:");
            System.out.println("1.  Cargar archivo");
            System.out.println("2.  Generar camión aleatorio");
            System.out.println("3.  Ejecutar greedyContLoading");
            System.out.println("4.  Ejecutar backtracking1");
            System.out.println("5.  Ejecutar backtracking2");
            System.out.println("6.  Ejecutar backtracking3");
            System.out.println("7.  Ejecutar iterative");
            System.out.println("8.  Ejecutar pruebas de rendimiento manual");
            System.out.println("9.  Ejecutar pruebas de rendimiento para pesos aleatorios");
            System.out.println("10. Ejecutar pruebas de rendimiento para el escenario todos los pesos a 1");
            System.out.println("11. Ejecutar pruebas de rendimiento con archivos");
            System.out.println("12. Salir");
            System.out.print("Opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("Introduce el nombre del dataset (sin extensión): ");
                    System.out.println("p01, p02, p03, p04");
                    String archivo = scanner.next();
                    main.setDataset(main.dataset + archivo);
                    main.loadFile();
                    break;
                case 2:
                    System.out.println("Introduce el número de objetos: ");
                    int n = scanner.nextInt();
                    System.out.println("Introduce la capacidad máxima del camión: ");
                    int P = scanner.nextInt();
                    main.generateRandomTruck(n, P);
                    System.out.println("Generando camión aleatorio...");
                    break;
                case 3:
                    main.greedyContLoading();
                    System.out.println("Peso total cargado: " + main.result);
                    break;
                case 4:
                    main.backtracking1();
                    System.out.println("Peso total cargado: " + main.result);
                    
                    break;
                case 5:
                    main.backtracking2();
                    System.out.println("Peso total cargado: " + main.result);
                    
                    break;
                case 6:
                    main.backtracking3();
                    System.out.println("Peso total cargado: " + main.result);
                    break;
                case 7:
                    main.iterative();
                    System.out.println("Peso total cargado: " + main.result);
                    break;
                case 8:
                    System.out.println("Prueba de Rendimiento Manual");
                    System.out.println("Introduzca el número de objetos");
                    int n2 = scanner.nextInt();
                    System.out.println("Introduzca la capacidad máxima del camión");
                    int P2 = scanner.nextInt();
                
                    main.testRendimientoM(n2, P2);
                    break;
                case 9:
                    System.out.println("Prueba de Rendimiento Manual");
                    int n3 = 0;
                    System.out.println("Introduzca la capacidad máxima del camión");
                    int P3 = scanner.nextInt();
                
                    main.testRendimientov3(n3, P3);
                    break;
                case 10:
                    System.out.println("Prueba de Rendimiento Manual");
                    System.out.println("Introduzca la capacidad máxima del camión");
                    int P4 = scanner.nextInt();

                    main.testRendimientoP1(P4);
                    break; 
                case 11:
                    main.testArchivosData();
                	break;
                case 12:
                    System.out.println("Fin");  

                    break;
                default:
                    System.out.println("Opción no válida");

                    break;

            }
        }
    }

    /*
     * -------------------------- METODOS AUXILIARES --------------------------
     */

    /**
     * Genera una mochila con objetos generados aleatoriamente.
     * Utilizada principalmente para los test de rendimiento.
     * 
     * @param n Número de objetos
     * @param P Capacidad máxima del camión
     */
    private void generateRandomTruck(int n, int P)
    {
        int[] weights = new int[n+1];

        Random random = new Random();
        // System.out.println("Generando camión con " + n + " objetos y capacidad máxima de " + P + "...");
        // System.out.println("Pesos de los objetos: ");
        for (int i = 0; i < n+1; i++) {
            // if(i == 0) continue; // Queremos que el primer objeto tenga peso 0 
            int weight = random.nextInt(P) + 1;// Peso entre 1 y P
            weights[i] = weight;
            // System.out.print(weight + " ");
        }
        setCapacidad(P);
        setPesos(weights);
        // System.out.println("\nCapacidad del camión: " + P);
    }

    /**
     * Genera una mochila con pesos específicos que aseguran que se puedan sumar
     * todos los valores posibles desde 1 hasta la capacidad máxima P. Los pesos
     * son generados utilizando potencias de 2 para los primeros n elementos,
     * y el último peso se ajusta para asegurar que la suma de todos los pesos no 
     * exceda la capacidad máxima P.
     *
     * Este método es utilizado para pruebas de rendimiento donde es necesario
     * garantizar que todas las combinaciones posibles de pesos para que puedan ser
     * evaluadas, proporcionando un conjunto de datos que permite verificar
     * la capacidad de los algoritmos para recorrer el espacio completo de soluciones.
     *
     * @param n Número de objetos a incluir en la mochila.
     * @param P Capacidad máxima de la mochila.
     */
    private void generateRandomTruckP1(int n, int P, boolean visual)
    {
        int[] weights = new int[n+1];
    
        if(visual == true)
        {
            System.out.println("Generando camión con " + n + " objetos y capacidad máxima de " + P + "...");
            System.out.println("Pesos de los objetos: ");
        }
        
        // Aseguramos que los pesos permiten sumar cualquier valor hasta P
        for (int i = 0; i < n; i++) {
            // weights[i] = (int)Math.pow(2, i);
            weights[i] = 1;
            if(visual == true)
            {
                System.out.print(weights[i] + " ");
            }
        }
        
        // Ajustamos el último peso para que no supere la capacidad P
        // weights[n] = P - (int)Math.pow(2, n-1) + 1;
        if(visual == true)
        {
            System.out.print(weights[n] + " ");
        }
        
        setCapacidad(P);
        setPesos(weights);
        // System.out.println("\nCapacidad del camión: " + P);
    }
    
    /*
     * -------------------------- METODOS PRINCIPALES --------------------------
     */
    public int greedyContLoading()
    {
        return result = gt.greedyContLoading(this.getPesos(), this.getCapacidad());
    }
    
    public int backtracking1()
    {
    	return result = RecursiveBTContLoading1.maxLoading(this.getPesos(), this.getCapacidad());
    }
    
    public int backtracking2()
    {
    	return result = RecursiveBTContLoading2.maxLoading(this.getPesos(), this.getCapacidad());
    }

    public int backtracking3()
    {
    	return result = RecursiveBTContLoading3.maxLoading(this.getPesos(), this.getCapacidad());
    }

    public int iterative(){
        return result = IterativeBTContLoading.maxLoading(this.getPesos(), this.getCapacidad());
    }

    public int btThread(){
        return result = RecursiveBTContLoading1Thread.máximaCargaParalelo(this.getPesos(), this.getCapacidad());
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public ArrayList<Integer> getWeight() {
        return weight;
    }

    public void setWeight(ArrayList<Integer> weight) {
        this.weight = weight;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getDataset() {
        return dataset;
    }
   
    public int[] getPesos() {
		return pesos;
	}

	public void setPesos(int[] pesos) {
		this.pesos = pesos;
	}

	public GreedyContLoading getGt() {
		return gt;
	}

	public void setGt(GreedyContLoading gt) {
		this.gt = gt;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public void loadFile()
    {
        this.weight = new ArrayList<>();
       
        Scanner sc;

        try {
            // Cargando la capacidad del archivo _c.txt
            File f = new File(dataset + "_c.txt");
            sc = new Scanner(f);
            if (sc.hasNextLine()) {
                this.capacidad = Integer.parseInt(sc.nextLine().trim());
            }
            sc.close();

            // Cargando los pesos del archivo _w.txt
            f = new File(dataset + "_w.txt");
            sc = new Scanner(f);
            while (sc.hasNextLine()) {
                this.weight.add(Integer.parseInt(sc.nextLine().trim()));
            }
            sc.close();
            
            this.setPesos(new int[this.getWeight().size()]);
            
    		for (int i = 0; i < this.getWeight().size(); i++) {
    			this.getPesos()[i] = this.getWeight().get(i);
    		}

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error al cargar los archivos. Asegúrate de que la ruta y los nombres de archivo son correctos.");
        }
    }

    /*
     * -------------------------- TESTS DE RENDIMIENTO --------------------------
     * 
     */
	private void testRendimientov3(int n, int P) {
	    long start = 0;
	    long end = 0;

	    System.out.println("------------------------------------");
	    System.out.println("Test de rendimiento.");
	    System.out.println("------------------------------------");
	    System.out.format("%-17s%-17s%-17s%-17s%-17s%-17s%-17s%-17s\n", "N", "Size", "Grdy", "It", "Bt1", "Bt2", "Bt3", "Tht");

	    PrintWriter csvWriter;
	    String archivo = this.carpeta_result + "rendimientoV128.csv";

	    try {
	        csvWriter = new PrintWriter(archivo);
	    } catch (FileNotFoundException e) {
	        System.err.println("No se pudo crear el archivo.");
            System.err.println(e);
	        return;
	    }

	    csvWriter.println("N,Size,Grdy,It,Bt1,Bt2,Bt3,Tht");

	    for (int j = 1; j <= 8; j++) {
	        int size = (int) Math.pow(2, j);
	        generateRandomTruck(size, P);

	        long timeGreedy = 0, timeIterative = 0, timeBt01 = 0, timeBt02 = 0, timeBt03 = 0, timeThread = 0;

	        int iterations = (size >= 256) ? 1 : 10;

	        for (int i = 0; i < iterations; i++) {
	            start = System.nanoTime();
	            greedyContLoading();
	            end = System.nanoTime();
	            timeGreedy += end - start;

	            start = System.nanoTime();
	            iterative();
	            end = System.nanoTime();
	            timeIterative += end - start;

	            start = System.nanoTime();
	            backtracking1();
	            end = System.nanoTime();
	            timeBt01 += end - start;

	            start = System.nanoTime();
	            backtracking2();
	            end = System.nanoTime();
	            timeBt02 += end - start;

	            start = System.nanoTime();
	            backtracking3();
	            end = System.nanoTime();
	            timeBt03 += end - start;

	            start = System.nanoTime();
	            btThread();
	            end = System.nanoTime();
	            timeThread += end - start;


                // Escribe los resultados en el CSV
                csvWriter.printf("%d,%d,%d,%d,%d,%d,%d,%d\n", size, P, timeGreedy, timeIterative, timeBt01, timeBt02, timeBt03, timeThread);

	        }

	        // Calcula la media de los tiempos si hay más de una iteración
	        if (iterations > 1) {
	            timeGreedy /= iterations;
	            timeIterative /= iterations;
	            timeBt01 /= iterations;
	            timeBt02 /= iterations;
	            timeBt03 /= iterations;
	            timeThread /= iterations;
	        }

	        // Imprime los tiempos medios
	        System.out.format("%-17d%-17d%-17d%-17d%-17d%-17d%-17d%-17d\n", size, P, timeGreedy, timeIterative, timeBt01, timeBt02, timeBt03, timeThread);
	    }

	    csvWriter.close();
	}
  
    private void testRendimientoM(int n, int P) {
        long start = 0;
        long end = 0;

        System.out.println("------------------------------------");
        System.out.println("Test de rendimiento.");
        System.out.println("------------------------------------");
        System.out.format("%-17s%-17s%-17s%-17s%-17s%-17s%-17s%-17s\n", "N", "Size", "Grdy", "It", "Bt1", "Bt2", "Bt3",
                "Tht");

        long timeGreedy = 0, timeIterative = 0, timeBt01 = 0, timeBt02 = 0, timeBt03 = 0, timeThread = 0;

        generateRandomTruck(n, P);

        start = System.nanoTime();
        greedyContLoading();
        end = System.nanoTime();
        timeGreedy += end - start;

        start = System.nanoTime();
        iterative();
        end = System.nanoTime();
        timeIterative += end - start;

        start = System.nanoTime();
        backtracking1();
        end = System.nanoTime();
        timeBt01 += end - start;

        start = System.nanoTime();
        backtracking2();
        end = System.nanoTime();
        timeBt02 += end - start;

        start = System.nanoTime();
        backtracking3();
        end = System.nanoTime();
        timeBt03 += end - start;

        start = System.nanoTime();
        btThread();
        end = System.nanoTime();
        timeThread += end - start;

        // Imprime los tiempos medios
        System.out.format("%-17d%-17d%-17d%-17d%-17d%-17d%-17d%-17d\n", n, P, timeGreedy, timeIterative, timeBt01,
                timeBt02, timeBt03, timeThread);

    }

    private void testRendimientoP1(int P) {
        long start = 0;
        long end = 0;
    
    
        System.out.println("------------------------------------");
        System.out.println("Test de rendimiento para los casos P1.");
        System.out.println("------------------------------------");
	    System.out.format("%-17s%-17s%-17s%-17s%-17s%-17s%-17s%-17s\n", "N", "Size", "Grdy", "It", "Bt1", "Bt2", "Bt3", "Tht");
    
        PrintWriter csvWriter;
        String archivo = this.carpeta_result + "rendimientoP1V32.csv";
    
        
        try {
            csvWriter = new PrintWriter(archivo);
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo crear el archivo.");
            return;
        }

        csvWriter.println("N,Size,Grdy,It,Bt1,Bt2,Bt3,Tht");

        for (int j = 1; j <= 6; j++) {
	        int size = (int) Math.pow(2, j);
	        generateRandomTruckP1(size, P,false);

	        long timeGreedy = 0, timeIterative = 0, timeBt01 = 0, timeBt02 = 0, timeBt03 = 0, timeThread = 0;

	        int iterations = (size >= 30) ? 1 : 10;

	        for (int i = 0; i < iterations; i++) {
	            start = System.nanoTime();
	            greedyContLoading();
	            end = System.nanoTime();
	            timeGreedy += end - start;

	            start = System.nanoTime();
	            iterative();
	            end = System.nanoTime();
	            timeIterative += end - start;

	            start = System.nanoTime();
	            backtracking1();
	            end = System.nanoTime();
	            timeBt01 += end - start;

	            start = System.nanoTime();
	            backtracking2();
	            end = System.nanoTime();
	            timeBt02 += end - start;

	            start = System.nanoTime();
	            backtracking3();
	            end = System.nanoTime();
	            timeBt03 += end - start;

	            start = System.nanoTime();
	            btThread();
	            end = System.nanoTime();
	            timeThread += end - start;


                // Escribe los resultados en el CSV
                csvWriter.printf("%d,%d,%d,%d,%d,%d,%d,%d\n", size, P, timeGreedy, timeIterative, timeBt01, timeBt02, timeBt03, timeThread);

	        }

	        // Calcula la media de los tiempos si hay más de una iteración
	        if (iterations > 1) {
	            timeGreedy /= iterations;
	            timeIterative /= iterations;
	            timeBt01 /= iterations;
	            timeBt02 /= iterations;
	            timeBt03 /= iterations;
	            timeThread /= iterations;
	        }

	        // Imprime los tiempos medios
	        System.out.format("%-17d%-17d%-17d%-17d%-17d%-17d%-17d%-17d\n", size, P, timeGreedy, timeIterative, timeBt01, timeBt02, timeBt03, timeThread);
	    }

	    csvWriter.close();
    }
    
    private void testArchivosData() {
        long start, end;
        
        PrintWriter csvWriter;
        
        String[] test = { "p01", "p02", "p03", "p04" };
        String archivo2 = this.carpeta_result + "rendimientoArchivo.csv";
        String data = System.getProperty("user.dir") + File.separator + "docs" + File.separator + "practica04"
    			+ File.separator + "dataset" + File.separator;
        
        try {
            csvWriter = new PrintWriter(archivo2);
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo crear el archivo.");
            return;
        }
        
        csvWriter.println("Archivo,Grdy,It,Bt1,Bt2,Bt3,Tht");

        for (int i = 0; i < test.length; i++) {
            String archivo = data + test[i];
            this.setDataset(archivo);  
            this.loadFile();
            
            long timeGreedy = 0, timeIterative = 0, timeBt01 = 0, timeBt02 = 0, timeBt03 = 0, timeThread = 0;
            int iterations = 10; 

            for (int j = 1; j <= iterations; j++) {
                start = System.nanoTime();
                greedyContLoading();
                end = System.nanoTime();
                timeGreedy += end - start;

                start = System.nanoTime();
                iterative();
                end = System.nanoTime();
                timeIterative += end - start;

                start = System.nanoTime();
                backtracking1();
                end = System.nanoTime();
                timeBt01 += end - start;

                start = System.nanoTime();
                backtracking2();
                end = System.nanoTime();
                timeBt02 += end - start;

                start = System.nanoTime();
                backtracking3();
                end = System.nanoTime();
                timeBt03 += end - start;

                start = System.nanoTime();
                btThread();
                end = System.nanoTime();
                timeThread += end - start;
            }

            // Calcula la media de los tiempos
            timeGreedy /= iterations;
            timeIterative /= iterations;
            timeBt01 /= iterations;
            timeBt02 /= iterations;
            timeBt03 /= iterations;
            timeThread /= iterations;

            // Escribe los resultados en el CSV
            csvWriter.printf("%s,%d,%d,%d,%d,%d,%d\n", test[i], timeGreedy, timeIterative, timeBt01, timeBt02, timeBt03, timeThread);

            // Imprime los tiempos medios
            System.out.format("%-17s%-17d%-17d%-17d%-17d%-17d%-17d\n", test[i], timeGreedy, timeIterative, timeBt01, timeBt02, timeBt03, timeThread);
        }
        this.setDataset(data);
        csvWriter.close();
    }


}
