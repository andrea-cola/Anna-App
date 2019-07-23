package com.cola.vita;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Timing {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "id_set")
    public int id_set;

    @ColumnInfo(name = "time")
    public long time;

    @ColumnInfo(name = "skipped")
    public boolean skipped;

}
