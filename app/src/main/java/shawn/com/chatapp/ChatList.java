package shawn.com.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

// Activity for displaying active conversations
public class ChatList extends AppCompatActivity implements ConversationsFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void itemClicked(String otherUser) {
        // Start Chat activity, passing user name
        Intent intent = new Intent(this, Chat.class);
        intent.putExtra("OtherUser", otherUser);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
