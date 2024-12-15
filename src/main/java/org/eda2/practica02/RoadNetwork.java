package org.eda2.practica02;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Clase RoadNetwork
 * 
 * Clase que implementa los algoritmos diseñados para resolver los distintos
 * ejercicios
 * propuestos en la práctica. Se implementarán los algoritmos para:
 * Ejercicio1) Algoritmo de Prim con las dos versiones solicitadas (con y sin
 * cola de prioridad),
 * Ejercicio2) Algoritmo de Kruskal con cola de prioridad,
 * Ejercicio3) resuelva el problema planteado (revisión de la red
 * por la unidad de mantenimiento). Éste consiste en encontrar el circuito con
 * la distancia más corta
 * (problema del viajante de comercio utilizando caminos simples) que debe
 * seguir la unidad de
 * mantenimiento, partiendo desde Almería, y visitando todas las ciudades que
 * tiene una centralita
 * exactamente una vez y regresar a Almería. Este algoritmo se probará su
 * correcto funcionamiento
 * únicamente en la nueva red de telecomunicaciones reducida
 * 
 * Todos los algoritmos se probarán con los distintso dataset facilitados por
 * los profesores y por los creados
 * por nuestra clase generadora.
 * 
 * @author Adrián Jiménez Benitez
 * @author Antonio José Jiménez Luque
 * 
 * @version 1.0
 */
public class RoadNetwork extends Network {

	// Constantes
	//Tamaño del tipo de datos INT en bytes
	private static final int INT_SIZE_BYTES = 4;
	//Tamaño del tipo de datos STRING en bytes
	private static final int STRING_SIZE_BYTES = 4;
	//Tamaño del tipo de datos DOUBLE en bytes
	private static final int DOUBLE_SIZE_BYTES = 8;
	//ArrayList solucion para el algoritmo de TSPBackTracking
	private static ArrayList<String> solucion = new ArrayList<String>();
	//Variable minimo para el algoritmo de TSPBackTracking
	private static Double minimo = Double.MAX_VALUE;
	//Variable para el algoritmo de TSPBackTracking
	private double pesoBackTracking = 0.0;
	//Variable para el algoritmo de TSPGreedy
	private double pesoGreedy = 0.0;
	//Variable para el origen del grafo
	private String origen;

	//Path
	private String carpeta = System.getProperty("user.dir") + File.separator +
			"src" + File.separator +
			"main" + File.separator +
			"java" + File.separator +
			"org" + File.separator +
			"eda2" + File.separator +
			"practica02" + File.separator +
			"dataset" + File.separator;

	/**
	 * Constructor
	 */
	public RoadNetwork() {
		super();
	}

	/**
	 * Secciones
	 * 
	 * Enumerado que contiene las distintas secciones que se pueden encontrar en el
	 * archivo de texto
	 */
	private enum Secciones {
		Type, Vertex, Edge;

		void load(RoadNetwork net, String linea) {
			switch (this) {
				case Type:
					net.setDirected(linea.contains("0") ? false : true);
					break;
				case Vertex:
					net.addVertex(linea.trim());
					break;
				case Edge:
					String[] items = linea.trim().split("[ \t]+");
					net.addEdge(items[0].trim(), items[1].trim(), Double.valueOf(items[2].trim()));
					break;
			}

		}
	}

	/**
	 * loadNetwork
	 * 
	 * Método que carga la red de a partir de un archivo de texto
	 * 
	 * @param filename
	 * @return
	 */
	public boolean loadNetwork(String filename) {
		Scanner scan = null;
		Secciones seccion = null;
		this.adjacencyMap.clear();
		try {
			scan = new Scanner(new File(filename));
		} catch (Exception e) {
			return false;
		}
		String line = scan.nextLine().trim();

		// Primera línea se le asigna 0 o 1 dependiendo de si es dirigido o no
		seccion = Secciones.Type;
		seccion.load(this, line);

		// Lee el número de vértices
		line = scan.nextLine().trim();
		int numVertices = Integer.valueOf(line);

		// Bucle para leer los vértices
		for (int i = 0; i < numVertices; i++) {
			line = scan.nextLine().trim();
			seccion = Secciones.Vertex;
			seccion.load(this, line);
		}

		// Lee el número de aristas
		line = scan.nextLine().trim();
		int numAristas = Integer.valueOf(line);

		for (int i = 0; i < numAristas; i++) {
			line = scan.nextLine().trim();
			seccion = Secciones.Edge;
			seccion.load(this, line);
		}

		scan.close();
		return true;
	}

	/*
	 * Algoritmos
	 */

	/**
	 * Prim
	 * 
	 * Implementa el algoritmo de Prim para encontrar el árbol de expansión mínima
	 * (MST) de un grafo no dirigido. Esta versión del algoritmo utiliza una lista
	 * para almacenar las aristas
	 * candidatas y selecciona manualmente la arista de menor peso en cada iteración
	 * para añadirla al MST.
	 * 
	 * El método inicia desde un vértice dado y expande el MST añadiendo la arista
	 * de menor peso que conecte el árbol con un vértice no incluido previamente.
	 * Este proceso se repite hasta
	 * que el MST conecte todos los vértices del grafo (n-1 aristas para un grafo de
	 * n vértices) o se identifique
	 * que el grafo está desconectado.
	 * 
	 * Se asume que el grafo es no dirigido y que todas las aristas tienen un peso
	 * asignado. El algoritmo verifica si el grafo está completamente conectado; si
	 * detecta que no es
	 * posible continuar la expansión sin formar ciclos, lanza una excepción.
	 * 
	 * @param origen el vértice desde el cual se inicia la construcción del MST.
	 *               Este vértice debe existir
	 *               en el grafo. Si el vértice es null o no existe, el
	 *               comportamiento del método no está definido.
	 * @throws RuntimeException si el método no puede encontrar una arista válida
	 *                          para continuar la construcción
	 *                          del MST, lo cual suele indicar que el grafo está
	 *                          desconectado.
	 */
	public void Prim(Boolean visual) {

		if (this.origen == null) {
			randomOrigen();
		}

		// Inicializar las estructuras de datos
		ArrayList<Arista> solucion = new ArrayList<Arista>();
		ArrayList<Arista> candidatos = new ArrayList<Arista>();
		TreeSet<String> usadas = new TreeSet<String>();

		usadas.add(this.origen);
		String actual = this.origen;

		/*
		 * Mientas que la solucion sea menor que el número de vértices - 1 (origen)
		 */
		while (solucion.size() < this.numberOfVertices() - 1) {

			// Añadir los candidatos a la lista de candidatos (si no están en usadas)
			for (String destino : this.getNeighbors(actual)) {
				// Si ya está en usadas , no se tiene en cuenta
				if (usadas.contains(destino))
					continue;
				double peso = this.getWeight(actual, destino);
				candidatos.add(new Arista(actual, destino, peso));
			}

			// Seleccionar el candidato con menor peso
			Arista menor = null;
			for (Arista arista : candidatos) {
				if (menor == null || arista.getPeso() < menor.getPeso()) {
					menor = arista;
				}
			}

			if (menor == null) {
				throw new RuntimeException(
						"No se pudo encontrar una arista válida. Esto puede deberse a un grafo desconectado.");
			}

			// Añadir la arista a la solución, borramos de candidatos y añadimos a usadas
			candidatos.remove(menor);
			solucion.add(menor);
			usadas.add(menor.getDestino());
			actual = menor.getDestino(); // Aquí actualizamos `actual` con el nuevo destino

			// Comprobamos si se formaria un ciclo
			Iterator<Arista> it = candidatos.iterator();
			while (it.hasNext()) {
				Arista arista = it.next();
				if (usadas.contains(arista.getOrigen()) && usadas.contains(arista.getDestino()))
					it.remove();

			}

		}

		if (visual) {
			System.out.println("**************** Generando archivo para visualizar ****************");
			GenerateGraph gg = new GenerateGraph();
			gg.SaveVisualizateResultGraph(this, solucion);
			System.out.println();
			System.out.println();
			System.out.println("Se ha creado el .dot de manera satisfactoria.");
			System.out.println();
			System.out.println();
			System.out.println("--------------------- Solucion ----------------------");
			System.out.println();
			this.mostrarSolucion(solucion);

		}

	}

	/**
	 * PrimPriorityQueue
	 * 
	 * Calcula el árbol de expansión mínima (Minimum Spanning Tree, MST) para un
	 * grafo no dirigido utilizando el algoritmo de Prim. Este método emplea una
	 * cola de prioridad
	 * para seleccionar en cada paso la arista de menor peso que conecta un nuevo
	 * vértice al árbol en
	 * construcción.
	 * 
	 * El algoritmo comienza desde un vértice de origen dado y, de manera iterativa,
	 * expande el MST añadiendo la arista de menor peso que conecte el árbol con un
	 * vértice aún no
	 * incluido. Este proceso se repite hasta que el árbol de expansión mínima
	 * incluya todos los vértices
	 * del grafo (n-1 aristas para un grafo de n vértices), o se determine que el
	 * grafo no está
	 * completamente conectado.
	 * 
	 * 
	 * El método asume que el grafo sobre el cual se invoca es no dirigido y que
	 * todas las aristas tienen pesos asignados. Además, se asume que el grafo está
	 * completamente
	 * conectado; sin embargo, el método maneja el caso en que el grafo no lo esté,
	 * terminando la ejecución
	 * prematuramente si encuentra que no puede conectar más vértices sin formar
	 * ciclos.
	 * 
	 * @param origen el vértice desde el cual comenzar la construcción del MST. Este
	 *               vértice debe existir
	 *               en el grafo; de lo contrario, el método no funcionará
	 *               correctamente.
	 * @throws NullPointerException  si el vértice de origen es null o no existe en
	 *                               el grafo.
	 * @throws IllegalStateException si el grafo está vacío o desconectado,
	 *                               impidiendo la formación de un MST completo.
	 */

	public void PrimPriorityQueue(Boolean visual) {

		if (this.origen == null) {
			randomOrigen();
		}

		ArrayList<Arista> solucion = new ArrayList<Arista>();
		PriorityQueue<Arista> candidatos = new PriorityQueue<Arista>(Comparator.comparing(Arista::getPeso));
		TreeSet<String> usadas = new TreeSet<String>();

		usadas.add(this.origen);

		/*
		 * Mientas que la solucion sea menor que el número de vértices - 1 (origen)
		 */
		while (solucion.size() < this.numberOfVertices() - 1) {
			for (String destino : this.getNeighbors(origen)) {
				if (usadas.contains(destino))
					continue;
				double peso = this.getWeight(origen, destino);
				candidatos.add(new Arista(origen, destino, peso));
			}

			Arista menor;
			do {
				menor = candidatos.poll();
				if (menor == null) {
					System.out.println("El grafo puede estar desconectado.");
					return;
				}
			} while (usadas.contains(menor.getDestino()));

			solucion.add(menor);
			usadas.add(menor.getDestino());
			origen = menor.getDestino(); // Actualizar el origen para la próxima iteración
		}

		if (visual) {
			System.out.println("**************** Generando archivo para visualizar ****************");
			GenerateGraph gg = new GenerateGraph();
			gg.SaveVisualizateResultGraph(this, solucion);
			System.out.println();
			System.out.println();
			System.out.println("Se ha creado el .dot de manera satisfactoria.");
			System.out.println();
			System.out.println();
			System.out.println("--------------------- Solucion ----------------------");
			System.out.println();
			this.mostrarSolucion(solucion);

		}

	}

	/**
	 * TSPGreedy
	 * 
	 * Implementa el algoritmo greedy para resolver el problema del viajante de
	 * comercio (TSP) en un grafo no dirigido y completo. El algoritmo comienza en
	 * un vértice dado y selecciona en cada paso el vértice más cercano que no haya
	 * sido visitado previamente. El proceso se repite hasta que todos los vértices
	 * hayan sido visitados y se regrese al vértice de origen.
	 * 
	 * @param origen El vértice de inicio del recorrido.
	 * @throws RuntimeException Si no es posible encontrar una solución o no se
	 *                          puede obtener el peso de una arista.
	 */
	@SuppressWarnings("unused")
	public void TSPGreedy(Boolean visual) {

		if (this.origen == null) {
			randomOrigen();
		}

		// Inicializamos las estructuas de datos
		ArrayList<String> visitados = new ArrayList<String>();
		ArrayList<Arista> solucion = new ArrayList<Arista>();
		String actual = this.origen;

		// Agregamos el origen a visitados
		visitados.add(this.origen);

		// Mientras que el tamaño de visitados se menor que el numero de vertices del
		// grafo iteramos
		while (visitados.size() < this.numberOfVertices()) {
			// Buscamos del actual el vecino mas cercano
			String next = vecinoMasCercano(visitados, actual);

			/*
			 * Si el siguiente es nulo, lanzamos expecion
			 */
			if (next == null) {
				System.out.println("No hay solución");
				return;
			}

			// Agregamos la arista a la solucion, agregamos el vecino a visitados y
			// actualizamos el actual con el vecino
			solucion.add(new Arista(actual, next, this.getWeight(actual, next)));
			visitados.add(next);
			actual = next;

		}

		// Obtenemos el peso del regreso, en caso de que no se pueda obtener lanzamos la
		// excepcion.
		Double fin = this.getWeight(actual, this.origen);
		solucion.add(new Arista(actual, this.origen, fin));
		if (fin == null) {
			System.out.println("No hay solución");
			return;
		}

		// Mostramos la solucion
		if (visual) {
			System.out.println("**************** Generando archivo para visualizar ****************");
			GenerateGraph gg = new GenerateGraph();
			gg.SaveVisualizateResultGraph(this, solucion);
			System.out.println();
			System.out.println();
			System.out.println("Se ha creado el .dot de manera satisfactoria.");
			System.out.println();
			System.out.println();
			System.out.println("--------------------- Solucion ----------------------");
			System.out.println();
			this.mostrarSolucion(solucion);
		}else {
			
			double peso_count = 0.0;
			for (Arista arista : solucion) {
				peso_count += arista.getPeso();
			}
			this.setPesoGreedy(peso_count);
		}

	}

	/**
	 * vecinoMasCercano
	 * 
	 * Busca el vecino más cercano que aún no ha sido visitado.
	 *
	 * @param visitados La lista de vértices que ya han sido visitados.
	 * @param actual    El vértice actual desde el que se busca el vecino más
	 *                  cercano.
	 * @return El vecino más cercano no visitado, o null si todos los vecinos han
	 *         sido visitados o no existen.
	 */
	private String vecinoMasCercano(ArrayList<String> visitados, String actual) {
		double min = Double.MAX_VALUE;
		String vecino = null;

		// Obtenemos los vecinos del actual
		TreeMap<String, Double> aux = this.adjacencyMap.get(actual);

		/*
		 * Si nulo, no entramos ya que no disponemos de ningun vecino y puede saltar
		 * algun error,
		 * en caso contraro, entramos e iteramos con los vecinos, en caso de que no
		 * esten en visitados y sea el menor actualizamos
		 * las variables min y vecino (el vecino mas cercano) y devolvemos el vecino.
		 */
		if (aux != null) {
			for (Entry<String, Double> auxi : aux.entrySet()) {
				if (!visitados.contains(auxi.getKey()) && auxi.getValue() < min) {
					min = auxi.getValue();
					vecino = auxi.getKey();
				}
			}
		}

		return vecino;
	}

	/**
	 * getPriorityQueue
	 * 
	 * Método que devuelve una cola de prioridad con todas las aristas del grafo
	 * 
	 * @param edges
	 */

	public void getPriorityQueue(PriorityQueue<Arista> edges) {

		// Añadir para cada Vertice las aristas que contiene con otros Vertices
		for (String v : this.adjacencyMap.keySet()) {
			for (String to : this.adjacencyMap.get(v).keySet()) {
				if (this.getDirected() == false && v.compareTo(to) > 0)
					continue;
				edges.add(new Arista(v, to, this.adjacencyMap.get(v).get(to)));
			}
		}
	}

	/**
	 * Kruskal
	 * 
	 * 
	 * Método que aplica el algoritmo de Kruskal para encontrar
	 * el árbol de recubrimiento de coste mínimo de un grafo ponderado
	 * no dirigido con una cola de prioridad.
	 * 
	 * Este método ordena las aristas del grafo por pesos crecientes y
	 * luego selecciona las aristas de menor peso que no formen ciclos hasta
	 * que se haya incluido un número de aristas igual a |V| - 1, donde |V|
	 * es el número de vértices en el grafo. Utiliza conjuntos disjuntos para
	 * mantener un seguimiento de los vértices conectados y evitar la formación de
	 * ciclos.
	 * 
	 * @param visual indica si se debe generar un archivo para visualizar el grafo y
	 *               su solución.
	 */

	public void Kruskal(Boolean visual) {

		// Ordenar las aristas E de G por pesos crecientes
		PriorityQueue<Arista> candidatos = new PriorityQueue<Arista>(Comparator.comparing(Arista::getPeso));

		getPriorityQueue(candidatos);

		// Iniciar n conjuntos disjuntos, uno por cada vértice de V
		TreeMap<String, TreeSet<String>> conjuntos = new TreeMap<String, TreeSet<String>>();
		// Bucle en el que se añaden los conjuntos disjuntos
		for (String v : this.adjacencyMap.keySet()) {
			// Se crea conjunto disjunto como TreeSet de String
			TreeSet<String> conjunto = new TreeSet<String>();
			// Se añade el conjunto al conjunto de conjuntos disjuntos
			conjuntos.put(v, conjunto);
			// Se añade el vértice al conjunto
			conjunto.add(v);
		}

		ArrayList<Arista> solucion = new ArrayList<Arista>();

		// Condición de MST (Ahorra tiempo de ejecución, ya que no se recorren todas las
		// aristas)
		while (solucion.size() < this.numberOfVertices() - 1) {

			// Selecciono arista de menor peso
			Arista arista = candidatos.poll();
			// Conjunto Disjunto que contiene al vertice u
			TreeSet<String> conjuntoOrigen = conjuntos.get(arista.getOrigen());

			// Conjunto Disjunto que contiene al vertice v
			TreeSet<String> conjuntoDestino = conjuntos.get(arista.getDestino());

			// Itero los valores de conjuntoDestino
			for (String conj : conjuntoDestino) {
				if (conjuntoOrigen.contains(conj)) {
					break;
				} else {
					solucion.add(arista);
					conjuntoOrigen.addAll(conjuntoDestino);
					conjuntos.put(arista.getDestino(), conjuntoOrigen);
					conjuntoDestino.clear();
					break;
				}
			}
		}

		// Mostrar la solución
		if (visual) {
			System.out.println("**************** Generando archivo para visualizar ****************");
			GenerateGraph gg = new GenerateGraph();
			gg.SaveVisualizateResultGraph(this, solucion);
			System.out.println();
			System.out.println();
			System.out.println("Se ha creado el .dot de manera satisfactoria.");
			System.out.println();
			System.out.println();
			System.out.println("--------------------- Solucion ----------------------");
			System.out.println();
			this.mostrarSolucion(solucion);

		}
	}

	public ArrayList<String> TSPBackTracking(String origen, String destino) {
		// Inicializamos las estructuras de datos
		ArrayList<String> resultado = new ArrayList<String>();
		// Añadimos el origen a la lista de resultados
		resultado.add(origen);
		// Creamos un TreeMap para almacenar los vertices visitados
		TreeMap<String, Boolean> visitados = new TreeMap<String, Boolean>();
		for (String vertice : this.adjacencyMap.keySet()) {
			visitados.put(vertice, false);
		}
		// Inicializamos el peso a 0
		Double peso = 0.0;
		// Llamamos al método recursivo para los vecinos del origen
		for (String vecino : this.adjacencyMap.get(origen).keySet()) {
			TSPBackTracking(new ArrayList<>(resultado), visitados, vecino, destino,
					peso + this.getWeight(origen, vecino));
		}
		// Devolvemos la solución
		this.setPesoBackTracking(peso);

		return solucion;
	}

	/**
	 * algoritmoTSP
	 * 
	 * Método que implementa el algoritmo de TSP para encontrar el camino más corto
	 * que debe seguir la unidad de mantenimiento, partiendo desde Almería, y
	 * visitando todas las ciudades que tiene una centralita exactamente una vez y
	 * regresar a Almería.
	 */

	private void TSPBackTracking(ArrayList<String> resultado, TreeMap<String, Boolean> visitados, String actual,
			String destino, double peso) {
		// Se añade el vertice actual a la lista de vertices
		resultado.add(actual);
		visitados.put(actual, true);

		// Si el vertice actual es igual al destino, se elimina el ultimo vertice de la
		// lista y se termina el algoritmo
		if (actual.equals(destino) && resultado.size() == this.numberOfVertices() + 1) {

			if (peso < minimo) {
				// Actualizamos el peso minimo
				minimo = peso;
				// Actualizamos la solucion
				solucion = new ArrayList<>(resultado);
			}
			// Eliminamos el ultimo vertice de la lista
			resultado.remove(resultado.size() - 1);
			// Marcamos el vertice actual como no visitado
			visitados.put(actual, false);
			return;
		}
		// Iteramos los vecinos del vertice actual
		for (String vecino : this.getNeighbors(actual)) {
			// Si el vertice ya ha sido visitado, continuamos
			if (visitados.get(vecino) == true)
				continue;
			// Aumentamos el peso
			peso += this.getWeight(actual, vecino);
			// Llamamos recursivamente al metodo
			TSPBackTracking(resultado, visitados, vecino, destino, peso);
			// Disminuimos el peso si no ha entrado como solucion
			peso -= this.getWeight(actual, vecino);
		}
		// Eliminamos el ultimo vertice de la lista
		resultado.remove(resultado.size() - 1);
		// Marcamos el vertice actual como no visitado
		visitados.put(actual, false);

	}

	/**
	 * randomOrigen
	 * 
	 * Establece un origen aleatorio para el grafo
	 */
	private void randomOrigen() {
		Random random = new Random();
		this.origen = Integer.toString(random.nextInt(this.numberOfVertices()));
	}

	/**
	 * mostrarSolucion
	 * 
	 * @param sol
	 */
	private void mostrarSolucion(ArrayList<Arista> sol) {
		double peso_count = 0.0;

		for (Arista arista : sol) {
			peso_count += arista.getPeso();
			System.out.println(arista);
		}

		this.setPesoGreedy(peso_count);
		System.out.println();
		System.out.println("Peso de: " + peso_count);
		System.out.println();

	}

	/**
	 * setOrigen
	 * 
	 * Establece el origen del grafo
	 * 
	 * @param origen
	 * @return
	 */
	public void setOrigen(String origen) {
		this.origen = origen;
	}

	/**
	 * getOrigen
	 * 
	 * Devuelve el origen del grafo
	 * 
	 * @return String, origen
	 */
	public String getOrigen() {
		return this.origen;
	}


	/**
	 * setPesoBackTracking
	 * 
	 * Establece el peso del algoritmo de BackTracking
	 * 
	 * @param peso
	 */
	public void setPesoBackTracking(double peso) {
		this.pesoBackTracking = peso;
	}

	/**
	 * getPesoBackTracking
	 * 
	 * Devuelve el peso del algoritmo de BackTracking
	 * 
	 * @return double, peso
	 */
	public double getPesoBackTracking() {
		return this.pesoBackTracking;
	}

	/**
	 * setPesoGreedy
	 * 
	 * Establece el peso del algoritmo Greedy
	 * 
	 * @param peso
	 */
	public void setPesoGreedy(double peso) {
		this.pesoGreedy = peso;
	}

	/**
	 * getPesoGreedy
	 * 
	 * Devuelve el peso del algoritmo Greedy
	 * 
	 * @return double, peso
	 */
	public double getPesoGreedy() {
		return this.pesoGreedy;
	}

	/**
	 * getCarpeta
	 * 
	 * Devuelve el path
	 * 
	 * @return String, path
	 */
	public String getCarpeta() {
		return this.carpeta;
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

		for (int i = 0; i < tiempo.length - 1; i++) {
			suma += tiempo[i];
		}

		double media = suma / (double) (tiempo.length - 1);

		return media;
	}

	private long calcularEspacio(int algoritmo) {
        int nodos = this.adjacencyMap.size();
        int aristas = 0;
        long candidatos = 0;
        long solucion = 0;
        long usadas = 0;

        for(TreeMap<String, Double> entrada : this.adjacencyMap.values()){
			for(String destino : entrada.keySet()){
            aristas++;
            }
		}

        switch(algoritmo){
            case 0: //PRIM
                solucion = (nodos) * (INT_SIZE_BYTES + STRING_SIZE_BYTES + DOUBLE_SIZE_BYTES);
                candidatos = aristas * (INT_SIZE_BYTES + STRING_SIZE_BYTES + DOUBLE_SIZE_BYTES);
                usadas = nodos * STRING_SIZE_BYTES;
                break;
            case 1: //KRUSKAL
                solucion = (nodos+1)*(STRING_SIZE_BYTES + STRING_SIZE_BYTES + DOUBLE_SIZE_BYTES);
                candidatos = aristas * (INT_SIZE_BYTES + STRING_SIZE_BYTES + DOUBLE_SIZE_BYTES);
                usadas = nodos * STRING_SIZE_BYTES;
                break;
            case 2: //Backtracking
                solucion = (nodos+1)* STRING_SIZE_BYTES;
                candidatos = (nodos * STRING_SIZE_BYTES)/8;
                break;

            case 3: //TSP Greedy
                usadas = nodos * STRING_SIZE_BYTES;
                solucion = (nodos-1)*(STRING_SIZE_BYTES + STRING_SIZE_BYTES + DOUBLE_SIZE_BYTES);
                break;
        }

        return (long) solucion + candidatos + usadas;
    }

	public static void main(String[] args) {
		RoadNetwork net = new RoadNetwork();

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
		int n = 0, m = 0;

		// Tiempos
		long tiempoInicial, tiempoFinal, tiempoInicial2, tiempoFinal2;
		long[] tiempos1 = new long[10];
		long[] tiempos2 = new long[10];
		double tiempoMedio = 0.0;
		double tiempoMedio2 = 0.0;
		String d = "";
		String origen = "";

		// Tamaño
//		long memoriaInicial = Runtime.getRuntime().freeMemory();
		double memoriaTotalUsada = 0.0;

		System.out.println("==============================================================");
		System.out.println(" ███████╗██████╗  █████╗");
		System.out.println(" ██╔════╝██╔══██╗██╔══██╗");
		System.out.println(" █████╗░░██║░░██║███████║");
		System.out.println(" ██╔══╝░░██║░░██║██╔══██║");
		System.out.println(" ███████╗██████╔╝██║░░██║");
		System.out.println(" ╚══════╝╚═════╝░╚═╝░░╚═╝");
		System.out.println("==============================================================");
		System.out.println("              BIENVENIDOS A LA PRÁCTICA 02 DE LOSJIMENEZ      ");
		System.out.println("==============================================================");

		while (opcion != 13) {
			System.out.println();
			System.out.println("Selecciona una opción:");
			System.out.println("1. Generación de un nuevo grafo");
			System.out.println("2. Ejercicio1: Algoritmo de Prim sin PriorityQueue");
			System.out.println("3. Ejercicio1: Algoritmo de Prim con PriorityQueue");
			System.out.println("4. Ejercicio2: Algoritmo de Kruskal");
			System.out.println("5. Ejercicio3: Algoritmo TSP");
			System.out.println("6. Ejercicio5: Algoritmo TSPGreedy ");
			System.out.println("7. Resultados experimentales: Algoritmo de Prim sin PriorityQueue");
			System.out.println("8. Resultados experimentales: Algoritmo de Prim con PriorityQueue");
			System.out.println("9. Resultados experimentales: Algoritmo de Kruskal");
			System.out.println("10. Resultados experimentales: Algoritmo TSPGreedy");
			System.out.println("11. Resultados experimentales: Algoritmo TSP");
			System.out.println("12. Resultados experimentales: Algoritmo TSPBackTracking vs TSPGreedy");
			System.out.println("13. Salir");
			System.out.print("Opción: ");

			opcion = scanner.nextInt();

			switch (opcion) {
				case 1:

					System.out.println("Ejercicio 1");
					System.out.println("Introduce el número de vértices: ");
					n = scanner.nextInt();
					System.out.println("Introduce el número de aristas: ");
					m = scanner.nextInt();

					// Generar grafo
					System.out.println("Generando grafo...");
					GenerateGraph gg = new GenerateGraph(n, m);

					try {
						gg.GenerateGraphRandom();

						System.out.println("================== Grafo generado correctamente. ==================");
						System.out.println(
								"Se ha generado correctamente el grafo en y el archivo .dot para su visualización.");
						System.out.println(
								"El archivo .txt está en la carpeta 'equipo-losjimenez/src/main/java/org/eda2/practica02/dataset/graph_'"
										+ n + "_" + m + ".txt");
						System.out.println(
								"El archivo .dot está en la carpeta 'equipo-losjimenez/src/main/java/org/eda2/practica02/dataset/graphviz/graph_visual_'"
										+ n + "_" + m + ".dot");
					} catch (Exception e) {
						System.out.println("Ha ocurrido un error al generar el grafo.");
						break;
					}

					break;

				case 2:
					System.out.println("Ejercicio 1 - Algoritmo de Prim sin PriorityQueue");
					System.out.println("Introduce el grafo a probar: reducido o completo");
					d = scanner.next();

					while (!d.equals("reducido") && !d.equals("completo")) {
						System.out.println("Introduce el grafo a probar: reducido o completo");
						d = scanner.next();
					}

					/*
					 * Cargamos el grafo elegido
					 */
					if (d.equals("reducido")) {
						try {
							net.loadNetwork(net.getCarpeta() + "graphEDAland.txt");
							System.out.println("Grafo cargado correctamente.");
						} catch (Exception e) {
							System.out.println(
									"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
							break;
						}
					} else {
						try {
							net.loadNetwork(net.getCarpeta() + "graphEDAlandLarge.txt");
							System.out.println("Grafo cargado correctamente.");
						} catch (Exception e) {
							System.out.println(
									"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
							break;
						}
					}

					System.out.println("Introduzca origen: ");
					System.out.println("¡Asegurese de que el origen exista en el grafo!");
					origen = scanner.next();
					net.setOrigen(origen);

					/*
					 * Realizamos las pruebas
					 */

					try {

						net.Prim(true);

					} catch (Exception e) {
						System.out.println(
								"Ha ocurrido un error al ejecutar el algoritmo de Prim. Revise que el vértice de origen exista.");
					}

					System.out.println("==============================================================");
					System.out.println();

					break;
				case 3:
					System.out.println("Ejercicio 2 - Algoritmo de Prim con PriorityQueue");
					System.out.println("Introduce el grafo a probar: reducido o completo");
					d = scanner.next();

					while (!d.equals("reducido") && !d.equals("completo")) {
						System.out.println("Introduce el grafo a probar: reducido o completo");
						d = scanner.next();
					}

					/*
					 * Cargamos el grafo elegido
					 */
					if (d.equals("reducido")) {
						try {
							net.loadNetwork(net.getCarpeta() + "graphEDAland.txt");
							System.out.println("Grafo cargado correctamente.");
						} catch (Exception e) {
							System.out.println(
									"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
							break;
						}
					} else {
						try {
							net.loadNetwork(net.getCarpeta() + "graphEDAlandLarge.txt");
							System.out.println("Grafo cargado correctamente.");
						} catch (Exception e) {
							System.out.println(
									"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
							break;
						}
					}

					System.out.println("Introduzca origen: ");
					System.out.println("¡Asegurese de que el origen exista en el grafo!");
					origen = scanner.next();
					net.setOrigen(origen);

					/*
					 * Realizamos las pruebas
					 */

					try {

						net.PrimPriorityQueue(true);

					} catch (Exception e) {
						System.out.println(
								"Ha ocurrido un error al ejecutar el algoritmo de Prim. Revise que el vértice de origen exista.");
					}

					System.out.println("==============================================================");
					System.out.println();

					break;

				case 4:
					System.out.println("Ejercicio 2 - Algoritmo de Kruskal con PriorityQueue");
					System.out.println("Introduce el grafo a probar: reducido o completo");
					d = scanner.next();

					while (!d.equals("reducido") && !d.equals("completo")) {
						System.out.println("Introduce el grafo a probar: reducido o completo");
						d = scanner.next();
					}

					/*
					 * Cargamos el grafo elegido
					 */
					if (d.equals("reducido")) {
						try {
							net.loadNetwork(net.getCarpeta() + "graphEDAland.txt");
							System.out.println("Grafo cargado correctamente.");
						} catch (Exception e) {
							System.out.println(
									"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
							break;
						}
					} else {
						try {
							net.loadNetwork(net.getCarpeta() + "graphEDAlandLarge.txt");
							System.out.println("Grafo cargado correctamente.");
						} catch (Exception e) {
							System.out.println(
									"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
							break;
						}
					}

					/*
					 * Realizamos las pruebas
					 */

					try {

						net.Kruskal(true);

					} catch (Exception e) {
						System.out.println(
								"Ha ocurrido un error al ejecutar el algoritmo de Prim. Revise que el vértice de origen exista.");
					}

					System.out.println("==============================================================");
					System.out.println();
					break;

				case 5:
					System.out.println("Ejercicio 3 - Algoritmo TSP-BackTracking");
					System.out.println("==============================================================");
					System.out.println(
							"Se va a proceder a ejecutar el algoritmo TSP-BackTracking en la nueva red de telecomunicaciones.");

					/*
					 * Cargamos el grafo
					 */
					try {
						net.loadNetwork(net.getCarpeta() + "graphEDAlandNewroads.txt");
						System.out.println("Grafo cargado correctamente.");
					} catch (Exception e) {
						System.out.println(
								"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
						break;
					}

					System.out.println("Introduzca origen: ");
					System.out.println("¡Asegurese de que el origen exista en el grafo!");
					origen = scanner.next();
					net.setOrigen(origen);

					/*
					 * Realizamos las pruebas
					 */
					try {

						ArrayList<String> array = net.TSPBackTracking(net.origen, net.origen);
						System.out.println("El camino más corto que debe seguir la unidad de mantenimiento es: "
								+ array.toString() + " y tiene un peso de " + minimo);
					} catch (Exception e) {
						System.out.println(
								"Ha ocurrido un error al ejecutar el algoritmo de TSP-BackTracking. Revise que el vértice de origen exista.");
					}

					System.out.println("==============================================================");
					System.out.println();
					break;

				case 6:
					System.out.println("Ejercicio 5 - Algoritmo TSP-Greedy");
					System.out.println(
							"Introduce el grafo a probar: reducido, completo u otro (para desginar vertices y aristas)");
					d = scanner.next();

					while (!d.equals("reducido") && !d.equals("completo") && !d.equals("otro")) {
						System.out.println(
								"Introduce el grafo a probar: reducido o completo u otro (para desginar vertices y aristas)");
						d = scanner.next();
					}

					/*
					 * Cargamos el grafo elegido
					 */
					if (d.equals("reducido")) {
						try {
							net.loadNetwork(net.getCarpeta() + "graphEDAland.txt");
							System.out.println("Grafo cargado correctamente.");
						} catch (Exception e) {
							System.out.println(
									"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
							break;
						}
					} else if (d.equals("completo")) {
						try {
							net.loadNetwork(net.getCarpeta() + "graphEDAlandLarge.txt");
							System.out.println("Grafo cargado correctamente.");
						} catch (Exception e) {
							System.out.println(
									"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
							break;
						}
					} else {
						System.out.println("Introduce el número de vértices: ");
						n = scanner.nextInt();
						System.out.println("Introduce el número de aristas: ");
						m = scanner.nextInt();

						try {
							String data = net.getCarpeta() + "graph_" + n
									+ "n_" + m + "m.txt";
							net.loadNetwork(data);
							System.out.println("Grafo cargado correctamente.");
						} catch (Exception e) {
							System.out.println(
									"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
							break;
						}

					}

					System.out.println("¿Quiere introducir origen? En caso contrario se generará aleatoriamente.");
					System.out.println("Introduzca si o no");
					String setOr = scanner.next();

					if (setOr.equals("si")) {
						System.out.println("Introduzca origen: ");
						System.out.println("¡Asegurese de que el origen exista en el grafo!");
						origen = scanner.next();
						net.setOrigen(origen);
					}

					/*
					 * Realizamos las pruebas
					 */

					try {

						net.TSPGreedy(true);

					} catch (Exception e) {
						System.out.println(
								"Ha ocurrido un error al ejecutar el algoritmo de Prim. Revise que el vértice de origen exista.");
					}

					System.out.println("==============================================================");
					System.out.println();

					break;

				case 7:
					System.out.println("--------------------- Resultados experimentales ----------------------");
					System.out.println("Algoritmo de Prim sin PriorityQueue");
					System.out.println("¿Existe previamente el grafo? Escriba S (Sí) o N (No)");
					d = scanner.next();

					if (d.equals("S") || d.equals("s") || d.equals("Si") || d.equals("Sí")) {

						System.out.println("¿Son los grafos de las prácticas? (si o no)");
						String t = scanner.next();

						if (t.equals("si") || t.equals("Si")) {

							System.out.println(
									"Introduce el grafo a probar: reducido, completo u otro (para desginar vertices y aristas)");
							d = scanner.next();

							while (!d.equals("reducido") && !d.equals("completo") && !d.equals("otro")) {
								System.out.println(
										"Introduce el grafo a probar: reducido o completo u otro (para desginar vertices y aristas)");
								d = scanner.next();
							}

							/*
							 * Cargamos el grafo elegido
							 */
							if (d.equals("reducido")) {
								try {
									net.loadNetwork(net.getCarpeta() + "graphEDAland.txt");
									System.out.println("Grafo cargado correctamente.");
								} catch (Exception e) {
									System.out.println(
											"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
									break;
								}
							} else if (d.equals("completo")) {
								try {
									net.loadNetwork(net.getCarpeta() + "graphEDAlandLarge.txt");
									System.out.println("Grafo cargado correctamente.");
								} catch (Exception e) {
									System.out.println(
											"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
									break;
								}
							}
						} else {
							System.out.println("Introduce el número de vértices: ");
							n = scanner.nextInt();
							System.out.println("Introduce el número de aristas: ");
							m = scanner.nextInt();

							try {
								String data = net.getCarpeta() + "graph_" + n
										+ "n_" + m + "m.txt";
								net.loadNetwork(data);
								System.out.println("Grafo cargado correctamente.");
							} catch (Exception e) {
								System.out.println(
										"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
								break;
							}

						}
					} else {
						System.out.println(
								"Ha ocurrido un error al ejecutar el algoritmo de Prim. Revise que el vértice de origen exista.");
						break;
					}

					System.out.println("¿Quiere introducir origen? En caso contrario se generará aleatoriamente.");
					System.out.println("Introduzca si o no");
					String setOri = scanner.next();

					if (setOri.equals("si")) {
						System.out.println("Introduzca origen: ");
						System.out.println("¡Asegurese de que el origen exista en el grafo!");
						origen = scanner.next();
						net.setOrigen(origen);
					}

					try {

						for (int i = 0; i < 10; i++) {
							System.gc(); // Sugerencia para solicitar la ejecución del Garbage Collector

							tiempoInicial = System.currentTimeMillis();
							net.Prim(false);
							tiempoFinal = System.currentTimeMillis();

							tiempos1[i] = tiempoFinal - tiempoInicial;
						}

						// Calcula la media de tiempo y memoria
						tiempoMedio = calcularMedia(tiempos1);
						memoriaTotalUsada = net.calcularEspacio(0);

						// Imprime los resultados
						System.out.println("\nTiempo medio de ejecución: " + tiempoMedio + " milisegundos.");
						System.out.println("Memoria media: " + memoriaTotalUsada + " bytes (" +
								(memoriaTotalUsada / 1024.0 / 1024.0) + " MB).");

					} catch (Exception e) {
						System.out.println(
								"Ha ocurrido un error al ejecutar el algoritmo de Prim. Revise que el vértice de origen exista.");
					}
					System.out.println("==============================================================");
					System.out.println();

					break;
				case 8:
					System.out.println("--------------------- Resultados experimentales ----------------------");
					System.out.println("Algoritmo de Prim con PriorityQueue");
					System.out.println("¿Existe previamente el grafo? Escriba S (Sí) o N (No)");
					d = scanner.next();

					if (d.equals("S") || d.equals("s") || d.equals("Si") || d.equals("Sí")) {

						System.out.println("¿Son los grafos de las prácticas? (si o no)");
						String t = scanner.next();

						if (t.equals("si") || t.equals("Si")) {

							System.out.println(
									"Introduce el grafo a probar: reducido, completo u otro (para desginar vertices y aristas)");
							d = scanner.next();

							while (!d.equals("reducido") && !d.equals("completo") && !d.equals("otro")) {
								System.out.println(
										"Introduce el grafo a probar: reducido o completo u otro (para desginar vertices y aristas)");
								d = scanner.next();
							}

							/*
							 * Cargamos el grafo elegido
							 */
							if (d.equals("reducido")) {
								try {
									net.loadNetwork(net.getCarpeta() + "graphEDAland.txt");
									System.out.println("Grafo cargado correctamente.");
								} catch (Exception e) {
									System.out.println(
											"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
									break;
								}
							} else if (d.equals("completo")) {
								try {
									net.loadNetwork(net.getCarpeta() + "graphEDAlandLarge.txt");
									System.out.println("Grafo cargado correctamente.");
								} catch (Exception e) {
									System.out.println(
											"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
									break;
								}
							}
						} else {
							System.out.println("Introduce el número de vértices: ");
							int nn = scanner.nextInt();
							System.out.println("Introduce el número de aristas: ");
							int mm = scanner.nextInt();

							try {
								String data = net.getCarpeta() + "graph_" + nn
										+ "n_" + mm + "m.txt";
								net.loadNetwork(data);
								System.out.println("Grafo cargado correctamente.");
							} catch (Exception e) {
								System.out.println(
										"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
								break;
							}

						}
					} else {
						System.out.println(
								"Ha ocurrido un error al ejecutar el algoritmo de Prim. Revise que el vértice de origen exista.");
						break;
					}

					System.out.println("¿Quiere introducir origen? En caso contrario se generará aleatoriamente.");
					System.out.println("Introduzca si o no");
					String setOrig = scanner.next();

					if (setOrig.equals("si")) {
						System.out.println("Introduzca origen: ");
						System.out.println("¡Asegurese de que el origen exista en el grafo!");
						origen = scanner.next();
						net.setOrigen(origen);
					}

					try {

						for (int i = 0; i < 10; i++) {
							System.gc(); // Sugerencia para solicitar la ejecución del Garbage Collector

							tiempoInicial = System.currentTimeMillis();
							net.PrimPriorityQueue(false);
							tiempoFinal = System.currentTimeMillis();

							tiempos1[i] = tiempoFinal - tiempoInicial;
						}

						// Calcula la media de tiempo y memoria
						tiempoMedio = calcularMedia(tiempos1);
						memoriaTotalUsada = net.calcularEspacio(0);

						// Imprime los resultados
						System.out.println("\nTiempo medio de ejecución: " + tiempoMedio + " milisegundos.");
						System.out.println("Memoria media: " + memoriaTotalUsada + " bytes (" +
								(memoriaTotalUsada / 1024.0 / 1024.0) + " MB).");

					} catch (Exception e) {
						System.out.println(
								"Ha ocurrido un error al ejecutar el algoritmo de Prim. Revise que el vértice de origen exista.");
					}
					System.out.println("==============================================================");
					System.out.println();
					break;

				case 9:
					System.out.println("--------------------- Resultados experimentales ----------------------");
					System.out.println("Algoritmo de Kruskal con PriorityQueue");
					System.out.println("¿Existe previamente el grafo? Escriba S (Sí) o N (No)");
					d = scanner.next();

					if (d.equals("S") || d.equals("s")) {

						System.out.println("¿Son los grafos de las prácticas? (si o no)");
						String t = scanner.next();

						if (t.equals("si") || t.equals("Si")) {

							System.out.println(
									"Introduce el grafo a probar: reducido, completo u otro (para desginar vertices y aristas)");
							d = scanner.next();

							while (!d.equals("reducido") && !d.equals("completo") && !d.equals("otro")) {
								System.out.println(
										"Introduce el grafo a probar: reducido o completo u otro (para desginar vertices y aristas)");
								d = scanner.next();
							}

							/*
							 * Cargamos el grafo elegido
							 */
							if (d.equals("reducido")) {
								try {
									net.loadNetwork(net.getCarpeta() + "graphEDAland.txt");
									System.out.println("Grafo cargado correctamente.");
								} catch (Exception e) {
									System.out.println(
											"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
									break;
								}
							} else if (d.equals("completo")) {
								try {
									net.loadNetwork(net.getCarpeta() + "graphEDAlandLarge.txt");
									System.out.println("Grafo cargado correctamente.");
								} catch (Exception e) {
									System.out.println(
											"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
									break;
								}
							}

						} else {

							System.out.println("Introduce el número de vértices: ");
							n = scanner.nextInt();
							System.out.println("Introduce el número de aristas: ");
							m = scanner.nextInt();

							try {
								String data = net.getCarpeta() + "graph_" + n
										+ "n_" + m + "m.txt";
								net.loadNetwork(data);
								System.out.println("Grafo cargado correctamente.");
							} catch (Exception e) {
								System.out.println(
										"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
								break;
							}

						}

					} else {
						System.out.println("Procedamos a generar el grafo...");
						System.out.println("Introduce el número de vértices: ");
						n = scanner.nextInt();
						System.out.println("Introduce el número de aristas: ");
						m = scanner.nextInt();

						// Generar grafo
						System.out.println("Generando grafo...");
						GenerateGraph gg2 = new GenerateGraph(n, m);

						try {
							gg2.GenerateGraphRandom();

							System.out
									.println("================== Grafo generado correctamente. ==================");
							System.out.println(
									"Se ha generado correctamente el grafo en y el archivo .dot para su visualización.");
							System.out.println(
									"El archivo .txt está en la carpeta 'equipo-losjimenez/src/main/java/org/eda2/practica02/dataset/graph_'"
											+ n + "n_" + m + "m.txt");
							System.out.println(
									"El archivo .dot está en la carpeta 'equipo-losjimenez/src/main/java/org/eda2/practica02/dataset/graphviz/graph_visual_'"
											+ n + "n_" + m + "m.dot");

							net.loadNetwork("equipo-losjimenez/src/main/java/org/eda2/practica02/dataset/graph_" + n
									+ "_" + m + ".txt");
						} catch (Exception e) {
							System.out.println("Ha ocurrido un error al generar el grafo.");
							break;
						}

					}

					try {

						for (int i = 0; i < 10; i++) {
							System.gc(); // Sugerencia para solicitar la ejecución del Garbage Collector

							tiempoInicial = System.currentTimeMillis();
							net.Kruskal(false);
							tiempoFinal = System.currentTimeMillis();

							tiempos1[i] = tiempoFinal - tiempoInicial;
						}

						// Calcula la media de tiempo y memoria
						tiempoMedio = calcularMedia(tiempos1);
						memoriaTotalUsada = net.calcularEspacio(1);

						// Imprime los resultados
						System.out.println("\nValores asociados para un grafo n: " + n + " y m: " + m + ".");
						System.out.println("\nTiempo medio de ejecución: " + tiempoMedio + " milisegundos.");
						System.out.println("Memoria media: " + memoriaTotalUsada + " bytes (" +
								(memoriaTotalUsada / 1024.0 / 1024.0) + " MB).");

					} catch (Exception e) {
						System.out.println(
								"Ha ocurrido un error al ejecutar el algoritmo de Kruskal. Revise que el vértice de origen exista.");
					}
					System.out.println("==============================================================");
					System.out.println();

					break;
				case 10:
					System.out.println(
							"--------------------- Resultados experimentales: Algoritmo TSPGreedy ----------------------");
					System.out.println("¿Existe previamente el grafo? Escriba S (Sí) o N (No)");
					d = scanner.next();

					if (d.equals("S") || d.equals("s")) {
						System.out.println("Introduce el número de vértices: ");
						n = scanner.nextInt();
						System.out.println("Introduce el número de aristas: ");
						m = scanner.nextInt();

						try {
							String data = net.getCarpeta() + "graph_" + n
									+ "n_" + m + "m.txt";
							net.loadNetwork(data);
							System.out.println("Grafo cargado correctamente.");
						} catch (Exception e) {
							System.out.println(
									"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
							break;
						}

					} else {
						System.out.println("Procedamos a generar el grafo...");
						System.out.println("Introduce el número de vértices: ");
						n = scanner.nextInt();
						System.out.println("Introduce el número de aristas: ");
						m = scanner.nextInt();

						// Generar grafo
						System.out.println("Generando grafo...");
						GenerateGraph gg2 = new GenerateGraph(n, m);

						try {
							gg2.GenerateGraphRandom();

							System.out
									.println("================== Grafo generado correctamente. ==================");
							System.out.println(
									"Se ha generado correctamente el grafo en y el archivo .dot para su visualización.");
							System.out.println(
									"El archivo .txt está en la carpeta 'equipo-losjimenez/src/main/java/org/eda2/practica02/dataset/graph_'"
											+ n + "n_" + m + "m.txt");
							System.out.println(
									"El archivo .dot está en la carpeta 'equipo-losjimenez/src/main/java/org/eda2/practica02/dataset/graphviz/graph_visual_'"
											+ n + "n_" + m + "m.dot");

							net.loadNetwork("equipo-losjimenez/src/main/java/org/eda2/practica02/dataset/graph_" + n
									+ "_" + m + ".txt");
						} catch (Exception e) {
							System.out.println("Ha ocurrido un error al generar el grafo.");
							break;
						}

					}

					System.out.println("Introduzca origen: ");
					System.out.println("¡Asegurese de que el origen exista en el grafo!");
					origen = scanner.next();
					net.setOrigen(origen);

					try {

						for (int i = 0; i < 10; i++) {
							System.gc(); // Sugerencia para solicitar la ejecución del Garbage Collector

							tiempoInicial = System.currentTimeMillis();
							net.TSPGreedy(false);
							tiempoFinal = System.currentTimeMillis();

							tiempos1[i] = tiempoFinal - tiempoInicial;
						}

						// Calcula la media de tiempo y memoria
						tiempoMedio = calcularMedia(tiempos1);
						memoriaTotalUsada = net.calcularEspacio(3);

						// Imprime los resultados
						System.out.println("\nValores asociados para un grafo n: " + n + " y m: " + m + ".");
						System.out.println("\nTiempo medio de ejecución: " + tiempoMedio + " milisegundos.");
						System.out.println("Memoria media: " + memoriaTotalUsada + " bytes (" +
								(memoriaTotalUsada / 1024.0 / 1024.0) + " MB).");

					} catch (Exception e) {
						System.out.println(
								"Ha ocurrido un error al ejecutar el algoritmo TSPGreedy. Revise que el vértice de origen exista.");
					}
					System.out.println("==============================================================");
					System.out.println();

					break;

				case 11:
					System.out.println(
							"--------------------- Resultados experimentales: Algoritmo TSPBacktraking ----------------------");
					System.out.println("¿Existe previamente el grafo? Escriba S (Sí) o N (No)");
					d = scanner.next();

					if (d.equals("S") || d.equals("s")) {

						System.out.println("¿Son los grafos de las prácticas? (si o no)");
						String t = scanner.next();

						if (t.equals("si") || t.equals("Si")) {

							System.out.println(
									"Introduce el grafo a probar: nuevared u otro (para desginar vertices y aristas)");
							d = scanner.next();

							while (!d.equals("nuevared") && !d.equals("otro")) {
								System.out.println(
										"Introduce el grafo a probar: reducido o completo u otro (para desginar vertices y aristas)");
								d = scanner.next();
							}

							/*
							 * Cargamos el grafo elegido
							 */
							if (d.equals("nuevared")) {
								try {
									net.loadNetwork(net.getCarpeta() + "graphEDAlandNewroads.txt");
									System.out.println("Grafo cargado correctamente.");
								} catch (Exception e) {
									System.out.println(
											"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
									break;
								}
							}

						} else {

							System.out.println("Introduce el número de vértices: ");
							n = scanner.nextInt();
							System.out.println("Introduce el número de aristas: ");
							m = scanner.nextInt();

							try {
								String data = net.getCarpeta() + "graph_" + n
										+ "n_" + m + "m.txt";
								net.loadNetwork(data);
								System.out.println("Grafo cargado correctamente.");
							} catch (Exception e) {
								System.out.println(
										"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
								break;
							}

						}

					} else {
						System.out.println("Procedamos a generar el grafo...");
						System.out.println("Introduce el número de vértices: ");
						n = scanner.nextInt();
						System.out.println("Introduce el número de aristas: ");
						m = scanner.nextInt();

						// Generar grafo
						System.out.println("Generando grafo...");
						GenerateGraph gg2 = new GenerateGraph(n, m);

						try {
							gg2.GenerateGraphRandom();

							System.out
									.println("================== Grafo generado correctamente. ==================");
							System.out.println(
									"Se ha generado correctamente el grafo en y el archivo .dot para su visualización.");
							System.out.println(
									"El archivo .txt está en la carpeta 'equipo-losjimenez/src/main/java/org/eda2/practica02/dataset/graph_'"
											+ n + "n_" + m + "m.txt");
							System.out.println(
									"El archivo .dot está en la carpeta 'equipo-losjimenez/src/main/java/org/eda2/practica02/dataset/graphviz/graph_visual_'"
											+ n + "n_" + m + "m.dot");

							net.loadNetwork("equipo-losjimenez/src/main/java/org/eda2/practica02/dataset/graph_" + n
									+ "_" + m + ".txt");
						} catch (Exception e) {
							System.out.println("Ha ocurrido un error al generar el grafo.");
							break;
						}

					}

					System.out.println("Introduzca origen: ");
					System.out.println("¡Asegurese de que el origen exista en el grafo!");
					origen = scanner.next();
					net.setOrigen(origen);

					try {

						for (int i = 0; i < 10; i++) {
							System.gc(); // Sugerencia para solicitar la ejecución del Garbage Collector

							tiempoInicial = System.currentTimeMillis();
							net.TSPBackTracking(net.origen, net.origen);
							tiempoFinal = System.currentTimeMillis();

							tiempos1[i] = tiempoFinal - tiempoInicial;
						}

						// Calcula la media de tiempo y memoria
						tiempoMedio = calcularMedia(tiempos1);
						memoriaTotalUsada = net.calcularEspacio(2);

						// Imprime los resultados
						System.out.println("\nValores asociados para un grafo n: " + n + " y m: " + m + ".");
						System.out.println("\nTiempo medio de ejecución: " + tiempoMedio + " milisegundos.");
						System.out.println("Memoria media: " + memoriaTotalUsada + " bytes (" +
								(memoriaTotalUsada / 1024.0 / 1024.0) + " MB).");

					} catch (Exception e) {
						System.out.println(
								"Ha ocurrido un error al ejecutar el algoritmo TSPGreedy. Revise que el vértice de origen exista.");
					}
					System.out.println("==============================================================");
					System.out.println();

					break;

				case 12:
					System.out.println(
							"--------------------- Resultados experimentales: Algoritmo TSPBacktraking ----------------------");
					System.out.println("¿Existe previamente el grafo? Escriba S (Sí) o N (No)");
					d = scanner.next();

					if (d.equals("S") || d.equals("s")) {

						System.out.println("¿Son los grafos de las prácticas? (si o no)");
						String t = scanner.next();

						if (t.equals("si") || t.equals("Si")) {

							System.out.println(
									"Introduce el grafo a probar: nuevared u otro (para desginar vertices y aristas)");
							d = scanner.next();

							while (!d.equals("nuevared") && !d.equals("otro")) {
								System.out.println(
										"Introduce el grafo a probar: reducido o completo u otro (para desginar vertices y aristas)");
								d = scanner.next();
							}

							/*
							 * Cargamos el grafo elegido
							 */
							if (d.equals("nuevared")) {
								try {
									net.loadNetwork(net.getCarpeta() + "graphEDAlandNewroads.txt");
									System.out.println("Grafo cargado correctamente.");
								} catch (Exception e) {
									System.out.println(
											"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
									break;
								}
							}

						} else {

							System.out.println("Introduce el número de vértices: ");
							n = scanner.nextInt();
							System.out.println("Introduce el número de aristas: ");
							m = scanner.nextInt();

							try {
								String data = net.getCarpeta() + "graph_" + n
										+ "n_" + m + "m.txt";
								net.loadNetwork(data);
								System.out.println("Grafo cargado correctamente.");
							} catch (Exception e) {
								System.out.println(
										"Ha ocurrido un error al cargar el grafo. Revise si existe dicho grafo.");
								break;
							}

						}

					}

					System.out.println("Introduzca origen: ");
					System.out.println("¡Asegurese de que el origen exista en el grafo!");
					origen = scanner.next();
					net.setOrigen(origen);

					try {


							System.gc(); // Sugerencia para solicitar la ejecución del Garbage Collector

							tiempoInicial = System.currentTimeMillis();
							net.TSPBackTracking(net.origen, net.origen);
							tiempoFinal = System.currentTimeMillis();

							tiempos1[0] = tiempoFinal - tiempoInicial;
						



							System.gc(); // Sugerencia para solicitar la ejecución del Garbage Collector

							tiempoInicial2 = System.currentTimeMillis();
							net.TSPGreedy(false);
							tiempoFinal2 = System.currentTimeMillis();

							tiempos2[0] = tiempoFinal2 - tiempoInicial2;
						

						// Calcula la media de tiempo y memoria
						tiempoMedio = calcularMedia(tiempos1);
						tiempoMedio2 = calcularMedia(tiempos2);
						memoriaTotalUsada = net.calcularEspacio(2);

						// Imprime los resultados
						System.out.println("\nValores asociados para un grafo n: " + n + " y m: " + m + ".");
						System.out.println("\nTiempo medio de ejecución TSPBackTracking: " + tiempoMedio + " milisegundos y pesos: " + minimo);
						System.out.println("\nTiempo medio de ejecución TSPGreedy: " + tiempoMedio2 + " milisegundos y pesos: " + net.getPesoGreedy());
						double error = (net.getPesoGreedy() - minimo) / minimo * 100;
						System.out.println("\nPorcentaje de error cometido: " + error + "%");
						System.out.println("Memoria media: " + memoriaTotalUsada + " bytes (" +
								(memoriaTotalUsada / 1024.0 / 1024.0) + " MB).");

					} catch (Exception e) {
						System.out.println(
								"Ha ocurrido un error al ejecutar el algoritmo TSPGreedy. Revise que el vértice de origen exista.");
					}
					System.out.println("==============================================================");
					System.out.println();
					break;
				case 13:
					System.out.println("Saliendo del programa...");
					break;
				default:
					System.out.println("Opción no válida. Por favor, intenta de nuevo.");
			}
		}

		scanner.close();
		System.out.println("Programa finalizado.");
		System.out.println("==============================================================");
		System.out.println("==============================================================");

	}
}
