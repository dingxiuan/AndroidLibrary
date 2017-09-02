package com.dxa.android.ormlite;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * App的数据库操作类
 */

public class OrmLiteDbHelper<Helper extends OrmLiteSqliteOpenHelper> {

    /**
     * 对象锁
     */
    private final Object lock = new Object();

    /**
     * 操作数据库的OrmLiteSqlite帮助类
     */
    private final ConcurrentHashMap<String, Dao> daoMap;
    private Helper helper;

    private SQLExceptionCallback callback;

    public OrmLiteDbHelper() {
        this.daoMap = new ConcurrentHashMap<>();
    }

    public OrmLiteDbHelper(Helper helper) {
        this();
        if (helper == null)
            throw new NullPointerException("OrmLiteSqliteOpenHelper对象不能为 null !");
        this.helper = helper;
    }

    public OrmLiteDbHelper(Helper helper, SQLExceptionCallback callback) {
        this(helper);
        this.callback = callback;
    }

    /**
     * 初始化Helper
     */
    public void initHelper(Helper h) {
        if (h == null)
            throw new NullPointerException("OrmLiteSqliteOpenHelper对象不能为 null !");

        this.helper = h;
    }

    /**
     * 添加异常处理的回调
     */
    public void addSQLExceptionCallback(SQLExceptionCallback callback) {
        this.callback = callback;
    }

    /**
     * 处理异常
     */
    private void handleSQLException(SQLException e) {
        if (callback != null) {
            callback.onThrowSQLException(e);
        } else {
            e.printStackTrace();
        }
    }

    /**
     * 获取OrmLiteSqliteOpenHelper对象
     *
     * @return 返回当前的OrmLiteSqliteOpenHelper对象
     */
    public Helper getOpenHelper() {
        return helper;
    }

    /**
     * 获取Dao的Map集合
     */
    public Map<String, Dao> getDaoMap() {
        return new HashMap<>(daoMap);
    }

    /**
     * 获取Dao进行操作
     *
     * @param clazz Dao的类型
     * @param <T>   类型
     * @return 返回对应类型的Dao
     */
    @SuppressWarnings("unchecked")
    public <T> Dao<T, ?> getDao(Class<T> clazz) throws SQLException {
        if (clazz == null) return null;

        String className = clazz.getName();
        Dao<T, ?> dao;
        synchronized (lock) {
            dao = daoMap.get(className);
            if (dao == null) {
                dao = helper.getDao(clazz);
                daoMap.put(className, dao);
            }
        }
        return dao;
    }

    /**
     * 获取Dao进行操作
     *
     * @param clazz Dao的类型
     * @param <T>   类型
     * @return 返回对应类型的Dao
     */
    @SuppressWarnings("unchecked")
    public <T> Dao<T, ?> getDaoNoSQLException(Class<T> clazz) {
        if (clazz == null) return null;

        String className = clazz.getName();
        Dao<T, ?> dao;
        synchronized (lock) {
            dao = daoMap.get(className);
            if (dao == null) {
                try {
                    dao = helper.getDao(clazz);
                    daoMap.put(className, dao);
                } catch (SQLException e) {
                    handleSQLException(e);
                }
            }
        }
        return dao;
    }


    /**
     * 插入对一条记录
     *
     * @param t   对象
     * @param <T> 类型
     */
    public <T> T insert(T t) {
        Class<T> clazz = getTofClass(t);
        try {
            Dao<T, ?> dao = getDao(clazz);
            // 如果不存在就插入一条记录
            return dao != null ? dao.createIfNotExists(t) : null;
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    /**
     * 插入一个集合的记录
     *
     * @param c     集合对象
     * @param clazz 插入的数据表对应的类
     * @param <T>   类型
     * @param <C>   集合类型
     * @return 数据库中更新的行数
     */
    public <T, C extends Collection<T>> int insert(C c, Class<T> clazz) {
        try {
            Dao<T, ?> dao = getDao(clazz);
            return dao != null ? dao.create(c) : 0;
        } catch (SQLException e) {
            handleSQLException(e);
            return 0;
        }
    }

    /**
     * 更新记录
     *
     * @param t   将更新记录的对象
     * @param <T> 类型
     * @return 几条记录被更新
     */
    public <T> int update(T t) {
        try {
            Class<T> clazz = getTofClass(t);
            Dao<T, ?> dao = getDao(clazz);
            return dao != null ? dao.update(t) : 0;
        } catch (SQLException e) {
            handleSQLException(e);
            return 0;
        }
    }

    /**
     * 更新一整个集合
     *
     * @param c     集合
     * @param clazz 更新的数据表对应的类
     * @param <T>   类型
     * @param <C>   集合类型
     * @return 返回多少条记录被更新
     */
    public <T, C extends Collection<T>> int update(C c, Class<T> clazz) {
        int count = 0;
        try {
            Dao<T, ?> dao = getDao(clazz);
            if (dao != null && c != null && !c.isEmpty()) {
                for (T t : c) {
                    int update = dao.update(t);
                    count += update;
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return count;
    }

    /**
     * 更新一整个集合
     *
     * @param list List集合
     * @param <T>  类型
     * @return 返回多少条记录被更新
     */
    public <T> int update(List<T> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }

        T t = list.get(0);
        Class<T> clazz = getTofClass(t);
        return update(list, clazz);
    }

    /**
     * 插入或更新
     *
     * @param t 将更新记录的对象
     * @return 返回 CreateOrUpdateStatus 对象或 null 值
     */
    public <T> CreateOrUpdateStatus insertOrUpdate(T t) {
        try {
            Class<T> clazz = getTofClass(t);
            Dao<T, ?> dao = getDao(clazz);
            return dao != null ? dao.createOrUpdate(t) : null;
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }


    /**
     * 删除记录
     *
     * @param t   记录对象
     * @param <T> 类型
     * @return 如果删除成功返回 1 ，否则返回 0
     */
    public <T> int delete(T t) {
        try {
            Class<T> clazz = getTofClass(t);
            Dao<T, ?> dao = getDao(clazz);
            return dao != null ? dao.delete(t) : 0;
        } catch (SQLException e) {
            handleSQLException(e);
            return 0;
        }
    }

    /**
     * 删除一个集合的记录
     *
     * @param c     集合对象
     * @param clazz 删除的数据表对应的类
     * @param <T>   类型
     * @param <C>   集合类型
     * @return 返回被删除的条数
     */
    public <T, C extends Collection<T>> int delete(C c, Class<T> clazz) {
        try {
            Dao<T, ?> dao = getDao(clazz);
            return dao != null ? dao.delete(c) : 0;
        } catch (SQLException e) {
            handleSQLException(e);
            return 0;
        }
    }

    /**
     * 删除集合
     *
     * @param list List集合
     * @param <T>  类型
     * @return 删除的数量
     */
    public <T> int delete(List<T> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        T t = list.get(0);
        Class<T> clazz = getTofClass(t);
        return delete(list, clazz);
    }

    /**
     * 通过条件查询
     *
     * @param clazz     查询的数据表对应的类
     * @param fieldName 表中的列
     * @param value     列对应的值
     * @param <T>       类型
     * @return 返回查询到的所有对象的List集合
     */
    public <T> List<T> find(Class<T> clazz, String fieldName, Object value) {
        List<T> list;
        try {
            Dao<T, ?> dao = getDao(clazz);
            if (dao != null) {
                list = dao.queryForEq(fieldName, value);
            } else {
                list = new ArrayList<>();
            }
        } catch (SQLException e) {
            handleSQLException(e);
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 通过条件查询
     *
     * @param clazz      查询的数据表对应的类
     * @param fieldValue FieldValue对象，包含FieldName和Value
     * @param <T>        类型
     * @return 返回查询到的所有对象的List集合
     */
    public <T> List<T> find(Class<T> clazz, FieldValue fieldValue) {
        return fieldValue != null ?
                find(clazz, fieldValue.getField(), fieldValue.getValue()) : new ArrayList<T>();
    }

    /**
     * 通过条件查询
     *
     * @param clazz       查询的数据表对应的类
     * @param fieldValues FieldValue对象，包含FieldName和Value
     * @param <T>         类型
     * @return 返回查询到的所有对象的List集合
     */
    public <T> List<T> find(Class<T> clazz, FieldValue... fieldValues) {
        List<T> list;
        try {
            Where<T, ?> where = getWhere(clazz);
            if (where != null) {
                list = where(where, fieldValues).query();
            } else {
                list = new ArrayList<>();
            }
        } catch (SQLException e) {
            handleSQLException(e);
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 查询第一个满足的记录
     *
     * @param clazz     查询的数据表对应的类
     * @param fieldName 表中的列
     * @param value     列对应的值
     * @param <T>       类型
     * @return 返回满足的第一个记录
     */
    public <T> T findFirst(Class<T> clazz, String fieldName, Object value) {
        T t = null;
        try {
            Dao<T, ?> dao = getDao(clazz);
            if (dao != null) {
                t = dao.queryBuilder().where().eq(fieldName, value).queryForFirst();
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return t;
    }

    /**
     * 查询第一个满足的记录
     *
     * @param clazz 查询的数据表对应的类
     * @param fv    FieldValue对象，包含FieldName和Value
     * @param <T>   类型
     * @return 返回满足的第一个记录
     */
    public <T> T findFirst(Class<T> clazz, FieldValue fv) {
        return fv != null ? findFirst(clazz, fv.getField(), fv.getValue()) : null;
    }

    /**
     * 查询全部
     *
     * @param clazz 查询的数据表对应的类
     * @return 返回表中全部的记录
     */
    public <T> List<T> findAll(Class<T> clazz) {
        try {
            Dao<T, ?> dao = getDao(clazz);
            return dao != null ? dao.queryForAll() : new ArrayList<T>();
        } catch (SQLException e) {
            handleSQLException(e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取Where
     *
     * @param clazz 查询的数据表对应的类
     * @param <T>   类型
     * @return 返回Where对象或 null
     */
    public <T> Where<T, ?> getWhere(Class<T> clazz) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao != null ? dao.queryBuilder().where() : null;
    }

    /**
     * 拼接Where
     *
     * @param where       Where对象
     * @param fieldValues 查询条件
     * @param <T>         Where查询类型
     * @return 返回拼接好的Where对象
     */
    public <T> Where<T, ?> where(Where<T, ?> where, FieldValue... fieldValues) throws SQLException {
        if (fieldValues != null && fieldValues.length > 0) {
            FieldValue fv = fieldValues[0];
            where.eq(fv.getField(), fv.getValue());
            for (int i = 1; i < fieldValues.length; i++) {
                fv = fieldValues[i];
                where.and();
                where.eq(fv.getField(), fv.getValue());
            }
        }
        return where;
    }

    /**
     * 清理表
     *
     * @param clazz 查询的数据表对应的类
     * @return 清理表成功返回 1，否则返回 0
     */
    public <Table> int clearTable(Class<Table> clazz) {
        if (clazz != null) {
            try {
                ConnectionSource source = helper.getConnectionSource();
                return TableUtils.clearTable(source, clazz);
            } catch (SQLException e) {
                handleSQLException(e);
            }
        }
        return 0;
    }

    /**
     * 关闭OrmLiteSqliteOpenHelper
     */
    public void close() {
        helper.close();
    }

    /**
     * 获取对象对应的Class对象
     *
     * @param t   对象
     * @param <T> 类型
     * @return 返回对象对应的Class对象或null
     */
    public static <T> Class<T> getTofClass(T t) {
        return getTofClass(t, null);
    }

    /**
     * 获取对象对应的Class对象
     *
     * @param t            对象
     * @param defaultClazz 默认的Class对象
     * @param <T>          类型
     * @return 返回对象对应的Class对象或Class对象
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getTofClass(T t, Class<T> defaultClazz) {
        return t != null ? (Class<T>) t.getClass() : defaultClazz;
    }

    /**
     * 抛出异常的回调
     */
    public interface SQLExceptionCallback {
        /**
         * 当抛出SQLException异常时
         *
         * @param e 异常对象
         */
        void onThrowSQLException(SQLException e);
    }
}