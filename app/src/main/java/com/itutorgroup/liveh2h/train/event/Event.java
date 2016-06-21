package com.itutorgroup.liveh2h.train.event;

import com.itutorgroup.liveh2h.train.bean.classesforcourse.Classes;
import com.itutorgroup.liveh2h.train.bean.resourseforclass.Resources;
import com.itutorgroup.liveh2h.train.bean.usercourse.Courses;

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
    public static class UpdateMyCourses{
        public Classes classes;
    }
    public static class UpdateViewcount{
        public Courses courses;
    }
}
