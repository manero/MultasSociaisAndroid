package net.multassociais.mobile.comunicacoes;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class EnviaMultaTask extends AsyncTask<MultipartEntity, Void, Integer> {
    private Activity mActivity;
	private ProgressDialog dialog;

    public EnviaMultaTask(Activity activity) {
        super();
        mActivity = activity;
        dialog = new ProgressDialog(mActivity);
    }
	
	@Override
	protected void onPreExecute() {
	    super.onPreExecute();
	    
		dialog.setMessage("Enviando...");
		dialog.show();
	}

	@Override
	protected Integer doInBackground(MultipartEntity... entity) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost("http://testes.multassociais.net/api");

		httpPost.setEntity(entity[0]);
		HttpResponse response;
		try {
			response = httpClient.execute(httpPost, localContext);
			int statusCode = response.getStatusLine().getStatusCode();
			return statusCode;
			
		} catch (ClientProtocolException e) {
			showToast("Falhou");
			cancel(true);
			return null;
		} catch (IOException e) {
			showToast("Falhou");
			cancel(true);
			return null;
		}
	}
	
	@Override
	protected void onCancelled() {
		dialog.dismiss();
		super.onCancelled();
	}
	
	@Override
	protected void onPostExecute(Integer statusCode) {
		super.onPostExecute(statusCode);
		
		if (statusCode >= 200 && statusCode < 300) {
			showToast("Sucesso");
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
}