package com.art1415926535.ya_translate.models;

public class Phrase {
    private String fromLangCode;
    private String fromText;
    private String toLangCode;
    private String toText;

    public String getFromLangCode() {
        return fromLangCode;
    }

    public String getFromText() {
        return fromText;
    }

    public String getToLangCode() {
        return toLangCode;
    }

    public String getToText() {
        return toText;
    }

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
        if (! getFromLangCode().equals(otherPhrase.getFromLangCode())){
            return false;
        }

        if (! getToLangCode().equals(otherPhrase.getToLangCode())){
            return false;
        }

        if (! getFromText().contains(otherPhrase.getFromText())){
            return false;
        }

        // Переведенный текст не сравниваем, так как могут быть большие изменения.

        return true;
    }
}