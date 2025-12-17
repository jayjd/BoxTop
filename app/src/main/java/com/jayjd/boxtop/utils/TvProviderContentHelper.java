package com.jayjd.boxtop.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.tv.TvContract;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * TvProvider 内容获取辅助类。
 * 用于查询指定 TV 输入服务 (Input Service) 提供的频道和节目信息。
 */
public class TvProviderContentHelper {

    private static final String TAG = "TvContentHelper";

    /**
     * 【方法一】获取指定 TV 输入服务 (InputId) 下注册的所有频道信息。
     *
     * @param context Context 对象。
     * @param inputId 目标 TvInputService 的 ID (通常是其包名)。
     * @return 匹配该 InputId 的 ChannelInfo 列表。
     */
    public static List<ChannelInfo> getChannelsByInputId(Context context, String inputId) {
        List<ChannelInfo> channelList = new ArrayList<>();
        if (inputId == null || inputId.isEmpty()) {
            return channelList;
        }

        ContentResolver resolver = context.getContentResolver();

        // 1. 定义查询的列 (Projection)

        // 2. 定义查询条件 (Selection)：筛选出特定 inputId 的频道

        String[] PROJECTION = {TvContract.Channels._ID, TvContract.Channels.COLUMN_DISPLAY_NAME, TvContract.Channels.COLUMN_INPUT_ID};
        String SELECTION = TvContract.Channels.COLUMN_INPUT_ID + "=?";
        String[] SELECTION_ARGS = {inputId};
        try (Cursor cursor = resolver.query(TvContract.Channels.CONTENT_URI, PROJECTION, SELECTION, SELECTION_ARGS, null // 不排序
        )) {
            // 不排序

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ChannelInfo info = new ChannelInfo();

                    int idIndex = cursor.getColumnIndexOrThrow(TvContract.Channels._ID);
                    int nameIndex = cursor.getColumnIndexOrThrow(TvContract.Channels.COLUMN_DISPLAY_NAME);
                    int inputIdIndex = cursor.getColumnIndexOrThrow(TvContract.Channels.COLUMN_INPUT_ID);

                    info.id = cursor.getLong(idIndex);
                    info.displayName = cursor.getString(nameIndex);
                    info.inputId = cursor.getString(inputIdIndex);
                    // 构建该频道的 Content URI，用于后续播放或查询节目单
                    info.channelUri = TvContract.buildChannelUri(info.id);

                    channelList.add(info);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // 权限不足或 TvProvider 未启用
            Log.e(TAG, "Error querying channels: " + e.getMessage());
        }
        return channelList;
    }

    /**
     * 【方法二】获取指定频道在特定时间范围内的节目信息 (EPG)。
     *
     * @param context         Context 对象。
     * @param channelId       目标频道的 ID (来自 ChannelInfo.id)。
     * @param startTimeMillis 开始时间 (UTC 毫秒)。
     * @param endTimeMillis   结束时间 (UTC 毫秒)。
     * @return 匹配该频道和时间范围的 ProgramInfo 列表。
     */
    public static List<ProgramInfo> getPrograms(Context context, long channelId, long startTimeMillis, long endTimeMillis) {
        List<ProgramInfo> programList = new ArrayList<>();

        if (channelId < 0) {
            return programList;
        }

        // 1. 构建该频道的 URI
        Uri channelUri = TvContract.buildChannelUri(channelId);

        // 2. 使用 TvContract.Programs 的辅助方法查询节目
        // 这是一个更方便的查询节目单的方式
        Uri programsUri = TvContract.buildProgramsUriForChannel(channelUri, startTimeMillis, endTimeMillis);

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;

        String[] PROJECTION = {TvContract.Programs.COLUMN_TITLE, TvContract.Programs.COLUMN_START_TIME_UTC_MILLIS, TvContract.Programs.COLUMN_END_TIME_UTC_MILLIS, TvContract.Programs.COLUMN_SHORT_DESCRIPTION};

        try {
            cursor = resolver.query(programsUri, PROJECTION, null, // 无额外 Selection
                    null, // 无额外 Selection Args
                    TvContract.Programs.COLUMN_START_TIME_UTC_MILLIS // 按开始时间排序
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ProgramInfo info = new ProgramInfo();

                    int titleIndex = cursor.getColumnIndexOrThrow(TvContract.Programs.COLUMN_TITLE);
                    int startIndex = cursor.getColumnIndexOrThrow(TvContract.Programs.COLUMN_START_TIME_UTC_MILLIS);
                    int endIndex = cursor.getColumnIndexOrThrow(TvContract.Programs.COLUMN_END_TIME_UTC_MILLIS);
                    int descIndex = cursor.getColumnIndexOrThrow(TvContract.Programs.COLUMN_SHORT_DESCRIPTION);

                    info.title = cursor.getString(titleIndex);
                    info.startTimeUtcMillis = cursor.getLong(startIndex);
                    info.endTimeUtcMillis = cursor.getLong(endIndex);
                    info.description = cursor.getString(descIndex);

                    programList.add(info);
                } while (cursor.moveToNext());
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Error querying programs: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return programList;
    }

    /**
     * 频道信息封装类
     */
    public static class ChannelInfo {
        public long id;               // 频道在 TvProvider 中的 ID
        public String displayName;    // 频道显示名称 (例如 "CCTV-1")
        public String inputId;        // 注册该频道的 TvInputService 的 ID (即包名)
        public Uri channelUri;        // 频道的 Content URI
    }

    /**
     * 节目信息封装类
     */
    public static class ProgramInfo {
        public String title;          // 节目名称
        public long startTimeUtcMillis; // 节目开始时间 (UTC 毫秒)
        public long endTimeUtcMillis;   // 节目结束时间 (UTC 毫秒)
        public String description;    // 节目描述
    }
}