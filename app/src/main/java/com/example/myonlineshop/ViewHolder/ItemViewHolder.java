package com.example.myonlineshop.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myonlineshop.Interface.ItemClickListener;
import com.example.myonlineshop.R;

import org.w3c.dom.Text;


public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtproductName,txtProductDescription,txtProductPrice,txtProductStatus;
    public ImageView imageView;
    public ItemClickListener listener;

    //view the products that wait approval from admin in seller's home page
    public ItemViewHolder(View itemView){
        super(itemView);

        imageView=(ImageView)itemView.findViewById(R.id.product_seller_image);
        txtproductName=(TextView)itemView.findViewById(R.id.product_seller__name);
        txtProductDescription=(TextView)itemView.findViewById(R.id.product_seller_description);
        txtProductPrice=(TextView)itemView.findViewById(R.id.product_seller_price);
        txtProductStatus=(TextView)itemView.findViewById(R.id.product_state);
    }

    public void ItemClickListener(ItemClickListener listener){

        this.listener=listener;
    }


    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);

    }
}

