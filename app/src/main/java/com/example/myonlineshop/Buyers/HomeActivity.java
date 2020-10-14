package com.example.myonlineshop.Buyers;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.myonlineshop.Admin.AdminMaintainProductsActivity;
import com.example.myonlineshop.Model.Products;
import com.example.myonlineshop.Prevalent.Prevalent;
import com.example.myonlineshop.R;
import com.example.myonlineshop.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);
//the intent will work only if it comes from the admin activity
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle !=null){

            type=getIntent().getExtras().get("Admin").toString();
        }


        ProductsRef= FirebaseDatabase.getInstance().getReference().child("Products");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        Paper.init(this);
//the cart icon
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!type.equals("Admin")){
                    Intent intent=new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);

                }


            }
        });
        //navigation drawer init
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//navigation view init
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
//navigation header view
        View headerView=navigationView.getHeaderView(0);
        TextView userNameTextView=headerView.findViewById(R.id.user_profile_name);
        ImageView profileImageView=headerView.findViewById(R.id.user_profile_image);

        if(!type.equals("Admin")){
            userNameTextView.setText(Prevalent.onlineUser.getName());
            Picasso.get().load(Prevalent.onlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);

        }
//displaying the products with recyclerview
        recyclerView=findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
//                R.id.nav_tools, R.id.nav_share)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
    }
//here is the code for the display of the products taken from the database to the home activity
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()//show only the products that are approved by the admin
                .setQuery(ProductsRef.orderByChild("productState").equalTo("Approved"),Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder>  adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {

                        //get product name and description
                        holder.txtproductName.setText(model.getName());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        //get product image
                        Picasso.get().load(model.getImage()).into(holder.imageView);



                        //click on product image and go to the product details
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(type.equals("Admin")){
                                    Intent intent=new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);

                                }else{
                                    Intent intent=new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);

                                }


                            }
                        });


                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                        ProductViewHolder holder=new ProductViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
//navigation drawer overrides
    @Override
    public void onBackPressed() {
        DrawerLayout drawer=(DrawerLayout)findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
        super.onBackPressed();
    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//       if(id==R.id.action_settings){
//           return true; }



        return super.onOptionsItemSelected(item);
    }
//taken to another activity if the user clicks the menuitems
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {



        int id = menuItem.getItemId();
        if (id == R.id.nav_cart) {
            //so as not to work for the admin because there is an error when you press the navigation drawer buttons for the admin
            if(!type.equals("Admin")){

                Intent intent=new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);


            }




        }
        if (id == R.id.nav_search) {

            if(!type.equals("Admin")){
                Intent intent=new Intent(HomeActivity.this, SearchProductsActivity.class);
                startActivity(intent);


            }


        }

        if (id == R.id.nav_settings) {

            if(!type.equals("Admin")){
                Intent intent=new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);


            }
            Intent intent=new Intent(HomeActivity.this,SettingsActivity.class);
            startActivity(intent);

        }

        if (id == R.id.nav_map) {
            //alert message for the map
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Store Location");
            dlgAlert.setTitle("");
            dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //dismiss the dialog
                }
            });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            if(!type.equals("Admin")){

                Intent intent=new Intent(HomeActivity.this,MapsActivity.class);
                startActivity(intent);

            }

        }


        if (id == R.id.nav_sensors) {

            if(!type.equals("Admin")){
                Intent intent=new Intent(HomeActivity.this,SensorsActivity.class);
                startActivity(intent);

            }
            Intent intent=new Intent(HomeActivity.this,SensorsActivity.class);
            startActivity(intent);



        }


        if (id == R.id.nav_logout) {

            if(!type.equals("Admin")){
                //destroy the user from the database
                //already logged in doesnt work if you press the logout
                //taken from home to main activity
                Paper.book().destroy();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();



            }


        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;






    }


}

