package com.art1415926535.ya_translate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.art1415926535.ya_translate.DB.DbHelper;

public class SavedPhrasesActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener{
    // Declare Variables
    SearchView editsearch;
    RecyclerView favoritiesRecyclerView;
    PhrasesRecyclerAdapter phrasesRecyclerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_phrases);

        // Recycler View with history requests.
        favoritiesRecyclerView = (RecyclerView) findViewById(R.id.favoritesRecycler);
        favoritiesRecyclerView.setHasFixedSize(false);

        // Use a linear layout manager.
        RecyclerView.LayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        favoritiesRecyclerView.setLayoutManager(recyclerLayoutManager);

        // Set adapter with history data.
        phrasesRecyclerAdapter = new PhrasesRecyclerAdapter(this,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView topLang = (TextView) v.findViewById(R.id.topLang);
                        TextView bottomLang = (TextView) v.findViewById(R.id.bottomLang);

                        TextView fromText = (TextView) v.findViewById(R.id.fromText);
                        TextView toText = (TextView) v.findViewById(R.id.toText);

                        Intent intent = new Intent();
                        intent.putExtra("fromLangCode", topLang.getText().toString());
                        intent.putExtra("toLangCode", bottomLang.getText().toString());
                        intent.putExtra("fromText", fromText.getText().toString());
                        intent.putExtra("toText", toText.getText().toString());

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                },
                DbHelper.TABLE_FAVOURITES);

        favoritiesRecyclerView.setAdapter(phrasesRecyclerAdapter);
        ItemTouchHelper.Callback callback = new MovieTouchHistoryHelper(phrasesRecyclerAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(favoritiesRecyclerView);

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
        phrasesRecyclerAdapter.filter(newText);
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
}
