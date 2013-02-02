package net.multassociais.mobile;

import java.util.ArrayList;
import java.util.Random;

import net.multassociais.mobile.views.MultasAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ListaMultasActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_multas_activity);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        listaMultas(pegaMultas());
    }

    private void listaMultas(ArrayList<String> multas) {
        MultasAdapter multasAdapter = new MultasAdapter(getBaseContext(), multas);
        ((ListView) findViewById(R.id.lista_multas)).setAdapter(multasAdapter);
    }
    
    private ArrayList<String> pegaMultas() {
        ArrayList<String> itens = new ArrayList<String>();
        
        for (int i = 0; i < new Random().nextInt(50); i ++) {
            itens.add("Item " + i + " // " + new Random().nextInt(100));
        }
        
        return itens;
    }
}
