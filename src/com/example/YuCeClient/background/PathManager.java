package com.example.YuCeClient.background;

import com.example.YuCeClient.util.FileUtil;

import java.io.File;

/**
 * Created by sreay on 14-8-18.
 */
public class PathManager {
    //自定义相机存储路径（图片经过剪裁后的图片，生成640*640）
    public static File getCropPhotoPath() {
        File photoFile = new File(getCropPhotoDir().getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpeg");
        return photoFile;
    }

    //私聊 群聊  保存图片存储的路径
    public static File getSavePicPath() {
        File photoFile = new File(getSavePicDir().getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpeg");
        return photoFile;
    }

    //存储剪裁后的图片的文件夹
    public static File getCropPhotoDir() {
        String path = FileUtil.getRootPath() + "/childHelp/crop/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    //调用系统相机拍照后图片存储路径
    public static File getCameraPhotoPath() {
        File photoFile = new File(getCameraPhotoDir().getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpeg");
        return photoFile;
    }

    //调用系统相机拍照后图片所在的文件夹
    public static File getCameraPhotoDir() {
        String path = FileUtil.getRootPath() + "/childHelp/photo/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    //存储私聊和群聊后的图片的文件夹
    public static File getSavePicDir() {
        String path = FileUtil.getRootPath() + "/childHelp/chat/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    //文件缓存目录
    public static File getImageLoaderCacheDir() {
        String path = FileUtil.getRootPath() + "childHelp/imageloader/cache";
        File cacheDir = new File(path);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }
}
