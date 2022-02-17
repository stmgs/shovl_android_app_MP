package com.example.shovl_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //if (Patterns.EMAIL_ADDRESS.matcher(username).matches() )
    }

    private fun validatePassword(password: String): Boolean {
        val PASSWORD_PATTERN =
            Pattern.compile(
                "^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        "(?=.*[a-z])" +         //at least 1 lower case letter
                        "(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=.*[a-zA-Z])" +      //any letter
                        "(?=.*[@#$%^&+=])" +    //at least 1 special character
                        "(?=\\S+$)" +           //no white spaces
                        ".{8,}" +               //minimum 8 characters
                        "$"

            )

        // if password field is empty
        // it will display error message "Password can not be empty"
        return PASSWORD_PATTERN.matcher(password).matches()
    }

}