/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import client.StartScreen;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import server.ServerGUI;

/**
 * @author Jürgen Luttenberger
 * 15.05.2015
 * aTwistOfDefense_JavaProject
 */
public class StartProject {
    
    /**
     * This method will restart the application after one player won. It will
     * return all clients to the StartScreen.
     */
    public void restart()
    {
        //get screen size
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        
        //start server
        ServerGUI server = new ServerGUI();
        server.setVisible(true);
        server.setSize(800, 290);
        server.setLocation(width / 2 - 400, 5);
        server.setResizable(false);

        //start client 1
        StartScreen client1 = new StartScreen(1);
        client1.setSize(500, 400);
        client1.setLocation(width / 2 - 500 - 10, height / 2 - 80);
        client1.setVisible(true);
        client1.setResizable(false);

        //start client 2
        StartScreen client2 = new StartScreen(2);
        client2.setSize(500, 400);
        client2.setLocation(width / 2 + 10, height / 2 - 80);
        client2.setVisible(true);
        client2.setResizable(false);
    }

    public static void main(String[] args) {
        StartProject sp = new StartProject();
        sp.restart();
    }
}
