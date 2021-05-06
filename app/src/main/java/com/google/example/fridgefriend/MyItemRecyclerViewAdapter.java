package com.google.example.fridgefriend;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.example.fridgefriend.dummy.EdibleItem;//TODO make this path not int he dummy folder

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link EdibleItem}.
 * TODO: Replace the implementation with code for your data type and rename the class
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.MyViewHolder> {
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, mContentView, price;
        public ImageView thumbnail;
        public RelativeLayout viewBackground_left, viewBackground_right;
        public RelativeLayout viewForeground;
        public EdibleItem mItem;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);

            viewBackground_left = view.findViewById(R.id.view_background_left);
            viewBackground_right = view.findViewById(R.id.view_background_right);
            viewForeground = view.findViewById(R.id.view_foreground);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }

    private List<EdibleItem> mValues;
    private Context context;

    public MyItemRecyclerViewAdapter(Context context, List<EdibleItem> items) {
        this.context = context;
        this.mValues = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_fridge_list, parent, false);
        //fragment_shopping_list
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        //holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void removeItem(int position) {
        mValues.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
        //notifyItemRangeChanged(position, getItemCount());
    }

    //thse are essentially the same function
    public void restoreItem(EdibleItem item, int position) {
        mValues.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void addItem(EdibleItem item, int position){
        mValues.add(position, item);
        notifyItemInserted(position);
    }

    public void clear(){
        while(getItemCount() > 0){
            removeItem(getItemCount()-1);
        }
    }
}