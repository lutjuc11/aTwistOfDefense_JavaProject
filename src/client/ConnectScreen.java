/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.imageio.ImageIO;

/**
 * @author Jürgen Luttenberger
 * 15.05.2015
 * aTwistOfDefense_JavaProject
 */
public class ConnectScreen extends javax.swing.JFrame {

    /**
     * Creates new form ConnectScreen
     */
    private int player;

    public ConnectScreen(int player) {
        initComponents();
        this.player = player;
    }

    @Override
    public void paint(Graphics g) {
        try {
            super.paint(g);
            g = lblChamp.getGraphics();
            if (player == 1) {
                BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "thresh.png"));
                g.drawImage(image, lblChamp.getWidth() * 10 / 100, lblChamp.getHeight() - image.getWidth() * lblChamp.getHeight() / lblChamp.getWidth() * 80 / 100 - 150, lblChamp.getWidth() * 80 / 100, image.getWidth() * lblChamp.getHeight() / lblChamp.getWidth() * 80 / 100 + 150, null);
            } else {
                BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "darius.png"));
                g.drawImage(image, lblChamp.getWidth() - lblChamp.getWidth() * 10 / 100, lblChamp.getHeight() - image.getWidth() * lblChamp.getHeight() / lblChamp.getWidth() * 80 / 100 - 150, -lblChamp.getWidth() * 80 / 100, image.getWidth() * lblChamp.getHeight() / lblChamp.getWidth() * 80 / 100 + 150, null);

            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNickname = new javax.swing.JTextField();
        txtServerAddress = new javax.swing.JTextField();
        txtServerPort = new javax.swing.JTextField();
        btnConnect = new javax.swing.JButton();
        lblChamp = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(2, 3));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Nickname");
        jPanel1.add(jLabel1);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Server Address");
        jPanel1.add(jLabel2);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Server Port");
        jPanel1.add(jLabel3);

        txtNickname.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtNickname);

        txtServerAddress.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtServerAddress.setText("localhost");
        jPanel1.add(txtServerAddress);

        txtServerPort.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtServerPort.setText("9009");
        jPanel1.add(txtServerPort);

        jPanel2.add(jPanel1, java.awt.BorderLayout.CENTER);

        btnConnect.setText("Connect");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onConnect(evt);
            }
        });
        jPanel2.add(btnConnect, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);
        getContentPane().add(lblChamp, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onConnect(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onConnect
        if (!txtNickname.getText().isEmpty() && !txtServerAddress.getText().isEmpty() && !txtServerPort.getText().isEmpty()) {
            try {
                InetAddress inet = InetAddress.getByName(txtServerAddress.getText());
                int portnr = Integer.parseInt(txtServerPort.getText());
                String nickname = txtNickname.getText();
                if(nickname.length() > 15)
                {
                    nickname = nickname.substring(0, 15);
                }
                new GameClient().connectToServer(nickname, inet, portnr, this, player);
                btnConnect.setEnabled(false);
                btnConnect.setText("Waiting for other player to connect ...");
            } catch (UnknownHostException ex) {
                btnConnect.setText("Wrong host address! - Try again");
                txtServerAddress.setText("");
            } catch (NumberFormatException ex) {
                btnConnect.setText("Wrong port number! - Try again");
                txtServerPort.setText("");
            } catch (IOException ex) {
                btnConnect.setText("Error connecting to the server! - Try again later");
            }
        }
    }//GEN-LAST:event_onConnect

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
//            java.util.logging.Logger.getLogger(ConnectScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ConnectScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ConnectScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ConnectScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ConnectScreen().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblChamp;
    private javax.swing.JTextField txtNickname;
    private javax.swing.JTextField txtServerAddress;
    private javax.swing.JTextField txtServerPort;
    // End of variables declaration//GEN-END:variables
}
