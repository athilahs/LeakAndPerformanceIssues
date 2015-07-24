package com.training.leakandperformanceissues.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.training.leakandperformanceissues.database.table.WondersTable;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "performance_issues.db";
    private static final int DATABASE_VERSION = 1;

    public Database(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public final void onCreate(final SQLiteDatabase db) {
        db.execSQL(WondersTable.SQL_CREATE);
    }

    @Override
    public final void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {}

}