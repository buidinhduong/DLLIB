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
            int maxtrixRow = 1;
            int maxtrixCol = 4;
            int maxtrix1[][] = new int[maxtrixRow][maxtrixCol];
            int maxtrix2[][] = new int[maxtrixRow][maxtrixCol];
            int maxtrix3[][] = new int[4][5];
            System.out.print("row "+maxtrix3.length+" col"+maxtrix3[0].length);
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
        } catch (Exception ex) {
            Logger.getLogger(TestMaxtrix.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
}
