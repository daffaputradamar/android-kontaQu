package com.daffa.kontaqu.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.daffa.kontaqu.model.Kontak;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    Long insertKontak(Kontak kontak);

    @Query("SELECT * FROM Kontak ORDER BY nama asc")
    LiveData<List<Kontak>> fetchAllKontak();

    @Query("SELECT * FROM Kontak WHERE id =:taskId")
    LiveData<Kontak> getKontak(int taskId);

    @Update
    void updateKontak(Kontak note);

    @Delete
    void deleteKontak(Kontak note);


}
