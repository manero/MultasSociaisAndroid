package net.multassociais.mobile.views;

import net.multassociais.mobile.R;
import net.multassociais.mobile.modelos.Multa;
import net.multassociais.mobile.modelos.Multa.MultasCollection;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

public class MultasAdapter extends ArrayAdapter<Multa> {
    
    private AQuery mAquery;
    private LayoutInflater mInflater;

    public MultasAdapter(Context context, MultasCollection objects) {
        super(context, R.layout.multa_item_view, objects);
        
        mInflater = LayoutInflater.from(getContext());
        mAquery = new AQuery(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.multa_item_view, parent, false);
        }
        
        Multa multa = getItem(position);
        
        TextView descView = (TextView) convertView.findViewById(R.id.descricao);
        descView.setText(multa.getTitle());
        
        ImageView imageView = (ImageView) convertView.findViewById(R.id.thumb);
        mAquery.id(imageView).image(multa.getFotoPequena());
//        imageView.setImageURI(Uri.parse(multa.getFotoPequena()));
        
        return convertView;
    }
    
}
