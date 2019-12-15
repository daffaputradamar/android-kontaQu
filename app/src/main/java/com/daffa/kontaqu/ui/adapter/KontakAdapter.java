package com.daffa.kontaqu.ui.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daffa.kontaqu.R;
import com.daffa.kontaqu.model.Kontak;

import java.util.List;

public class KontakAdapter extends RecyclerView.Adapter<KontakAdapter.MyViewHolder> {

    List<Kontak> kontakList;
    private Context context;
    boolean isList;

    public KontakAdapter(List<Kontak> kontakList, Context context, boolean isList) {
        this.kontakList = kontakList;
        this.context = context;
        this.isList = isList;
    }

    @NonNull
    @Override
    public KontakAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View kontakView;
        if (isList) {
            kontakView = layoutInflater.inflate(R.layout.kontak_item, parent, false);
        } else {
            kontakView = layoutInflater.inflate(R.layout.kontak_item_grid, parent, false);
        }

        MyViewHolder viewHolder = new MyViewHolder(kontakView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KontakAdapter.MyViewHolder holder, final int position) {
        Kontak kontak = kontakList.get(position);

        holder.kontakName.setText(kontak.getNama());
        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getItem(position).getNoTelp()));
                context.startActivity(intent);
            }
        });
        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("vnd.android-dir/mms-sms");
                intent.putExtra("address", getItem(position).getNoTelp());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (kontakList != null) ? kontakList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView kontakName;
        ImageView phone;
        ImageView message;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            kontakName = itemView.findViewById(R.id.txtNama);
            phone = itemView.findViewById(R.id.btnPhone);
            message = itemView.findViewById(R.id.btnEmail);
        }
    }

    public Kontak getItem(int position) {
        return kontakList.get(position);
    }
}
