package com.joshuamccluskey.taskmaster.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.joshuamccluskey.taskmaster.dao.TaskDao;
import com.joshuamccluskey.taskmaster.model.Task;

@Database(entities = {Task.class}, version = 1)
@TypeConverters({TaskMasterDatabaseConverters.class})
public abstract class TaskMasterDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();


}
