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
 * @author Jürgen Luttenberger
 * 15.05.2015
 * aTwistOfDefense_JavaProject
 */
public class SelectScreen extends javax.swing.JFrame {

    /**
     * Contains all playable Units in the game, read from a CSV file.
     */
    private LinkedList<Unit> units;
    /**
     * Contains the two different types of minios (meele, ranged) in form of a
     * Unit.
     *
     * @see Unit
     */
    private LinkedList<Unit> minions = new LinkedList<>();
    /**
     * Contains all playable Units, excluding minions.
     *
     * @see Unit
     */
    private LinkedList<JLabel> champions = new LinkedList<>();

    private LinkedList<Unit> chosenUnits = new LinkedList<>();
    /**
     * This list is used to store the two spells the player chooses.
     *
     * @see Unit
     */
    private LinkedList<String> chosenSpells = new LinkedList<>();

    /**
     * Creates new form SelectScreen
     */
    public SelectScreen(LinkedList<Unit> units) {
        initComponents();

        lblExhaust.setSize(pnSpells.getHeight() / 5, pnSpells.getHeight() / 5);
        lblExhaust.setBorder(BorderFactory.createLineBorder(Color.black));
        lblExhaust.setName("lblExhaust");
        lblHeal.setSize(pnSpells.getHeight() / 5, pnSpells.getHeight() / 5);
        lblHeal.setBorder(BorderFactory.createLineBorder(Color.black));
        lblHeal.setName("lblHeal");
        lblGhost.setSize(pnSpells.getHeight() / 5, pnSpells.getHeight() / 5);
        lblGhost.setBorder(BorderFactory.createLineBorder(Color.black));
        lblGhost.setName("lblGhost");
        lblIgnite.setSize(pnSpells.getHeight() / 5, pnSpells.getHeight() / 5);
        lblIgnite.setBorder(BorderFactory.createLineBorder(Color.black));
        lblIgnite.setName("lblIgnite");
        lblSmite.setSize(pnSpells.getHeight() / 5, pnSpells.getHeight() / 5);
        lblSmite.setBorder(BorderFactory.createLineBorder(Color.black));
        lblSmite.setName("lblSmite");

        lblSpell1.setSize(pnChosenSpells.getHeight() / 2, pnChosenSpells.getHeight() / 2);
        lblSpell1.setBorder(BorderFactory.createLineBorder(Color.black));
        lblSpell2.setSize(pnChosenSpells.getHeight() / 2, pnChosenSpells.getHeight() / 2);
        lblSpell2.setBorder(BorderFactory.createLineBorder(Color.black));

        this.units = units;
        for (Unit unit : units) {
            if (unit.getTyp().equalsIgnoreCase("minion")) {
                minions.add(unit);
            }
        }

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

    /**
     * This method will add a Unit to the chosenUnits List, using the name of
     * the Source of the parameter evt.
     *
     * @param evt
     * @see Unit
     */
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

    /**
     * This method will add a random Unit to the chosenUnits List.
     *
     * @see Unit
     * @see Random
     */
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
    public void paint(Graphics g) {
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

            if (!chosenSpells.isEmpty()) {
                if (chosenSpells.size() >= 1) {
                    g = lblSpell1.getGraphics();
                    BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + chosenSpells.get(0) + ".png"));
                    g.drawImage(image, 3, 3, lblSpell1.getWidth() - 6, lblSpell1.getHeight() - 6, null);
                }
                if (chosenSpells.size() >= 2) {
                    g = lblSpell2.getGraphics();
                    BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + chosenSpells.get(1) + ".png"));
                    g.drawImage(image, 3, 3, lblSpell2.getWidth() - 6, lblSpell2.getHeight() - 6, null);
                }
            }

            g = lblExhaust.getGraphics();
            BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "Exhaust.png"));
            g.drawImage(image, 3, 3, lblExhaust.getWidth() - 6, lblExhaust.getHeight() - 6, null);

            g = lblHeal.getGraphics();
            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "Heal.png"));
            g.drawImage(image, 3, 3, lblHeal.getWidth() - 6, lblHeal.getHeight() - 6, null);

            g = lblGhost.getGraphics();
            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "Ghost.png"));
            g.drawImage(image, 3, 3, lblGhost.getWidth() - 6, lblGhost.getHeight() - 6, null);

            g = lblIgnite.getGraphics();
            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "Ignite.png"));
            g.drawImage(image, 3, 3, lblIgnite.getWidth() - 6, lblIgnite.getHeight() - 6, null);

            g = lblSmite.getGraphics();
            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "Smite.png"));
            g.drawImage(image, 3, 3, lblSmite.getWidth() - 6, lblSmite.getHeight() - 6, null);
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * getter method to get the chosen units.
     *
     * @return
     */
    public LinkedList<Unit> getChosenChampions() {
        return chosenUnits;
    }

    /**
     * getter method to get the chosen spells.
     *
     * @return
     */
    public LinkedList<String> getChosenSpells() {
        return chosenSpells;
    }

    /**
     * getter method to get the list, which contains the minions.
     *
     * @return
     */
    public LinkedList<Unit> getMinions() {
        return minions;
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
        jPanel1 = new javax.swing.JPanel();
        lblFocusedChampion = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        pnSpells = new javax.swing.JPanel();
        lblIgnite = new javax.swing.JLabel();
        lblSmite = new javax.swing.JLabel();
        lblHeal = new javax.swing.JLabel();
        lblExhaust = new javax.swing.JLabel();
        lblGhost = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        pnChosenChamps = new javax.swing.JPanel();
        lblChamp1 = new javax.swing.JLabel();
        lblChamp2 = new javax.swing.JLabel();
        lblChamp3 = new javax.swing.JLabel();
        pnChosenSpells = new javax.swing.JPanel();
        lblSpell1 = new javax.swing.JLabel();
        lblSpell2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        pnChampions.setBorder(javax.swing.BorderFactory.createTitledBorder("Choose wisely"));
        pnChampions.setLayout(new java.awt.GridLayout(6, 4, 5, 5));
        getContentPane().add(pnChampions, java.awt.BorderLayout.CENTER);

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

        pnSpells.setBorder(javax.swing.BorderFactory.createTitledBorder("-"));
        pnSpells.setPreferredSize(new java.awt.Dimension(64, 100));
        pnSpells.setLayout(new java.awt.GridLayout(5, 1));

        lblIgnite.setPreferredSize(new java.awt.Dimension(32, 32));
        lblIgnite.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onChooseSpells(evt);
            }
        });
        pnSpells.add(lblIgnite);

        lblSmite.setPreferredSize(new java.awt.Dimension(32, 32));
        lblSmite.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onChooseSpells(evt);
            }
        });
        pnSpells.add(lblSmite);

        lblHeal.setPreferredSize(new java.awt.Dimension(32, 32));
        lblHeal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onChooseSpells(evt);
            }
        });
        pnSpells.add(lblHeal);

        lblExhaust.setPreferredSize(new java.awt.Dimension(32, 32));
        lblExhaust.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onChooseSpells(evt);
            }
        });
        pnSpells.add(lblExhaust);

        lblGhost.setPreferredSize(new java.awt.Dimension(32, 32));
        lblGhost.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onChooseSpells(evt);
            }
        });
        pnSpells.add(lblGhost);

        getContentPane().add(pnSpells, java.awt.BorderLayout.EAST);

        jPanel2.setLayout(new java.awt.BorderLayout());

        pnChosenChamps.setBorder(javax.swing.BorderFactory.createTitledBorder("Champions"));
        pnChosenChamps.setLayout(new java.awt.GridLayout(1, 3, 5, 0));

        lblChamp1.setPreferredSize(new java.awt.Dimension(80, 80));
        pnChosenChamps.add(lblChamp1);
        pnChosenChamps.add(lblChamp2);
        pnChosenChamps.add(lblChamp3);

        jPanel2.add(pnChosenChamps, java.awt.BorderLayout.CENTER);

        pnChosenSpells.setBorder(javax.swing.BorderFactory.createTitledBorder("Spells"));
        pnChosenSpells.setPreferredSize(new java.awt.Dimension(64, 100));
        pnChosenSpells.setLayout(new java.awt.GridLayout(2, 1));
        pnChosenSpells.add(lblSpell1);
        pnChosenSpells.add(lblSpell2);

        jPanel2.add(pnChosenSpells, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onHelp(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onHelp
        String text = String.format("%-5s - Health\n%-5s - Attack Damage\n%-5s - Ability Power\n%-5s - Armor\n%-5s - Magic Resistance\n%-5s - Attack Speed\n%-5s - Range\n%-5s - Movement Speed", "H", "AD", "AP", "A", "M", "AS", "R", "MS");
        JOptionPane.showMessageDialog(this, text, "Championinformation", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_onHelp

    private void onChooseSpells(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onChooseSpells
        if (chosenSpells.size() < 2) {
            String selectedSpell = ((JLabel) evt.getSource()).getName().substring(3);
            switch (selectedSpell) {
                case "Heal":
                    lblFocusedChampion.setText("  Heal:    Heals your champions by half of their maximal health");
                    break;
                case "Smite":
                    lblFocusedChampion.setText("  Smite:   Deals 50% of the minion's maximal health to all minions");
                    break;
                case "Ignite":
                    lblFocusedChampion.setText("  Ignite:  Deals 50% of the champions's maximal health to all champions");
                    break;
                case "Ghost":
                    lblFocusedChampion.setText("  Ghost:   Boosts your troops' speed");
                    break;
                case "Exhaust":
                    lblFocusedChampion.setText("  Exhaust: Slows down the enemy troops' speed");
                    break;
                default:
                    System.out.println(selectedSpell);
            }
            if (evt.getClickCount() == 2) {
                if (!chosenSpells.contains(selectedSpell)) {
                    chosenSpells.add(selectedSpell);
                }
                repaint();
            }
        }
    }//GEN-LAST:event_onChooseSpells

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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblChamp1;
    private javax.swing.JLabel lblChamp2;
    private javax.swing.JLabel lblChamp3;
    private javax.swing.JLabel lblExhaust;
    private javax.swing.JLabel lblFocusedChampion;
    private javax.swing.JLabel lblGhost;
    private javax.swing.JLabel lblHeal;
    private javax.swing.JLabel lblIgnite;
    private javax.swing.JLabel lblSmite;
    private javax.swing.JLabel lblSpell1;
    private javax.swing.JLabel lblSpell2;
    private javax.swing.JPanel pnChampions;
    private javax.swing.JPanel pnChosenChamps;
    private javax.swing.JPanel pnChosenSpells;
    private javax.swing.JPanel pnSpells;
    // End of variables declaration//GEN-END:variables

}
