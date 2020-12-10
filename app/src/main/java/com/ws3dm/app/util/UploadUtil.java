package com.ws3dm.app.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.yu.imgpicker.entity.ImageItem;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * @Function：上传工具类
 * 
 * @author： DKjuan
 * 
 * @date ： 2016-7-6上午9:20:30
 * 
 */
public class UploadUtil {
	private static final String TAG = "uploadFile";
	private static final int TIME_OUT = 8 * 1000;                          //超时时间
	private static final String CHARSET = "UTF-8";                         //编码格式
	private static final String PREFIX = "--";                            //前缀
	private static final String BOUNDARY = UUID.randomUUID().toString();  //边界标识 随机生成
	private static final String CONTENT_TYPE = "multipart/form-data";     //内容类型
	private static final String LINE_END = "\r\n";                        //换行

	/**
	 * android上传文件到服务器
	 *
	 * @param file       需要上传的文件
	 * @param RequestURL 请求的rul
	 * @return 返回响应的内容
	 */
	public static String uploadFile(String strParams, File file, String RequestURL) {
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型

		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
			conn.connect();

			if (file != null) {
				//读取图片进行压缩  
				//如果不需要压缩的话直接读取文件则可 InputStream is = new FileInputStream(file);  
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); //0-100 100为不压缩  
				InputStream is = new ByteArrayInputStream(baos.toByteArray());

				OutputStream outputSteam = conn.getOutputStream();
				DataOutputStream dos = new DataOutputStream(outputSteam);
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);

				//特别注意  
				//name是服务器端需要key;filename是文件的名字（包括后缀）  
				sb.append("Content-Disposition: form-data; name=\"upload!\"; filename=\""+"imgfile"+"\""+LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());

				byte[] bytes = new byte[10240];
				int len = 0;
				while((len=is.read(bytes))!=-1)
				{
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
				dos.write(end_data);
				dos.flush();

				//获取返回码，根据返回码做相应处理  
				int res = conn.getResponseCode();
				Log.d(TAG, "response code:"+res);
				if(conn.getResponseCode() == 200)
				{
					BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					StringBuilder result = new StringBuilder();
					String line;
					while((line = input.readLine()) != null)
					{
						result.append(line).append("\n");
					}
					Log.i(TAG, result.toString());
					return result.toString();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String uploadFile(final Map<String, String> strParams,File file, String requestURL) {
		String TAG = "uploadFile";
		String PREFIX = "--";
		String LINE_END = "\r\n";
		String BOUNDARY = UUID.randomUUID().toString();  //随机生成边界  

		try {
			URL url = new URL(requestURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(30 * 1000); //30秒连接超时  
			connection.setReadTimeout(30 * 1000);   //30秒读取超时  
			connection.setDoInput(true);  //允许文件输入流  
			connection.setDoOutput(true); //允许文件输出流  
			connection.setUseCaches(false);  //不允许使用缓存  
			connection.setRequestMethod("POST");  //请求方式为POST  
			connection.setRequestProperty("Charset", CHARSET);  //设置编码为utf-8  
			connection.setRequestProperty("connection", "keep-alive"); //保持连接  
//			connection.setRequestProperty("Cookie", "sid=");//设置cookie，多个cookie用;分开  
			connection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY); //特别注意：Content-Type必须为multipart/form-data  

			//如果传入的文件路径不为空的话，则读取文件并上传  
			if(file!=null) {
				/** * 当文件不为空，把文件包装并且上传 */
				//读取图片进行压缩  
				//如果不需要压缩的话直接读取文件则可 InputStream is = new FileInputStream(file);  
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); //0-100 100为不压缩  
				InputStream is = new ByteArrayInputStream(baos.toByteArray());

				OutputStream outputSteam = connection.getOutputStream();
				DataOutputStream dos = new DataOutputStream(outputSteam);
				
				String[] params = {"\"uid\"","\"fid\"","\"tid\"","\"pid\"","\"time\"","\"sign\""};
				String[] values = {strParams.get("uid"),strParams.get("fid"),strParams.get("tid"),strParams.get("pid"),
						strParams.get("time"),strParams.get("sign")};
				//添加docName,docType,sessionKey,sig参数  
				for(int i=0;i<params.length;i++){
					//添加分割边界  
					StringBuffer sb = new StringBuffer();
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINE_END);

					sb.append("Content-Disposition: form-data; name=" + params[i] + LINE_END);
					sb.append(LINE_END);
					sb.append(values[i]);
					sb.append(LINE_END);
					dos.write(sb.toString().getBytes());
				}

				//file内容  
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);

				sb.append("Content-Disposition: form-data; name=\"imgfile\";filename=" + "\"" + file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: image/jpg"+LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				//读取文件的内容  
//				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while((len=is.read(bytes))!=-1)
				{
					dos.write(bytes, 0, len);
				}
				is.close();
				//写入文件二进制内容  
				dos.write(LINE_END.getBytes());
				//写入end data  
				byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 
				 * 当响应成功，获取响应的流 
				 */
				int res = connection.getResponseCode();
				Log.e(TAG, "response code:"+res);
				if(res==200) {
					String oneLine;
					StringBuffer response = new StringBuffer();
					BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					while ((oneLine = input.readLine()) != null) {
						response.append(oneLine);
					}
					Log.i(TAG, response.toString());
					return response.toString();
				}else{
					return res+"";
				}
			}else{
				return "file not found";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "failed";
		} catch (IOException e) {
			e.printStackTrace();
			return "failed";
		}
	}

	public static String uploadFeed(final Map<String, String> strParams, List<ImageItem> imageItems, String requestURL) {
		String TAG = "uploadFile";
		String PREFIX = "--";
		String LINE_END = "\r\n";
		String BOUNDARY = UUID.randomUUID().toString();  //随机生成边界  

		try {
			URL url = new URL(requestURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(30 * 1000); //30秒连接超时  
			connection.setReadTimeout(30 * 1000);   //30秒读取超时  
			connection.setDoInput(true);  //允许文件输入流  
			connection.setDoOutput(true); //允许文件输出流  
			connection.setUseCaches(false);  //不允许使用缓存  
			connection.setRequestMethod("POST");  //请求方式为POST  
			connection.setRequestProperty("Charset", CHARSET);  //设置编码为utf-8  
			connection.setRequestProperty("connection", "keep-alive"); //保持连接  
//			connection.setRequestProperty("Cookie", "sid=");//设置cookie，多个cookie用;分开
			connection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY); //特别注意：Content-Type必须为multipart/form-data  

			//如果传入的文件路径不为空的话，则读取文件并上传
//			File file = new File(imageItems.get(0).path);
			if(true||imageItems.size()>0) {
				/** * 当文件不为空，把文件包装并且上传 */
				OutputStream outputSteam=connection.getOutputStream();
				DataOutputStream dos = new DataOutputStream(outputSteam);

				String[] params = {"\"uid\"","\"content\"","\"time\"","\"sign\""};
				String[] values = {strParams.get("uid"),strParams.get("content"),strParams.get("time"),strParams.get("sign")};
				//添加docName,docType,sessionKey,sig参数  
				for(int i=0;i<params.length;i++){
					//添加分割边界
					StringBuffer sb = new StringBuffer();
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINE_END);

					sb.append("Content-Disposition: form-data; name=" + params[i] + LINE_END);
					sb.append(LINE_END);
					sb.append(values[i]);
					sb.append(LINE_END);
					dos.write(sb.toString().getBytes());
				}

				//file内容  
				for(int i=0;i<imageItems.size();i++){
					File file = new File(imageItems.get(i).path);
					StringBuffer sb = new StringBuffer();
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINE_END);

					sb.append("Content-Disposition: form-data; name=\"imgfile\";filename=" + "\"" + file.getName() + "\"" + LINE_END);
					sb.append("Content-Type: image/jpg"+LINE_END);
					sb.append(LINE_END);
					dos.write(sb.toString().getBytes());
					//读取文件的内容  
					InputStream is = new FileInputStream(file);
					byte[] bytes = new byte[1024];
					int len = 0;
					while((len=is.read(bytes))!=-1)
					{
						dos.write(bytes, 0, len);
					}
					is.close();
					//写入文件二进制内容  
					dos.write(LINE_END.getBytes());
				}
				//写入end data  
				byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 
				 * 当响应成功，获取响应的流 
				 */
				int res = connection.getResponseCode();
				Log.e(TAG, "response code:"+res);
				if(res==200) {
					String oneLine;
					StringBuffer response = new StringBuffer();
					BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					while ((oneLine = input.readLine()) != null) {
						response.append(oneLine);
					}
					Log.i(TAG, response.toString());
					return response.toString();
				}else{
					return res+"";
				}
			}else{
				return "file not found";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "failed";
		} catch (IOException e) {
			e.printStackTrace();
			return "failed";
		}
	}

	/**
	 * 对post参数进行编码处理
	 */
	private static StringBuilder getStrParams(Map<String, String> strParams) {
		StringBuilder strSb = new StringBuilder();
		for (Map.Entry<String, String> entry : strParams.entrySet()) {
			strSb.append(PREFIX)
					.append(BOUNDARY)
					.append(LINE_END)
					.append("Content-Type: text/plain; charset=" + CHARSET + LINE_END)
					.append("Content-Transfer-Encoding: 8bit" + LINE_END)
					.append("Content-Disposition: form-data; name=\"" + entry.getKey()+ "\"" + LINE_END)
					.append(LINE_END)// 参数头设置完以后需要两个换行，然后才是参数内容
					.append(entry.getValue())
					.append(LINE_END);
		}
		return strSb;
	}
}