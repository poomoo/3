/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.commlib.SPUtils;
import com.poomoo.commlib.picUtils.Bimp;
import com.poomoo.commlib.picUtils.FileUtils;
import com.poomoo.model.base.BaseRequestBO;
import com.poomoo.model.response.RAreaBO;
import com.poomoo.model.response.RResumeBO;
import com.poomoo.model.response.RUrl;
import com.poomoo.parttimejob.GlideCircleTransform;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.ZoneAdapter;
import com.poomoo.parttimejob.database.AreaInfo;
import com.poomoo.parttimejob.database.CityInfo;
import com.poomoo.parttimejob.database.DataBaseHelper;
import com.poomoo.parttimejob.database.ProvinceInfo;
import com.poomoo.parttimejob.event.Events;
import com.poomoo.parttimejob.event.RxBus;
import com.poomoo.parttimejob.presentation.ResumePresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.custom.CustomerDatePickerDialog;
import com.poomoo.parttimejob.ui.popup.HeightPopUpWindow;
import com.poomoo.parttimejob.ui.popup.SelectPicsPopupWindow;
import com.poomoo.parttimejob.view.ResumeView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.schedulers.Schedulers;

/**
 * 个人简历
 * 作者: 李苜菲
 * 日期: 2016/4/18 10:27.
 */
public class ResumeActivity extends BaseActivity implements ResumeView {
    @Bind(R.id.img_userAvatar)
    ImageView userAvatarImg;
    @Bind(R.id.edt_realName)
    EditText nameEdt;
    @Bind(R.id.rBtn_man)
    RadioButton manRbtn;
    @Bind(R.id.rBtn_woman)
    RadioButton womanRbtn;
    @Bind(R.id.txt_resumeHeight)
    TextView heightTxt;
    @Bind(R.id.txt_resumeDate)
    TextView dateTxt;
    @Bind(R.id.txt_province)
    TextView provinceTxt;
    @Bind(R.id.txt_city)
    TextView cityTxt;
    @Bind(R.id.txt_area)
    TextView areaTxt;
    @Bind(R.id.edt_schoolName)
    EditText schoolNameEdt;
    @Bind(R.id.edt_email)
    EditText emailEdt;
    @Bind(R.id.edt_qqNum)
    EditText qqNumEdt;
    @Bind(R.id.txt_resumeTel)
    TextView telTxt;
    @Bind(R.id.edt_workResume)
    EditText workResumeEdt;
    @Bind(R.id.edt_workExp)
    EditText workExpEdt;

    private Bitmap bitmap;
    private File file;
    private SelectPicsPopupWindow popupWindow;
    private static final int NONE = 0;
    private static final int PHOTOHRAPH = 1;// 拍照
    private static final int PHOTORESOULT = 2;// 结果

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private final static String image_capture_path = Environment.getExternalStorageDirectory() + "/" + "partTimeJob.png";

    private HeightPopUpWindow heightPopUpWindow = null;
    private ZoneAdapter adapter;
    private AddressPopUpWindow addressPopUpWindow;

    private List<String> provinceList = new ArrayList<>();
    private List<String> cityList = new ArrayList<>();
    private List<String> areaList = new ArrayList<>();
    private String provinceName;
    private String cityName;
    private String areaName;
    private int provinceId;
    private int cityId;
    private int areaId;
    public int selectedPosition;
    private ResumePresenter resumePresenter;

    private String headPic;
    private int sex = 1;
    private String realName;
    private String height;
    private String birthday;
    private String schoolName;
    private String email;
    private String qqNum;
    private String workResume;
    private String workExp;

    private List<ProvinceInfo> province_list;
    private List<CityInfo> city_list;
    private List<AreaInfo> area_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        ButterKnife.bind(this);
        resumePresenter = new ResumePresenter(this);
        initView();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_resume);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_resume;
    }


    private void initView() {
        userAvatarImg.setOnClickListener(v -> select_pics());
        telTxt.setText(application.getTel());

        adapter = new ZoneAdapter(this);
        addressPopUpWindow = new AddressPopUpWindow(this);

        getCity();
    }

    private void select_pics() {
        // 实例化SelectPicPopupWindow
        popupWindow = new SelectPicsPopupWindow(this, itemsOnClick);
        // 显示窗口
        popupWindow.showAtLocation(this.findViewById(R.id.llayout_resume),
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
        userAvatarImg.setImageBitmap(bitmap);
        file = FileUtils.saveBitmapByPath(bitmap, image_capture_path);
        LogUtils.d(TAG, "选择的图片文件:" + file);
    }


    @OnClick({R.id.rBtn_man, R.id.rBtn_woman})
    void sex(View view) {
        switch (view.getId()) {
            case R.id.rBtn_man:
                sex = 1;
                break;
            case R.id.rBtn_woman:
                sex = 2;
                break;
        }
    }

    public void selectHeight(View view) {
        if (heightPopUpWindow == null)
            heightPopUpWindow = new HeightPopUpWindow(this, heightCategory);
        heightPopUpWindow.showAtLocation(this.findViewById(R.id.llayout_resume), Gravity.CENTER, 0, 0);
    }

    HeightPopUpWindow.SelectCategory heightCategory = new HeightPopUpWindow.SelectCategory() {
        @Override
        public void selectCategory(String height) {
            ResumeActivity.this.height = height;
            heightTxt.setText(height);
        }
    };

    public void selectDate(View view) {
        pickDate();
    }

    private Calendar cal = Calendar.getInstance();
    private int nYear;
    private int nMonth;
    private int nDay;
    private String playDt;

    private void pickDate() {

        final DatePickerDialog mDialog = new CustomerDatePickerDialog(this, null, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        // 手动设置按钮
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", (dialog, which) -> {
            // 通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
            DatePicker datePicker = mDialog.getDatePicker();
            nYear = datePicker.getYear();
            nMonth = datePicker.getMonth() + 1;
            nDay = datePicker.getDayOfMonth();

            playDt = nYear + "-" + nMonth + "-" + nDay;
            birthday = playDt;
            dateTxt.setText(playDt);

        });
        // 取消按钮，如果不需要直接不设置即可
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", (dialog, which) -> {
        });

        mDialog.show();

        DatePicker dp = mDialog.getDatePicker();
        dp.setMaxDate(cal.getTime().getTime());
    }

    public void selectProvince(View view) {
        adapter.setCurrAddress(ZoneAdapter.PROVINCE);
        provinceList = DataBaseHelper.getProvince();
        if (!TextUtils.isEmpty(provinceName)) {
            selectedPosition = provinceList.indexOf(provinceName + "#" + provinceId);
            adapter.setSelectedPosition(selectedPosition);
        }
        adapter.setItems(provinceList);
        LogUtils.d(TAG, "selectProvince:" + provinceName + " " + selectedPosition);
        addressPopUpWindow.showAtLocation(this.findViewById(R.id.llayout_resume), Gravity.CENTER, 0, 0);
    }

    public void selectCity(View view) {
        if (provinceId == 0)
            return;

        adapter.setCurrAddress(ZoneAdapter.CITY);
        cityList = DataBaseHelper.getCity(provinceId);
        if (cityList.size() == 0)
            return;
        if (!TextUtils.isEmpty(cityName)) {
            selectedPosition = cityList.indexOf(cityName + "#" + cityId);
            adapter.setSelectedPosition(selectedPosition);
        }
        adapter.setItems(cityList);
        LogUtils.d(TAG, "selectCity:" + cityName + " " + selectedPosition);
        addressPopUpWindow.showAtLocation(this.findViewById(R.id.llayout_resume), Gravity.CENTER, 0, 0);
    }

    public void selectArea(View view) {
        if (cityId == 0)
            return;

        adapter.setCurrAddress(ZoneAdapter.AREA);
        areaList = DataBaseHelper.getArea(cityId);
        if (areaList.size() == 0)
            return;
        if (!TextUtils.isEmpty(areaName)) {
            selectedPosition = areaList.indexOf(areaName + "#" + areaId);
            adapter.setSelectedPosition(selectedPosition);
        }
        adapter.setItems(areaList);
        LogUtils.d(TAG, "selectArea:" + areaName + " " + selectedPosition);
        addressPopUpWindow.showAtLocation(this.findViewById(R.id.llayout_resume), Gravity.CENTER, 0, 0);
    }

    public class AddressPopUpWindow extends PopupWindow {
        private View mMenuView;
        private ListView list_address;
        private Button confirmBtn;

        public AddressPopUpWindow(Activity context) {
            super(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mMenuView = inflater.inflate(R.layout.popup_resume, null);
            list_address = (ListView) mMenuView.findViewById(R.id.list_address);
            confirmBtn = (Button) mMenuView.findViewById(R.id.btn_resumeConfirm);

            list_address.setAdapter(adapter);
            list_address.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MyUtils.getScreenHeight(ResumeActivity.this) / 2));

            this.setContentView(mMenuView);
            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            this.setFocusable(true);
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            this.setBackgroundDrawable(dw);

            list_address.setOnItemClickListener((parent, view, position, id) -> {
                selectedPosition = position;
                adapter.setSelectedPosition(position);
            });

            confirmBtn.setOnClickListener(v -> {
                dismiss();
                if (selectedPosition == -1)
                    return;
                String temp[];
                switch (adapter.getCurrAddress()) {
                    case ZoneAdapter.PROVINCE:
                        temp = provinceList.get(selectedPosition).split("#");
                        provinceName = temp[0];
                        provinceTxt.setText(provinceName);
                        provinceId = Integer.parseInt(temp[1]);
                        cityId = 0;
                        cityTxt.setText("选择城市");
                        areaTxt.setText("选择城区");
                        break;
                    case ZoneAdapter.CITY:
                        temp = cityList.get(selectedPosition).split("#");
                        cityName = temp[0];
                        cityTxt.setText(cityName);
                        cityId = Integer.parseInt(temp[1]);
                        areaTxt.setText("选择城区");
                        break;
                    case ZoneAdapter.AREA:
                        temp = areaList.get(selectedPosition).split("#");
                        areaName = temp[0];
                        areaTxt.setText(areaName);
                        areaId = Integer.parseInt(temp[1]);
                        break;
                }
            });

            mMenuView.setOnTouchListener((v, event) -> {
                int height_top = mMenuView.findViewById(R.id.llayout_resume).getTop();
                int height_bottom = mMenuView.findViewById(R.id.llayout_resume).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height_top || y > height_bottom) {
                        dismiss();
                    }
                }
                return true;
            });
        }
    }

    public void toSubmit(View view) {
        realName = nameEdt.getText().toString().trim();
        if (TextUtils.isEmpty(realName)) {
            MyUtils.showToast(getApplicationContext(), MyConfig.nameEmpty);
            return;
        }
        if (provinceId == 0) {
            MyUtils.showToast(getApplicationContext(), MyConfig.provinceEmpty);
            return;
        }
        if (cityId == 0) {
            MyUtils.showToast(getApplicationContext(), MyConfig.cityEmpty);
            return;
        }
        if (areaId == 0) {
            MyUtils.showToast(getApplicationContext(), MyConfig.areaEmpty);
            return;
        }
        workExp = workExpEdt.getText().toString().trim();
        if (TextUtils.isEmpty(workExp)) {
            MyUtils.showToast(getApplicationContext(), MyConfig.workExpEmpty);
            return;
        }
        email = emailEdt.getText().toString().trim();
        qqNum = qqNumEdt.getText().toString().trim();
        schoolName = schoolNameEdt.getText().toString().trim();
        workResume = workResumeEdt.getText().toString().trim();
        showProgressDialog(getString(R.string.dialog_msg));
        if (file == null)
            resumePresenter.changeResume(application.getUserId(), headPic, realName, sex, height, birthday, provinceId, cityId, areaId, schoolName, email, qqNum, application.getTel(), workResume, workExp);
        else
            resumePresenter.uploadPic(file);
    }

    public void getCity() {
        if (DataBaseHelper.getProvinceList().size() == 1) {
            showProgressDialog("同步城市中,请稍后...");
            BaseRequestBO baseRequestBO = new BaseRequestBO(NetConfig.COMMACTION, NetConfig.CITY);
            Network.getCommApi().getCitys(baseRequestBO)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new AbsAPICallback<List<RAreaBO>>() {
                        @Override
                        protected void onError(ApiException e) {
                            runOnUiThread(() -> {
                                closeProgressDialog();
                                MyUtils.showToast(getApplicationContext(), e.getMessage());
                                finish();
                            });
                        }

                        @Override
                        public void onNext(List<RAreaBO> rAreaBOs) {
                            city_list = new ArrayList<>();
                            province_list = new ArrayList<>();
                            city_list = new ArrayList<>();
                            area_list = new ArrayList<>();
                            for (RAreaBO rAreaBO : rAreaBOs) {
                                ProvinceInfo provinceInfo = new ProvinceInfo(rAreaBO.provinceId, rAreaBO.provinceName);
                                city_list = new ArrayList<>();
                                for (RAreaBO.city city : rAreaBO.cityList) {
                                    CityInfo cityInfo = new CityInfo(city.cityId, city.cityName, city.isHot, provinceInfo.getProvinceId());
                                    city_list.add(cityInfo);
                                    area_list = new ArrayList<>();
                                    for (RAreaBO.area area : city.areaList)
                                        area_list.add(new AreaInfo(area.areaId, area.areaName, cityInfo.getCityId()));
                                    DataBaseHelper.saveArea(area_list);
                                    city_list.add(cityInfo);
                                }
                                DataBaseHelper.saveCity(city_list);
                                province_list.add(provinceInfo);
                            }
                            DataBaseHelper.saveProvince(province_list);
                            runOnUiThread(() -> {
                                closeProgressDialog();
                                showProgressDialog("同步简历中," + getString(R.string.dialog_msg));
                                resumePresenter.downResume(application.getUserId());
                            });
                        }
                    });
        } else {
            showProgressDialog("同步简历中," + getString(R.string.dialog_msg));
            resumePresenter.downResume(application.getUserId());
        }
    }


    @Override
    public void failed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void upLoadSucceed(RUrl rUrl) {
        headPic = rUrl.picUrl;
        resumePresenter.changeResume(application.getUserId(), headPic, realName, sex, height, birthday, provinceId, cityId, areaId, schoolName, email, qqNum, application.getTel(), workResume, workExp);
    }

    @Override
    public void submitSucceed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
        finish();
    }

    @Override
    public void downSucceed(RResumeBO rResumeBO) {
        closeProgressDialog();
        headPic = rResumeBO.headPic;
        realName = rResumeBO.realName;
        sex = rResumeBO.sex;
        height = rResumeBO.height;
        birthday = rResumeBO.birthday;
        provinceId = rResumeBO.provinceId;
        cityId = rResumeBO.cityId;
        areaId = rResumeBO.areaId;
        cityList = DataBaseHelper.getCity(provinceId);
        areaList = DataBaseHelper.getArea(cityId);
        schoolName = rResumeBO.schoolName;
        email = rResumeBO.email;
        qqNum = rResumeBO.qqNum;
        workResume = rResumeBO.workResume;
        workExp = rResumeBO.workExp;
        String temp[] = DataBaseHelper.getProvinceCityArea(provinceId, cityId, areaId);
        provinceName = temp[0];
        cityName = temp[1];
        areaName = temp[2];
        provinceTxt.setText(provinceName);
        cityTxt.setText(cityName);
        areaTxt.setText(areaName);

        Glide.with(this).load(headPic).placeholder(R.drawable.ic_defalut_avatar).transform(new GlideCircleTransform(this)).into(userAvatarImg);
        nameEdt.setText(realName);
        if (sex == 1) manRbtn.setChecked(true);
        else womanRbtn.setChecked(true);
        heightTxt.setText(height);
        dateTxt.setText(birthday);
        schoolNameEdt.setText(schoolName);
        emailEdt.setText(email);
        qqNumEdt.setText(qqNum);
        workResumeEdt.setText(workResume);
        workExpEdt.setText(workExp);

        SPUtils.put(this, getString(R.string.sp_headPic), headPic);
        SPUtils.put(this, getString(R.string.sp_realName), realName);
        SPUtils.put(this, getString(R.string.sp_resumeId), rResumeBO.resumeId);
        application.setHeadPic(headPic);
        application.setRealName(realName);

        RxBus.getInstance().send(Events.EventEnum.DELIVER_AVATAR, null);
    }

    @Override
    public void downFailed(String msg) {
        closeProgressDialog();
        if (!msg.contains("参数为空")) {//-2表示没有简历
            MyUtils.showToast(getApplicationContext(), msg);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resumePresenter.onDestroy();
    }
}
