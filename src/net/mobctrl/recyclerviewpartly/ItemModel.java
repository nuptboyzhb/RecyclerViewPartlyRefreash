package net.mobctrl.recyclerviewpartly;

/**
 * @date 2014年12月23日 下午4:36:28
 * @author Zheng Haibo
 * @Description:item的数据模型
 */
public class ItemModel {

	public static final int TYPE_COUNT = 2;// 2种类型

	public static final int TYPE_PROGRESS = 0;// 进度显示类型
	public static final int TYPE_ANIMATION = 1;// 动画类型

	private String uuid;// 用于标示此item的唯一ID

	private int type;// 显示的类型

	private int progress;// 存储进度

	private String tag;// 显示按钮的文本

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public ItemModel(String uuid, int progress, String tag, int type) {
		super();
		this.uuid = uuid;
		this.progress = progress;
		this.tag = tag;
		this.type = type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

}
