/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import beans.Unit;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.interrupted;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import javax.swing.JFrame;

/**
 *
 * @author Juergen
 */
public class GameClient {

    private Socket socket = null;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private InputThread it;

    private String nickname;
    private JFrame connectScreen;

    private int player = 0;

    public void connectToServer(String nickname, InetAddress addr, int portnr, JFrame connectScreen, int player) throws IOException {
        socket = new Socket(addr, portnr);
        //socket.setSoTimeout(500);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        this.nickname = nickname;
        this.connectScreen = connectScreen;
        this.player = player;
        oos.writeObject(nickname);

        it = new InputThread();
        it.start();
    }

    public void logoutFromServer() throws IOException {
        oos.writeObject("###EXIT###");
        it.interrupt();
        oos.close();
        ois.close();
        socket.close();
    }

    class InputThread extends Thread {

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    Object serverResponse = ois.readObject();
                    if (serverResponse instanceof LinkedList) {
                        SelectScreen select = new SelectScreen((LinkedList<Unit>) serverResponse);
                        select.setSize(connectScreen.getWidth(), connectScreen.getHeight());
                        select.setLocation(connectScreen.getX(), connectScreen.getY());
                        connectScreen.setVisible(false);
                        select.setVisible(true);

                        while (select.getChosenChampions().size() < 3 || select.getChosenSpells().size() < 2) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                System.out.println(ex.toString());
                            }
                        }
                        LinkedList<Unit> chosenChampions = select.getChosenChampions();
                        oos.writeObject(chosenChampions);
                        serverResponse = ois.readObject();
                        if (serverResponse.equals("###GO###")) {
                            oos.writeObject("###READY###");
                            GameScreen gs = new GameScreen(nickname, chosenChampions, select.getChosenSpells(), select.getMinions());
                            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                            gs.setSize(gd.getDisplayMode().getWidth() - 20, select.getHeight() / 2);
                            gs.setLocation(10, select.getY() + (player == 2 ? select.getY() / 2 + 50 : 0));
                            select.setVisible(false);
                            gs.setVisible(true);

                            EnemySpawnThread est = new EnemySpawnThread(oos, ois, gs);
                            est.start();

                            while (true) {
                                if (gs.getEnemyUnit() != null) {
                                    oos.writeObject(gs.getEnemyUnit());
                                    gs.setEnemyUnitNull();
                                }

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                    System.out.println(ex.toString());
                                }

                            }
                        }

                    }

                } catch (SocketTimeoutException ex) {
                    //System.out.println(ex.toString());
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                } catch (ClassNotFoundException ex) {
                    System.out.println(ex.toString());
                }
            }
        }
    }

    class EnemySpawnThread extends Thread {

        private ObjectOutputStream oos;
        private ObjectInputStream ois;
        private GameScreen gs;

        public EnemySpawnThread(ObjectOutputStream oos, ObjectInputStream ois, GameScreen gs) {
            this.oos = oos;
            this.ois = ois;
            this.gs = gs;
        }

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    Object serverResponse = ois.readObject();
                    if (serverResponse instanceof Unit) {
                        gs.startEnemyUnitThread((Unit) serverResponse);
                    }
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                } catch (ClassNotFoundException ex) {
                    System.out.println(ex.toString());
                }
            }
        }
    }
}
