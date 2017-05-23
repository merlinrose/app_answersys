package com.tao.answersys.dao;

import android.util.Log;

import com.google.gson.Gson;
import com.tao.answersys.bean.Lesson;
import com.tao.answersys.bean.Message;
import com.tao.answersys.bean.NbLessons;
import com.tao.answersys.bean.NbMessages;
import com.tao.answersys.bean.NbPublishResult;
import com.tao.answersys.bean.NbQuestionPublish;
import com.tao.answersys.bean.NbQuestions;
import com.tao.answersys.bean.NbUploadImageResult;
import com.tao.answersys.bean.NbUser;
import com.tao.answersys.bean.NetBean;
import com.tao.answersys.bean.Question;
import com.tao.answersys.bean.QuestionUpdate;
import com.tao.answersys.bean.Questions;
import com.tao.answersys.bean.Student;
import com.tao.answersys.bean.User;
import com.tao.answersys.event.ErrorEventAnswerPage;
import com.tao.answersys.event.ErrorEventFeedbackPage;
import com.tao.answersys.event.ErrorEventLessonPage;
import com.tao.answersys.event.ErrorEventLoginPage;
import com.tao.answersys.event.ErrorEventMainPage;
import com.tao.answersys.event.ErrorEventMessagePage;
import com.tao.answersys.event.ErrorEventPersonDataPage;
import com.tao.answersys.event.ErrorEventPublishPage;
import com.tao.answersys.event.ErrorEventQuestionDetailPage;
import com.tao.answersys.event.ErrorEventQuestionUpdatePage;
import com.tao.answersys.event.ErrorEventUserAnswerPage;
import com.tao.answersys.event.ErrorEventUserQuestionPage;
import com.tao.answersys.event.EventNewsUpdate;
import com.tao.answersys.event.EventPublishProgress;
import com.tao.answersys.global.Config;
import com.tao.answersys.global.CustApplication;
import com.tao.answersys.net.HttpClient;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by LiangTao on 2017/4/25.
 */

public class DaoUser {
    public User login(String account, String pwd) {
        User user = null;
        NbUser nbBuser = null;
        try {
            Response response = HttpClient.getInstance().get(Config.BASE_URL + "User_login?account=" + account + "&pwd=" + pwd);

            if (response.code() == 200) {
                String json = response.body().string();
                Log.e("json : ", json);
                nbBuser = new Gson().fromJson(json, NbUser.class);

                if (nbBuser.isSuc()) {
                    user = nbBuser.getResult();
                } else {
                    EventBus.getDefault().post(new ErrorEventLoginPage(nbBuser.getMsg()));
                }
            } else {
                EventBus.getDefault().post(new ErrorEventLoginPage("http code:" + response.code()));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventLoginPage(e.getMessage()));
            e.printStackTrace();
        }

        return user;
    }

    public List<Lesson> getStuLessons(int majorId) {
        List<Lesson> datas = null;
        NbLessons nbLessons = null;
        try {
            Response response = HttpClient.getInstance().get(Config.BASE_URL + "User_majorLesson?majorId=" + majorId);

            if (response.code() == 200) {
                String json = response.body().string();
                nbLessons = new Gson().fromJson(json, NbLessons.class);
                if (nbLessons.isSuc()) {
                    datas = nbLessons.getResult();
                } else {
                    EventBus.getDefault().post(new ErrorEventLessonPage("msg:" + nbLessons.getMsg()));
                }
            } else {
                EventBus.getDefault().post(new ErrorEventLessonPage("http code:" + response.code()));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventLessonPage(e.getMessage()));
            e.printStackTrace();
        }

        return nbLessons == null ? null : nbLessons.getResult();
    }

    public boolean publish(NbQuestionPublish question) {
        List<String> imgPath = null;
        /*final EventPublishProgress event = new EventPublishProgress("正在上传数据");*/
        //上传图片文件
        /*HttpClient.getInstance().setProgressCallBack(new HttpClient.IProgressCallBack() {
            @Override
            public void onProgressUpdate(float hasFinish, float allSize) {
                event.setProgress("正在上传图片：" + hasFinish + "/" + allSize);
                EventBus.getDefault().post(event);
            }
        });*/

        try {
            List<String> fileList = question.getAttchList();
            if(fileList != null || fileList.size() != 0) {
                List<File> files = new ArrayList<File>();
                for(String fileUrl : fileList) {
                    files.add(new File(fileUrl));
                }
                Map map = new HashMap<String, String>();
                map.put("id", question.getStuId()+"");
                Response response = HttpClient.getInstance().uploadFile(Config.BASE_URL + "User_upload", map, files);

                if(response.code() == 200) {
                    String json = response.body().string();
                    NbUploadImageResult uploadResult = new Gson().fromJson(json, NbUploadImageResult.class);

                    if(!uploadResult.isSuc()) {
                        EventBus.getDefault().post(new ErrorEventPublishPage(uploadResult.getMsg()));
                        return false;
                    } else {
                        imgPath = uploadResult.getResult();
                    }
                } else {
                    EventBus.getDefault().post(new ErrorEventPublishPage("服务器异常：http code : " + response.code()));
                    return false;
                }
            }
        } catch (IOException e){
            EventBus.getDefault().post(new ErrorEventPublishPage(e.getMessage()));
            return false;
        }

       /* event.setProgress("正在上传文本内容");
        EventBus.getDefault().post(event);*/

        //上传文本内容
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("stuId", question.getStuId()+"");
        params.put("title", question.getTitle());
        params.put("content", question.getContent());
        params.put("lessonId", question.getLessonId()+"");
        params.put("attachStrs", new Gson().toJson(imgPath));

        try {
            Response response = HttpClient.getInstance().post(Config.BASE_URL+"User_publish", params);

            if(response.code() == 200) {
                String json = response.body().string();
                NbPublishResult netBean = new Gson().fromJson(json, NbPublishResult.class);

                if(!netBean.isSuc()) {
                    EventBus.getDefault().post(new ErrorEventPublishPage(netBean.getMsg()));
                } else {
                    EventBus.getDefault().post(new EventNewsUpdate(netBean.getResult()));
                }

                return netBean.isSuc();
            } else {
                EventBus.getDefault().post(new ErrorEventPublishPage("服务器异常：http code : " + response.code()));
                return false;
            }
        }catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventPublishPage(e.getMessage()));
            e.printStackTrace();
        }

        return  false;
    }

    public boolean answer(String answer, int uid, int qid, String userType) {
        HttpClient httpClient = HttpClient.getInstance();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("uid", uid+"");
        params.put("qid", qid+"");
        params.put("userType", userType);
        params.put("answer", answer);

        try {
            Response response = httpClient.post(Config.BASE_URL+"User_answer", params);
            if(response.code() == 200) {
                String json = response.body().string();
                NetBean netBean = new Gson().fromJson(json, NetBean.class);

                if(!netBean.isSuc()) {
                    EventBus.getDefault().post(new ErrorEventAnswerPage(netBean.getMsg()));
                }

                return netBean.isSuc();
            } else {
                EventBus.getDefault().post(new ErrorEventAnswerPage("服务器异常：http code : " + response.code()));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            EventBus.getDefault().post(new ErrorEventAnswerPage(e.getMessage()));
        }

        return false;
    }

    public boolean updateAnswer(String answer, int aid, String userType) {
        HttpClient httpClient = HttpClient.getInstance();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("answerId", aid+"");
        params.put("userType", userType);
        params.put("answer", answer);

        try {
            Response response = httpClient.post(Config.BASE_URL+"User_updateAnswer", params);
            if(response.code() == 200) {
                String json = response.body().string();
                NetBean netBean = new Gson().fromJson(json, NetBean.class);

                if(!netBean.isSuc()) {
                    EventBus.getDefault().post(new ErrorEventAnswerPage(netBean.getMsg()));
                }

                return netBean.isSuc();
            } else {
                EventBus.getDefault().post(new ErrorEventAnswerPage("服务器异常：http code : " + response.code()));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            EventBus.getDefault().post(new ErrorEventAnswerPage(e.getMessage()));
        }

        return false;
    }

    public boolean updateQuestion(QuestionUpdate quesiotn) {
        HttpClient httpClient = HttpClient.getInstance();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("uid", quesiotn.getStuId()+"");
        params.put("qid", quesiotn.getId()+"");
        params.put("content", quesiotn.getContent());
        params.put("title", quesiotn.getTitle());

        try {
            Response response = httpClient.post(Config.BASE_URL+"User_updateQuestion", params);
            if(response.code() == 200) {
                String json = response.body().string();
                NetBean netBean = new Gson().fromJson(json, NetBean.class);

                if(!netBean.isSuc()) {
                    EventBus.getDefault().post(new ErrorEventQuestionUpdatePage(netBean.getMsg()));
                }

                return netBean.isSuc();
            } else {
                EventBus.getDefault().post(new ErrorEventQuestionUpdatePage("http code : " + response.code()));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            EventBus.getDefault().post(new ErrorEventQuestionUpdatePage(e.getMessage()));
        }

        return false;
    }

    public boolean collect(int uid, int qid, String userType) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("uid", uid+"");
        params.put("qid", qid+"");
        params.put("userType", userType);

        try {
            Response response = HttpClient.getInstance().post(Config.BASE_URL+"User_collect", params);

            if(response.code() == 200) {
                String json = response.body().string();
                NetBean netBean = new Gson().fromJson(json, NetBean.class);

                if(!netBean.isSuc()) {
                    EventBus.getDefault().post(new ErrorEventQuestionDetailPage(netBean.getMsg()));
                }

                return netBean.isSuc();
            } else {
                EventBus.getDefault().post(new ErrorEventQuestionDetailPage("服务器异常：http code : " + response.code()));
                return false;
            }
        }catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventQuestionDetailPage(e.getMessage()));
            e.printStackTrace();
        }

        return  false;
    }

    public boolean cancelCollect(int uid, int qid, String userType) {
        NetBean netBean = null;
        try {
            Response response = HttpClient.getInstance().get(Config.BASE_URL + "User_cancelUserCollect?qid=" + qid + "&uid=" + uid + "&userType=" + userType);

            if (response.code() == 200) {
                String json = response.body().string();
                netBean = new Gson().fromJson(json, NetBean.class);

                if (netBean.isSuc()) {
                    return true;
                } else {
                    EventBus.getDefault().post(new ErrorEventQuestionDetailPage(netBean.getMsg()));
                }
            } else {
                EventBus.getDefault().post(new ErrorEventQuestionDetailPage("http code:" + response.code()));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventQuestionDetailPage(e.getMessage()));
            e.printStackTrace();
        }

        return netBean == null ? false : netBean.isSuc();
    }

    public boolean feedback(String content, String email, String tel, String userType) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userType", userType);
        params.put("content", content);
        params.put("email", email);
        params.put("tel", tel);
        params.put("type", "BUG");

        try {
            Response response = HttpClient.getInstance().post(Config.BASE_URL+"User_feedback", params);

            if(response.code() == 200) {
                String json = response.body().string();
                NetBean netBean = new Gson().fromJson(json, NetBean.class);

                if(!netBean.isSuc()) {
                    EventBus.getDefault().post(new ErrorEventFeedbackPage(netBean.getMsg()));
                }

                return netBean.isSuc();
            } else {
                EventBus.getDefault().post(new ErrorEventFeedbackPage("服务器异常：http code : " + response.code()));
                return false;
            }
        }catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventFeedbackPage(e.getMessage()));
            e.printStackTrace();
        }

        return  false;
    }

    public List<Message> getNewMessage(long date) {
        List<Message> datas = null;
        NbMessages nbMessages = null;
        try {
            Response response = HttpClient.getInstance().get(Config.BASE_URL + "User_queryNewMsg?date=" + date);

            if (response.code() == 200) {
                String json = response.body().string();
                nbMessages = new Gson().fromJson(json, NbMessages.class);
                if (nbMessages.isSuc()) {
                    datas = nbMessages.getResult();
                } else {
                    EventBus.getDefault().post(new ErrorEventMessagePage("msg:" + nbMessages.getMsg()));
                }
            } else {
                EventBus.getDefault().post(new ErrorEventMessagePage("http code:" + response.code()));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventMessagePage(e.getMessage()));
            e.printStackTrace();
        }

        return nbMessages == null ? null : nbMessages.getResult();
    }

    public Boolean deleteAnswer(int aid, String userType) {
        NetBean netBean = null;
        try {
            Response response = HttpClient.getInstance().get(Config.BASE_URL + "User_deleteAnswer?answerId=" + aid + "&userType=" + userType);

            if (response.code() == 200) {
                String json = response.body().string();
                netBean = new Gson().fromJson(json, NetBean.class);

                if (netBean.isSuc()) {
                    return true;
                } else {
                    EventBus.getDefault().post(new ErrorEventQuestionDetailPage(netBean.getMsg()));
                }
            } else {
                EventBus.getDefault().post(new ErrorEventQuestionDetailPage("http code:" + response.code()));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventQuestionDetailPage(e.getMessage()));
            e.printStackTrace();
        }

        return netBean == null ? false : netBean.isSuc();
    }

    public boolean changePwd(int uid, String userType, String oldpwd, String newPwd) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("uid", uid+"");
        params.put("userType", userType);
        params.put("oldPassword", oldpwd);
        params.put("newPassword", newPwd);

        try {
            Response response = HttpClient.getInstance().post(Config.BASE_URL+"User_changePwd", params);

            if(response.code() == 200) {
                String json = response.body().string();
                NetBean netBean = new Gson().fromJson(json, NetBean.class);

                if(!netBean.isSuc()) {
                    EventBus.getDefault().post(new ErrorEventPersonDataPage(netBean.getMsg()));
                }

                return netBean.isSuc();
            } else {
                EventBus.getDefault().post(new ErrorEventPersonDataPage("服务器异常：http code : " + response.code()));
                return false;
            }
        }catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventPersonDataPage(e.getMessage()));
            e.printStackTrace();
        }

        return  false;
    }
//    public boolean updateQuestion(QuestionUpdate quesiotn) {
//        HttpClient httpClient = HttpClient.getInstance();
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("uid", quesiotn.getStuId()+"");
//        params.put("qid", quesiotn.getId()+"");
//        params.put("content", quesiotn.getContent());
//        params.put("title", quesiotn.getTitle());
//
//        try {
//            Response response = httpClient.post(Config.BASE_URL+"User_updateQuestion", params);
//            if(response.code() == 200) {
//                String json = response.body().string();
//                NetBean netBean = new Gson().fromJson(json, NetBean.class);
//
//                if(!netBean.isSuc()) {
//                    EventBus.getDefault().post(new ErrorEventQuestionUpdatePage(netBean.getMsg()));
//                }
//
//                return netBean.isSuc();
//            } else {
//                EventBus.getDefault().post(new ErrorEventQuestionUpdatePage("http code : " + response.code()));
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            EventBus.getDefault().post(new ErrorEventQuestionUpdatePage(e.getMessage()));
//        }
//
//        return false;
//    }

}
