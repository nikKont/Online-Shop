package com.example.myonlineshop.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myonlineshop.Interface.ItemClickListener;
import com.example.myonlineshop.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtproductName,txtProductDescription,txtProductPrice;
    public ImageView imageView;
    public  ItemClickListener listener;
    //constructor
    //view the products in the home activity with the productviewholder of the user
    public ProductViewHolder(View itemView){
        super(itemView);

        imageView=(ImageView)itemView.findViewById(R.id.product_image);
        txtproductName=(TextView)itemView.findViewById(R.id.product_name);
        txtProductDescription=(TextView)itemView.findViewById(R.id.product_description);
        txtProductPrice=(TextView)itemView.findViewById(R.id.product_price);
    }

    public void ItemClickListener(ItemClickListener listener){
        this.listener=listener;
    }


    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);

    }
}
