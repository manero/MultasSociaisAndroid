package net.multassociais.mobile.comunicacoes;

import java.io.IOException;

import net.multassociais.mobile.modelos.Multa;
import net.multassociais.mobile.modelos.Multa.MultasCollection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class RequisitaMultasTask extends AsyncTask<Void, Void, MultasCollection> {

    private Activity mActivity;
    private ProgressDialog dialog;
    
    private MultasListener mListener;

    public RequisitaMultasTask(Activity activity, MultasListener listener) {
        super();
        mActivity = activity;
        mListener = listener;
        dialog = new ProgressDialog(mActivity);
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        
        dialog.setMessage("Enviando...");
        dialog.show();
    }

    @Override
    protected MultasCollection doInBackground(Void... nada) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet("http://testes.multassociais.net/multas.json");

        HttpResponse response;
        try {
            response = httpClient.execute(httpGet, localContext);
            int statusCode = response.getStatusLine().getStatusCode();
            
            if (200 == statusCode) {
                String jsonText = EntityUtils.toString(response.getEntity());
                return parse(transformeRespostaEmJson(jsonText));
            }
            else {
                return null;
            }
            
        } catch (ClientProtocolException e) {
            showToast("Falhou");
            cancel(true);
            return null;
        } catch (IOException e) {
            showToast("Falhou");
            cancel(true);
            return null;
        } catch (JSONException e) {
            showToast("Falhou");
            cancel(true);
            return null;
        }
        
    }
    private JSONArray transformeRespostaEmJson(String resposta) throws JSONException {
        return new JSONArray(resposta);
    }
    
    private MultasCollection parse(JSONArray json) throws JSONException {
        MultasCollection multas = new MultasCollection();
        
        for (int i = 0; i < json.length(); i ++) {
            multas.add(new Multa(json.getJSONObject(i)));
        }
        
        return multas;
    }

    @Override
    protected void onCancelled() {
        dialog.dismiss();
        super.onCancelled();
    }
    
    @Override
    protected void onPostExecute(MultasCollection multas) {
        super.onPostExecute(multas);
        
        if (multas != null) {
            showToast("Sucesso");
            mListener.onMultasLoaded(multas);
        }
        else {
            showToast("Falhou!");
        }
        dialog.dismiss();
    }

    private void showToast(String string) {
        Toast feedback;
        feedback = Toast.makeText(mActivity, string, Toast.LENGTH_SHORT);
        feedback.show();
    }
    
    public interface MultasListener {
        public void onMultasLoaded(MultasCollection multas);
    }
}
