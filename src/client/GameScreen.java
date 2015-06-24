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
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.interrupted;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;

/**
 *
 * @author Juergen
 */
public class GameScreen extends javax.swing.JFrame {

    /**
     * Contains all three Units, chosen in the SelectScreen.
     * @see Unit
     */
    private LinkedList<Unit> champList = new LinkedList<>();
    
    private LinkedList<String> spellList = new LinkedList<>();
    private LinkedList<Unit> turretList = new LinkedList<>();
    private LinkedList<Unit> minionList = new LinkedList<>();
    private Graphics g;
    private List<GameScreen.UnitThread> unitsThreadList = new LinkedList<>();
    private List<GameScreen.UnitThread> enemyUnitsThreadList = new LinkedList<>();
    private List<GameScreen.UnitThread> minionsThreadList = new LinkedList<>();
    private List<GameScreen.UnitThread> enemyMinionsThreadList = new LinkedList<>();
    private List<GameScreen.TurretThread> turretsThreadList = new LinkedList<>();
    private List<GameScreen.TurretThread> enemyTurretsThreadList = new LinkedList<>();

    private double fieldWidth;
    private int amountOfFields;
    private int HealthBarX;

    private MoneyThread mt;
    private TimerThread timt;
    int spawnTime = 0;
    
    private GameClient gc = new GameClient();

    private Unit enemyUnit = null;

    public GameScreen(String nickname, LinkedList<Unit> champions, LinkedList<String> spells, LinkedList<Unit> minions) {
        initComponents();

        unitsThreadList = Collections.synchronizedList(unitsThreadList);
        enemyUnitsThreadList = Collections.synchronizedList(enemyUnitsThreadList);
        minionsThreadList = Collections.synchronizedList(minionsThreadList);
        enemyMinionsThreadList = Collections.synchronizedList(enemyMinionsThreadList);
        turretsThreadList = Collections.synchronizedList(turretsThreadList);
        enemyTurretsThreadList = Collections.synchronizedList(enemyTurretsThreadList);
        
        
        
        //this.setLayout(null);
        drawPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        drawPanel.setDoubleBuffered(true);
        menPlayer.setText(nickname);
        menChampion1.setText(champions.get(0).getDisplayname());
        menChampion1.setActionCommand(menChampion1.getText());
        menChampion2.setText(champions.get(1).getDisplayname());
        menChampion2.setActionCommand(menChampion2.getText());
        menChampion3.setText(champions.get(2).getDisplayname());
        menChampion3.setActionCommand(menChampion3.getText());
        menSpell1.setText(spells.get(0));
        menSpell2.setText(spells.get(1));
        champList = champions;
        spellList = spells;
        minionList = minions;
        turretList.add(new Unit(24, "OuterTurret", 75, 50, 0, 300, 300, 1.00, 110, 0, "Turret", 0));
        turretList.add(new Unit(25, "InnerTurret", 100, 75, 0, 200, 200, 1.00, 110, 0, "Turret", 0));
        unitsThreadList.add(new GameScreen.UnitThread(champList.get(0)));
        unitsThreadList.add(new GameScreen.UnitThread(champList.get(1)));
        unitsThreadList.add(new GameScreen.UnitThread(champList.get(2)));

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        fieldWidth = (gd.getDisplayMode().getWidth() - 20) / 200;
        fieldWidth = (int) fieldWidth;
        //System.out.println("FIELD WIDTH: " + fieldWidth);
        //System.out.println("DRAWPANEL.getWidth(): " + drawPanel.getWidth());
        amountOfFields = (int) (gd.getDisplayMode().getWidth() / fieldWidth);
        //System.out.println("AMOUNT OF FIELDS: " + amountOfFields);

        turretsThreadList.add(new GameScreen.TurretThread(turretList.get(0), (int) (fieldWidth * 20)));
        turretsThreadList.add(new GameScreen.TurretThread(turretList.get(1), (int) (fieldWidth * 50)));
        enemyTurretsThreadList.add(new GameScreen.TurretThread(turretList.get(0), (int) ((int) (fieldWidth) * (amountOfFields - 3) - ((int) (fieldWidth)) * 20) - (int) (fieldWidth) * 8));
        enemyTurretsThreadList.add(new GameScreen.TurretThread(turretList.get(1), (int) ((int) (fieldWidth) * (amountOfFields - 3) - ((int) (fieldWidth)) * 50) - (int) (fieldWidth) * 8));

        if (turretsThreadList.size() == 0 || !turretsThreadList.get(0).isAlive()) {
            turretsThreadList.get(0).start();
            turretsThreadList.get(0).setTurretWidth((int) (fieldWidth * 8));
            turretsThreadList.get(1).start();
            turretsThreadList.get(1).setTurretWidth((int) (fieldWidth * 8));
        }
        if (enemyTurretsThreadList.size() == 0 || !enemyTurretsThreadList.get(0).isAlive()) {
            enemyTurretsThreadList.get(0).start();
            enemyTurretsThreadList.get(0).setTurretWidth((int) (fieldWidth * 8));
            enemyTurretsThreadList.get(1).start();
            enemyTurretsThreadList.get(1).setTurretWidth((int) (fieldWidth * 8));
        }
        
        mt = new MoneyThread();
        mt.start();

        timt = new TimerThread();
        timt.start();

        System.out.println("SPELL 1: "+spellList.get(0));
        
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        try {
            g = drawPanel.getGraphics();
            BufferedImage image;
            this.g = g;
// NEXUS 1
            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "nexusBlue.png"));
            g.drawImage(image, 0, drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, (int) (fieldWidth * 10), drawPanel.getHeight() / 3, drawPanel);

// NEXUS 2
            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "nexusRed.png"));
            g.drawImage(image, ((int) (fieldWidth)) * (amountOfFields - 3) - ((int) (fieldWidth * 10)), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, (int) (fieldWidth * 10), drawPanel.getHeight() / 3, null);

// TOWER THREAD 1
            if (turretsThreadList.size() > 0) {
                image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "towerBlue.png"));
                g.drawImage(image, (int) (fieldWidth * 20), drawPanel.getHeight() - drawPanel.getHeight() / 2 + 5, (int) (fieldWidth * 8), drawPanel.getHeight() / 2, null);
            }
// TOWER THREAD 2
            if (turretsThreadList.size() > 1) {
                image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "towerBlue.png"));
                g.drawImage(image, (int) (fieldWidth * 50), drawPanel.getHeight() - drawPanel.getHeight() / 2 + 5, (int) (fieldWidth * 8), drawPanel.getHeight() / 2, null);
            }
// TOWER THREAD 3
            if (enemyTurretsThreadList.size() > 0) {
                image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "towerRed.png"));
                g.drawImage(image, (int) ((int) (fieldWidth) * (amountOfFields - 3) - ((int) (fieldWidth)) * 20) - enemyTurretsThreadList.get(0).getTurretWidth(), drawPanel.getHeight() - drawPanel.getHeight() / 2 + 5, (int) (fieldWidth * 8), drawPanel.getHeight() / 2, null);
            }
// TOWER THREAD 4
            if (enemyTurretsThreadList.size() > 1) {
                image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "towerRed.png"));
                g.drawImage(image, (int) ((int) (fieldWidth) * (amountOfFields - 3) - ((int) (fieldWidth)) * 50) - enemyTurretsThreadList.get(1).getTurretWidth(), drawPanel.getHeight() - drawPanel.getHeight() / 2 + 5, (int) (fieldWidth * 8), drawPanel.getHeight() / 2, null);
            }
//Healthbars
            for (TurretThread tT : turretsThreadList) {
                HealthBarX = (tT.getCurrentHealth() * 100) / tT.getMaxHealth();
                g.setColor(Color.GREEN);
                g.fillRect(tT.getX(), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20 - 15, (40 * HealthBarX / 100), 10);
                g.setColor(Color.BLACK);
                g.drawRect(tT.getX(), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20 - 15, 40, 10);
                g.drawString(tT.getCurrentHealth() + "", (tT.getX()), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20 - 20);

            }
            for (TurretThread tT : enemyTurretsThreadList) {
                HealthBarX = (tT.getCurrentHealth() * 100) / tT.getMaxHealth();
                g.setColor(Color.RED);
                g.fillRect(tT.getX(), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20 - 15, (40 * HealthBarX / 100), 10);
                g.setColor(Color.BLACK);
                g.drawRect(tT.getX(), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20 - 15, 40, 10);
                g.drawString(tT.getCurrentHealth() + "", (tT.getX()), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20 - 20);

            }
// CHAMP THREADS
            int unitWidth;
            for (UnitThread uT : unitsThreadList) {
                if (uT.isAlive() && uT.getCurrentHealth() > 0) {
                    image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + uT.getUnit().getDisplayname() + ".png"));
                    unitWidth = drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight();

                    if (unitWidth < ((int) (fieldWidth)) * 1) {
                        unitWidth = ((int) (fieldWidth));
                    } else {

                        for (int i = 1; i < 10; i++) {
                            if ((unitWidth > ((int) (fieldWidth)) * i) && (unitWidth < (((int) (fieldWidth)) * i + 1) - (((int) (fieldWidth)) / 2))) {
                                unitWidth = (((int) (fieldWidth)) * i);
                                break;
                            } else {
                                if ((unitWidth > ((int) (fieldWidth)) * i) && (unitWidth < (((int) (fieldWidth)) * (i + 1)))) {
                                    unitWidth = (((int) (fieldWidth)) * (i + 1));
                                    break;
                                }
                            }
                        }
                    }
                    uT.setUnitWidth(unitWidth);
                    g.drawImage(image, uT.getX(), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, unitWidth, drawPanel.getHeight() / 3, null);

                    //Healthbar
                    HealthBarX = (uT.getCurrentHealth() * 100) / uT.getMaxHealth();
                    g.setColor(Color.GREEN);
                    g.fillRect(uT.getX() + ((unitWidth / 2) - 20), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20, 40 * HealthBarX / 100, 10);
                    g.setColor(Color.BLACK);
                    g.drawRect(uT.getX() + ((unitWidth / 2) - 20), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20, 40, 10);
                    g.drawString(uT.getCurrentHealth() + "", (uT.getX() + ((unitWidth / 2) - 20)), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20 - 5);
                }
            }

//ENEMY THREADS
            if (enemyUnitsThreadList.size() > 0) {
                for (UnitThread uT : enemyUnitsThreadList) {
                    if (uT.isAlive() && uT.currentHealth > 0) {
                        image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + uT.getUnit().getDisplayname() + ".png"));
                        unitWidth = drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight();

                        if (unitWidth < fieldWidth * 1) {
                            unitWidth = (int) fieldWidth;
                        } else {

                            for (int i = 1; i < 10; i++) {
                                if ((unitWidth > fieldWidth * i) && (unitWidth < (fieldWidth * i + 1) - (fieldWidth / 2))) {
                                    unitWidth = (int) (fieldWidth * i);
                                    break;
                                } else {
                                    if ((unitWidth > fieldWidth * i) && (unitWidth < (fieldWidth * (i + 1)))) {
                                        unitWidth = (int) (fieldWidth * (i + 1));
                                        break;
                                    }
                                }
                            }
                        }
                        uT.setUnitWidth(unitWidth);
                        g.drawImage(image, uT.getX() + unitWidth, drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, -unitWidth, drawPanel.getHeight() / 3, null);

                        //Healthbar
                        HealthBarX = (uT.getCurrentHealth() * 100) / uT.getMaxHealth();
                        g.setColor(Color.RED);
                        g.fillRect((uT.getX() + unitWidth) - ((unitWidth / 2) + 20), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20, 40 * HealthBarX / 100, 10);
                        g.setColor(Color.BLACK);
                        g.drawRect(uT.getX() + ((unitWidth / 2) - 20), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20, 40, 10);
                        g.drawString(uT.getCurrentHealth() + "", (uT.getX() + unitWidth) - ((unitWidth / 2) + 20), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20 - 5);
                    }
                }
            }

//MINION THREADS
            if (minionsThreadList.size() > 0) {
                for (UnitThread uT : minionsThreadList) {
                    if (uT.isAlive() && uT.getCurrentHealth() > 0) {
                        if (uT.getUnit().getDisplayname().equals("CasterMinion")) {
                            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "EnemyCasterMinion.png"));
                        } else {
                            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "EnemyMeeleMinion.png"));
                        }
                        unitWidth = drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight();

                        if (unitWidth < fieldWidth * 1) {
                            unitWidth = (int) fieldWidth;
                        } else {

                            for (int i = 1; i < 10; i++) {
                                if ((unitWidth > fieldWidth * i) && (unitWidth < (fieldWidth * i + 1) - (fieldWidth / 2))) {
                                    unitWidth = (int) (fieldWidth * i);
                                    break;
                                } else {
                                    if ((unitWidth > fieldWidth * i) && (unitWidth < (fieldWidth * (i + 1)))) {
                                        unitWidth = (int) (fieldWidth * (i + 1));
                                        break;
                                    }
                                }
                            }
                        }
                        uT.setUnitWidth(unitWidth);
                        g.drawImage(image, uT.getX(), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, unitWidth, drawPanel.getHeight() / 3, null);

                        //Healthbar
                        HealthBarX = (uT.getCurrentHealth() * 100) / uT.getMaxHealth();
                        g.setColor(Color.GREEN);
                        g.fillRect(uT.getX() + ((unitWidth / 2) - 20), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20, 40 * HealthBarX / 100, 10);
                        g.setColor(Color.BLACK);
                        g.drawRect(uT.getX() + ((unitWidth / 2) - 20), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20, 40, 10);
                    }
                }
            }

//ENEMY MINION THREADS
            if (enemyMinionsThreadList.size() > 0) {
                for (UnitThread uT : enemyMinionsThreadList) {
                    if (uT.isAlive() && uT.getCurrentHealth() > 0) {
                        //Minion
                        if (uT.getUnit().getDisplayname().equals("CasterMinion")) {
                            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "EnemyCasterMinion.png"));
                        } else {
                            image = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "res" + File.separator + "EnemyMeeleMinion.png"));
                        }
                        unitWidth = drawPanel.getHeight() / 3 * image.getWidth() / image.getHeight();

                        if (unitWidth < fieldWidth * 1) {
                            unitWidth = (int) fieldWidth;
                        } else {
                            for (int i = 1; i < 10; i++) {
                                if ((unitWidth > fieldWidth * i) && (unitWidth < (fieldWidth * i + 1) - (fieldWidth / 2))) {
                                    unitWidth = (int) (fieldWidth * i);
                                    break;
                                } else {
                                    if ((unitWidth > fieldWidth * i) && (unitWidth < (fieldWidth * (i + 1)))) {
                                        unitWidth = (int) (fieldWidth * (i + 1));
                                        break;
                                    }
                                }
                            }
                        }
                        uT.setUnitWidth(unitWidth);
                        g.drawImage(image, uT.getX() + unitWidth, drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1, -unitWidth, drawPanel.getHeight() / 3, null);

                        //Healthbar
                        HealthBarX = (uT.getCurrentHealth() * 100) / uT.getMaxHealth();
                        g.setColor(Color.RED);
                        g.fillRect((uT.getX() + unitWidth) - ((unitWidth / 2) + 20), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20, 40 * HealthBarX / 100, 10);
                        g.setColor(Color.BLACK);
                        g.drawRect((uT.getX() + unitWidth) - ((unitWidth / 2) + 20), drawPanel.getHeight() - drawPanel.getHeight() / 3 - 1 - 20, 40, 10);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } catch (ConcurrentModificationException ex) {
            System.out.println("Exception: repaint");
            repaint();
        }
    }
    
    
    /**
     * Returns the last spawned unit of the enemy.
     * @return Unit 
     */
    public Unit getEnemyUnit() {
        return enemyUnit;
        
    }

    /**
     * Starts the UnitThread of the last spawned unit of the enemy.
     * This method will add an UnitThread to enemyUnitsThreadList or enemyMinionsThreadList,
     * depending whether the spawned Unit is a champion or a minion. It will not
     * add the UnitThread to the list if the list already contains an UnitThread for
     * the champion, nor will the method add an UnitThread to the enemyMinionsThreadList
     * when this list already contains 3 elements.
     * @param unit 
     * @see Unit
     * @see UnitThread
     * @see enemyUnitsThreadList
     * @see enemyMinionsThreadList
     */
    public void startEnemyUnitThread(Unit unit) {        
        if (unit.getTyp().equals("Champ")) {
            if (!enemyUnitsThreadList.contains(unit)) {
                enemyUnitsThreadList.add(new GameScreen.UnitThread(unit, true));
            }
        } else {
            if (unit.getTyp().equals("Minion")) {
                int index = 1;
                if (unit.getDisplayname().equals("MeeleMinion")) {
                    index = 0;
                }
                if (enemyMinionsThreadList.size() < 3) {
                    enemyMinionsThreadList.add(new GameScreen.UnitThread(minionList.get(index), true));
                    enemyMinionsThreadList.get(enemyMinionsThreadList.size() - 1).start();
                }
            }
        }
        
        for (UnitThread uT : enemyUnitsThreadList) {
            if (uT.getUnit() == unit) {
                uT.start();
            }
        }
    }

    /**
     * sets the variable enemyUnit to null.
     */
    public void setEnemyUnitNull() {
        enemyUnit = null;
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
        menSpells = new javax.swing.JMenu();
        menSpell1 = new javax.swing.JMenuItem();
        menSpell2 = new javax.swing.JMenuItem();
        MoneyBar = new javax.swing.JMenu();

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
        menMeleeMinion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCreaterMeleeMinion(evt);
            }
        });
        menTroops.add(menMeleeMinion);

        menCasterMinion.setText("Caster Minion");
        menCasterMinion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCreaterCasterMinion(evt);
            }
        });
        menTroops.add(menCasterMinion);

        jMenuBar1.add(menTroops);

        menSpells.setText("Spells");

        menSpell1.setText("Spell1");
        menSpell1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menSpell1ActionPerformed(evt);
            }
        });
        menSpells.add(menSpell1);

        menSpell2.setText("Spell2");
        menSpells.add(menSpell2);

        jMenuBar1.add(menSpells);

        MoneyBar.setText("xxxxx");
        MoneyBar.setPreferredSize(new java.awt.Dimension(100, 19));
        jMenuBar1.add(MoneyBar);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onCreateChamp1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCreateChamp1
        spawnChampion(evt);
    }//GEN-LAST:event_onCreateChamp1

    private void onCreaterChamp2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCreaterChamp2
        spawnChampion(evt);
    }//GEN-LAST:event_onCreaterChamp2

    private void onCreaterChamp3(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCreaterChamp3
        spawnChampion(evt);
    }//GEN-LAST:event_onCreaterChamp3

    private void onCreaterMeleeMinion(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCreaterMeleeMinion
        if (mt.getBalance() >= 150) {
            if (minionsThreadList.size() < 3) {
                minionsThreadList.add(new GameScreen.UnitThread(minionList.get(0)));
                minionsThreadList.get(minionsThreadList.size() - 1).start();
                enemyUnit = minionsThreadList.get(minionsThreadList.size() - 1).getUnit();
                mt.spawnMeleeMinion();
            }

        }
    }//GEN-LAST:event_onCreaterMeleeMinion

    private void onCreaterCasterMinion(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCreaterCasterMinion
        if (mt.getBalance() >= 200) {
            if (minionsThreadList.size() < 3) {
                minionsThreadList.add(new GameScreen.UnitThread(minionList.get(1)));
                minionsThreadList.get(minionsThreadList.size() - 1).start();
                enemyUnit = minionsThreadList.get(minionsThreadList.size() - 1).getUnit();
                mt.spawnCasterMinion();
            }
        }
    }//GEN-LAST:event_onCreaterCasterMinion

    private void menSpell1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menSpell1ActionPerformed
        switch(menSpell1.getText()){
            case "Heal": 
                break;
            case "Ignite":break;
            case "Smite":break;
            case "Ghost":break;
            case "Exhaust":break;
        }
    }//GEN-LAST:event_menSpell1ActionPerformed
   
    /**
     * Spwans a champion, based on the ActionEvent parameter.
     * This method will start the appropriate UnitThread from the unitsTheadList, 
     * set the variable enemyUnit to the spawned Unit and deduct money using the MoneyThread.
     * @param evt 
     * @see UnitThread
     * @see Unit
     * @see unitsTheadList
     * @see MoneyThread
     */
    public void spawnChampion(ActionEvent evt) {
        if (mt.getBalance() >= 500) {
            UnitThread temp = null;
            for (UnitThread uT : unitsThreadList) {
                if (uT.getUnit().getDisplayname().equals(evt.getActionCommand())) {
                    temp = uT;
                }
            }
            if (temp == null || !temp.isAlive()) {
                temp.start();
                enemyUnit = temp.getUnit();
                mt.spawnChampion();
            }
        }
        
    }

    class TurretThread extends Thread {

        private Unit turret;
        private int x;
        private int currentHealth;
        private int turretWidth;

        public TurretThread(Unit turret, int x) {
            this.turret = turret;
            this.currentHealth = turret.getHealth();
            this.x = x;
        }

        @Override
        public void run() {
            while (currentHealth > 10) {
                try {
                    Thread.sleep(2000 - (int) (turret.getAttackspeed() * 1000));
                } catch (InterruptedException ex) {
                    System.out.println(ex.toString());
                }
                try {
                    if (turretsThreadList.contains(this)) {
                        for (UnitThread uT : enemyUnitsThreadList) {
                            if (this.getX() + turretWidth + turret.getRange() >= uT.getX()) {
                                if (currentHealth > 0) {
                                    doDamage(this, uT);
                                }
                                try {
                                    Thread.sleep((int) turret.getAttackspeed());
                                } catch (InterruptedException ex) {
                                }
                            }
                        }
                        for (UnitThread uT : enemyMinionsThreadList) {
                            if (this.getX() + turretWidth + turret.getRange() >= uT.getX()) {
                                if (currentHealth > 0) {
                                    doDamage(this, uT);
                                }
                                try {
                                    Thread.sleep((int) turret.getAttackspeed());
                                } catch (InterruptedException ex) {
                                }
                            }
                        }
                    } else {
                        for (UnitThread uT : unitsThreadList) {
                            if (uT.getX() + uT.getUnitWidth() >= this.getX() - turret.getRange()) {
                                if (currentHealth > 0) {
                                    doDamage(this, uT);
                                    try {
                                        Thread.sleep((int) turret.getAttackspeed());
                                    } catch (InterruptedException ex) {
                                    }
                                }
                            }
                        }
                        for (UnitThread uT : minionsThreadList) {
                            if (uT.getX() + uT.getUnitWidth() >= this.getX() - turret.getRange()) {
                                if (currentHealth > 0) {
                                    doDamage(this, uT);
                                    try {
                                        Thread.sleep((int) turret.getAttackspeed());
                                    } catch (InterruptedException ex) {
                                    }
                                }
                            }
                        }
                    }
                } catch (ConcurrentModificationException ex) {
                    System.out.println("Exception: " + turret.getDisplayname());
                    
                }
            }
            if (turretsThreadList.contains(this)) {
                synchronized (turretsThreadList) {
                    this.interrupt();
                    turretsThreadList.remove(this);
                }
            } else {
                synchronized (enemyTurretsThreadList) {
                    this.interrupt();
                    enemyTurretsThreadList.remove(this);
                }
            }
        }

        public Unit getTurret() {
            return turret;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getCurrentHealth() {
            return currentHealth;
        }

        public void setCurrentHealth(int health) {
            this.currentHealth = health;
        }

        public int getMaxHealth() {
            return turret.getHealth();
        }

        public int getTurretWidth() {
            return turretWidth;
        }

        public void setTurretWidth(int turretWidth) {
            this.turretWidth = turretWidth;
        }

        public void doDamage(TurretThread damageDealingUnit, UnitThread damageGettingUnit) {
            int damage = (int) (100.0 / (100 + damageGettingUnit.getUnit().getArmor()) * damageDealingUnit.getTurret().getAd()) + (int) ((100.0 / (100 + damageGettingUnit.getUnit().getMagicres())) * damageDealingUnit.getTurret().getAp());
            damageGettingUnit.setCurrentHealth(damageGettingUnit.getCurrentHealth() - damage);
        }

    }

    class UnitThread extends Thread {

        private Unit unit;
        private int x = 0;
        private boolean enemy = false;

        private int currentHealth;

        private int unitWidth = 1;

        public UnitThread(Unit unit) {
            this.unit = unit;
            this.currentHealth = unit.getHealth();
        }

        public UnitThread(Unit unit, boolean enemy) {
            this.unit = unit;
            this.currentHealth = unit.getHealth();
            this.enemy = enemy;
            x = (((int) (fieldWidth)) * (amountOfFields - 2) - (((int) (fieldWidth)) * 10));
        }

        @Override
        public void run() {
            while (currentHealth > 0) {
                try {
                    Thread.sleep(unit.getMovespeed());
                } catch (InterruptedException ex) {
                    System.out.println(ex.toString());
                }
                try {
                    boolean move = true;
                    if (!enemy) {
                        if (x >= ((int) (fieldWidth)) * (amountOfFields - 3) - ((int) (fieldWidth * 10))) {
                            System.out.println(menPlayer.getText() + " WIIIIINNNNNNNNNN");
                        }
                        for (UnitThread uT : unitsThreadList) {
                            if (uT != this && uT.isAlive()) {
                                if (uT.getX() > this.getX() && this.getX() + ((int) fieldWidth) + unitWidth > uT.getX()) {
                                    move = false;
                                }
                            }
                        }
                        for (UnitThread uT : enemyUnitsThreadList) {
                            if (this.getX() + unitWidth + unit.getRange() >= uT.getX()) {
                                move = false;
                                if (currentHealth > 0) {
                                    doDamage(this, uT);
                                }
                                try {
                                    Thread.sleep((int) unit.getAttackspeed());
                                } catch (InterruptedException ex) {
                                }
                            }
                        }
                        for (UnitThread uT : minionsThreadList) {
                            if (uT.getX() > this.getX() && uT != this && uT.isAlive()) {
                                if (this.getX() + ((int) fieldWidth) + unitWidth > uT.getX()) {
                                    move = false;
                                }
                            }
                        }
                        for (UnitThread uT : enemyMinionsThreadList) {
                            if (this.getX() + unitWidth + unit.getRange() >= uT.getX()) {
                                move = false;
                                if (currentHealth > 0) {
                                    doDamage(this, uT);
                                }
                                try {
                                    Thread.sleep((int) unit.getAttackspeed());
                                } catch (InterruptedException ex) {
                                }
                            }
                        }
                        for (TurretThread tT : enemyTurretsThreadList) {
                            if (this.getX() + unitWidth + unit.getRange() >= tT.getX()) {
                                move = false;
                                if (currentHealth > 0) {
                                    doDamage(this, tT);
                                }
                                try {
                                    Thread.sleep((int) unit.getAttackspeed());
                                } catch (InterruptedException ex) {
                                }
                            }
                        }
                    } else {
                        if (x <= ((int) (fieldWidth * 10))) {
                            System.out.println(menPlayer.getText() + " LOOOOOSEEEEEEEEEEEEEEEEe");
                        }
                        for (UnitThread uT : unitsThreadList) {
                            if (uT.getX() + uT.getUnitWidth() >= this.getX() - unit.getRange()) {
                                move = false;
                                if (currentHealth > 0) {
                                    doDamage(this, uT);
                                    try {
                                        Thread.sleep((int) unit.getAttackspeed());
                                    } catch (InterruptedException ex) {
                                    }
                                }
                            }
                        }
                        for (UnitThread uT : enemyUnitsThreadList) {
                            if (uT != this && uT.isAlive()) {
                                if (uT.getX() < this.getX() && uT.getX() + uT.getUnitWidth() + ((int) fieldWidth) > this.getX()) {
                                    move = false;
                                }
                            }
                        }
                        for (UnitThread uT : minionsThreadList) {
                            if (uT.getX() + uT.getUnitWidth() >= this.getX() - unit.getRange()) {
                                move = false;
                                if (currentHealth > 0) {
                                    doDamage(this, uT);
                                    try {
                                        Thread.sleep((int) unit.getAttackspeed());
                                    } catch (InterruptedException ex) {
                                    }
                                }
                            }
                        }
                        for (UnitThread uT : enemyMinionsThreadList) {
                            if (uT != this && uT.isAlive()) {
                                if (uT.getX() < this.getX() && uT.getX() + uT.getUnitWidth() + ((int) fieldWidth) > this.getX()) {
                                    move = false;
                                }
                            }
                        }
                        for (TurretThread tT : turretsThreadList) {
                            if (tT.getX() + tT.getTurretWidth() >= this.getX() - unit.getRange()) {
                                move = false;
                                if (currentHealth > 0) {
                                    doDamage(this, tT);
                                    try {
                                        Thread.sleep((int) unit.getAttackspeed());
                                    } catch (InterruptedException ex) {
                                    }
                                }
                            }
                        }
                    }

                    if (move) {
                        if (enemy) {
                            x -= fieldWidth;
                        } else {
                            x += fieldWidth;
                        }
                    }
                } catch (ConcurrentModificationException ex) {
                    System.out.println("Exception: " + unit.getDisplayname());
                }
            }

            if (unitsThreadList.contains(this)) {
                synchronized (unitsThreadList) {
                    this.interrupt();
                    unitsThreadList.remove(this);
                    unitsThreadList.add(new UnitThread(unit));
                }
            } else if (enemyUnitsThreadList.contains(this)) {
                synchronized (enemyUnitsThreadList) {
                    this.interrupt();
                    enemyUnitsThreadList.remove(this);
                }
            } else if (minionsThreadList.contains(this)) {
                synchronized (minionsThreadList) {
                    this.interrupt();
                    minionsThreadList.remove(this);
                }
            } else {
                synchronized (enemyMinionsThreadList) {
                    this.interrupt();
                    enemyMinionsThreadList.remove(this);
                }
            }
        }

        public void setUnitWidth(int unitWidth) {
            this.unitWidth = unitWidth;
        }

        public int getUnitWidth() {
            return unitWidth;
        }

        public int getX() {
            return x;
        }

        public Unit getUnit() {
            return unit;
        }

        public int getCurrentHealth() {
            return currentHealth;
        }

        public void setCurrentHealth(int health) {
            this.currentHealth = health;
        }

        public int getMaxHealth() {
            return unit.getHealth();
        }

        public void doDamage(UnitThread damageDealingUnit, UnitThread damageGettingUnit) {
            int damage = (int) (100.0 / (100 + damageGettingUnit.getUnit().getArmor()) * damageDealingUnit.getUnit().getAd()) + (int) ((100.0 / (100 + damageGettingUnit.getUnit().getMagicres())) * damageDealingUnit.getUnit().getAp());
            damageGettingUnit.setCurrentHealth(damageGettingUnit.getCurrentHealth() - damage);
        }

        public void doDamage(UnitThread damageDealingUnit, TurretThread damageGettingUnit) {
            int damage = (int) (100.0 / (100 + damageGettingUnit.getTurret().getArmor()) * damageDealingUnit.getUnit().getAd()) + (int) ((100.0 / (100 + damageGettingUnit.getTurret().getMagicres())) * damageDealingUnit.getUnit().getAp());
            damageGettingUnit.setCurrentHealth(damageGettingUnit.getCurrentHealth() - damage);
        }

        public boolean checkUnitAlive() {
            return currentHealth > 0;
        }

    }

    class MoneyThread extends Thread {

        private int money = 10000000;
        private Random rand = new Random();

        @Override
        public void run() {
            while (!interrupted()) {
                MoneyBar.setText("Gold: " + money);
                money += (10 + rand.nextInt(5));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
        }

        public void spawnChampion() {
            money -= 500;
            MoneyBar.setText("" + money);
        }

        public void spawnMeleeMinion() {
            money -= 150;
            MoneyBar.setText("" + money);
        }

        public void spawnCasterMinion() {
            money -= 200;
            MoneyBar.setText("" + money);
        }

        public void ChampionKilledMoney() {
            money += 200;
            MoneyBar.setText("" + money);
        }

        public void MinionKilledMoney() {
            money += 100;
            MoneyBar.setText("" + money);
        }

        public int getBalance() {
            return money;
        }

    }

    class TimerThread extends Thread {

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
                synchronized (unitsThreadList) {
                    synchronized (enemyUnitsThreadList) {
                        synchronized (minionsThreadList) {
                            synchronized (enemyMinionsThreadList) {
                                repaint();
                            }
                        }
                    }
                }
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu MoneyBar;
    private javax.swing.JPanel drawPanel;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuItem menCasterMinion;
    private javax.swing.JMenuItem menChampion1;
    private javax.swing.JMenuItem menChampion2;
    private javax.swing.JMenuItem menChampion3;
    private javax.swing.JMenuItem menMeleeMinion;
    private javax.swing.JMenu menPlayer;
    private javax.swing.JMenuItem menSpell1;
    private javax.swing.JMenuItem menSpell2;
    private javax.swing.JMenu menSpells;
    private javax.swing.JMenuItem menSurrender;
    private javax.swing.JMenu menTroops;
    // End of variables declaration//GEN-END:variables
}
