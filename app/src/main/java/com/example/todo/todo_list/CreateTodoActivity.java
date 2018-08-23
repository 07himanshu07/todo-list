package com.example.todo.todo_list;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CreateTodoActivity extends AppCompatActivity {


    private EditText etTodo;
    private MultiSelectionSpinner spinner;
    private FirebaseUser user;
    private ArrayList<User> usersList;
    private TextView tvEndDate;
    private EditText etAddNote;
    private RadioGroup radioGroupPriority;
    private String priority = "";
    private DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);


        user = FirebaseAuth.getInstance().getCurrentUser();
        spinner = findViewById(R.id.spinner_users);

        tvEndDate = findViewById(R.id.tv_todo_end_date);
        etAddNote = findViewById(R.id.et_add_note);
        etTodo = findViewById(R.id.et_todo);
        Button btnCreate = findViewById(R.id.btn_todo_done);
        radioGroupPriority = findViewById(R.id.rgPriority);
        radioGroupPriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radButtonHigh:
                        priority = "High";
                        break;
                    case R.id.radButtonMedium:
                        priority = "Medium";
                        break;
                    case R.id.radButtonLow:
                        priority = "Low";
                        break;
                }

            }
        });


        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
               int  mYear = c.get(Calendar.YEAR); // current year
               int  mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(CreateTodoActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        tvEndDate.setText(String.format(Locale.getDefault(),
                                "%d/%d/%d", dayOfMonth, monthOfYear + 1, year));

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        getAllUsers();
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                final String todo = etTodo.getText().toString();
                final String note = etAddNote.getText().toString();
                final String endDate = tvEndDate.getText().toString();
                if (todo.isEmpty() || spinner.getSelectedIndicies().isEmpty() ||
                        priority.isEmpty() || note.isEmpty() || endDate.isEmpty()) {
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
                        drUsers.child("note").setValue(note);
                        drUsers.child("startDate").setValue(date);
                        drUsers.child("endDate").setValue(endDate);
                        drUsers.child("priority").setValue(priority);

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

    public void onPrioritySelected(View view) {
    }
}
