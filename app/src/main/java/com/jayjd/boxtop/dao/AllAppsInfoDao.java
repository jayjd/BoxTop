package com.jayjd.boxtop.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.jayjd.boxtop.entity.AppInfo;

import java.util.List;

@Dao
public interface AllAppsInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AppInfo appInfo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AppInfo> appInfos);

    @Update
    void update(AppInfo favoriteAppInfo);
    // 根据包名 更新应用打开次数
    @Query("UPDATE app_info SET openAppCount = :openAppCount WHERE packageName = :packageName")
    void updateOpenAppCountByPackageName(String packageName, long openAppCount);
    // 查询 不是系统应用 boolean isSystem = false 不是隐藏应用 boolean isHidden = false 按照启动次数 降序排序 只返回3个
    @Query("SELECT * FROM app_info WHERE isSystem = 0 AND isHidden = 0 ORDER BY openAppCount DESC LIMIT :count")
    List<AppInfo> getAppInfoByOpenAppCountDesc(int count);

    // 根据包名修改排序
    @Query("UPDATE app_info SET sortIndex = :sortIndex WHERE packageName = :packageName")
    void updateSortIndexByPackageName(String packageName, int sortIndex);

    @Delete
    void delete(AppInfo favoriteAppInfo);

    // 根据包名 修改应用是否隐藏
    @Query("UPDATE app_info SET isHidden = :isHidden WHERE packageName = :packageName")
    void updateIsHiddenByPackageName(String packageName, boolean isHidden);

    @Query("DELETE FROM app_info WHERE packageName = :packageName")
    void deleteByPackageName(String packageName);

    @Query("SELECT * FROM app_info")
    List<AppInfo> getAllAppInfo();
}
