package com.mosai.corporatetraining.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rays on 16/5/16.
 */
public class CtUser implements Serializable {
    public String ctUserId;
    public int userSn; // 相当于userId
    public int ctRole;
    public String ctCompanyId;
    public String ctCompanyName;
    public boolean valid;
    public String name;
    public String email;
    public String phone;
    public String timeZone;
    public int userState;
    public List<CtGroup> groups;
}
