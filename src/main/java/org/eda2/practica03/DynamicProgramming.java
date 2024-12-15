package org.eda2.practica03;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Dynamic Programming
 * 
 * Clase que implementa los algoritmos de programación dinámica.
 * 
 * @author Adrián Jiménez Benitez
 * @author Antonio José Jiménez Luque
 */
public class DynamicProgramming {
 
    private double[][] tabla;
    private int Capacidad;

    private ArrayList<Integer> weight;
    private ArrayList<Double> profit;

    private int[] solucion;
    private double[] aux;

    /*
     * Clase interna para representar un objeto con un peso y un beneficio asociado.
     */
    static class Objeto {
 
        int beneficio, peso;
 
        // Item value function
        public Objeto(int beneficio, int peso)
        {
            this.peso = peso;
            this.beneficio = beneficio;
        }
    }

    /*
     * Constructores
     */
    public DynamicProgramming() {
    }

    public DynamicProgramming(double[][] tabla) {
        this.tabla = tabla;
    }

    /**
     * Instancia una nueva mochila a partir de los archivos de prueba disponibles.
     * Los datos de capacidad, pesos y beneficios se leen de tres archivos diferentes,
     * cada uno especificado con un sufijo diferente (_c.txt, _w.txt, _p.txt).
     *
     * @param dataset path del archivo de prueba sin la extensión y el sufijo.
     */
    public DynamicProgramming(String dataset) {
        this.weight = new ArrayList<>();
        this.profit = new ArrayList<>();
       
        Scanner sc;

        try {
            // Cargando la capacidad del archivo _c.txt
            File f = new File(dataset + "_c.txt");
            sc = new Scanner(f);
            if (sc.hasNextLine()) {
                this.Capacidad = Integer.parseInt(sc.nextLine().trim());
            }
            sc.close();

            // Cargando los pesos del archivo _w.txt
            f = new File(dataset + "_w.txt");
            sc = new Scanner(f);
            while (sc.hasNextLine()) {
                this.weight.add(Integer.parseInt(sc.nextLine().trim()));
            }
            sc.close();

            // Cargando los beneficios del archivo _p.txt
            f = new File(dataset + "_p.txt");
            sc = new Scanner(f);
            while (sc.hasNextLine()) {
                this.profit.add(Double.parseDouble(sc.nextLine().trim()));
            }
            sc.close();
            
            this.solucion = new int[this.getProfit().size()];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error al cargar los archivos. Asegúrate de que la ruta y los nombres de archivo son correctos.");
        }
    }

    /**
     * camionRecursive Ejercicio 1
     * 
     * Calcula el máximo beneficio que puede ser transportado en un camión con un peso máximo permitido, utilizando un enfoque recursivo.
     * 
     * Este método resuelve el problema de la mochila mediante recursividad, determinando el mayor valor
     * de beneficio que puede ser obtenido de un conjunto dado de objetos, cada uno con un peso y un beneficio definidos,
     * sin exceder el peso máximo permitido del camión.
     * 
     * @param n El número de objetos disponibles.
     * @param P El peso máximo permitido (PMA) del camión en unidades de peso.
     * @param p Un array de enteros donde cada elemento representa el peso de un objeto.
     * @param b Un array de doubles donde cada elemento representa el beneficio asociado a un objeto.
     * @return El máximo beneficio que puede ser transportado sin exceder el PMA.
     * 
     * @implNote Este método realiza cálculos recursivos y puede ser ineficiente para grandes conjuntos de datos
     * debido a la posible superposición de subproblemas. Considerar la implementación de técnicas de memorización
     * o una solución iterativa con programación dinámica para mejorar el rendimiento como se plantea en los siguientes
     * ejercicios.
     */
    public double camionRecursive(int n, int P, int p[], double b[]){
        // Caso base: si no quedan objetos para considerar (n == 0) o la capacidad máxima permitida del camión (P) es 0, el beneficio máximo es 0.
        if(n == 0 || P == 0){
            return 0;
        }

        // Comprobar si el peso del último objeto considerado es mayor que la capacidad restante del camión.
        if(p[n-1] > P){
            // Si el peso del objeto es mayor que la capacidad disponible, no se puede incluir el objeto.
            // Por lo tanto, se resuelve el problema para los restantes n-1 objetos.
            return camionRecursive(n-1, P, p, b);
        }else{
            // Considerar dos casos: no incluir el objeto actual o incluirlo.
            // No incluir el objeto: Resolver el problema para los n-1 objetos sin modificar la capacidad.
            // Incluir el objeto: Agregar el beneficio del objeto actual y resolver para los n-1 objetos 
            // con la capacidad reducida por el peso del objeto actual.
            // Devuelve el máximo de ambos casos.
            return Math.max(camionRecursive(n-1, P, p, b), b[n-1] + camionRecursive(n-1, P-p[n-1], p, b));
        }
    }

    /**
     * camionRec Ejercicio 2
     * 
     * Método recursivo para calcular el beneficio máximo. Utiliza una tabla para almacenar
     * los resultados intermedios.
     *
     * @param n int, El número de objetos restantes para considerar.
     * @param P int, La capacidad restante del camión.
     * @param p int[], Los pesos de los objetos.
     * @param b double[], Los beneficios de los objetos.
     * @param table dobule[][], La tabla.
     * @return El beneficio máximo obtenido con los objetos restantes y la capacidad dada.
     */
    private double camionRec(int n, int P, int p[], double b[], double[][] table){
        // Caso base: no hay objetos o no hay capacidad.
        if (n == 0 || P == 0) {
            return 0; 
        }

        // devuelve el valor guardado si ya ha sido calculado, de esta manera evitamos 
        // recalcular valores ya calculados.
        if (table[n][P] != -1) {
            return table[n][P]; 
        }
        if (p[n-1] > P) {
            // El objeto actual es demasiado pesado para incluirlo.
            table[n][P] = camionRec(n-1, P, p, b, table);
        } else {
            // Considerar dos casos: no incluir el objeto actual o incluirlo.
            table[n][P] = Math.max(camionRec(n-1, P, p, b, table),
                                   b[n-1] + camionRec(n-1, P-p[n-1], p, b, table)); // máximo entre no llevar o llevar el objeto
        }

        // Almacena y retorna el beneficio máximo calculado.
        return table[n][P];
        
    }

    /** 
     * camionTable Ejercicio 2
     * 
     * Método principal para calcular el beneficio maximo que puede cargar el camión.
     * Este método prepara la tabla e invoca el mEtodo recursivo.
     *
     * @param n int, El número total de objetos disponibles.
     * @param P int, La capacidad máxima de peso del camión.
     * @param p int[], Los pesos de los objetos.
     * @param b int[], Los beneficios asociados a cada objeto.
     * @return El beneficio máximo que puede obtenerse sin exceder el peso máximo.
     */
    public double camionTable(int n, int P, int p[], double b[])
    {
        // tabla para guardar los resultados intermedios, inicializada con  n+1 filas y P+1 columnas
        double[][] table = new double[n + 1][P + 1]; 
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= P; j++) {
                table[i][j] = -1; // inicializa la tabla con -1
            }
        }
        this.tabla = table;
        return camionRec(n, P, p, b, table);

    }

    /**
     * camionDP Ejercicio 3
     * 
     * Resuelve el problema de la mochila mediante programación dinámica para determinar
     * la selección óptima de elementos que maximiza el beneficio sin exceder la capacidad
     * máxima del camión.
     * 
     * Este método construye una tabla dinámica donde cada celda representa el máximo beneficio
     * alcanzable con un subconjunto de los primeros i elementos y con una capacidad j.
     * Luego recupera la solución específica que indica qué elementos se incluyen en la mochila
     * óptima.
     *
     * @param P La capacidad máxima del camión (peso máximo que puede llevar).
     * @param p Array de enteros donde cada elemento representa el peso de un item.
     * @param b Array de doubles donde cada elemento representa el beneficio de un item.
     * @return El beneficio máximo que se puede obtener dado el peso y beneficio de los elementos.
     *
     * @throws IllegalArgumentException si los arrays de pesos y beneficios no son de igual longitud
     *                                  o si alguno de los parámetros es null.
    */
    public double camionDP(int P, int p[], double b[])
    {

        // Verifica que los parámetros de entrada sean válidos
        if (p == null || b == null || p.length != b.length) {
            throw new IllegalArgumentException("Los arrays deben tener la misma longitud y no ser nulos.");
        }

        int i;
		int j;
		int n = b.length;
		this.tabla = new double[n+1][P+1];
		
		for (i = 1; i <= n; i++) {
			for (j = 1; j < p[i-1]; j++) {
				this.tabla[i][j] = this.tabla[i-1][j];
			}
			for (j = p[i-1]; j <= P; j++) {
				double b1 = this.tabla[i-1][j-p[i-1]] + b[i-1];
				double b2 = this.tabla[i-1][j];
				this.tabla[i][j] = Math.max(b1, b2);
			}
		}
		        
//        System.out.println();
        test(n, P, p, b, this.solucion);
//        System.out.println("\n");
		
	    return this.tabla[n][P];
    }

    /**
     * test Ejercicio 3
     * 
     * Muestra la solución óptima para el problema de la mochila.
     * 
     * Este método recorre la tabla de programación dinámica para determinar
     * qué elementos se incluyen en la solución óptima.
     *
     * @param j El número total de objetos disponibles.
     * @param c La capacidad máxima de peso del camión.
     * @param p Array de enteros donde cada elemento representa el peso de un item.
     * @param b Array de doubles donde cada elemento representa el beneficio de un item.
     * @param sol Array de enteros que almacena la solución óptima.
    */
    public void test(int j, int c, int[] p, double[] b, int[] sol) {
        if (j > 0) {
            if (c < p[j - 1]) {
                test(j - 1, c, p, b, sol);  // Si no cabe el objeto, continúa sin incluirlo
            } else {
                // Comprobar si incluyendo el objeto se obtiene un mejor resultado
                if (tabla[j - 1][c - p[j - 1]] + b[j - 1] > tabla[j - 1][c]) {
                    test(j - 1, c - p[j - 1], p, b, sol);  // Recursión incluyendo el objeto
                    this.solucion[j - 1] = 1;  // Marcar este objeto como incluido
                } else {
                    test(j - 1, c, p, b, sol);  // Recursión sin incluir el objeto
                }
            }
        }
    }

    /**
     * camionDP2 Ejercicio 5
     * 
     * Resuelve el problema de la mochila mediante programación dinámica para determinar
     * la selección óptima de elementos que maximiza el beneficio sin exceder la capacidad
     * máxima del camión.
     * 
     * Este método construye una tabla dinámica donde cada celda representa el máximo beneficio
     * alcanzable con un subconjunto de los primeros i elementos y con una capacidad j.
     * Luego recupera la solución específica que indica qué elementos se incluyen en la mochila
     * óptima.
     * 
     * @param P La capacidad máxima del camión (peso máximo que puede llevar).
     * @param p Array de enteros donde cada elemento representa el peso de un item.
     * @param b Array de doubles donde cada elemento representa el beneficio de un item.
     * @return El beneficio máximo que se puede obtener dado el peso y beneficio de los elementos.
     * 
     */

     public double camionDP2(int P, int p[], double b[], boolean print){
        //Asignamos el tamaño del bucle i
        int n = b.length;

        //Creamos un array bidimensional de double con tamaño n+1 y P+1
        double camion[][] = new double[n+1][P+1];

        //Inicializamos la primera fila y columna de la tabla a 0
        for(int i = 0; i < n; i++){
            camion[i][0] = 0;
        }
        for(int j = 0; j < P; j++){
            camion[0][j] = 0;
        }
        
        //Realizamos un bucle que recorre la tabla
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= P; j++){

                //Si el peso del objeto es menor o igual al peso del camión
                if(p[i-1] <= j){
                    //Asignamos el máximo entre el valor actual y el valor anterior
                    if(camion[i-1][j-p[i-1]] + b[i-1] > camion[i-1][j]){
                        camion[i][j] = camion[i-1][j-p[i-1]] + b[i-1];
                    }else{
                        camion[i][j] = camion[i-1][j];
                    }
                }else{
                    camion[i][j] = camion[i-1][j];
                }
            }
        }

        //Si se quiere mostrar la solución
        if(print == true){

        //Recuperamos la solución
        int [] Sol = new int[n];
        int auxW = P;
        //Recorremos la tabla de atrás hacia adelante
        for (int i = n; i >= 2; i--) {
            //Si el valor de la celda actual es igual al valor de la celda anterior
        	if (camion[i - 1][auxW] == camion[i][auxW]) {
                //Asignamos 0
        		Sol[i - 1] = 0;
        	}
            //Si el valor de la celda actual es diferente al valor de la celda anterior
        	else {
                //Asignamos 1
        		Sol[i - 1] = 1;
                //Restamos el peso del objeto al peso del camión
        		auxW -= p[i - 1];
        	}
        }
        //Si el valor de la celda actual no es 0
        if (camion[1][auxW] != 0)
            //Asignamos 0 al primer valor de la solución
        	Sol[0] = 0;
        else
            //Asignamos 1 al primer valor de la solución
        	Sol[0] = 1;
        
        //Imprimimos la solución
        System.out.print("Solution: ");
        for (int i = 0; i < Sol.length; i++)
            if(Sol.length - 1 == i)
                System.out.print(Sol[i]);
            else
                System.out.print(Sol[i] + ", ");
        System.out.println();
        
        System.out.println();
        //Imprimimos la tabla
        test(n, P, p, b, camion);
        System.out.println("\n");
    }

        return camion[n][P];
    }

    /**
     * test Ejercicio 5
     * 
     * Muestra la solución óptima para el problema de la mochila.
     * 
     * Este método recorre la tabla de programación dinámica para determinar
     * qué elementos se incluyen en la solución óptima.
     * 
     * @param j El número total de objetos disponibles.
     * @param c La capacidad máxima de peso del camión.
     * @param p Array de enteros donde cada elemento representa el peso de un item.
     * @param b Array de doubles donde cada elemento representa el beneficio de un item.
     * @param camion Array bidimensional de doubles que representa la tabla de programación dinámica.
     */

    public void test(int j, int c, int p[], double b[], double camion[][]) {
        while (j > 0 && c > 0) {
            if (camion[j][c] != camion[j-1][c]) {
                System.out.print(j + ", ");
                c -= p[j-1];
            }
            j--;
        }
    }
    
     /*
     * camionDP3 Ejercicio 6
     * 
     * Resuelve el problema de la mochila mediante programación dinámica para determinar
     * la selección óptima de elementos que maximiza el beneficio sin exceder la capacidad
     * máxima del camión.
     * 
     * Este método construye una tabla dinámica donde cada celda representa el máximo beneficio
     * alcanzable con un subconjunto de los primeros i elementos y con una capacidad j.
     * Luego se recupera el valor máximo que puede obtener la el beneficio máximo
     * sin exceder la capacidad.
     * 
     * @param P La capacidad máxima del camión (peso máximo que puede llevar).
     * @param p Array de enteros donde cada elemento representa el peso de un item.
     * @param b Array de doubles donde cada elemento representa el beneficio de un item.
     * @return El beneficio máximo que se puede obtener dado el peso y beneficio de los elementos.
     */
    public double camionDP3(int P, int p[], double b[]) {
        //Asignamos el tamaño del bucle i
    	int n = b.length;
        //Creamos un array de double con tamaño P + 1
        double[] dp = new double[P + 1];
 
        //Realizamos un bucle que recorre el array de double
        for (int i = 1; i < n + 1; i++) {
            for (int w = P; w >= 0; w--) {
                //Si el peso es menor o igual al peso del camión
                if (p[i - 1] <= w)
                    //Asignamos el máximo entre el valor actual y el valor anterior
                    dp[w] = Math.max(dp[w - p[i - 1]] + b[i - 1], dp[w]);
            }
        }
        
        return dp[P];
    }

    /**
     * camionDPMP Ejercicio 7
     * 
     * Resuelve el problema de la mochila mediante programación dinámica para maximizar el beneficio.
     * 
     * Este método implementa el algoritmo de la mochila con un enfoque de programación dinámica, donde cada entrada del
     * array representa el máximo beneficio que se puede obtener con una capacidad dada. Itera sobre cada peso
     * y beneficio posible, actualizando el array para reflejar el máximo beneficio que se puede lograr con cada
     * incremento de peso hasta la capacidad máxima P.
     * 
     * @param P La capacidad máxima del camión (o mochila).
     * @param p El array de pesos de los elementos que se pueden cargar en el camión.
     * @param b El array de beneficios asociados a cada elemento.
     * 
     * @return El valor máximo de beneficio que se puede obtener sin exceder la capacidad del camión.
     */
    public double camionDPMP(int P, int p[], double b[]){
		double [] array = new double[P + 1];
		
		int n = b.length;
		for (int i = 0; i <= P; i++) {
			for (int j = 0; j < n; j++) {
                if(p[j] <= i) {
                    array[i] = Math.max(array[i], array[i - p[j]] + b[j]);
                }
			}
		}
        this.setAux(array);
		return array[array.length-1];
	}
    
    /**
     * obtenerSolucion Ejercicio 7 c)
     * 
     * Calcula y devuelve los elementos que forman parte de la solución óptima de la mochila.
     * 
     * Este método determina la cantidad de cada elemento que se debe incluir en la mochila para maximizar
     * el beneficio total sin exceder la capacidad máxima permitida. Utiliza una estrategia de recuperación
     * de elementos donde, comenzando desde la capacidad máxima y retrocediendo, selecciona el elemento que
     * maximiza el beneficio hasta alcanzar la capacidad mínima o hasta que no se puedan incluir más elementos.
     *
     * @return Un array de enteros donde cada índice representa un elemento específico y su valor
     *         la cantidad de veces que dicho elemento se incluye en la solución óptima.
     */
	public int[] obtenerSolucion(int P, int p[], double b[]) {
        double minWeight = 0;
        int c = P;
        int n = p.length;
        int[] sol = new int[n];
        while (c >= minWeight) {
            double maxValue = -1;
            int item = -1;
            for (int i = n-1; i >= 0; i--) {
                if (p[i] <= c && p[i] >= 0) {  // Asegura que el peso i es menor que la capacidad restante y no negativo
                    double newValue = this.aux[c - p[i]] + b[i];
                    if (newValue > maxValue) {
                        maxValue = newValue;
                        item = i;
                    }
                }
            }
            if (item == -1) break;  // No se encontró ningún item válido para agregar, termina el bucle
            sol[item]++;
            c -= p[item];
        }
        return sol;
    }

    /*
     * camionFraccionadoGreedy Ejercicio 9
     * 
     * Resuelve el problema de la mochila fraccionada mediante un enfoque voraz (greedy) para determinar
     * la selección óptima de elementos que maximiza el beneficio sin exceder la capacidad
     * máxima del camión.
     * 
     * Este método ordena los objetos por la relación beneficio/peso y selecciona fracciones de los objetos
     * en orden descendente de la relación beneficio/peso hasta que se llena la capacidad del camión.
     * 
     * @param arr Array de objetos donde cada elemento representa un objeto con un peso y un beneficio.
     * @param capacidad La capacidad máxima del camión (peso máximo que puede llevar).
     * @return El beneficio máximo que se puede obtener dado el peso y beneficio de los elementos.
     * 
     * @implNote Este método garantica la solución óptima para el problema de la mochila fraccionada
     * debido a la naturaleza del enfoque voraz.
     */
    public double camionFraccionadoGreedy(Objeto[] arr, int capacidad) {

        Arrays.sort(arr, new Comparator<Objeto>() {
            @Override
            public int compare(Objeto item1, Objeto item2) {
                double cpr1 = (double)(item1.beneficio / (double)item1.peso);
                double cpr2 = ((double)item2.beneficio / (double)item2.peso);
 
                if (cpr1 < cpr2)
                    return 1;
                else
                    return -1;
            }
        });
 
        double totalValue = 0d;
        double [] sol = new double[arr.length];
        for (int j = 0; j < sol.length; j++)
        	sol[j] = 0.0;
        
        int j = 0;
        for (Objeto i : arr) {
            int curWt = (int)i.peso;
            int curVal = (int)i.beneficio;
 
            if (capacidad - curWt >= 0) {
                // this weight can be picked while
                capacidad = capacidad - curWt;
                totalValue += curVal;
                sol[j++] = (double)curWt;
            }
            else {
                // Item cant be picked whole
                double fraction = ((double)capacidad / (double)curWt);
                totalValue += (curVal * fraction);
                capacidad = (int)(capacidad - (curWt * fraction));
                sol[j] = curWt * fraction;
                break;
            }
        }        
        return totalValue;
    }

    /*
     * camionFraccionadoDP Ejercicio 9
     * 
     * Resuelve el problema de la mochila fraccionada mediante programación dinámica para determinar
     * la selección óptima de elementos que maximiza el beneficio sin exceder la capacidad
     * máxima del camión.
     * 
     * Este método divide los objetos en fracciones y los almacena en una tabla dinámica para determinar
     * la selección óptima de elementos que maximiza el beneficio sin exceder la capacidad máxima del camión.
     * 
     * @param P La capacidad máxima del camión (peso máximo que puede llevar).
     * @param p Array de enteros donde cada elemento representa el peso de un item.
     * @param b Array de doubles donde cada elemento representa el beneficio de un item.
     * @param divisiones Número de divisiones para obtener un resultado más preciso.
     * @return El beneficio máximo que se puede obtener dado el peso y beneficio de los elementos.
     * 
     */
    public double camionFraccionadoDP(int P, int p[], double b[], int divisiones) {
        //Asignamos el tamaño del bucle i
        int n = b.length;

        //Creamos dos ArrayList para almacenar los valores de los pesos y beneficios
        ArrayList<Double> values = new ArrayList<>();
        ArrayList<Integer> weights = new ArrayList<>();

        //Realizamos un bucle que recorre el array de double
        for (int i = 0; i < n; i++) {
                //Asignamos el valor actual a la variable numero
                int peso = p[i];       
            //Realizamos un bucle que itera según el número de divisiones   
            for(int j = 0; j < divisiones; j++) {
                //Si el modulo del peso entre las divisiones es 0
                if(p[i]%divisiones == 0){
                    //Añadimos el peso y el beneficio al array directamente
                    weights.add(p[i] / divisiones);
                    values.add(b[i] / divisiones);
                    continue;
                }else{
                    //Si el modulo del peso entre las divisiones no es 0
                    //Comprobamos si el peso es menor que las divisiones y se añade directamente
                    //el objeto a los ArrayList
                    if(p[i] < divisiones){
                        weights.add(p[i]);
                        values.add(b[i]);
                        //Salimos del bucle
                        break;
                    }
                    //Si el peso es mayor que las divisiones
                    //Calculamos el cociente y el resto
                    int cociente = p[i] / divisiones;
                    int resto = p[i] % divisiones;
                    //Realizamos un bucle que itera según el cociente
                    for(int k = 0; k < cociente; k++){
                        //Añadimos el peso y el beneficio al array
                        weights.add(divisiones);
                        //El beneficio se calcula multiplicando el beneficio del objeto por las divisiones
                        //y dividiendo el resultado entre el peso del objeto
                        values.add(b[i]*divisiones / peso);
                    }
                    //Añadimos el resto del peso y el beneficio al array
                    //como un objeto fraccionado
                    weights.add(resto);
                    values.add(b[i]*resto / peso);
                    //Salimos del bucle
                    break;
                }       
                }

        }
        //Asignamos el tamaño del bucle i
        int n2 = values.size();
        //Creamos un array de double con tamaño P + 1
        double[] dp = new double[P + 1];
        
 
        //Realizamos un bucle que recorre el array de double
        for (int i = 1; i < n2; i++) {
            for (int w = P; w >= 0; w--) {
                //Si el peso es menor o igual al peso del camión
                if (weights.get(i-1) <= w)
                    //Asignamos el máximo entre el valor actual y el valor anterior
                    dp[w] = Math.max(dp[w - weights.get(i - 1)] + values.get(i - 1), dp[w]);
            }
        }
        
        return dp[P];
    }

    public void printSolucion() {
        int n = this.tabla.length - 1; // Total de elementos
        int[] sol = new int[n];   // Array para almacenar la solución
        int auxP = this.Capacidad;  // Comenzamos con la capacidad máxima del camión
    
        // Recorremos la tabla desde el último elemento hasta el primero
        for (int i = n; i > 0; i--) {
            // Verificamos si el valor actual es diferente al del objeto anterior con la misma capacidad
            if (this.tabla[i][auxP] != this.tabla[i-1][auxP]) {
                sol[i-1] = 1; // Marcamos este objeto como incluido
                auxP -= this.weight.get(i-1); // Reducimos la capacidad por el peso del objeto incluido
            } else {
                sol[i-1] = 0; // No incluimos este objeto
            }
        }
    
        // Verificamos si el primer objeto debe incluirse, asumiendo que el array de pesos y beneficios empieza desde índice 1
        if (auxP >= this.weight.get(0) && this.tabla[1][auxP] != 0) {
            sol[0] = 1; // El primer objeto es incluido si su peso es menor que la capacidad restante y su beneficio corresponde al calculado
        } else {
            sol[0] = 0; // No incluimos el primer objeto
        }
    
        // Imprimimos la solución en el formato solicitado
        System.out.print("{");
        for (int i = 0; i < sol.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(sol[i]);
        }
        System.out.println("}");
    }

    /**
     * showSolution
     * 
     * Muestra la solución óptima para el problema de la mochila.
     */
    public void showSolution() {
		System.out.println("==== CAPACIDAD [ "+ this.Capacidad +" ] ====");
		double profit = 0;
		int weight = 0;
		for (int i = 0; i < this.solucion.length; i++) {
			if(this.solucion[i] >= 1) {
				System.out.println(this.getProfit().get(i) + (this.solucion[i] != 1 ? " q="+this.solucion[i] : ""));
				profit += this.getProfit().get(i) * this.solucion[i];
				weight += this.getWeight().get(i) * this.solucion[i];
			}
		}
		System.out.println("BENEFICIO TOTAL: "+profit);
		System.out.println("PESO TOTAL: "+weight);
        System.out.println("===================================");
    }

    public void printArray(double[] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i]);
			if(i < array.length-1) {
				System.out.print(", ");
			}
		}
		System.out.println();
	}

    public void printTabla() {
		for (int i = 0; i < this.tabla.length; i++) {
			for (int j = 0; j < this.tabla[0].length; j++) {
				if(tabla[i][j] == -1.0) {
					System.out.printf("%-5s", "X");
				}else {
					System.out.printf("%-5.1f", tabla[i][j]);
				}
			}
			System.out.println();
		}
	}

  
    public double[][] getTabla() {
		return tabla;
	}

	public void setTabla(double[][] tabla) {
		this.tabla = tabla;
	}

	public int getCapacidad() {
		return Capacidad;
	}

	public void setCapacidad(int capacidad) {
		Capacidad = capacidad;
	}

	public ArrayList<Integer> getWeight() {
		return weight;
	}

	public void setWeight(ArrayList<Integer> weight) {
		this.weight = weight;
	}

	public ArrayList<Double> getProfit() {
		return profit;
	}

	public void setProfit(ArrayList<Double> profit) {
		this.profit = profit;
	}
	
	public int[] getSolucion() {
		return solucion;
	}

	public void setSolucion(int[] solucion) {
		this.solucion = solucion;
	}

    public void setAux(double[] aux) {
        this.aux = aux;
    }

    public double[] getAux() {
        return aux;
    }

    public static void main(String[] args) {
        int P = 20;
        int p[] = {3, 6, 7, 1, 4, 5, 1, 3};
        double b[] = {8.0, 8.0, 2.0, 3.0, 4.0, 3.0, 2.0, 5.0};

        DynamicProgramming dp = new DynamicProgramming(new double[0][0]);
        dp.setSolucion(new int[b.length]);
        System.out.println("-------------------------------------");
        System.out.println("Caso camionRecursive Ejercicio 1:");
        System.out.println("Beneficio: " + dp.camionRecursive(b.length, P, p, b));

        System.out.println("Caso camionDP2:");
        System.out.println("-------------------------------------");
        System.out.println("Beneficio: " + dp.camionDP2(P, p, b, true));

        System.out.println("Caso camionDP3:");
        System.out.println("-------------------------------------");
        System.out.println("Beneficio: " + dp.camionDP3(P, p, b));

        System.out.println("-------------------------------------");
        System.out.println("Caso camionTable:");
        System.out.println("Beneficio: " + dp.camionTable(b.length, P, p, b));

        System.out.println("-------------------------------------");
        System.out.println("Caso camionDP:");
        System.out.println("Beneficio: " + dp.camionDP(P, p, b));
        
        System.out.println("-------------------------------------");
        System.out.println("Caso camionDPMP:");
        System.out.println("Beneficio: " + dp.camionDPMP(P, p, b));
        int [] test = dp.obtenerSolucion(P, p, b);

        System.out.println("Solución: ");
        for (int i = 0; i < test.length; i++) {
            System.out.println("Objeto " + i + ": " + test[i]);
        }

        System.out.println("-------------------------------------");
        System.out.println("Caso mochilaFraccionada:");
        System.out.println("Beneficio: " + dp.camionFraccionadoDP(P, p, b, 3));
        
        System.out.println("-------------------------------------");
        System.out.println("Caso camionFraccionadoGreedy:");

        //Creando un array de objetos tipo beneficio/peso
        Objeto[] arr = new Objeto[p.length];
        for(int i = 0; i < p.length; i++) {
            arr[i] = new Objeto((int)b[i], p[i]);
        }

        System.out.println("Beneficio: " + dp.camionFraccionadoGreedy(arr, P));

    }
}
