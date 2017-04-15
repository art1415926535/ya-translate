package com.art1415926535.ya_translate;

import android.content.Context;

import java.util.HashMap;


class Languages {
    private static Languages ourInstance;

    static Languages getInstance(Context context) {
        if (ourInstance == null){
            ourInstance = new Languages(context);
        }
        return ourInstance;
    }

    // Language names and codes.
    private static HashMap<String, String> languages = new HashMap<>();

    private Languages(Context context){
        // Read languages from resource.
        if (languages.isEmpty()){
            String[] stringArray = context.getResources().getStringArray(R.array.langs);
            for (String entry : stringArray) {
                String[] splitResult = entry.split("\\|", 2);
                // Name and code.
                languages.put(splitResult[0], splitResult[1]);
            }
        }
    }

    /**
     * @return set of language names
     */
    Object[] getLanguageNames(){
        return languages.keySet().toArray();
    }
}
