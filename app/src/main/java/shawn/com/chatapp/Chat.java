package shawn.com.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

// Chat interface activity
public class Chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("OtherUser"));
    }

    // Send message to other user
    public void sendMessage(View view) {
        String otherUserName = getIntent().getStringExtra("OtherUser");
        String userName = getIntent().getStringExtra("UserName");
        EditText editMessage = findViewById(R.id.messageEntry);
        String message = editMessage.getText().toString();
        new ServerInterface.MessageSender().execute("MSG/" + userName + "/" + otherUserName + "/" + message);
    }
}
