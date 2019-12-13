package com.daffa.kontaqu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daffa.kontaqu.R;
import com.daffa.kontaqu.model.Kontak;

import java.util.List;

public class KontakAdapter extends RecyclerView.Adapter<KontakAdapter.MyViewHolder> {

    List<Kontak> kontakList;

    public KontakAdapter(List<Kontak> kontakList) {
        this.kontakList = kontakList;
    }

    @NonNull
    @Override
    public KontakAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View kontakView = layoutInflater.inflate(R.layout.kontak_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(kontakView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KontakAdapter.MyViewHolder holder, int position) {
        Kontak kontak = kontakList.get(position);

        holder.kontakName.setText(kontak.getNama());
    }

    @Override
    public int getItemCount() {
        return (kontakList != null) ? kontakList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView kontakName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            kontakName = itemView.findViewById(R.id.txtNama);
        }
    }

    public Kontak getItem(int position) {
        return kontakList.get(position);
    }
}
