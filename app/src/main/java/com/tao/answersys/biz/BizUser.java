package com.tao.answersys.biz;

import com.tao.answersys.bean.Lesson;
import com.tao.answersys.bean.Message;
import com.tao.answersys.bean.NbQuestionPublish;
import com.tao.answersys.bean.QuestionUpdate;
import com.tao.answersys.bean.Student;
import com.tao.answersys.bean.User;
import com.tao.answersys.dao.DaoUser;
import com.tao.answersys.global.Config;
import com.tao.answersys.global.CustApplication;
import com.tao.answersys.utils.SharePreferencesManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by LiangTao on 2017/4/25.
 */

public class BizUser {
    public final static int LOGIN_SUC = 1;
    private DaoUser mDaoUser;

    public BizUser() {
        mDaoUser = new DaoUser();
    }

    public Object login(String account, String pwd) {
        User user = mDaoUser.login(account, pwd);

        if(user == null) {
            return null;
        }

        if(user.getStudent() != null) {
            return user.getStudent();
        } else if(user.getTeacher() != null){
            return user.getTeacher();
        }

        return null;
    }

    public List<Lesson> getLessons() {
        Student student = null;
        try {
            student = (Student) CustApplication.getCurrUser();
        }catch (Exception e) {
            e.printStackTrace();
            return  null;
        }

        return mDaoUser.getStuLessons(student.getMajorId());
    }

    public boolean answer(String answer, int qid) {
        String useType = CustApplication.getUserType();
        return  mDaoUser.answer(answer, CustApplication.getCurrUserId(), qid, useType);
    }

    public boolean updateAnswer(String answer, int aid) {
        String useType = CustApplication.getUserType();
        return  mDaoUser.updateAnswer(answer, aid, useType);
    }

    public boolean updateQuestion(QuestionUpdate question) {
        if(CustApplication.getUserType().equals(Config.USER_TYPE_STU)) {
            question.setStuId(CustApplication.getCurrUserId());
            return mDaoUser.updateQuestion(question);
        } else {
            return false;
        }
    }


    public boolean cancelCollect(int qid) {
        return mDaoUser.cancelCollect(CustApplication.getCurrUserId(), qid, CustApplication.getUserType());
    }

    public boolean collect(int qid) {
        String useType = CustApplication.getUserType();
        return  mDaoUser.collect(CustApplication.getCurrUserId(), qid, useType);
    }

    public boolean publishQuestion(NbQuestionPublish question) {
        boolean reasult = mDaoUser.publish(question);
        return reasult;
    }

    public boolean feedback(String content, String email, String tel) {
        return mDaoUser.feedback(content, email, tel, CustApplication.getUserType());
    }

    public List<Message> getNewMessage() {
        SharePreferencesManager manager = SharePreferencesManager.getInstance();
        Calendar cal = Calendar.getInstance();

        long lastLoadDate = manager.getDate(Config.SHARED_PREF_KEY_MESSAGE_LOAD_DATE, cal.getTime()).getTime();
        manager.put(Config.SHARED_PREF_KEY_MESSAGE_LOAD_DATE, cal.getTime());

        return mDaoUser.getNewMessage(lastLoadDate);
    }

    public Boolean deleteAnswer(int aid) {
        return mDaoUser.deleteAnswer(aid, CustApplication.getUserType());
    }

    public boolean changePwd(String oldPwd, String newPwd) {
        return mDaoUser.changePwd(CustApplication.getCurrUserId(), CustApplication.getUserType(), oldPwd, newPwd);
    }
}
