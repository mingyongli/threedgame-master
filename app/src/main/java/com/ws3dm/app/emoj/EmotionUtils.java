
package com.ws3dm.app.emoj;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.collection.ArrayMap;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.TypedValue;

import com.ws3dm.app.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 EmotionUtils emoutil=new EmotionUtils(this);
 et_emotion.setText(emoutil.addSmileySpans("噜啦噜啦[微笑][闭嘴吧][大哭][尴尬][生气]"));
 */
public class EmotionUtils {

	/**
	 * 表情类型标志符
	 */
	public static final int EMOTION_CLASSIC_TYPE=0x0001;//经典表情

	/**
	 * key-表情文字;
	 * value-表情图片资源
	 */
	public static ArrayMap<String, Integer> EMPTY_MAP;
	public static ArrayMap<String, Integer> EMOTION_CLASSIC_MAP;
	// 正则表达式
	public Pattern mPattern;
	/*
		 * 单例模式 1文字资源，图片资源 2.使用正则表达式进行匹配文字 3.把edittext当中整体的内容匹配正则表达式一次
		 * 4.SpannableStringBuilder 进行替换
		 */

	public Context mContext;
	public EmotionUtils(Context context) {
		mContext = context;
		// 获取构建的正则表达式
		mPattern = buildPattern();
	}
	/**
	 * 构建正则表达式,用来找到我们所要使用的图片
	 *
	 * @return
	 */
	private Pattern buildPattern() {
		StringBuilder patternString = new StringBuilder(
				(EMOTION_CLASSIC_MAP.size() + EMOTION_CLASSIC_MAP.size()) * 3);
		patternString.append('(');

		for (String key : EMOTION_CLASSIC_MAP.keySet()) {
			patternString.append(Pattern.quote(key));
			patternString.append('|');
		}

		for (String key : EMOTION_CLASSIC_MAP.keySet()) {
			patternString.append(Pattern.quote(""+EMOTION_CLASSIC_MAP.get(key)));
			patternString.append('|');
		}

		patternString.replace(patternString.length() - 1,
				patternString.length(), ")");
		// 把String字符串编译成正则表达式()
		// ([调皮]|[调皮]|[调皮])
		return Pattern.compile(patternString.toString());
	}

	static {
		EMPTY_MAP = new ArrayMap<>();
		EMOTION_CLASSIC_MAP = new ArrayMap<>();

		EMOTION_CLASSIC_MAP.put("[微笑]", R.drawable.wxj);
		EMOTION_CLASSIC_MAP.put("[爱心]", R.drawable.axj);
		EMOTION_CLASSIC_MAP.put("[委屈]", R.drawable.wqj);
		EMOTION_CLASSIC_MAP.put("[害羞]", R.drawable.hxj);
		EMOTION_CLASSIC_MAP.put("[闭嘴]", R.drawable.bzbj);
		EMOTION_CLASSIC_MAP.put("[犯困]", R.drawable.fkj);
		EMOTION_CLASSIC_MAP.put("[大哭]", R.drawable.dkj);
		EMOTION_CLASSIC_MAP.put("[尴尬]", R.drawable.ggj);
		EMOTION_CLASSIC_MAP.put("[生气]", R.drawable.sqj);
		EMOTION_CLASSIC_MAP.put("[可爱]", R.drawable.kaj);
		EMOTION_CLASSIC_MAP.put("[赞个]", R.drawable.zgj);
		EMOTION_CLASSIC_MAP.put("[怀疑]", R.drawable.hyj);
		EMOTION_CLASSIC_MAP.put("[汗]", R.drawable.hj);
		EMOTION_CLASSIC_MAP.put("[鄙视]", R.drawable.bsj);
		EMOTION_CLASSIC_MAP.put("[呆]", R.drawable.drmj);
		EMOTION_CLASSIC_MAP.put("[辣]", R.drawable.lj);
		EMOTION_CLASSIC_MAP.put("[坏笑]", R.drawable.huaixj);
		EMOTION_CLASSIC_MAP.put("[机智]", R.drawable.jzj);
		EMOTION_CLASSIC_MAP.put("[晕]", R.drawable.yj);
		EMOTION_CLASSIC_MAP.put("[思考]", R.drawable.skj);
	}

	/**
	 * 根据名称获取当前表情图标R值
	 *
	 * @param EmotionType 表情类型标志符
	 * @param imgName     名称
	 * @return
	 */
	public static int getImgByName(int EmotionType, String imgName) {
		Integer integer = null;
		switch (EmotionType) {
			case EMOTION_CLASSIC_TYPE:
				integer = EMOTION_CLASSIC_MAP.get(imgName);
				break;
			default:
				break;
		}
		return integer == null ? -1 : integer;
	}

	/**
	 * 根据类型获取表情数据
	 *
	 * @param EmotionType
	 * @return
	 */
	public static ArrayMap<String, Integer> getEmojiMap(int EmotionType) {
		ArrayMap EmojiMap = null;
		switch (EmotionType) {
			case EMOTION_CLASSIC_TYPE:
				EmojiMap = EMOTION_CLASSIC_MAP;
				break;
			default:
				EmojiMap = EMPTY_MAP;
				break;
		}
		return EmojiMap;
	}

	/**
	 * 根据文本替换成图片
	 *
	 * @param text
	 *            对应表情
	 * @return 一个表示图片的序列
	 */
	public CharSequence addSmileySpans(CharSequence text) {
		// 把文字替换为对应图片
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		// 判断提取工具类（按照正则表达式）
		Matcher matcher = mPattern.matcher(text);
		while (matcher.find()) {
			// 获取对应表情的图片id
			int resId = EMOTION_CLASSIC_MAP.get(matcher.group());
			// 替换制定字符
			builder.setSpan(new ImageSpan(mContext, resId), matcher.start(),
					matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return builder;
	}

	/**
	 * 根据文本替换成图片，int width, int height 可控制表情大小
	 *
	 * @param text
	 *            对应表情
	 * @return 一个表示图片的序列
	 */

	public CharSequence addSmileySpansReSize(CharSequence text, int width,
	                                         int height) {
		// 把文字替换为对应图片
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		// 判断提取工具类（按照正则表达式）
		Matcher matcher = mPattern.matcher(text);
		while (matcher.find()) {
			// 获取对应表情的图片id
			int resId = EMOTION_CLASSIC_MAP.get(matcher.group());
			// 替换制定字符
			builder.setSpan(
					new ImageSpan(mContext, decodeSampledBitmapFromResource(
							mContext.getResources(), resId,
							EmotionUtils.dp2px(mContext, width),
							EmotionUtils.dp2px(mContext, height))), matcher
							.start(), matcher.end(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		}
		return builder;
	}

	/**
	 * dp转px
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
	                                                     int resId, int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDensity = res.getDisplayMetrics().densityDpi;
		BitmapFactory.decodeResource(res, resId, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
	                                        int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

}