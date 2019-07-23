package com.cola.vita;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Timing.class}, version = 5)
public abstract class VitaDatabase extends RoomDatabase {

    public abstract TimingDAO timingDAO();

}
