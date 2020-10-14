package com.example.myonlineshop.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myonlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class SellerLoginActivity extends AppCompatActivity {

    private Button loginSellerBtn;
    private EditText emailInput, passwordInput;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    private SharedPreferences sharedPref;


    //shared prefs code
//    private CheckBox sellerCheckbox;
 //   SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;
//    private static final String PREF_NAME = "prefs";
//    private static final String KEY_REMEMBER = "remember";
//    private static final String KEY_EMAIL = "email";
//    private static final String KEY_PASS = "password";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        emailInput = findViewById(R.id.seller_login_email);
        passwordInput = findViewById(R.id.seller_login_password);
        loginSellerBtn = findViewById(R.id.seller_login_btn);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

//shared pref code
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();
                    String userEmail = firebaseUser.getEmail();
                    sharedPref = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("firebasekey", userId);
                    editor.commit();
                }
            }
        };



        //shared prefs code
//        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        sellerCheckbox = (CheckBox) findViewById(R.id.seller_checkbox);
//
//        if (sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
//            sellerCheckbox.setChecked(true);
//        }
//        else{
//            sellerCheckbox.setChecked(false);
//            emailInput.setText(sharedPreferences.getString(KEY_EMAIL, ""));
//            passwordInput.setText(sharedPreferences.getString(KEY_PASS, ""));
//
//            emailInput.addTextChangedListener(this);
//            passwordInput.addTextChangedListener(this);
//            sellerCheckbox.setOnCheckedChangeListener(this);
//    }


        loginSellerBtn.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        loginSeller();
    }
    });

}


    private void loginSeller() {

        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();

        if (!email.equals("") && !password.equals("")) {


            loadingBar.setTitle("Seller account login");
            loadingBar.setMessage("Please wait while checking user credentials!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {


                                Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();


                            }
                        }
                    });

        } else {
            Toast.makeText(this, "Please complete the login form!", Toast.LENGTH_SHORT).show();

        }

    }
//shared prefs
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        managePrefs();
//
//    }
//shared prefs
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//shared prefs
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        managePrefs();
//
//    }

//    private void managePrefs() {
//
//        if(sellerCheckbox.isChecked()){
//            editor.putString(KEY_EMAIL, emailInput.getText().toString().trim());
//            editor.putString(KEY_PASS, passwordInput.getText().toString().trim());
//            editor.putBoolean(KEY_REMEMBER, true);
//            editor.apply();
//        }else{
//            editor.putBoolean(KEY_REMEMBER, false);
//            editor.remove(KEY_PASS);//editor.putString(KEY_PASS,"");
//            editor.remove(KEY_EMAIL);//editor.putString(KEY_USERNAME, "");
//            editor.apply();
//        }
//
//    }

    //shared prefs
//    @Override
//    public void afterTextChanged(Editable s) {
//
//    }
}
