package utils;

import smile.math.Math;

/**
 * Created by Andrea De Castri on 09/11/2017.
 *
 */
public class Matrix {

    public static double[][] proximityMatrix(double[][] matrix){
        double[][] proximity = new double[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < i; j++){
                proximity[i][j] = Math.distance(matrix[i], matrix[j]);
                proximity[j][i] = Math.distance(matrix[i], matrix[j]);
            }
        }
        return proximity;
    }

    public static double[][] proximityHalfMatrix(double[][] matrix){
        double[][] proximity = new double[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            proximity[i] = new double[i+1];
            for (int j = 0; j < i; j++)
                proximity[i][j] = Math.distance(matrix[i], matrix[j]);
        }
        return proximity;
    }

    /*public static double[][] generateProximityMatrix(double[][] matrix){
        double[][] proximity = new double[matrix.length][matrix.length];

        for(int row=0; row<matrix.length; row++){
            for(int row2 = 0; row2 < matrix.length; row2++){
                double d = 0;
                for(int col = 0; col < matrix[0].length; col++){
                    d += Math.pow(matrix[row][col] - matrix[row2][col], 2);
                }
                double distance = Math.sqrt(d);
                proximity[row][row2] = distance;
                proximity[row2][row] = distance;
            }
        }

        return proximity;
    }*/

}
