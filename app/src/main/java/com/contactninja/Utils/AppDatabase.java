package com.contactninja.Utils;

import android.annotation.SuppressLint;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.contactninja.Model.Contect_Db;
import com.contactninja.Model.InviteListData;


@Database(entities = {Contect_Db.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    @SuppressLint("UnknownNullness")
    public abstract ContectInterface taskDao();
}