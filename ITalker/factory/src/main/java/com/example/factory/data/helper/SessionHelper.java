package com.example.factory.data.helper;

import com.example.factory.model.db.Session;
import com.example.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;



/**
 * 会话辅助工具类
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class SessionHelper {
    // 从本地查询Session
    public static Session findFromLocal(String id) {
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
