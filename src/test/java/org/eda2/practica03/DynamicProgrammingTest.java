package org.eda2.practica03;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.junit.jupiter.api.Test;

/**
 * GreedyTest
 * 
 * Clase generada para la realizacion de los juegos de pruebas para comprobar el
 * correcto funcionamiento de los algoritmos de PD de la practica 03. Para
 * realizar las pruebas se han utilizado los archivos de datos proporcionados en
 * el enlace adjunto a la memoria. Para conseguir unos test optimos se ha optado
 * por realizar un test para cada uno de los ejercicios propuestos en la
 * practica. Siendo test parametrizados para poder realizar las pruebas con
 * todos los archivos de datos.
 * 
 * @author Adrian Jimenez Benitez
 * @author Antonio Jose Jimenez Luque
 */

class DynamicProgrammingTest {

	private String dataset = System.getProperty("user.dir") + File.separator + "docs" + File.separator + "practica03"
			+ File.separator + "dataset" + File.separator;

	@ParameterizedTest(name = "Ejercicio 01: archivo {0}")
	@CsvSource({ "p01", "p02", "p03", "p04", "p05", "p06", "p07", "p08" })
	void testCamionRecursive(String basename) {
		DynamicProgramming dp = new DynamicProgramming(dataset + basename);
		double realvalue = cargaValor(basename);

		int[] p = new int[dp.getProfit().size()];
		double[] w = new double[dp.getWeight().size()];

		for (int i = 0; i < dp.getWeight().size(); i++) {
			p[i] = dp.getWeight().get(i);
		}

		for (int i = 0; i < dp.getProfit().size(); i++) {
			w[i] = dp.getProfit().get(i);
		}

		double value = dp.camionRecursive(dp.getProfit().size(), dp.getCapacidad(), p, w);
		assertTrue(realvalue == value);
	}

	@ParameterizedTest(name = "Ejercicio 02: archivo {0}")
	@CsvSource({ "p01", "p02", "p03", "p04", "p05", "p06", "p07", "p08" })
	void testCamionTable(String basename) {

		DynamicProgramming dp = new DynamicProgramming(dataset + basename);

		double realvalue = cargaValor(basename);

		int[] p = new int[dp.getProfit().size()];
		double[] w = new double[dp.getWeight().size()];

		for (int i = 0; i < dp.getWeight().size(); i++) {
			p[i] = dp.getWeight().get(i);
		}

		for (int i = 0; i < dp.getProfit().size(); i++) {
			w[i] = dp.getProfit().get(i);
		}

		double value = dp.camionTable(dp.getProfit().size(), dp.getCapacidad(), p, w);

		assertTrue(realvalue == value);
	}

	@ParameterizedTest(name = "Ejercicio 03: archivo {0}")
	@CsvSource({ "p01", "p02", "p03", "p04", "p05", "p06", "p07", "p08" })
	void testCamionDP(String basename) {
		DynamicProgramming dp = new DynamicProgramming(dataset + basename);
		int[] realsol = loadSolucion(basename);
		double realvalue = cargaValor(basename);

		int[] p = new int[dp.getProfit().size()];
		double[] w = new double[dp.getWeight().size()];

		for (int i = 0; i < dp.getWeight().size(); i++) {
			p[i] = dp.getWeight().get(i);
		}

		for (int i = 0; i < dp.getProfit().size(); i++) {
			w[i] = dp.getProfit().get(i);
		}

		double value = dp.camionDP(dp.getCapacidad(), p, w);
		
		assertTrue(realvalue == value);
		assertTrue(realsol.length == dp.getSolucion().length);
		assertArrayEquals(realsol, dp.getSolucion());
	}
	
	@ParameterizedTest(name = "Ejercicio 05: archivo {0}")
	@CsvSource({ "p01", "p02", "p03", "p04", "p05", "p06", "p07", "p08" })
	void testCamionDP2(String basename) {
		DynamicProgramming dp = new DynamicProgramming(dataset + basename);
		int[] realsol = loadSolucion(basename);
		double realvalue = cargaValor(basename);

		int[] p = new int[dp.getProfit().size()];
		double[] w = new double[dp.getWeight().size()];

		for (int i = 0; i < dp.getWeight().size(); i++) {
			p[i] = dp.getWeight().get(i);
		}

		for (int i = 0; i < dp.getProfit().size(); i++) {
			w[i] = dp.getProfit().get(i);
		}

		double value = dp.camionDP2(dp.getCapacidad(), p, w, false);
		
		assertTrue(realvalue == value);
		assertTrue(realsol.length == dp.getSolucion().length);
	}
	
	@ParameterizedTest(name = "Ejercicio 06: archivo {0}")
	@CsvSource({ "p01", "p02", "p03", "p04", "p05", "p06", "p07", "p08" })
	void testCamionDP3(String basename) {
		DynamicProgramming dp = new DynamicProgramming(dataset + basename);
		int[] realsol = loadSolucion(basename);
		double realvalue = cargaValor(basename);

		int[] p = new int[dp.getProfit().size()];
		double[] w = new double[dp.getWeight().size()];

		for (int i = 0; i < dp.getWeight().size(); i++) {
			p[i] = dp.getWeight().get(i);
		}

		for (int i = 0; i < dp.getProfit().size(); i++) {
			w[i] = dp.getProfit().get(i);
		}

		double value = dp.camionDP3(dp.getCapacidad(), p, w);
		
		assertTrue(realvalue == value);
		assertTrue(realsol.length == dp.getSolucion().length);
	}
	
	@ParameterizedTest(name = "Ejercicio 07: archivo {0}")
	@CsvSource({"p01, 644.0", "p02, 51.0", "p03, 155.0", "p04, 107.0", "p05, 1410.0", "p06, 1768.0", "p07, 1488.0", "p08, 1.406338E7"})
	void testCamionDPMP(String basename, double realValue) {
		DynamicProgramming dp = new DynamicProgramming(dataset + basename);

		int[] p = new int[dp.getProfit().size()];
		double[] w = new double[dp.getWeight().size()];

		for (int i = 0; i < dp.getWeight().size(); i++) {
			p[i] = dp.getWeight().get(i);
		}

		for (int i = 0; i < dp.getProfit().size(); i++) {
			w[i] = dp.getProfit().get(i);
		}

		double value = dp.camionDPMP(dp.getCapacidad(), p, w);
		
		assertTrue(realValue == value);
	}

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