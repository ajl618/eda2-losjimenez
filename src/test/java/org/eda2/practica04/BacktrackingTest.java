package org.eda2.practica04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * BacktrackingTest
 * 
 * Clase generada para la realizacion de los juegos de pruebas para comprobar el
 * correcto funcionamiento de los algoritmos de backtracking de la practica 04.
 * Para
 * realizar las pruebas se han utilizado los archivos de datos proporcionados en
 * el enlace adjunto a la memoria. Para conseguir unos test optimos se ha optado
 * por realizar un test para cada uno de los ejercicios propuestos en la
 * practica. Siendo test parametrizados para poder realizar las pruebas con
 * todos los archivos de datos.
 * 
 * @author Adrian Jimenez Benitez
 * @author Antonio Jose Jimenez Luque
 */
public class BacktrackingTest {

    private String dataset = System.getProperty("user.dir") + File.separator + "docs" + File.separator + "practica04"
            + File.separator + "dataset" + File.separator;

    @ParameterizedTest(name = "Ejercicio 01: archivo {0}")
    @CsvSource({ "p01, 87" , "p02, 85", "p03, 87", "p04, 509"})
    public void testEjercicio1(String basename, int resultadoEsperado) {
        MainBacktracking main = new MainBacktracking();
        main.setDataset(dataset + basename);

        main.loadFile();
        int resultado = main.greedyContLoading();

        assertEquals(resultado, resultadoEsperado);

    }

    @ParameterizedTest(name = "Ejercicio 02: archivo {0}")
    @CsvSource({ "p01, 100", "p02, 100", "p03, 100", "p04, 524"})
    public void testEjercicio2(String basename, int resultadoEsperado) {
    	
        MainBacktracking main = new MainBacktracking();
        main.setDataset(dataset + basename);

        main.loadFile();
        int resultado = main.backtracking1();

        assertEquals(resultado, resultadoEsperado);
        
    }

    @ParameterizedTest(name = "Ejercicio 03: archivo {0}")
    @CsvSource({ "p01, 100", "p02, 100", "p03, 100", "p04, 524"})
    public void testEjercicio3(String basename, int resultadoEsperado) {
    	
        MainBacktracking main = new MainBacktracking();
        main.setDataset(dataset + basename);

        main.loadFile();
        int resultado = main.backtracking2();

        assertEquals(resultado, resultadoEsperado);
        
    }

    @ParameterizedTest(name = "Ejercicio 04: archivo {0}")
    @CsvSource({ "p01, 100", "p02, 100", "p03, 100", "p04, 524"})
    public void testEjercicio4(String basename, int resultadoEsperado) {
    	
        MainBacktracking main = new MainBacktracking();
        main.setDataset(dataset + basename);

        main.loadFile();
        int resultado = main.backtracking3();

        assertEquals(resultado, resultadoEsperado);
        
    }

    @ParameterizedTest(name = "Ejercicio 05: archivo {0}")
    @CsvSource({ "p01, 100", "p02, 100", "p03, 100", "p04, 524"})
    public void testEjercicio5(String basename, int resultadoEsperado) {
        
        MainBacktracking main = new MainBacktracking();
        main.setDataset(dataset + basename);

        main.loadFile();
        int resultado = main.iterative();

        assertEquals(resultado, resultadoEsperado);

    }

    /*
     * -------------------------- METODOS AUXILIARES --------------------------
     */
    private double cargaValor(String basename) {
        List<Integer> list = new ArrayList<Integer>();
        double value = 0;
        try {
            Scanner sc = new Scanner(new File(dataset + basename + "_s.txt"));
            Scanner sc2 = new Scanner(new File(dataset + basename + "_p.txt"));
            while (sc.hasNextLine()) {
                int x = Integer.parseInt(sc.nextLine());
                double p = Double.parseDouble(sc2.nextLine());
                list.add(x);
                if (x == 1) {
                    value += p;
                }
            }
            sc.close();
            sc2.close();
            return value;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int[] loadSolucion(String basename) {
        List<Integer> list = new ArrayList<Integer>();
        try {
            Scanner sc = new Scanner(new File(dataset + basename + "_s.txt"));
            while (sc.hasNextLine()) {
                list.add(Integer.parseInt(sc.nextLine()));
            }
            int[] sol = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                sol[i] = list.get(i);
            }
            sc.close();
            return sol;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
