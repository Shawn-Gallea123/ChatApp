package shawn.com.chatapp;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerInterface {
    public static String serverIP = "192.168.1.192";
    public static boolean firstTime = true;
    public static BufferedReader fromServer = null;
    public static int port;
    public static Socket sock;
    public static PrintWriter send;
    public static String result = "";



}
