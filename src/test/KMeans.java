package test;

import org.jxmapviewer.viewer.GeoPosition;
import waypoint.MyWaypoint;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeans {
    private final int nClusters;
    private final int maxIters;
    private double[][] centroids;

    public KMeans(int nClusters, int maxIters) {
        this.nClusters = nClusters;
        this.maxIters = maxIters;
    }

    public void fit(double[][] X) {
        Random random = new Random();
        centroids = new double[nClusters][X[0].length];
        for (int i = 0; i < nClusters; i++) {
            centroids[i] = Arrays.copyOf(X[random.nextInt(X.length)], X[0].length);
        }

        for (int iter = 0; iter < maxIters; iter++) {
            int[] labels = assignLabels(X);

            double[][] newCentroids = updateCentroids(X, labels);

            if (Arrays.deepEquals(centroids, newCentroids)) {
                break;
            }

            centroids = newCentroids;
        }
    }

    private int[] assignLabels(double[][] X) {
        double[][] distances = new double[X.length][nClusters];
        for (int i = 0; i < X.length; i++) {
            for (int j = 0; j < nClusters; j++) {
                distances[i][j] = euclideanDistance(X[i], centroids[j]);
            }
        }

        int[] labels = new int[X.length];
        for (int i = 0; i < X.length; i++) {
            labels[i] = findMinIndex(distances[i]);
        }

        return labels;
    }

    private double[][] updateCentroids(double[][] X, int[] labels) {
        double[][] newCentroids = new double[nClusters][X[0].length];

        for (int i = 0; i < nClusters; i++) {
            int clusterSize = countOccurrences(labels, i);
            for (int j = 0; j < X[0].length; j++) {
                double sum = 0;
                for (int k = 0; k < X.length; k++) {
                    if (labels[k] == i) {
                        sum += X[k][j];
                    }
                }
                newCentroids[i][j] = sum / clusterSize;
            }
        }

        return newCentroids;
    }

    private double euclideanDistance(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(sum);
    }

    private int findMinIndex(double[] array) {
        int minIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[minIndex]) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    private int countOccurrences(int[] array, int target) {
        int count = 0;
        for (int value : array) {
            if (value == target) {
                count++;
            }
        }
        return count;
    }

    public static void calcKmeans(Main main) {
        DataFrame df = new DataFrame("C:\\Users\\jerdna\\IdeaProjects\\project_prog3\\in\\germany.json");
        System.out.println();
        List<Object> laColumn = df.getColumn("la");
        List<Object> loColumn = df.getColumn("lo");
        List<Object> name = df.getColumn("{{name");
        System.out.println(laColumn.get(1).getClass());
        double[][] X = new double[laColumn.size()][2];
        for (int i = 0; i < laColumn.size(); i++) {
            X[i][0] = Double.parseDouble(laColumn.get(i).toString());
            X[i][1] = Double.parseDouble(loColumn.get(i).toString().replace("}",""));
        }
        int clusters = 25;
        int maxiters = 1000;
        KMeans kMeans = new KMeans(clusters, maxiters);
        Color[] colors = new Color[clusters];
        Random rand = new Random();

        for (int i = 0; i < clusters; i++) {
            colors[i]=new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat());
        }
        kMeans.fit(X);

        int[] labels = kMeans.assignLabels(X);


        for (int i = 0; i < laColumn.size(); i++) {
            main.addWaypoint(new MyWaypoint((String) name.get(i),new GeoPosition(X[i][0],X[i][1]),colors[labels[i]]));
        }
        System.out.println("Cluster Assignments: " + Arrays.toString(labels));
        System.out.println("Final Centroids: " + Arrays.deepToString(kMeans.centroids));
    }
}