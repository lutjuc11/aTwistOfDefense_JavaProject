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
 * @author Jürgen Luttenberger
 * 15.05.2015
 * aTwistOfDefense_JavaProject
 */
public class GameScreen extends javax.swing.JFrame {

    /**
     * Contains all three Units, chosen in the SelectScreen.
     *
     * @see Unit
     */
    private LinkedList<Unit> champList = new LinkedList<>();
    /**
     * Contains 2 spells, chosen in the SelectScreen.
     */
    private LinkedList<String> spellList = new LinkedList<>();
    /**
     * Contains the 2 different types of turrets available in the game.
     *
     * @see Unit
     */
    private LinkedList<Unit> turretList = new LinkedList<>();
    /**
     * Contains the 2 different types of minions available in the game.
     *
     * @see Unit
     */
    private LinkedList<Unit> minionList = new LinkedList<>();
    /**
     * Constantly contains 3 Threads containing your champs chosen in the Select
     * Screen.
     *
     * @see UnitThread
     * @see champList
     * @see Unit
     */
    private List<GameScreen.UnitThread> unitsThreadList = new LinkedList<>();
    /**
     * Constantly contains 3 Threads containing your enemy's champs chosen in
     * the Select Screen.
     *
     * @see UnitThread
     * @see champList
     * @see Unit
     */
    private List<GameScreen.UnitThread> enemyUnitsThreadList = new LinkedList<>();
    /**
     * Contains the Threads of your minions.
     *
     * @see UnitThread
     * @see minionList
     * @see Unit
     */
    private List<GameScreen.UnitThread> minionsThreadList = new LinkedList<>();
    /**
     * Contains the Threads of your enemy's minions.
     *
     * @see UnitThread
     * @see minionList
     * @see Unit
     */
    private List<GameScreen.UnitThread> enemyMinionsThreadList = new LinkedList<>();
    /**
     * Contains the Threads of your turrets.
     *
     * @see UnitThread
     * @see turretList
     * @see Unit
     */
    private List<GameScreen.TurretThread> turretsThreadList = new LinkedList<>();
    /**
     * Contains the Threads of your enemy's turrets.
     *
     * @see UnitThread
     * @see turretList
     * @see Unit
     */
    private List<GameScreen.TurretThread> enemyTurretsThreadList = new LinkedList<>();

    /**
     * The DrawPanel is dynamically devided into a certain amount of fields.
     * This fieldWidth is saved on this variable.
     *
     * @see drawPanel
     * @see amountOfFields
     */
    private double fieldWidth;
    /**
     * The DrawPanel is dynamically devided into a certain amount of fields. The
     * number of fields is saved on this variable.
     *
     * @see drawPanel
     * @see fieldWidth
     */
    private int amountOfFields;

    /**
     * object of the MoneyThread
     *
     * @see MoneyThread
     */
    private MoneyThread mt;
    /**
     * object of the TimerThread
     *
     * @see TimerThread
     */
    private TimerThread timt;
    /**
     * object of the SpawnTimerThread
     *
     * @see SpawnTimerThread
     */
    private SpawnTimerThread spawnt;
    /**
     * object of the SpellTimerThread
     *
     * @see SpellTimerThread
     */
    private SpellTimerThread spellt;

    /**
     * variable is given a value once you spawn an enemy. The GameClient then
     * will use a getter method to retrieve it and set it to null again
     *
     * @see getEnemyUnit
     */
    private Unit enemyUnit = null;
    /**
     * variable is given a value once you cast a spell The GameClient then will
     * use a getter method to retrieve it and set it to null again
     *
     * @see getEnemySpell
     */
    private String enemySpell = null;

    /**
     * variable indicating the state of the game. When it is in the running
     * state the game is running. Otherwise you will have the values: "winner",
     * "loser", "surrender", "the enemy has surrendered"
     */
    private String game = "running";

    /**
     * Constructor of the GameScreen all the data from the previous screens is
     * set, the lists get filled and the threads get started
     *
     * @param nickname
     * @param champions
     * @param spells
     * @param minions
     */
    public GameScreen(String nickname, LinkedList<Unit> champions, LinkedList<String> spells, LinkedList<Unit> minions) {
        initComponents();

        unitsThreadList = Collections.synchronizedList(unitsThreadList);
        enemyUnitsThreadList = Collections.synchronizedList(enemyUnitsThreadList);
        minionsThreadList = Collections.synchronizedList(minionsThreadList);
        enemyMinionsThreadList = Collections.synchronizedList(enemyMinionsThreadList);
        turretsThreadList = Collections.synchronizedList(turretsThreadList);
        enemyTurretsThreadList = Collections.synchronizedList(enemyTurretsThreadList);

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
        menSpell1.setActionCommand(spells.get(0));
        menSpell2.setText(spells.get(1));
        menSpell2.setActionCommand(spells.get(1));
        champList = champions;
        spellList = spells;
        minionList = minions;
        turretList.add(new Unit(24, "OuterTurret", 200, 100, 0, 150, 100, 1000, 150, 0, "Turret", 0));
        turretList.add(new Unit(25, "InnerTurret", 300, 150, 0, 300, 200, 1000, 100, 0, "Turret", 0));
        unitsThreadList.add(new GameScreen.UnitThread(champList.get(0)));
        unitsThreadList.add(new GameScreen.UnitThread(champList.get(1)));
        unitsThreadList.add(new GameScreen.UnitThread(champList.get(2)));

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        fieldWidth = (gd.getDisplayMode().getWidth() - 20) / 200;
        fieldWidth = (int) fieldWidth;
        amountOfFields = (int) (gd.getDisplayMode().getWidth() / fieldWidth);

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

        spawnt = new SpawnTimerThread();
        spawnt.start();

        spellt = new SpellTimerThread();
        spellt.start();

        repaint();
    }

    /**
     * overwritten paint method is used to graphically display the game it is
     * periodically called to reduce the flickering by the TimerThread (due to
     * the lack of time we couldn't reduce nor remove the flickering)
     *
     * @see TimerThread
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        try {
            g = drawPanel.getGraphics();
            BufferedImage image;
            //this.g = g;
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
            int HealthBarX;

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
     * with this method the GameClient gets the Unit, which has been spawned.
     *
     * @return Unit
     */
    public Unit getEnemyUnit() {
        return enemyUnit;
    }

    /**
     * with this method the GameClient gets the Spell, which has been cast.
     *
     * @return
     */
    public String getEnemySpell() {
        return enemySpell;
    }

    /**
     * Whenever the enemy has spawned a Unit, the GameClient calls this method A
     * new Thread will be started and added to the right list
     *
     * @see enemyUnitsThreadList
     * @see enemyMinionsThreadList
     * @param unit
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
     * Whenever the enemy has cast a Spell, the GameClient calls this method The
     * Spells get exectuted
     *
     * @param spell
     */
    public void enemySpell(String spell) {
        switch (spell) {
            case "Heal":
                spellHeal(true);
                break;
            case "Exhaust":
                spellExhaust(true);
                break;
            case "Ignite":
                spellIgnite(true);
                break;
            case "Smite":
                spellSmite(true);
                break;
            case "Ghost":
                spellGhost(true);
                break;
        }
    }

    /**
     * once the GameClient has informed the enemy client about a cast Spell, the
     * enemySpell variable is set to null again
     *
     * @see enemySpell
     */
    public void setEnemySpellNull() {
        enemySpell = null;
    }

    /**
     * once the GameClient has informed the enemy client about a spawned Unit,
     * the enemyUnit variable is set to null again
     *
     * @see enemyUnit
     */
    public void setEnemyUnitNull() {
        enemyUnit = null;
    }

    /**
     * returns the state of the game to the GameClient
     *
     * @return game
     */
    public String getGameStatus() {
        return game;
    }

    /**
     * Method primarily used to inform the client if the enemy has surrendered
     * it overwrites the game status
     *
     * @param status
     */
    public void setGameStatus(String status) {
        game = status;
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
        menMessage = new javax.swing.JMenu();

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
        menSurrender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onSurrender(evt);
            }
        });
        menPlayer.add(menSurrender);

        jMenuBar1.add(menPlayer);

        menTroops.setText("Troops");

        menChampion1.setText("Champion1");
        menChampion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCreateChampion(evt);
            }
        });
        menTroops.add(menChampion1);

        menChampion2.setText("Champion2");
        menChampion2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCreateChampion(evt);
            }
        });
        menTroops.add(menChampion2);

        menChampion3.setText("Champion3");
        menChampion3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCreateChampion(evt);
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
                onSpell(evt);
            }
        });
        menSpells.add(menSpell1);

        menSpell2.setText("Spell2");
        menSpell2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onSpell(evt);
            }
        });
        menSpells.add(menSpell2);

        jMenuBar1.add(menSpells);

        MoneyBar.setText("xxxxx");
        MoneyBar.setPreferredSize(new java.awt.Dimension(100, 19));
        jMenuBar1.add(MoneyBar);
        jMenuBar1.add(menMessage);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * OnClick method to spawn a Melee Minion
     *
     * @param evt
     */
    private void onCreaterMeleeMinion(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCreaterMeleeMinion
        menMessage.setText("");
        if (spawnt.canSpawn()) {
            if (mt.getBalance() >= 150) {
                if (minionsThreadList.size() < 3) {
                    minionsThreadList.add(new GameScreen.UnitThread(minionList.get(0)));
                    minionsThreadList.get(minionsThreadList.size() - 1).start();
                    enemyUnit = minionsThreadList.get(minionsThreadList.size() - 1).getUnit();
                    mt.spawnMeleeMinion();
                }
                spawnt.spawning();
            } else {
                menMessage.setText("You don't have enough money to spawn another champion");
            }
        } else {
            menMessage.setText("You cannot spawn another champion within the next few seconds");
        }
    }//GEN-LAST:event_onCreaterMeleeMinion

    /**
     * OnClick method to spawn a Caster Minion
     *
     * @param evt
     */
    private void onCreaterCasterMinion(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCreaterCasterMinion
        menMessage.setText("");
        if (spawnt.canSpawn()) {
            if (mt.getBalance() >= 200) {
                if (minionsThreadList.size() < 3) {
                    minionsThreadList.add(new GameScreen.UnitThread(minionList.get(1)));
                    minionsThreadList.get(minionsThreadList.size() - 1).start();
                    enemyUnit = minionsThreadList.get(minionsThreadList.size() - 1).getUnit();
                    mt.spawnCasterMinion();
                }
                spawnt.spawning();
            } else {
                menMessage.setText("You don't have enough money to spawn another champion");
            }
        } else {
            menMessage.setText("You cannot spawn another champion within the next few seconds");
        }
    }//GEN-LAST:event_onCreaterCasterMinion

    /**
     * OnClick method to cast a spell
     *
     * @param evt
     */
    private void onSpell(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onSpell
        menMessage.setText("");
        if (spellt.canCast()) {
            String tempSpell = evt.getActionCommand();
            enemySpell = tempSpell;
            switch (tempSpell) {
                case "Heal":
                    spellHeal(false);
                    break;
                case "Exhaust":
                    spellExhaust(false);
                    break;
                case "Ignite":
                    spellIgnite(false);
                    break;
                case "Smite":
                    spellSmite(false);
                    break;
                case "Ghost":
                    spellGhost(false);
                    break;
                default:
                    System.out.println("error in config");
            }
            spellt.spelling();
        } else {
            menMessage.setText("You cannot cast another spell within the next few seconds");
        }
    }//GEN-LAST:event_onSpell

    /**
     * OnClick method to spawn a new Champion
     *
     * @param evt
     */
    private void onCreateChampion(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCreateChampion
        menMessage.setText("");
        if (spawnt.canSpawn()) {
            UnitThread temp = null;
            for (UnitThread uT : unitsThreadList) {
                if (uT.getUnit().getDisplayname().equals(evt.getActionCommand())) {
                    temp = uT;
                }
            }
            if (mt.getBalance() >= temp.getUnit().getCosts()) {
                if (temp == null || !temp.isAlive()) {
                    temp.start();
                    enemyUnit = temp.getUnit();
                    mt.spawnChampion(temp.getUnit());
                }
                spawnt.spawning();
            } else {
                menMessage.setText("You don't have enough money to spawn another champion! " + temp.getUnit().getDisplayname() + " costs " + temp.getUnit().getCosts());
            }
        } else {
            menMessage.setText("You cannot spawn another champion within the next few seconds");
        }
    }//GEN-LAST:event_onCreateChampion

    /**
     * OnClick method to surrender the game
     *
     * @param evt
     */
    private void onSurrender(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onSurrender
        menMessage.setText("");
        game = "surrender";
    }//GEN-LAST:event_onSurrender

    /**
     * casts the spell "Heal" the boolean enemy indicates who to affect
     *
     * @param enemy
     */
    public void spellHeal(boolean enemy) {
        if (!enemy) {
            for (UnitThread uT : unitsThreadList) {
                if (uT.isAlive()) {
                    uT.setCurrentHealth((uT.getCurrentHealth() + uT.getMaxHealth() / 2) > uT.getMaxHealth() ? uT.getMaxHealth() : uT.getCurrentHealth() + uT.getMaxHealth() / 2);
                }
            }
        } else {
            for (UnitThread uT : enemyUnitsThreadList) {
                if (uT.isAlive()) {
                    uT.setCurrentHealth((uT.getCurrentHealth() + uT.getMaxHealth() / 2) > uT.getMaxHealth() ? uT.getMaxHealth() : uT.getCurrentHealth() + uT.getMaxHealth() / 2);
                }
            }
        }
    }

    /**
     * casts the spell "Exhaust" the boolean enemy indicates who to affect
     *
     * @param enemy
     */
    public void spellExhaust(boolean enemy) {
        if (!enemy) {
            for (UnitThread uT : enemyUnitsThreadList) {
                if (uT.isAlive()) {
                    uT.setCurrentSpeed(uT.getCurrentSpeed() * 4);
                }
            }
        } else {
            for (UnitThread uT : unitsThreadList) {
                if (uT.isAlive()) {
                    uT.setCurrentSpeed(uT.getCurrentSpeed() * 4);
                }
            }
        }
    }

    /**
     * casts the spell "Ignite" the boolean enemy indicates who to affect
     *
     * @param enemy
     */
    public void spellIgnite(boolean enemy) {
        if (!enemy) {
            for (UnitThread uT : enemyUnitsThreadList) {
                if (uT.isAlive()) {
                    uT.setCurrentHealth(uT.getCurrentHealth() - uT.getMaxHealth() / 2);
                }
            }
        } else {
            for (UnitThread uT : unitsThreadList) {
                if (uT.isAlive()) {
                    uT.setCurrentHealth(uT.getCurrentHealth() - uT.getMaxHealth() / 2);
                }
            }
        }
    }

    /**
     * casts the spell "Smite" the boolean enemy indicates who to affect
     *
     * @param enemy
     */
    public void spellSmite(boolean enemy) {
        if (!enemy) {
            for (UnitThread uT : enemyMinionsThreadList) {
                if (uT.isAlive()) {
                    uT.setCurrentHealth(uT.getCurrentHealth() - uT.getMaxHealth() / 2);
                }
            }
        } else {
            for (UnitThread uT : minionsThreadList) {
                if (uT.isAlive()) {
                    uT.setCurrentHealth(uT.getCurrentHealth() - uT.getMaxHealth() / 2);
                }
            }
        }
    }

    /**
     * casts the spell "Ghost" the boolean enemy indicates who to affect
     *
     * @param enemy
     */
    public void spellGhost(boolean enemy) {
        if (!enemy) {
            for (UnitThread uT : unitsThreadList) {
                if (uT.isAlive()) {
                    uT.setCurrentSpeed(uT.getCurrentSpeed() / 4);
                }
            }
        } else {
            for (UnitThread uT : enemyUnitsThreadList) {
                if (uT.isAlive()) {
                    uT.setCurrentSpeed(uT.getCurrentSpeed() / 4);
                }
            }
        }

    }

    /**
     * Thread class to controll the Turrets
     */
    class TurretThread extends Thread {

        /**
         * Unit, which provides all the information for how the Thread has to
         * react
         */
        private Unit turret;
        /**
         * x position on the DrawPanel the Turret is placed on
         *
         * @see DrawPanel
         */
        private int x;
        /**
         * value of the turret's current health
         */
        private int currentHealth;
        /**
         * value of the turret's width
         */
        private int turretWidth;

        /**
         * Constructor where the type of Unit is set and the placement The
         * currentHealth gets the maximum Health directly from the Unit
         *
         * @param turret
         * @param x
         */
        public TurretThread(Unit turret, int x) {
            this.turret = turret;
            this.currentHealth = turret.getHealth();
            this.x = x;
        }

        /**
         * Overwritten run method, which controlls the turret Whenever enemy
         * units come to close to the turret, it deals damage Once the turret's
         * currentHealth falls to zero (or below), the TurretThread will be
         * interrupted and remove from its list.
         *
         * @see doDamage
         */
        @Override
        public void run() {
            while (currentHealth > 10) {
                try {
                    Thread.sleep((int) turret.getAttackspeed());
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

        /**
         * getter method to get the Unit
         *
         * @return
         */
        public Unit getTurret() {
            return turret;
        }

        /**
         * getter method to get the X coordinate of the Turret
         *
         * @return
         */
        public int getX() {
            return x;
        }

        /**
         * setter method to set the X coordinate of the Turret
         *
         * @param x
         */
        public void setX(int x) {
            this.x = x;
        }

        /**
         * getter method to get the current Health of the turret
         *
         * @see currentHealth
         * @return
         */
        public int getCurrentHealth() {
            return currentHealth;
        }

        /**
         * setter method to set the current Health of the turret
         *
         * @see currentHealth
         * @param health
         */
        public void setCurrentHealth(int health) {
            this.currentHealth = health;
        }

        /**
         * getter method to get the turret's maximal Health
         *
         * @return
         */
        public int getMaxHealth() {
            return turret.getHealth();
        }

        /**
         * getter method to get the turret's width
         *
         * @return
         */
        public int getTurretWidth() {
            return turretWidth;
        }

        /**
         * setter method to set the turret's width
         *
         * @param turretWidth
         */
        public void setTurretWidth(int turretWidth) {
            this.turretWidth = turretWidth;
        }

        /**
         * Considering the Turret's ap and ad values and the unit's magic
         * resistance and armor values the damage on the attacking unit will be
         * calculated and set
         *
         * @param damageDealingUnit
         * @param damageGettingUnit
         */
        public void doDamage(TurretThread damageDealingUnit, UnitThread damageGettingUnit) {
            int damage = (int) (100.0 / (100 + damageGettingUnit.getUnit().getArmor()) * damageDealingUnit.getTurret().getAd()) + (int) ((100.0 / (100 + damageGettingUnit.getUnit().getMagicres())) * damageDealingUnit.getTurret().getAp());
            damageGettingUnit.setCurrentHealth(damageGettingUnit.getCurrentHealth() - damage);
        }

    }

    /**
     * Thread class to controll the Champions and Minions
     */
    class UnitThread extends Thread {

        /**
         * Unit, which provides all the information for how the Thread has to
         * react
         */
        private Unit unit;
        /**
         * x position on the DrawPanel the Union is placed on
         *
         * @see DrawPanel
         */
        private int x = 0;
        /**
         * boolean to indicate whether the unit is one of yours or your enemy's
         */
        private boolean enemy = false;

        /**
         * value of the unit's current health
         */
        private int currentHealth;
        /**
         * value of the unit's current movement speed
         */
        private int currentSpeed;

        /**
         * value of the unit's width
         */
        private int unitWidth;

        /**
         * Constructor for the UnitThread (for your own Units)
         *
         * @param unit
         */
        public UnitThread(Unit unit) {
            this.unit = unit;
            this.currentHealth = unit.getHealth();
            this.currentSpeed = unit.getMovespeed();
        }

        /**
         * Overloaded Constructor for the UnitThread (for your enemy's Units)
         *
         * @param unit
         * @param enemy
         */
        public UnitThread(Unit unit, boolean enemy) {
            this.unit = unit;
            this.currentHealth = unit.getHealth();
            this.currentSpeed = unit.getMovespeed();
            this.enemy = enemy;
            x = (((int) (fieldWidth)) * (amountOfFields - 2) - (((int) (fieldWidth)) * 10));
        }

        /**
         * Overwritten run method, which controlls the units Whenever own units
         * are in the way the unit waits Whenever an enemy unit is in the way
         * damage is dealt Whenever an enemy tower is in the way damage is dealt
         * Whenever the enemy nexus is reached the game is won (Everything is
         * done vice verca)
         *
         * Whenever a unit's current Health falls to zero, the Thread gets
         * interrupted and removed from its list A new Thread Object with the
         * same Unit will be created immediately to fill its place
         *
         * @see doDamage
         */
        @Override
        public void run() {
            while (currentHealth > 0) {
                try {
                    Thread.sleep(currentSpeed);
                } catch (InterruptedException ex) {
                    System.out.println(ex.toString());
                }
                try {
                    boolean move = true;
                    if (!enemy) {
                        if (x >= ((int) (fieldWidth)) * (amountOfFields - 3) - ((int) (fieldWidth * 10))) {
                            game = "winner";
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
                                if (uT.isAlive()) {
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
                                if (uT.isAlive()) {
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
                            game = "loser";
                        }
                        for (UnitThread uT : unitsThreadList) {
                            if (uT.getX() + uT.getUnitWidth() >= this.getX() - unit.getRange()) {
                                if (uT.isAlive()) {
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
                                if (uT.isAlive()) {
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

            if (enemy) {
                if (unit.getTyp().equalsIgnoreCase("minion")) {
                    mt.MinionKilledMoney();
                } else {
                    mt.ChampionKilledMoney();
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

        /**
         * setter method to set the unit's width
         *
         * @param unitWidth
         */
        public void setUnitWidth(int unitWidth) {
            this.unitWidth = unitWidth;
        }

        /**
         * getter method to get the unit's width
         *
         * @return
         */
        public int getUnitWidth() {
            return unitWidth;
        }

        /**
         * getter method to get the x coordinate of the unit
         *
         * @return
         */
        public int getX() {
            return x;
        }

        /**
         * getter method to get the Unit
         *
         * @return
         */
        public Unit getUnit() {
            return unit;
        }

        /**
         * getter method to get the unit's current health
         *
         * @return
         */
        public int getCurrentHealth() {
            return currentHealth;
        }

        /**
         * setter method to set the unit's current health
         *
         * @param health
         */
        public void setCurrentHealth(int health) {
            this.currentHealth = health;
        }

        /**
         * getter method to get the unit's maximal health
         *
         * @return
         */
        public int getMaxHealth() {
            return unit.getHealth();
        }

        /**
         * setter method to set the unit's current speed
         *
         * @param speed
         */
        public void setCurrentSpeed(int speed) {
            currentSpeed = speed;
        }

        /**
         * getter method to get the unit's current speed
         *
         * @return
         */
        public int getCurrentSpeed() {
            return currentSpeed;
        }

        /**
         * Considering the unit's ap and ad values and the enemy unit's magic
         * resistance and armor values the damage on the enemy unit will be
         * calculated and set
         *
         * @param damageDealingUnit
         * @param damageGettingUnit
         */
        public void doDamage(UnitThread damageDealingUnit, UnitThread damageGettingUnit) {
            int damage = (int) (100.0 / (100 + damageGettingUnit.getUnit().getArmor()) * damageDealingUnit.getUnit().getAd()) + (int) ((100.0 / (100 + damageGettingUnit.getUnit().getMagicres())) * damageDealingUnit.getUnit().getAp());
            if (damageDealingUnit.getUnit().getDisplayname().toLowerCase().equals("masteryi")) {
                switch (damageGettingUnit.getUnit().getDisplayname().toLowerCase()) {
                    case "alistar":
                        damage *= 2;break;
                    case "renekton":
                        damage *= 2;break;
                    case "singed":
                        damage *= 2;break;
                    case "thresh":
                        damage *= 2;break;
                }
            }
            damageGettingUnit.setCurrentHealth(damageGettingUnit.getCurrentHealth() - damage);
        }

        /**
         * Considering the unit's ap and ad values and the enemy turret's magic
         * resistance and armor values the damage on the enemy turret will be
         * calculated and set
         *
         * @param damageDealingUnit
         * @param damageGettingUnit
         */
        public void doDamage(UnitThread damageDealingUnit, TurretThread damageGettingUnit) {
            int damage = (int) (100.0 / (100 + damageGettingUnit.getTurret().getArmor()) * damageDealingUnit.getUnit().getAd()) + (int) ((100.0 / (100 + damageGettingUnit.getTurret().getMagicres())) * damageDealingUnit.getUnit().getAp());
            damageGettingUnit.setCurrentHealth(damageGettingUnit.getCurrentHealth() - damage);
        }

        /**
         * method to check whether the unit's health is above zero or not
         *
         * @return
         */
        public boolean checkUnitAlive() {
            return currentHealth > 0;
        }

    }

    /**
     * Thread, which controlls the player's money
     */
    class MoneyThread extends Thread {

        /**
         * amount of money the player currently has a starting value is set
         */
        private int money = 500;
        /**
         * random generator
         */
        private Random rand = new Random();

        /**
         * overwritten run method, which increases the amount of money every
         * second by 10 + a random value between 0 and 5
         */
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

        /**
         * removes the amount of money, which spawning a champion costs
         */
        public void spawnChampion(Unit unit) {
            money -= unit.getCosts();
            MoneyBar.setText("" + money);
        }

        /**
         * removes the amount of money, which spawning a melee minion costs
         */
        public void spawnMeleeMinion() {
            money -= 150;
            MoneyBar.setText("" + money);
        }

        /**
         * removes the amount of money, which spawning a caster minion costs
         */
        public void spawnCasterMinion() {
            money -= 200;
            MoneyBar.setText("" + money);
        }

        /**
         * adds an amount of money, which you get by killing a champion
         */
        public void ChampionKilledMoney() {
            money += 200;
            MoneyBar.setText("" + money);
        }

        /**
         * adds an amount of money, which you get by killing a minion
         */
        public void MinionKilledMoney() {
            money += 100;
            MoneyBar.setText("" + money);
        }

        /**
         * getter method to get the current amount of money
         *
         * @return
         */
        public int getBalance() {
            return money;
        }

    }

    /**
     * Timer, which controlls when the next unit can be spawned
     */
    class SpawnTimerThread extends Thread {

        /**
         * variable, which indicates whether you're free to spawn a unit or not
         */
        private boolean spawn = true;

        /**
         * overwritten run method, which constantly looks whether a unit has
         * been spawned and sets the spawn variable regarding its state
         */
        @Override
        public void run() {
            while (!interrupted()) {
                if (spawn) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                    }
                } else {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                    }
                    spawn = true;
                }
            }
        }

        /**
         * method is called when a unit has been spawned it disables the player
         * to spawn new units
         */
        public void spawning() {
            spawn = false;
        }

        /**
         * method returns whether you can spawn a new unit or not
         *
         * @return
         */
        public boolean canSpawn() {
            return spawn;
        }

    }

    /**
     * Timer, which controlls when the next spell can be cast
     */
    class SpellTimerThread extends Thread {

        /**
         * variable, which indicates whether you're free to cast a spell or not
         */
        private boolean cast = true;

        /**
         * overwritten run method, which constantly looks whether a spell has
         * been cast and sets the cast variable regarding its state
         */
        @Override
        public void run() {
            while (!interrupted()) {
                if (cast) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                    }
                } else {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                    }
                    cast = true;
                }
            }
        }

        /**
         * method is called when a spell has been cast it disables the player to
         * cast new spells
         */
        public void spelling() {
            cast = false;
        }

        /**
         * method returns whether you can cast a new spell or not
         *
         * @return
         */
        public boolean canCast() {
            return cast;
        }
    }

    /**
     * Timer which constantly updates the graphical interface
     */
    class TimerThread extends Thread {

        /**
         * overwritten run method, which only stops when the game isn't running
         * anymore
         */
        @Override
        public void run() {
            while (game.equals("running")) {
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
    private javax.swing.JMenu menMessage;
    private javax.swing.JMenu menPlayer;
    private javax.swing.JMenuItem menSpell1;
    private javax.swing.JMenuItem menSpell2;
    private javax.swing.JMenu menSpells;
    private javax.swing.JMenuItem menSurrender;
    private javax.swing.JMenu menTroops;
    // End of variables declaration//GEN-END:variables
}
