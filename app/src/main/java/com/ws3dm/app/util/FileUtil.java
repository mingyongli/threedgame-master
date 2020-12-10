package com.ws3dm.app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.StatFs;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {

	public static final String FILE_SEPARATOR = "/";
	public final static String TEMP_SUFFIX = ".temp";
	public static final int FILE_SUCCESS_WITH_WRITE_FILE_EXISTED = 0x01;
	public static final int FILE_SUCCESS = 0x00;
	public static final int FILE_ERROR_READ_PARAM = (0 - 0x01);
	public static final int FILE_ERROR_WRITE_PARAM = (0 - 0x02);
	public static final int FILE_ERROR_READ_FILE_NOT_FOUND = (0 - 0x03);
	public static final int FILE_ERROR_WRITE_FILE_NOT_FOUND = (0 - 0x04);
	public static final int FILE_ERROR_READ_IO_EXCEPTION = (0 - 0x05);
	public static final int FILE_ERROR_WRITE_IO_EXCEPTION = (0 - 0x06);
	public static final int FILE_ERROR_CREATE_DIR = (0 - 0x10);
	public static final int FILE_ERROR_NOT_ENOUGH_SPACE = (0 - 0x20);
	public static final int FILE_ERROR_UNKNOWN = (0 - 0xFF);
	private static final String TAG = "FileUtil";
	private static final int BYTE_BUFFER_SIZE = 1024 * 64;

	public static void deleteDir(String path, boolean bDelRoot) {
		File dir = new File(path);
		if (dir.exists() && dir.isDirectory()) {
			File[] tmp = dir.listFiles();
			if (null != tmp) {
				int forSize=tmp.length;
				for (int i = 0; i <forSize; i++) {
					if (tmp[i].isDirectory()) {
						deleteDir(path + "/" + tmp[i].getName(), true);
					} else {
						tmp[i].delete();
					}
				}
			}
			if (bDelRoot) {
				dir.delete();
			}
		}
	}

	public static String getExtensionName(String path){
		return path.substring(path.lastIndexOf(".")+1);
	}
	
	/**
	 * 格式化单位
	 *
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return size + "B";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
	}

	public static Boolean delFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			//如果是目录
			return file.delete();
		}
		return true;
	}

	public static BufferedOutputStream getOutputStream(String file) throws IOException {
		return getOutputStream(new File(file));
	}

	public static BufferedOutputStream getOutputStream(File file) throws IOException {
		return new BufferedOutputStream(new FileOutputStream(file), 64 * 1024);
	}

	public static boolean createFileIfNotExist(String filePath) {
		boolean ret = false;
		File file = new File(filePath);
		if (!file.isFile()) {
			try {
				Log.d(TAG, "create file.file=" + file.getAbsolutePath());
				createDirIfNotExist(file);
				ret = file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static boolean createDirIfNotExist(File file) {
		boolean ret = true;
		if (null == file) {
			ret = false;
			return ret;
		}
		if (!file.exists()) {
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				ret = parentFile.mkdirs();
				chmod777(parentFile, null);
			}
			file.mkdir();
		}
		return ret;
	}

	public static boolean createDirIfNotExist(String filePath) {
		File file = new File(filePath);
		return createDirIfNotExist(file);
	}

	public static boolean dirExisted(String path) {
		boolean ret = false;
		File file = new File(path);
		if (file.isDirectory() && file.exists()) {
			ret = true;
		}
		return ret;
	}

	public static boolean fileExisted(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists())
			return true;

		return false;
	}

	public static boolean fileExisted(String filePath, boolean hasContent) {
		boolean ret = false;
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			ret = true;
			if (hasContent && (file.length() <= 0)) {
				ret = false;
			}
		}
		return ret;
	}

	public static void writeFile(String str, String descFile, boolean append) throws Exception {
		if ((null == str) || (null == descFile)) {
			return;
		}
		File file = new File(descFile);

		createDirIfNotExist(file.getParent());

		BufferedOutputStream out = null;

		try {
			byte[] src = str.getBytes("UTF-8");
			out = new BufferedOutputStream(new FileOutputStream(descFile, append), 1024 * 64);
			out.write(src);
			out.flush();
		} finally {
			close(out);
		}
	}

	public static void appendFile(String str, String descFile) throws Exception {
		writeFile(str, descFile, true);
	}

	public static String getTempName(String file) {
		return file + TEMP_SUFFIX;
	}

	public static int randomWriteBigFile(InputStream inputStream, String descFile, long offset) {
		Log.d(TAG, "enter randomWriteBigFile(" + inputStream + ", " + descFile + ", " + offset + ")");
		int ret = FILE_SUCCESS;
		RandomAccessFile random = null;

		try {
			do {
				if (null == inputStream) {
					ret = FILE_ERROR_READ_PARAM;
					Log.d(TAG, "error param inputStream is null.");
					break;
				}
				if (isNull(descFile)) {
					ret = FILE_ERROR_WRITE_PARAM;
					Log.d(TAG, "error param descFile=" + descFile);
					break;
				}

				createDirIfNotExist(descFile);
				File tempFile = new File(descFile + TEMP_SUFFIX);
				try {
					random = new RandomAccessFile(tempFile, "rwd");
					random.seek(offset);
				} catch (FileNotFoundException e) {
					ret = FILE_ERROR_WRITE_FILE_NOT_FOUND;
					break;
				} catch (IOException e) {
					ret = FILE_ERROR_WRITE_IO_EXCEPTION;
					break;
				}
				byte[] buffer = new byte[BYTE_BUFFER_SIZE];
				int count = -1;
				try {
					while ((count = inputStream.read(buffer)) != -1) {
						try {
							random.write(buffer, 0, count);
						} catch (IOException e) {
							ret = FILE_ERROR_WRITE_IO_EXCEPTION;
							break;
						}
					}
				} catch (IOException e) {
					ret = FILE_ERROR_READ_IO_EXCEPTION;
					break;
				}
			} while (false);
		} catch (Throwable e) {
			e.printStackTrace();
			ret = FILE_ERROR_UNKNOWN;
		} finally {
			close(random);
		}

		return ret;
	}

	public static void chmod777(File file, String root) {
		try {
			if (null == file || !file.exists()) {
				Log.d(TAG, "chmod777 param error. file=" + file);
				return;
			}

			Runtime.getRuntime().exec("chmod 777 " + file.getAbsolutePath());
			File tempFile = file.getParentFile();
			String tempName = tempFile.getName();
			if (tempFile.getName() == null || "".equals(tempName)) {
				Log.d(TAG, "chmod777 to the root directory. return");
				return;
			} else if (isNotNull(root) && root.equals(tempName)) {
				Runtime.getRuntime().exec("chmod 777 " + tempFile.getAbsolutePath());
				Log.d(TAG, "chmod777 match return. root=" + root);
				return;
			}
			chmod777(file.getParentFile(), root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void chmod777(String file, String root) {
		Log.d(TAG, "enter chmod777 : " + file + ", " + root);
		chmod777(new File(file), root);
	}

	public static long getFreeSpaceInKB(String path) {
		long result = -1;
		try {
			File device = new File(path);
			if (device.exists()) {
				StatFs statfs = new StatFs(path);
				int nBlocSize = statfs.getBlockSize();
				int nAvailaBlock = statfs.getAvailableBlocks();
				result = nAvailaBlock * nBlocSize / 1024;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, result + "------");
		return result;
	}

	/**
	 * 写在/mnt/sdcard/目录下面的文件
	 * @param message
	 *            文件内容
	 * @param b
	 *            boolean参数<br>
	 *            true:会检查文件是否存在，存在就往文件追加内容，否则就创建新文件<br>
	 *            false:写入的内容会覆盖原文件的内容<br>
	 */
	public static void writeFileSdcard(String path, String message, boolean b) {
		try {
			FileOutputStream fout = new FileOutputStream(path, b);
			byte[] bytes = message.getBytes();
			fout.write(bytes);
			fout.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void clearDir(File file, String[] exceptSuffix) {//需要保留的后缀
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (null != files) {
				for (File tempFile : files) {
					deleteFileEx(tempFile, exceptSuffix);
				}
			}
		} else {
			Log.d(TAG, "Is not a valid directory!" + file.getAbsolutePath());
		}
	}

	private static void deleteFileEx(File file, String[] exceptSuffix) {
		if (file.isFile()) {
			boolean del = true;
			if (null != exceptSuffix) {
				for (String suffix : exceptSuffix) {
					if ((null != suffix) && (file.getName().endsWith(suffix))) {
						del = false;
						break;
					}
				}
			}
			if (del) {
				Log.d(TAG, "delete file : " + file.getAbsolutePath());
				file.delete();
			}
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (null == files || files.length == 0) {
				file.delete();
			} else {
				for (File tempFile : files) {
					deleteFileEx(tempFile, exceptSuffix);
				}
				file.delete();
			}
		}
	}

	public static void decompress(String srcFile, String destFile) throws Exception {
		decompress(new File(srcFile), new File(destFile));
	}

	/**
	 * 解压文件
	 * <p>
	 * 如解压文件存放路径不是有效目录，则解压后文件存放在待解压文件同级目录里
	 *
	 * @param srcFile  待解压文件
	 * @param destFile 解压文件存放路径
	 * @throws Exception
	 */
	public static void decompress(File srcFile, File destFile) throws Exception {

		if (null == srcFile || !srcFile.isFile()) {
			return;
		}

		if (null == destFile || !destFile.isDirectory()) {

			destFile = srcFile.getParentFile();
		}

		ZipInputStream in = null;
		BufferedOutputStream out = null;

		try {
			in = new ZipInputStream(new FileInputStream(srcFile));
			ZipEntry entry = null;
			byte[] b = new byte[BYTE_BUFFER_SIZE];
			int len = -1;

			while (null != (entry = in.getNextEntry())) {
				File file = new File(destFile.getAbsolutePath() + FILE_SEPARATOR + entry.getName());

				if (entry.isDirectory()) {
					file.mkdirs();
					continue;
				}

				if (!file.getParentFile().isDirectory()) {
					file.getParentFile().mkdirs();
				}
				out = new BufferedOutputStream(new FileOutputStream(file));
				while (-1 != (len = in.read(b))) {
					out.write(b, 0, len);
				}
				out.close();
				out = null;
			}

		} finally {
			close(out);
			close(in);
		}
	}
	
	/**
	 * 功能描述: 删除文件或文件夹
	 */
	public static void deleteFile(File file) {

		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				int forSize=files.length;
				for (int i = 0; i <forSize; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		}
	}
	
	public static void close(Closeable in) {
		if (null != in) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			in = null;
		}
	}

	public static String getFileName(String url) {
		File file = new File(url);
		return file.getName();
	}

	public static String getFileSuffix(String url) {
		String ret = "";
		String fileName = getFileName(url);
		Log.d(TAG, "file name is " + fileName);
		ret = fileName.substring(fileName.lastIndexOf(".") + 1);
		Log.d(TAG, "file suffix is " + ret);
		return ret;
	}

	public static boolean isZipFile(String url) {
		boolean ret = false;
		final String suffixZip = "zip";
		final String suffixRar = "rar";
		final String suffix7Z = "7z";
		String suffix = getFileSuffix(url);
		if ((0 == suffixZip.compareToIgnoreCase(suffix)) || (0 == suffixRar.compareToIgnoreCase(suffix)) || (0 == suffix7Z.compareToIgnoreCase(suffix))) {
			ret = true;
		}
		return ret;
	}

	public static Drawable loadImageFromNetwork(String imageUrl) {
		Drawable drawable = null;
		try {
			if (isZipFile(imageUrl)) {
				return drawable;
			}
			String srcImg = "upgrade_desc" + getFileSuffix(imageUrl);
			// 可以在这里通过文件名来判断，是否本地有此图片
			drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), srcImg);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		Log.d(TAG, "loadImageFromNetwork(" + imageUrl + ") return " + drawable);

		return drawable;
	}

	public static String readFile(String srcFile) {
		String ret = "";
		try {
			File file = new File(srcFile);
			if (file.exists()) {
				ret = readByInputStream(new FileInputStream(file));
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String readByInputStream(InputStream is) {
		StringBuffer sb = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			sb = new StringBuffer();
			String line = "";
			while (null != (line = br.readLine())) {
				sb.append(line);
			}
		} catch (IOException e) {
			sb = null;
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != isr) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (null != sb) {
			return sb.toString();
		}

		return null;
	}

	public static boolean isNull(String arg) {
		boolean result = false;
		if (null == arg || "".equals(arg.trim()))
			result = true;
		return result;
	}

	public static boolean isNotNull(String arg) {
		boolean result = true;
		if (null == arg || "".equals(arg.trim()))
			result = false;
		return result;
	}

	public static void initDirs(List<String> dirs) {
		for (String dir : dirs) {
			createDirIfNotExist(dir);
		}
	}
	/**
	 * 从URL读取图片
	 */
	public static Drawable loadImageFromUrl(String url, String path_name) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
			File myFileTemp = new File(path_name);
			if (!myFileTemp.exists()) {
				FileOutputStream fos = new FileOutputStream(myFileTemp);
				byte[] b = new byte[1024];
				int aa;
				while ((aa = i.read(b)) != -1) {
					fos.write(b, 0, aa);
				}
				fos.close();
				i.close();
			}
			return Drawable.createFromPath(path_name);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 把bitmap转换成base64
	 *
	 * @param bitmap
	 * @param bitmapQuality
	 * @return
	 */
	public static String getBase64FromBitmap(Bitmap bitmap, int bitmapQuality) {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, bStream);
		byte[] bytes = bStream.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	/**
	 * 加载本地图片 http://bbs.3gstdy.com
	 *
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Describution :根据url返回一个bitmap对象
	 *
	 * Author : DKjuan
	 *
	 * Date : 2017/9/23 17:23
	 **/
	public static Bitmap getBitmapFromUrl(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;

		try {
			myFileUrl = new URL(url);
			HttpURLConnection conn;
			conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
	 * 把base64转换成bitmap
	 *
	 * @param string
	 * @return
	 */
	public static Bitmap getBitmapFromBase64(String string) {
		byte[] bitmapArray = null;
		try {
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BitmapFactory
				.decodeByteArray(bitmapArray, 0, bitmapArray.length);
	}
}
