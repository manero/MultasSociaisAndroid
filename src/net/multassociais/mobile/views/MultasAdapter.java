package net.multassociais.mobile.views;

import net.multassociais.mobile.modelos.Multa;
import net.multassociais.mobile.modelos.Multa.MultasCollection;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MultasAdapter extends ArrayAdapter<Multa> {
    
    private LayoutInflater mInflater;

    public MultasAdapter(Context context, MultasCollection objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        mInflater = LayoutInflater.from(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView rowView = (TextView) convertView;
        if (rowView == null) {
            rowView = (TextView) mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        
        rowView.setText(getItem(position).getTitle());
        
        return rowView;
    }
    
}
