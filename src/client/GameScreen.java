/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import beans.Unit;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.interrupted;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;

/**
 *
 * @author Juergen
 */
public class GameScreen extends javax.swing.JFrame {

    private LinkedList<Unit> champList = new LinkedList<>();
    private Graphics g;
    private LinkedList<UnitThread> unitsThreadList = new LinkedList<>();

    public GameScreen(String nickname, LinkedList<Unit> champions) {
        initComponents();
        drawPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        drawPanel.setDoubleBuffered(true);
        menPlayer.setText(nickname);
        menChampion1.setText(champions.get(0).getDisplayname());
        menChampion2.setText(champions.get(1).getDisplayname());
        menChampion3.setText(champions.get(2).getDisplayname());
        champList = champions;
        unitsThreadList.add(new UnitThread(champList.get(0)));
        unitsThreadList.add(new UnitThread(champList.get(1)));
        unitsThreadList.add(new UnitThread(champList.get(2)));
        repaint();
    }

    public GameScreen() {
        initComponents();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.setSize(gd.getDisplayMode().getWidth() - 20, gd.getDisplayMode().getHeight() / 4);
        this.setLocationRelativeTo(null);
        drawPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        try {
            g = drawPanel.getGraphics();
            BufferedImage image;
            this.g = g;

// NEXUS THREAD 1
            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "nexusBlue.png"));
            g.drawImage(image, 1, drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight(), drawPanel.getHeight() / 3, null);
// NEXUS THREAD 2
            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "nexusRed.png"));
            g.drawImage(image, drawPanel.getWidth() - drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight() - 1, drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight(), drawPanel.getHeight() / 3, null);

// TOWER THREAD 1
            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "tower.png"));
            g.drawImage(image, 100, drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight(), drawPanel.getHeight() / 3, null);
// TOWER THREAD 2
            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "tower.png"));
            g.drawImage(image, 200, drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight(), drawPanel.getHeight() / 3, null);
// TOWER THREAD 3
            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "tower.png"));
            g.drawImage(image, drawPanel.getWidth() - 200 - drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight(), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight(), drawPanel.getHeight() / 3, null);
// TOWER THREAD 4
            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "tower.png"));
            g.drawImage(image, drawPanel.getWidth() - 100 - drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight(), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight(), drawPanel.getHeight() / 3, null);

            if (unitsThreadList.get(0).isAlive()) {
                image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + champList.get(0).getDisplayname() + ".png"));
                g.drawImage(image,  unitsThreadList.get(0).getX(), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight(), drawPanel.getHeight() / 3, null);
            }
            
            if (unitsThreadList.get(1).isAlive()) {
                image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + champList.get(1).getDisplayname() + ".png"));
                g.drawImage(image,  unitsThreadList.get(1).getX(), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight(), drawPanel.getHeight() / 3, null);
            }
            
            if (unitsThreadList.get(2).isAlive()) {
                image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + champList.get(2).getDisplayname() + ".png"));
                g.drawImage(image,  unitsThreadList.get(2).getX(), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight(), drawPanel.getHeight() / 3, null);
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

        drawPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menPlayer = new javax.swing.JMenu();
        menSurrender = new javax.swing.JMenuItem();
        menTroops = new javax.swing.JMenu();
        menChampion1 = new javax.swing.JMenuItem();
        menChampion2 = new javax.swing.JMenuItem();
        menChampion3 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menMeleeMinion = new javax.swing.JMenuItem();
        menCasterMinion = new javax.swing.JMenuItem();
        menItems = new javax.swing.JMenu();
        menSpells = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        javax.swing.GroupLayout drawPanelLayout = new javax.swing.GroupLayout(drawPanel);
        drawPanel.setLayout(drawPanelLayout);
        drawPanelLayout.setHorizontalGroup(
            drawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        drawPanelLayout.setVerticalGroup(
            drawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 281, Short.MAX_VALUE)
        );

        getContentPane().add(drawPanel, java.awt.BorderLayout.CENTER);

        menPlayer.setText("Player");

        menSurrender.setText("Surrender");
        menPlayer.add(menSurrender);

        jMenuBar1.add(menPlayer);

        menTroops.setText("Troops");

        menChampion1.setText("Champion1");
        menChampion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCreateChamp1(evt);
            }
        });
        menTroops.add(menChampion1);

        menChampion2.setText("Champion2");
        menChampion2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCreaterChamp2(evt);
            }
        });
        menTroops.add(menChampion2);

        menChampion3.setText("Champion3");
        menChampion3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCreaterChamp3(evt);
            }
        });
        menTroops.add(menChampion3);
        menTroops.add(jSeparator1);

        menMeleeMinion.setText("Melee Minion");
        menTroops.add(menMeleeMinion);

        menCasterMinion.setText("Caster Minion");
        menTroops.add(menCasterMinion);

        jMenuBar1.add(menTroops);

        menItems.setText("Items");
        jMenuBar1.add(menItems);

        menSpells.setText("Spells");
        jMenuBar1.add(menSpells);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onCreateChamp1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCreateChamp1
        if (unitsThreadList.get(0) == null || !unitsThreadList.get(0).isAlive()) {
            unitsThreadList.get(0).start();
        }
    }//GEN-LAST:event_onCreateChamp1

    private void onCreaterChamp2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCreaterChamp2
        if (unitsThreadList.get(1) == null || !unitsThreadList.get(1).isAlive()) {
            unitsThreadList.get(1).start();
        }
    }//GEN-LAST:event_onCreaterChamp2

    private void onCreaterChamp3(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCreaterChamp3
        if (unitsThreadList.get(2) == null || !unitsThreadList.get(2).isAlive()) {
            unitsThreadList.get(2).start();
        }
    }//GEN-LAST:event_onCreaterChamp3

    class UnitThread extends Thread {

        private Unit unit;
        private int x = 1;

        private UnitThread(Unit unit) {
            this.unit = unit;
        }

        @Override
        public void run() {
            while (!interrupted()) {
                repaint();
                x+=30;
                try {
                    System.out.println(unit.getMovespeed());
                    Thread.sleep(unit.getMovespeed());
                } catch (InterruptedException ex) {
                    System.out.println(ex.toString());
                }
            }
        }

        public int getX() {
            return x;
        }
        
        
        
    }

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
            java.util.logging.Logger.getLogger(GameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel drawPanel;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuItem menCasterMinion;
    private javax.swing.JMenuItem menChampion1;
    private javax.swing.JMenuItem menChampion2;
    private javax.swing.JMenuItem menChampion3;
    private javax.swing.JMenu menItems;
    private javax.swing.JMenuItem menMeleeMinion;
    private javax.swing.JMenu menPlayer;
    private javax.swing.JMenu menSpells;
    private javax.swing.JMenuItem menSurrender;
    private javax.swing.JMenu menTroops;
    // End of variables declaration//GEN-END:variables
}
