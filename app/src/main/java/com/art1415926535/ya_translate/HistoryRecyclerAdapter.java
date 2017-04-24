package com.art1415926535.ya_translate;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.art1415926535.ya_translate.DB.DataBase;
import com.art1415926535.ya_translate.DB.DbHelper;
import com.art1415926535.ya_translate.models.Phrase;

import java.util.Collections;
import java.util.List;


class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {
    private List<Phrase> data;
    private View.OnClickListener onClickListener;
    private DataBase db;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    HistoryRecyclerAdapter(Context context, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;

        db = new DataBase(context, DbHelper.TABLE_HISTORY);
        db.open();

        List<Phrase> phrases = db.getAllPhrases();
        Collections.reverse(phrases);
        data = phrases;
    }

    void addItem(String fromLangCode, String fromText, String toLangCode, String toText) {
        Phrase newPhrase = new Phrase(fromLangCode, fromText, toLangCode, toText);

        // If list is not empty and last phrase same as new.
        if ((! data.isEmpty()) && (newPhrase.contains(data.get(0)))){
            Phrase lastPhrase = data.get(0);

            // Update last phrase.
            lastPhrase.setFromLangCode(newPhrase.getFromLangCode());
            lastPhrase.setFromText(newPhrase.getFromText());
            lastPhrase.setToLangCode(newPhrase.getToLangCode());
            lastPhrase.setToText(newPhrase.getToText());

            // Update row in database.
            db.updatePhrase(lastPhrase);
        } else {
            // Add new phrase.
            data.add(0, newPhrase);
            newPhrase.setId(db.createPhrase(newPhrase));
        }
        notifyDataSetChanged();
    }

    void removeItem(int position){
        Phrase phrase = data.get(position);
        db.deletePhrase(phrase);
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public HistoryRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        v.setOnClickListener(onClickListener);
        return new ViewHolder((CardView) v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView topLang = (TextView) holder.cardView.findViewById(R.id.topLang);
        TextView bottomLang = (TextView) holder.cardView.findViewById(R.id.bottomLang);

        TextView fromText = (TextView) holder.cardView.findViewById(R.id.fromText);
        TextView toText = (TextView) holder.cardView.findViewById(R.id.toText);

        // Revers of list
        Phrase currentPhrase = data.get(position);

        topLang.setText(currentPhrase.getFromLangCode());
        bottomLang.setText(currentPhrase.getToLangCode());

        toText.setText(currentPhrase.getToText());
        fromText.setText(currentPhrase.getFromText());

    }

    // Return the size of dataset
    @Override
    public int getItemCount() {
        return data.size();
    }

}
