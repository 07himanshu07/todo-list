package com.example.todo.todo_list;

import java.util.ArrayList;

public class Task {
    private ArrayList<User> assignto;
    private ArrayList<User> assignby;
    private String todo;
    private String note;
    private String priority;
    private String startDate;
    private String endDate;


     Task(ArrayList<User> assignto, ArrayList<User> assignby, String todo,
                String note, String priority, String startDate, String endDate) {
        this.assignto = assignto;
        this.assignby = assignby;
        this.todo = todo;
        this.note = note;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getNote() {
        return note;
    }

    public String getPriority() {
        return priority;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
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
