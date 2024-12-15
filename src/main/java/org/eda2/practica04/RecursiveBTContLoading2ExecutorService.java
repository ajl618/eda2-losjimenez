package org.eda2.practica04;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class RecursiveBTContLoading2ExecutorService {

    static int n;
    static int[] p;
    static int C;
    static int pact;
    static int voa;
    static int pesoRestante;
    static ExecutorService executor;

    /**
     * maxLoading
     *
     * Calcula la carga máxima que puede soportar el barco usando un algoritmo de
     * backtracking paralelizado.
     *
     * @param pesos     Array con los pesos de los contenedores
     * @param capacidad Capacidad máxima del barco
     * @return La carga máxima que se puede cargar en el barco
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static int maxLoading(int[] pesos, int capacidad) throws InterruptedException, ExecutionException {
        n = pesos.length - 1;
        p = pesos;
        C = capacidad;
        pact = 0;
        voa = 0;
        pesoRestante = 0;
        executor = Executors.newCachedThreadPool(); // Crea un pool de hilos para ejecutar tareas en paralelo

        for (int i = 1; i < p.length; i++) {
            pesoRestante += p[i];
        }

        // Inicia la tarea de carga recursiva en un hilo separado
        Future<Integer> result = executor.submit(new LoadingTask(1, pesoRestante, pact));
        voa = result.get(); // Espera a que la tarea termine y obtiene el resultado

        executor.shutdown();
        return voa;
    }

    /**
     * LoadingTask
     *
     * Clase que representa una tarea de carga de contenedores utilizando
     * backtracking. Implementa Callable para poder ser ejecutada en un hilo y
     * devolver un resultado.
     */
    static class LoadingTask implements Callable<Integer> {
        int currentLevel;
        int pesoRestante;
        int pact;

        /**
         * Constructor para LoadingTask
         *
         * @param currentLevel  Nivel actual en el árbol de decisiones
         * @param pesoRestante  Peso total de los contenedores aún no considerados
         * @param pact          Peso actual cargado en el barco
         */
        LoadingTask(int currentLevel, int pesoRestante, int pact) {
            this.currentLevel = currentLevel;
            this.pesoRestante = pesoRestante;
            this.pact = pact;
        }

        @Override
        public Integer call() throws Exception {
            if (pact + pesoRestante <= voa) return voa;

            if (currentLevel > n) {
                synchronized (RecursiveBTContLoading2Thread.class) {
                    if (pact > voa) voa = pact;
                }
                return voa;
            }

            // Create a list to hold futures
            List<Future<Integer>> futures = new ArrayList<>();

            if (pact + p[currentLevel] <= C) {
                futures.add(executor.submit(new LoadingTask(currentLevel + 1, pesoRestante - p[currentLevel], pact + p[currentLevel])));
            }

            futures.add(executor.submit(new LoadingTask(currentLevel + 1, pesoRestante - p[currentLevel], pact)));

            // Wait for all tasks to complete and get the maximum result
            for (Future<Integer> future : futures) {
                int result = future.get();
                synchronized (RecursiveBTContLoading2Thread.class) {
                    if (result > voa) voa = result;
                }
            }

            return voa;
        }
    }

    /** Test program */
    public static void main(String[] args) {
		int [] w = {0, 2, 2, 6, 5, 5, 3, 1, 4, 7, 9, 8, 5, 5};
		int C = 45;

        try {
            System.out.println("Value of max loading is " + maxLoading(w, C));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
