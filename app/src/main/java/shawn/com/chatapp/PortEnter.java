package shawn.com.chatapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.io.PrintWriter;
import java.net.UnknownHostException;

public class PortEnter extends AppCompatActivity {

    private void displayNotification(String noti) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "NotiChannel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Test")
                .setContentText("Testing notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(12, mBuilder.build());


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "Send Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("NotiChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port_enter);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // createNotificationChannel();

    }

    // Continuously check for messages from the server
    private void runMessageChecker() {
        final Handler handler = new Handler();

        // Loop
        handler.post(new Runnable() {
            @Override
            public void run() {
                new MessageReceiver().execute("");

                if (ServerInterface.result.contains("CL")) {
                    goToConversations();
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        });
    }

    // Go to ChatList activity
    private void goToConversations() {
        Intent intent = new Intent(this, ChatList.class);
        EditText userEdit = findViewById(R.id.userEdit);
        String userName = userEdit.getText().toString();
        intent.putExtra("UserName", userName);
        startActivity(intent);
    }

    // Set up the connection
    private static class SetupConnection extends AsyncTask<String, Integer, Long> {

        protected Long doInBackground(String... params) {

                try {
                   // EditText edit = findViewById(R.id.textField);
                    ServerInterface.sock = new Socket(InetAddress.getByName(ServerInterface.serverIP), ServerInterface.port);
                    ServerInterface.send = new PrintWriter(ServerInterface.sock.getOutputStream(), true);
                    ServerInterface.fromServer = new BufferedReader(new InputStreamReader(ServerInterface.sock.getInputStream()));

                } catch (IOException e) {
                    System.out.println("ZEBRA: IOEX");
                    ServerInterface.send = null;
                    ServerInterface.sock = null;
                    ServerInterface.fromServer = null;
                    ServerInterface.failedConnection = true;
                    return new Long(5);
                }


            return Long.valueOf(5);
        }
    }

    // Receive message
    private static class MessageReceiver extends AsyncTask<String, Integer, Long> {
        protected Long doInBackground(String... params) {
            try {
                if (ServerInterface.fromServer.ready()) {
                    ServerInterface.result = ServerInterface.fromServer.readLine();
                }
            } catch (IOException e) {

            }

            return new Long(5);
        }
    }

    // SignUp/LogIn
    public void signUpLogIn(View view) {
        EditText userEdit = findViewById(R.id.userEdit);
        EditText passEdit = findViewById(R.id.passEdit);
        String userName = userEdit.getText().toString();
        String passWord = passEdit.getText().toString();
        String code = null;

        // If first time ran, setup connection
        if (ServerInterface.firstTime) {

            new SetupConnection().execute("");

            while (ServerInterface.send == null) {
                // Loop until set up

                if (ServerInterface.failedConnection) {
                    // Notify user of no sign in
                    Context context = getApplicationContext();
                    CharSequence text = "No connection";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    ServerInterface.failedConnection = false;

                    return;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // Nothing
                }
            }
        }

        ServerInterface.firstTime = false;

        // Begin listening for messages
        runMessageChecker();

        // Prepare message
        switch(view.getId()) {
            case R.id.signUp:
                code = "SU";
                break;
            case R.id.logIn:
                code = "LI";
                break;
        }

        // Send message
        new ServerInterface.MessageSender().execute(code + "/" + userName + "/" + passWord);
    }
}
