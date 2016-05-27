package com.mosai.corporatetraining.bean.quiz;

import java.io.Serializable;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/27 0027 17:06
 * 邮箱：nianbin@mosainet.com
 */
public class QuizSummary implements Serializable {
    public float passingGrade,passRatio;
    public boolean pass;
    public int grade,passCount,totalAnswer;
    /**
     * {
     * "summary":{"
     * passingGrade":0.0,
     * "grade":0,
     * "passCount":0,
     * "totalAnswer":0,
     * "passRatio":0.0,
     * "pass":false}
     * }
     */
}
