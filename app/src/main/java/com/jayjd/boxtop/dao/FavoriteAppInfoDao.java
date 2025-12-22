package com.jayjd.boxtop.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.jayjd.boxtop.entity.AppInfo;
import com.jayjd.boxtop.entity.FavoriteApp;

import java.util.List;

@Dao
public interface FavoriteAppInfoDao {
    @Insert
    void insert(FavoriteApp favoriteApp);

    // 根据包名删除 收藏应用
    @Query("DELETE FROM favorite_app WHERE packageName = :packageName")
    void deleteByPackageName(String packageName);
    // sortIndex 排序
    @Query("SELECT DISTINCT a.* FROM app_info a JOIN favorite_app f ON a.packageName = f.packageName ORDER BY a.sortIndex ASC")
    List<AppInfo> getFavoriteApps();

}
