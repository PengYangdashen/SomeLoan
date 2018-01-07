package util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {

    //压缩图片尺寸
    public static Bitmap compressBySize(String pathName, int targetWidth,
                                 int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

        // 得到图片的宽度、高度；
        float imgWidth = options.outWidth;
        float imgHeight = options.outHeight;

        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        options.inSampleSize = 1;

        // 如果尺寸接近则不压缩，否则进行比例压缩
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                options.inSampleSize = widthRatio;
            } else {
                options.inSampleSize = heightRatio;
            }
        }

        //设置好缩放比例后，加载图片进内容；
        options.inJustDecodeBounds = false; // 这里一定要设置false
        bitmap = BitmapFactory.decodeFile(pathName, options);
        return bitmap;
    }
}