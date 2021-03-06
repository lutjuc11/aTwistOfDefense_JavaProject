/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import beans.Unit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.text.JTextComponent;

/**
 * @author Jürgen Luttenberger
 * 15.05.2015
 * aTwistOfDefense_JavaProject
 */
public class GamingServer {

    private ServerSocket server;
    private final int PORTNR;
    private final int TIMEOUT_DURATION = 500;

    private int noPlayer = 0;
    private int noPlayer2 = 0;

    private int playerClose = 2;

    private ServerThread st;
    private HashMap<String, ObjectOutputStream> clientMap = new HashMap<>();

    /**
     * Contains all playable Units and Turrets, read from a CSV file
     */
    private LinkedList<Unit> units = new LinkedList<>();

    private JTextComponent logArea;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss>");

    public GamingServer(int portnr) {
        this.PORTNR = portnr;
        //readCsv();
    }

    public GamingServer(int portnr, JTextComponent logArea) {
        this(portnr);
        this.logArea = logArea;
        readCsv();
    }

    /**
     * This method will start the ServerThread and therefore the server
     */
    public void startServer() {
        if (st == null || !st.isAlive()) {
            st = new ServerThread();
            try {
                server = new ServerSocket(PORTNR);
                server.setSoTimeout(TIMEOUT_DURATION);
            } catch (IOException ex) {
                log("ServerException: " + ex.toString());
            }
            st.start();
        }
    }

    public void stopServer() throws IOException {
        for (ObjectOutputStream coos : clientMap.values()) {
            coos.writeObject("###SERVER#CLOSED###");
        }
        if (st != null && st.isAlive()) {
            st.interrupt();
        }
    }

    /**
     * This method will output logging texts into the logArea.
     * @param logText 
     */
    private synchronized void log(String logText) {
        logText = sdf.format(new Date()) + " " + logText;
        if (logArea == null) {
            System.out.println(logText);
        } else {
            synchronized (logArea) {
                logArea.setText("".equals(logArea.getText()) ? logText : logArea.getText() + logText + System.lineSeparator());
            }
        }
    }

    /**
     * This method reads the CSV file, containing all playable Units and Turrets
     * and then add them into the units list.
     * @see Unit
     */
    public void readCsv() {
        FileReader fr = null;
        try {
            String sep = File.separator;
            fr = new FileReader(System.getProperty("user.dir") + sep + "src" + sep + "res" + sep + "UnitData.csv");
            BufferedReader br = new BufferedReader(fr);
            String line;
            String splitedLine[];
            int unitID, Health, ad, ap, armor, magicres, range, movespeed, costs;
            double attackspeed;
            String displayname, typ;

            while ((line = br.readLine()) != null) {
                splitedLine = line.split(";");
                unitID = Integer.parseInt(splitedLine[0]);
                displayname = splitedLine[1];
                Health = Integer.parseInt(splitedLine[2]);
                ad = Integer.parseInt(splitedLine[3]) * 2;
                ap = Integer.parseInt(splitedLine[4]) * 2;
                armor = Integer.parseInt(splitedLine[5]);
                magicres = Integer.parseInt(splitedLine[6]);
                attackspeed = Double.parseDouble(splitedLine[7]) * 2;
                range = Integer.parseInt(splitedLine[8]);
                movespeed = Integer.parseInt(splitedLine[9]);
                typ = splitedLine[10];
                costs = Integer.parseInt(splitedLine[11]);
                units.add(new Unit(unitID, displayname, Health, ad, ap, armor, magicres, attackspeed, range, movespeed, typ, costs));
            }
            log("Units from CSV file loaded");
        } catch (FileNotFoundException ex) {
            log(ex.toString());
        } catch (IOException ex) {
            log(ex.toString());
        }
    }
    
    /**
     * With this thread the Server is running
     */
    class ServerThread extends Thread {

        public ServerThread() {
            this.setPriority(Thread.MIN_PRIORITY + 2);
        }

        @Override
        public void run() {
            log("Server started on port: " + PORTNR);
            while (playerClose > 0) {
                try {
                    Socket socket = server.accept();
                    log("Connection established with: " + socket.getRemoteSocketAddress().toString());
                    new ClientCommunicationThread(socket).start();
                } catch (SocketTimeoutException ex) {
                    //log("Timeout from accept");
                } catch (Exception ex) {
                    log("ServerException: " + ex.toString());
                }
            }
            try {
                server.close();
                System.exit(0);
            } catch (Exception ex) {
                log("ServerException: " + ex.toString());
            }
            log("Server closed");
        }

    }

    /**
     * This Thread communicates with the clients and listens if they send
     * something.
     */
    class ClientCommunicationThread extends Thread {

        private Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private String nickname = null;

        public ClientCommunicationThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try {

                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());

                Object inputFromClient = ois.readObject();
                if (inputFromClient instanceof String) {
                    nickname = (String) inputFromClient;
                    if (clientMap.containsKey(nickname)) {
                        nickname = nickname + "1";
                    }
                    log(nickname + " connected");
                    clientMap.put(nickname, oos);
                    noPlayer++;
                    log(noPlayer + " out of 2 players online");
                }

                while (noPlayer < 2) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                    }
                }

                oos.writeObject(units);

                inputFromClient = ois.readObject();
                if (inputFromClient instanceof LinkedList) {
                    LinkedList<Unit> chosenChamps = (LinkedList<Unit>) inputFromClient;
                    log(nickname + " locked " + chosenChamps.get(0).getDisplayname() + ", " + chosenChamps.get(1).getDisplayname() + ", " + chosenChamps.get(2).getDisplayname());
                }
                noPlayer2++;

                while (noPlayer2 < 2) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                    }
                }

                oos.writeObject("###GO###");
                inputFromClient = ois.readObject();
                if (inputFromClient.equals("###READY###")) {
                    while (true) {
                        inputFromClient = ois.readObject();
                        if (inputFromClient instanceof Unit) {
                            for (ObjectOutputStream coos : clientMap.values()) {
                                if (!coos.equals(oos)) {
                                    coos.writeObject(inputFromClient);
                                }
                            }
                        }
                        if (inputFromClient instanceof String) {
                            if (((String) inputFromClient).equals("winner") || ((String) inputFromClient).equals("loser")) {
                                log(nickname + " " + inputFromClient);
                            } else if (((String) inputFromClient).equals("surrender")) {
                                for (ObjectOutputStream coos : clientMap.values()) {
                                    if (!coos.equals(oos)) {
                                        coos.writeObject("surrender");
                                    }
                                }
                            } else {
                                for (ObjectOutputStream coos : clientMap.values()) {
                                    if (!((String) inputFromClient).equals("###EXIT###")) {
                                        if (!coos.equals(oos)) {
                                            coos.writeObject(inputFromClient);
                                        }
                                    }
                                }
                                if (!((String) inputFromClient).equals("###EXIT###")) {
                                    log(nickname + " used " + (String) inputFromClient);
                                }
                            }
                        }

                        if (inputFromClient.equals("###EXIT###")) {
                            clientMap.remove(nickname);
                            break;
                        }
                    }
                }

            } catch (IOException ex) {
                log("Servercommunication  failure");
            } catch (ClassNotFoundException ex) {
                log("Servercommunication  failure");
            } finally {
                try {
                    log("User \"" + nickname + "\" logged out");
                    log("Connection lost with: " + socket.getRemoteSocketAddress().toString());
                    ois.close();
                    oos.close();
                    socket.close();
                    playerClose--;
                } catch (Exception ex) {
                    log("Server communication failure");
                }
            }
        }
    }
}
