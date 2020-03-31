package de.ft.robocontrol.roboconnection;
import com.fazecast.jSerialComm.*;

import java.sql.Time;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SerialConnection {

    public static int empfangen(SerialPort sport){
        Scanner data = new Scanner(sport.getInputStream());
        System.out.println(data.hasNextLine());
        if (data.hasNextLine()) {
            int number = 0;
            try {
                number = Integer.parseInt(data.nextLine());
            } catch (Exception e) {
            }
            System.out.println("nubmer    "+number);
            return number;
        }else{
            return -1;
        }


    }


public static void searchArduino() {
    // determine which serial port to use

    SerialPort ports[] = SerialPort.getCommPorts();
    System.out.println("Select a port:");
    int i = 1;
    for (SerialPort port : ports) {
        System.out.println(i++ + ". " + port.getSystemPortName());
        System.out.println(port.getDescriptivePortName()+"   deks");
        System.out.println("i     "+i);
try {
    SerialPort testport = ports[i-2];

    if (testport.openPort()) {
        System.out.println("Successfully opened the port.");
        System.out.println("baudrate:   " + testport.getDescriptivePortName());
    } else {
        System.out.println("Unable to open the port.");
        return;
    }

    testport.setBaudRate(9600);
    testport.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 10, 0);

long save=System.currentTimeMillis()+2000;
boolean found=false;
while(System.currentTimeMillis()<save && found==false){
    if( empfangen(testport)==1234){
        System.out.println("Arduino gefunden");
        found=true;
    }
}





}catch (ArrayIndexOutOfBoundsException e){System.out.println("mist");}

        System.out.println("-----------------------------------------------------------");

    }

}

}
