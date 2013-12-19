package com.xury.vicluanch.menu.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.xury.vicluanch.R;

public class BaseLunchFlipper extends LunchFlipper {

	public BaseLunchFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseLunchFlipper(Context context) {
		this(context, null);
	}

	/**
	 * 设置个模块的条数，以及是否有未读消息
	 * 
	 * @param entity
	 */
	void setItemCount(Entity entity) {
		for (LunchItem item : lunchItemList) {
			if (item.getEntity().getType() == entity.getType()) {
				if (entity.getCount() != -1) {
					TextView tvCount = (TextView) item
							.findViewById(R.id.tv_typeCounte);
					item.getEntity().setCount(entity.getCount());
					if (entity.getCount() != 0) {
						tvCount.setVisibility(View.VISIBLE);
						tvCount.setText("("
						            + (item.getEntity().getCount() > 999 ? "999+"
						                : item.getEntity().getCount()) + ")");
					} else {
						tvCount.setVisibility(View.GONE);
					}
				}
				item.getEntity().setNews(entity.isNews());
				View ivNew = item.findViewById(R.id.iv_newmessage);
				if (entity.isNews()) {
						ivNew.setVisibility(View.VISIBLE);
				} else {
						ivNew.setVisibility(View.GONE);
				}
				item.measure(
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				item.layout(item.getLeft(), item.getTop(), item.getRight(),
						item.getBottom());
				if (!isMoveRunnable) {
					invalidate();
					requestLayout();
				}
			}
		}
	}

	/**
	 * 检查模块有变化（包括位置顺序变化，模板条数变化），如果有变化才更新，避免不必要的更新
	 * 
	 * @param list
	 * @return
	 */
	public boolean isHasUpdate(List<Entity> list) {
		boolean flag = false;
		if (lunchItemList == null || lunchItemList.size() == 0) {
			flag = true;
		}
		if (list.size() == lunchItemList.size()) {
			for (int i = 0; i < list.size(); i++) {
				if (!flag) {
					Entity en1 = list.get(i);
					Entity en2 = lunchItemList.get(i).getEntity();
					if (en1.getType() != en2.getType()
							|| (en1.getCount() != en2.getCount())
							|| (en1.isNews() != en2.isNews())) {
						flag = true;
					}

				}
			}
		}else{
			flag = true;
		}
		return flag;
	}

	public List<Entity> getArrayName() {
		List<Entity> array = new ArrayList<Entity>();
		for (int i = 0; i < lunchItemList.size(); i++) {
			array.add(lunchItemList.get(i).getEntity());
		}
		return array;
	}

}
