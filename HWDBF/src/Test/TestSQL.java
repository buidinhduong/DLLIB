/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import DLMath.DLFibonaci;
import java.util.Date;

/**
 *
 * @author buiduong
 */
public class TestSQL {

    public static void main(String[] arg) {
            int numberth=45;
            System.out.println(" ,Number Nb_Fibo:" + DLFibonaci.Nb_Fibo(numberth));
            double startTime = new Date().getTime();
            int number = DLFibonaci.Nb_Fibo_R(numberth);
            double endTime = new Date().getTime();
            System.out.printf("\n Number :"+number+"  Time in Nb_Fibo R: %.00f complexity"+1.68*numberth, endTime - startTime);
            
        
    }
}
