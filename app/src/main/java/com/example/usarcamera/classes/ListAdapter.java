package com.example.usarcamera.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.usarcamera.R;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Remedio> {

    public ListAdapter (Context context,
                        ArrayList<Remedio> arrayList){
        super(context, R.layout.list_item_favoritos, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Remedio remedio = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_favoritos, parent, false
            );
        }

        TextView nomeRemedio = convertView.findViewById(R.id.txtNomeRemedio);
        TextView principioAtivo = convertView.findViewById(R.id.txtPrincipioAtivo);

        nomeRemedio.setText(remedio.getPrincipioAtivo());

        return super.getView(position, convertView, parent);
    }
}
