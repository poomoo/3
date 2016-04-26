/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.commlib.SPUtils;
import com.poomoo.commlib.picUtils.Bimp;
import com.poomoo.commlib.picUtils.FileUtils;
import com.poomoo.model.response.RUrl;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.presentation.AuthPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.popup.SelectPicsPopupWindow;
import com.poomoo.parttimejob.view.AuthView;
import com.poomoo.parttimejob.view.PubView;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 实名认证
 * 作者: 李苜菲
 * 日期: 2016/4/18 11:03.
 */
public class AuthActivity extends BaseActivity implements AuthView {
    @Bind(R.id.edt_authName)
    EditText nameEdt;
    @Bind(R.id.edt_authSchoolName)
    EditText schoolNameEdt;
    @Bind(R.id.edt_authDate)
    EditText dateEdt;
    @Bind(R.id.txt_authTel)
    TextView telTxt;
    @Bind(R.id.edt_authIdNum)
    EditText idNumEdt;
    @Bind(R.id.img_idCard)
    ImageView idCardImg;

    private AuthPresenter authPresenter;
    private String name;
    private String school;
    private String date;
    private String idNum;
    private String idPic;

    private Bitmap bitmap;
    private File file;
    private SelectPicsPopupWindow popupWindow;
    private static final int NONE = 0;
    private static final int PHOTOHRAPH = 1;// 拍照
    private static final int PHOTORESOULT = 2;// 结果

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private final static String image_capture_path = Environment.getExternalStorageDirectory() + "/" + "partTimeJob.temp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        authPresenter = new AuthPresenter(this);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_auth);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_auth;
    }

    private void initView() {
        if (!TextUtils.isEmpty((String) SPUtils.get(this, getString(R.string.sp_realName), "")))
            nameEdt.setText((String) SPUtils.get(this, getString(R.string.sp_realName), ""));
        if (!TextUtils.isEmpty((String) SPUtils.get(this, getString(R.string.sp_schoolName), "")))
            schoolNameEdt.setText((String) SPUtils.get(this, getString(R.string.sp_schoolName), ""));
        if (!TextUtils.isEmpty((String) SPUtils.get(this, getString(R.string.sp_intoSchoolDt), "")))
            dateEdt.setText((String) SPUtils.get(this, getString(R.string.sp_intoSchoolDt), ""));
        if (!TextUtils.isEmpty((String) SPUtils.get(this, getString(R.string.sp_idCardNum), "")))
            idNumEdt.setText((String) SPUtils.get(this, getString(R.string.sp_idCardNum), ""));
        if (!TextUtils.isEmpty((String) SPUtils.get(this, getString(R.string.sp_idPicture), "")))
            Glide.with(this).load((String) SPUtils.get(this, getString(R.string.sp_idPicture), "")).into(idCardImg);

        telTxt.setText(application.getTel());
    }

    /**
     * @param View
     */
    public void selectIdImg(View View) {
        select_pics();
    }

    private void select_pics() {
        // 实例化SelectPicPopupWindow
        popupWindow = new SelectPicsPopupWindow(this, itemsOnClick);
        // 显示窗口
        popupWindow.showAtLocation(this.findViewById(R.id.llayout_auth),
                Gravity.BOTTOM, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            popupWindow.dismiss();
            switch (view.getId()) {
                case R.id.btn_camera:
                    camera();
                    break;
                case R.id.btn_photo:
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, PHOTORESOULT);
                    break;
            }
        }
    };

    private void camera() {
        Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
        intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(image_capture_path)));
        startActivityForResult(intent1, PHOTOHRAPH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;
        // 拍照
        if (requestCode == PHOTOHRAPH) {
            System.out.println("拍照返回");
            setImage(image_capture_path);
        }
        if (data == null) {
            System.out.println("返回为空");
            return;
        }
        // 处理结果
        if (requestCode == PHOTORESOULT) {
            // 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
            Uri mImageCaptureUri = data.getData();
            System.out.println("mImageCaptureUri:" + mImageCaptureUri);
            // 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
            if (mImageCaptureUri != null) {
                try {
                    String imagePath;
                    Cursor cursor = getContentResolver().query(mImageCaptureUri, new String[]{MediaStore.Images.Media.DATA}, null,
                            null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    imagePath = cursor.getString(columnIndex); // 从内容提供者这里获取到图片的路径
                    cursor.close();
                    setImage(imagePath);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setImage(String path) {
        try {
            bitmap = Bimp.revitionImageSize(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        idCardImg.setImageBitmap(bitmap);
        file = FileUtils.saveBitmapByPath(bitmap, image_capture_path);
        LogUtils.d(TAG, "选择的图片文件:" + file);
    }

    public void toSubmit(View view) {
        name = nameEdt.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            nameEdt.setFocusable(true);
            nameEdt.requestFocus();
            MyUtils.showToast(getApplicationContext(), MyConfig.nameEmpty);
            return;
        }
        school = schoolNameEdt.getText().toString().trim();
        if (TextUtils.isEmpty(school)) {
            schoolNameEdt.setFocusable(true);
            schoolNameEdt.requestFocus();
            MyUtils.showToast(getApplicationContext(), MyConfig.schoolNameEmpty);
            return;
        }
        date = dateEdt.getText().toString().trim();
        if (TextUtils.isEmpty(date)) {
            dateEdt.setFocusable(true);
            dateEdt.requestFocus();
            MyUtils.showToast(getApplicationContext(), MyConfig.dateEmpty);
            return;
        }
        idNum = idNumEdt.getText().toString().trim();
        if (TextUtils.isEmpty(idNum)) {
            idNumEdt.setFocusable(true);
            idNumEdt.requestFocus();
            MyUtils.showToast(getApplicationContext(), MyConfig.idNumEmpty);
            return;
        }
        if (idNum.length() != 18) {
            idNumEdt.setFocusable(true);
            idNumEdt.requestFocus();
            MyUtils.showToast(getApplicationContext(), MyConfig.idNumIllegal);
            return;
        }
        showProgressDialog(getString(R.string.dialog_msg));
        authPresenter.uploadPic(file);
    }

    @Override
    public void failed(String msg) {
        closeProgressDialog();

    }

    @Override
    public void upLoadSucceed(RUrl rUrl) {
        idPic = rUrl.picUrl;
        authPresenter.auth(application.getUserId(), name, school, date, idNum, rUrl.picUrl);
    }

    @Override
    public void submitSucceed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
        SPUtils.put(this, getString(R.string.sp_realName), name);
        SPUtils.put(this, getString(R.string.sp_schoolName), school);
        SPUtils.put(this, getString(R.string.sp_intoSchoolDt), date);
        SPUtils.put(this, getString(R.string.sp_idCardNum), idNum);
        SPUtils.put(this, getString(R.string.sp_idPicture), idPic);
        finish();
    }
}
