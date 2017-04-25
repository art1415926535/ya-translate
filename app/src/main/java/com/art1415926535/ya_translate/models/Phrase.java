package com.art1415926535.ya_translate.models;

import android.content.ContentValues;

import com.art1415926535.ya_translate.DB.DBHelper;

public class Phrase {
    private long id;

    private String fromLangCode;
    private String fromText;
    private String toLangCode;
    private String toText;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFromLangCode() {
        return fromLangCode;
    }

    public void setFromLangCode(String fromLangCode) {
        this.fromLangCode = fromLangCode;
    }

    public String getFromText() {
        return fromText;
    }

    public void setFromText(String fromText) {
        this.fromText = fromText;
    }

    public String getToLangCode() {
        return toLangCode;
    }

    public void setToLangCode(String toLangCode) {
        this.toLangCode = toLangCode;
    }

    public String getToText() {
        return toText;
    }

    public void setToText(String toText) {
        this.toText = toText;
    }

    public Phrase(){}

    public Phrase(String fromLangCode, String fromText, String toLangCode, String toText){
        this.fromLangCode = fromLangCode;
        this.fromText = fromText;
        this.toLangCode = toLangCode;
        this.toText = toText;
    }

    /**
     * Does this phrase contain otherPhrase?
     * @param otherPhrase Other Phrase
     * @return is it contains
     */
    public boolean contains(Phrase otherPhrase){
        if (getFromLangCode() != null &&
                otherPhrase.getFromLangCode() != null &&
                ! getFromLangCode().equals(otherPhrase.getFromLangCode())){
            return false;
        }

        if (! getToLangCode().equals(otherPhrase.getToLangCode())){
            return false;
        }

        if (! getFromText().contains(otherPhrase.getFromText())){
            return false;
        }

        return true;
    }

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();

        values.put(DBHelper.KEY_ID, getId());

        values.put(DBHelper.KEY_FROM_LANG_CODE, getFromLangCode());
        values.put(DBHelper.KEY_FROM_TEXT, getFromText());

        values.put(DBHelper.KEY_TO_LANG_CODE, getToLangCode());
        values.put(DBHelper.KEY_TO_TEXT, getToText());

        return values;
    }
}