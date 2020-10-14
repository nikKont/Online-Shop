package com.example.myonlineshop.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myonlineshop.Model.Products;
import com.example.myonlineshop.Prevalent.Prevalent;
import com.example.myonlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartButton;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productName,productDescription,productPrice;
    private String productID="",state="Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID=getIntent().getStringExtra("pid");

        addToCartButton=(Button)findViewById(R.id.add_to_cart_btn);
        numberButton=(ElegantNumberButton)findViewById(R.id.number_btn);
        productImage=(ImageView)findViewById(R.id.product_image_details);
        productName=(TextView)findViewById(R.id.product_name_details);
        productDescription=(TextView)findViewById(R.id.product_description_details);
        productPrice=(TextView)findViewById(R.id.product_price_details);

        //getProductDetails method
        getProductDetails(productID);

        //button listener
        //add to cart limit one order per time until the initial order is shipped validation
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state.equals("Order Placed") || state.equals("Order Shipped")){
                    Toast.makeText(ProductDetailsActivity.this,"You can place more orders once your initial order is shipped!",Toast.LENGTH_LONG).show();
                }else{
                    addToCartList();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    private void addToCartList() {
        //time and date the add occured
        String saveCurrentTime,saveCurrentDate;

        Calendar callForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM DD,yyyy");
        saveCurrentDate=currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentDate.format(callForDate.getTime());

        //store it to database
        final DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");

        //Hashmap to store the data
        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("name",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");
        //database for the cartlist of the user
        cartListRef.child("User View").child(Prevalent.onlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //for the admin view
                        if(task.isSuccessful()){
                            cartListRef.child("Admin View").child(Prevalent.onlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(ProductDetailsActivity.this,"Added to Cart!",Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    private void getProductDetails(final String productID) {
        DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products products=dataSnapshot.getValue(Products.class);

                    //set the details
                    productName.setText(products.getName());
                    productPrice.setText(products.getPrice()+"$");
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void CheckOrderState(){
        DatabaseReference ordersRef;
        ordersRef=FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.onlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingState=dataSnapshot.child("state").getValue().toString();


                    if(shippingState.equals("shipped")){
                       state="Order Shipped" ;

                    }else if(shippingState.equals("not shipped")) {
                        state="Order Placed";


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
