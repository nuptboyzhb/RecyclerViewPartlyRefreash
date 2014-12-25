package net.mobctrl.recyclerviewpartly;

import java.util.ArrayList;
import java.util.List;

import net.mobctrl.recyclerviewpartlyrefreash.R;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * @date 2014年12月23日 下午3:58:48
 * @author Zheng Haibo
 * @Description:
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.RootViewHolder> {
	private static final int MESSAGE_ITEM_CHANGE = 0;
	private List<ItemModel> mDataset;

	/**
	 * 基础ViewHolder
	 */
	public class RootViewHolder extends RecyclerView.ViewHolder {

		public RootViewHolder(View itemView) {
			super(itemView);
		}
	}

	public class ProgressViewHolder extends RootViewHolder {
		private Button button;
		private ProgressBar progressBar;
		private Button btnDel;

		public ProgressViewHolder(View v) {
			super(v);
			button = (Button) v.findViewById(R.id.btn);
			progressBar = (ProgressBar) v.findViewById(R.id.pb_show);
			btnDel = (Button) v.findViewById(R.id.btn_del);
		}

		/**
		 * 根据数据绑定view的显示
		 * 
		 * @param itemModel
		 */
		public void bindViewHolderByData(final ItemModel itemModel) {
			progressBar.setProgress(itemModel.getProgress());
			button.setText(itemModel.getTag() + ":Pos" + getPosition());
			button.setOnClickListener(new OnClickListener() {// 点击开始更新进度条

				@Override
				public void onClick(View v) {
					new Thread(new Runnable() {

						@Override
						public void run() {// 模拟进度条更新
							for (int i = 0; i <= 100; i++) {
								updateProgressInUiThread(itemModel, i);
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}

					}).start();
				}
			});

			/** 为父控件容器添加长按删除事件 */
			this.itemView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					delOneDataInPos(getPosition());
					return false;
				}
			});

			btnDel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {// 点击删除
					delOneDataInPos(getPosition());
				}
			});
		}
	}

	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_ITEM_CHANGE:
				notifyItemChanged(msg.arg1);// 在UI线程更新进度
				break;
			default:
				break;
			}
			return false;
		}
	});

	/**
	 * 更新
	 * 
	 * @param itemModel
	 * @param i
	 */
	private void updateProgressInUiThread(ItemModel itemModel, int i) {
		int position = getPositionByUUID(itemModel.getUuid());
		if (position >= 0) {
			itemModel.setProgress(i);// 设置进度
			mDataset.set(position, itemModel);// 修改数据源
			// 通知Item
			Message message = handler.obtainMessage();
			message.what = MESSAGE_ITEM_CHANGE;
			message.arg1 = position;
			handler.sendMessage(message);
		}
	}

	/**
	 * 需要根据uuid，实时查找该消息的位置，以防止position在添加或删除时发生变化
	 * 
	 * @param uuid
	 * @return
	 */
	private int getPositionByUUID(String uuid) {
		for (int i = 0; i < mDataset.size(); i++) {
			if (uuid != null && uuid.equals(mDataset.get(i).getUuid())) {
				return i;
			}
		}
		return -1;
	}

	public MyAdapter() {
		mDataset = new ArrayList<ItemModel>();
	}

	public void addOneDataInHead(ItemModel itemModel) {
		mDataset.add(0, itemModel);
		this.notifyItemInserted(0);
	}

	public void addOneDataInTail(ItemModel itemModel) {
		mDataset.add(itemModel);
		this.notifyItemInserted(mDataset.size() - 1);
	}

	/**
	 * 在头部删除数据
	 * 
	 * @return
	 */
	public boolean delOneDataInHead() {
		if (mDataset.size() > 0) {
			mDataset.remove(0);
			this.notifyItemRemoved(0);
			return true;
		}
		return false;
	}

	/**
	 * 在尾部删除
	 * 
	 * @return
	 */
	public boolean delOneDataInTail() {
		if (mDataset.size() > 0) {
			mDataset.remove(mDataset.size() - 1);
			this.notifyItemRemoved(mDataset.size());
			return true;
		}
		return false;
	}

	/**
	 * 删除pos位置的数据
	 * 
	 * @param pos
	 * @return
	 */
	public boolean delOneDataInPos(int pos) {
		System.out.println("debug:del in " + pos);
		if (pos >= 0 && pos < mDataset.size()) {
			mDataset.remove(pos);
			this.notifyItemRemoved(pos);
			return true;
		}
		return false;
	}

	@Override
	public MyAdapter.RootViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		if (viewType == ItemModel.TYPE_PROGRESS) {//
			View v = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item, parent, false);
			ProgressViewHolder vh = new ProgressViewHolder(v);
			return vh;
		} else {// 未知消息类型,TODO
			View v = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item, parent, false);
			ProgressViewHolder vh = new ProgressViewHolder(v);
			return vh;
		}
	}

	@Override
	public void onBindViewHolder(RootViewHolder holder, int position) {
		if (holder instanceof ProgressViewHolder) {
			ProgressViewHolder pvh = (ProgressViewHolder) holder;
			pvh.bindViewHolderByData(mDataset.get(position));
		}
	}

	@Override
	public int getItemCount() {
		if (mDataset == null) {
			return 0;
		}
		return mDataset.size();
	}

	@Override
	public int getItemViewType(int position) {
		return mDataset.get(position).getType();
	}
}