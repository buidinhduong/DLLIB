/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorycontrolmanagement;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author buiduong
 */
public class Model {

    int K;
    int M;
    int N;
    float[] p;
    public final float NotAvaiable = -1234;

    public Model(int k, int m, int n, float[] prob) {
        this.K = k;
        this.M = m;
        this.N = n;
        this.p = prob;
    }

    float c(int u) {
        return (float) 2 * u;
    }

    float O(int u) {
        if (u != 0) {
            return K + this.c(u);
        }
        return 0;

    }

    float g(int u) {
        return 0;
    }

    float h(int u) {
        return u;
    }

    float f(int u) {
        return 8 * u;
    }

    float p(int j) {
        return p[j];
    }

    float[][] optimal_MD() {
        float matrixR[][] = new float[K][K+2];
        
        return matrixR;
    }

    float transitionProbabilities(int sa, int j) throws Exception {
        //j: new state , in other word: stock at new state
        //sa = s + a
        //a: number of unit need to be ordered to sastified demand
        //s: stock in hand at current state
        if (M >= j && j > sa) {
            return 0;
        }
        if (M >= sa && sa >= j && j > 0) {
            return this.p(sa - j);
        }
        if (M >= sa && j == 0) {
            return this.q(sa);
        }
        throw new Exception("TransitionProbabilities Not any case");
    }

    float q(int u) {
        float sum = 0;
        for (int j = 0; j <= u - 1; j++) {
            sum += this.p(j);
        }
        return 1 - sum;
    }

    float F(int u) {
        float expectedRevenue = 0;
        float sum_f_p = 0;
        for (int j = 0; j <= u - 1; j++) {
            sum_f_p += this.f(j) * this.p(j);
        }
        sum_f_p += (this.f(u) * this.q(u));
        expectedRevenue += sum_f_p;
        return expectedRevenue;
    }

    float[] getTableFu() {
        float[] Fu = new float[K];
        for (int u = 0; u < K; u++) {
            Fu[u] = this.F(u);
        }
        return Fu;
    }

    float[][] getTableR() {
        float matrixR[][] = new float[K][K];
        for (int s = 0; s < K; s++) {
            for (int a = 0; a < K; a++) {
                if ((s + a) <= M) {
                    matrixR[s][a] = this.getTableFu()[s + a] - this.O(a) - this.h(s + a);

                } else {
                    matrixR[s][a] = NotAvaiable;
                }
            }
        }
        return matrixR;
    }

    float[][] getTableP() {
        float matrixP[][] = new float[K][K];
        for (int sa = 0; sa < K; sa++) {
            for (int j = 0; j < K; j++) {
                try {
                    matrixP[sa][j] = this.transitionProbabilities(sa, j);
                } catch (Exception ex) {
                    Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return matrixP;
    }
}
