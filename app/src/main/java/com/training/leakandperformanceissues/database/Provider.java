package com.training.leakandperformanceissues.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.training.leakandperformanceissues.database.table.WondersTable;

public class Provider extends ContentProvider {
    private static final String AUTHORITY = "com.training.leakandperformanceissues";
    private static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    private static UriMatcher URI_MATCHER;
    private Database database;

    // References of DIR and ID
    private static final int WONDER_DIR = 0;
    private static final int WONDER_ID = 1;

    // Configuration of UriMatcher
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        URI_MATCHER.addURI(AUTHORITY, WonderContent.CONTENT_PATH, WONDER_DIR);
        URI_MATCHER.addURI(AUTHORITY, WonderContent.CONTENT_PATH + "/#", WONDER_ID);
    }

    // Content URIs
    public static final Uri WONDER_CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, WonderContent.CONTENT_PATH);

    private static final class WonderContent implements BaseColumns {
        public static final String CONTENT_PATH = "wonder";
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase dbConnection = database.getWritableDatabase();

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {

                case WONDER_DIR:
                case WONDER_ID:
                    Long postId = dbConnection.insertOrThrow(WondersTable.TABLE_NAME, null, values);
                    Uri newPost = ContentUris.withAppendedId(WONDER_CONTENT_URI, postId);
                    getContext().getContentResolver().notifyChange(newPost, null);
                    dbConnection.setTransactionSuccessful();
                    return newPost;

                default:
                    throw new IllegalArgumentException("Unsupported URI: " + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }
    }

    @Override
    public boolean onCreate() {
        database = new Database(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        String table;
        switch (URI_MATCHER.match(uri)) {

            case WONDER_DIR:
            case WONDER_ID:
                table = WondersTable.TABLE_NAME;
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        SQLiteDatabase dbConnection = database.getWritableDatabase();
        int res = 0;
        for (ContentValues v : values) {
            try {
                dbConnection.beginTransaction();
                long id = dbConnection.insertWithOnConflict(table, null, v, SQLiteDatabase.CONFLICT_REPLACE);
                Log.i("LeakAndPerformance", "inserted into table: " + table + " id: " + id);

                dbConnection.yieldIfContendedSafely();
                dbConnection.setTransactionSuccessful();

                if (id != -1) {
                    res++;
                }

                if (res != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

            } finally {
                dbConnection.endTransaction();
            }
        }

        return res;
    }
}
