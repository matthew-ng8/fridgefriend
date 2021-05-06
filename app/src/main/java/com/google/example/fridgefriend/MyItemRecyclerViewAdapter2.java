package com.google.example.fridgefriend;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.example.fridgefriend.dummy.DummyContent.DummyItem;
import com.google.example.fridgefriend.dummy.EdibleItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.google.example.fridgefriend.dummy.EdibleItem}.
 * TODO: rename this class so we know it's for the Shopping List
 * Differs from the FridgeList Adapter only by what view it inflates. i'm sure could make them the same
 * Uses the same ViewHolder defined in {@link MyItemRecyclerViewAdapter}
 */
public class MyItemRecyclerViewAdapter2 extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.MyViewHolder> {

    private final List<EdibleItem> mValues;
    private Context context;

    public MyItemRecyclerViewAdapter2(List<EdibleItem> items) {
        mValues = items;
    }

    public MyItemRecyclerViewAdapter2(Context context, List<EdibleItem> items) {
        this.context = context;
        this.mValues = items;
    }

    @Override
    public MyItemRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_shopping_list, parent, false);
        return new MyItemRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyItemRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

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



        /*@Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }*/
    }
