package com.dxa.android.ormlite;

import android.database.sqlite.SQLiteDatabase;

import com.dxa.android.logger.DLogger;
import com.dxa.android.logger.LogLevel;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


/**
 * 数据库的回调
 */

public class OrmLiteOpenCallback implements SuperSqliteOpenHelper.Callback, OrmLiteDbHelper.SQLExceptionCallback {

    protected String tag = getClass().getSimpleName();
    protected final DLogger logger = new DLogger(tag + " ==> ");

    public void setDebug(boolean debug) {
        if (debug) {
            logger.setLevel(LogLevel.VERBOSE);
        } else {
            logger.setLevel(LogLevel.NONE);
        }
    }

    @Override
    public void onThrowSQLException(SQLException e) {
        e.printStackTrace();
    }

    public String getDatabaseInfo(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("MaximumSize: ").append(db.getMaximumSize());
        builder.append(", PageSize: ").append(db.getPageSize());
        builder.append(", Version: ").append(db.getVersion());
        builder.append(", Path: ").append(db.getPath());
        builder.append(", Open: ").append(db.isOpen());
        builder.append(", ReadOnly: ").append(db.isReadOnly());
        return builder.toString();
    }

    /**
     * 创建表
     *
     * @param source 数据源
     * @param table  表的Class对象
     * @param <T>    类型
     */
    public <T> void createTable(ConnectionSource source, Class<T> table) {
        try {
            // 创建用户信息表
            int t = TableUtils.createTable(source, table);
            DatabaseTable annotation = table.getAnnotation(DatabaseTable.class);
            String tableName = "";
            if (annotation != null) {
                tableName = annotation.tableName();
            }
            tableName = (!"".equals(tableName.trim()) ? tableName : table.getSimpleName());
            logger.i(t, " - 创建表：", tableName);
        } catch (SQLException e) {
            onThrowSQLException(e);
        }
    }

    /**
     * 删除表
     *
     * @param source       数据源
     * @param table        表
     * @param ignoreErrors 是否忽略错误
     * @param <T>          类型
     */
    public <T> void dropTable(ConnectionSource source, Class<T> table, boolean ignoreErrors) {
        try {
            int t = TableUtils.dropTable(source, table, ignoreErrors);
            DatabaseTable annotation = table.getAnnotation(DatabaseTable.class);
            String tableName = "";
            if (annotation != null) {
                tableName = annotation.tableName();
            }
            tableName = (!"".equals(tableName.trim()) ? tableName : table.getSimpleName());
            logger.i(t, " - 删除表：", tableName);
        } catch (SQLException e) {
            onThrowSQLException(e);
        }
    }

    /**
     * 删除数据库
     *
     * @param db     数据库
     * @param dbName 数据库名称
     */
    public void dropDatabase(SQLiteDatabase db, String dbName) {
        db.execSQL("DROP DATABASE " + dbName);
    }

    /**
     * 当创建数据库
     *
     * @param db     数据库对象
     * @param source 数据源
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource source) {
        String info = getDatabaseInfo(db);
        logger.i("onCreate ==> ", info);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource source, int oldVersion, int newVersion) {
        String info = getDatabaseInfo(db);
        logger.i("onDowngrade ==> 老版本: ", oldVersion, "， 新版本: ", newVersion, info);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String info = getDatabaseInfo(db);
        logger.i("onDowngrade ==> 老版本: ", oldVersion, "， 新版本: ", newVersion, info);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        String info = getDatabaseInfo(db);
        logger.i("onOpen ==> ", info);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        String info = getDatabaseInfo(db);
        logger.i("onConfigure ==> ", info);
    }
}
