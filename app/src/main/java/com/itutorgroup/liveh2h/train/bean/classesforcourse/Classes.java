package com.itutorgroup.liveh2h.train.bean.classesforcourse;

import java.io.Serializable;

public class Classes implements Serializable {
    private ClassInfo classInfo;

    private CreatorInfo creatorInfo;

    private int sequence;

    private int contentCount;

    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    public ClassInfo getClassInfo() {
        return this.classInfo;
    }

    public void setCreatorInfo(CreatorInfo creatorInfo) {
        this.creatorInfo = creatorInfo;
    }

    public CreatorInfo getCreatorInfo() {
        return this.creatorInfo;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getSequence() {
        return this.sequence;
    }

    public void setContentCount(int contentCount) {
        this.contentCount = contentCount;
    }

    public int getContentCount() {
        return this.contentCount;
    }

}