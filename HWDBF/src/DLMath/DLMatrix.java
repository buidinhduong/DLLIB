/*
BUI DINH DUONG's Math Package
Email : dinhduong.bui@gmail.com
 */

package DLMath;

/**
 *
 * @author buiduong
 */
public class DLMatrix {
    public static void displayMatrix(int[][] maxtrix) {
        System.out.print("\n=====Maxtrix....");
        for (int i = 0; i < maxtrix.length; i++) {//all rows
            System.out.println("");
            for (int j = 0; j < maxtrix[0].length; j++) {
                if(j==0)
                System.out.print("[");
                else if(j==maxtrix[0].length-1)
                System.out.print("]");
                else System.out.print(" "+maxtrix[i][j]);
            }
        }
    }

    public static int[][] multiplyMatrix(int[][] maxtrix1, int[][] maxtrix2) throws Exception {
        System.out.println("\n---start----");

        //if M*N x N*P => M*P maxtrix
        if (maxtrix1[0].length != maxtrix2.length) {
            throw new Exception("Can only mutiply two maxtrix M*N x N*P => M*P ");
        }
        int maxRows = maxtrix1.length;
        int maxCols = maxtrix2[0].length;
        int resutlMaxtrix[][] = new int[maxRows][maxCols];
        for (int row1 = 0; row1 < maxRows; row1++) {
            for (int c = 0; c < maxCols; c++) {
                // loop all rows maxtrix 1
                int sum = 0;
                int[] rowvectorM1 = getRowVector(maxtrix1, row1);
                int[] colvectorM2 = getColVector(maxtrix2, c);
                sum += multiplyVector(rowvectorM1, colvectorM2);
                resutlMaxtrix[row1][c] = sum;
            }

        }
        System.out.println("stop----");
        return resutlMaxtrix;
    }

    public static int[] getRowVector(int[][] matrix, int row) {
        int[] vector = new int[matrix[0].length];
        for (int i = 0; i < matrix[0].length; i++)//all rows 
        {
            vector[i] = matrix[row][i];
        }
        return vector;
    }

    public static int[] getColVector(int[][] matrix, int col) {
        int[] vector = new int[matrix.length];
        for (int row = 0; row < matrix.length; row++)//all rows 
        {
            vector[row] = matrix[row][col];
        }
        return vector;
    }

    public static int multiplyVector(int vector1[], int vector2[]) {
        int result = 0;
        for (int i = 0; i < vector1.length; i++) {
            result += vector1[i] * vector2[i];
        }
        return result;
    }
}
