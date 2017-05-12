package com.tao.answersys.global;

import android.app.Application;
import android.content.Context;

import com.tao.answersys.bean.Student;
import com.tao.answersys.bean.Teacher;

/**
 * Created by LiangTao on 2017/4/17.
 */

public class CustApplication extends Application{
    private static Context mApplicationContext;
    private static boolean mIsStudent = false;
    private static Student currentStudent;
    private static Teacher currentTeacher;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mApplicationContext;
    }

    public static void setCurrentUser(boolean isStudent, Object user) {
        mIsStudent = isStudent;

        if(isStudent) {
            currentStudent = (Student) user;
        } else {
            currentTeacher = (Teacher) user;
        }
    }

    public static int getCurrUserId() {
        if(mIsStudent) {
            return ((Student)currentStudent).getStuId();
        } else {
            return ((Teacher)currentTeacher).getId();
        }
    }

    public static Object getCurrUser() {
        if(mIsStudent) {
            return currentStudent;
        } else {
            return currentTeacher;
        }
    }

    public static String getUserType() {
        if(mIsStudent) {
            return Config.USER_TYPE_STU;
        } else {
            return Config.USER_TYPE_TEACHER;
        }
    }

    public static void logout() {
        currentStudent = null;
        currentTeacher = null;
    }
}
