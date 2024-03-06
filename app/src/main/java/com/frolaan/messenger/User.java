package com.frolaan.messenger;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private int age;
    private boolean online;

    public User(String id, String firstName, String lastName, int age, boolean online) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.online = online;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public boolean isOnline() {
        return online;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", isOnline=" + online +
                '}';
    }
}
