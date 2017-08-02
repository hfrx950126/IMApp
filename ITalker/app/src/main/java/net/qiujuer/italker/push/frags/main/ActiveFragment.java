package net.qiujuer.italker.push.frags.main;


import net.qiujuer.italker.common.app.Fragment;
import net.qiujuer.italker.common.widget.GalleryView;
import net.qiujuer.italker.push.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends Fragment {
//    @BindView(R.id.galleryView)
//    GalleryView mGalley;

    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
//        mGalley.setup(getLoaderManager(), new GalleryView.SelectedChangeListener() {
//            @Override
//            public void onSelectedCountChanged(int count) {
//
//            }
//        });
    }
}
