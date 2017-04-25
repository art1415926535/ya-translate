package com.art1415926535.ya_translate;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.art1415926535.ya_translate.DB.DataBase;
import com.art1415926535.ya_translate.models.Phrase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


class PhrasesRecyclerAdapter extends RecyclerView.Adapter<PhrasesRecyclerAdapter.ViewHolder> {
    private List<Phrase> data;
    private List<Phrase> visibleData;
    private View.OnClickListener onClickListener;
    private DataBase db;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    PhrasesRecyclerAdapter(Context context, View.OnClickListener onClickListener, String table) {
        this.onClickListener = onClickListener;

        db = new DataBase(context, table);
        db.open();

        List<Phrase> phrases = db.getAllPhrases();
        Collections.reverse(phrases);
        data = phrases;
        visibleData = new ArrayList<>();

        visibleData.addAll(data);
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
        Phrase phrase = visibleData.get(position);
        db.deletePhrase(phrase);
        for (int i=0; i<data.size(); i++){
            if(phrase.equals(data.get(i))){
                data.remove(i);
            }
        }
        visibleData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public PhrasesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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
        Phrase currentPhrase = visibleData.get(position);

        topLang.setText(currentPhrase.getFromLangCode());
        bottomLang.setText(currentPhrase.getToLangCode());

        toText.setText(currentPhrase.getToText());
        fromText.setText(currentPhrase.getFromText());

    }

    // Return the size of dataset
    @Override
    public int getItemCount() {
        return visibleData.size();
    }

    public void filter(String text){
        text = text.toLowerCase(Locale.getDefault());

        visibleData.clear();

        if (text.length() == 0) {
            visibleData.addAll(data);
        } else {
            for (Phrase phrase : data) {
                if (phrase.getFromText().toLowerCase(Locale.getDefault()).contains(text) ||
                        phrase.getToText().toLowerCase(Locale.getDefault()).contains(text)) {
                    visibleData.add(phrase);
                }
            }
        }
        notifyDataSetChanged();
    }

}
