package com.tao.answersys.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tao.answersys.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by LiangTao on 2017/5/14.
 */

public class OptionDialog {
    private AlertDialog mDialog;
    private AlertDialog.Builder mBuilder;
    private Context mContext;
    private View mRootView;
    private OnDialogButtonClickListener mButtonListener;
    private OnOptionClickListener mOptionListener;

    private ListView mOptionList;
    private String[] mOptions;

    private TextView mTextviewTitle;
    private View mButtonCancel;

    private String mTitle;
    private SimpleAdapter mAdapter;

    public OptionDialog(Context context, String[] options) {
        this.mContext = context;
        this.mOptions = options;
        initView();

        mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setView(mRootView);
        mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mRootView = layoutInflater.inflate(R.layout.layout_dialog_option, null);

        mButtonCancel = mRootView.findViewById(R.id.dialog_option_btn_cancel);

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

        mTextviewTitle = (TextView) mRootView.findViewById(R.id.dialog_option_title);

        mOptionList = (ListView) mRootView.findViewById(R.id.dialog_option_list);
        if(mOptions != null) {
            List<Map<String, String>> lm = new ArrayList<Map<String, String>>();
            for(String s : mOptions) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("option", s);
                lm.add(map);
            }

            mAdapter = new SimpleAdapter(mContext, lm, R.layout.layout_option_item, new String[]{"option"}, new int[]{R.id.dialog_option_item});
        }
        mOptionList.setAdapter(mAdapter);
        mOptionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mOptionListener != null) {
                    mOptionListener.onOptionButtonClick(position);
                }
            }
        });
    }

    public OptionDialog setTitle(String title) {
        mTitle = title;
        mTextviewTitle.setText(title);

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

    public void setOptionListener(OnOptionClickListener l) {
        this.mOptionListener = l;
    }

    public void addDissmissListener(DialogInterface.OnDismissListener l) {
        mDialog.setOnDismissListener(l);
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public interface OnDialogButtonClickListener {
        void onCancelButtonClick();
    }

    public interface  OnOptionClickListener {
        void onOptionButtonClick(int optionId);
    }
}
