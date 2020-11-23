package com.qlm.llw.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2020/11/23.
 */

public class FileUtils {

    public static void writeTxt(String fileName,String str) {

        String filePath = null;

        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        str.replaceAll("\\n","\n");

        if (hasSDCard) {
            filePath =Environment.getExternalStorageDirectory().toString() + File.separator +"Download"+File.separator+fileName+".txt";

        } else

            filePath =Environment.getDownloadCacheDirectory().toString() + File.separator +"Download"+File.separator+fileName+".txt";

        try {

            File file = new File(filePath);

            if (!file.exists()) {

                File dir = new File(file.getParent());

                dir.mkdirs();

                file.createNewFile();

            }

            FileOutputStream outStream = new FileOutputStream(file);

            outStream.write(str.getBytes());

            outStream.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
