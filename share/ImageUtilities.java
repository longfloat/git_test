package com.sas.android.bimobile.ui.share;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ImageUtilities {

	// should be moved to com.sas.android.bimobile.util.ImageUtil?

	private final static int MAX_IMAGE_SIZE = 1024;

	private static boolean isNeedSizeChanged(Bitmap src) {
		return src.getWidth() > MAX_IMAGE_SIZE || src.getHeight() > MAX_IMAGE_SIZE;
	}

	public static Bitmap convertBitmap(Bitmap src) {
		if (isNeedSizeChanged(src)) {
			int width = src.getWidth();
			int height = src.getHeight();
			if (height <= width && width > MAX_IMAGE_SIZE) {
				height = height * MAX_IMAGE_SIZE / width;
				width = MAX_IMAGE_SIZE;
			} else if (height > width && height > MAX_IMAGE_SIZE) {
				width = width * MAX_IMAGE_SIZE / height;
				height = MAX_IMAGE_SIZE;
			}
			return Bitmap.createScaledBitmap(src, width, height, true);
		} else {
			return Bitmap.createBitmap(src);
		}
	}

	public static void convertBitmap(String filePath) {
		Bitmap src = BitmapFactory.decodeFile(filePath);
		if (isNeedSizeChanged(src)) {
			Bitmap dst = convertBitmap(src);
			recycleBitmap(src);
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			try {
				FileOutputStream fos = new FileOutputStream(file);
				dst.compress(filePath.endsWith("png") ? CompressFormat.PNG : CompressFormat.JPEG,
						100, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String img2Base64(String filepath) {
		Bitmap src = BitmapFactory.decodeFile(filepath);
		Bitmap dst = src;
		if (isNeedSizeChanged(src)) {
			dst = convertBitmap(src);
			recycleBitmap(src);
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		dst.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] byteArrayImage = baos.toByteArray();
		String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
		return encodedImage;
	}

	private static void recycleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
			System.gc();
		}
	}

}
