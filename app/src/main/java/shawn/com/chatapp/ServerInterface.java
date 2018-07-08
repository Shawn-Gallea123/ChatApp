package shawn.com.chatapp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

// Variables for interfacing with server
public class ServerInterface {
    public static String serverIP = "70.27.120.214"; // Server IP address
    public static boolean firstTime = true; // First time connecting flag
    public static BufferedReader fromServer = null; // Input from server
    public static int port = 60000; // Server listening port
    public static Socket sock = null; // Socket
    public static PrintWriter send = null; // Output to server
    public static String result = ""; // Temporary results from received messages
    public static boolean failedConnection = false; // True if connection failed

    // Send message
    public static class MessageSender extends AsyncTask<String, Integer, Long> {
        protected Long doInBackground(String... params) {
            ServerInterface.send.println(params[0]);
            return new Long(5);
        }
    }


}
