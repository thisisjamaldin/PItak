package com.nextinnovation.pitak.model.user;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class UserWhenSignedIn {
    private long id;
    private String username;
    private String surname;
    private String patronymic;
    private String name;
    private String email;
    private String[] roles;
    private String tokenType;
    private String accessToken;
    private String newPassword;

    public UserWhenSignedIn(long id, String username, String surname, String patronymic, String name, String email, String[] roles, String tokenType, String accessToken, String newPassword) {
        this.id = id;
        this.username = username;
        this.surname = surname;
        this.patronymic = patronymic;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.newPassword = newPassword;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "UserWhenSignedIn{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + Arrays.toString(roles) +
                ", tokenType='" + tokenType + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
