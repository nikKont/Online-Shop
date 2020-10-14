package com.example.myonlineshop.Buyers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myonlineshop.Prevalent.Prevalent;
import com.example.myonlineshop.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {


    private CircleImageView profileImageView;
    private EditText fullnameEditText,userphoneEditText,addressEditText;
    private TextView profileChangeTextBtn,closeTextBtn,saveTextBtn;
    private Button securityQuestionBtn;

    //to change the users settings
    private Uri imageUri;
    private String myUrl="";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile pics");

        profileImageView=(CircleImageView)findViewById(R.id.settings_profile_image);
        fullnameEditText=(EditText)findViewById(R.id.settings_full_name);
        userphoneEditText=(EditText)findViewById(R.id.settings_phone_number);
        addressEditText=(EditText)findViewById(R.id.settings_address);
        profileChangeTextBtn=(TextView)findViewById(R.id.profile_image_change_btn);
        closeTextBtn=(TextView)findViewById(R.id.close_settings_btn);
        saveTextBtn=(TextView)findViewById(R.id.update_account_settings_btn);
        securityQuestionBtn=findViewById(R.id.security_questions_btn);

        userInfoDisplay(profileImageView,fullnameEditText,userphoneEditText,addressEditText);

        securityQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this, ResetPasswordActivity.class);
                //send some data
                intent.putExtra("check","settings");
                startActivity(intent);
            }
        });

        //code to change the profile settings
        //close button
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("Clicked")){

                    userInfoSaved();

                }else{

                    updateOnlyUserInfo();

                }
            }
        });
        //click the profile image
        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="Clicked";

                // start cropping activity for pre-acquired image saved on the device
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);

            }
        });

    }
    //method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            //store the result
            imageUri=result.getUri();
            //display the image
            profileImageView.setImageURI(imageUri);
        }else{
            Toast.makeText(this,"Error! Try again!",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }

    }

    private void updateOnlyUserInfo() {

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
        //update only info without the image
        //use hashmap because there is a lot of data
        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("name",fullnameEditText.getText().toString());
        userMap.put("address",addressEditText.getText().toString());
        userMap.put("phoneOrder",userphoneEditText.getText().toString());
        ref.child(Prevalent.onlineUser.getPhone()).updateChildren(userMap);



        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this,"Profile info updated successfully!",Toast.LENGTH_SHORT).show();
        finish();

    }

    private void userInfoSaved() {

        if(TextUtils.isEmpty(fullnameEditText.getText().toString())){
            Toast.makeText(this,"Name is mandatory!",Toast.LENGTH_SHORT);
        }else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this,"Address is mandatory!",Toast.LENGTH_SHORT);
        }else if(TextUtils.isEmpty(userphoneEditText.getText().toString())){
            Toast.makeText(this,"Phone number is mandatory!",Toast.LENGTH_SHORT);
        }else if(checker.equals("Clicked")){
            uploadImage();
        }


}

    private void uploadImage() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setTitle("Please wait,while we are updating your account information.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null){
            final StorageReference fileRef=storageProfilePictureRef
                    .child(Prevalent.onlineUser.getPhone() + ".jpg");

            uploadTask=fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        //get the result of the downloaded url
                        Uri downloadUrl=task.getResult();
                        myUrl=downloadUrl.toString();

                        //store it to users database
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");

                        //use hashmap because there is a lot of data
                        HashMap<String,Object> userMap=new HashMap<>();
                        userMap.put("name",fullnameEditText.getText().toString());
                        userMap.put("address",addressEditText.getText().toString());
                        userMap.put("phoneOrder",userphoneEditText.getText().toString());
                        userMap.put("image",myUrl);
                        ref.child(Prevalent.onlineUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                        Toast.makeText(SettingsActivity.this,"Profile info updated successfully!",Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(this,"Image is not selected!",Toast.LENGTH_SHORT).show();
        }
    }

    //code for the settings activity where the user can change phone password username address and profile image
    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullnameEditText, final EditText userphoneEditText, final EditText addressEditText) {

        DatabaseReference UsersRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.onlineUser.getPhone());
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    if(dataSnapshot.child("image").exists()){
                        String image=dataSnapshot.child("image").getValue().toString();
                        String name=dataSnapshot.child("name").getValue().toString();
                        String phone=dataSnapshot.child("phone").getValue().toString();
                        String address=dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullnameEditText.setText(name);
                        userphoneEditText.setText(phone);
                        addressEditText.setText(address);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
