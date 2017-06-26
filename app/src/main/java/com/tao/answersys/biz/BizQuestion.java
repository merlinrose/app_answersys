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
 * 问题服务类
 * <br>主要处理问题相关的业务逻辑
 */
public class BizQuestion {
    private DaoQuestion mDaoQuestion;

    public BizQuestion() {
        mDaoQuestion = new DaoQuestion();
    }

    /**
     * 获取问题动态
     * @param page
     * @return
     */
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
                    imgUrls[i] = Config.HOST+":"+Config.PORT + "/" + imgUrls[i];
                }
            }
        }

        return data;
    }

    /**
     * 根据Id获取问题
     * @param questionId
     * @return
     */
    public Question getQuestion(int questionId) {
        String useType = CustApplication.getUserType();
        return  mDaoQuestion.getQuestion(questionId, CustApplication.getCurrUserId(), useType);
    }

    /**
     * 获取用户的历史回答
     * @param page
     * @return
     */
    public List<Question> getUserAnswer(int page) {
        return mDaoQuestion.getUserAnswer(page, CustApplication.getCurrUserId(), CustApplication.getUserType());
    }

    /**
     * 获取用户的历史提问
     * @param page
     * @return
     */
    public List<Question> getUserQuestion(int page) {
        return mDaoQuestion.getUserQuestion(page, CustApplication.getCurrUserId());
    }

    /**
     * 获取用户的历史收藏
     * @param page
     * @return
     */
    public List<Question> getUserCollect(int page) {
        return mDaoQuestion.getUserCollect(page, CustApplication.getCurrUserId(), CustApplication.getUserType());
    }

    /**
     * 获取问题的学生回答
     * @param qid
     * @return
     */
    public List<AnswerItem> getStuAnswer(int qid) {
        return mDaoQuestion.getStuAnswer(qid);
    }

    /**
     * 获取问题的老师回答
     * @param qid
     * @return
     */
    public List<AnswerItem> getTeacherAnswer(int qid) {
        return mDaoQuestion.getTeacherAnswer(qid);
    }

    /**
     * 搜索问题
     * @param page
     * @param keyWord
     * @return
     */
    public List<Question> searchQuestion(int page, String keyWord) {
        return mDaoQuestion.searchQuestion(page, keyWord);
    }
}
