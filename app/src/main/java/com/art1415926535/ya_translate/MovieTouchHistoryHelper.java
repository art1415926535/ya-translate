package com.art1415926535.ya_translate;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class MovieTouchHistoryHelper extends ItemTouchHelper.SimpleCallback {
    private HistoryRecyclerAdapter adapter;

    public MovieTouchHistoryHelper(HistoryRecyclerAdapter adapter){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // Not implemented
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // Remove item
        adapter.removeItem(viewHolder.getAdapterPosition());
    }
}
