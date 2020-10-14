package com.example.myonlineshop.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myonlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputPhoneNumber,InputPassword,InputName;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username);
        InputPhoneNumber=(EditText)findViewById(R.id.register_phone);
        InputPassword=(EditText)findViewById(R.id.register_password);
        loadingBar= new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }
//warning to fill up all the fields
    //creating account
    private void CreateAccount() {

        String name=InputName.getText().toString();
        String phone=InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please fill up all the fields!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please fill up all the fields!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please fill up all the fields!",Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Create account");
            loadingBar.setMessage("Please wait while checking user credentials!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Validatephone(name,phone,password);


        }

    }
//checking if there is an account with the same phone number and then creating the account
    private void Validatephone(final String name, final String phone, final String password) {

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())){
                    HashMap<String,Object> userdataMap=new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name", name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"Congratulations,your account has been created!",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent= new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                    }else{
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Network error!Please try again!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else {

                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "This phone already exists!Please try again with another phone number!", Toast.LENGTH_SHORT).show();

                    Intent intent= new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
