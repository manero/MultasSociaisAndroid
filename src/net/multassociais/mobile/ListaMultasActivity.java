package net.multassociais.mobile;

import net.multassociais.mobile.comunicacoes.RequisitaMultasTask;
import net.multassociais.mobile.comunicacoes.RequisitaMultasTask.MultasListener;
import net.multassociais.mobile.modelos.Multa.MultasCollection;
import net.multassociais.mobile.views.MultasAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ListaMultasActivity extends Activity implements MultasListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_multas_activity);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        pegaMultas();
    }

    private void listaMultas(MultasCollection multas) {
        MultasAdapter multasAdapter = new MultasAdapter(getBaseContext(), multas);
        ((ListView) findViewById(R.id.lista_multas)).setAdapter(multasAdapter);
    }
    
    private void pegaMultas() {
        RequisitaMultasTask requerente = new RequisitaMultasTask(this, this);
        requerente.execute((Void[])null);
    }

    @Override
    public void onMultasLoaded(MultasCollection multas) {
        listaMultas(multas);
    }
}
