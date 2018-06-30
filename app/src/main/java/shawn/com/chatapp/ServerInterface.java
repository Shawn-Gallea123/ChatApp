package shawn.com.chatapp;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

// Variables for interfacing with server
public class ServerInterface {
    public static String serverIP = "192.168.1.192"; // Server IP address
    public static boolean firstTime = true; // First time connecting flag
    public static BufferedReader fromServer = null; // Input from server
    public static int port = 60000; // Server listening port
    public static Socket sock = null; // Socket
    public static PrintWriter send = null; // Output to server
    public static String result = ""; // Temporary results from received messages
    public static boolean failedConnection = false; // True if connection failed
}
