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
 */

public class ProgressDialog {
    private AlertDialog mDialog;
    private AlertDialog.Builder mBuilder;
    private Context mContext;
    private View mRootView;

    private TextView mTextviewMessage;
    private String mMessage;

    public ProgressDialog(Context context) {
        this.mContext = context;
        initView();

        mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setView(mRootView);
        mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mRootView = layoutInflater.inflate(R.layout.layout_dialog_progress, null);

        mTextviewMessage = (TextView) mRootView.findViewById(R.id.dialog_progress_msg);
    }

    public ProgressDialog setMessage(String message) {
        this.mMessage = message;
        mTextviewMessage.setText(message);

        return this;
    }

    public void show() {
        mDialog.show();
    }

    public void addDissmissListener(DialogInterface.OnDismissListener l) {
        mDialog.setOnDismissListener(l);
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public interface OnDialogButtonClickListener {
        void onOkButtonClick();
        void onCancelButtonClick();
    }
}
