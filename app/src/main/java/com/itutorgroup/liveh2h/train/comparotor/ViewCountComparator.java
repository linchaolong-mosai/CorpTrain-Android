package com.itutorgroup.liveh2h.train.comparotor;

import com.itutorgroup.liveh2h.train.bean.usercourse.Courses;

import java.util.Comparator;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/20 0020 10:21
 * 邮箱：zhounianbin@mastercom.cn
 */
public class ViewCountComparator implements Comparator<Courses>{

    @Override
    public int compare(Courses lhs, Courses rhs) {
        if(lhs.getCourseInfo().getViewCount()-rhs.getCourseInfo().getViewCount()>0){
            return -1;
        }else if(lhs.getCourseInfo().getViewCount()-rhs.getCourseInfo().getViewCount()<0){
            return 1;
        }
        return 0;
    }
}
