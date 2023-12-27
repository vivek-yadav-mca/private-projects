package com.o2games.playwin.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.o2games.playwin.android.R;
import com.o2games.playwin.android.model.HomeFragItemModel;

import java.util.ArrayList;

public class HomeFragItemAdapter extends RecyclerView.Adapter<HomeFragItemAdapter.HomeFragItemViewHolder> {

    Context context;
    ArrayList<HomeFragItemModel> homeFragItemList;

    public HomeFragItemAdapter(Context context, ArrayList<HomeFragItemModel> homeFragItemList){
        this.context = context;
        this.homeFragItemList = homeFragItemList;
    }

    @NonNull
    @Override
    public HomeFragItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rv_notice_home_frag, null);
        return new HomeFragItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragItemViewHolder holder, int position) {
        HomeFragItemModel model = homeFragItemList.get(position);

        holder.textView.setText(model.getItemName());
        holder.imageView.setImageResource(model.getItemImage());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = null;
//                switch (position) {
//                    case 0:
//                        intent = new Intent(context, GoldenSpinActivity.class);
//                        context.startActivity(intent);
//                        break;
//                    case 1 :
//                        intent = new Intent(context, SpinningActivity.class);
//                        context.startActivity(intent);
//                        break;
//                    case 2 :
//                        Toast.makeText(context, "Flip and Win Card Clicked", Toast.LENGTH_LONG).show();
//                        break;
//                }
//            }
//        });
//    }


    }

    @Override
    public int getItemCount() {
        return homeFragItemList.size();
    }

    public class HomeFragItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public HomeFragItemViewHolder(@NonNull View itemView) {
            super(itemView);
//            imageView = itemView.findViewById(R.id.home_item_image);
//            textView = itemView.findViewById(R.id.home_item_name);
        }
    }

}
