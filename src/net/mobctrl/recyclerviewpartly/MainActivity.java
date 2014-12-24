package net.mobctrl.recyclerviewpartly;

import java.util.UUID;

import net.mobctrl.recyclerviewpartlyrefreash.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * @date 2014年12月22日 下午3:40:15
 * @author Zheng Haibo
 * @Description:
 */
public class MainActivity extends Activity {

	private RecyclerView mRecyclerView;
	private MyAdapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;

	private Button btnAddInHead;
	private Button btnAddInTail;
	private Button btnDelInHead;
	private Button btnDelInTail;

	private int i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

		mLayoutManager = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, false);
		mRecyclerView.setLayoutManager(mLayoutManager);

		mAdapter = new MyAdapter();
		mRecyclerView.setAdapter(mAdapter);
		
		for (i = 0; i < 5; i++) {
			mAdapter.addOneDataInHead(new ItemModel(UUID.randomUUID()
					.toString(), 0, "item" + i, ItemModel.TYPE_PROGRESS));
		}

		btnAddInHead = (Button) findViewById(R.id.btn_add_head);
		btnAddInHead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAdapter.addOneDataInHead(new ItemModel(UUID.randomUUID()
						.toString(), 0, "item" + (++i), ItemModel.TYPE_PROGRESS));
				scollToTop();
			}
		});

		btnAddInTail = (Button) findViewById(R.id.btn_add_tail);
		btnAddInTail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAdapter.addOneDataInTail(new ItemModel(UUID.randomUUID()
						.toString(), 0, "item" + (++i), ItemModel.TYPE_PROGRESS));
				scollToBottom();
			}
		});

		btnDelInHead = (Button) findViewById(R.id.btn_del_head);
		btnDelInHead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isSucc = mAdapter.delOneDataInHead();
				if (!isSucc) {
					showNoItemTips();
					return;
				}
				scollToTop();
			}
		});

		btnDelInTail = (Button) findViewById(R.id.btn_del_tail);
		btnDelInTail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isSucc = mAdapter.delOneDataInTail();
				if (!isSucc) {
					showNoItemTips();
					return;
				}
				scollToBottom();
			}
		});

	}

	private void showNoItemTips() {
		Toast.makeText(getApplicationContext(),
				"no item in the RecyclerView...", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 滚动到头部
	 */
	private void scollToTop() {
		mRecyclerView.scrollToPosition(0);
	}

	/**
	 * 滚动到尾部
	 */
	private void scollToBottom() {
		mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
	}

}
