package com.dxa.android.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;

/**
 * OrmLiteSqliteOpenHelper的超类
 */

public class SuperSqliteOpenHelper extends OrmLiteSqliteOpenHelper {

    private Callback callback;

    private Context context;
    private String dbName;
    private int dbVersion;

    public SuperSqliteOpenHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
        setupConfig(context, dbName, dbVersion);
    }

    public SuperSqliteOpenHelper(Context context,
                                  String dbName, int dbVersion, int configFileId) {
        super(context, dbName, null, dbVersion, configFileId);
        setupConfig(context, dbName, dbVersion);
    }

    public SuperSqliteOpenHelper(Context context,
                                  String dbName, int dbVersion, File configFile) {
        super(context, dbName, null, dbVersion, configFile);
        setupConfig(context, dbName, dbVersion);
    }

    private void setupConfig(Context context, String dbName, int dbVersion) {
        this.context = context;
        this.dbName = dbName;
        this.dbVersion = dbVersion;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource source) {
        if (callback != null) {
            callback.onCreate(db, source);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource source,
                          int oldVersion, int newVersion) {
        if (callback != null) {
            callback.onUpgrade(db, source, oldVersion, newVersion);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        if (callback != null) {
            callback.onDowngrade(db, oldVersion, newVersion);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (callback != null) {
            callback.onOpen(db);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        if (callback != null) {
            callback.onConfigure(db);
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Context getContext() {
        return context;
    }

    public String getDatabaseName() {
        return dbName;
    }

    public int getDatabaseVersion() {
        return dbVersion;
    }

    /**
     * 回调
     */
    public interface Callback {

        /**
         * 创建时
         *
         * @param db     数据库对象
         * @param source 数据库的连接源
         */
        void onCreate(SQLiteDatabase db, ConnectionSource source);

        /**
         * 升级时
         *
         * @param db         数据库对象
         * @param source     数据库的连接源
         * @param oldVersion 老版本
         * @param newVersion 新版本
         */
        void onUpgrade(SQLiteDatabase db, ConnectionSource source, int oldVersion, int newVersion);

        /**
         * 数据库降低版本时
         *
         * @param db         数据库对象
         * @param oldVersion 老版本
         * @param newVersion 新版本
         */
        void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion);


        /**
         * 当数据打开时
         *
         * @param db 数据库对象
         */
        void onOpen(SQLiteDatabase db);

        /**
         * 配置时
         */
        void onConfigure(SQLiteDatabase db);

    }
}
