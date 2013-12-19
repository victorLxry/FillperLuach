package com.xury.vicluanch.menu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.xury.vicluanch.R;

public class RoundView extends View {
	private int count;
	private int index;
	private int[] local;
	private Context ctx;
	private Bitmap bit1;
	private Bitmap bit2;

	public RoundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ctx = context;
		bit1=BitmapFactory.decodeResource(getResources(), R.drawable.page_current);
		bit2=BitmapFactory.decodeResource(getResources(), R.drawable.page_non_current);
	}

	public RoundView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public void setCount(int count) {
		this.count = count;
		local = new int[count];
	}

	public void setIndex(int index) {
		this.index = index;
		invalidate();
	}

	private int width;
	private int height;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		height=bottom-top;
		int dime = (int) ctx.getResources().getDimension(R.dimen.pop_span1);
		int startLocal = (width / 2 - count * dime / 2);
		for (int i = 0; i < count; i++) {
			local[i] = startLocal + i * dime + dime / 2;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint p1 = new Paint();
		p1.setAntiAlias(true);
		p1.setColor(getResources().getColor(R.color.roundcolor));
		Paint p2 = new Paint();
		p2.setAntiAlias(true);
		p2.setTextSize(ctx.getResources().getDimension(R.dimen.round_textsize));
		p2.setColor(Color.WHITE);
		for (int i = 0; i < count; i++) {
			if (i == index) {
				canvas.drawBitmap(bit1, local[index], ctx.getResources()
						.getDimension(R.dimen.round_height), p1);
//				canvas.drawCircle(local[index], ctx.getResources()
//						.getDimension(R.dimen.round_height), ctx.getResources()
//						.getDimension(R.dimen.big_radius), p1);
//				canvas.drawText(i + 1 + "", local[index] -  ctx.getResources().getDimension(
//						R.dimen.small_radius), ctx
//						.getResources().getDimension(R.dimen.round_textheight), p2);
			} else {
				canvas.drawBitmap(bit2,local[i], ctx.getResources().getDimension(
						R.dimen.round_height), p1);
				
//				canvas.drawCircle(local[i], ctx.getResources().getDimension(
//						R.dimen.round_height), ctx.getResources().getDimension(
//						R.dimen.small_radius), p1);
			}
		}
	}
}
