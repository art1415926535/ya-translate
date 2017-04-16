package com.art1415926535.ya_translate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TranslatorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TranslatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TranslatorFragment extends Fragment
        implements View.OnClickListener{
    private OnFragmentInteractionListener mListener;
    private TextView leftLang;
    private TextView rightLang;
    private EditText editText;
    private TextView translatedText;
    private ConstraintLayout translatedTextConstraintLayout;
    private static long lastTranslateRequest = 0;

    public TranslatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment TranslatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TranslatorFragment newInstance() {
        TranslatorFragment fragment = new TranslatorFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_translator, container, false);

        leftLang = (TextView) view.findViewById(R.id.leftLang);
        rightLang = (TextView) view.findViewById(R.id.rightLang);

        translatedTextConstraintLayout = (ConstraintLayout) view.findViewById(
                R.id.translatedTextConstraintLayout);

        ImageButton swapLangs = (ImageButton) view.findViewById(R.id.swapLangs);
        ImageButton playEditText = (ImageButton) view.findViewById(R.id.playEditText);
        ImageButton clearEditText = (ImageButton) view.findViewById(R.id.clearEditText);

        swapLangs.setOnClickListener(this);
        playEditText.setOnClickListener(this);
        clearEditText.setOnClickListener(this);

//        leftLang.setText((String) Languages.getLanguageNames()[0]);
//        rightLang.setText((String) Languages.getLanguageNames()[1]);
        leftLang.setText("русский");
        rightLang.setText("английский");

        editText = (EditText)view.findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                int len = s.toString().length();

                if (len == 0 && translatedTextConstraintLayout.getVisibility() == View.VISIBLE){
                    translatedTextConstraintLayout.setVisibility(View.GONE);
                }else if (len > 0 && translatedTextConstraintLayout.getVisibility() == View.GONE){
                    translatedTextConstraintLayout.setVisibility(View.VISIBLE);
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

//                if (System.currentTimeMillis() - lastTranslateRequest > 1000) {
                    updateTranslate();
//                }
            }
        });

        translatedText = (TextView) view.findViewById(R.id.translatedText);

        return view;
    }

    /**
     * Translate text from editText and write to translatedText.
     */
    private void updateTranslate() {
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
                        text, leftLang.getText().toString(),rightLang.getText().toString());

                handler.post(new Runnable() {
                    public void run() {
                        // If this is NOT last request.
                        if (myTime != lastTranslateRequest){
                            return;
                        }

                        // Connection or JSON error.
                        if (response == null) {
                            Toast.makeText(getContext(), R.string.connection_error,
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
            Toast.makeText(getContext(), R.string.response_data_error,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        switch (code){
            case 200:
                try {
                    translatedText.setText((String) response.getJSONArray("text").get(0));
                }catch (JSONException e){
                    Toast.makeText(getContext(), R.string.response_data_error,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case 401:
            case 402:
                Toast.makeText(getContext(), R.string.api_key_error,
                        Toast.LENGTH_SHORT).show();
                break;
            case 404:
                Toast.makeText(getContext(), R.string.too_many_requests_error,
                        Toast.LENGTH_SHORT).show();
                break;
            case 413:
                Toast.makeText(getContext(), R.string.request_entity_too_large_error,
                        Toast.LENGTH_SHORT).show();
                break;
            case 422:
                Toast.makeText(getContext(), R.string.text_can_not_be_translated_error,
                        Toast.LENGTH_SHORT).show();
                break;
            case 501:
                Toast.makeText(getContext(),
                        R.string.specified_translation_direction_is_not_supported_error,
                        Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.swapLangs:
                Animation toRight = AnimationUtils.loadAnimation(getContext(), R.anim.translate_to_right);
                Animation toLeft = AnimationUtils.loadAnimation(getContext(), R.anim.translate_to_left);

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

                        leftLang.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_back_to_right));
                        rightLang.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_back_to_left));
                    }
                });

                leftLang.startAnimation(toRight);
                rightLang.startAnimation(toLeft);
                break;

            case R.id.playEditText:
                Toast.makeText(getContext(), "Play", Toast.LENGTH_SHORT).show();
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
