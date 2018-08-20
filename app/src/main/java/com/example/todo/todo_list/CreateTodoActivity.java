package com.example.todo.todo_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateTodoActivity extends AppCompatActivity {


    private EditText etTodo;
    private MultiSelectionSpinner spinner;
    private FirebaseUser user;
    private ArrayList<User> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);


        user = FirebaseAuth.getInstance().getCurrentUser();
        spinner = findViewById(R.id.spinner_users);

        etTodo = findViewById(R.id.et_todo);
        Button btnCreate = findViewById(R.id.btn_todo_done);

        getAllUsers();


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String todo = etTodo.getText().toString();
                if (todo.isEmpty() || spinner.getSelectedIndicies().isEmpty()) {
                    Toast.makeText(CreateTodoActivity.this, "Something is not selected",
                            Toast.LENGTH_SHORT).show();
                } else {
                    final HashMap<String, String> map = new HashMap<>();
                    for (int i = 0; i < spinner.getSelectedStrings().size(); i++) {
                        map.put(String.valueOf(i), spinner.getSelectedStrings().get(i));
                    }

                    for (Integer position : spinner.getSelectedIndicies()) {
                            DatabaseReference drUsers = FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("tasks")
                                    .child(usersList.get(position).getId()).push();
                            HashMap<String, String> assignby1 = new HashMap<>();
                            //  assignby.put(user.getUid(), user.getDisplayName());
                            assignby1.put(user.getUid(), user.getDisplayName());
                            drUsers.child("assignby").setValue(assignby1);

                            HashMap<String, String> assignTo1 = new HashMap<>();
                            for (Integer position2 : spinner.getSelectedIndicies()) {
                                assignTo1.put(usersList.get(position2).getId(),
                                        usersList.get(position2).getCustomerName());
                            }
                            drUsers.child("assignto").setValue(assignTo1);
                            drUsers.child("todo").setValue(todo);
                        }
                    }
                    finish();

            }
        });


    }

    private void getAllUsers() {
        usersList = new ArrayList<>();
        final DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("TAG", dataSnapshot.getValue().toString());
                usersList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String userId = childSnapshot.getKey().toString();
                    String userName = childSnapshot.getValue().toString();

                    usersList.add(new User(userId, userName));
                    spinner.setItems(usersList);
                }

                //  customAdapter.addUsers(usersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", databaseError.toString());
            }


        });


    }
}
