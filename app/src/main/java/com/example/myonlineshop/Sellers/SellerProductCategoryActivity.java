package com.example.myonlineshop.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.myonlineshop.R;

public class SellerProductCategoryActivity extends AppCompatActivity {

    private ImageView tshirts,sports,femaledresses,sweaters;
    private ImageView glasses,hats,bags,shoes;


//code for the categories of products the admin could add to the shop
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_category);




        tshirts=(ImageView)findViewById(R.id.tshirts);
        sports=(ImageView)findViewById(R.id.sports);
        femaledresses=(ImageView)findViewById(R.id.female_dresses);
        sweaters=(ImageView)findViewById(R.id.sweaters);
        glasses=(ImageView)findViewById(R.id.glasses);
        hats=(ImageView)findViewById(R.id.hats);
        bags=(ImageView)findViewById(R.id.bags);
        shoes=(ImageView)findViewById(R.id.shoes);




        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category","Tshirts");
                startActivity(intent);
            }
        });

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category","Sports");
                startActivity(intent);
            }
        });

        femaledresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category","Female Dresses");
                startActivity(intent);
            }
        });

        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category","Sweaters");
                startActivity(intent);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);
            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category","Hats");
                startActivity(intent);
            }
        });

        bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category","Bags");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category","Shoes");
                startActivity(intent);
            }
        });



    }
}
