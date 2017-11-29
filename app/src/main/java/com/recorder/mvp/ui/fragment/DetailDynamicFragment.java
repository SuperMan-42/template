package com.recorder.mvp.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.base.BaseFragment;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.recorder.R;
import com.recorder.mvp.model.entity.DealDetailBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hpw on 17-11-29.
 */

public class DetailDynamicFragment extends BaseFragment {

    @BindView(R.id.dynamic_iv)
    ImageView dynamicIv;
    @BindView(R.id.img_count_tv)
    TextView imgCountTv;
    @BindView(R.id.dynamic_content_tv)
    TextView dynamicContentTv;
    @BindView(R.id.last_time_line)
    View lastTimeLine;
    @BindView(R.id.next_time_iv)
    ImageView nextTimeIv;
    @BindView(R.id.current_time_iv)
    ImageView currentTimeIv;
    @BindView(R.id.last_time_iv)
    ImageView lastTimeIv;
    @BindView(R.id.current_time_tv)
    TextView currentTimeTv;
    @BindView(R.id.imgs_layout)
    View imgsLayout;

    DealDetailBean.DataEntity.GrowthEntity mGrowth;

    public static DetailDynamicFragment newInstance(DealDetailBean.DataEntity.GrowthEntity entity) {
        DetailDynamicFragment fragment = new DetailDynamicFragment();
        fragment.setData(entity);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_dynamic, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (mGrowth.getImages() == null || mGrowth.getImages().isEmpty()) {
            imgCountTv.setText("暂无图片");
//            dynamicIv.setImageResource(R.drawable.ysh_brand);
            imgsLayout.setVisibility(View.GONE);
        } else {
            imgCountTv.setText(getString(R.string.imgs_count, "" + mGrowth.getImages().size()));
            CoreUtils.imgLoader(getContext(), "http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f19cc0079.jpg", dynamicIv);//TODO
            imgsLayout.setVisibility(View.VISIBLE);
        }
        if (mGrowth != null) {
            if (("" + mGrowth.getContent()).length() <= 30) {
                dynamicContentTv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            } else {
                dynamicContentTv.setGravity(Gravity.TOP | Gravity.LEFT);
            }
            dynamicContentTv.setText("" + mGrowth.getContent());
        }
        currentTimeTv.setText(mGrowth.getOcc_time());
    }

    @Override
    public void setData(Object data) {
        this.mGrowth = (DealDetailBean.DataEntity.GrowthEntity) data;
    }

    @OnClick(R.id.imgs_layout)
    public void onViewClicked() {
        List<LocalMedia> list = new ArrayList<>();
        for (String string : mGrowth.getImages()) {
            LocalMedia localMedia = new LocalMedia();
            localMedia.setPath(string);
            list.add(localMedia);
        }
        PictureSelector.create(this).externalPicturePreview(0, list);
    }
}
