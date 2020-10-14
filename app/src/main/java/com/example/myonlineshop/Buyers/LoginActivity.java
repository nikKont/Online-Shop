package com.example.myonlineshop.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myonlineshop.Admin.AdminHomeActivity;
import com.example.myonlineshop.Sellers.SellerProductCategoryActivity;
import com.example.myonlineshop.Model.Users;
import com.example.myonlineshop.Prevalent.Prevalent;
import com.example.myonlineshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputPhoneNumber,InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink,NotAdminLink,ForgetPasswordLink;

    //shared prefs
//    private String phoneNumber,password1;
//    private SharedPreferences loginPreferences;
//    private SharedPreferences.Editor loginPrefsEditor;
//    private boolean saveLogin;

    private String parentDbName="Users";

    //checkbox
    private CheckBox checkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPhoneNumber=(EditText)findViewById(R.id.login_phone);
        InputPassword=(EditText)findViewById(R.id.login_password);
        AdminLink=(TextView)findViewById(R.id.admin_panel);
        NotAdminLink=(TextView)findViewById(R.id.not_admin_panel);
        loadingBar= new ProgressDialog(this);
        ForgetPasswordLink=findViewById(R.id.forget_password);

        //shared prefs
//        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
//        loginPrefsEditor = loginPreferences.edit();
//
//        saveLogin=loginPreferences.getBoolean("saveLogin",false);
//        if (saveLogin == true) {
//            InputPhoneNumber.setText(loginPreferences.getString("phoneNumber",""));
//            InputPassword.setText(loginPreferences.getString("password", ""));
//            checkBoxRememberMe.setChecked(true);
//        }

        checkBoxRememberMe=(CheckBox)findViewById(R.id.remember_shared_pref);
        Paper.init(this);

        ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, ResetPasswordActivity.class);
                //send some data
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });




        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName="Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";
            }
        });




    }

//    public void onClick(View view) {
//        if (view == LoginButton) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(InputPhoneNumber.getWindowToken(), 0);
//
//            phoneNumber = InputPhoneNumber.getText().toString();
//            password1 = InputPassword.getText().toString();
//
//            if (checkBoxRememberMe.isChecked()) {
//                loginPrefsEditor.putBoolean("saveLogin", true);
//                loginPrefsEditor.putString("phone",phoneNumber );
//                loginPrefsEditor.putString("password", password1);
//                loginPrefsEditor.commit();
//            } else {
//                loginPrefsEditor.clear();
//                loginPrefsEditor.commit();
//            }
//        }
//
//
//    }


//login user interface
    private void LoginUser() {
        String phone=InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please fill up all the fields!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please fill up all the fields!",Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Login account");
            loadingBar.setMessage("Please wait while checking user credentials!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone,password);

        }
    }

    private void AllowAccessToAccount(final String phone, final String password) {

        if(checkBoxRememberMe.isChecked()){
            //remember me checkbox#2
           Paper.book().write(Prevalent.UserPhoneKey,phone);
           Paper.book().write(Prevalent.UserPasswordKey,password);
        }




        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();



        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData=dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

//                    usersData.setName(dataSnapshot.child(parentDbName).child(phone).child("username").getValue().toString());
//                    usersData.setName(dataSnapshot.child(parentDbName).child(phone).child("password").getValue().toString());

                    //retrieve data and checking if the user is an admin or a user
                    if(usersData.getPhone().equals(phone)){
                            if(usersData.getPassword().equals(password)){
                                if(parentDbName.equals("Admins"))
                                {
                                    Toast.makeText(LoginActivity.this," Welcome admin!Logged in successfully..",Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    Intent intent= new Intent(LoginActivity.this, AdminHomeActivity.class);
                                    startActivity(intent);

                                }else if(parentDbName.equals("Users"))
                                {
                                    Toast.makeText(LoginActivity.this,"Logged in successfully!",Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    Intent intent= new Intent(LoginActivity.this, HomeActivity.class);
                                    Prevalent.onlineUser=usersData;
                                    startActivity(intent);

                                }
                        }else{
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this,"Password is incorrect!",Toast.LENGTH_SHORT).show();

                            }
                    }

//
                }else{
                    Toast.makeText(LoginActivity.this,"Account with this number " + phone + " doesn't exist!",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
