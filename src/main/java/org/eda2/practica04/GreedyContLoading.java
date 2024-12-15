package org.eda2.practica04;

import java.util.Arrays;

/**
 * GreedyContLoading
 * 
 * Clase que implementa el algoritmo greedy para el problema de la carga contenerizada.
 * Esta clase se ha realizado para solventar el primer ejercicio de la práctica 4 de la asignatura de Estructuras de Datos y Algoritmos 2.
 * El buque podrá cargarse por etapas y en cada etapa se cargará un contenedor, decidiendo cuál es el contenedor más apropiado. Para esta decisión
 * se seleccionará el contenedor de menos peso, luego el siguiente de peso más pequenio y así sucevismamente hasta que se hayan cargado todos
 * los contenedores o no haya suficiente capacidad en el buque para el siguiente.
 * 
 * 
 * @author Antonio José Jiménez Luque
 * @author Adrián Jiménez Benítez
 */
public class GreedyContLoading {
    
    /**
     * Constructor de la clase GreedyContLoading
     */
    public GreedyContLoading() {
    }

    /**
     * greedyContLoading
     * 
    * Carga los contenedores en un buque utilizando un enfoque greedy para maximizar
    * el número de contenedores cargados sin exceder la capacidad del buque.
    * Los contenedores se cargan en orden de menor a mayor peso.
    *
    * @param contenedores Array de enteros donde cada entero representa el peso de un contenedor.
    * @param capacidadBuque Capacidad máxima de peso que el buque puede cargar.
    * @return Un array que contiene los pesos de los contenedores efectivamente cargados en el buque.
    *         Este array tiene la misma longitud que el array de entrada, con ceros al final si
    *         no todos los contenedores fueron cargados.
    */
    public int greedyContLoading(int[] contenedores, int capacidadBuque) {

        // Ordenamos los contenedores de menor a mayor peso.
        Arrays.sort(contenedores); // O(n log n)

        // Inicializa el array que almacenará los pesos de los contenedores cargados.
        int[] contenedoresCargados = new int[contenedores.length];
        int pesoCarga = 0; // Lleva la cuenta del peso total cargado en el buque hasta el momento.
        int contenedoresCargadosIndex = 0; // Índice para el array de contenedores cargados.
        int contenedoresIndex = 0; // Índice para iterar sobre el array de contenedores.

        // Continúa cargando contenedores mientras haya contenedores disponibles y espacio en el buque.
        while (contenedoresIndex < contenedores.length && pesoCarga + contenedores[contenedoresIndex] <= capacidadBuque) {

            contenedoresCargados[contenedoresCargadosIndex] = contenedores[contenedoresIndex]; // Carga el contenedor actual en el buque.
            pesoCarga += contenedores[contenedoresIndex]; // Aumenta el peso total cargado con el peso del contenedor actual.
            contenedoresCargadosIndex++; // Avanza al siguiente índice en el array de contenedores cargados.
            contenedoresIndex++; // Avanza al siguiente contenedor.

        }

        return pesoCarga;

    }



}
