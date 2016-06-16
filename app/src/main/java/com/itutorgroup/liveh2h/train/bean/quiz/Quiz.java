package com.itutorgroup.liveh2h.train.bean.quiz;

import java.io.Serializable;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/27 0027 10:35
 * 邮箱：nianbin@mosainet.com
 */
public class Quiz implements Serializable{
    /**
     * {
     "returnCode": 0,
     "quiz": {
     "quizId": "EEEEEEEE-EEEE-EEEE-EEEE-111111111111",
     "title": "quiz 1",
     "description": "quiz description 1",
     "passingGrade": 10
     }
     }
     */
    public String quizId,title,description;
    public int passingGrade;
}
