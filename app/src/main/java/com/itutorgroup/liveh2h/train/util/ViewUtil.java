package com.itutorgroup.liveh2h.train.util;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;

public class ViewUtil {

	public static <T> T findViewById(View v, int id) {
        //noinspection unchecked
        return (T) v.findViewById(id);
	}

	public static <T> T findViewById(Activity activity, int id) {
        //noinspection unchecked
        return (T) activity.findViewById(id);
	}

	public static void setPricePoint(final EditText editText) {
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
						editText.setText(s);
						editText.setSelection(s.length());
						return;
					}
				}
				if (s.toString().startsWith(".")) {
					s = "0.";
					editText.setText(s);
					editText.setSelection(2);
					return;
				}
				if (s.toString().startsWith("0") && s.length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						editText.setText("0");
						editText.setSelection(1);
					}
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	/**
	 * 该方法使api 19以上的设备状态栏颜色改变
	 * 注意：状态栏的颜色可以在主题的status_bar_bg里面设置，默认为黑色
	 * @param activity
	 */
	public static void initStatusBar(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			try {
				int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
				if (resourceId > 0) {
					int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
					TypedArray array = activity.getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimaryDark});
					int backgroundColor = array.getColor(0, Color.TRANSPARENT);
					array.recycle();
					ViewGroup contentParent = (ViewGroup) activity.findViewById(android.R.id.content).getParent();
					View view = new View(activity);
					view.setTag("statusBar");
					view.setBackgroundColor(backgroundColor);
					ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
					contentParent.addView(view, 0, layoutParams);
					activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//					activity.getWindow().findViewById(resourceId).setBackgroundColor(Color.TRANSPARENT);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void setStatusBarBg(Activity activity, int bg) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			ViewGroup viewGroup = (ViewGroup) activity.findViewById(android.R.id.content).getParent();
			View view = viewGroup.findViewWithTag("statusBar");
			if (view != null) {
				view.setBackgroundColor(bg);
			}
		}
	}

	/**
	 * 根据EditText是否有内容，显示label
	 * @param editText
	 * @param label
	 */
	public static void setEdittextLabelVisibility(EditText editText, final View label) {
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				setVisibility(label, s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
			}
		});
	}
	
	/**
	 * 设置View显示或隐藏
	 * @param view
	 * @param visibility
	 */
	public static void setVisibility(View view, int visibility) {
		if (view.getVisibility() != visibility) {
			view.setVisibility(visibility);
		}
	}
	
	/**
	 * 设置密码输入框显示或隐藏
	 * @param editText
	 * @param textView
	 */
	public static void setPasswordShow(final EditText editText, final TextView textView) {
		textView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String isShow = (String) textView.getTag();
				if (TextUtils.equals(isShow, "false")) {
                    textView.setTag("true");
                    textView.setText(R.string.show);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    textView.setTag("false");
                    textView.setText(R.string.hide);
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
				editText.setSelection(editText.length());
			}
		});
	}
	
	/**
	 * 设置输入法直接触发点击事件
	 * @param editText
	 * @param target
	 */
	public static void setEditorAction(EditText editText, final View target) {
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					target.performClick();
				}
				return false;
			}
		});
	}
}
