package com.tao.answersys.biz;

import android.util.Log;

import com.tao.answersys.bean.AnswerItem;
import com.tao.answersys.bean.NbQuestionPublish;
import com.tao.answersys.bean.Question;
import com.tao.answersys.dao.DaoQuestion;
import com.tao.answersys.global.Config;
import com.tao.answersys.global.CustApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiangTao on 2017/4/13.
 */

public class BizQuestion {
    private DaoQuestion mDaoQuestion;

    public BizQuestion() {
        mDaoQuestion = new DaoQuestion();
    }

    public List<Question> getNews(int page) {
        List<Question> data = new ArrayList<Question>();

        data = mDaoQuestion.getLatest(page, CustApplication.getCurrUserId(), CustApplication.getUserType());

        if(data == null) {
            return data;
        }

        for(Question question : data) {
            String imgUrls[] = question.getImgUrls();
            for(int i = 0; i < (imgUrls == null ? 0: imgUrls.length); i++) {
                if(!imgUrls[i].startsWith("http://")) {
                    imgUrls[i] = Config.BASE_URL + imgUrls[i];
                }
            }
        }

        return data;
    }

    public Question getQuestion(int questionId) {
        String useType = CustApplication.getUserType();
        return  mDaoQuestion.getQuestion(questionId, CustApplication.getCurrUserId(), useType);
    }

    public List<Question> getUserAnswer(int page) {
        return mDaoQuestion.getUserAnswer(page, CustApplication.getCurrUserId(), CustApplication.getUserType());
    }

    public List<Question> getUserQuestion(int page) {
        return mDaoQuestion.getUserQuestion(page, CustApplication.getCurrUserId());
    }

    public List<Question> getUserCollect(int page) {
        return mDaoQuestion.getUserCollect(page, CustApplication.getCurrUserId(), CustApplication.getUserType());
    }
    public List<AnswerItem> getStuAnswer(int qid) {
        return mDaoQuestion.getStuAnswer(qid);
    }

    public List<AnswerItem> getTeacherAnswer(int qid) {
        return mDaoQuestion.getTeacherAnswer(qid);
    }

    public List<Question> searchQuestion(int page, String keyWord) {
        return mDaoQuestion.searchQuestion(page, keyWord);
    }
}
