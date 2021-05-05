package com.google.example.fridgefriend;

import android.graphics.Canvas;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.View;



public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((MyItemRecyclerViewAdapter.MyViewHolder) viewHolder).viewForeground;

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((MyItemRecyclerViewAdapter.MyViewHolder) viewHolder).viewForeground;

        //if moving to the right, means dx > 0
        View bkgd_left = ((MyItemRecyclerViewAdapter.MyViewHolder) viewHolder).viewBackground_left;
        View bkgd_right = ((MyItemRecyclerViewAdapter.MyViewHolder) viewHolder).viewBackground_right;
        if(dX >0 ){
            bkgd_left.setVisibility(View.INVISIBLE);
            bkgd_right.setVisibility(View.VISIBLE);
        }else{
            bkgd_left.setVisibility(View.VISIBLE);
            bkgd_right.setVisibility(View.INVISIBLE);
        }

        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((MyItemRecyclerViewAdapter.MyViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foregroundView);
        //added below b/c item wasnt disapperaing from list
       // final View bkgd_left_view = ((MyItemRecyclerViewAdapter.MyViewHolder) viewHolder).viewBackground_left;
        //getDefaultUIUtil().clearView(bkgd_left_view);
        //final View bkgd_right_view = ((MyItemRecyclerViewAdapter.MyViewHolder) viewHolder).viewBackground_right;
        //getDefaultUIUtil().clearView(bkgd_right_view);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((MyItemRecyclerViewAdapter.MyViewHolder) viewHolder).viewForeground;

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}