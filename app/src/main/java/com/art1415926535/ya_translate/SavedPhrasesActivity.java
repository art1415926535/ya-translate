package com.art1415926535.ya_translate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.art1415926535.ya_translate.models.Phrase;

import java.util.ArrayList;

public class SavedPhrasesActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener{
    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    ArrayList<Phrase> arraylist = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_phrases);

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.searchable_list_view);

        for (int i = 0; i < 20; i++) {
            arraylist.add(new Phrase(
                    "ru", "Текст " + String.valueOf(i),
                    "en", "Text " + String.valueOf(i)
            ));
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist, list);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Phrase entry = (Phrase) parent.getAdapter().getItem(position);

                Intent intent = new Intent();
                intent.putExtra("fromLangCode", entry.getFromLangCode());
                intent.putExtra("toLangCode", entry.getToLangCode());
                intent.putExtra("fromText", entry.getFromText());
                intent.putExtra("toText", entry.getToText());

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }
}
