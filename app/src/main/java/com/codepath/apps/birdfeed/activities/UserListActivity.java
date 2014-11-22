package com.codepath.apps.birdfeed.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.adapters.UserListAdapter;
import com.codepath.apps.birdfeed.models.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class UserListActivity extends Activity {

    public static final String TITLE = "title";
    public static final String USERS_JSON = "users";

    private GridView gvUsers;
    private List<User> users;
    private UserListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        if (getIntent().getExtras() != null) {
            Bundle args = getIntent().getExtras();
            setTitle(args.getString(TITLE));
            try {
                JSONArray usersArray = new JSONArray(args.getString(USERS_JSON));
                users = User.fromArray(usersArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter = new UserListAdapter(this, users);
        }
        setGrid();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setGrid() {
        gvUsers = (GridView) findViewById(R.id.gvUsers);
        gvUsers.setAdapter(mAdapter);
    }
}
