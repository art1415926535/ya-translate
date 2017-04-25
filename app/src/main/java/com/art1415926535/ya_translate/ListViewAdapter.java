package com.art1415926535.ya_translate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.art1415926535.ya_translate.DB.DBHelper;
import com.art1415926535.ya_translate.DB.DataBase;
import com.art1415926535.ya_translate.models.Phrase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


class ListViewAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Phrase> phraseList;
    private ArrayList<Phrase> arrayList;
    private DataBase db;

    ListViewAdapter(Context context) {
        mContext = context;

        db = new DataBase(context, DBHelper.TABLE_FAVOURITES);
        db.open();

        phraseList = db.getAllPhrases();
        Collections.reverse(phraseList);

        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(phraseList);
    }

    private class ViewHolder {
        TextView fromLangCode;
        TextView fromText;
        TextView toLangCode;
        TextView toText;
    }

    @Override
    public int getCount() {
        return phraseList.size();
    }

    @Override
    public Phrase getItem(int position) {
        return phraseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.card, null);

            // Locate the TextViews in listview_item.xml.
            holder.fromLangCode = (TextView) view.findViewById(R.id.topLang);
            holder.toLangCode = (TextView) view.findViewById(R.id.bottomLang);

            holder.fromText = (TextView) view.findViewById(R.id.fromText);
            holder.toText = (TextView) view.findViewById(R.id.toText);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews.
        holder.fromLangCode.setText(phraseList.get(position).getFromLangCode());
        holder.toLangCode.setText(phraseList.get(position).getToLangCode());
        holder.fromText.setText(phraseList.get(position).getFromText());
        holder.toText.setText(phraseList.get(position).getToText());

        return view;
    }

    // Filter Class.
    void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        phraseList.clear();
        if (charText.length() == 0) {
            phraseList.addAll(arrayList);
        } else {
            for (Phrase wp : arrayList) {
                if (wp.getFromText().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getToText().toLowerCase(Locale.getDefault()).contains(charText)) {
                    phraseList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}