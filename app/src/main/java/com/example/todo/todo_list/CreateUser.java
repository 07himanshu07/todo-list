package com.example.todo.todo_list;

public class CreateUser {
    private String email;
    private String password;
    private String displayName;
    private String phoneNo;

    public CreateUser(String email, String password, String displayName, String phoneNo) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.phoneNo = phoneNo;
    }

    public CreateUser() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
