package org.eda2.practica04;

public class RecursiveBTContLoading3 {

	static int n;
	static int[] p;
	static int C;
	static int pact;
	static int voa;
	static int pesoRestante;
	static int[] soa;
	static int[] s;

	/**
	 * maxLoading
	 * 
	 * Este método es el que se encarga de realizar la llamada al método
	 * recursivo que se encargará de realizar el backtracking para encontrar la
	 * solución al problema de la carga contenerizada.
	 * Se inicializan las variables globales que se utilizarán en el método
	 * recursivo.
	 * 
	 * Con esta versión del algoritmo, se añade una variable que guarda el peso
	 * restante de los contenedores que quedan por cargar y se modifica la
	 * condición de poda para que no se sigan explorando ramas que no van a
	 * mejorar la solución.
	 * 
	 * 
	 * @param pesos     array of container weights
	 * @param capacidad C of the ship
	 * @return p of max loading
	 */
	public static int maxLoading(int[] pesos, int capacidad) {
		n = pesos.length - 1;
		p = pesos;
		C = capacidad;
		pact = 0;
		voa = 0;

		s = new int[n];
		soa = new int[n];

		for (int i = 1; i < p.length; i++) {
			pesoRestante += p[i];
		}

		rContLoad(1, pesoRestante);

		return voa;
	}

	/**
	 * rContLoad
	 * 
	 * Realiza un backtracking recursivo optimizado para resolver el problema de la
	 * carga contenerizada, almacenando la mejor solución encontrada.
	 *
	 * Este método utiliza una estrategia de poda para evitar explorar ramas no
	 * prometedoras y almacena la mejor combinación de contenedores encontrada hasta el momento.
	 *
	 * * Algoritmo:
	 * 1. Si el peso acumulado actual más el peso restante no puede mejorar la mejor
	 * solución encontrada (voa),
	 * se descarta la rama actual.
	 * 2. Si currentLevel es mayor que n, se ha evaluado una combinación completa de
	 * contenedores. Si el peso
	 * acumulado actual (pact) es mayor que voa, se actualiza voa y se almacena la
	 * combinación actual en soa.
	 * 3. Si incluir el contenedor actual no excede la capacidad C, se incluye
	 * temporalmente, se marca en el array
	 * de soluciones actuales (s), se llama recursivamente al siguiente nivel, y
	 * luego se revierte la inclusión.
	 * 4. Independientemente de la inclusión, se llama recursivamente al siguiente
	 * nivel para evaluar la posibilidad
	 * de no incluir el contenedor actual.
	 * 
	 * @param currentLevel El nivel actual del backtracking, que indica el índice
	 *                     del contenedor
	 *                     que se está considerando.
	 * @param pesoRestante La suma de los pesos de los contenedores que aún no han
	 *                     sido considerados.
	 *
	 * 
	 */
	private static void rContLoad(int currentLevel, int pesoRestante) {
		// Si el peso actual más el peso restante es menor o igual que el valor de la
		// mejor solución encontrada hasta el momento
		// no seguimos con la rama actual
		if (pact + pesoRestante <= voa)
			return;

		if (currentLevel > n) {
			// si se ha llegado al final de la carga y se actualiza la mejor solución 
			if (pact > voa)
				voa = pact;

			// copiar la solución actual, si es mejor que la anterior, en el array soa
			for (int i = 0; i < n; i++) {
				soa[i] = s[i];
			}
			return;
		}

		if (pact + p[currentLevel] <= C) {
			pact += p[currentLevel];

			// si se incluye el contenedor actual, se marca en el array de soluciones con un 1 ya que se incluye
			s[currentLevel - 1] = 1;
			// se llama recursivamente al siguiente nivel
			rContLoad(currentLevel + 1, pesoRestante - p[currentLevel]);
			// backtracking
			pact -= p[currentLevel];
			s[currentLevel - 1] = 0;
		}
		rContLoad(currentLevel + 1, pesoRestante - p[currentLevel]);
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
