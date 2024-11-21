package com.example.tareakoko;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ContraseñasAdapter extends BaseAdapter {

    private Context context;
    private List<Contraseñas> listaContraseñas;

    // Constructor corregido
    public ContraseñasAdapter(Context context, List<Contraseñas> listaContraseñas) {
        this.context = context;
        this.listaContraseñas = listaContraseñas;
    }

    @Override
    public int getCount() {
        return listaContraseñas.size();
    }

    @Override
    public Object getItem(int position) {
        return listaContraseñas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Utilizar un ViewHolder para mejorar el rendimiento
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contrasena, parent, false);
            holder = new ViewHolder();
            holder.contraseñaTextView = convertView.findViewById(R.id.contraseñaTextView);
            holder.origenTextView = convertView.findViewById(R.id.origenTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Obtener la contraseña actual
        Contraseñas contraseña = listaContraseñas.get(position);

        // Vincular los datos
        holder.contraseñaTextView.setText(contraseña.getContraseña());
        holder.origenTextView.setText(contraseña.getNombreUsuario());

        return convertView;
    }

    // Clase interna para ViewHolder
    private static class ViewHolder {
        TextView contraseñaTextView;
        TextView origenTextView;
    }
}
