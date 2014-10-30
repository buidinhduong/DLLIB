/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import DLMath.DLMatrix;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author buiduong
 */
public class TestMaxtrix {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            int maxtrixRow = 3;
            int maxtrixCol = 3;
            int maxtrix1[][] = new int[maxtrixRow][maxtrixCol];
            int maxtrix2[][] = new int[maxtrixRow][maxtrixCol];
            int maxtrix3[][] = new int[3][1];
            System.out.print("row " + maxtrix3.length + " col" + maxtrix3[0].length);
            Random rand = new Random();
            //maxtrix 1/2
            for (int i = 0; i < maxtrixRow; i++) {
                for (int j = 0; j < maxtrixCol; j++) {
                    maxtrix1[i][j] = rand.nextInt(5) + 1;
                    maxtrix2[i][j] = rand.nextInt(5) + 1;
                }
            }
            for (int i = 0; i < maxtrix3.length; i++) {
                for (int j = 0; j < maxtrix3[0].length; j++) {
                    maxtrix3[i][j] = rand.nextInt(5) + 1;
                }
            }
            DLMatrix.displayMatrix(maxtrix1);
            DLMatrix.displayMatrix(maxtrix3);
            DLMatrix.displayMatrix(DLMatrix.multiplyMatrix(maxtrix1, maxtrix3));
            int[] d1 = {1, 1, 0, 1, 1, 1};
            int[] d2 = {0, 1, 1, 1, 1, 1};
            int[] d3 = {1, 0, 0, 1, 0, 0};
            int[] d4 = {0, 0, 1, 0, 1, 0};
            int[] d5 = {0, 0, 1, 0, 1, 1};
            int[] q = {0, 1, 1, 0, 1, 0};
            System.out.println("Distance :"+DLMatrix.distanceEuclidean(d1, q));
            System.out.println("Distance :"+DLMatrix.distanceEuclidean(d2, q));
            System.out.println("Distance :"+ DLMatrix.distanceEuclidean(d3, q));
            System.out.println("Distance :"+ DLMatrix.distanceEuclidean(d4, q));
            System.out.println("Distance :"+ DLMatrix.distanceEuclidean(d5, q));

        } catch (Exception ex) {
            Logger.getLogger(TestMaxtrix.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
