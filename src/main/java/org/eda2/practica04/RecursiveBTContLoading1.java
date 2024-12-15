
/** first recursive backtracking code to load containers onto one ship
  * code finds p of max loading for the ship */

package org.eda2.practica04;

/**
 * RecursiveBTContLoading1
 * 
 * Clase que implementa el algoritmo de backtracking recursivo para el problema
 * de la carga contenerizada.
 * En esta se desarrollará la explicación para la realización del Ejercicio 2 de
 * la práctica.
 *
 * @author Profesores
 */
public class RecursiveBTContLoading1 {
	static int n;
	static int[] p;
	static int C;
	static int pact;
	static int voa;

	/**
	 * maxLoading
	 * 
	 * Este método es el que se encarga de realizar la llamada al método
	 * recursivo que se encargará de realizar el backtracking para encontrar la
	 * solución al problema de la carga contenerizada.
	 * Se inicializan las variables globales que se utilizarán en el método
	 * recursivo.
	 * 
	 * 
	 * @param pesos   array of container weights
	 * @param capacidad C of the ship
	 * @return p of max loading
	 */
	public static int maxLoading(int[] pesos, int capacidad) {
		n = pesos.length - 1;
		p = pesos;
		C = capacidad;
		pact = 0;
		voa = 0;

		// llamada al método recursivo, con el nivel 1
		rContLoad(1);

		// se devuelve el valor de la carga máxima
		return voa;
	}

	/**
	 * rContLoad
	 * 
	 * Este método es el encargado de realizar el backtracking recursivo para
	 * encontrar la solución al problema de la carga contenerizada.
	 * Se recibe el nivel actual en el que se encuentra el algoritmo.
	 * 
	 * 
	 * Algoritmo:
	 * 1. Si currentLevel es mayor que n, se actualiza voa si pact es mayor.
	 * En este caso, se termina la llamada recursiva, ya que se ha llegado al
	 * final de la carga.
	 * 2. Si incluir el contenedor actual no excede la capacidad C, se incluye
	 * temporalmente, se llama recursivamente al siguiente nivel, y luego se excluye,
	 * para seguir con la siguiente llamada recursiva y realizar el backtracking.
	 * 3. Independientemente de la inclusión, se llama recursivamente al siguiente
	 * nivel. Para el caso en el que no se incluya el contenedor actual.
	 * 
	 * Podemos verlo como un arbol binario, en el que en cada nivel se toma una
	 * decisión, incluir o no incluir el contenedor actual.
	 * 
	 * 
	 * @param currentLevel El nivel actual del backtracking, que indica el índice del contenedor
	 * que se está considerando.
	 */
	private static void rContLoad(int currentLevel) {
		// si se ha llegado al final de la carga
		if (currentLevel > n) {
			if (pact > voa)
				voa = pact;
			return;
		}

		// incluir el contenedor actual 1
		if (pact + p[currentLevel] <= C) {
			pact += p[currentLevel];
			rContLoad(currentLevel + 1);
			// backtracking
			pact -= p[currentLevel];
		}

		// no incluir el contenedor actual 0
		rContLoad(currentLevel + 1);
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
