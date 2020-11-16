package com.movie4us;

public class Message {
    private String username;
    private String connectedUser;
    private String action;
    private String selectedCategory;

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Message{" +
                "username='" + username + '\'' +
                ", connectedUser='" + connectedUser + '\'' +
                ", action='" + action + '\'' +
                '}';
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(String connectedUser) {
        this.connectedUser = connectedUser;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
