package com.daffa.kontaqu.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.daffa.kontaqu.dao.DaoAccess;
import com.daffa.kontaqu.model.Kontak;

@Database(entities = {Kontak.class}, version = 1, exportSchema = false)
public abstract class KontakDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
