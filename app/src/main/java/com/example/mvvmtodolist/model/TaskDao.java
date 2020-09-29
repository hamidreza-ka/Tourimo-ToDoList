package com.example.mvvmtodolist.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;


@Dao
public interface TaskDao {

    @Insert
    Long insert(Task task);

    @Delete
    void delete(Task task);

    @Update
    int update(Task task);

    @Query("SELECT * FROM tbl_tasks ORDER BY priority ASC")
    Observable<List<Task>> getTasks();

    @Query("DELETE FROM tbl_tasks")
    void deleteAll();

    @Query("SELECT * FROM tbl_tasks WHERE title LIKE '%' || :query || '%' ORDER BY priority ASC")
    Maybe<List<Task>> search(String query);
}
