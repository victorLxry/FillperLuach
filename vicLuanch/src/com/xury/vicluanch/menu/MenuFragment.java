package com.xury.vicluanch.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seeyon.mobile.android.R;
import com.seeyon.mobile.android.model.base.BaseActivity;
import com.seeyon.mobile.android.model.base.BaseFragment;
import com.seeyon.mobile.android.model.common.menu.view.BaseLunchFlipper;
import com.seeyon.mobile.android.model.common.menu.view.Entity;
import com.seeyon.mobile.android.model.common.menu.view.OnItemCilickListener;
import com.seeyon.mobile.android.model.common.menu.view.SeeyonMainMenuLayout;
import com.seeyon.mobile.android.model.main.MainActivity;

public class MenuFragment extends BaseFragment implements OnItemCilickListener {
	protected BaseActivity b;
	protected View v;
	private BaseLunchFlipper lunch;
	private boolean isShowMenu;
	private RelativeLayout rlHead;
	private RelativeLayout rlCenter;
	private TextView ivLeft;
	private TextView ivRight;
	private ImageView ivDis;
	private TextView tvTitle;
	private Entity cru;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		isShowMenu = false;
		b = (BaseActivity) getActivity();
		v = inflater.inflate(b.getLayoutId("fragment_menu"), null);
		lunch = (BaseLunchFlipper) v.findViewById(R.id.gridview);
		rlHead = (RelativeLayout) v.findViewById(R.id.rl_head);
		rlCenter = (RelativeLayout) v.findViewById(R.id.rl_head_center);
		ivLeft = (TextView) v.findViewById(R.id.iv_head_left);
		ivRight = (TextView) v.findViewById(R.id.iv_head_right);
		ivDis = (ImageView) v.findViewById(R.id.iv_head_dis);
		tvTitle = (TextView) v.findViewById(R.id.tv_head_title);
		SeeyonMainMenuLayout layout = new SeeyonMainMenuLayout(b);
		layout.setLayout(lunch);
		lunch.setOnItemClickListener(this);
		rlCenter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isShowMenu) {
					lunch.setVisibility(View.VISIBLE);
					lunch.startAnimation(setPopAnimation());
					ivDis.setBackgroundResource(R.drawable.ic_banner_triangle_up);
					isShowMenu = true;
				} else {
					lunch.startAnimation(setSmallPopAnimation());
					isShowMenu = false;
				}
			}
		});
		Log.v("tagg", "......MenuFragment +onCreateView");
		return v;
	}

	public void addContentLayout(String layoutName) {
		LinearLayout ll = (LinearLayout) v.findViewById(R.id.ll_control);
		View view = (View) LayoutInflater.from(b).inflate(
				b.getLayoutId(layoutName), null);
		ll.addView(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	}

	public void addContentLayout(View view) {
		LinearLayout ll = (LinearLayout) v.findViewById(R.id.ll_control);
		ll.addView(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	}

	public View findViewById(int id) {
		return v.findViewById(id);
	}

	/**
	 * 璁剧疆椤堕儴宸︿晶鎸夐挳
	 * 
	 * @param id
	 * @return
	 */
	public TextView setHeadLeftImageView(int id) {
		ivLeft.setVisibility(View.VISIBLE);
		ivLeft.setBackgroundResource(id);
		return ivLeft;
	}

	/**
	 * 璁剧疆椤堕儴鍙充晶鎸夐挳
	 * 
	 * @param id
	 * @return
	 */
	public TextView setHeadRightImageView(int id) {
		ivRight.setVisibility(View.VISIBLE);
		ivRight.setBackgroundResource(id);
		return ivRight;
	}

	/**
	 * 璁剧疆椤堕儴鏂囨湰妗嗗唴瀹�
	 * 
	 * @param text
	 */
	public void setHeadTextViewContent(String text) {
		tvTitle.setText(text);
	}

	private Animation setPopAnimation() {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0);
		animation.setInterpolator(AnimationUtils.loadInterpolator(b,
				android.R.anim.accelerate_decelerate_interpolator));
		animation.setDuration(300);
		return animation;
	}

	private Animation setSmallPopAnimation() {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
		animation.setDuration(300);
		animation.setInterpolator(AnimationUtils.loadInterpolator(b,
				android.R.anim.accelerate_decelerate_interpolator));
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				lunch.setVisibility(View.GONE);
				ivDis.setBackgroundResource(R.drawable.ic_banner_triangle_down);
			}
		});
		return animation;
	}

	@Override
	public final void itemClick(Entity e) {
		if (e != cru) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), MainActivity.class);
			if (e != null && e.getName() != null && e.getName().equals("鍗忓悓")) {
				intent.putExtra(MainActivity.C_sMainAction,
						MainActivity.C_sMianModle_FLOW);
			} else if (e != null && e.getName() != null
					&& e.getName().equals("鍏憡")) {
				intent.putExtra(MainActivity.C_sMainAction,
						MainActivity.C_sMianModle_BULLETIN);
			} else if (e != null && e.getName() != null
					&& e.getName().equals("鏂伴椈")) {
				intent.putExtra(MainActivity.C_sMainAction,
						MainActivity.C_sMianModle_NEW);
			}
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			getActivity().startActivity(intent);
		}

		lunch.startAnimation(setSmallPopAnimation());
		isShowMenu = false;
	}

	@Override
	public void startLongPress() {
		rlHead.setEnabled(false);
		rlCenter.setEnabled(false);
		ivLeft.setEnabled(false);
		ivRight.setEnabled(false);
	}

	@Override
	public void stopLongPress() {
		rlHead.setEnabled(true);
		rlCenter.setEnabled(true);
		ivLeft.setEnabled(true);
		ivRight.setEnabled(true);
	}

}
