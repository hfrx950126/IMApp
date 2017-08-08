package net.qiujuer.italker.push.frags.search;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.factory.model.card.UserCard;
import com.example.factory.presenter.search.SearchContract;
import com.example.factory.presenter.search.SearchUserPresenter;

import net.qiujuer.italker.common.app.PresenterFragment;
import net.qiujuer.italker.common.widget.EmptyView;
import net.qiujuer.italker.common.widget.PortraitView;
import net.qiujuer.italker.common.widget.recycler.RecyclerAdapter;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.activities.SearchActivity;

import java.util.List;

import butterknife.BindView;

import static com.igexin.push.core.g.R;

/**
 * 搜索人的界面实现
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment, SearchContract.UserView {
    @BindView(R.id.empty)
    EmptyView mEmptyView;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    public SearchUserFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        //初始化Recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(new RecyclerAdapter<UserCard>() {
            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                //返回cell的布局ID
                return 0;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        });
    }

    @Override
    public void search(String content) {
        //Activity->Fragment->Presenter->Net
        mPresenter.search(content);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        //数据成功的情况下返回数据
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        //初始化Presenter
        return new SearchUserPresenter(this);
    }

    /**
     * 每一个cell的布局操作
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard>{
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;
        @BindView(R.id.txt_name)
        TextView mName;
        @BindView(R.id.im_follow)
        ImageView mFollow;
        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(UserCard userCard) {

        }
    }
}
