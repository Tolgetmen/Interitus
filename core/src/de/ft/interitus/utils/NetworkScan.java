/*
 * Copyright (c) 2020.
 * Copyright by Tim and Felix
 */

package de.ft.interitus.utils;

import de.ft.interitus.DisplayErrors;
import de.ft.interitus.Programm;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkScan {
    public static ArrayList<InetAddress> device = new ArrayList<>();

    public static String piaddress = "";

    public static void get() {

        try {

            try (final DatagramSocket socket = new DatagramSocket()) {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002); //TODO port kann belegt sein
                String ip = socket.getLocalAddress().getHostAddress();


                ip = ip.split("\\.")[2];

                try {
                    InetAddress raspberrypi = InetAddress.getByName("raspberrypi");
                    if (raspberrypi.isReachable(300)) {
                        Programm.logger.info("Found Raspberry at " + raspberrypi.getHostAddress());
                        piaddress = raspberrypi.getHostAddress();
                    }

                } catch (UnknownHostException e) {
                }
                for (int i = 0; i < 255; i++) {
                    InetAddress testdevice = InetAddress.getByName("192.168." + ip + "." + i); //TODO doesn't work on MAC OS

                    if (testdevice.isReachable(60)) {
                        device.add(testdevice);
                    }

                }


                for (int i = 0; i < device.size(); i++) {


                    Programm.logger.config(device.get(i).getHostName());

                }


            }
        } catch (IOException e) {
            DisplayErrors.error = e;
            e.printStackTrace();
        }
    }

    public static void scan() {

    }
}
