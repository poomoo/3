package com.poomoo.model.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李苜菲 on 2016/4/26.
 */
public class RIntentionBO {
    public List<cate> cateList;
    public String workDay;
    public String workAreaId;
    public String otherInfo;
    public List<Integer> type = new ArrayList<>();
    public List<Integer> area = new ArrayList<>();
    public List<Integer> work = new ArrayList<>();

    public class cate {
        public int cateId;
        public String cateName;
        public boolean selected;
    }
}
