package com.art1415926535.ya_translate;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {
    private List<String[][]> data;
    private View.OnClickListener onClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private CardView cardView;
        ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    HistoryRecyclerAdapter(Context context, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        data = new ArrayList<>();
    }

    void addItem(String fromLangCode, String fromText, String toLangCode, String toText) {
        String[] codes = {fromLangCode, toLangCode};
        String[] texts = {fromText, toText};
        String[] new_data[] = {codes, texts};

        data.add(new_data);
        this.notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HistoryRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setOnClickListener(onClickListener);
        return new ViewHolder((CardView) v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView topLang = (TextView) holder.cardView.findViewById(R.id.topLang);
        TextView bottomLang = (TextView) holder.cardView.findViewById(R.id.bottomLang);

        TextView fromText = (TextView) holder.cardView.findViewById(R.id.fromText);
        TextView toText = (TextView) holder.cardView.findViewById(R.id.toText);

        String[][] positoinData = data.get(data.size()-position-1);

        topLang.setText(positoinData[0][0]);
        bottomLang.setText(positoinData[0][1]);

        fromText.setText(positoinData[1][0]);
        toText.setText(positoinData[1][1]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

}
