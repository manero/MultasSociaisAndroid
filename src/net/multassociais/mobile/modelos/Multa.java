package net.multassociais.mobile.modelos;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class Multa {

    private String mFotoPequena;
    private String mDescricao;
    
    public Multa(String title) {
        super();
        mDescricao = title;
    }
    
    public Multa(JSONObject json) throws JSONException {
        mDescricao = json.getString("descricao");
        mFotoPequena = json.getString("foto_small");
    }
    
    public String getTitle() {
        return mDescricao;
    }
    
    public String getFotoPequena() {
        return mFotoPequena;
    }

    public static class MultasCollection extends ArrayList<Multa> {
        private static final long serialVersionUID = 5231569247900180309L;
        
    }
}
