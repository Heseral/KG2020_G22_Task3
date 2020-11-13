package util;

import pixels.ScreenPoint;

public abstract class Misc {
    public static double[][] multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix) {
        if (firstMatrix.length != secondMatrix[0].length) {
            return null;
        }
        double[][] result = new double[firstMatrix.length][secondMatrix[0].length];
        for (int i = 0; i < firstMatrix.length; i++) {
            for (int j = 0; j < secondMatrix[0].length; j++) {
                for (int k = 0; k < secondMatrix.length; k++) {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }
        return result;
    }

    public static void transformByMultiplication(ScreenPoint screenPoint, double[][] matrix) {
        screenPoint.setX((int) Math.round(screenPoint.getX() * matrix[0][0] + screenPoint.getY() * matrix[1][0] + matrix[2][0]));
        screenPoint.setY((int) Math.round(screenPoint.getX() * matrix[0][1] + screenPoint.getY() * matrix[1][1] + matrix[2][1]));
    }
}
