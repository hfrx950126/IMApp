package com.example.administrator.myimapp;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class UserService implements IUserService {
    @Override
    public String search(int hashCode){
        return "User:" + hashCode;
    }
}
