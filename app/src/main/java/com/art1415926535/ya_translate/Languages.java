package com.art1415926535.ya_translate;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;


class Languages {
    private static Languages ourInstance;

    static Languages getInstance() {
        return ourInstance;
    }

    public static void setContext(Context context){
        ourInstance = new Languages(context);
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
    static Object[] getLanguageNames(){
        return languages.keySet().toArray();
    }

    static String getCodeByName(String name){
        return languages.get(name);
    }

    static String getNameByCode(String code){
        for (Map.Entry<String, String> entry : languages.entrySet()) {
            if (entry.getValue().equals(code)){
                return entry.getKey();
            }
        }
        return "";
    }
}
