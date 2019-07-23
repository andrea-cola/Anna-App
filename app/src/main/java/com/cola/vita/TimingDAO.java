package com.cola.vita;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TimingDAO {

    @Query("SELECT * FROM timing")
    List<Timing> getAll();

    @Insert
    void insert(Timing timing);

}
