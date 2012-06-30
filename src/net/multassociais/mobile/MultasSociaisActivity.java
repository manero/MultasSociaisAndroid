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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
				data.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(createImageFile())); //imageFilePath defined inside createImageFile()
				thumbnailImageView.setImageBitmap((Bitmap) data.getExtras().get("data"));
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
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(11);
        nameValuePairs.add(new BasicNameValuePair("api_id", "908237202 "));
        nameValuePairs.add(new BasicNameValuePair("api_secret", "14feefc9725729307649b526bd83x11"));
        nameValuePairs.add(new BasicNameValuePair("multa_data_ocorrencia_1i", "2012"));
        nameValuePairs.add(new BasicNameValuePair("multa_data_ocorrencia_2i", "1"));
        nameValuePairs.add(new BasicNameValuePair("multa_data_ocorrencia_3i", "1"));
        nameValuePairs.add(new BasicNameValuePair("multa_data_ocorrencia_4i", "00"));
        nameValuePairs.add(new BasicNameValuePair("multa_data_ocorrencia_5i", "00"));
        nameValuePairs.add(new BasicNameValuePair("multa_placa", "123"));
        nameValuePairs.add(new BasicNameValuePair("multa_foto", imageFilePath));
        nameValuePairs.add(new BasicNameValuePair("multa_video", "123"));
        nameValuePairs.add(new BasicNameValuePair("multa_descricao", "ESTE é só um teste do app multassociais mobile para android. por favor ignore. ainda vou arrepiar um lorem ipsum aqui: " + "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));

        doTheHttpPost(nameValuePairs);
    }
   
    
    public void doTheHttpPost(List<NameValuePair> nameValuePairs) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost("http://www.multassociais.net/");

        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            for(int index=0; index < nameValuePairs.size(); index++) {
                if(nameValuePairs.get(index).getName().equalsIgnoreCase("multa_foto")) {
                    // If the key equals to "image", we use FileBody to transfer the data
                    entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File (nameValuePairs.get(index).getValue())));
                } else {
                    // Normal string data
                    entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
                }
            }

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            Log.i("HTTPRESPONSE", EntityUtils.toString(response.getEntity()));
            Log.i("HTTPRESPONSE", "------");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}