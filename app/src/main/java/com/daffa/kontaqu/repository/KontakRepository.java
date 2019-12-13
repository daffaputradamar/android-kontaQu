package com.daffa.kontaqu.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.daffa.kontaqu.db.KontakDatabase;
import com.daffa.kontaqu.model.Kontak;

import java.util.List;

public class KontakRepository {
    public static final String DB_NAME = "db_kontak";

    private KontakDatabase kontakDatabase;

    public KontakRepository(Context context) {
        kontakDatabase = Room.databaseBuilder(context, KontakDatabase.class, DB_NAME).build();
    }

    public void insertKontak(String nama, String noTelp, String email) {
        Kontak kontak = new Kontak();
        kontak.setNama(nama);
        kontak.setNoTelp(noTelp);
        kontak.setEmail(email);

        insertKontak(kontak);
    }

    public void insertKontak(final Kontak kontak) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                kontakDatabase.daoAccess().insertKontak(kontak);
                return null;
            }
        }.execute();
    }

    public void updateKontak(final Kontak kontak) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                kontakDatabase.daoAccess().updateKontak(kontak);
                return null;
            }
        }.execute();
    }

    public void deleteKontak(final int id) {
        final LiveData<Kontak> kontak = getKontak(id);
        if(kontak != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    kontakDatabase.daoAccess().deleteKontak(kontak.getValue());
                    return null;
                }
            }.execute();
        }
    }

    public void deleteKontak(final Kontak kontak) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                kontakDatabase.daoAccess().deleteKontak(kontak);
                return null;
            }
        }.execute();
    }

    public LiveData<Kontak> getKontak(int id) {
        return kontakDatabase.daoAccess().getKontak(id);
    }

    public LiveData<List<Kontak>> getKontaks() {
        return kontakDatabase.daoAccess().fetchAllKontak();
    }
}
