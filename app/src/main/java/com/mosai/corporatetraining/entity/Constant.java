package com.mosai.corporatetraining.entity;

/**
 * Created by Rays on 16/5/13.
 */
public interface Constant {
    // share type
    int COURSE_SHARE_TYPE_NONE = 1;    	// private course, default
    int COURSE_SHARE_TYPE_COMPANY = 2;	// company wide course
    int COURSE_SHARE_TYPE_SELECTED = 3;	// invitation-based course
    int COURSE_SHARE_TYPE_PUBLIC = 4;	// everyone can access

    // ct role
    int CT_ROLE_STUDENT = 0;	// can take course
    int CT_ROLE_TEACHER = 1;	// can create/modify/assign his/her own courses
    int CT_ROLE_ADMIN = 2;	    // can create/modify/assign all company-wide courses and manage users.
    int CT_ROLE_SUPER_ADMIN = 3;// can create/modify all courses across companies.
}
