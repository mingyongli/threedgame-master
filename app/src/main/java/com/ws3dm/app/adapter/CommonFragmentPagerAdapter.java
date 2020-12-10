package com.ws3dm.app.adapter;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import android.view.ViewGroup;

public class CommonFragmentPagerAdapter<T> extends FragmentPagerAdapter {
	private ArrayList<T> fragments; 
	private FragmentManager fm;

    public CommonFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    	this.fm = fm;
    }

    public CommonFragmentPagerAdapter(FragmentManager fm, ArrayList<T> fragments) {
        super(fm);
		this.fm = fm;
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment)fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    
    
	public void setFragments(ArrayList<T> fragments) {
		if(this.fragments != null){
			FragmentTransaction ft = fm.beginTransaction();
			for(T f:this.fragments){
				ft.remove((Fragment)f);
			}
			ft.commit();
			ft=null;
			fm.executePendingTransactions();
		}
		this.fragments = fragments;
		notifyDataSetChanged();
	}
	
	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
	    Object obj = super.instantiateItem(container, position);
	    return obj;
	}
		
}