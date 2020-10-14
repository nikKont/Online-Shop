package com.example.myonlineshop.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myonlineshop.Buyers.HomeActivity;
import com.example.myonlineshop.Buyers.MainActivity;
import com.example.myonlineshop.R;

public class AdminHomeActivity extends AppCompatActivity {

    private Button LogoutBtn,CheckOrdersBtn,maintainProductsBtn,approveProductsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        //for the adminneworders activity
        LogoutBtn=(Button)findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn=(Button)findViewById(R.id.check_orders_btn);
        maintainProductsBtn=(Button)findViewById(R.id.maintain_btn);
        approveProductsBtn=findViewById(R.id.approve_product_btn);

        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);

            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        approveProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomeActivity.this, ApproveNewProductsActivity.class);
                startActivity(intent);

            }
        });

    }
}
