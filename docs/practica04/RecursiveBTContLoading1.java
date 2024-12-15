
/** first recursive backtracking code to load containers onto one ship
  * code finds weight of max loading for the ship */

package org.eda2.practica4;

public class RecursiveBTContLoading1 {
	static int numberOfContainers;
	static int [] weight;
	static int capacity;
	static int weightOfCurrentLoading;
	static int maxWeightSoFar;
   
	
	/** @param theWeight array of container weights
     * @param theCapacity capacity of the ship
     * @return weight of max loading */
	public static int maxLoading(int [] theWeight, int theCapacity) {
		numberOfContainers = theWeight.length - 1;
		weight = theWeight;
		capacity = theCapacity;
		weightOfCurrentLoading = 0;
		maxWeightSoFar = 0;
   
		rContLoad(1);
		
		return maxWeightSoFar;
	}
   
	/** actual method to find max loading */
	private static void rContLoad(int currentLevel) {
		if (currentLevel > numberOfContainers) {
			if (weightOfCurrentLoading > maxWeightSoFar)
				maxWeightSoFar = weightOfCurrentLoading;
			return;
		}

		suma = aqui se agrega

		if (weightOfCurrentLoading + weight[currentLevel] <= capacity) {
			weightOfCurrentLoading += weight[currentLevel];
			rContLoad(currentLevel + 1);
			weightOfCurrentLoading -= weight[currentLevel];
		}
		rContLoad(currentLevel + 1);
		suma = aqui se ha echo backtrancking, se devuelve
	}


	private static void rContLoad(int currentLevel) {
		if (currentLevel > numberOfContainers) {
			// if (weightOfCurrentLoading > maxWeightSoFar)
				// maxWeightSoFar = weightOfCurrentLoading;
			// return;

			MejorSolucion = solucion.clone(); //sin el clone no funca

		}
		
		if (weightOfCurrentLoading + weight[currentLevel] <= capacity) {
			weightOfCurrentLoading += weight[currentLevel];
			
			suma[currentLevel] = 1;
			rContLoad(currentLevel + 1);
			suma[currentLevel] = 0;
			weightOfCurrentLoading -= weight[currentLevel];
		}

		suma[currentLevel] = 1;
		rContLoad(currentLevel + 1);
		suma[currentLevel] = 0;
	}



	/** test program */
	public static void main(String [] args) {
		int [] w = {0, 8, 6, 2, 3};
		int C = 12;
		//int [] w = {0, 7, 2, 6, 5, 4};
		//int C = 10;
		//int [] w = {0, 2, 2, 6, 5, 5};
		//int C = 16;
		//int [] w = {0, 2, 2, 6, 5, 5, 3, 1, 4, 7, 9, 8, 5, 5};
		//int C = 45;
		
		System.out.println("Value of max loading is " + maxLoading(w, C));
	}
}
