package com.example.myonlineshop.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myonlineshop.Model.Cart;
import com.example.myonlineshop.Prevalent.Prevalent;
import com.example.myonlineshop.R;
import com.example.myonlineshop.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount,txtMsg1;

    //total price
    private int TotalPrice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn = (Button) findViewById(R.id.next_process_btn);
        txtTotalAmount = (TextView) findViewById(R.id.total_price);
        txtMsg1=(TextView)findViewById(R.id.msg1);

        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtTotalAmount.setText("Total Price = " +String.valueOf(TotalPrice));


                Intent intent=new Intent(CartActivity.this, ConfirmOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(TotalPrice));
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        //model class Cart for the cart and pass it to recycler options
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.onlineUser.getPhone()).child("Products"), Cart.class).build();
        //need another viewholder to pass on firebaserecycleradapter
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            //display
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {


                holder.txtProductQuantity.setText("Quantity= " + model.getQuantity());
                holder.txtProductName.setText(model.getName());
                holder.txtProductPrice.setText("Price $" + model.getPrice());
                //total price working with $ symbols

                try{
                int oneProductTotalPrice=((Integer.parseInt(model.getPrice().replaceAll("\\D+","")))) * Integer.parseInt(model.getQuantity());
                TotalPrice=TotalPrice + oneProductTotalPrice;}
                catch(NumberFormatException ex){

                }

                //create two options
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Edit", "Delete"

                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart options: ");

                        //set click button on these two options
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if (which == 1) {
                                    cartListRef.child("User View")
                                            .child(Prevalent.onlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "Item deleted successfully!", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
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
                    String userName=dataSnapshot.child("name").getValue().toString();
                    //depending on the shipping state set visibility for the next button and the message to the user
                    if(shippingState.equals("shipped")){
                        txtTotalAmount.setText("Dear "+userName+"\n order is shipped successfully!");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations,your order has been shipped successfully!The package will arrive in 2-3 days!");
                        NextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,"You can place more orders once your package arrives!",Toast.LENGTH_SHORT).show();

                    }else if(shippingState.equals("not shipped")){

                        txtTotalAmount.setText("Shipping State= Not Shipped");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        NextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,"You can place more orders once your package arrives!",Toast.LENGTH_SHORT).show();


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
