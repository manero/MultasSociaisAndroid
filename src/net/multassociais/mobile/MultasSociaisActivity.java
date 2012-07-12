package net.multassociais.mobile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MultasSociaisActivity extends Activity {

	private static final int TAKE_PICTURE_REQUEST_CODE = 666; // \,,/
	private static final int ACTIVITY_SELECT_IMAGE = 777;
	private String imageFilePath;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		imageFilePath = "";
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			ImageView thumbnailImageView = (ImageView) findViewById(R.id.thumbnail);
			switch (requestCode) {
			case TAKE_PICTURE_REQUEST_CODE:
				File f = new File(imageFilePath);
				if (f.exists()) {
					try {
						Uri contentUri = Uri.fromFile(f);
						thumbnailImageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), contentUri));
				        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				        mediaScanIntent.setData(contentUri);
				        this.sendBroadcast(mediaScanIntent);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						imageFilePath = "";
						thumbnailImageView.setImageBitmap(null);
						e.printStackTrace();
					}
				}
				break;
			case ACTIVITY_SELECT_IMAGE:
				Uri selectedImage = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex);
				cursor.close();
				thumbnailImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
				imageFilePath = filePath;
			}
		}
	}

	private File createImageFile() {
		File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MultasSociais");
		storageDir.mkdirs();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "multa_" + timeStamp;
		File image = new File(storageDir, imageFileName + ".jpg");
		imageFilePath = image.getAbsolutePath();
		return image;
	}

	public void bateFoto(View view) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(createImageFile())); //imageFilePath defined inside createImageFile()
		startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST_CODE);
	}

	public void selecionaFotoDaGaleria(View view) {
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, ACTIVITY_SELECT_IMAGE); 
	}

	// TODO: catch exception
	public void enviaMulta(View view) throws IOException {
		//verify if there's an image to be sent
		if (imageFilePath.equals("")) {
			Toast feedback;
			feedback = Toast.makeText(MultasSociaisActivity.this, "Selecione uma imagem ou use a câmera!", Toast.LENGTH_LONG);
			feedback.show();
			return;
		}
		
		File f = new File(imageFilePath);
		Date modifiedDate = new Date(f.lastModified());
		String year = new SimpleDateFormat("yyyy").format(modifiedDate);
		String month = new SimpleDateFormat("MM").format(modifiedDate);
		String day = new SimpleDateFormat("dd").format(modifiedDate);
		String hour = new SimpleDateFormat("HH").format(modifiedDate);
		String minute = new SimpleDateFormat("mm").format(modifiedDate);
		
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		entity.addPart("api_id", new StringBody("908237202"));
		entity.addPart("api_secret", new StringBody("14feefc9725729307649b526bd83x11"));
		entity.addPart("multa[data_ocorrencia(1i)]", new StringBody(year));
		entity.addPart("multa[data_ocorrencia(2i)]", new StringBody(month));
		entity.addPart("multa[data_ocorrencia(3i)]", new StringBody(day));
		entity.addPart("multa[data_ocorrencia(4i)]", new StringBody(hour));
		entity.addPart("multa[data_ocorrencia(5i)]", new StringBody(minute));
		entity.addPart("multa[placa]", new StringBody("123"));
		entity.addPart("multa[foto]", new FileBody(f));
		entity.addPart("multa[video]", new StringBody(""));
		entity.addPart("multa[descricao]", new StringBody("este eh soh um teste do android app. por favor ignore. sem lorem ipsum"));

		WebServiceCallTask task = new WebServiceCallTask();
		task.execute(entity);
	}

	private class WebServiceCallTask extends AsyncTask<MultipartEntity, Void, Integer> {
		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(MultasSociaisActivity.this);
			
			super.onPreExecute();
			dialog.setMessage("Enviando...");
			dialog.show();
		}

		@Override
		protected Integer doInBackground(MultipartEntity... entity) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost("http://msociais-qa.herokuapp.com/api");

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
			feedback = Toast.makeText(MultasSociaisActivity.this, string, Toast.LENGTH_SHORT);
			feedback.show();
		}
		
	}

}