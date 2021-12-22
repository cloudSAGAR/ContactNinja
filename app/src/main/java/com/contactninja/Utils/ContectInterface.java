package com.contactninja.Utils;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.contactninja.Model.Contect_Db;
import com.contactninja.Model.InviteListData;

import java.util.List;

@Dao
public interface ContectInterface {




        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAllcontect(List<Contect_Db> Contect_list);

        @Insert
        void insert(Contect_Db contect);

        @Query("SELECT * FROM  Contect_Db GROUP BY id1")
        List<Contect_Db> getvalue();

        @Query("SELECT * FROM Contect_Db WHERE first_name =:first_name OR last_name =:last_name AND email_number =:phone")
        List<Contect_Db> getSameValue(String first_name,String last_name,String phone);

        @Query("DELETE FROM Contect_Db")
        void RemoveData();

        @Query("DELETE FROM Contect_Db WHERE id NOT IN (SELECT MIN(id) FROM Contect_Db GROUP BY email_number, first_name)")
        void deleteDuplicates();

      /*  @Query("SELECT * FROM InviteListData ")
        List<InviteListData> getvalue();

        @Query("SELECT * FROM InviteListData WHERE userPhoneNumber =:taskId")
        List<InviteListData> getTask(String taskId);

        @Insert
        void insert(InviteListData contect);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAllcontect(List<InviteListData> contect);

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
        List<InviteListData> getvalue1();*/

}

