package org.eda2.practica02;

import java.util.Comparator;

/**
 * Arista
 * 
 * Clase que representa una arista de un grafo.
 * 
 * @author Antonio Jose Jimenez Luque
 * @author Adrian Jimenez Benitez
 * @param <String>
 */
public class Arista implements Comparable<Arista>, Comparator<Arista>{
    
    // Initialize variables
    private String origen;
    private String destino;
    private double peso;
    
    /**
     * Constructor
     * 
     * Metodo para la creacion de una arista.
     * @param origen String que representa el origen de la arista.
     * @param destino String que representa el destino de la arista.
     * @param peso double que representa el peso de la arista.
     */
    public Arista(String origen, String destino, double peso)
    {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }   

    /**
     * Metodo getOrigen
     * 
     * Metodo que devuelve el origen de la arista.
     * @return String que representa el origen de la arista.
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Metodo setOrigen
     * 
     * Metodo que establece el origen de la arista.
     * @param origen String que representa el origen de la arista.
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }

    /**
     * Metodo getDestino
     * 
     * Metodo que devuelve el destino de la arista.
     * @return String que representa el destino de la arista.
     */
    public String getDestino() {
        return destino;
    }

    /**
     * Metodo setDestino
     * 
     * Metodo que establece el destino de la arista.
     * @param destino String que representa el destino de la arista.
     */
    public void setDestino(String destino) {
        this.destino = destino;
    }

    /**
     * Metodo getPeso
     * 
     * Metodo que devuelve el peso de la arista.
     * @return double que representa el peso de la arista.
     */
    public double getPeso() {
        return peso;
    }

    /**
     * Metodo setPeso
     * 
     * Metodo que establece el peso de la arista.
     * @param peso double que representa el peso de la arista.
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * Metodo toString
     * 
     * Metodo que devuelve una cadena de texto con la informacion de la arista.
     * @return String que representa la informacion de la arista.
     */
    @Override
    public String toString() {
        return "Arista [Origen=" + this.origen + ", Destino=" + this.destino + ", Peso=" + this.peso + "]";
    }

    /**
     * Metodo compareTo
     * 
     * Metodo que compara dos aristas.
     * En primer lugar comparamos por peso, si el peso es igual, comparamos por origen y si el origen es igual, comparamos por destino.
     * @param o Arista que se compara con la arista actual.
     * @return int que indica si la arista actual es mayor, menor o igual a la arista o.
     */
        @Override
        public int compareTo(Arista o) {
            return Double.compare(this.peso, o.peso);
        }

    /**
     * Metodo equals
     * 
     * Metodo que compara dos aristas. 
     * @param obj Arista que se compara con la arista actual.
     * @return boolean que indica si las aristas son iguales.
     */
    @Override
    public boolean equals(Object o) {
    	Arista a = (Arista) o;
    	if(this.getDestino().equals(a.getDestino()) && this.getOrigen().equals(a.getOrigen()) && this.getPeso()==a.getPeso() ) return true;
    	return false;
    }

    @Override
    public int compare(Arista o1, Arista o2) {
        return o1.compareTo(o2);
    }
    

}
