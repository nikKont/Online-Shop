package com.example.myonlineshop.Interface;

import android.view.View;

//interface when the user clicks on the item

public interface ItemClickListener {

    void onClick(View view, int position,boolean isLongClick);

}
