package com.example.tripsnap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripsnap.Models.PasswordModel;
import com.example.tripsnap.Models.User;
import com.example.tripsnap.RetrofitApiInterface.RetrofitAPI;
import com.example.tripsnap.RetrofitApiInterface.BaseUrl;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginActivity extends AppCompatActivity {


    EditText userId, userPassword;
    String stUserId, stUserPassword, originalPassword;
    Long lnUserId;
    Dialog dialog;
    TextView btnSignupAct;
    Button submit;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        init();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringConverter();
                if (stUserId.isEmpty() || stUserPassword.isEmpty()) {
                    Toast.makeText(UserLoginActivity.this, "Please fill all data", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(UserLoginActivity.this, stUserId, Toast.LENGTH_SHORT).show()
                    dialog = new Dialog(UserLoginActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.progressbar);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    getPassByApi();
                }



            }
        });
        btnSignupAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(UserLoginActivity.this,UserSignUpActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    private void stringConverter() {
        stUserId = userId.getText().toString();

        stUserPassword = userPassword.getText().toString();
    }

    private void init() {
        userId = findViewById(R.id.etMobileNo);
        userPassword = findViewById(R.id.etPassword);
        submit = findViewById(R.id.btnLogin);
        btnSignupAct=findViewById(R.id.btnSignupAct);
    }

    private void getPassByApi() {


        RetrofitAPI retrofitAPI = BaseUrl.retrofit();
        lnUserId=Long.parseLong(stUserId);
        Call<Map<String,String>> call = retrofitAPI.getPassword(lnUserId);
//        Toast.makeText(this, ""+lnUserId.getClass()+lnUserId, Toast.LENGTH_SHORT).show();


       call.enqueue(new Callback<Map<String, String>>() {
           @Override
           public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
               if (response.code() == 200) {
                   Map<String, String> map = response.body();
                   originalPassword = map.get("password");
                   if (originalPassword != null) {
                       if (originalPassword.equals(stUserPassword)) {
                           Intent i = new Intent(UserLoginActivity.this, BusBookingActivity.class);
                           i.putExtra("User Id",lnUserId);
                           dialog.dismiss();
                           startActivity(i);
                           finish();
                       } else {
                           dialog.dismiss();
                           Toast.makeText(UserLoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                       }
                   } else {
                       dialog.dismiss();
                       Toast.makeText(UserLoginActivity.this, "Something Went wrong!.", Toast.LENGTH_SHORT).show();
                   }
               }else{
                   dialog.dismiss();
                   Toast.makeText(UserLoginActivity.this, "User Id not exists. Please, Create an account.", Toast.LENGTH_SHORT).show();
                   Intent i = new Intent(UserLoginActivity.this, UserSignUpActivity.class);
                   startActivity(i);
                   finish();
               }
           }

           @Override
           public void onFailure(Call<Map<String, String>> call, Throwable t) {
               dialog.dismiss();
               Toast.makeText(UserLoginActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });

    }
    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}