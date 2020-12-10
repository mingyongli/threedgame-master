package com.ws3dm.app.event;

import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/9/1  12:12
 * mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
		public void onItemClick(RecyclerView.ViewHolder viewHolder) {
			Toast.makeText(LinearActivity.this,"第"+viewHolder.getPosition()+"条",Toast.LENGTH_SHORT).show();
		}
		
		public void onItemLOngClick(RecyclerView.ViewHolder viewHolder) {
			Toast.makeText(LinearActivity.this,"长点击， 第"+viewHolder.getPosition()+"条",Toast.LENGTH_SHORT).show();
		}
 });
 */

public abstract class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener{

	private final RecyclerView recyclerView;
	private final GestureDetectorCompat mGestureDetector;

	public OnRecyclerItemClickListener(RecyclerView recyclerView){
		this.recyclerView=recyclerView;
		mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(),new ItemTouchHelperGestureListener());
	}

	@Override
	public void onTouchEvent(RecyclerView rv, MotionEvent e) {
		mGestureDetector.onTouchEvent(e);
	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
		mGestureDetector.onTouchEvent(e);
		return false;
	}

	public abstract void onItemClick(RecyclerView.ViewHolder viewHolder);

	public abstract void onItemLOngClick(RecyclerView.ViewHolder viewHolder);

	private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

		public  boolean onSingleTapUp(MotionEvent event){
			View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
			if (child != null){
				RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
				onItemClick(viewHolder);
			}
			return true;
		}

		public  void onLongPress(MotionEvent event){
			View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
			if (child != null){
				RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
				onItemLOngClick(viewHolder);
			}
		}
	}
}  
