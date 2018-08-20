package com.example.todo.todo_list;

public class User {

    private String id;
    private String userName;


    User(String id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return userName;
    }

    public void setCustomerName(String userName) {
        this.userName = userName;
    }


    @Override
    public String toString() {
        return userName;
    }
}
