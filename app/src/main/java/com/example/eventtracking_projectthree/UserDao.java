package com.example.eventtracking_projectthree;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE username LIKE :name AND " +
            "password LIKE :pass LIMIT 1")
    User findByName(String name, String pass);

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}
