package com.tao.answersys.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.adapter.MediaRecyclerViewAdapter;
import com.tao.answersys.bean.Lesson;
import com.tao.answersys.bean.MediaViewBean;
import com.tao.answersys.bean.NbQuestionPublish;
import com.tao.answersys.event.ErrorEventPublishPage;
import com.tao.answersys.event.EventPublishProgress;
import com.tao.answersys.global.CustApplication;
import com.tao.answersys.utils.FileUtil;
import com.tao.answersys.view.MessageDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiangTao on 2017/4/18.
 */

public class ActivityPublish extends ActivityBase {
    private final static String TITLE_PUBLISH = "创建问题";
    private final static String OPERATION_TEXT = "提交";

    private EditText mEdittextTitle;
    private EditText mEdittextLesson;
    private EditText mEdittextContent;

    private Lesson mSelectLesson = null;

    private MediaRecyclerViewAdapter adapter;
    private String currentFilePath = "";
    private List<String> filesHasCreate;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INTENT_REQ_LESSONS) {
            if(resultCode == RESULT_CANCELED || resultCode == INTENT_RESULT_CANCEL) {

            } else if(resultCode == INTENT_RESULT_SUC) {
                mSelectLesson = (Lesson) data.getExtras().get("lesson");
                if(mSelectLesson != null) {
                    mEdittextLesson.setText(mSelectLesson.getName());
                }
            }
        } if(requestCode == INTENT_REQ_PHOTO && resultCode != RESULT_OK) {
            File file = new File(currentFilePath);
            if(file.exists()) {
                file.delete();
            }
        } else if(requestCode == INTENT_REQ_PHOTO && resultCode == RESULT_OK) {
            AsyncTask<Void, Void, Bitmap> thumbnailTask = new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... arg0) {
                    filesHasCreate.add(currentFilePath);
                    Bitmap mBitmap = FileUtil.getLowQualityBitmap(currentFilePath);
                    FileUtil.saveBitmap(currentFilePath, mBitmap);
                    Bitmap imgThumbanil = FileUtil.getImageThumbnail(currentFilePath);
                    return imgThumbanil;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                protected void onPostExecute(Bitmap result) {
                    adapter.notifyItemInserted(adapter.addData(new MediaViewBean(adapter.getItemCount(), result, currentFilePath)));
                };
            } ;

            thumbnailTask.execute();
        }
    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_publish);
        EventBus.getDefault().register(this);
        initView();

        setOnTopBarListener(new TopBarListener() {
            @Override
            public void onButtonBackClick() {
                finish();
            }

            @Override
            public String onSetTitle() {
                return TITLE_PUBLISH;
            }

            @Override
            public void onButtonOperationClick() {
                String lesson = mEdittextLesson.getText().toString();
                String title = mEdittextTitle.getText().toString();
                String content = mEdittextContent.getText().toString();

                if(lesson == null ||
                        lesson.equals("") ||
                        title == null ||
                        title.equals("") ||
                        content == null ||
                        content.equals("")) {
                    showPromptMessage("所有输入项不能为空！！");
                } else {
                    new AsyncTaskPublish().execute(title, content);
                }
            }

            @Override
            public String onSetOperationText() {
                return OPERATION_TEXT;
            }
        });

        filesHasCreate = new ArrayList<String>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        mEdittextLesson = (EditText) findViewById(R.id.pulish_edittext_lesson);
        mEdittextContent = (EditText) findViewById(R.id.pulish_edittext_content);
        mEdittextTitle = (EditText) findViewById(R.id.pulish_edittext_title);

        mEdittextLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPublish.this, ActivityLessonSelect.class);
                startActivityForResult(intent, INTENT_REQ_LESSONS);
            }
        });

        RecyclerView attchList = (RecyclerView) findViewById(R.id.publish_attch_list);
        GridLayoutManager gridlayoutManager = new GridLayoutManager(this, 4);
        gridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        attchList.setLayoutManager(gridlayoutManager);
        attchList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = 3;
                outRect.bottom = 3;
                outRect.left = 3;
                outRect.right = 3;
            }
        });

        adapter = new MediaRecyclerViewAdapter(ActivityPublish.this);
        List<MediaViewBean> dataList = new ArrayList<MediaViewBean>();

        adapter.setMediaViewClickListener(new MediaRecyclerViewAdapter.MediaViewClickListener() {
            @Override
            public void onClick(MediaViewBean bean) {
                if(bean == null) {//若点击对象为添加媒体信息按钮
                    startCamera();
                } else {//若为已添加的媒体信息，执行播放或者显示操作
                    Intent openIntent = new Intent(Intent.ACTION_VIEW);
                    openIntent.setDataAndType(Uri.parse("file://" + bean.getPath()), "image/*");
                    startActivity(openIntent);
                }
            }

            //执行删除操作的回调
            @Override
            public void onDelte(MediaViewBean mediaViewBean) {
                int position = mediaViewBean.getPosition();
                adapter.removeData(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged();

                File file = new File(mediaViewBean.getPath());
                if(file.exists()) {
                    file.delete();
                }
            }
        });

        //设置动画效果
        DefaultItemAnimator ia = new DefaultItemAnimator();
        ia.setRemoveDuration(500);
        attchList.setItemAnimator(ia);

        //设置数据适配器
        adapter.setData(dataList);
        attchList.setAdapter(adapter);

    }

    /**
     * 开始拍照操作
     */
    private void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = FileUtil.createImageFile(this);

                currentFilePath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, INTENT_REQ_PHOTO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class AsyncTaskPublish extends AsyncTask<String, Long, Boolean> {
        private boolean codeError = false;
        @Override
        protected Boolean doInBackground(String... params) {
            if(params.length == 2) {
                NbQuestionPublish question = new NbQuestionPublish();
                question.setTitle(params[0]);
                question.setLessonId(mSelectLesson.getId());
                question.setContent(params[1]);
                question.setStuId(CustApplication.getCurrUserId());

                List<MediaViewBean> beanList = adapter.getData();
                List<String> paths = new ArrayList<String>(beanList.size());
                for(int i = 0; i < beanList.size(); i++) {
                    paths.add(beanList.get(i).getPath());
                }
                question.setAttchList(paths);
                return mBizUser.publishQuestion(question);
            }else {
                codeError = true;
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("正在上传数据");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(codeError) {
                showPromptMessage("程序员开小差了！！");
            } else {
                if(result) {
                    final MessageDialog dialog =  new MessageDialog(ActivityPublish.this).setTitle("提示").setMessage("你的问题成功发布");
                    dialog.setOutsideCancelable(false);
                    dialog.hideCancelButton();
                    dialog.setButtonListener(new MessageDialog.OnDialogButtonClickListener() {
                        @Override
                        public void onOkButtonClick() {
                            dialog.dismiss();
                            ActivityPublish.this.finish();
                        }

                        @Override
                        public void onCancelButtonClick() {
                        }
                    });
                    dialog.show();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPublishError(ErrorEventPublishPage event) {
        showPromptMessage(event.getMsg());
        dismissProgressDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPublishProgressUpdate(EventPublishProgress event) {
        updateProgressMessage(event.getProgress()+"");
    }
}
