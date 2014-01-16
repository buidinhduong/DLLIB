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
public class Matrix {
    int id;
    float[][] data;
    int rows;
    int cols;
    public Matrix(int rows,int cols) {
        data=new float[rows][cols];
        this.rows=rows;
        this.cols=cols;
    }

    public Matrix() {
    }
    
    public float[][] getData() {
        return data;
    }

    public void setData(float[][] data) {
        this.data = data;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

   
    
}
