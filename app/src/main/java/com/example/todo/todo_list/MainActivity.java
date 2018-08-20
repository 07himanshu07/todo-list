package com.example.todo.todo_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CustomAdapter customAdapter;
    private FirebaseUser user;
    DatabaseReference tasksRef;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.todo_list);
        customAdapter = new CustomAdapter(this);

        user = FirebaseAuth.getInstance().getCurrentUser();

        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(customAdapter);

        fetchListFromFirebase();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchListFromFirebase();
    }

    private void fetchListFromFirebase() {

        final ArrayList<Task> taskList = new ArrayList<>();

        tasksRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("tasks")
                .child(user.getUid());
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null)
                    Log.i("TAG", dataSnapshot.getValue().toString());
                for (DataSnapshot index : dataSnapshot.getChildren()) {
                    String key = index.getKey();
                    DataSnapshot dataSnapshotKey = index.child(key);

                    ArrayList<User> assignbyList = new ArrayList<>();
                    ArrayList<User> assigntoList = new ArrayList<>();

                    String todoString = null;


                    for (DataSnapshot data : index.getChildren()) {

                        if (data.getKey().equalsIgnoreCase("assignby")) {
                            for (DataSnapshot value : data.getChildren()) {
                                String assignbyString1 = value.getKey();
                                String assignbyString2 = value.getValue().toString();
                                assignbyList.add(new User(assignbyString1, assignbyString2));

                            }

                        } else if (data.getKey().equalsIgnoreCase("assignto")) {
                            for (DataSnapshot datasnapshotTo : data.getChildren()) {
                                String assigntoString1 = datasnapshotTo.getKey();
                                String assigntoString2 = datasnapshotTo.getValue().toString();

                                assigntoList.add(new User(assigntoString1, assigntoString2));

                            }
                        } else if (data.getKey().equalsIgnoreCase("todo")) {
                             todoString = data.getValue().toString();
                        }

                    }
                    taskList.add(new Task(assigntoList, assignbyList, todoString));


//                    DataSnapshot assignby = data.child("assignby");
//                    DataSnapshot assignto = data.child("assignto");
//                    String todoString = data.child("todo").getValue().toString();




                }
                customAdapter.addTasks(taskList);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", databaseError.toString());
            }

        };
        if (tasksRef != null)
            tasksRef.addValueEventListener(valueEventListener);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_todo:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(MainActivity.this, CreateTodoActivity.class);
                startActivity(intent);
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        tasksRef.removeEventListener(valueEventListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}
