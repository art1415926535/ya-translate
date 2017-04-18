package com.art1415926535.ya_translate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<Phrase> phraseList;
    private ArrayList<Phrase> arraylist;
    private ListView rootList;

    ListViewAdapter(Context context, List<Phrase> phraseList, ListView list) {
        mContext = context;
        this.phraseList = phraseList;
        this.rootList = list;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Phrase>();
        this.arraylist.addAll(phraseList);
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
            view = inflater.inflate(R.layout.history_card, null);

            // Locate the TextViews in listview_item.xml
            holder.fromLangCode = (TextView) view.findViewById(R.id.topLang);
            holder.toLangCode = (TextView) view.findViewById(R.id.bottomLang);

            holder.fromText = (TextView) view.findViewById(R.id.fromText);
            holder.toText = (TextView) view.findViewById(R.id.toText);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews
        holder.fromLangCode.setText(phraseList.get(position).fromLangCode);
        holder.toLangCode.setText(phraseList.get(position).toLangCode);
        holder.fromText.setText(phraseList.get(position).fromText);
        holder.toText.setText(phraseList.get(position).toText);

        return view;
    }

    // Filter Class
    void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        phraseList.clear();
        if (charText.length() == 0) {
            phraseList.addAll(arraylist);
        } else {
            for (Phrase wp : arraylist) {
                if (wp.fromText.toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.toText.toLowerCase(Locale.getDefault()).contains(charText)) {
                    phraseList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}