package com.contactninja.Utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.contactninja.Model.InviteListData;


@Database(entities = {InviteListData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContectInterface taskDao();
}