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
            double startTime1 = new Date().getTime();
            System.out.println("\n===============Nb_Fibo======");
            System.out.println("Number at:["+numberth+"] is :" + DLFibonaci.Nb_Fibo(numberth));
            double endTime1 = new Date().getTime();
            System.out.printf("\nComputation Time: %.00f (ms)",endTime1 - startTime1);
            System.out.println("\n================Nb_Fibo_R====");
            double startTime = new Date().getTime();
            int number = DLFibonaci.Nb_Fibo_R(numberth);
            double endTime = new Date().getTime();
            System.out.printf("\nNumber at:["+numberth+"] is:"+number+"\nComputation Time: %.00f(ms)", endTime - startTime);
            System.out.print("\n");
            
    }
}
