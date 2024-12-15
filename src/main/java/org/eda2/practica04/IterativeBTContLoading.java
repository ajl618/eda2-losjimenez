package org.eda2.practica04;
public class IterativeBTContLoading {
	static int n; //número de objetos
	static int [] p; //Array de pesos de los objetos
	static int P; //Capacidad de la mochila o camión
	static int pact; //Peso acumulado
	static int voa; //Valor óptimo actual
	static int [] soa;

	/*
	 * maxloading
	 * 
	 * Método que implementa el algoritmo de backtracking iterativo para resolver el problema de la carga de un camión.
	 * Reduce las necesidades de memoria y tiempo de ejecución del algoritmo de backtracking recursivo debido
	 * a la eliminación de la pila de recursividad.
	 * 
	 * @param pesos Array de enteros donde cada entero representa el peso de un objeto.
	 * @param capacidad Entero que representa la capacidad máxima de peso que el camión puede cargar.
	 * @return Un entero que representa el peso máximo que el camión puede cargar.
	 * 
	 */
	public static int maxLoading(int [] pesos, int capacidad) {

		// Inicialización de variables
		// Número de objetos
		n = pesos.length;
		// Array de pesos de los objetos
		p = pesos;
		// Capacidad del camión
		P = capacidad;
		// Peso acumulado actual
		pact = 0;
		// Valor óptimo actual
		voa = 0;
		// Array de solución óptima actual
		soa = new int [p.length];
		int pesoRestante = 0;
		// Calculamos el peso total de los objetos
		for (int i = 0; i <n; i++) {
			pesoRestante += p[i];
			
		}
		// Llamada al método backTrackingIterative
		backTrackingIterative(pesoRestante); 
		// Devolvemos el valor óptimo de la carga del camión
		return voa;
	}
	
	/*
	 * backTrackingIterative
	 * 
	 * Método que implementa el algoritmo de backtracking iterativo para resolver el problema de la carga de un camión.
	 * Reduce las necesidades de memoria y tiempo de ejecución del algoritmo de backtracking recursivo debido
	 * a la eliminación de la pila de recursividad.
	 * 
	 * @param pesoRestante Entero que representa el peso restante de los objetos que se pueden cargar en el camión.
	 * 
	 */
	private static void backTrackingIterative(int pesoRestante) {
		//El nivel es el objeto que estamos considerando
		int nivel = 0;
		//Array de solución
		int s [] = new int[p.length];
		//Inicializamos el array de solución a -1
		for(int i = 0; i<p.length ; i++) {
			s[i] = -1;
		}
		//Mientras el nivel sea mayor o igual a -1
		do {
			//Incrementamos el nivel
			s[nivel]++;
			//Se añade el peso del objeto al peso actual
			if(s[nivel] == 1) {
				pact+=p[nivel];
			}else {
				//Si el objeto no se añade al camión se resta el peso del objeto al peso actual
				pesoRestante -=p[nivel];
			}
			
			//Si el nivel es igual al número de objetos y el peso acumulado es menor o 
			//igual a la capacidad del camión 
			//y el peso acumulado es mayor que el valor óptimo actual
			if(nivel == n-1 && pact<=P && pact>voa) {
				//El valor óptimo actual es igual al peso acumulado
				voa = pact;
			}
			
			//Si el nivel es menor que el número de objetos y el peso acumulado es menor o igual a la capacidad del camión
			if(nivel<n-1 && pact<=P && pact + pesoRestante > voa) {
				//Incrementamos el nivel
				nivel++;
				//Si no, mientras el nivel sea mayor o igual a 0 y el objeto no se añade al camión
			}else while (nivel>= 0 && s[nivel] == 1) {
				//Si el objeto se añade al camión
				if(s[nivel] == 1 ) {
					//Se resta el peso del objeto al peso acumulado
					pact-=p[nivel];
					//Se suma el peso del objeto al peso restante
					pesoRestante +=p[nivel];
				}
				//El objeto no se añade al camión
				s[nivel] = -1;
				//Decrementamos el nivel
				nivel--;
			}
			
		} while (nivel > -1);
		
		
	}

	/** test program */
	public static void main(String[] args) {
		int[] w = { 0, 8, 6, 2, 3 };
		int C = 12;
		// int [] w = {0, 7, 2, 6, 5, 4};
		// int C = 10;
		// int [] w = {0, 2, 2, 6, 5, 5};
		// int C = 16;
		// int [] w = {0, 2, 2, 6, 5, 5, 3, 1, 4, 7, 9, 8, 5, 5};
		// int C = 45;

		System.out.println("Value of max loading is " + maxLoading(w, C));
	}




}
