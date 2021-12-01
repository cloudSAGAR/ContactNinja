package com.contactninja.Utils;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.contactninja.Model.InviteListData;

import java.util.List;

@Dao
public interface ContectInterface {

        @Query("SELECT * FROM InviteListData ")
        List<InviteListData> getvalue();

        @Query("SELECT * FROM InviteListData WHERE userPhoneNumber =:taskId")
        List<InviteListData> getTask(String taskId);

        @Insert
        void insert(InviteListData contect);

        @Delete
        void delete(InviteListData contect);

        @Query("Delete FROM InviteListData WHERE userPhoneNumber =:Mobile")
        void DeleteData(String Mobile);


        @Update
        void update(InviteListData contect);




        @Query("DELETE FROM InviteListData")
        void RemoveData();

        @Query("UPDATE InviteListData SET userName =:userName1 , userPhoneNumber =:number1 , flag =:flag1 WHERE userName =:userName1 OR userPhoneNumber =:number1 ")
        void updatevalue(String userName1,String number1,String flag1);


        @Query("DELETE FROM InviteListData WHERE id NOT IN (SELECT MIN(id) FROM InviteListData GROUP BY userPhoneNumber, userName)")
        void deleteDuplicates();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(List<InviteListData> values);



        @Query("SELECT * FROM InviteListData WHERE userPhoneNumber =:taskId AND userName =:username")
        List<InviteListData> getTaskUpdate(String taskId,String username);

        @Query("SELECT * FROM InviteListData WHERE userPhoneNumber =:taskId OR userName =:username")
        List<InviteListData> getTaskUpdate1(String taskId,String username);



        @Query("SELECT * FROM InviteListData ORDER BY userName")
        List<InviteListData> getvalue1();

}

