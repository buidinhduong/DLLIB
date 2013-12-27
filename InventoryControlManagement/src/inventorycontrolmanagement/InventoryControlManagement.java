/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorycontrolmanagement;

/**
 *
 * @author buiduong
 */
public class InventoryControlManagement {

    /**
     * @param args the command line arguments
     */
    Model myModel;

    public InventoryControlManagement(Model test) {
        this.myModel = test;
    }

    public static void main(String[] args) {
        // TODO code application logic here  
        //INPUT FOR MODEL
        int k = 4;
        int m = 3;
        int n = 3;
        float[] prob = {(float) 1 / 4, (float) 1 / 2, (float) 1 / 4};
        //INIT MODEL WITH INPUT
        Model testModel = new Model(k, m, n, prob);
        InventoryControlManagement icm = new InventoryControlManagement(testModel);
        //Start Testing Model
        float Fu[] = testModel.getTableFu();
        float[][] rt = testModel.getTableR();
        float[][] p = testModel.getTableP();
        
        //Display
        for (int i = 0; i < Fu.length; i++) {
            System.out.println("F(" + i + ")=" + Fu[i]);
        }
        icm.displayMatrix(rt);        
        icm.displayMatrix(p);

    }

    public  void displayMatrix(float[][] matrix) {
        System.out.println("\n====Maxtrix===Rows "+matrix.length+" Cols:"+matrix[0].length);
        for (float[] rows : matrix) {
            //all rows
            System.out.println("");
            for (int j = 0; j < rows.length; j++) {
                if (j==0) {
                    System.out.print("[");
                } 
                if(rows[j]==myModel.NotAvaiable)
                        System.out.print(" X");
                else 
                       System.out.print(" " + rows[j]);
                
                if (j==rows.length-1) {
                    System.out.print("]");
                } 
                
                
            }
        }
    }
}
