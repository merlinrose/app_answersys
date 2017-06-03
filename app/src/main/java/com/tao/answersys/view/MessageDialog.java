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

public class MessageDialog {
    private AlertDialog mDialog;
    private AlertDialog.Builder mBuilder;
    private Context mContext;
    private View mRootView;
    private OnDialogButtonClickListener mButtonListener;

    private TextView mTextviewTitle;
    private TextView mTextvireContent;
    private View mButtonOk;
    private View mButtonCancel;

    private String mTitle;
    private String mMessage;

    public MessageDialog(Context context) {
        this.mContext = context;
        initView();

        mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setView(mRootView);
        mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mRootView = layoutInflater.inflate(R.layout.layout_dialog_msg, null);

        mButtonCancel = mRootView.findViewById(R.id.dialog_msg_btn_cancel);
        mButtonOk = mRootView.findViewById((R.id.dialog_msg_btn_ok));
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mButtonListener != null) {
                    mButtonListener.onOkButtonClick();
                } else {
                    dismiss();
                }
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mButtonListener != null) {
                    mButtonListener.onCancelButtonClick();
                } else {
                    dismiss();
                }
            }
        });

        mTextvireContent = (TextView) mRootView.findViewById(R.id.dialog_msg_content);
        mTextviewTitle = (TextView) mRootView.findViewById(R.id.dialog_msg_title);
    }

    public MessageDialog setTitle(String title) {
        mTitle = title;
        mTextviewTitle.setText(title);

        return this;
    }

    public MessageDialog setMessage(String message) {
        this.mMessage = message;
        mTextvireContent.setText(message);

        return this;
    }

    public void setTitleBackground(int color) {
        this.mTextviewTitle.setBackgroundColor(color);
    }

    public void setTitleColor(int color) {
        this.mTextviewTitle.setTextColor(color);
    }

    public void show() {
        mDialog.show();
    }

    public void hideCancelButton() {
        this.mButtonCancel.setVisibility(View.INVISIBLE);
    }

    public void setOutsideCancelable(boolean able) {
        mDialog.setCanceledOnTouchOutside(able);
    }

    public void setButtonListener(OnDialogButtonClickListener listener) {
        this.mButtonListener = listener;
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
