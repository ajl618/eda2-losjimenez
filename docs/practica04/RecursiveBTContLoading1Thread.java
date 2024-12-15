
/** first recursive backtracking code to load containers onto one ship
  * code finds weight of max loading for the ship */
/**
 * SE IMPLEMENTA LA AMPLIACIÓN CON Thread PARA EL PROCESAMIENTO PARALELO.
 */

package org.eda2.practica4;

public class RecursiveBTContLoading1Thread extends Thread{
	static int numberOfContainers;
	static int [] weight;
	static int capacity;
	static int weightOfCurrentLoading;
	static int maxWeightSoFar;
	static int NúmeroDeHilo = 0;
   int pesoActual;
   int máximoConseguido;
   int nivel;
   int procesadores;
   int hilo;
	
   public RecursiveBTContLoading1Thread(int actual, int nivel, int cpus) {
	// TODO Auto-generated constructor stub
	   pesoActual = actual;
	   máximoConseguido = 0;
	   this.nivel = nivel;
	   this.procesadores = cpus;
   }
	/** @param theWeight array of container weights
     * @param theCapacity capacity of the ship
     * @return weight of max loading */
	public static int maxLoading(int [] theWeight, int theCapacity) {
		numberOfContainers = theWeight.length - 1;
		weight = theWeight;
		capacity = theCapacity;
		weightOfCurrentLoading = 0;
		maxWeightSoFar = 0;
		
		rContLoad(0);
		return maxWeightSoFar;
	}
   
	private int paraleliza(int nivel, int cpus) {
		// TODO Auto-generated method stub
		RecursiveBTContLoading1Thread r1 = null, r2 = null;
		if(cpus < 1) {
			System.out.println("Se ha llamado un proceso sin hilos disponibles " + cpus);
			return capacity;
		}
		else if(cpus == 1) {
			rContLoadParalelo(nivel);
			
			return máximoConseguido;
		}
		//Ajusto nivel a elemento que quepa.
		while(nivel < (weight.length))
			if(weight[nivel] > capacity) 
				nivel++;
			else break;
		if(nivel == weight.length) {
			System.out.println("No quedan elementos disponibles.\n");
			
			return pesoActual;//Lo que ya se ha metido.
		}
		r1 = new RecursiveBTContLoading1Thread(
				pesoActual + weight[nivel],
				nivel + 1, 
				cpus / 2);
		r1.hilo = NúmeroDeHilo++;

		r2 = new RecursiveBTContLoading1Thread(
				pesoActual + 0,
				nivel + 1, 
				cpus / 2);
		r2.hilo = NúmeroDeHilo++;
		
		r1.start();
		r2.start();
		try {
			r1.join();
			r2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		máximoConseguido = r1.máximoConseguido > r2.máximoConseguido ? r1.máximoConseguido : r2.máximoConseguido;

		return máximoConseguido;
	}

	 @Override
	 public void run() {
		 paraleliza(this.nivel, this.procesadores);
	 }
	 
	public static int máximaCargaParalelo(int [] pesos, int theCapacity) {
		numberOfContainers = pesos.length;
		weight = pesos;
		capacity = theCapacity;
		weightOfCurrentLoading = 0;
		maxWeightSoFar = 0;
		RecursiveBTContLoading1Thread p1 = null, p2 = null;
		int nivel = 0;
		int cpus = Runtime.getRuntime().availableProcessors();
		//Los paso a potencia de 2 al ser un problema binario.
		int potencia = 1;
		while((cpus /= 2) >= 1) {
			potencia *= 2;
		}
		cpus = potencia;
		//Se ajusta el nivel para el primero que quepa.
		while(nivel < (weight.length))
			if(weight[nivel] > theCapacity) 
				nivel++;
			else break;
		if(nivel == weight.length) {
			System.out.println("No quedan elementos disponibles.\n");
			
			return 0;//No se ha podido meter nada.
		}
		if(nivel == pesos.length) {
			System.out.println("No hay artículos para meter.");
			return 0;
		}
		//Se lanzan ya ambos. Se ha realizado el nivel 0.
		p1 = new RecursiveBTContLoading1Thread(//Se mete.
			pesos[0],
			1,
			cpus / 2);
		p1.hilo = NúmeroDeHilo++;
		p2 = new RecursiveBTContLoading1Thread(//No se mete.
			0,
			1,
			cpus / 2);
		p2.hilo = NúmeroDeHilo++;
		p1.start();
		p2.start();
		try {
			p1.join();
			p2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Math.max(p1.máximoConseguido, p2.máximoConseguido);
	}
   
	/** actual method to find max loading */
	private static void rContLoad(int currentLevel) {
		if (currentLevel > numberOfContainers) {
			if (weightOfCurrentLoading > maxWeightSoFar)
				maxWeightSoFar = weightOfCurrentLoading;
			return;
		}
		if (weightOfCurrentLoading + weight[currentLevel] <= capacity) {
			weightOfCurrentLoading += weight[currentLevel];
			rContLoad(currentLevel + 1);
			weightOfCurrentLoading -= weight[currentLevel];
		}
		rContLoad(currentLevel + 1);
	}

	/** actual method to find max loading en PARALELO */
	private void rContLoadParalelo(int currentLevel) {
		if (currentLevel >= numberOfContainers) {
			if (pesoActual > máximoConseguido)
				máximoConseguido = pesoActual;
			return;
		}
		if (pesoActual + weight[currentLevel] <= capacity) {
			pesoActual += weight[currentLevel];
			rContLoadParalelo(currentLevel + 1);
			pesoActual -= weight[currentLevel];
		}
		rContLoadParalelo(currentLevel + 1);
	}

	public static int[] inicializa(int cantidad, int mínimo, int máximo) {
		int[] salida = new int[cantidad];
		
		for(int i = 0; i < cantidad; ++i)
			salida[i] = (int)(Math.random()*(máximo - mínimo) + mínimo);
		
		return salida;
	}
	
	//Inserta un 0 al principio para que se pueda hacer el iterativo en igualdad
	public static int[] inserta0(int[] v) {
		int[] t = new int[v.length + 1];
		
		for(int i=1; i < t.length; ++i)
			t[i] = v[i - 1];
		
		return t;
	}
	
	//Para sumar el vector
	public static int suma(int[] v) {
		int t = 0;
		
		for(int i = 0;i < v.length; ++i) t += v[i];
		
		return t;
	}
	
	public static void escribeVector(int[] v) {
		System.out.print("El vector es {");
		for(int i=0;i<v.length;++i) {
			if(i > 0) System.out.print(", ");
			System.out.print(v[i]);
		}
		System.out.println("}");
	}
	
	/** test program */
	public static void main(String [] args) {
		int [] w = {0, 8, 6, 2, 3};
		int C = 12, cantidad = 32;
		int Carga = 1600;
		int s;
		int [] v = inicializa(cantidad, 65, 120).clone();
		escribeVector(v);
		s = suma(v) - 60;//Para que no sea siempre la carga máxima.
		System.out.println("La capacidad de la carga es " + s +
				" y la suma de artículos es " + (s + 60));
		int[] v0 = inserta0(v).clone();
		long tiempo = System.currentTimeMillis();
		System.out.println("Value of max loading para un hilo is " + maxLoading(v0, s));
		System.out.printf("Se ha tardado %,.0f ms para %d elementos en UN núcleo.\n"
				,(float) (System.currentTimeMillis() - tiempo)
				, cantidad);
		tiempo = System.currentTimeMillis();
		System.out.println("El valor de la carga máxima PARALELO es " + máximaCargaParalelo(v, s));
		System.out.printf("Se ha tardado %,.0f ms para %d elementos en " +
				Runtime.getRuntime().availableProcessors() + " núcleos.\n"
				,(float) (System.currentTimeMillis() - tiempo)
				, cantidad);
	}
}
