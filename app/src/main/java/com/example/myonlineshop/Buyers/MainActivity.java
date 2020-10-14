package com.example.myonlineshop.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myonlineshop.Model.Users;
import com.example.myonlineshop.Prevalent.Prevalent;
import com.example.myonlineshop.R;
import com.example.myonlineshop.Sellers.SellerHomeActivity;
import com.example.myonlineshop.Sellers.SellerRegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.paperdb.Paper;



public class MainActivity extends AppCompatActivity {

    private Button joinButton,loginButton,changeLangButton;
    private ProgressDialog loadingBar;
    private TextView sellerBegin,dateTextview;

    //shared prefs
    SharedPreferences prefs;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "myKey";



    //create account and login if you have already an account buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //date in main activity
        dateTextview=(TextView)findViewById(R.id.date_textview);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMMM/d");
        String strDate = sdf.format(cal.getTime());
        dateTextview.setText(strDate);
    //save date in a shared pref
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY,strDate);
        editor.apply();


        //change language button
        changeLangButton=(Button)findViewById(R.id.change_lang_button);

        joinButton = (Button) findViewById(R.id.main_join_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        //for the became a seller button
        sellerBegin=(TextView)findViewById(R.id.seller_begin);
        loadingBar=new ProgressDialog(this);

        Paper.init(this);

        //for the became a seller button send the user from main to seller
        sellerBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey= Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);
//logging in automatically if the user has logged in already one time and clicked the remember button
        if(UserPhoneKey !="" && UserPasswordKey!=""){
            if(!TextUtils.isEmpty(UserPhoneKey)&& !TextUtils.isEmpty(UserPasswordKey)){


                AllowAccess(UserPhoneKey,UserPasswordKey);

                loadingBar.setTitle("Already looged in!");
                loadingBar.setTitle("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null){
            Intent intent=new Intent(MainActivity.this, SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void AllowAccess(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();



        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(phone).exists())
                {
                    Users usersData=dataSnapshot.child("Users").child(phone).getValue(Users.class);

//                    usersData.setName(dataSnapshot.child(parentDbName).child(phone).child("username").getValue().toString());
//                    usersData.setName(dataSnapshot.child(parentDbName).child(phone).child("password").getValue().toString());

                    //retrieve data
                    if(usersData.getPhone().equals(phone)){
                        if(usersData.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this,"You are already logged in!",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent= new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.onlineUser=usersData;
                            startActivity(intent);
                        }else{
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this,"Password is incorrect!",Toast.LENGTH_SHORT).show();

                        }
                    }


                }else{
                    Toast.makeText(MainActivity.this,"Account with this number " + phone + " doesn't exist!",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
