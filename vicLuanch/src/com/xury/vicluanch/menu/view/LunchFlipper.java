package com.xury.vicluanch.menu.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.xury.vicluanch.R;

public class LunchFlipper extends ViewGroup implements LongPressClickListener,
		MoveListener, OnTouchListener, PressClickListener {
	/**
	 * 每页锟斤拷锟斤拷
	 */
	private int lunchRow = 3;
	/**
	 * 每页锟斤拷锟斤拷
	 */
	private int lunchColumns = 3;
	/**
	 * 每页锟斤拷锟斤拷
	 */
	private int lunchPageCount;
	protected List<LunchItem> lunchItemList;
	private Scroller mScroller;
	private Scroller mItemScroller;
	private Scroller nItemScroller;
	private BaseAdapter adapter;
	private Context ctx;
	private int width;
	private int height;
	private LunchView pressBackGroudView;
	private RoundView roundView;
	private boolean isRoundViewVisiable = true;
	private int[] groupIDArray;
	private int C_iLimitSlipSpan = 50;
	private static int C_iLongPressLimitSpan = 30;
	/**
	 * up锟铰硷拷锟斤拷杉锟揭筹拷锟斤拷位锟斤拷
	 */
	private int visiableViewIndex = 0;
	/**
	 * down锟铰硷拷时锟缴硷拷页锟斤拷锟轿伙拷锟?
	 */
	private int visiableViewIndexWhenDown = 0;

	private int viewWidth;
	private int viewHeight;
	/**
	 * 锟角否滑碉拷锟竭界，锟斤拷锟斤拷蔷筒锟斤拷芸锟斤拷锟??锟饺伙拷??
	 */
	private boolean isCanMaxSlip = false;
	/**
	 * 锟劫度革拷锟斤拷
	 */
	private VelocityTracker mVelocityTracker;
	private int mMaximumVelocity;
	/**
	 * 锟斤拷锟斤拷锟劫斤拷锟劫度ｏ拷锟斤拷锟劫度筹拷锟斤拷锟斤拷锟绞憋拷谢锟斤拷锟斤拷锟揭??
	 */
	private static final int SNAP_VELOCITY = 150;
	/**
	 * 锟劫讹拷锟斤拷锟斤拷
	 */
	private static final int velocityRight = -1;
	/**
	 * 锟劫讹拷锟斤拷锟斤拷
	 */
	private static final int velocityLeft = 1;
	/**
	 * 没锟斤拷锟劫讹拷
	 */
	private static final int velocityNo = 0;
	/**
	 * 锟斤拷锟斤拷??
	 */
	private boolean lockSlip = false;
	private OnItemCilickListener onItemClickListener;

	private DrawingCacheView ShadowView;
	private boolean isShadowFlag = false;

	/*
	 * 
	 */
	public LunchFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
		ctx = context;
		setLayoutRowsAndColumns();
		setOnTouchListener(this);
		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		lunchItemList = new ArrayList<LunchItem>();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
		mScroller = new Scroller(context, new DecelerateInterpolator());
		mItemScroller = new Scroller(context, new DecelerateInterpolator());
		nItemScroller = new Scroller(context, new DecelerateInterpolator());
		initDate();
	}

	public LunchFlipper(Context context) {
		this(context, null);
	}

	private void setLayoutRowsAndColumns() {
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		lunchColumns = dm.widthPixels
				/ (int) getResources().getDimension(R.dimen.lunch_Columns);
		lunchRow = (dm.heightPixels - (int) (getResources().getDimension(
				R.dimen.lunch_bottom_height) * dm.density))
				/ (int) getResources().getDimension(R.dimen.lunch_rows);
		lunchPageCount = lunchRow * lunchColumns;
	}

	private void initDate() {
		groupIDArray = new int[lunchPageCount];
		for (int i = 0; i < lunchPageCount; i++) {
			groupIDArray[i] = C_iRlViewID1 + i;
		}
	}

	void initlayout() {
		lunchItemList.clear();
		removeAllViews();
		if (adapter == null)
			return;
		for (int x = 0; x < adapter.getCount(); x++) {
			LunchItem lun = new LunchItem(ctx);
			android.widget.RelativeLayout.LayoutParams laP = new android.widget.RelativeLayout.LayoutParams(
					(int) getResources().getDimension(R.dimen.thumbnail_width),
					height / lunchRow / 10 * 9);
			laP.addRule(RelativeLayout.CENTER_IN_PARENT);
			lun.addView(adapter.getView(x, null, null), laP);
			lun.setEntity((Entity) adapter.getItem(x));
			lunchItemList.add(lun);
			addView(lun, new LayoutParams(width / lunchColumns, height
					/ lunchRow));
			lun.setOnLongPressClickListener(this);
			lun.setOnMoveListener(this);
			lun.setOnPressClickListener(this);
		}
	}

	private boolean onceFlag = true;
	private int widthMeasureSpec;
	private int heightMeasureSpec;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.i("tag", "onMeasure");
		this.widthMeasureSpec = widthMeasureSpec;
		this.heightMeasureSpec = heightMeasureSpec;
		viewWidth = MeasureSpec.getSize(widthMeasureSpec);
		viewHeight = MeasureSpec.getSize(heightMeasureSpec);
		if (getResources().getDisplayMetrics().density == 2)
			viewHeight -= (int) (getResources().getDimension(
					R.dimen.lunch_bottom_height)
					* getResources().getDisplayMetrics().density*2);
		else
			viewHeight -= (int) (getResources().getDimension(
					R.dimen.lunch_bottom_height) * getResources()
					.getDisplayMetrics().density);
		width = viewWidth;
		height = viewHeight;
		if (onceFlag == false) {
			for (LunchItem item : lunchItemList) {
				measureChild(item, widthMeasureSpec, heightMeasureSpec);
			}
			roundView.measure(widthMeasureSpec, heightMeasureSpec);
			pressBackGroudView.measure(widthMeasureSpec, heightMeasureSpec);
			ShadowView.measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		if (onceFlag) {
			Log.i("tag", "onLayout  tt");
			initlayout();
			if (pressBackGroudView == null)
				pressBackGroudView = new LunchView(ctx);
			if (ShadowView == null)
				ShadowView = new DrawingCacheView(ctx);
			if (roundView == null)
				roundView = new RoundView(ctx);
			for (LunchItem item : lunchItemList) {
				measureChild(item, widthMeasureSpec, heightMeasureSpec);
			}
			roundView.measure(widthMeasureSpec, heightMeasureSpec);
			pressBackGroudView.measure(widthMeasureSpec, heightMeasureSpec);
			ShadowView.measure(widthMeasureSpec, heightMeasureSpec);
			onceFlag = false;
			onLayoutForItem();
		}
		int pageCount = lunchItemList.size() / lunchPageCount
				+ (lunchItemList.size() % lunchPageCount == 0 ? 0 : 1);
		if (pageCount == 1) {
			isRoundViewVisiable = false;
			C_iLimitSlipSpan = 1;
			C_iLongPressLimitSpan = 0;
		}
		roundView.setCount(pageCount);
		roundView.layout(
				0,
				height
						+ ((int) getResources().getDimension(
								R.dimen.lunch_bottom_height))
						/ 2
						- (int) ctx.getResources().getDimension(
								R.dimen.pop_left),
				width,
				height
						+ ((int) getResources().getDimension(
								R.dimen.lunch_bottom_height)) / 2);
		pressBackGroudView.layout(0 * width, 0, (0 + 1) * width, height
				+ (int) getResources()
						.getDimension(R.dimen.lunch_bottom_height));
		ShadowViewSlip(downX, downY);
		invalidate();
	}

	private void onLayoutForItem() {
		onLayoutForItem(0);
	}

	/**
	 * 锟斤拷始锟斤拷页锟芥布锟斤拷
	 * 
	 * @param dictanceX
	 */
	private void onLayoutForItem(int dictanceX) {
		int itemWidth = width / lunchColumns;
		int itemHeight = height / lunchRow;
		// Log.i("tag", "dictanceX"+dictanceX);
		for (int i = 0; i < lunchItemList.size(); i++) {
			int x = (i / lunchPageCount) * width
					+ (i - lunchPageCount * (i / lunchPageCount))
					% lunchColumns * itemWidth;
			int y = (i - lunchPageCount * (i / lunchPageCount)) / lunchColumns
					* itemHeight;
			lunchItemList.get(i).layout(x + dictanceX + firstViewLeft, y,
					x + itemWidth + dictanceX + firstViewLeft, y + itemHeight);
			// lunchItemList.get(i).invalidate();
			// lunchItemList.get(i).requestLayout();
		}
		// invalidate();
		// requestLayout();
	}

	/**
	 * 锟斤拷锟斤拷某一锟斤拷lunchitem锟斤拷位锟斤拷
	 * 
	 * @param down
	 *            锟斤拷始位锟斤拷
	 * @param i
	 *            锟斤拷锟轿伙拷锟?
	 */
	private void onLayoutOne(int down, int i) {
		i = i % lunchPageCount;
		int itemWidth = width / lunchColumns;
		int itemHeight = height / lunchRow;
		int x = (i / lunchPageCount) * width
				+ (i - lunchPageCount * (i / lunchPageCount)) % lunchColumns
				* itemWidth;
		int y = (i - lunchPageCount * (i / lunchPageCount)) / lunchColumns
				* itemHeight;
		lunchItemList.get(down).layout(x, y, x + itemWidth, y + itemHeight);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (canvas == null) {
			Log.v("taggg", "canvas");
		}
		if (pressBackGroudView != null) {
			// Log.v("taggg", "pressBackGroudView");
			// drawChild(canvas, pressBackGroudView, getDrawingTime());//需求取消背景色
			if (isRoundViewVisiable)
				drawChild(canvas, roundView, getDrawingTime());
			if (isShadowFlag)
				drawChild(canvas, ShadowView, getDrawingTime());
		}

	}

	private int firstViewLeft;
	private int lastViewRight;
	private int downX;
	private int downY;
	private boolean downPressFlag;
	private int longPressDownLocal;
	private int downVisiableIndex;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
		// if (isSwitchRunnable)
		// return false;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (lunchItemList.size() > 0) {
				// Log.i("tag","leftLoacl="+lunchItemList.get(0).getLeft());
				firstViewLeft = lunchItemList.get(0).getLeft();
				lastViewRight = getLastItemRight();
			}
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			lockSlip = true;
			longFlag = true;
			iscomeLongpress = true;
			longPressDownLocal = getLongPressBackLocal((int) ev.getX(),
					(int) ev.getY());
			visiableViewIndexWhenDown = Math.abs(lunchItemList.get(0).getLeft()
					/ width);
			if (!isMoveRunnable)
				indexForScroller = visiableViewIndexWhenDown;// 锟节伙拷锟斤拷停止锟斤拷时锟斤拷锟斤拷锟矫伙拷锟斤拷页锟斤拷锟斤拷锟斤拷锟斤拷为indexForScroller锟斤拷锟街碉拷锟斤拷锟斤拷诨锟斤拷锟斤拷锟绞憋拷锟斤拷锟?
			// Log.i("tag",
			// "longPressDownLocal="+longPressDownLocal+"visiableViewIndexWhenDown="+visiableViewIndexWhenDown);
			if (visiableViewIndexWhenDown * lunchPageCount + longPressDownLocal < lunchItemList
					.size()) {
				if (!isMoveRunnable) {
					// setBackGroud(
					// 0,
					// pressBackGroudView
					// .findViewById(groupIDArray[longPressDownLocal]));
					Log.i("tag", "gggg" + dayuHeight);
					if (!dayuHeight) {
						setPressItemBackGroud(0);
					}
				}
			}
			longPressLocal = longPressDownLocal;
			downPressFlag = false;
			break;
		case MotionEvent.ACTION_MOVE:
			// Log.i("tag", "lockSlip=" + lockSlip + "@" + longFlag);
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			int local = getLongPressBackLocal(x, y);
			if (lockSlip && longFlag) {// 页锟斤拷锟斤拷锟?
				slipItemView((int) ev.getX() - downX);
				setPageIndexRoundView();
				if (!downPressFlag) {
					setPressItemBackGroud(1);
					// setBackGroud(1,
					// pressBackGroudView
					// .findViewById(groupIDArray[local]));
					downPressFlag = true;
				}
			} else if (!longFlag) {// 锟斤拷锟斤拷锟铰硷拷
				if (local != longPressLocal && local != -1) {
					// Log.i("tag", "local=" + local);

					if (!isSwitchRunnable
							&& !isMoveRunnable
							&& (downVisiableIndex * lunchPageCount
									+ longPressLocal != visiableViewIndex
									* lunchPageCount + local)&&((visiableViewIndex
											* lunchPageCount + local)<lunchItemList.size())) {
						setBackGroud(1,
								pressBackGroudView
										.findViewById(groupIDArray[local]));
						if (longPressLocal != -1)
							setBackGroud(
									0,
									pressBackGroudView
											.findViewById(groupIDArray[longPressLocal]));
						// Log.i("tag", "SWITCHMO");
						isMoveRunnable = true;
						RefreshLunchView(downVisiableIndex * lunchPageCount
								+ longPressLocal, visiableViewIndex
								* lunchPageCount + local);
						longPressLocal = local;
						downVisiableIndex=visiableViewIndex;
					}

				}
				// LogM.i("绉诲姩妯″潡"+x+"&&"+y);
				ShadowViewSlip(x, y);
				longPressSlip(x);
			}
			break;
		case MotionEvent.ACTION_UP:
			Log.i("tag", "up");
			dayuHeight = false;
			if (lockSlip && longFlag) {// 锟斤拷锟?
				int sign = getVelocityType();
				touchUpEvent(sign);
			} else if (!longFlag) {// 锟斤拷锟斤拷锟铰硷拷
				// if (longPressLocal != -1)
				if (onItemClickListener != null)
					onItemClickListener.stopLongPress();
				pressBackGroudView.setBackGround(1);

				isShadowFlag = false;
				longPressLocal = -1;
				if (!longFlag) {// 锟斤拷示锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟角斤拷锟斤拷模锟斤拷锟铰硷拷没锟叫憋拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷要锟斤拷示锟斤拷锟斤拷锟截的帮拷钮
					// Log.i("tag", "visiableMo=" + visiableViewIndexWhenDown *
					// 9
					// + longPressDownLocal);
					// lunchItemList.get(visiableViewIndex * lunchPageCount
					// + longPressLocal).setVisibility(
					// View.VISIBLE);
					for (LunchItem itrm : lunchItemList) {
						if (!itrm.isShown()) {
							itrm.setVisibility(View.VISIBLE);
						}
					}
				}
			}
			if (longPressLocal != -1) {
				setBackGroud(1,
						pressBackGroudView
								.findViewById(groupIDArray[longPressLocal]));
				setPressItemBackGroud(1);
			}
			longPressLocal = -1;
			break;

		default:
			break;
		}
		invalidate();
		return super.dispatchTouchEvent(ev);
	}

	private void slipItemView(int dictanceX) {
		if (limitSlip()) {
			isCanMaxSlip = false;
			int right = lastViewRight;
			if ((firstViewLeft + dictanceX) > C_iLimitSlipSpan) {
				onLayoutForItem(C_iLimitSlipSpan);
				pressBackGroudView.layout(+C_iLimitSlipSpan, 0, ((1) * width)
						+ C_iLimitSlipSpan, height);
			} else if ((right + dictanceX) < (width - C_iLimitSlipSpan)) {
				isCanMaxSlip = false;
				onLayoutForItem(-C_iLimitSlipSpan);
				pressBackGroudView.layout(-C_iLimitSlipSpan, 0, (1 * width)
						- C_iLimitSlipSpan, height);
			} else {
				onLayoutForItem(dictanceX);
				isCanMaxSlip = true;
				pressBackGroudView.layout(+dictanceX, 0, ((1) * width)
						+ dictanceX, height);
			}
			invalidate();
		}
	}

	private int offset;

	private void slipViewForShrikRUN(int dictanceX) {
		int xOffSet = dictanceX - offset;
		// Log.i("tag", "offset="+xOffSet);
		for (int i = 0; i < lunchItemList.size(); i++) {
			lunchItemList.get(i).offsetLeftAndRight(xOffSet);
			// lunchItemList.get(i).invalidate();
			// lunchItemList.get(i).requestLayout();
		}
		setPageIndexRoundView();
		invalidate();
		// requestLayout();
		offset = dictanceX;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @return
	 */
	private boolean limitSlip() {
		boolean flag = true;
		if (lunchItemList.size() > 0) {
			LunchItem viewFirst = lunchItemList.get(0);
			int right = getLastItemRight();
			if (viewFirst.getLeft() > C_iLimitSlipSpan) {
				flag = false;
				isCanMaxSlip = false;
			}
			if (right < width - C_iLimitSlipSpan) {
				flag = false;
				isCanMaxSlip = false;
			}
		}
		return flag;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @param moveX
	 */
	protected boolean isMoveRunnable = false;
	private int longPressX;

	private void longPressSlip(int moveX) {
		if (!isMoveRunnable) {
			if (Math.abs(longPressX - moveX) > 5) {
				// Log.i("tag", "LongSlip=" + moveX + "**" + viewWidth);
				if (moveX > (viewWidth - C_iLongPressLimitSpan)) {
					if (visiableViewIndex != (lunchItemList.size() / lunchPageCount)) {
						indexForScroller++;
						startScrollByScroller(0, -width);
					}
				} else if (moveX < C_iLongPressLimitSpan) {
					if (visiableViewIndex != 0) {
						indexForScroller--;
						startScrollByScroller(0, width);
					}
				}
			}
		} else {
			longPressX = moveX;
		}
	}

	private int getVelocityType() {
		final VelocityTracker velocityTracker = mVelocityTracker;
		// 锟斤拷锟姐当前锟劫讹拷
		velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
		// x锟斤拷锟斤拷锟????
		int velocityX = (int) velocityTracker.getXVelocity();
		Log.i("tag", "velocityX=" + velocityX);
		Log.i("tag", "isCanMaxSlip=" + isCanMaxSlip + "indexForScroller="
				+ indexForScroller);
		int sign = 0;
		int firstItemLeft = lunchItemList.get(0).getLeft();
		int lastItemRight = getLastItemRight();
		Log.i("tag", "lastItemRight+" + lastItemRight);
		if ((velocityX > SNAP_VELOCITY) && isCanMaxSlip
				&& (indexForScroller != 0) && (firstItemLeft < 0))// 锟姐够锟斤拷??锟斤拷锟斤拷锟揭伙拷??
		{
			sign = velocityRight;
			Log.e("enough to move left", "right");
		} else if ((velocityX < -SNAP_VELOCITY)
				&& isCanMaxSlip
				&& (indexForScroller != (lunchItemList.size() / lunchPageCount))
				&& (lastItemRight > width))// 锟姐够锟斤拷??锟斤拷锟斤拷锟斤拷??
		{
			sign = velocityLeft;
			Log.e("enough to move right", "left");
		} else {
			Log.e("enough to move right", "center");
			sign = velocityNo;
		}
		return sign;
	}

	/**
	 * up锟铰硷拷锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @param sign
	 */
	private void touchUpEvent(int sign) {
		if (!mScroller.isFinished()) {
			mScroller.forceFinished(true);
			handler.removeCallbacks(mScrollerRunnable);
		}
		// Log.i("tag","leftLoacl="+lunchItemList.get(0).getLeft());
		if (lunchItemList.size() > 0) {
			int firstItemLeft = lunchItemList.get(0).getLeft();
			int lastItemRight = getLastItemRight();
			LunchItem view = lunchItemList.get(0);
			int left = view.getLeft() % width;
			if (left >= 0) {
				switch (sign) {
				case velocityRight:
					indexForScroller--;
					startScrollByScroller(left, width - left);
					break;
				case velocityLeft:
					indexForScroller++;
					startScrollByScroller(left, -left);
					break;
				case velocityNo:
					if (Math.abs(left) < (width / 2) || firstItemLeft > 0) {
						startScrollByScroller(left, -left);
					} else {
						indexForScroller--;
						startScrollByScroller(left, width - left);
					}
					break;

				default:
					break;
				}
			} else {
				switch (sign) {
				case velocityRight:
					indexForScroller--;
					startScrollByScroller(left, -left);
					break;
				case velocityLeft:
					indexForScroller++;
					startScrollByScroller(left, -width - left);
					break;
				case velocityNo:
					if (Math.abs(left) < (width / 2) || lastItemRight < width) {
						startScrollByScroller(left, -left);
					} else {
						indexForScroller++;
						startScrollByScroller(left, -width - left);
					}
					break;

				default:
					break;
				}
			}

		}
	}

	private int indexForScroller = 0;

	private void startScrollByScroller(int startX, int dX) {
		Log.i("tag", "startX=" + startX + "dX=" + dX);
		offset = startX;
		mScroller.startScroll(startX, 0, dX, 0, 400);
		handler.post(mScrollerRunnable);
	}

//	SwitchRunnable switchRunnable;

	private void RefreshLunchView(int downIndex, int upIndex) {
		downIndex = downIndex > (lunchItemList.size() - 1) ? lunchItemList
				.size() - 1 : downIndex;
		if (upIndex >= lunchItemList.size() - 1)
			upIndex = lunchItemList.size() - 1;
//		LogM.i("downIndex=" + downIndex + "$$upIndex=" + upIndex);
		startScrollByScrollerForLongPress(downIndex, upIndex);
	}

	/**
	 * 锟斤拷锟斤拷模锟斤拷
	 * 
	 * @param downIndex
	 * @param upIndex
	 */
	private void startScrollByScrollerForLongPress(int downIndex, int upIndex) {
		List<Integer> normal = new ArrayList<Integer>();
		List<Integer> spacial = new ArrayList<Integer>();
		List<Integer> spanSpacial = new ArrayList<Integer>();
		if (downIndex < upIndex) {
			for (int i = 0; i < upIndex - downIndex; i++) {
				int selectedIndex = downIndex + 1 + i;
				if (downIndex / lunchPageCount != selectedIndex
						/ lunchPageCount
						&& selectedIndex % lunchPageCount == 0) {
					spanSpacial.add(selectedIndex);
				} else {

					if (isLeftBoundary(selectedIndex)) {
						spacial.add(selectedIndex);
					} else {
						normal.add(selectedIndex);
					}

				}
			}
			// offsetX = 0;
			// spacialOffsetX = 0;
			// spacialOffsetY = 0;
			// spanSpacialOffsetX = 0;
			// spanSpacialOffsetY = 0;
			mScroller.startScroll(0, 0, -width / lunchColumns, 0, 400);
			mItemScroller.startScroll(0, 0, width / lunchColumns
					* (lunchColumns - 1), -height / lunchRow, 400);
			nItemScroller.startScroll(0, 0, -width / lunchColumns, height
					/ lunchRow * (lunchRow - 1), 400);
		} else if (downIndex > upIndex) {
			for (int i = 0; i < downIndex - upIndex; i++) {
				int selectedIndex = upIndex + i;
				if (downIndex / lunchPageCount != selectedIndex
						/ lunchPageCount
						&& selectedIndex % lunchPageCount == (lunchPageCount - 1)) {
					spanSpacial.add(selectedIndex);
				} else {
					if (isRightBoundary(upIndex + i)) {
						spacial.add(upIndex + i);
					} else {
						normal.add(upIndex + i);
					}
				}
			}
			// offsetX = 0;
			// spacialOffsetX = 0;
			// spacialOffsetY = 0;
			// spanSpacialOffsetX = 0;
			// spanSpacialOffsetY = 0;
			mScroller.startScroll(0, 0, width / lunchColumns, 0, 400);
			mItemScroller.startScroll(0, 0, -width / lunchColumns
					* (lunchColumns - 1), height / lunchRow, 400);
			nItemScroller.startScroll(0, 0, width / lunchColumns, -height
					/ lunchRow * (lunchRow - 1), 400);
		}
		// if (switchRunnable == null)
		SwitchRunnable switchRunnable = new SwitchRunnable();
		switchRunnable
				.setData(normal, spacial, spanSpacial, downIndex, upIndex);
		handler.post(switchRunnable);
	}

	private boolean isLeftBoundary(int index) {
		if ((index + 1) % lunchColumns == 1)
			return true;
		return false;
	}

	private boolean isRightBoundary(int index) {
		if ((index + 1) % lunchColumns == 0)
			return true;
		return false;
	}

	private Handler handler = new Handler();
	/**
	 * 锟斤拷锟斤拷锟斤拷锟狡讹拷锟斤拷位锟斤拷锟斤拷锟斤拷
	 * 
	 * @param x
	 * @param y
	 * @return 锟斤拷锟斤拷-1锟斤拷示锟斤拷锟斤拷幕之锟斤拷
	 */
	private int longPressLocal = -1;
	private boolean dayuHeight;

	private int getLongPressBackLocal(int x, int y) {
		// Log.i("tag", "x=" + x + "y=" + y);
		int local = 0;
		if (y > height) {
			dayuHeight = true;
		}
		local = (x / (viewWidth / lunchColumns))
				+ (y / (viewHeight / lunchRow)) * lunchColumns;
		if (local < 0)
			local = 0;
		if (local > lunchPageCount - 1)
			local = lunchPageCount - 1;
//		LogM.i("lunchindex=" + local);
		return local;
	}

	private void setQuake() {
		Vibrator vibrator = (Vibrator) ctx
				.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 5, 100, 0, 0 }; // 停止 ???? 停止 ????
		vibrator.vibrate(pattern, -1);
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
		onceFlag = true;
		firstViewLeft = 0;
		requestLayout();
	}

	public void setOnItemClickListener(OnItemCilickListener l) {
		onItemClickListener = l;
	}

	private boolean longFlag;

	/**
	 * 锟斤拷锟斤拷锟铰硷拷
	 */
	@Override
	public void longClick(View v) {
		longFlag = false;
		if (!downPressFlag) {
			if (onItemClickListener != null)
				onItemClickListener.startLongPress();
			setPressItemBackGroud(1);
			setQuake();
			pressBackGroudView.setBackGround(0);
			LunchItem itemview = lunchItemList.get(visiableViewIndexWhenDown
					* lunchPageCount + longPressDownLocal);
			// Log.i("tag", "long="+itemview.getEntity().getName());
			itemview.setVisibility(View.INVISIBLE);
			ShadowView.setBitmap(getItemDrawingCache(itemview));
			ShadowViewSlip(downX, downY);
			ShadowView.setFlag(true);
			ShadowView.invalidate();
			ShadowView.requestLayout();
			isShadowFlag = true;
			downPressFlag = true;
			invalidate();
			requestLayout();
		}

	}

	@Override
	public void move() {
		lockSlip = true;
	}

	@Override
	public void stop() {
		lockSlip = false;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟??
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			lockSlip = true;
		return true;
	}

	/**
	 * 锟斤拷锟斤拷锟铰硷拷
	 */
	@Override
	public void click(View v) {
		if (longFlag) {
			Log.i("tag", "锟斤拷锟斤拷");
			if (longPressLocal != -1)
				setBackGroud(1,
						pressBackGroudView
								.findViewById(groupIDArray[longPressLocal]));
			if (onItemClickListener != null)
				onItemClickListener.itemClick(((LunchItem) v).getEntity());
		}
	}

	/**
	 * 锟斤拷图
	 * 
	 * @param view
	 * @return
	 */
	private Bitmap getItemDrawingCache(View view) {
		view.findViewById(R.id.rl).setBackgroundResource(
				R.drawable.btn_menu_drag);
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(view.getLeft(), view.getTop(), view.getRight(),
				view.getBottom());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache();
		view.findViewById(R.id.rl).setBackgroundResource(
				R.drawable.menu_item_selector);
		return bitmap;
	}

	/**
	 * 闀挎寜鎸夐挳鐨勭Щ鍔?
	 * 
	 * @param x
	 * @param y
	 */
	private void ShadowViewSlip(int x, int y) {
		// ShadowViewSlipRunnable run=new ShadowViewSlipRunnable(x, y);
		// handler.post(run);
		int width = viewWidth / lunchColumns;
		int height = viewHeight / lunchRow;
		ShadowView.layout(x - width / 2, y - height / 2, x + width / 2, y
				+ height / 2);
	}

	private int roundIndex;

	/**
	 * 页锟斤拷丶锟斤拷锟斤拷锟?
	 */
	private void setPageIndexRoundView() {
		int pageIndex = Math.abs((lunchItemList.get(0).getLeft() - width / 2)
				/ width);
		if (pageIndex != roundIndex) {
			roundView.setIndex(pageIndex);
			roundIndex = pageIndex;
		}
	}

	private int getLastItemRight() {
		int lastItemRight = lunchItemList.get(lunchItemList.size() - 1)
				.getRight();
		int i = lunchItemList.size() % lunchColumns;
		if (i == 0)
			i = lunchColumns;
		lastItemRight = lastItemRight + width / lunchColumns
				* (lunchColumns - i);
		return lastItemRight;
	}

	private Runnable mScrollerRunnable = new Runnable() {
		@Override
		public void run() {
			final Scroller scroller = mScroller;
			if (!scroller.isFinished()) {
				isMoveRunnable = true;
				scroller.computeScrollOffset();
				// Log.i("tag", "LL="+scroller.getCurrX());
				slipViewForShrikRUN(scroller.getCurrX());
				handler.postDelayed(this, 16);
				// Log.i("tag","leftLoacl="+lunchItemList.get(0).getLeft());
			} else {
				isMoveRunnable = false;
				if (lunchItemList.size() > 0) {
					int firstLeft = lunchItemList.get(0).getLeft();
					visiableViewIndex = Math.abs(firstLeft / width);// 锟斤拷取锟缴硷拷页锟斤拷位锟斤拷
					if(longFlag){
						downVisiableIndex=visiableViewIndex;
					}
					invalidate();
				}

				pressBackGroudView.layout(0, 0, width, height);
			}
		}
	};

	private boolean iscomeLongpress = true;
	private boolean isSwitchRunnable;

	class SwitchRunnable implements Runnable {
		private List<Integer> normalArray;
		private List<Integer> spacialArray;
		private List<Integer> spanSpacialArray;
		private int downIndex;
		private int upIndex;

		public void setData(List<Integer> normalArray,
				List<Integer> spacialArray, List<Integer> spanSpacialArray,
				int downIndex, int upIndex) {
			this.normalArray = normalArray;
			this.spacialArray = spacialArray;
			this.spanSpacialArray = spanSpacialArray;
			this.downIndex = downIndex;
			this.upIndex = upIndex;
			iscomeLongpress = false;
		}

		@Override
		public void run() {
			final Scroller scroller = mScroller;
			final Scroller spcailScroller = mItemScroller;
			final Scroller spanSpcailScroller = nItemScroller;
			if (!scroller.isFinished() && !spcailScroller.isFinished()
					&& !spanSpcailScroller.isFinished()) {
				isMoveRunnable = true;
				isSwitchRunnable = true;
				scroller.computeScrollOffset();
				spcailScroller.computeScrollOffset();
				spanSpcailScroller.computeScrollOffset();
				switchViewForShrikRUN(normalArray, scroller.getCurrX());
				switchSpacialViewForShrikRUN(spacialArray,
						spcailScroller.getCurrX(), spcailScroller.getCurrY());
				switchSpanSpacialViewForShrikRUN(spanSpacialArray,
						spanSpcailScroller.getCurrX(),
						spanSpcailScroller.getCurrY());
				handler.postDelayed(this, 16);
			} else {
				isSwitchRunnable = false;
				if (lunchItemList.size() > 0) {
					int firstLeft = lunchItemList.get(0).getLeft();
					visiableViewIndex = Math.abs(firstLeft / width);// 锟斤拷取锟缴硷拷页锟斤拷位锟斤拷
				}
				onLayoutOne(downIndex, upIndex);
				// lunchItemList.get(
				// visiableViewIndexWhenDown * lunchPageCount
				// + longPressDownLocal).setVisibility(
				// View.VISIBLE);
				rePackageLunchList(downIndex, upIndex);
				isMoveRunnable = false;
			}
		}

		private int spanSpacialOffsetX;
		private int spanSpacialOffsetY;

		private void switchSpanSpacialViewForShrikRUN(
				List<Integer> spaciallArray, int dictanceX, int dictanceY) {
			int xOffSet = dictanceX - spanSpacialOffsetX;
			int yOffSet = dictanceY - spanSpacialOffsetY;
			// Log.i("tag", "offset="+xOffSet);
			// Log.i("tag", "offset="+yOffSet);
			for (int i : spaciallArray) {
				// Log.i("tag", "$$$$$$$"+lunchItemList.get(i).getTop());
				lunchItemList.get(i).offsetLeftAndRight(xOffSet);
				lunchItemList.get(i).offsetTopAndBottom(yOffSet);
				// lunchItemList.get(i).invalidate();
				// lunchItemList.get(i).requestLayout();
				invalidate();
				// requestLayout();
			}
			spanSpacialOffsetX = dictanceX;
			spanSpacialOffsetY = dictanceY;
		}

		private int offsetX;

		private void switchViewForShrikRUN(List<Integer> normalArray,
				int dictanceX) {
			int xOffSet = dictanceX - offsetX;
			for (int i : normalArray) {
				lunchItemList.get(i).offsetLeftAndRight(xOffSet);
				// lunchItemList.get(i).invalidate();
				// lunchItemList.get(i).requestLayout();
				invalidate();
				// requestLayout();
			}
			offsetX = dictanceX;
		}

		private int spacialOffsetX;
		private int spacialOffsetY;

		private void switchSpacialViewForShrikRUN(List<Integer> spaciallArray,
				int dictanceX, int dictanceY) {
			int xOffSet = dictanceX - spacialOffsetX;
			int yOffSet = dictanceY - spacialOffsetY;
			// Log.i("tag", "offset="+xOffSet);
			// Log.i("tag", "offset="+yOffSet);
			for (int i : spaciallArray) {
				// Log.i("tag", "$$$$$$$"+lunchItemList.get(i).getTop());
				lunchItemList.get(i).offsetLeftAndRight(xOffSet);
				lunchItemList.get(i).offsetTopAndBottom(yOffSet);
				// lunchItemList.get(i).invalidate();
				// lunchItemList.get(i).requestLayout();
				invalidate();
				// requestLayout();
			}
			spacialOffsetX = dictanceX;
			spacialOffsetY = dictanceY;
		}

		private void rePackageLunchList(int downIndex, int upIndex) {
			if (upIndex < lunchItemList.size()) {
				if (upIndex > downIndex) {
					lunchItemList
							.add(upIndex + 1, lunchItemList.get(downIndex));
					lunchItemList.remove(downIndex);
				} else if (upIndex < downIndex) {
					lunchItemList.add(upIndex, lunchItemList.get(downIndex));
					lunchItemList.remove(downIndex + 1);
				}
			} else if (upIndex >= lunchItemList.size()) {
				lunchItemList.add(lunchItemList.size(),
						lunchItemList.get(downIndex));
				lunchItemList.remove(downIndex);
			}
		}
	}

	private PresserRunnable pressRunnable;

	private void setBackGroud(int type, View view) {
		pressRunnable = new PresserRunnable(type, view);
		post(pressRunnable);
	}

	private void setPressItemBackGroud(int type) {
		int index = visiableViewIndexWhenDown * lunchPageCount
				+ longPressDownLocal;
		if (index >= lunchItemList.size())
			return;
		if (type == 0) {
			((ViewGroup) lunchItemList.get(index).getChildAt(0)).getChildAt(0)
					.setBackgroundResource(R.drawable.btn_menu_pre);
		} else if (type == 1) {
			((ViewGroup) lunchItemList.get(index).getChildAt(0)).getChildAt(0)
					.setBackgroundResource(R.drawable.btn_menu_def);
		}
	}

	class PresserRunnable implements Runnable {
		private int type;
		private View view;

		public PresserRunnable(int type, View view) {
			this.type = type;
			this.view = view;
		}

		@Override
		public void run() {
			if (type == 0) {
				view.setBackgroundColor(getResources().getColor(R.color.blue));
				view.getBackground().setAlpha(50);
			} else if (type == 1) {
				view.setBackgroundResource(R.drawable.transport);
			}
		}

	}

	private static final int C_iRlViewID1 = 0x000101;
	private static final int C_iLLGrouplViewID = 0x000111;

	class LunchView extends LinearLayout {
		private List<List<RelativeLayout>> rlArray;
		private RelativeLayout rlBottom;

		public LunchView(Context context) {
			super(context);
			int width = viewWidth / lunchColumns;
			int height = viewHeight / lunchRow;
			LayoutParams llP = new LayoutParams(LayoutParams.FILL_PARENT,
					height);
			LayoutParams rlP = new LayoutParams(width, LayoutParams.FILL_PARENT);
			setId(C_iLLGrouplViewID);
			setOrientation(LinearLayout.VERTICAL);
			List<LinearLayout> viewRows = new ArrayList<LinearLayout>();
			for (int i = 0; i < lunchRow; i++) {
				LinearLayout view = new LinearLayout(ctx);
				view.setOrientation(LinearLayout.HORIZONTAL);
				viewRows.add(view);
			}
			List<List<RelativeLayout>> viewRLs = new ArrayList<List<RelativeLayout>>();
			int id = C_iRlViewID1;
			for (int x = 0; x < lunchRow; x++) {
				List<RelativeLayout> list = new ArrayList<RelativeLayout>();
				for (int y = 0; y < lunchColumns; y++) {
					RelativeLayout rl = new RelativeLayout(ctx);
					rl.setId(id);
					id++;
					list.add(rl);
				}
				viewRLs.add(list);
			}
			for (int i = 0; i < viewRows.size(); i++) {
				addView(viewRows.get(i), llP);
				for (int j = 0; j < lunchColumns; j++) {
					viewRows.get(i).addView(viewRLs.get(i).get(j), rlP);
				}
			}

			rlBottom = new RelativeLayout(context);
			addView(rlBottom, LayoutParams.FILL_PARENT, (int) getResources()
					.getDimension(R.dimen.lunch_bottom_height));
			rlArray = new ArrayList<List<RelativeLayout>>();
			rlArray = viewRLs;
		}

		public void setBackGround(int type) {
			for (List<RelativeLayout> list : rlArray) {
				for (RelativeLayout rl : list) {
					if (type == 0) {
						rl.setBackgroundColor(getResources().getColor(
								R.color.blue));
						rl.getBackground().setAlpha(50);
						rlBottom.setBackgroundColor(getResources().getColor(
								R.color.blue));
						rlBottom.getBackground().setAlpha(50);

					} else if (type == 1) {
						rl.setBackgroundResource(R.drawable.transport);
						rlBottom.setBackgroundResource(R.drawable.transport);
					}

				}
			}

		}

	}

}
