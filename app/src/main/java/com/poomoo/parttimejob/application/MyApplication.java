package com.poomoo.parttimejob.application;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.poomoo.commlib.CrashHandler;
import com.poomoo.parttimejob.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义Application
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:26.
 */
public class MyApplication extends Application {
    // 用户信息
    private String userId = "";//用户编号
    private String nickName = "";//用户昵称
    private String realName = "";//用户实名
    private String headPic = "";//用户头像
    private String schoolName = "";//毕业学校名称
    private String intoSchoolDt = "";//入校时间
    private String tel = "";//电话号码
    private String idCardNum = "";//身份证编号
    private String idPicture = "";//身份证照片（取一张正面）
    private String password = "";//密码
    private String status = "";//状态（0:用户已经被禁用，1合法用户）
    private String inviteCode = "";//邀请码
    private String deviceNum = "";//设备编号
    private String insertDt = "";//注册时间
    private String updateDtv = "";//修改时间
    private String locateCity = "";//定位城市
    private String currCity = "";//展示城市

    private static MyApplication instance;//当前对象
    private List<Activity> activityList;//activity栈

    @Override
    public void onCreate() {
        super.onCreate();

        initImageLoader();
        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
        crashHandler.init(getApplicationContext());

    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.mipmap.ic_launcher) //
                .showImageOnFail(R.mipmap.ic_launcher) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
        ImageLoaderConfiguration config = new ImageLoaderConfiguration//
                .Builder(getApplicationContext())//
                .defaultDisplayImageOptions(defaultOptions)//
                .writeDebugLogs()//
                .build();//
        ImageLoader.getInstance().init(config);
    }

    public List<Activity> getActivityList() {
        if (activityList == null) {
            activityList = new ArrayList<>();
        }
        return activityList;
    }

    /**
     * 清除所有activity
     */
    public void clearAllActivity() {
        for (Activity activity : getActivityList()) {
            activity.finish();
        }
        getActivityList().clear();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getIntoSchoolDt() {
        return intoSchoolDt;
    }

    public void setIntoSchoolDt(String intoSchoolDt) {
        this.intoSchoolDt = intoSchoolDt;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getIdPicture() {
        return idPicture;
    }

    public void setIdPicture(String idPicture) {
        this.idPicture = idPicture;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getUpdateDtv() {
        return updateDtv;
    }

    public void setUpdateDtv(String updateDtv) {
        this.updateDtv = updateDtv;
    }

    public String getLocateCity() {
        return locateCity.replace("市", "");
    }

    public void setLocateCity(String locateCity) {
        this.locateCity = locateCity;
    }

    public String getCurrCity() {
        return currCity.replace("市", "");
    }

    public void setCurrCity(String currCity) {
        this.currCity = currCity;
    }
}
