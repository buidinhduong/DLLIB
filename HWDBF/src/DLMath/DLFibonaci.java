/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DLMath;

/**
 *
 * @author buiduong
 */
public class DLFibonaci {

    public static int Nb_Fibo(int n) {
        float a = 1;
        float b = 1;
        for (int i = 3; i <= n; i++) {
            b = a + b;
            a = b - a;
        }
        return (int)b;
    }

    public static int Nb_Fibo_R(int n) {
        float x;
        if (n <= 2) {
            x = 1;
        } else {
            x = Nb_Fibo_R(n - 1) + Nb_Fibo_R(n - 2);
        }
        return (int) x;

    }
}
