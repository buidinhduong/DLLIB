/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import inventorycontrolmanagement.Matrix;
import inventorycontrolmanagement.Model;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author buiduong
 */
public class InventoryGUI extends javax.swing.JFrame {

    /**
     * Creates new form InventoryGUI
     */
    //INPUT FOR MODEL
    int k = 4;
    int m = 3;//capacity
    int n = 4;//decision epoch
    float revenue_unit_sold = 8;//float revenue_unit_sold
    float cost_order = 2;//float cost_order/ unit
    float cost_maintain = 1;//float cost maintain per unit
    float[] prob = {(float) 1 / 4, (float) 1 / 2, (float) 1 / 4};
    DefaultListModel prob_model;
    //INIT MODEL WITH INPUT   
    Model myModel = new Model(k, m, n, prob, revenue_unit_sold, cost_order, cost_maintain);

    public InventoryGUI() {
        initComponents();
        this.setTitle("Inventory Management Simulation");
        this.syncProbWithList();
        this.display();
    }

    public void syncProbWithList() {
        if (prob_model == null) {
            prob_model = new DefaultListModel();
            for (float f : prob) {
                prob_model.addElement(f + "");
            }
        }
        listProb.setModel(prob_model);
    }

    public void updateModel() {
        try {
            myModel.K = Integer.valueOf(txtK.getText());
            myModel.M = Integer.valueOf(txtCapacity.getText());
            myModel.N = Integer.valueOf(txtDecisionEpoch.getText());
            myModel.revenue_unit = Integer.valueOf(txtRevenue.getText());
            myModel.cost_maintain_unit = Integer.valueOf(txtMaintainCost.getText());
            myModel.cost_ordered_unit = Integer.valueOf(txtOrderCost.getText());
            if (myModel.K < 0 || myModel.M < 0 || myModel.N < 0 || myModel.cost_maintain_unit < 0
                    || myModel.cost_ordered_unit < 0 || myModel.revenue_unit < 0) {
                JOptionPane.showMessageDialog(this, "Can not input negative number");
                return;
            }
            myModel.p = new float[prob_model.getSize()];
            for (int i = 0; i < prob_model.getSize(); i++) {
                myModel.p[i] = Float.valueOf(prob_model.get(i).toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    void display() {
        try {
           // TODO code application logic here  
            //check input

            //Start Testing Model
            float Fu[] = myModel.getTableFu();
            float[][] rt = myModel.getTableR();
            float[][] p = myModel.getTableP();
            String htmlProb = "{";
            for (int i = 0; i < myModel.p.length; i++) {
                if (i != myModel.p.length - 1) {
                    htmlProb += "" + myModel.p[i] + ",";
                } else {
                    htmlProb += "" + myModel.p[i];
                }
            }
            htmlProb += "}";
            String html;
            html = "<html><head><title>Simple Page</title></head>";
            html += "<body bgcolor='white'><hr/><font size=50>Inventory Management Simulation</font>"
                    + "<br><h2>Name:Bùi Đình Dương, Email:dinhduong.bui@gmail.com</h2><hr/>";
            html += "<h2>Model Input:</h2>";
            html += "<h2>K= " + myModel.K + "<br>Capacity:" + myModel.M + "<br>Order Cost/ Unit= " + myModel.cost_ordered_unit
                    + "<br>Maintain Cost/ Unit= " + myModel.cost_maintain_unit + " <br> Decision Epoch=" + myModel.N
                    + "<br>Revenue/ Unit Sold=" + myModel.revenue_unit
                    + "<br>Transition Probabilities=" + htmlProb
                    + "</h2>"
                    + "<br><hr></hr>";
            html += "<h2>Model Ouput</h2><br><h2>F(u) Expected Revenue</h2>";
            html += this.htmlFromFu(Fu);
            html += "<br><h2>Rt(s,a) Reward</h2>";
            html += this.htmlFromMatrixReward(rt);
            html += "<br><h2>P(j|s,a) Propability</h2><br>";
            html += this.htmlFromMatrixProbability(p);
            html += "<br><h2>Result by applying Backward Induction Algorithm to find optimal policies</h2><br>";
            Matrix[] result = myModel.optimal_MD();
            for (int i = result.length - 1; i >= 0; i--) {
                html += "<br>" + this.htmlFromMatrix(result[i]);
            }
            //summary
            html += "<br><h2>Summary</h2>" + this.htmlFromMatrixSummary(myModel.summary());
            html += "</body></html>";

            output.setContentType("text/html");
            output.setText(html);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            System.out.println("Some problem has occured " + e.getMessage());
        }
    }

    public String htmlFromFu(float[] vector) {
        String html = "<table border=\"1\">";
        html += "<tr><th>u</th>";
        html += "<th>F(u)</th></tr>";
        int a=0;
        for (float f : vector) {
            html += "<tr>";
            if (f != myModel.NotAvaiable) {
                html += "<td>" + a+ "</td>";
                html += "<td>" + f + "</td>";
            } else {
                html += "<td>X</td>";
            }
            html += "</tr>";
            a++;
        }
        
        return html + "</table>";
    }

    public String htmlFromVector(float[] vector) {
        String html = "<table border=\"1\">";
        html += "<tr>";
        for (float f : vector) {
            if (f != myModel.NotAvaiable) {
                html += "<td>" + f + "</td>";
            } else {
                html += "<td>X</td>";
            }
        }
        html += "</tr>";
        return html + "</table>";

    }

    public String htmlFromMatrix(float[][] matrix) {
        String html = "<table border=\"1\">";
        for (float[] rows : matrix) {
            html += "<tr>";
            for (float f : rows) {
                if (f != myModel.NotAvaiable) {
                    html += "<td>" + f + "</td>";
                } else {
                    html += "<td>X</td>";
                }

            }
            html += "</tr>";
        }
        return html + "</table>";
    }
    public String htmlFromMatrixProbability(float[][] matrix) {
        String html = "<table border=\"1\">";
        html += "<td></th><th colspan="+matrix[0].length+">Pt(j|s,a)</th><tr><td><b>s+a/j</b></td>";
        for(int a=0;a<matrix[0].length;a++)
        {
           html += "<td><b>"+a+"</b></td>";
        }
        html+="</tr>";
        int a=0;
        for (float[] rows : matrix) {
            html += "<tr><td><b>"+a+"</b></td>";
            a++;
            for (float f : rows) {
                if (f != myModel.NotAvaiable) {
                    html += "<td>" + f + "</td>";
                } else {
                    html += "<td>X</td>";
                }

            }
            html += "</tr>";
        }
        return html + "</table>";
    }
public String htmlFromMatrixReward(float[][] matrix) {
        String html = "<table border=\"1\">";
        html += "<td></th><th colspan="+matrix[0].length+">Rt(s,a)</th><tr><td><b>s/a</b></td>";
        for(int a=0;a<matrix[0].length;a++)
        {
           html += "<td><b>"+a+"</b></td>";
        }
        html+="</tr>";
        int a=0;
        for (float[] rows : matrix) {
            html += "<tr><td><b>"+a+"</b></td>";
            a++;
            for (float f : rows) {
                if (f != myModel.NotAvaiable) {
                    html += "<td>" + f + "</td>";
                } else {
                    html += "<td>X</td>";
                }

            }
            html += "</tr>";
        }
        return html + "</table>";
    }
    public String htmlFromMatrixSummary(float[][] matrix) {
        String html = "<table border=\"1\">";
        html += "<th>s</th>";
        for (int a = 0; a < matrix[0].length - 1; a++) {
            html += "<th >  d*" + (a + 1) + "(s)</th>";

        }
        html += "<th >V*" + (matrix[0].length) + "(s)</th>";
        html += "</tr>";
        int a = 0;
        for (float[] rows : matrix) {
            html += "<tr>";
            html += "<td><b>" + a + "</b></td>";
            a++;
            for (float f : rows) {

                if (f != myModel.NotAvaiable) {
                    html += "<td>" + f + "</td>";
                } else {
                    html += "<td>X</td>";
                }

            }
            html += "</tr>";
        }
        return html + "</table>";
    }

    public String htmlFromMatrix(Matrix matrix) {
        String html = "<h3>Decision Epoch T=" + matrix.getId() + "</h3><table border=\"1\"><tr>";
        html += "<th >s/a</th>";

        for (int a = 0; a < matrix.getData()[0].length; a++) {
            if (a < matrix.getData()[0].length - 2) {
                html += "<th >  " + a + "</th>";
            } else if (a == matrix.getData()[0].length - 2) {
                html += "<th > U" + matrix.getId() + "*(s)" + " </th>";
            } else {
                html += "<th > As," + matrix.getId() + "*</th>";
            }
        }
        html += "</tr>";
        int row = 0;
        for (float[] rows : matrix.getData()) {
            html += "<tr><td><b>" + row + "</b></td>";
            for (float f : rows) {
                if (f != myModel.NotAvaiable) {
                    html += "<td>" + f + "</td>";
                } else {
                    html += "<td>X</td>";
                }
            }
            html += "</tr>";
            row++;
        }
        return html + "</table>";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        txtCapacity = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtK = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtDecisionEpoch = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtMaintainCost = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listProb = new javax.swing.JList();
        txtUpdateProb = new javax.swing.JTextField();
        btUpdateProb = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtRevenue = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtOrderCost = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        output = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Input"));

        txtCapacity.setText("3");
        txtCapacity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCapacityActionPerformed(evt);
            }
        });
        txtCapacity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCapacityFocusLost(evt);
            }
        });
        txtCapacity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCapacityKeyReleased(evt);
            }
        });

        jLabel2.setText("Storage Capacity");

        txtK.setText("4");

        jLabel1.setText("Fixed Cost K");

        jLabel3.setText("Decision Epoch");

        txtDecisionEpoch.setText("4");

        jButton1.setText("Run Model");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Maintain Cost/ Unit");

        txtMaintainCost.setText("1");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Transition Propabilities"));

        listProb.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listProb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listProbMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(listProb);

        txtUpdateProb.setText("0.25");

        btUpdateProb.setText("Update Propability");
        btUpdateProb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateProbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUpdateProb)
                    .addComponent(btUpdateProb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtUpdateProb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btUpdateProb, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );

        jLabel5.setText("Revenue / Unit Sold");

        txtRevenue.setText("8");

        jLabel6.setText("Order Cost /Unit");

        txtOrderCost.setText("2");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtMaintainCost, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                                    .addComponent(txtK)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(txtRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCapacity)
                            .addComponent(txtDecisionEpoch)
                            .addComponent(txtOrderCost, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(txtCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDecisionEpoch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(txtMaintainCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(txtOrderCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Output"));

        jScrollPane1.setViewportView(output);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCapacityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCapacityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCapacityActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.updateModel();
        this.display();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void listProbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listProbMouseClicked
        // TODO add your handling code here:
        if (listProb.getSelectedValue() != null) {
            txtUpdateProb.setText(listProb.getSelectedValue().toString());
        }
    }//GEN-LAST:event_listProbMouseClicked

    private void btUpdateProbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateProbActionPerformed
        // TODO add your handling code here:
        if (Float.valueOf(txtUpdateProb.getText()) < 0) {
            JOptionPane.showMessageDialog(this, "Can not input negative number");
            return;
        }
        prob_model.setElementAt(txtUpdateProb.getText(), listProb.getSelectedIndex());
    }//GEN-LAST:event_btUpdateProbActionPerformed

    private void txtCapacityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCapacityKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txtCapacityKeyReleased

    private void txtCapacityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCapacityFocusLost
        // TODO add your handling code here:
        if (prob_model.getSize() != Float.valueOf(txtCapacity.getText())) {
            prob_model = new DefaultListModel();
            int cap = Integer.valueOf(txtCapacity.getText());
            for (int i = 0; i < cap; i++) {
                prob_model.addElement((float) 1 / cap);
            }
            this.syncProbWithList();
        }
    }//GEN-LAST:event_txtCapacityFocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InventoryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InventoryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InventoryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InventoryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InventoryGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btUpdateProb;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JList listProb;
    private javax.swing.JEditorPane output;
    private javax.swing.JTextField txtCapacity;
    private javax.swing.JTextField txtDecisionEpoch;
    private javax.swing.JTextField txtK;
    private javax.swing.JTextField txtMaintainCost;
    private javax.swing.JTextField txtOrderCost;
    private javax.swing.JTextField txtRevenue;
    private javax.swing.JTextField txtUpdateProb;
    // End of variables declaration//GEN-END:variables
}
