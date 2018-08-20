package com.example.todo.todo_list;

import java.util.ArrayList;

public class Task {
    private ArrayList<User> assignto;
    private ArrayList<User> assignby;
    private String todo;


     Task(ArrayList<User> assignto, ArrayList<User> assignby, String todo) {
        this.assignto = assignto;
        this.assignby = assignby;
        this.todo = todo;
    }

    public ArrayList<User> getAssignto() {
        return assignto;
    }

    public ArrayList<User> getAssignby() {
        return assignby;
    }

    public String getTodo() {
        return todo;
    }
}
