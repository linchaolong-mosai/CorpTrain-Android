package com.mosai.corporatetraining.event;

import com.mosai.corporatetraining.bean.resourseforclass.Resources;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/6/8 0008 12:42
 * 邮箱：nianbin@mosainet.com
 */
public class Event {
    public static class NetChange{
        public boolean netChange;
    }
    public static  class UpdateAvatar{
        public boolean update;
    }
    public static class SubmitPercent{
        public Resources resources;
    }
    public static class SubmitRate{
        public float rate;
    }
}
