package shawn.com.chatapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// ListFragment for displaying active conversations
public class ConversationsFragment extends ListFragment {

    // Interface for containing activity
    interface Listener {
        void itemClicked(String s);
    }

    // Listener memb-var
    private Listener listener;

    public ConversationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Remove "CL" token from tokens, place in convos
        String[] tokens = ServerInterface.result.split("/");
        String[] convos = new String[tokens.length - 1];

        for (int i = 1; i < tokens.length; ++i) {
            convos[i-1] = tokens[i];
        }

        // Create and set ArrayAdapter for ListFragment
        ArrayAdapter<String> adapter = new ArrayAdapter<>(inflater.getContext(),
                android.R.layout.simple_list_item_1, convos);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (Listener) context;
    }

    @Override // Send user name chosen to next activity
    public void onListItemClick(ListView listView, View itemView, int position, long id) {
        if (listener != null) {
            listener.itemClicked(listView.getItemAtPosition(position).toString());
        }
    }

}
