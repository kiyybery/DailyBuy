package izhipeng.dailybuy.myprefire;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import izhipeng.dailybuy.BaseFragment;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.widget.SlidingTabLayoutForOrder;
import izhipeng.dailybuy.widget.TabsAdapter;

/**
 * Created by Administrator on 2016/8/16 0016.
 */
public class MyCollectionFragment extends BaseFragment {

    private SlidingTabLayoutForOrder mSlidingTabLayout;
    private TabsAdapter mTabsAdapter;
    private FragmentManager fm;
    private ViewPager mViewPager;

    private MyCollecContentFragment myCollecContentFragment;
    private MyCollecStoreFragment myCollecStoreFragment;

    private LinearLayout ll_section_title_back;
    private TextView mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myCollecContentFragment = MyCollecContentFragment.newInstance();
        myCollecStoreFragment = MyCollecStoreFragment.newInstance();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fm = getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.avtivity_mycollection, container, false);

        ll_section_title_back = (LinearLayout) view.findViewById(R.id.ll_section_title_back);
        ll_section_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        mTitle = (TextView) view.findViewById(R.id.tv_section_title_title);
        mTitle.setText("我的收藏");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = (ViewPager) view.findViewById(R.id.activity_container);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setPageMargin(5);//??
//        mViewPager.setOnPageChangeListener(onPageChangeListener);
        mTabsAdapter = new TabsAdapter(fm);
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager.setAdapter(mTabsAdapter);

        mTabsAdapter.addTab("内容", myCollecContentFragment, 0);//
        mTabsAdapter.addTab("店铺", myCollecStoreFragment, 1);//

        mSlidingTabLayout =
                (SlidingTabLayoutForOrder) view.findViewById(R.id.check_layout);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator_tint_bottom, R.id.tab_text_title);

        Resources res = getResources();
        mSlidingTabLayout.setSelectedIndicatorColors(
                res.getColor(R.color.youhui_red));

        mSlidingTabLayout.setDistributeEvenly(true);

        mSlidingTabLayout.setViewPager(mViewPager);
    }
}
