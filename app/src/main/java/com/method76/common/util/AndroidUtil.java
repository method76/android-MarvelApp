package com.method76.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Sungjoon Kim on 2016-02-01.
 */
public class AndroidUtil {

    private static Toast mCurrentToast;

    public static int dpToPixel(Context context, float dp){
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            Log.e(e);
        }
        return "";
    }

    /*public static void setHomeWallpaperFromUrl(final Context context, final String imageUri){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL DICE_API_URL = new URL(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(DICE_API_URL.openConnection().getInputStream());
                    System.out.println("Hi I am try to open Bit map");
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                    wallpaperManager.setBitmap(bitmap);
//                    if(Build.VERSION.SDK_INT >= 19) {
//                        wallpaperManager.getCropAndSetWallpaperIntent(
//                                Uri.parse(imageUri));
//                    }
                }catch(MalformedURLException e){
                    Log.e(e);
                }catch(IOException e){
                    Log.e(e);
                }
            }
        }.start();

    }


    public static void downloadFromUrl(Handler handler, Items items) {

        String imageURL = items.getLink();
        String fileName = FilenameUtils.getName(items.getLink()); // baseName + "." + extension;
        long imgSize = items.getByteSize();

        try {
            URL DICE_API_URL = new URL(imageURL);
            File saveDir = new File(AppConst.IMG_DOWNLOAD_PATH);
            saveDir.mkdirs();
            File file = new File(AppConst.IMG_DOWNLOAD_PATH + "/" + fileName);
            if(file.exists()) {
                if(imgSize==file.length()) {
                    handler.sendEmptyMessage(AppConst.IMG_ALREADY_EXISTS);
                    return;
                }else{
                    renameDuplicateFile(file);
                }
            }

            long startTime = System.currentTimeMillis();

            Log.d("Downloading img:" + fileName
                        + " to SDCard: " + file.getAbsolutePath());
            *//* Open a connection to that URL. *//*
            URLConnection ucon = DICE_API_URL.openConnection();
            *//*
             * Define InputStreams to read from the URLConnection.
             *//*
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            *//*
             * Read bytes to the Buffer until there is nothing more to read(-1).
             *//*
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            *//* Convert the Bytes read to a String. *//*
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            Log.d("download ready in " + ((System.currentTimeMillis() - startTime) / 1000)
                    + " sec");
            handler.sendEmptyMessage(AppConst.IMG_DOWNLOAD_COMPLETED);

        } catch (IOException e) {
            Log.w(e);
        }

    }

    public static void renameDuplicateFile(File filepath)
    {
        int index = 0;
        String fullPath = filepath.getAbsolutePath();
        String folder    = FilenameUtils.getPath(fullPath); // filepath.getParent();
        String baseName  = FilenameUtils.getBaseName(fullPath);
        String extension = FilenameUtils.getExtension(fullPath);

        do {
            index++;
            filepath = new File(folder + baseName + "(" + index + ")." + extension);
            Log.d(filepath.getAbsolutePath());
        }
        while (filepath.exists());

    }*/

}
