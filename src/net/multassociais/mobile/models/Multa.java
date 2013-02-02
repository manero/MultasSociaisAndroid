package net.multassociais.mobile.models;

import java.util.ArrayList;

public class Multa {

    private String mTitle;
    
    public static class MultasCollection extends ArrayList<Multa> {
        private static final long serialVersionUID = 5231569247900180309L;
        
    }

    public String getTitle() {
        return mTitle;
    }

    public Multa(String title) {
        super();
        mTitle = title;
    }
    
}
