package com.intricare.test.Utils;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.intricare.test.Model.InviteListData;

import java.util.List;

import okhttp3.internal.concurrent.Task;

@Dao
public interface ContectInterface {

        @Query("SELECT * FROM InviteListData ")
        List<InviteListData> getvalue();

        @Query("SELECT * FROM InviteListData WHERE userName =:taskId")
        List<InviteListData> getTask(String taskId);

        @Insert
        void insert(InviteListData contect);

        @Delete
        void delete(InviteListData contect);

        @Update
        void update(InviteListData contect);




        @Query("DELETE FROM InviteListData")
        void RemoveData();

        @Query("UPDATE InviteListData SET userName=:userName &  userPhoneNumber=:number & flag='1' WHERE userName != :userName & userPhoneNumber != :number ")
        void updatevalue(String userName,String number);
    }

