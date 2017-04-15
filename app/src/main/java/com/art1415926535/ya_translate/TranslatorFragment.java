package com.art1415926535.ya_translate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TranslatorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TranslatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TranslatorFragment extends Fragment implements View.OnClickListener{
    private OnFragmentInteractionListener mListener;
    private ImageButton swapLangs;
    private TextView leftLang;
    private TextView rightLang;

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
        swapLangs = (ImageButton) view.findViewById(R.id.swapLangs);
        swapLangs.setOnClickListener(this);

        leftLang.setText((String)Languages.getInstance(getContext()).getLanguageNames()[0]);
        rightLang.setText((String)Languages.getInstance(getContext()).getLanguageNames()[1]);

        return view;
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

                        leftLang.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_back_to_right));
                        rightLang.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_back_to_left));
                    }

                });

                leftLang.startAnimation(toRight);
                rightLang.startAnimation(toLeft);

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
