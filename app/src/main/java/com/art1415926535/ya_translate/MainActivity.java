package com.art1415926535.ya_translate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView leftLang;
    private TextView rightLang;
    private EditText editText;
    private TextView translatedText;
    private ConstraintLayout translatedTextConstraintLayout;
    private RecyclerView historyRecyclerView;
    private HistoryRecyclerAdapter historyRecyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    private static long lastTranslateRequest = 0;
    private static long lastTextChangedTime = 0;
    private boolean textTranslated = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_translator);

        Languages.setContext(this);

        leftLang = (TextView) findViewById(R.id.leftLang);
        rightLang = (TextView) findViewById(R.id.rightLang);

        translatedTextConstraintLayout = (ConstraintLayout) findViewById(
                R.id.translatedTextConstraintLayout);

        ImageButton swapLangs = (ImageButton) findViewById(R.id.swapLangs);
        ImageButton playEditText = (ImageButton) findViewById(R.id.playEditText);
        ImageButton clearEditText = (ImageButton) findViewById(R.id.clearEditText);

        swapLangs.setOnClickListener(this);
        playEditText.setOnClickListener(this);
        clearEditText.setOnClickListener(this);

        leftLang.setText("русский");
        rightLang.setText("английский");

        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                int len = s.toString().length();

                if (len == 0 && translatedTextConstraintLayout.getVisibility() == View.VISIBLE){
                    translatedTextConstraintLayout.setVisibility(View.GONE);
                    historyRecyclerView.setVisibility(View.VISIBLE);
                }else if (len > 0 && translatedTextConstraintLayout.getVisibility() == View.GONE){
                    translatedTextConstraintLayout.setVisibility(View.VISIBLE);
                    historyRecyclerView.setVisibility(View.GONE);
                }

                if (len < 60){
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                }else if (len < 100){
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                }else if (len < 160){
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                }else{
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }

                textTranslated = false;
                lastTextChangedTime = System.currentTimeMillis();

            }
        });

        translatedText = (TextView) findViewById(R.id.translatedText);
        translatedText.setMovementMethod(new ScrollingMovementMethod());

        // Recycler View with history requests.
        historyRecyclerView = (RecyclerView) findViewById(R.id.historyRecycler);
        historyRecyclerView.setHasFixedSize(false);

        // Use a linear layout manager.
        recyclerLayoutManager = new LinearLayoutManager(this);
        historyRecyclerView.setLayoutManager(recyclerLayoutManager);

        // Set adapter with history data.
        historyRecyclerAdapter = new HistoryRecyclerAdapter(this,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView topLang = (TextView) v.findViewById(R.id.topLang);
                        TextView bottomLang = (TextView) v.findViewById(R.id.bottomLang);

                        TextView fromText = (TextView) v.findViewById(R.id.fromText);
                        TextView toText = (TextView) v.findViewById(R.id.toText);

                        leftLang.setText(Languages.getNameByCode(
                                topLang.getText().toString().toLowerCase()));
                        rightLang.setText(Languages.getNameByCode(
                                bottomLang.getText().toString().toLowerCase()));

                        editText.setText(fromText.getText().toString());
                        translatedText.setText(toText.getText().toString());
                    }
                });
        historyRecyclerView.setAdapter(historyRecyclerAdapter);

        runTextTranslateWatcher();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Переводчик");

//        Intent intent = new Intent(this, SavedPhrasesActivity.class);
//        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        leftLang.setText(Languages.getNameByCode(data.getStringExtra("fromLangCode")));
        rightLang.setText(Languages.getNameByCode(data.getStringExtra("toLangCode")));

        editText.setText(data.getStringExtra("fromText"));
        translatedText.setText(data.getStringExtra("toText"));
    }

    /**
     * Send an update request if there were no text changes for the last two seconds.
     */
    private void runTextTranslateWatcher() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                while (true) {
                    if ((System.currentTimeMillis() - lastTextChangedTime > 1500) && !textTranslated) {
                        textTranslated = true;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                updateTranslate();
                            }
                        });
                    }
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * Translate text from editText and write to translatedText.
     */
    private void updateTranslate() {
//        Toast.makeText(getApplicationContext(), "UPDATE",
//                Toast.LENGTH_SHORT).show();
        translatedText.setText("");

        final String text = editText.getText().toString();
        if (text.isEmpty()){
            // Nothing to translate.
            return;
        }

        // Save last request time.
        lastTranslateRequest = System.currentTimeMillis();

        final long myTime = lastTranslateRequest;

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {

                final JSONObject response = Translator.translateText(
                        text, leftLang.getText().toString(), rightLang.getText().toString());

                handler.post(new Runnable() {
                    public void run() {
                        // If this is NOT last request.
                        if (myTime != lastTranslateRequest){
                            return;
                        }

                        // Connection or JSON error.
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), R.string.connection_error,
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        processResponseData(response, handler);
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    /**
     * Parse data from response and upate translateText.
     * @param response response from Translator.
     * @param handler handler for update translateText.
     */
    private void processResponseData(final JSONObject response, Handler handler) {
        int code;
        try {
            code = response.getInt("code");
        } catch (JSONException e){
            Toast.makeText(getApplicationContext(), R.string.response_data_error,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        switch (code){
            case 200:
                try {
                    String newTranslatedText = (String) response.getJSONArray("text").get(0);
                    translatedText.setText(newTranslatedText);

                    int len = newTranslatedText.length();
                    if (len < 60){
                        translatedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                    }else if (len < 100){
                        translatedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                    }else if (len < 160){
                        translatedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    }else{
                        translatedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                    }

                    historyRecyclerAdapter.addItem(
                            Languages.getCodeByName(leftLang.getText().toString()),
                            editText.getText().toString(),
                            Languages.getCodeByName(rightLang.getText().toString()),
                            translatedText.getText().toString()
                    );

                } catch (JSONException e){
                    Toast.makeText(getApplicationContext(), R.string.response_data_error,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case 401:
            case 402:
                Toast.makeText(getApplicationContext(), R.string.api_key_error,
                        Toast.LENGTH_SHORT).show();
                break;
            case 404:
                Toast.makeText(getApplicationContext(), R.string.too_many_requests_error,
                        Toast.LENGTH_SHORT).show();
                break;
            case 413:
                Toast.makeText(getApplicationContext(), R.string.request_entity_too_large_error,
                        Toast.LENGTH_SHORT).show();
                break;
            case 422:
                Toast.makeText(getApplicationContext(), R.string.text_can_not_be_translated_error,
                        Toast.LENGTH_SHORT).show();
                break;
            case 501:
            case 502:
                Toast.makeText(getApplicationContext(),
                        R.string.specified_translation_direction_is_not_supported_error,
                        Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.swapLangs:
                Animation toRight = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.translate_to_right);
                Animation toLeft = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.translate_to_left);

                toLeft.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        CharSequence left = leftLang.getText();
                        leftLang.setText(rightLang.getText());
                        rightLang.setText(left);

                        updateTranslate();

                        leftLang.startAnimation(AnimationUtils.loadAnimation(
                                getApplicationContext(), R.anim.translate_back_to_right));
                        rightLang.startAnimation(AnimationUtils.loadAnimation(
                                getApplicationContext(), R.anim.translate_back_to_left));
                    }
                });

                leftLang.startAnimation(toRight);
                rightLang.startAnimation(toLeft);
                break;

            case R.id.playEditText:
                Toast.makeText(getApplicationContext(), "Play", Toast.LENGTH_SHORT).show();
                break;

            case R.id.clearEditText:
                translatedText.setText("");
                editText.setText("");
                editText.requestFocus();
                break;

            default:
                break;
        }
    }
}
