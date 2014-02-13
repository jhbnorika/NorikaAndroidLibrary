
package com.norika.android.library.base.adapter;

import java.util.List;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

public abstract class IFragmentStatePagerAdapter<T extends Parcelable> extends FragmentStatePagerAdapter {
    private static final String ARG_OBJECT = "detailobj";
    private final List<T> list;
    private final int maxSize;

    public IFragmentStatePagerAdapter(FragmentManager fm, List<T> list, int maxSize) {
        super(fm);
        if (list == null)
            throw new IllegalArgumentException("the list is not null");
        this.list = list;
        this.maxSize = maxSize;
    }

    @Override
    public int getCount() {
        return list.size() < maxSize ? list.size() + 1 : maxSize;
    }

    // @Override
    // public View instantiateItem(ViewGroup container, int position) {
    // ImageView imageView = new ImageView(container.getContext());
    // imageView.setImageResource(sDrawables[position]);
    //
    // // Now just add ImageView to ViewPager and return it
    // container.addView(imageView, LayoutParams.MATCH_PARENT,
    // LayoutParams.MATCH_PARENT);
    //
    // return imageView;
    // }

    // @Override
    // public void destroyItem(ViewGroup container, int position, Object object)
    // {
    // container.removeView((View) object);
    // }

    // @Override
    // public boolean isViewFromObject(View view, Object object) {
    // return view == object;
    // }

    @Override
    public abstract Fragment getItem(int position);

    // FIXME 示例
    // @Override
    // public Fragment getItem(int position) {
    // AnjukeDetailFragment fragment = new AnjukeDetailFragment();
    // Bundle args = new Bundle();
    //
    // args.putParcelable(AnjukeDetailPagerAdapter.ARG_OBJECT,
    // getListData(position));
    // args.putInt("oldPosition", position);
    // fragment.setArguments(args);
    //
    // return fragment;
    // }

    @Override
    public int getItemPosition(Object object) {
        Fragment f = (Fragment) object;
        Bundle b = f.getArguments();

        T t = b.getParcelable(ARG_OBJECT);
        int oldPosition = b.getInt("oldPosition");
        if (oldPosition < list.size() && list.get(oldPosition).equals(t))
            return PagerAdapter.POSITION_UNCHANGED;
        else
            return PagerAdapter.POSITION_NONE;

        // return super.getItemPosition(object);
    }

    protected T getListData(int position) {
        return position < list.size() ? list.get(position) : null;
    }
}
