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

    public float K;
    public int M;
    public int N;
    public float[] p;
    public float revenue_unit;
    public float cost_ordered_unit;
    public float cost_maintain_unit;
    public final float NotAvaiable = -1234;

    public Model(int k, int m, int n, float[] prob, float revenue_unit, float costOrder, float costMaintain) {
        this.K = k;
        this.M = m;
        this.N = n;
        this.p = prob;
        this.revenue_unit = revenue_unit;
        this.cost_ordered_unit = costOrder;
        this.cost_maintain_unit = costMaintain;
    }

    float c(int u) {
        return (float) cost_ordered_unit * u;
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
        return cost_maintain_unit * u;
    }

    float f(int u) {
        return revenue_unit * u;
    }

    float p(int j) {
        return p[j];
    }

    public float[][] summary() {
        float matrixSum[][] = new float[M + 1][N];
        Matrix result[] = this.optimal_MD();
//        for (int s = 0; s <= M; s++)//all rows
//        {
//            matrixSum[s][0] = s;
//        }
        for (int s = 0; s <= M; s++)//all rows
        {
            matrixSum[s][matrixSum[0].length - 1] = result[0].getData()[s][result[0].getData()[0].length - 2];
        }
        for (int col = 0; col < matrixSum[0].length - 1; col++)//all rest cols
        {
            Matrix mt = result[col];
            for (int rows = 0; rows <= M; rows++)//all rows
            {
                matrixSum[rows][col] = mt.getData()[rows][mt.getData()[0].length - 1];
            }
        }
        return matrixSum;
    }

    public Matrix[] optimal_MD() {
        //extend 2 cols ro U*s and A*s
        //If we have M => M+1 states
        int states = M + 1;
        int cols_2 = states + 2;
        float matrixOpU[][] = new float[states][cols_2];
        float matrixR[][] = this.getTableR();
        float matrixP[][] = this.getTableP();
        int t = N;
        Matrix matrix[] = new Matrix[N];
        Matrix mN = new Matrix();
        mN.setId(t);
        //loop s in S
        for (int s = 0; s <= M; s++) {
            matrixOpU[s][states] = 0;
            matrixOpU[s][cols_2 - 1] = 0;//a
        }
        mN.setData(matrixOpU);
        //Index = T-1
        matrix[t - 1] = mN;

        while (t > 1) {
            t = t - 1;
            Matrix mT = new Matrix(states, cols_2);
            mT.setId(t);
            //for each s Do
            for (int s = 0; s <= M; s++) {
                float maxS = 0;
                float max_a = 0;
                for (int a = 0; a <= M; a++) {
                    if (s + a <= M) {
                        //Rt(s,a)
                        float r_s_a = matrixR[s][a];
                        float sumPU = 0;
                        for (int j = 0; j <= M; j++) {
                            // Need to access to U* of T+1 , but because T start = N, therefore, index at T+1 in here
                            //is just T
                            sumPU += (matrixP[s + a][j] * matrix[t].getData()[j][cols_2 - 2]);
                        }
                        float[][] data = mT.getData();
                        data[s][a] = r_s_a + sumPU;
                        mT.setData(data);
                        if (r_s_a + sumPU > maxS) {
                            maxS = r_s_a + sumPU;
                            max_a = a;
                            mT.getData()[s][cols_2 - 2] = maxS;
                            mT.getData()[s][cols_2 - 1] = max_a;
                        }
                    } else {
                        float[][] data = mT.getData();
                        data[s][a] = NotAvaiable;
                        mT.setData(data);
                    }
                    matrix[t - 1] = mT;

                }
            }
        }
        return matrix;
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

    public float[] getTableFu() {
        int states = M + 1;
        float[] Fu = new float[states];
        for (int u = 0; u < states; u++) {
            Fu[u] = this.F(u);
        }
        return Fu;
    }

    public float[][] getTableR() {
        int states = M + 1;
        float matrixR[][] = new float[states][states];
        for (int s = 0; s < states; s++) {
            for (int a = 0; a < states; a++) {
                if ((s + a) <= M) {
                    matrixR[s][a] = this.getTableFu()[s + a] - this.O(a) - this.h(s + a);
                } else {
                    matrixR[s][a] = NotAvaiable;
                }
            }
        }
        return matrixR;
    }

    public float[][] getTableP() {
        int states = M + 1;
        float matrixP[][] = new float[states][states];
        for (int sa = 0; sa < states; sa++) {
            for (int j = 0; j < states; j++) {
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
