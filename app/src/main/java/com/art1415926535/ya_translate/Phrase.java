package com.art1415926535.ya_translate;

class Phrase {
    public String fromLangCode;
    public String fromText;
    public String toLangCode;
    public String toText;

    Phrase(String fromLangCode, String fromText, String toLangCode, String toText){
        this.fromLangCode = fromLangCode;
        this.fromText = fromText;
        this.toLangCode = toLangCode;
        this.toText = toText;
    }
}