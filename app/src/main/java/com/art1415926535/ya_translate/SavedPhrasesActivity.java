package com.art1415926535.ya_translate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.art1415926535.ya_translate.models.Phrase;

public class SavedPhrasesActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, ListView.OnTouchListener{
    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;

    float historicX = Float.NaN, historicY = Float.NaN;
    static final int DELTA = 50;
    enum Direction {LEFT, RIGHT;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_phrases);

        // Locate the ListView in activity_saved_phrases.xml
        list = (ListView) findViewById(R.id.searchable_list_view);

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this);

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

        // Locate the EditText in activity_saved_phrases.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            // Enable the Up button.
            ab.setDisplayHomeAsUpEnabled(true);
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                historicX = event.getX();
                historicY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                if (event.getX() - historicX < -DELTA) {
                    return true;
                }
                else if (event.getX() - historicX > DELTA) {
                    return true;
                }
                break;

            default:
                return false;
        }
        return false;
    }
}
