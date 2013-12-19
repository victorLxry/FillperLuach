package com.xury.vicluanch.menu.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xury.vicluanch.MainActivity;
import com.xury.vicluanch.R;
import com.xury.vicluanch.adapt.TArrayListAdapter;
import com.xury.vicluanch.adapt.TArrayListAdapter.IOnDrawViewEx;
import com.xury.vicluanch.adapt.ViewGropMap;
import com.xury.vicluanch.menu.db.LunchDataBaseAdapter;

public class SeeyonMainMenuLayout {
	private BaseLunchFlipper lunch;
	private LunchDataBaseAdapter lunchDB;
	private Context ctx;
	private boolean isUpdateForServer = true;// 避免在oncreat和onresume的时候调用两次

	public SeeyonMainMenuLayout(Context con) {
		ctx = con;
	}

	public void setLayout(BaseLunchFlipper lun) {
		lunch = lun;
		setLayoutToView(false);
	}

	public void setLayout(BaseLunchFlipper lun, final boolean isUpdateForServer) {
		this.isUpdateForServer = isUpdateForServer;
		lunch = lun;
		setLayoutToView(isUpdateForServer);
	}

	public void setLayoutToView(boolean isUpdateForServer) {
		lunch.setOnItemClickListener(new OnItemCilickListener() {
			
			@Override
			public void stopLongPress() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void startLongPress() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void itemClick(Entity e) {
				// TODO Auto-generated method stub
				Toast.makeText(ctx, e.getName(), Toast.LENGTH_SHORT).show();
			}
		});
		List<Entity> updateList =getInitMenuData();
//		if (updateList == null || !lunch.isHasUpdate(updateList))
//			return;
		TArrayListAdapter<Entity> t = new TArrayListAdapter<Entity>(ctx);
		t.setIsItemBackGround(false);
		t.setLayout(R.layout.view_menu_item);
		t.addListData(updateList);
		t.setDrawViewEx(new IOnDrawViewEx<Entity>() {
			@Override
			public void OnDrawViewEx(Context aContext, Entity templateItem,
					ViewGropMap view, int aIndex) {
				((TextView) view.getView(R.id.tv_modle)).setText(templateItem
						.getName());
				((ImageView) view.getView(R.id.ItemImage))
						.setImageResource(templateItem.getId());
				if (templateItem.getCount() != -1
						&& templateItem.getCount() != 0) {
					TextView tvCount = (TextView) view
							.getView(R.id.tv_typeCounte);
					tvCount.setVisibility(View.VISIBLE);
					tvCount.setText("("
							+ (templateItem.getCount() > 999 ? "999+"
									: templateItem.getCount()) + ")");
				}
				View ivNew = view.getView(R.id.iv_newmessage);
				if (templateItem.isNews()) {
					ivNew.setVisibility(View.VISIBLE);
				} else {
					ivNew.setVisibility(View.GONE);
				}
			}
		});
		lunch.setAdapter(t);
//		saveLunchState();
	}

	/**
	 * 根据数据保存信息
	 */
//	public void saveLunchData() {
//		if (lunch == null)
//			return;
//		BaseActivity activity = (BaseActivity) lunch.getContext();
//		MOrgMember me = ((M1ApplicationContext) activity.getApplication())
//				.getCurrMember();
//		if (me == null)
//			return;
//		long useID = me.getOrgID();
//		List<Entity> initUpdataList = getInitMenuData();
//		List<Entity> sqlUpdataList = getSqlData();
//		List<Entity> array = bingInitdataAndSqldata(initUpdataList,
//				sqlUpdataList);
//		if (array.size() == 0)// 在打开一个新页面而且没有展开菜单的情况下，array的size==0，此时不用保留数据
//			return;
//		lunchDB.open();
//		Cursor cursor = lunchDB.select(useID, me.getAccount().getOrgID());
//
//		try {
//			String update = JSONUtil.writeEntityToJSONString(array);
//			if (!cursor.moveToFirst()) {
//				lunchDB.insert(useID, update, me.getAccount().getOrgID());
//			} else {
//				lunchDB.update(update, useID, me.getAccount().getOrgID());
//			}
//		} catch (M1Exception e) {
//			e.printStackTrace();
//		}
//
//		cursor.close();
//		lunchDB.close();
//
//	}

	/**
	 * 根据页面的值保存信息
	 */
//	public void saveLunchState() {
//		if (lunch == null)
//			return;
//		BaseActivity activity = (BaseActivity) lunch.getContext();
//		MOrgMember me = ((M1ApplicationContext) activity.getApplication())
//				.getCurrMember();
//		if (me == null)
//			return;
//		long useID = me.getOrgID();
//		List<Entity> array = lunch.getArrayName();
//		if (array.size() == 0)// 在打开一个新页面而且没有展开菜单的情况下，array的size==0，此时不用保留数据
//			return;
//		lunchDB.open();
//		Cursor cursor = lunchDB.select(useID, me.getAccount().getOrgID());
//
//		try {
//			String update = JSONUtil.writeEntityToJSONString(array);
//			if (!cursor.moveToFirst()) {
//				lunchDB.insert(useID, update, me.getAccount().getOrgID());
//			} else {
//				lunchDB.update(update, useID, me.getAccount().getOrgID());
//			}
//		} catch (M1Exception e) {
//			e.printStackTrace();
//		}
//
//		cursor.close();
//		lunchDB.close();
//	}

	public void setItemCount(Entity entity) {
		lunch.setItemCount(entity);
//		saveLunchState();
	}

	private List<Entity> getInitList(List<Entity> list) {
		for (int a = 0; a < list.size(); a++) {
			if (list.get(a).getType() == 1) {
				list.get(a).setId(R.drawable.ic_message);
				list.get(a).setName(ctx.getString(R.string.main_notification));
			} else if (list.get(a).getType() ==2) {
				list.get(a).setId(R.drawable.ic_collaborative);
				list.get(a).setName(ctx.getString(R.string.main_flow));
			} else if (list.get(a).getType() == 3) {
				list.get(a).setId(R.drawable.ic_off_doc);
				list.get(a).setName(ctx.getString(R.string.main_off));
			} else if (list.get(a).getType() == 4) {
				list.get(a).setId(R.drawable.ic_announcement);
				list.get(a).setName(ctx.getString(R.string.main_bulletin));
			} else if (list.get(a).getType() == 5) {
				list.get(a).setId(R.drawable.ic_news);
				list.get(a).setName(ctx.getString(R.string.main_news));
			} else if (list.get(a).getType() == 6) {
				list.get(a).setId(R.drawable.ic_doc);
				list.get(a).setName(ctx.getString(R.string.main_archive));
			} else if (list.get(a).getType() == 7) {
				list.get(a).setId(R.drawable.ic_address_book);
				list.get(a).setName(ctx.getString(R.string.main_contact));
			} else if (list.get(a).getType() == 8) {
				list.get(a).setId(R.drawable.ic_statistics);
				list.get(a).setName(ctx.getString(R.string.main_chart));
			} else if (list.get(a).getType() ==9) {
				list.get(a).setId(R.drawable.ic_schedule);
				list.get(a).setName(ctx.getString(R.string.main_schedule));
			} else if (list.get(a).getType() == 10) {
				list.get(a).setId(R.drawable.ic_mmetting);
				list.get(a).setName(ctx.getString(R.string.main_conference));
			}
//			else if (list.get(a).getType() == MPrivilegeConstant.C_iMPrivilegeMenu_SetUp) {
//				list.get(a).setId(R.drawable.ic_set);
//				list.get(a).setName(ctx.getString(R.string.main_settings));
//			}
			else if (list.get(a).getType() == 11) {
				list.get(a).setId(R.drawable.ic_im);
				list.get(a).setName(ctx.getString(R.string.main_im));
			} else if (list.get(a).getType() == 12) {
				list.get(a).setId(R.drawable.ic_business_control);
				list.get(a).setName(ctx.getString(R.string.main_business));
			} else {
				list.get(a).setId(R.drawable.ic_im);
			}
		}
		return list;
	}

	private List<Entity> getInitMenuData() {

		List<Entity> list = new ArrayList<Entity>();

//		RBACControlService controlService = (RBACControlService) M1ApplicationContext
//				.getInstance().getM1Service(M1ApplicationContext.RBAC_SERVICE);
//		MList<MPrivilegeResource> mList = controlService
//				.getPrivilegeResourceMList();
//		if (mList == null) {
//			return null;
//		}
//
//		List<MPrivilegeResource> rList = mList.getValue();
//
//		for (MPrivilegeResource res : rList) {
//			if (res.isHasPermissions()) {
//				Entity entity = new Entity();
//				entity.setName(res.getResourceName());
//				entity.setType(res.getResourceType());
//				list.add(entity);
//			}
//		}
		for(int i=1;i<13;i++){
			Entity entity = new Entity();
			entity.setType(i);
			list.add(entity);
		}
//		// 手动加入统计图（用于调试）
//		Entity entity = new Entity();
//		entity.setType(MPrivilegeConstant.C_iMPrivilegeMenu_StatisticalFigure);
//		entity.setName("统计图");
//		list.add(entity);
		getInitList(list);
		return list;
	}

//	private List<Entity> getMenuData(boolean isUpdateForServer) {
//		List<Entity> updateList = null;
//		List<Entity> initUpdataList = getInitMenuData();
//		List<Entity> sqlUpdataList = getSqlData();
//
//		if (initUpdataList == null && sqlUpdataList == null) {
//			return null;
//		} else {
//			if (isUpdateForServer
//					&& (MainActivity.class.isInstance(ctx) || SettingActivity.class
//							.isInstance(ctx))) {
//				if (sqlUpdataList == null)
//					updateList = initUpdataList;
//				else {
//					updateList = bingInitdataAndSqldata(initUpdataList,
//							sqlUpdataList);
//				}
//			} else {
//				updateList = sqlUpdataList;
//			}
//		}
//		if (updateList == null)
//			return null;
//		updateList = getInitList(updateList);
//		for(Entity en:updateList){
//			if(en.getType()==MPrivilegeConstant.C_iMPrivilegeMenu_SetUp){
//				updateList.remove(en);
//			}
//		}
//		return updateList;
//	}

//	private List<Entity> getSqlData() {
//		List<Entity> sqlUpdataList = null;
//		if (lunchDB == null) {
//			lunchDB = new LunchDataBaseAdapter(ctx);
//		}
//		lunchDB.open();
//		BaseActivity activity = (BaseActivity) ctx;
//		MOrgMember me = ((M1ApplicationContext) activity.getApplication())
//				.getCurrMember();
//		if (me != null) {
//			Cursor cursor = lunchDB.select(me.getOrgID(), me.getAccount()
//					.getOrgID());
//			if (cursor.moveToFirst()) {
//
//				String arr = cursor.getString(cursor
//						.getColumnIndex(LunchDataBaseAdapter.COLUMN_Date));
//				LogM.i("menu=" + arr);
//				try {
//					sqlUpdataList = JSONUtil.parseJSONString(arr,
//							new TypeReference<List<Entity>>() {
//							});
//				} catch (M1Exception e1) {
//					e1.printStackTrace();
//				}
//			}
//			lunchDB.close();
//			cursor.close();
//		}
//		return sqlUpdataList;
//	}

//	private List<Entity> bingInitdataAndSqldata(List<Entity> init,
//			List<Entity> sql) {
//		if (sql == null)
//			return init;
//		List<Entity> updata = new ArrayList<Entity>();
//		for (int i = 0; i < sql.size(); i++) {
//			for (int j = 0; j < init.size(); j++) {
//				if (sql.get(i).getType() == init.get(j).getType()) {
//					init.get(j).setNews(sql.get(i).isNews());
//					updata.add(init.get(j));
//				}
//			}
//		}
//		init.removeAll(updata);
//		updata.addAll(init);
//		return updata;
//	}

}
