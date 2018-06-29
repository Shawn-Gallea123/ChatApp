package shawn.com.chatapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;

public class PortEnter extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port_enter);

        EditText edit = findViewById(R.id.textField);
        edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    setSend(null);
                    return true;
                }

                return false;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    // Continuously check for messages from the server
    private void runMessageChecker() {
        final TextView status = findViewById(R.id.statusText);
        final Handler handler = new Handler();

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
        startActivity(intent);
    }

    // Set up the connection
    private class SetupConnection extends AsyncTask<String, Integer, Long> {

        protected Long doInBackground(String... params) {

            if (params[0] == "First") {
                try {
                    EditText edit = findViewById(R.id.textField);
                    ServerInterface.port = Integer.parseInt(edit.getText().toString());
                    ServerInterface.sock = new Socket(ServerInterface.serverIP, ServerInterface.port);
                    ServerInterface.send = new PrintWriter(ServerInterface.sock.getOutputStream(), true);
                    ServerInterface.fromServer = new BufferedReader(new InputStreamReader(ServerInterface.sock.getInputStream()));


                } catch (IOException e) {
                    System.out.println("IOEX");
                    return new Long(5);
                }
            } else {
                if (ServerInterface.send != null) {
                    ServerInterface.send.println(params[0]);
                }
            }

            return new Long(5);
        }
    }

    // Send message
    private class MessageSender extends AsyncTask<String, Integer, Long> {
        protected Long doInBackground(String... params) {
            ServerInterface.send.println(params[0]);
            return new Long(5);
        }
    }

    // Receive message
    private class MessageReceiver extends AsyncTask<String, Integer, Long> {
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

        switch(view.getId()) {
            case R.id.signUp:
                code = "SU";
                break;
            case R.id.logIn:
                code = "LI";
                break;
        }

        new MessageSender().execute(code + "/" + userName + "/" + passWord);
    }

    // Set the port number and set up the connection
    public void setSend(View view) {

        EditText edit = findViewById(R.id.textField);

        if (ServerInterface.firstTime) {
            new SetupConnection().execute("First");
            ServerInterface.firstTime = false;
        } else {

            String message = edit.getText().toString();
            new MessageSender().execute(message);
        }

        edit.setText("");
        runMessageChecker();

    }
}
