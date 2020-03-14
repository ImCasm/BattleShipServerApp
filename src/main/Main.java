/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import battleship.BattleShipListener;

/**
 *
 * @author Cristian
 */
public class Main {

    public static void main(String[] args) {

        BattleShipListener socketListener = null;
        int port = -1;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Error: invalid port");
            }
            if (port != -1) {
                socketListener = new BattleShipListener(port);
                System.out.println("Servidor Batalla Naval (version 1.0.0)");
                System.out.println("Escuchando en puerto: " + port);
                socketListener.run();
                System.out.println("Servidor Batalla Naval Finalizado");
            }
        } else {
            System.out.println("Error: parametros invalidos, ingrese el puerto");
            System.out.println("Ejemplo: java -jar BattleShipServer.jar 25500");
        }
    }

}
