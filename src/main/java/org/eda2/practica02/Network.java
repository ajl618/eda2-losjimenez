package org.eda2.practica02;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Stack;


/**
 * 
 * Clase Network
 * 
 * 
 * @author Antonio Jose Jimenez Luque
 * @author Adrian Jimenez Benitez
 * @param <String>
 */
public class Network implements Graph<String>, Iterable<String> {

	private boolean directed; 	
	
	protected TreeMap<String, TreeMap<String, Double>> adjacencyMap; 
	
	/**
	 * Constructor
	 */
	public Network(){
		this.directed = true;
		this.adjacencyMap = new TreeMap<String, TreeMap<String, Double>>();
	}
	
 
  	public void setDirected(boolean uD_Or_D) {
  		this.directed = uD_Or_D;
  	}

  	public boolean getDirected() {
  		return this.directed;
  	}

  	@Override
  	public boolean isEmpty() {
    	return this.adjacencyMap.isEmpty();
  	} 

  	@Override
  	public void clear() {
		this.adjacencyMap.clear();
	}

  	@Override
  	public int numberOfVertices() {
    	return this.adjacencyMap.size();
  	}

  	@Override
  	public int numberOfEdges() {
  		int count = 0;
  		for (TreeMap<String, Double> itMap : this.adjacencyMap.values())
  			count += itMap.size();
  		return count;
  	} 

  	@Override
  	public boolean containsVertex(String string) {
    	return this.adjacencyMap.containsKey(string);
  	} 
  	
  	@Override
  	public boolean containsEdge(String v1, String v2) {
  		TreeMap<String, Double> neighbors = this.adjacencyMap.get(v1);
  		if (neighbors == null) return false;
    	return neighbors.containsKey(v2);
  	} 

  	@Override
  	public double getWeight (String v1, String v2) {
  		TreeMap<String, Double> neighbors = this.adjacencyMap.get(v1);
  		if (neighbors == null) return -1;
  		Double weight = neighbors.get(v2);
  		return weight == null ? -1 : weight;
   	} 

  	@Override
  	public double setWeight (String v1, String v2, double w) {
  		TreeMap<String, Double> neighbors = this.adjacencyMap.get(v1);
  		if (neighbors == null) return -1;
  		Double oldWeight = neighbors.get(v2);
  		return oldWeight == null ? -1 : neighbors.put(v2, w);
	}

  	public boolean isAdjacent (String v1, String v2) {
  		TreeMap<String, Double> neighbors = this.adjacencyMap.get(v1);
  		return neighbors == null ? false : neighbors.containsKey(v2);
 
	}

  	public boolean addVertex(String String) {
        if (this.adjacencyMap.containsKey(String)) return false;
        this.adjacencyMap.put(String, new TreeMap<String, Double>());
        return true;
  	} 

  	public boolean addEdge(String v1, String v2, double w) {
  		if (!containsVertex(v1) || !containsVertex(v2)) return false;
  		this.adjacencyMap.get(v1).put(v2, w);
       	if (!this.directed) {//Matriz simétrica
        	this.adjacencyMap.get(v2).put(v1, w);
       	}
    	return true;
  	} 

  	public boolean removeVertex(String String) {
        if (!containsVertex(String)) return false;

        for (TreeMap<String, Double> itMap: this.adjacencyMap.values()) {
        	itMap.remove(String);
        } 
        this.adjacencyMap.remove(String);
        return true;
   	} 

  	public boolean removeEdge (String v1, String v2) {
    	if (!containsEdge(v1,v2)) return false;

    	this.adjacencyMap.get(v1).remove(v2);
    	
    	if (!this.directed) {
        	this.adjacencyMap.get(v2).remove(v1);    		
    	}
    	
    	return true;
  	} 
  	
	@Override
  	public TreeSet<String> vertexSet() {
    	return new TreeSet<String>(this.adjacencyMap.keySet());
  	}

  	/**
  	 *  Returns a TreeSet link of the neighbors of a specified String object.
  	 *
  	 *  @param v - the String object whose neighbors are returned.
  	 *
  	 *  @return a TreeSet of the vertices that are neighbors of v.
   	 */

  	public TreeSet<String> getNeighbors(String v) {
  		TreeMap<String, Double> neighbors = this.adjacencyMap.get(v);//...
  		if(neighbors == null) return null;
  		TreeSet<String> data = new TreeSet<>();
  		for(String dataString : neighbors.keySet()) {
  			data.add(dataString);
  		}
  		return data == null ? null : data; //...
  	}

  	/**
  	 * toString
  	 */
  	@Override
  	public String toString() {
    	return this.adjacencyMap.toString();
  	} 

  	//toArray() methods
	
	//DF = depthFirst (en profundidad)
	//BF = breadthFirst (en anchura)
  
  	public ArrayList<String> toArrayDFRecursive(String start) {
  		if (!this.adjacencyMap.containsKey(start)) return null;
  		ArrayList<String> result = new ArrayList<String>();
		TreeMap<String,Boolean> visited = new TreeMap<String, Boolean>();
		for (String v : this.adjacencyMap.keySet()){
			visited.put(v,false);
		}
		
		toArrayDFRecursive(start, result, visited);
		return result;
	}
	
	private void toArrayDFRecursive(String current, ArrayList<String> result, TreeMap<String,Boolean> visited) {
		result.add(current);
		visited.put(current, true);
		for (String to : this.adjacencyMap.get(current).descendingKeySet()) {
			if (visited.get(to)) continue;
			toArrayDFRecursive(to, result, visited);
		}
	}
	
	public ArrayList<String> toArrayDF(String start) {
		if (!this.adjacencyMap.containsKey(start)) return null;
		ArrayList<String> resultado = new ArrayList<String>();
		Stack<String> stack = new Stack<String>();
		TreeMap<String, Boolean> visited = new TreeMap<String, Boolean>();
		for (String String : this.adjacencyMap.keySet()) {
			visited.put(String, false);
		}
		String current;
		
		stack.push(start);
		while (!stack.isEmpty()) {
			current = stack.pop();
			if (visited.get(current)) continue;
			visited.put(current, true);
			resultado.add(current);
			for (String to : this.adjacencyMap.get(current).keySet()) {
    			if (visited.get(to)) continue;
      			stack.push(to);
			}
		}
		return resultado;
	}
	
	public ArrayList<String> toArrayBF(String start) {
		if (!this.adjacencyMap.containsKey(start)) return null;
		ArrayList<String> resultado = new ArrayList<String>();
		LinkedList<String> queue = new LinkedList<String>();
		TreeMap<String, Boolean> visited = new TreeMap<String, Boolean>();
		for (String String: this.adjacencyMap.keySet()) {
			visited.put(String, false);
		}
		String current;
		queue.add(start);
		while (!queue.isEmpty()) {
			current = queue.poll();
			if (visited.get(current)) continue;
			visited.put(current, true);
			resultado.add(current);
        	for (String to : adjacencyMap.get(current).keySet()) {
    			if (visited.get(to)) continue;
   				queue.add(to);
        	}
		}
		return resultado;
	}
	
	
 	////Iteradores
	
	@Override
	public Iterator<String> iterator() { //Iterador sobre el conjunto de claves --> orden lexicografico
        return this.adjacencyMap.keySet().iterator();
  	} 

  	public ArrayList<String> breadthFirstIterator (String v) { //Iterador en anchura a partir de v
    	if (!adjacencyMap.containsKey(v)) return null;
    	return this.toArrayBF(v);
  	} 


  	public ArrayList<String> depthFirstIterator (String v) { //Iterador en profundidad a partir de v
    	if (!adjacencyMap.containsKey (v)) return null;
    	return this.toArrayDFRecursive(v);
  	}
	
 } // class Network
