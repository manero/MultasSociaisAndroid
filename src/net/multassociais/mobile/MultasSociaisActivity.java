package net.multassociais.mobile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class MultasSociaisActivity extends Activity {
	
	private static final int TAKE_PICTURE_REQUEST_CODE = 666;
	private static final int ACTIVITY_SELECT_IMAGE = 777;
	private String imageFilePath;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			ImageView thumbnailImageView = (ImageView) findViewById(R.id.thumbnail);
			switch (requestCode) {
			case TAKE_PICTURE_REQUEST_CODE:
				data.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(createImageFile()));
				thumbnailImageView.setImageBitmap((Bitmap) data.getExtras().get(
						"data"));
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
			}
		}
	}
	
	private File createImageFile() {
		try {
			File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "multassociais");
			storageDir.mkdirs();
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String imageFileName = "multa_" + timeStamp;
			File image = File.createTempFile(imageFileName, ".jpg", storageDir);
			imageFilePath = image.getAbsolutePath();
			return image;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.global.warning(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
    
    public void bateFoto(View view) {
    	Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST_CODE);
    }
    
    public void selecionaFotoDaGaleria(View view) {
    	Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    	startActivityForResult(i, ACTIVITY_SELECT_IMAGE); 
    }
    
    public void enviaMulta(View view) {
    	// Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://www.multassociais.net/api/new");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(11);
            nameValuePairs.add(new BasicNameValuePair("api_id", "908237202 "));
            nameValuePairs.add(new BasicNameValuePair("api_secret", "14feefc9725729307649b526bd83x11"));
            nameValuePairs.add(new BasicNameValuePair("multa_data_ocorrencia_1i", "2012"));
            nameValuePairs.add(new BasicNameValuePair("multa_data_ocorrencia_2i", "1"));
            nameValuePairs.add(new BasicNameValuePair("multa_data_ocorrencia_3i", "1"));
            nameValuePairs.add(new BasicNameValuePair("multa_data_ocorrencia_4i", "00"));
            nameValuePairs.add(new BasicNameValuePair("multa_data_ocorrencia_5i", "00"));
            nameValuePairs.add(new BasicNameValuePair("multa_placa", ""));
            nameValuePairs.add(new BasicNameValuePair("multa_foto", ""));
            nameValuePairs.add(new BasicNameValuePair("multa_video", ""));
            nameValuePairs.add(new BasicNameValuePair("multa_descricao", "ESTE � s� um teste do app multassociais mobile para android. por favor ignore. ainda vou arrepiar um lorem ipsum aqui: " + "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }
}