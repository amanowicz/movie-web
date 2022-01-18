package com.amanowicz.movieweb.service;

public interface UserService {

    void register(String username, String password);

    void login(String username, String password);
}
