/*
 * Copyright (c) 2020.
 * Copyright by Tim and Felix
 */

package de.ft.interitus.deviceconnection.ev3connection;

import de.ft.interitus.utils.ArrayList;
import org.usb4java.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/***
 *          !Vermutung!
 *
 * Bei Lego gibt es Definition von String, Int,...
 *
 * Bei String ist es 0x84 am Anfang
 * Bei Int ist es 0x81 am Anfang
 *
 *
 */


public class ev3 {


    static final byte opCom_Set = (byte) 0xD4;

    static final byte opSound = (byte) 0x94;
    static final byte opSound_Ready = (byte) 0x96;

    static final byte opUI_Write = (byte) 0x82;
    static final byte opUI_draw = (byte) 0x84;
    static final byte opUI_Button = (byte) 0x83;

    static final byte opTimer_Wait = (byte) 0x85;
    static final byte opTimer_Ready = (byte) 0x86;

    static final byte opFile = (byte) 0xC0;

    static final byte opProgram_Start = (byte) 0x03;
    static final byte opProgram_Stop = (byte) 0x02;


    static final byte SET_BRICKNAME = (byte) 0x08;

    static final byte TONE = (byte) 0x01;
    static final byte PLAY = (byte) 0x02;
    static final byte REPEAT = (byte) 0x03;
    static final byte BREAK = (byte) 0x00;

    static final byte LED = (byte) 0x1B;
    static final byte GREEN = (byte) 0x01;
    static final byte RED = (byte) 0x02;
    static final byte ORANGE = (byte) 0x03;
    static final byte GREEN_FLASH = (byte) 0x04;
    static final byte RED_FLASH = (byte) 0x05;
    static final byte ORANGE_FLASH = (byte) 0x06;
    static final byte GREEN_FLASH_TWO_TIMES = (byte) 0x07;
    static final byte RED_FLASH_TWO_TIMES = (byte) 0x08;
    static final byte ORANGE_FLASH_TWO_TIMES = (byte) 0x09;

    static final byte SET_BACK_BLOCK = (byte) 0x0A;
    static final byte GET_BACK_BLOCK = (byte) 0x0B;
    static final byte SCREEN_BLOCK = (byte) 0x10;

    static final byte UPDATE = (byte) 0x00;
    static final byte TOPLINE = (byte) 0x12;
    static final byte FILLWINDOW = (byte) 0x13;
    static final byte BMPFile = (byte) 0x1C;
    static final byte LINE = (byte) 0x03;

    static final byte LOAD_IMAGE = (byte) 0x08;


    static final byte PRESS = (byte) 0x05;
    static final byte RELEASE = (byte) 0x06;

    static final byte NO_BUTTON = (byte) 0x00;
    static final byte UP_BUTTON = (byte) 0x01;
    static final byte ENTER_BUTTON = (byte) 0x02;
    static final byte DOWN_BUTTON = (byte) 0x03;
    static final byte RIGHT_BUTTON = (byte) 0x04;
    static final byte LEFT_BUTTON = (byte) 0x05;
    static final byte BACK_BUTTON = (byte) 0x06;
    static final byte ANY_BUTTON = (byte) 0x07;
    /**
     * EV3 Connection
     */

    static final short ID_VENDOR_LEGO = (short) 0x0694;
    static final short ID_PRODUCT_EV3 = (short) 0x0005;
    static final byte EP_IN = (byte) 0x81;
    static final byte EP_OUT = (byte) 0x01;
    static final byte DIRECT_COMMAND_REPLY = (byte) 0x00;
    static DeviceHandle handle;

    public static byte[] LCS(String string) {

        byte[] b = string.getBytes(StandardCharsets.UTF_8);
        byte[] a = new byte[string.getBytes(StandardCharsets.UTF_8).length + 2];

        a[0] = (byte) 0x84;
        for (int i = 0; i < string.getBytes(StandardCharsets.UTF_8).length; i++) {
            a[i + 1] = string.getBytes(StandardCharsets.UTF_8)[i];
        }

        a[string.getBytes(StandardCharsets.UTF_8).length + 1] = (byte) 0x00;

        return a;

    }

    public static byte[] LCX(int value) {


        byte count1 = (byte) 0x01;
        byte count2 = (byte) 0x81;
        byte count3 = (byte) 0x82;
        byte count4 = (byte) 0x83;
        byte count5 = (byte) 0x84;

        byte[] returnarray = null;
        Integer integer = value;

        if (value >= -32 && value <= 31) {

            returnarray = new byte[1];

            returnarray[0] = integer.byteValue();
            return returnarray;
        } else {


            if (value >= -127 && value <= 127) {
                returnarray = new byte[2];
                returnarray[0] = count2;
                returnarray[1] = integer.byteValue();
                return returnarray;

            } else {

                if (value >= -32767 && value <= 32767) {
                    returnarray = new byte[3];
                    returnarray[0] = (byte) 0x82;
                    returnarray[1] = (byte) value;
                    returnarray[2] = (byte) (value >> 8);
                } else {
                    returnarray = new byte[5];
                    returnarray[0] = (byte) 0x83;
                    returnarray[1] = (byte) value;
                    returnarray[2] = (byte) (value >> 8);
                    returnarray[3] = (byte) (value >> 16);
                    returnarray[4] = (byte) (value >> 24);
                }

            }

        }


        return returnarray;


    }

    public static byte[] LVX(int value) {
        List<Byte> ops = new ArrayList<>();
        if (value < 0) {
            throw new IllegalArgumentException("Parameter must be positive " + value);
        } else if (value < 32) {
            ops.add((byte) (0x40 | value));
        } else if (value < 256) {
            ops.add((byte) 0xc1);
            ops.add((byte) value);
        } else if (value < 65536) {
            ops.add((byte) 0xc2);
            ops.add((byte) value);
            ops.add((byte) (value >> 8));
        } else {
            ops.add((byte) 0xc3);
            ops.add((byte) value);
            ops.add((byte) (value >> 8));
            ops.add((byte) (value >> 16));
            ops.add((byte) (value >> 24));
        }
        byte[] returnarray = new byte[ops.size()];
        for (int i = 0; i < ops.size(); i++) {
            returnarray[i] = ops.get(i);
        }

        return returnarray;

    }

    // Global Variable 1,2,4 bytes follow
    public static byte[] GVX(int value) {
        List<Byte> ops = new ArrayList<>();
        if (value < 0) {
            throw new IllegalArgumentException("Parameter must be positive " + value);
        } else if (value < 32) {
            ops.add((byte) (0x60 | value));
        } else if (value < 256) {
            ops.add((byte) 0xe1);
            ops.add((byte) value);
        } else if (value < 65536) {
            ops.add((byte) 0xe2);
            ops.add((byte) value);
            ops.add((byte) (value >> 8));
        } else {
            ops.add((byte) 0xe3);
            ops.add((byte) value);
            ops.add((byte) (value >> 8));
            ops.add((byte) (value >> 16));
            ops.add((byte) (value >> 24));
        }
        byte[] returnarray = new byte[ops.size()];
        for (int i = 0; i < ops.size(); i++) {
            returnarray[i] = ops.get(i);
        }

        return returnarray;

    }

    private static void connectUsb() {
        int result = LibUsb.init(null);
        Device device = null;
        DeviceList list = new DeviceList();
        result = LibUsb.getDeviceList(null, list);


        if (result < 0) {
            throw new RuntimeException("Unable to get device list. Result=" + result);
        }
        boolean found = false;
        for (Device dev : list) {
            DeviceDescriptor descriptor = new DeviceDescriptor();
            result = LibUsb.getDeviceDescriptor(dev, descriptor);
            if (result != LibUsb.SUCCESS) {
                throw new LibUsbException("Unable to read device descriptor", result);
            }
            if (descriptor.idVendor() == ID_VENDOR_LEGO
                    || descriptor.idProduct() == ID_PRODUCT_EV3) {
                device = dev;
                found = true;
                break;
            }
        }
        LibUsb.freeDeviceList(list, false);
        if (!found) throw new RuntimeException("Lego EV3 device not found.");

        handle = new DeviceHandle();
        result = LibUsb.open(device, handle);
        if (result != LibUsb.SUCCESS) {
            System.out.println(device.getPointer());
            throw new LibUsbException("Unable to open USB device", result);
        }
        boolean detach = LibUsb.kernelDriverActive(handle, 0) != 0;

        if (detach) result = LibUsb.detachKernelDriver(handle, 0);
        if (result != LibUsb.SUCCESS) {
            throw new LibUsbException("Unable to detach kernel driver", result);
        }

        result = LibUsb.claimInterface(handle, 0);
        if (result != LibUsb.SUCCESS) {
            throw new LibUsbException("Unable to claim interface", result);
        }
    }

    private static ByteBuffer sendSystemCmd(ArrayList<Byte> operations) { //TODO inarbeit
        ByteBuffer buffer = ByteBuffer.allocateDirect(operations.size() + 7);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort((short) 0x01);   // length
        buffer.putShort((short) 0xA0);                            // counter
        buffer.put(DIRECT_COMMAND_REPLY);                       // legth of creare


        for (int i = 0; i < operations.size(); i++) {         // operations
            buffer.put(operations.get(i));
        }


        IntBuffer transferred = IntBuffer.allocate(1);
        int result = LibUsb.bulkTransfer(handle, EP_OUT, buffer, transferred, 100);
        if (result != LibUsb.SUCCESS) {
            throw new LibUsbException("Unable to write data", transferred.get(0));
        }
        printHex("Sent", buffer);

        buffer = ByteBuffer.allocateDirect(1024);
        transferred = IntBuffer.allocate(1);
        result = LibUsb.bulkTransfer(handle, EP_IN, buffer, transferred, 100);

        if (result != LibUsb.SUCCESS) {
            System.out.println("Restart EV3!");
        }

        printHex("Recv", buffer);
        return buffer;
    }

    private static ByteBuffer sendDirectCmd(ArrayList<Byte> operations,
                                            int local_mem, int global_mem) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(operations.size() + 7);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort((short) (operations.size() + 5));   // length
        buffer.putShort((short) 42);                            // counter
        buffer.put(DIRECT_COMMAND_REPLY);                       // type
        buffer.putShort((short) (local_mem * 1024 + global_mem)); // header

        for (int i = 0; i < operations.size(); i++) {         // operations
            buffer.put(operations.get(i));
        }


        IntBuffer transferred = IntBuffer.allocate(1);
        int result = LibUsb.bulkTransfer(handle, EP_OUT, buffer, transferred, 100);
        if (result != LibUsb.SUCCESS) {
            throw new LibUsbException("Unable to write data", transferred.get(0));
        }
        printHex("Sent", buffer);

        buffer = ByteBuffer.allocateDirect(1024);
        transferred = IntBuffer.allocate(1);
        result = LibUsb.bulkTransfer(handle, EP_IN, buffer, transferred, 100);
        if (result != LibUsb.SUCCESS) {
            throw new LibUsbException("Unable to read data", result);
        }
        buffer.position(global_mem + 5);
        printHex("Recv", buffer);
        return buffer;
    }

    private static void printHex(String desc, ByteBuffer buffer) {
        System.out.print(desc + " 0x|");
        for (int i = 0; i < buffer.position() - 1; i++) {
            System.out.printf("%02X:", buffer.get(i));
        }
        System.out.printf("%02X|", buffer.get(buffer.position() - 1));
        System.out.println();
    }


    public static void openev3sesseion() {
        connectUsb();
    }

    public static void closeev3session() {
        LibUsb.releaseInterface(handle, 0);
        LibUsb.close(handle);
    }


    public static void sendcommand(ArrayList<Byte> command, int local_mem, int global_mem) {
        ByteBuffer reply = sendDirectCmd(command, local_mem, global_mem);
    }

    public static void sendsystemcommand(ArrayList<Byte> command) {
        ByteBuffer reply = sendSystemCmd(command);
    }


}
