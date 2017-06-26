package com.tao.answersys.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tao.answersys.R;

/**
 * Created by LiangTao on 2017/4/26.
 * <br>进度提示框
 */
public class ProgressDialog {
    private AlertDialog mDialog;
    private AlertDialog.Builder mBuilder;
    private Context mContext;
    private View mRootView;

    private TextView mTextviewMessage;
    private String mMessage;

    /**
     * 构造方法
     * @param context
     */
    public ProgressDialog(Context context) {
        this.mContext = context;
        initView();

        mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setView(mRootView);
        mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 初始化
     */
    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mRootView = layoutInflater.inflate(R.layout.layout_dialog_progress, null);

        mTextviewMessage = (TextView) mRootView.findViewById(R.id.dialog_progress_msg);
    }

    /**
     * 设置显示的内容
     * @param message
     * @return
     */
    public ProgressDialog setMessage(String message) {
        this.mMessage = message;
        mTextviewMessage.setText(message);

        return this;
    }

    /**
     * 显示弹框
     */
    public void show() {
        mDialog.show();
    }

    /**
     * 设置弹框消失监听器
     * @param l
     */
    public void addDissmissListener(DialogInterface.OnDismissListener l) {
        mDialog.setOnDismissListener(l);
    }

    /**
     * 使弹框消失
     */
    public void dismiss() {
        mDialog.dismiss();
    }

    /**
     * 弹框按钮点击事件接口
     */
    public interface OnDialogButtonClickListener {
        void onOkButtonClick();
        void onCancelButtonClick();
    }
}
