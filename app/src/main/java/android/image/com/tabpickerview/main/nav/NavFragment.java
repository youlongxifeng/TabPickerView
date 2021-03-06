package android.image.com.tabpickerview.main.nav;

import android.content.Context;
import android.image.com.tabpickerview.R;
import android.image.com.tabpickerview.base.fragments.BaseFragment;
import android.image.com.tabpickerview.widget.NavigationButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by xiezhenggen on 2017/11/22.
 * 作者：xiezhenggen on 2017/11/22 19:32
 */

public class NavFragment extends BaseFragment implements View.OnClickListener,  View.OnLongClickListener {
    @BindView(R.id.nav_item_news)
    NavigationButton mNavNews;
    @BindView(R.id.nav_item_tweet)
    NavigationButton mNavTweet;
    @BindView(R.id.nav_item_explore)
    NavigationButton mNavExplore;
    @BindView(R.id.nav_item_me)
    NavigationButton mNavMe;
    @BindView(R.id.nav_item_tweet_pub)
    ImageView mNavPub;
    private Context mContext;
    private int mContainerId;
    private FragmentManager mFragmentManager;
    private NavigationButton mCurrentNavButton;
    private OnNavigationReselectListener mOnNavigationReselectListener;

    public NavFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nav;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

       /** ShapeDrawable lineDrawable = new ShapeDrawable(new BorderShape(new RectF(0, 1, 0, 0)));
      lineDrawable.getPaint().setColor(getResources().getColor(R.color.list_divider_color));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                new ColorDrawable(getResources().getColor(R.color.white)),
                lineDrawable
        });
        root.setBackgroundDrawable(layerDrawable);*/

        mNavNews.init(R.drawable.tab_icon_new,
                R.string.main_tab_name_news,
                DynamicTabFragment.class);

        mNavTweet.init(R.drawable.tab_icon_tweet,
                R.string.main_tab_name_tweet,
                DynamicTabFragment.class);

        mNavExplore.init(R.drawable.tab_icon_explore,
                R.string.main_tab_name_explore,
                DynamicTabFragment.class);

        mNavMe.init(R.drawable.tab_icon_me,
                R.string.main_tab_name_my,
                DynamicTabFragment.class);


    }

    @OnClick({R.id.nav_item_news, R.id.nav_item_tweet,
            R.id.nav_item_explore, R.id.nav_item_me,
            R.id.nav_item_tweet_pub})
    @Override
    public void onClick(View v) {
        if (v instanceof NavigationButton) {
            NavigationButton nav = (NavigationButton) v;
            doSelect(nav);
        } else if (v.getId() == R.id.nav_item_tweet_pub) {
            //PubActivity.show(getContext());
            //TweetPublishActivity.show(getContext(), mRoot.findViewById(R.id.nav_item_tweet_pub));
        }
    }

    @OnLongClick({R.id.nav_item_tweet_pub})
    @Override
    public boolean onLongClick(View v) {
       // TweetPublishActivity.show(getContext(), mRootView.findViewById(R.id.nav_item_tweet_pub));
        return false;
    }

    public void setup(Context context, FragmentManager fragmentManager, int contentId, OnNavigationReselectListener listener) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mContainerId = contentId;
        mOnNavigationReselectListener = listener;

        // do clear
        clearOldFragment();
        // do select first
        doSelect(mNavNews);
    }

    public void select(int index) {
        if (mNavMe != null)
            doSelect(mNavMe);
    }

    @SuppressWarnings("RestrictedApi")
    private void clearOldFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (transaction == null || fragments == null || fragments.size() == 0)
            return;
        boolean doCommit = false;
        for (Fragment fragment : fragments) {
            if (fragment != this && fragment != null) {
                transaction.remove(fragment);
                doCommit = true;
            }
        }
        if (doCommit)
            transaction.commitNow();
    }

    private void doSelect(NavigationButton newNavButton) {
        // If the new navigation is me info fragment, we intercept it
        /*
        if (newNavButton == mNavMe) {
            if (interceptMessageSkip())
                return;
        }
        */

        NavigationButton oldNavButton = null;
        if (mCurrentNavButton != null) {
            oldNavButton = mCurrentNavButton;
            if (oldNavButton == newNavButton) {
                onReselect(oldNavButton);
                return;
            }
            oldNavButton.setSelected(false);
        }
        newNavButton.setSelected(true);
        doTabChanged(oldNavButton, newNavButton);
        mCurrentNavButton = newNavButton;
    }

    private void doTabChanged(NavigationButton oldNavButton, NavigationButton newNavButton) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft.detach(oldNavButton.getFragment());
            }
        }
        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                Fragment fragment = Fragment.instantiate(mContext,
                        newNavButton.getClx().getName(), null);
                ft.add(mContainerId, fragment, newNavButton.getTag());
                newNavButton.setFragment(fragment);
            } else {
                ft.attach(newNavButton.getFragment());
            }
        }
        ft.commit();
    }

    /**
     * 拦截底部点击，当点击个人按钮时进行消息跳转
     */
 /*   private boolean interceptMessageSkip() {
        NoticeBean bean = NoticeManager.getNotice();
        if (bean.getAllCount() > 0) {
            if (bean.getLetter() + bean.getMention() + bean.getReview() > 0)
                UserMessageActivity.show(getActivity());
            else
                UserFansActivity.show(getActivity(), AccountHelper.getUserId());
            return true;
        }
        return false;
    }*/

    private void onReselect(NavigationButton navigationButton) {
        OnNavigationReselectListener listener = mOnNavigationReselectListener;
        if (listener != null) {
            listener.onReselect(navigationButton);
        }
    }


    public interface OnNavigationReselectListener {
        void onReselect(NavigationButton navigationButton);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void initData() {
        super.initData();

    }
}
