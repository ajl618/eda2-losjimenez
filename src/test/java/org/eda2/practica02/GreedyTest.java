package org.eda2.practica02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * GreedyTest
 * 
 * Clase generada para la realizacion de los juegos de pruebas para comprobar el
 * correcto funcionamiento de los algoritmos
 * de la practica 02.
 * 
 * @author Adrian Jimenez Benitez
 * @author Antonio Jose Jimenez Luque
 */

class GreedyTest {

    
    //Funcionamiento del loadNetwork
    @Test
    public void testLoadNetworkArchivoInvalido() {
        RoadNetwork roadNetwork = new RoadNetwork();
        String archivo = "archivo_invalido.txt";
        boolean cargado = roadNetwork.loadNetwork(archivo);
        assertFalse(cargado, "Se cargó incorrectamente una red de carreteras desde un archivo inválido");
    }


    @Test
    public void testLoadVertex() {
        RoadNetwork roadNetwork = new RoadNetwork();
        roadNetwork.loadNetwork(roadNetwork.getCarpeta() + "graphEDAland.txt");

        assertEquals(roadNetwork.containsVertex("Almeria"), true);

        assertEquals(roadNetwork.containsVertex("Cordoba"), false);

        assertEquals(roadNetwork.containsVertex("123"), false);
    }

    @Test
    public void testLoadEdges() {
        RoadNetwork roadNetwork = new RoadNetwork();
        roadNetwork.loadNetwork(roadNetwork.getCarpeta() + "graphEDAland.txt");

        assertEquals(roadNetwork.containsEdge("Almeria", "Granada"), true);

        assertEquals(roadNetwork.containsEdge("Almeria", "Huelva"), false);
    }

    @Test
    public void testLoadDirected() {
        RoadNetwork roadNetwork = new RoadNetwork();
        roadNetwork.loadNetwork(roadNetwork.getCarpeta() + "graphEDAland.txt");

        assertEquals(roadNetwork.getDirected(), false);
    }

    @Test
    public void testLoadNumVertices() {
        RoadNetwork roadNetwork = new RoadNetwork();
        roadNetwork.loadNetwork(roadNetwork.getCarpeta() + "graphEDAland.txt");

        assertEquals(roadNetwork.numberOfVertices(), 21);
    }

    @Test
    public void testLoadRemoveEdge() {
        RoadNetwork roadNetwork = new RoadNetwork();
        roadNetwork.loadNetwork(roadNetwork.getCarpeta() + "graphEDAland.txt");

        assertEquals(roadNetwork.removeEdge("Almeria", "Granada"), true);
        assertEquals(roadNetwork.containsEdge("Almeria", "Granada"), false);
    }

    @Test
    public void testSetAndGetOrigen() {
        RoadNetwork roadNetwork = new RoadNetwork();
        roadNetwork.setOrigen("Madrid");
        assertEquals("Madrid", roadNetwork.getOrigen(), "El origen no se estableció correctamente");
    }

    //Funcionamiento del algoritmo TSP Greedy
    @Test
    public void testTSPGreedy() {
        RoadNetwork roadNetwork = new RoadNetwork();
        roadNetwork.loadNetwork(roadNetwork.getCarpeta() + "graphEDAland.txt");
        roadNetwork.setOrigen("Almeria");
        
        assertDoesNotThrow(() -> roadNetwork.TSPGreedy(false), "Error al ejecutar el algoritmo TSP Greedy sin visualización");
        assertDoesNotThrow(() -> roadNetwork.TSPGreedy(true), "Error al ejecutar el algoritmo TSP Greedy con visualización");
    }




}
