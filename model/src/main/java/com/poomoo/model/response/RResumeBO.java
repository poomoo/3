package com.poomoo.model.response;

/**
 * Created by 李苜菲 on 2016/4/25.
 */
public class RResumeBO {
    public String headPic;//	--头像（选填）
    public String realName;//--用户姓名（必填）
    public int sex;//--性别（必填），1男，2女
    public String height;//--身高（选填）
    public String birthday;//--生日（选填）
    public int provinceId;//--省份编号（必填）
    public int cityId;//--城市编号（必填）
    public int areaId;//--区域编号（必填）
    public String schoolName;//--学校名称（选填）
    public String email;//-邮箱（选填）
    public String qqNum;//--QQ号码（选填）
    public String contactTel;//--联系电话（必填）
    public String workResume;//--工作简历（选填）
    public String workExp;//--工作经验（必填）
    public String insertDt;
    public String updateDt;
    public int resumeId;
}
