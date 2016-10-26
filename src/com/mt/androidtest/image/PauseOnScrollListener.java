package com.mt.androidtest.image;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class PauseOnScrollListener implements OnScrollListener {

	private ImageLoader imageLoader;

	private final boolean pauseOnScroll;
	private final boolean pauseOnFling;

	public PauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
		this.imageLoader = imageLoader;
		this.pauseOnScroll = pauseOnScroll;
		this.pauseOnFling = pauseOnFling;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				imageLoader.resume();
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				if (pauseOnScroll) {
					imageLoader.pause();
				}
				break;
			case OnScrollListener.SCROLL_STATE_FLING:
				if (pauseOnFling) {
					imageLoader.pause();
				}
				break;
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}
}
