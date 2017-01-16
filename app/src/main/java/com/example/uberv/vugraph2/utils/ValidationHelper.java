package com.example.uberv.vugraph2.utils;

import android.util.Patterns;

public abstract class ValidationHelper {

    public static final int MIN_PWD_LENGTH=6;
    public static final int MAX_PWD_LENGTH=20;
    // FIXME no magic numbers
    private static final String USERNAME_PATTERN = "^[A-Za-z0-9_-]{6,20}$";
    private static final String FULLNAME_PATTERN = "^[\\p{L} .'-]+$";
    private static final String PASSWORD_PATTERN =  "^(?=\\S+$)[@#$%^&+=a-zA-Z0-9]{"+MIN_PWD_LENGTH+","+MAX_PWD_LENGTH+"}+$";

    public static final boolean isUsernameValid(String username){
        if(isEmptyOrNull(username)){
            return false;
        }else{
            return username.matches(USERNAME_PATTERN);
        }
    }

    public static boolean isEmailValid(String email){
        if(isEmptyOrNull(email)){
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password){
        if(isEmptyOrNull(password)){
            return false;
        }else{
            return password.matches(PASSWORD_PATTERN);
        }
    }

    public static boolean isFullnameValid(String fullname){
        if(isEmptyOrNull(fullname)){
            return false;
        }else{
            return fullname.matches(FULLNAME_PATTERN);
        }
    }

    public static boolean isEmptyOrNull(String string){
        return string!=null && !string.isEmpty() && string.equals("");
    }

}
