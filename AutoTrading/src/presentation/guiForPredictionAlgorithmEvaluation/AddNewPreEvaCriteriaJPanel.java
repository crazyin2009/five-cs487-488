/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AddNewCriteriaJPanel.java
 *
 * Created on Jun 24, 2011, 6:14:10 AM
 */

package presentation.guiForPredictionAlgorithmEvaluation;

import business.predictionAlgorithmEvaluation.PredictionCriteria;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;

/**
 *
 * @author Dinh
 */
public class AddNewPreEvaCriteriaJPanel extends javax.swing.JPanel {

    private JDialog parent;
    private boolean ok;
    private PredictionCriteria predictionCriteria;

    public boolean isOk() {
        return ok;
    }

    public PredictionCriteria getPredictionCriteria() {
        return predictionCriteria;
    }

    /** Creates new form AddNewCriteriaJPanel */
    public AddNewPreEvaCriteriaJPanel(JDialog jDialog) {
        this.parent = jDialog;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        preEvaCriJLabel = new javax.swing.JLabel();
        preEvaCriJComboBox = new javax.swing.JComboBox();
        cancelJButton = new javax.swing.JButton();
        okJButton = new javax.swing.JButton();

        preEvaCriJLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        preEvaCriJLabel.setText("Prediction Evaluation Criteria:");

        preEvaCriJComboBox.setModel(new DefaultComboBoxModel(business.predictionAlgorithmEvaluation.Utility.predictionCriteriaList));

        cancelJButton.setText("Cancel");
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        okJButton.setText("OK");
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(preEvaCriJLabel)
                .addGap(18, 18, 18)
                .addComponent(preEvaCriJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(111, Short.MAX_VALUE)
                .addComponent(okJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelJButton)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelJButton, okJButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(preEvaCriJLabel)
                    .addComponent(preEvaCriJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelJButton)
                    .addComponent(okJButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cancelJButton, okJButton});

    }// </editor-fold>//GEN-END:initComponents

    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        ok = true;

        predictionCriteria = business.predictionAlgorithmEvaluation.Utility.getPredictionAlgorithm((String)preEvaCriJComboBox.getSelectedItem());

        this.parent.dispose();
    }//GEN-LAST:event_okJButtonActionPerformed

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        ok = false;

        this.parent.dispose();
    }//GEN-LAST:event_cancelJButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelJButton;
    private javax.swing.JButton okJButton;
    private javax.swing.JComboBox preEvaCriJComboBox;
    private javax.swing.JLabel preEvaCriJLabel;
    // End of variables declaration//GEN-END:variables

    public JDialog getParentDialog() {
        return parent;
    }

}
