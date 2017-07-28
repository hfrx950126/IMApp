package net.qiujuer.italker.push.frags.account;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yalantis.ucrop.UCrop;

import net.qiujuer.italker.common.app.Fragment;
import net.qiujuer.italker.common.widget.PortraitView;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.frags.media.GalleryFragment;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用于更新信息的界面
 */
public class UpdateInfoFragment extends Fragment {
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick(){
       new GalleryFragment()
               .setListener(new GalleryFragment.OnSelectedListener() {
                   @Override
                   public void onSelectedImage(String path) {
                       UCrop.Options options = new UCrop.Options();
                       //设置图片处理的格式JPEG
                       options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                       //设置压缩后的图片精度
                       options.setCompressionQuality(96);
                       File dPath =
                       UCrop.of(Uri.fromFile(new File(path)),)
                   }
               })
               //show的时候建议使用getChildFragment，
               //tag GalleryFragment class 名
               .show(getChildFragmentManager(),
               GalleryFragment.class.getName());
    }
}
