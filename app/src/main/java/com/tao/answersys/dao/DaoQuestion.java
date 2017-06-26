package com.tao.answersys.dao;

import android.util.Log;

import com.google.gson.Gson;
import com.tao.answersys.bean.AnswerItem;
import com.tao.answersys.bean.NbAnswerItems;
import com.tao.answersys.bean.NbQuestion;
import com.tao.answersys.bean.NbQuestionPublish;
import com.tao.answersys.bean.NbQuestions;
import com.tao.answersys.bean.NetBean;
import com.tao.answersys.bean.Question;
import com.tao.answersys.bean.Questions;
import com.tao.answersys.event.ErrorEventAnswerPage;
import com.tao.answersys.event.ErrorEventMainPage;
import com.tao.answersys.event.ErrorEventPublishPage;
import com.tao.answersys.event.ErrorEventQuestionDetailPage;
import com.tao.answersys.event.ErrorEventQuestionPage;
import com.tao.answersys.event.ErrorEventSearchPage;
import com.tao.answersys.event.ErrorEventUserAnswerPage;
import com.tao.answersys.event.ErrorEventUserQuestionPage;
import com.tao.answersys.global.Config;
import com.tao.answersys.net.HttpClient;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Created by LiangTao on 2017/4/24.
 * 问题Dao类
 * <br>主要完成问题相关的数据访问
 */
public class DaoQuestion {
    /**
     * 分页获取最新问题动态
     * @param page  页号
     * @param uid   用户编号
     * @param userType 用户类型
     * @return
     */
    public List<Question> getLatest(int page, int uid, String userType) {
        Questions questions = null;
        NbQuestions nBquestions = null;
        try {
            Response response = HttpClient.getInstance().get(Config.BASE_URL+"Question_news?page=" + page + "&uid=" + uid + "&userType=" + userType);

            if(response.code() == 200) {
                String json = response.body().string();
                nBquestions = new Gson().fromJson(json, NbQuestions.class);
                if(nBquestions.isSuc()) {
                    questions = nBquestions.getResult();
                } else {
                    EventBus.getDefault().post(new ErrorEventMainPage("msg:" + nBquestions.getMsg()));
                }
            } else {
                EventBus.getDefault().post(new ErrorEventMainPage("http code:" + response.code()));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventMainPage(e.getMessage()));
            e.printStackTrace();
        }

        return questions == null ? null : questions.getQuestionList();
    }

    /**
     * 获取问题详情
     * @param questionId
     * @param uid
     * @param userType
     * @return
     */
    public Question getQuestion(int questionId, int uid, String userType) {
        Question question = null;
        NbQuestion nbQuestion = null;
        try {
            Response response = HttpClient.getInstance().get(Config.BASE_URL+"Question_question?qid=" + questionId + "&uid="+uid+"&userType=" + userType);

            if(response.code() == 200) {
                String json = response.body().string();
                nbQuestion = new Gson().fromJson(json, NbQuestion.class);
                if(nbQuestion.isSuc()) {
                    question = nbQuestion.getResult();
                } else {
                    EventBus.getDefault().post(new ErrorEventQuestionDetailPage("msg:" + nbQuestion.getMsg()));
                }
            } else {
                EventBus.getDefault().post(new ErrorEventQuestionDetailPage("http code:" + response.code()));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventQuestionDetailPage(e.getMessage()));
            e.printStackTrace();
        }

        return nbQuestion == null ? null : nbQuestion.getResult();
    }

    /**
     * 获取问题的学生回答
     * @param qid
     * @return
     */
    public List<AnswerItem> getStuAnswer(int qid) {
           List<AnswerItem> datas = null;
            NbAnswerItems nbAnswerItems = null;
            try {
                Response response = HttpClient.getInstance().get(Config.BASE_URL+"Question_stuAnswer?qid=" + qid);

                if(response.code() == 200) {
                    String json = response.body().string();
                    nbAnswerItems = new Gson().fromJson(json, NbAnswerItems.class);
                    if(nbAnswerItems.isSuc()) {
                        datas = nbAnswerItems.getResult();
                    } else {
                        EventBus.getDefault().post(new ErrorEventQuestionPage("msg:" + nbAnswerItems.getMsg()));
                    }
                } else {
                    EventBus.getDefault().post(new ErrorEventQuestionPage("http code:" + response.code()));
                }
            } catch (Exception e) {
                EventBus.getDefault().post(new ErrorEventQuestionPage(e.getMessage()));
                e.printStackTrace();
            }

            return nbAnswerItems == null ? null : nbAnswerItems.getResult();
    }

    /**
     * 获取问题的老师回答
     * @param qid
     * @return
     */
    public List<AnswerItem> getTeacherAnswer(int qid) {
        List<AnswerItem> datas = null;
        NbAnswerItems nbAnswerItems = null;
        try {
            Response response = HttpClient.getInstance().get(Config.BASE_URL+"Question_teacherAnswer?qid=" + qid);

            if(response.code() == 200) {
                String json = response.body().string();
                nbAnswerItems = new Gson().fromJson(json, NbAnswerItems.class);
                if(nbAnswerItems.isSuc()) {
                    datas = nbAnswerItems.getResult();
                } else {
                    EventBus.getDefault().post(new ErrorEventQuestionPage("msg:" + nbAnswerItems.getMsg()));
                }
            } else {
                EventBus.getDefault().post(new ErrorEventQuestionPage("http code:" + response.code()));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventQuestionPage(e.getMessage()));
            e.printStackTrace();
        }

        return nbAnswerItems == null ? null : nbAnswerItems.getResult();
    }

    /**
     * 获取用户的历史提问
     * @param page
     * @param userId
     * @return
     */
    public List<Question> getUserQuestion(int page, int userId) {
        Questions questions = null;
        NbQuestions nBquestions = null;
        try {
            Response response = HttpClient.getInstance().get(Config.BASE_URL+"Question_userQuestion?page=" + page+"&uid="+userId);

            if(response.code() == 200) {
                String json = response.body().string();
                nBquestions = new Gson().fromJson(json, NbQuestions.class);
                if(nBquestions.isSuc()) {
                    questions = nBquestions.getResult();
                } else {
                    EventBus.getDefault().post(new ErrorEventUserQuestionPage("msg:" + nBquestions.getMsg()));
                }
            } else {
                EventBus.getDefault().post(new ErrorEventUserQuestionPage("http code:" + response.code()));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventUserQuestionPage(e.getMessage()));
            e.printStackTrace();
        }

        return questions == null ? null : questions.getQuestionList();
    }

    /**
     * 获取用户的历史回答
     * @param page
     * @param userId
     * @param userType
     * @return
     */
    public List<Question> getUserAnswer(int page, int userId, String userType) {
        Questions questions = null;
        NbQuestions nBquestions = null;
        try {
            Response response = HttpClient.getInstance().get(Config.BASE_URL+"Question_userAnswer?page=" + page+"&uid="+userId+"&userType=" + userType);

            if(response.code() == 200) {
                String json = response.body().string();
                nBquestions = new Gson().fromJson(json, NbQuestions.class);
                if(nBquestions.isSuc()) {
                    questions = nBquestions.getResult();
                } else {
                    EventBus.getDefault().post(new ErrorEventUserAnswerPage("msg:" + nBquestions.getMsg()));
                }
            } else {
                EventBus.getDefault().post(new ErrorEventUserAnswerPage("http code:" + response.code()));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventUserAnswerPage(e.getMessage()));
            e.printStackTrace();
        }

        return questions == null ? null : questions.getQuestionList();
    }

    /**
     * 获取用户的历史收藏
     * @param page
     * @param userId
     * @param userType
     * @return
     */
    public List<Question> getUserCollect(int page, int userId, String userType) {
        Questions questions = null;
        NbQuestions nBquestions = null;
        try {
            Response response = HttpClient.getInstance().get(Config.BASE_URL+"Question_userCollect?page=" + page+"&uid="+userId+"&userType=" + userType);

            if(response.code() == 200) {
                String json = response.body().string();
                nBquestions = new Gson().fromJson(json, NbQuestions.class);
                if(nBquestions.isSuc()) {
                    questions = nBquestions.getResult();
                } else {
                    EventBus.getDefault().post(new ErrorEventMainPage("msg:" + nBquestions.getMsg()));
                }
            } else {
                EventBus.getDefault().post(new ErrorEventMainPage("http code:" + response.code()));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventMainPage(e.getMessage()));
            e.printStackTrace();
        }

        return questions == null ? null : questions.getQuestionList();
    }

    /**
     * 搜索问题
     * @param page 页号
     * @param keyWord 关键词
     * @return
     */
    public List<Question> searchQuestion(int page, String keyWord) {
        Questions questions = null;
        NbQuestions nBquestions = null;
        try {
            Response response = HttpClient.getInstance().get(Config.BASE_URL+"Question_search?page=" + page + "&keyWord=" + keyWord);

            if(response.code() == 200) {
                String json = response.body().string();
                nBquestions = new Gson().fromJson(json, NbQuestions.class);
                if(nBquestions.isSuc()) {
                    questions = nBquestions.getResult();
                } else {
                    EventBus.getDefault().post(new ErrorEventSearchPage("msg:" + nBquestions.getMsg()));
                }
            } else {
                EventBus.getDefault().post(new ErrorEventSearchPage("http code:" + response.code()));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventSearchPage(e.getMessage()));
            e.printStackTrace();
        }

        return questions == null ? null : questions.getQuestionList();
    }
}
