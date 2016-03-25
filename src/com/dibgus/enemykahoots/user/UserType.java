package com.dibgus.enemykahoots.user;

/**
 * Created by Ivan on 3/17/2016.
 */
public enum UserType {
    MIMIC("m"), RANDOM("r"), DEAD("d");
    public String type;
    UserType(String type)
    {
        this.type = type;
    }
}
