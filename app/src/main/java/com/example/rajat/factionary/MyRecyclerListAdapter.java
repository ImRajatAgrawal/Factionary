package com.example.rajat.factionary;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyRecyclerListAdapter  extends RecyclerView.Adapter<MyRecyclerListAdapter.ViewHolder>{
   private void callactivity(ViewHolder viewHolder){
       Intent i=new Intent(mainActivity,FactsActivity.class);
             i.putExtra("fact",viewHolder.textView.getText().toString());
                mainActivity.startActivity(i);

   }
    private MyRecyclerListData[] listData;
    private Activity mainActivity;
    MyRecyclerListAdapter(MyRecyclerListData[] listData , Activity mainActivity){
        this.listData=listData;
        this.mainActivity=mainActivity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.homescreenrecyclerview, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final MyRecyclerListData myListData = listData[position];
        viewHolder.textView.setText(myListData.getDescription());
        viewHolder.button.setBackgroundResource(myListData.getImgId());
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    callactivity(viewHolder);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton button;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.button = itemView.findViewById(R.id.factimage);
            this.textView =  itemView.findViewById(R.id.titleoffact);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.recyclerrelativelayout);
        }
    }
}
