package com.intricare.test.Utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.intricare.test.Model.InviteListData;

@Database(entities = {InviteListData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContectInterface taskDao();
}