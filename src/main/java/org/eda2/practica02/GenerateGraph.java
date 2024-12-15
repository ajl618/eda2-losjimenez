package org.eda2.practica02;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.RowFilter.Entry;

/**
 * Greedy
 *	
 * Clase generada para la realizacion de la practica 2 de Estructura de datos
 * y algoritmos II (EDA 2), con respecto al uso de soluciones a un problema 
 * utilizando el método algorítmico greedy (o voraz). Esta clase va a estar preparada para 
 * poder generar grafos aleatorios, concretamente el ejercicio 4.
 * 
 * Se podran generar grandes redes aleatorias (grafos no orientados, valorados positivamente y conexos) de 
 * diferentes tamaños. Por ejemplo, podemos fijar un número vértices (n = 500, 1000, 1500, 2000 y 
 * 2500) y variando el número aristas (m) de tal manera que el grafo quede siempre conexo 
 * (generador de redes sintéticas).
 * 
 * Estos grafos 
 * 
 * @author Adrián Jiménez Benítez
 * @author Antonio José Jiménez Luque
 * 
 */
public class GenerateGraph {

	private int vertices;
    private int aristas;
    private double densidad;
    private int minimoAristas;
    private int maximoAristas;
    private String carpeta = System.getProperty("user.dir") + File.separator + 
                                "src" + File.separator +
                                "main" + File.separator + 
                                "java" + File.separator + 
                                "org" + File.separator + 
                                "eda2" + File.separator + 
                                "practica02" + File.separator + 
                                "dataset" + File.separator;
    private String carpeta_visual = System.getProperty("user.dir") + File.separator + 
					            "src" + File.separator +
					            "main" + File.separator + 
					            "java" + File.separator + 
					            "org" + File.separator + 
					            "eda2" + File.separator + 
					            "practica02" + File.separator + 
					            "dataset" + File.separator +
					            "graphviz" + File.separator;
    private String carpeta_result = System.getProperty("user.dir") + File.separator + 
					            "src" + File.separator +
					            "main" + File.separator + 
					            "java" + File.separator + 
					            "org" + File.separator + 
					            "eda2" + File.separator + 
					            "practica02" + File.separator + 
					            "dataset" + File.separator +
					            "graphviz" + File.separator +
                                "resultado" + File.separator;
    private boolean directed = false; //Por defecto no dirigido
    private ArrayList<String> verticesList = new ArrayList<>(); // Lista de vértices
    private ArrayList<String> edgeList = new ArrayList<>(); // Lista de aristas
    private ArrayList<String> edges = new ArrayList<>(); 
    private ArrayList<String> conexiones = new ArrayList<>();
    private static final Random random = new Random(); // Generador de números aleatorios

    public static void main(String[] args) {
        GenerateGraph test_generacion = new GenerateGraph(10, 45);
        test_generacion.GenerateGraphRandom();
    }
    
    /**
     * Constructor de la clase GenerateGraph
     */
    public GenerateGraph()
    {
        
    }

    /**
     * Constructor de la clase GenerateGraph
     * 
     * @param vertices int que representa el número de vértices del grafo
     * @param aristas int que representa el número de aristas del grafo
     * @param densidad  double que representa la densidad del grafo
     * @param carpeta String que representa la carpeta donde se va a guardar el archivo
     * @param directed boolean que representa si el grafo es dirigido o no
     */
    public GenerateGraph(int vertices, int aristas) 
    {
        this.vertices = vertices;
        this.aristas = aristas;
        this.densidad = calcularDensidad();

        if (!esNumeroAristasValido()) {
            throw new IllegalArgumentException("El número de aristas no es válido para un grafo conexo.");
        }
    }

    /**
     * GenerateGraph
     * 
     * Genera el grafo aleatorio con los parámetros dados y asegura su conectividad.
     * Agrega aristas adicionales aleatoriamente hasta alcanzar el número deseado de aristas.
     */
    public void GenerateGraphRandom() 
    {
        
        /*
         * Agregamos todos los vertices segun el parametro a la lista de vertices,
         * de esta manera colocamos todos los vertices necesarios para el grafo
         */
        for(int i = 1; i <= this.vertices; i++)
        {
            this.verticesList.add(String.valueOf(i));
        }
       
        
        /*
         * Creamos las conexiones, apartir del primer vertice con el resto de vertices.
         * Lo que hacemos es generar todas las posibles aristas y las guardamos en una lista.
         * Simplemente empezamos desde el primer vertice hasta el penultimo, y el siguiente vertice
         * lo tomamos desde el siguiente al actual hasta el ultimo vertice.
         */
        for(int i = 0; i < this.vertices - 1; i++)
        {
            for(int j = i + 1; j < this.vertices; j++)
            {
                this.edges.add(this.verticesList.get(i) + " " + this.verticesList.get(j));
            }

        }
        
        /**
         * Barajamos la lista de aristas para que no se repitan las mismas conexiones
         */        
        Collections.shuffle(this.edges);

        /**
         * Añadimos las aristas al grafo, de tal manera que se asegure la conectividad del grafo
         * Despues de agregar la conexión, eliminamos la arista de la lista de aristas.
         * De esta maneras tenemos un grafo conexo.
         */
        for(int i = 1; i < this.vertices; i++)
        {
            String from = this.verticesList.get(i); // origen
            String to = this.verticesList.get(random.nextInt(i)); // destino
            
            /*
             * Añadimos la conexión al grafo, con un peso aleatorio entre 1 y 100
             */
            this.conexiones.add(from + " " + to + " " + (Math.round((Math.random() * 100 + 1) * 1000) / 1000.0));

            /*
             * Eliminamos la arista de la lista de aristas, para que no se repita
             */
            if(from.compareTo(to) < 0)
            {
                this.edges.remove(from + " " + to);
            }
            else
            {
                this.edges.remove(to + " " + from);
            }
        }


        /*
         * Obtenemos el número de aristas que faltan para completar la densidad del grafo.
         * De la misma manera para que no se repitan las aristas, barajamos la lista de aristas.
         * Si no se barajan, se repetirán las mismas aristas que se han eliminado anteriormente.
         */
        
        int aristasActuales = this.conexiones.size();
        int aristasNecesarias = (int) (this.maximoAristas * this.densidad / 100);
        int aristasParaAgregar = aristasNecesarias - aristasActuales;      
        
        Collections.shuffle(this.edges);

        // Añadir aristas restantes para cumplir con la densidad
        for (int i = 0; (i < this.edges.size()) && (i < aristasParaAgregar); i++) {
            String[] parts = this.edges.get(i).split(" ");
            String from = parts[0];
            String to = parts[1];
            this.conexiones.add(from + " " + to + " " + (Math.round((Math.random() * 100 + 1) * 1000) / 1000.0));
        }

        this.SaveGraphRandom();
        this.SaveVisualizateGraph();
    }

    
    /**
     * GenerateVisualizateGraph
     * 
     * Metodo para generar el .dot para poder visualizar con Graphviz, el metodo
     * genera un nuevo archivo .dot con la nomenclatura necesaria para visualizarse.
     * 
     */
    public void SaveVisualizateGraph()
    {
        String archivo = "graph_visual_" + this.vertices + "n_" + this.aristas + "m.dot";
        
        /*
         * Cabecera para el archivo .dot
         */
        String cabecera = "digraph graph_visual_" + this.vertices + "n_" + this.vertices + "m {\r\n"
        		+ "\r\n"
        		+ "	layout=circo\r\n"
        		+ "	fontname=\"Helvetica,Arial,sans-serif\"\r\n"
        		+ "	node [fontname=\"Helvetica,Arial,sans-serif\", fontsize=19, style=filled, shape=circle, height=0.25, color=\"#76b5c5\", fillcolor=\"#b7d7e8\"]\r\n"
        		+ "	edge [fontname=\"Helvetica,Arial,sans-serif\", fontsize=12, color=\"#f2b1d8\", penwidth=2]\r\n"
        		+ "	rankdir=LR;\r\n"
        		+ "\r\n"
        		+ "\tnode [style=filled, shape=circle, height=0.25]\r\n"
        		+ "\tedge [dir=none]";
    	
    	try {
    		
    		File file = new File(this.carpeta_visual + archivo);
    		
    		 java.io.PrintWriter pw = new java.io.PrintWriter(file);

    		 pw.println(cabecera);
    		 
             for (String vertice : this.verticesList) {
                 pw.println("\t" + vertice + "\t[xlabel=" + vertice + ", label=\"\"]");
             }

             pw.println();
             
             for (String conexion : this.conexiones) {
            	 
            	 String[] data = conexion.split(" ");
                 pw.println("\t" + data[0] + " -> " + data[1] + " [label=" + data[2] + " ]");
             }
             
             pw.println("}");
             
             pw.close();
         } catch (java.io.FileNotFoundException e) {
             e.printStackTrace();
         }
    }
    
    /**
     * SaveVisualizateResultGraph
     * 
     * Metodo para generar el .dot para poder visualizar el resultado con Graphviz, el metodo
     * genera un nuevo archivo .dot con la nomenclatura necesaria para visualizarse.
     * 
     * @param grafo
     * @param resultado
     */
    public void SaveVisualizateResultGraph(RoadNetwork grafo, ArrayList<Arista> resultado)
    {
        String archivo = "graph_visual_" + grafo.numberOfVertices() + "n_" + (grafo.numberOfEdges()/2+1) + "m.dot";

        String cabecera = "digraph graph_visual_" + grafo.numberOfVertices() + "n_" + (grafo.numberOfEdges()/2+1) + "m {\r\n"
        + "\r\n"
        + "	layout=circo\r\n"
        + "	fontname=\"Helvetica,Arial,sans-serif\"\r\n"
        + "	node [fontname=\"Helvetica,Arial,sans-serif\", fontsize=19, style=filled, shape=circle, height=0.25, color=\"#76b5c5\", fillcolor=\"#b7d7e8\"]\r\n"
        + "	edge [fontname=\"Helvetica,Arial,sans-serif\", fontsize=12, color=\"#f2b1d8\", penwidth=2]\r\n"
        + "	rankdir=LR;\r\n"
        + "\r\n"
        + "\tnode [style=filled, shape=circle, height=0.25]\r\n"
        + "\tedge [dir=none]";


        try {
            File file = new File(this.carpeta_result + archivo);

            java.io.PrintWriter pw = new java.io.PrintWriter(file);

            // Agregamos la cabecera
            pw.println(cabecera);

            // Agregamos los distintos vertices
            for (String vertices : grafo.vertexSet()) {
                
                if(vertices.equals(grafo.getOrigen()))
                {
                    pw.println("\t" + vertices + "\t[xlabel=" + vertices + ", label=\"\" , fillcolor=\"#2ca02c\"]");
                }else{
                    pw.println("\t" + vertices + "\t[xlabel=" + vertices + ", label=\"\"]");
                }
                
            }

            pw.println();

            // Agregamos las distintas aristas, en este caso comprovamos si entra en el resultado, si entra en el 
            // resultado cambiamos el fillcolor
            for (String origen : grafo.vertexSet()) {
                for (String destino : grafo.getNeighbors(origen)) {
                    // Para grafos no dirigidos, aseguramos procesar cada arista una sola vez
                    if (origen.compareTo(destino) < 0) { 
                        double peso = grafo.getWeight(origen, destino);
                        Arista aristaActual = new Arista(origen, destino, peso);
                        Arista aristaInversa = new Arista(destino, origen, peso); // Arista inversa para verificar ya que sino no se verificaria el inverso
                        
                        // Verifica si la arista actual o su inversa está en el conjunto de soluciones
                        boolean esSolucion = resultado.contains(aristaActual) || resultado.contains(aristaInversa);

                        if (esSolucion) {
                            // Si está en el resultado, destacamos la arista con un color diferente
                            pw.println("\t" + origen + " -> " + destino + " [label=\"" + peso + "\", color=\"#1f77b4\", penwidth=3]");
                        } else {
                            // Si no está en el resultado, usamos el color y estilo predeterminados
                        	pw.println("\t" + origen + " -> " + destino + " [label=\"" + peso + "\", color=\"#f2b1d8\", penwidth=2]");
                        }
                    }
                }
            }

            pw.println("}");
             
            pw.close();
            
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * SaveGraphRandom
     * 
     * Método que guarda el grafo generado aleatoriamente
     */
    public void SaveGraphRandom()
    {
        String archivo = "graph_" + this.vertices + "n_" + this.aristas + "m.txt";

        try {

            File file = new File(this.carpeta + archivo);

            java.io.PrintWriter pw = new java.io.PrintWriter(file);

            /*
             * Para el guardado seguimos el esqueleto ofrecido en la practica
             */

            if(this.directed)
            {
                pw.println(1); // Dirigido
            }
            else
            {
                pw.println(0); // No dirigido
            }

            pw.println(this.vertices); // Número de vértices

            for (String vertice : this.verticesList) {
                pw.println(vertice);
            }

            pw.println(this.aristas); // Número de aristas del grafo

            for (String conexion : this.conexiones) {
                pw.println(conexion);
            }
            pw.close();
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * calculateDensidad
     * Calcula la densidad del grafo en base al número de vértices y aristas.
     * 
     * @return Densidad del grafo.
     */
    private double calcularDensidad() {
        int maxAristas = this.vertices * (this.vertices - 1) / 2;

        this.maximoAristas = maxAristas;
        this.minimoAristas = this.vertices - 1;

        return (double) this.aristas / maxAristas * 100;
    }

    /**
     * esNumeroAristasValido
     * 
     * Verifica si el número de aristas es válido para un grafo conexo no dirigido.
     * return boolean que indica si el número de aristas es válido
     */
    private boolean esNumeroAristasValido() {
        int minAristas = this.vertices - 1;
        int maxAristas = this.vertices * (this.vertices - 1) / 2;
        return this.aristas >= minAristas && this.aristas <= maxAristas;
    }


    /**
     * getVertices
     * 
     * Método que devuelve el número de vértices del grafo
     * @return int que representa el número de vértices del grafo
     */
    public int getVertices() {
        return vertices;
    }

    /**
     * setVertices
     * 
     * Método que establece el número de vértices del grafo
     * @param vertices int que representa el número de vértices del grafo
     */
    public void setVertices(int vertices) {
        this.vertices = vertices;
    }

    /**
     * getAristas
     * 
     * Método que devuelve el número de aristas del grafo
     * @return int que representa el número de aristas del grafo
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * setAristas
     * 
     * Método que establece el número de aristas del grafo
     * @param aristas int que representa el número de aristas del grafo
     */
    public void setAristas(int aristas) {
        this.aristas = aristas;
    }

    /**
     * getDensidad
     * 
     * Método que devuelve la densidad del grafo
     * @return double que representa la densidad del grafo
     */
    public double getDensidad() {
        return densidad;
    }

    /**
     * setDensidad
     * 
     * Método que establece la densidad del grafo
     * @param densidad double que representa la densidad del grafo
     */
    public void setDensidad(double densidad) {
        this.densidad = densidad;
    }
	
}
