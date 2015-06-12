/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import beans.Unit;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Juergen
 */
public class SelectScreen extends javax.swing.JFrame {

    private LinkedList<Unit> units;
    private LinkedList<JLabel> champions = new LinkedList<>();
    private LinkedList<Unit> chosenUnits = new LinkedList<>();

    /**
     * Creates new form SelectScreen
     */
    public SelectScreen(LinkedList<Unit> units) {
        initComponents();
        this.units = units;
        JLabel lblChamp;
        for (Unit unit : units) {
            if (unit.getTyp().equals("Champ")) {
                lblChamp = new JLabel();
                lblChamp.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if (chosenUnits.size() < 3) {
                            onMouseClick(e);
                        }
                    }

                });
                lblChamp.setBorder(BorderFactory.createLineBorder(Color.black));
                lblChamp.setName(unit.getDisplayname().toLowerCase());
                pnChampions.add(lblChamp);
                champions.add(lblChamp);
            }
        }
        lblChamp = new JLabel();
        lblChamp.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (chosenUnits.size() < 3) {
                    onRandomMouseClick();
                }
            }

        });
        lblChamp.setBorder(BorderFactory.createLineBorder(Color.black));
        lblChamp.setName("random");
        pnChampions.add(lblChamp);
        champions.add(lblChamp);

        repaint();
    }

    public void onMouseClick(MouseEvent evt) {
        String selectedChampion = ((JLabel) evt.getSource()).getName();
        Unit selectedUnit = null;
        for (Unit unit : units) {
            if (unit.getDisplayname().toLowerCase().equals(selectedChampion)) {
                lblFocusedChampion.setText(unit.toShowString());
                selectedUnit = unit;
            }
        }
        if (evt.getClickCount() == 2) {
            if (!chosenUnits.contains(selectedUnit)) {
                chosenUnits.add(selectedUnit);
            }
            repaint();
        }
    }

    public void onRandomMouseClick() {
        boolean goAgain = true;
        while (goAgain) {
            Random rand = new Random();
            int randChamp = rand.nextInt(champions.size());
            String selectedChampion = champions.get(randChamp).getName();
            for (Unit unit : units) {
                if (unit.getDisplayname().toLowerCase().equals(selectedChampion)) {
                    lblFocusedChampion.setText(unit.toShowString());
                    if (!chosenUnits.contains(unit)) {
                        chosenUnits.add(unit);
                        goAgain = false;
                    }
                    repaint();
                }
            }
        }
    }

    @Override
    public void paint(Graphics g
    ) {
        super.paint(g);
        try {
            if (!champions.isEmpty()) {
                for (JLabel lblChamp : champions) {
                    g = lblChamp.getGraphics();
                    BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + lblChamp.getName() + ".png"));
                    g.drawImage(image, lblChamp.getWidth() / 2 - lblChamp.getHeight() * image.getWidth() / image.getHeight() / 2, 0, lblChamp.getHeight() * image.getWidth() / image.getHeight(), lblChamp.getHeight(), null);
                }
            }
            if (!chosenUnits.isEmpty()) {
                if (chosenUnits.size() >= 1) {
                    g = lblChamp1.getGraphics();
                    BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + chosenUnits.get(0).getDisplayname().toLowerCase() + ".png"));
                    g.drawImage(image, lblChamp1.getWidth() / 2 - lblChamp1.getHeight() * image.getWidth() / image.getHeight() / 2, 0, lblChamp1.getHeight() * image.getWidth() / image.getHeight(), lblChamp1.getHeight(), null);
                }
                if (chosenUnits.size() >= 2) {
                    g = lblChamp2.getGraphics();
                    BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + chosenUnits.get(1).getDisplayname().toLowerCase() + ".png"));
                    g.drawImage(image, lblChamp2.getWidth() / 2 - lblChamp2.getHeight() * image.getWidth() / image.getHeight() / 2, 0, lblChamp2.getHeight() * image.getWidth() / image.getHeight(), lblChamp2.getHeight(), null);

                }
                if (chosenUnits.size() >= 3) {
                    g = lblChamp3.getGraphics();
                    BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + chosenUnits.get(2).getDisplayname().toLowerCase() + ".png"));
                    g.drawImage(image, lblChamp3.getWidth() / 2 - lblChamp3.getHeight() * image.getWidth() / image.getHeight() / 2, 0, lblChamp3.getHeight() * image.getWidth() / image.getHeight(), lblChamp3.getHeight(), null);

                }
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    public LinkedList<Unit> getChosenChampions() {
        return chosenUnits;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnChampions = new javax.swing.JPanel();
        pnChosenChamps = new javax.swing.JPanel();
        lblChamp1 = new javax.swing.JLabel();
        lblChamp2 = new javax.swing.JLabel();
        lblChamp3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblFocusedChampion = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        pnChampions.setBorder(javax.swing.BorderFactory.createTitledBorder("Select your Champions"));
        pnChampions.setLayout(new java.awt.GridLayout(6, 4, 5, 5));
        getContentPane().add(pnChampions, java.awt.BorderLayout.CENTER);

        pnChosenChamps.setBorder(javax.swing.BorderFactory.createTitledBorder("Your Champions"));
        pnChosenChamps.setLayout(new java.awt.GridLayout(1, 3, 5, 0));

        lblChamp1.setPreferredSize(new java.awt.Dimension(80, 80));
        pnChosenChamps.add(lblChamp1);
        pnChosenChamps.add(lblChamp2);
        pnChosenChamps.add(lblChamp3);

        getContentPane().add(pnChosenChamps, java.awt.BorderLayout.NORTH);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setLayout(new java.awt.BorderLayout());

        lblFocusedChampion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblFocusedChampion.setPreferredSize(new java.awt.Dimension(0, 20));
        jPanel1.add(lblFocusedChampion, java.awt.BorderLayout.CENTER);

        jButton1.setText("?");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onHelp(evt);
            }
        });
        jPanel1.add(jButton1, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onHelp(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onHelp
        String text = String.format("%-5s - Health\n%-5s - Attack Damage\n%-5s - Ability Power\n%-5s - Armor\n%-5s - Magic Resistance\n%-5s - Attack Speed\n%-5s - Range\n%-5s - Movement Speed", "H", "AD", "AP", "A", "M", "AS", "R", "MS");
        JOptionPane.showMessageDialog(this, text, "Championinformation", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_onHelp

//    /**
    //     * @param args the command line arguments
    //     */
    //    public static void main(String args[]) {
    //        /* Set the Nimbus look and feel */
    //        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    //        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
    //         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
    //         */
    //        try {
    //            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
    //                if ("Nimbus".equals(info.getName())) {
    //                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
    //                    break;
    //                }
    //            }
    //        } catch (ClassNotFoundException ex) {
    //            java.util.logging.Logger.getLogger(SelectScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    //        } catch (InstantiationException ex) {
    //            java.util.logging.Logger.getLogger(SelectScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    //        } catch (IllegalAccessException ex) {
    //            java.util.logging.Logger.getLogger(SelectScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    //        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
    //            java.util.logging.Logger.getLogger(SelectScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    //        }
    //        //</editor-fold>
    //
    //        /* Create and display the form */
    //        java.awt.EventQueue.invokeLater(new Runnable() {
    //            public void run() {
    //                new SelectScreen().setVisible(true);
    //            }
    //        });
    //    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblChamp1;
    private javax.swing.JLabel lblChamp2;
    private javax.swing.JLabel lblChamp3;
    private javax.swing.JLabel lblFocusedChampion;
    private javax.swing.JPanel pnChampions;
    private javax.swing.JPanel pnChosenChamps;
    // End of variables declaration//GEN-END:variables

}
