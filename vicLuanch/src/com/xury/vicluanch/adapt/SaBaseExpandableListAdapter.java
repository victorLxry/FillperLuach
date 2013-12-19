package com.seeyon.mobile.android.model.common.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.seeyon.mobile.android.R;

@SuppressWarnings("rawtypes")
public class SaBaseExpandableListAdapter extends BaseExpandableListAdapter
		implements ArrayListAdapter.SeeyonNotifyDataChange {
	public ArrayAdapter<String> mGroupData;
	private List<ArrayListAdapter> mChildData;

	protected Activity mContext;

	public SaBaseExpandableListAdapter(Activity context) {
		super();
		this.mContext = context;
		mGroupData = new ArrayAdapter<String>(context,
				R.layout.view_contentlist_header) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				TextView tv = (TextView) v;
				if ("".equals(getItem(position))) {
					v.setVisibility(View.INVISIBLE);
				} else {
					v.setVisibility(View.VISIBLE);
					if (!tv.getText().toString().contains(")"))
						tv.setText(tv.getText() + "("
								+ mChildData.get(position).getCount() + ")");
					else
						tv.setText(tv.getText());
				}
				return v;
			}
		};
		mChildData = new ArrayList<ArrayListAdapter>();
	}

	public void addSection(String section, ArrayListAdapter adapter) {
		if (mGroupData.getCount() > 0
				&& mGroupData.getItem(mGroupData.getCount() - 1).contains(
						mContext.getString(R.string.content_reply))) {// 避免加载到回复下面
			mGroupData.insert(section, mGroupData.getCount() - 1);
			if (adapter != null)
				adapter.setSeeyonNotifyDataChange(this);
			this.mChildData.add(mChildData.size() - 1, adapter);
			return;
		}
		this.mGroupData.add(section);
		if (adapter != null)
			adapter.setSeeyonNotifyDataChange(this);
		this.mChildData.add(adapter);
	}

	public void setSection(String section, ArrayListAdapter adapter) {
		int index = mGroupData.getPosition(section);
		if (adapter != null)
			adapter.setSeeyonNotifyDataChange(this);
		if (index < 0) {
			return;
		}
		mChildData.remove(index);
		mChildData.add(index, adapter);
	}

	public void cleanAll() {
		this.mGroupData.clear();
		this.mChildData.clear();
	}

	public Object getChild(int groupPosition, int childPosition) {
		return mChildData.get(groupPosition).getItem(childPosition);
		// return mChildData.get(groupPosition).get(childPosition);
	}
	
	public Object getChild(int groupPosition) {
		return mChildData.get(groupPosition);
		// return mChildData.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		return mChildData.get(groupPosition).getView(childPosition,
				convertView, parent);
	}

	public int getChildrenCount(int groupPosition) {
		if (mChildData == null || mChildData.get(groupPosition) == null) {
			return 0;
		}
		return mChildData.get(groupPosition).getCount();
		// return mChildData.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		return mGroupData.getItem(groupPosition);// get(groupPosition);
	}

	public int getGroupCount() {
		if (mGroupData == null)
			return 0;
		return mGroupData.getCount();// size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		return mGroupData.getView(groupPosition, convertView, parent);
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

	public void insertDataChild() {
		notifyDataSetChanged();
	}

	@Override
	public void NotifyDataChange() {
		for (int i = 0; i < mGroupData.getCount(); i++) {
			if (mChildData.get(i) == null || mChildData.get(i).getCount() == 0) {
				mGroupData.remove(mGroupData.getItem(i));
				mChildData.remove(i);
			}
		}
		notifyDataSetChanged();
		// mGroupData.clear();
	}

}
