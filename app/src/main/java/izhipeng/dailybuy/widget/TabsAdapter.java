package izhipeng.dailybuy.widget;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.HashMap;

import izhipeng.dailybuy.MainActivity;

/**
 * Adapter that backs the ViewPager tabs on the phone UI.
 */
public class TabsAdapter extends FragmentPagerAdapter
        implements ViewPager.OnPageChangeListener {

    private static final String TAG = TabsAdapter.class.getSimpleName();
    private final HashMap<Integer, Fragment> mFragments;
    private final ArrayList<Integer> mTabNums;
    private final ArrayList<CharSequence> mTabTitles;
    private FragmentChangeListener changeListener;

    private static int currentTabPosition;

    @SuppressLint("UseSparseArrays")
    public TabsAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new HashMap<Integer, Fragment>(10);
        mTabNums = new ArrayList<Integer>(10);
        mTabTitles = new ArrayList<CharSequence>(10);
    }


    public TabsAdapter(FragmentManager fm, MainActivity activity) {
        super(fm);
        changeListener = (FragmentChangeListener) activity;
        mFragments = new HashMap<Integer, Fragment>(10);
        mTabNums = new ArrayList<Integer>(10);
        mTabTitles = new ArrayList<CharSequence>(10);
    }


    public void addTab(String tabTitle, Fragment newFragment, int tabId) {
        mTabTitles.add(tabTitle);
        mFragments.put(tabId, newFragment);
        mTabNums.add(tabId);
        currentTabPosition = tabId;
        notifyDataSetChanged();
    }

    //    private void layoutPhoneForPortrait() {
//        layoutFullscreenVideo(false);
//        SessionSummaryFragment sessionSummaryFragment =
//                (SessionSummaryFragment) mTabsAdapter.getTabFragment(TABNUM_SESSION_SUMMARY);
//        if (sessionSummaryFragment != null) {
//            sessionSummaryFragment.updateViews();
//        }
//    }
//    从上面可以看出这个方法的作用
    public Fragment getTabFragment(int tabNum) {
        if (mFragments.containsKey(tabNum)) {
            return mFragments.get(tabNum);
        }
        return null;
    }

    /**
     * A data set change may involve pages being added, removed, or changing position. The
     * ViewPager will keep the current page active provided(如果) the adapter implements
     * the method {@link #getItemPosition(Object)}
     */

    @Override
    public int getItemPosition(Object object) {


        int position = super.getItemPosition(object);

        Log.d(TAG, "getItemPosition==" + position);
        return position;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//            if (mFragments[position] == null) { //mFragments is the array of Fragments
//                mFragments[position] = new Fragment();
//            }
//            return mFragments[position];
        return super.instantiateItem(container, position);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mTabTitles.get(position);
//        Drawable image = App.get().getResources().getDrawable(imageResId[position]);
//        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//
//        SpannableString sb = new SpannableString("\n\n\n"+mTabTitles.get(position));
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return sb;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(mTabNums.get(position));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    // fuck
    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected--position--"+position);
        currentTabPosition = position;
        if (changeListener != null) {
            changeListener.onFragmentChanged(position);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public interface FragmentChangeListener {
        void onFragmentChanged(int position);
    }

}