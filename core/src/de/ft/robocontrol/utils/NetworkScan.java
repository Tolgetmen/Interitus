package de.ft.robocontrol.utils;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class NetworkScan {
   public static ArrayList<InetAddress> device = new ArrayList<>();
   public static String piaddress = "";
    public static void get() {


        try {

            try(final DatagramSocket socket = new DatagramSocket()){
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                String ip = socket.getLocalAddress().getHostAddress();




               ip = ip.split("\\.")[2];

               try {
                 InetAddress raspberrypi =  InetAddress.getByName("raspberrypi");
                   System.out.println("Found Raspberry at "+raspberrypi.getHostAddress());
                   piaddress =raspberrypi.getHostAddress();

               }catch (UnknownHostException e) {
               }
                  for(int i = 0;i<255;i++) {
                     InetAddress testdevice =InetAddress.getByName("192.168."+ip+"."+i);

                     if(testdevice.isReachable(40)) { //TODO bester wert ermittlen
                         device.add(testdevice);
                     }

                  }


                  for(int i =0;i<device.size();i++) {

                      System.out.println(device.get(i).getHostName());
                  }





            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void scan()  {

    }
}