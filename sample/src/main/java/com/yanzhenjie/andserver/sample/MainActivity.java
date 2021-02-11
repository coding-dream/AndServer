/*
 * Copyright © 2016 Zhenjie Yan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.andserver.sample;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yanzhenjie.andserver.sample.model.EventBusMessageId;
import com.yanzhenjie.andserver.sample.model.EventWrapper;
import com.yanzhenjie.andserver.sample.util.CopyHelper;
import com.yanzhenjie.andserver.sample.util.CustomFileProvider;
import com.yanzhenjie.andserver.sample.website.PathManager;
import com.yanzhenjie.loading.dialog.LoadingDialog;

import org.apache.commons.io.FileUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ServerManager mServerManager;

    private Button mBtnStart;
    private Button mBtnStop;
    private Button mBtnBrowser;
    private TextView mTvMessage;
    private TextView mTvPath;
    private EditText mEtSendMessage;
    private Button mBtnWebFolder;
    private Button mBtnSendMessage;
    private View mBtnMove;

    private LoadingDialog mDialog;
    private String mRootUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_main);

        mBtnStart = findViewById(R.id.btn_start);
        mBtnStop = findViewById(R.id.btn_stop);
        mBtnWebFolder = findViewById(R.id.btn_web);
        mBtnBrowser = findViewById(R.id.btn_browse);
        mBtnMove = findViewById(R.id.btn_move);
        mTvMessage = findViewById(R.id.tv_message);
        mTvPath = findViewById(R.id.tv_down_path);
        mEtSendMessage = findViewById(R.id.et_send_text_to_other);
        mBtnSendMessage = findViewById(R.id.btn_send_message);

        mBtnStart.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mBtnBrowser.setOnClickListener(this);
        mBtnWebFolder.setOnClickListener(this);
        mBtnSendMessage.setOnClickListener(this);
        mBtnMove.setOnClickListener(this);

        // AndServer run in the service.
        mServerManager = new ServerManager(this);
        mServerManager.register();

        // startServer;
        mBtnStart.performClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mServerManager.unRegister();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_start: {
                showDialog();
                mServerManager.startServer();
                break;
            }
            case R.id.btn_stop: {
                showDialog();
                mServerManager.stopServer();
                break;
            }
            case R.id.btn_browse: {
                if (!TextUtils.isEmpty(mRootUrl)) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse(mRootUrl));
                    startActivity(intent);
                }
                break;
            }
            case R.id.btn_web: {
                File folder = new File(PathManager.getInstance().getWebDir());
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(Uri.fromFile(folder), "*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 521);
                break;
            }
            case R.id.btn_move: {
                showFileChooser();
                break;
            }
            case R.id.btn_send_message: {
                String sendMessage = mEtSendMessage.getText().toString();
                GlobalValueMananger.getInstance().tempMessage = sendMessage;
                Toast.makeText(this, "发送成功，请主动访问浏览器", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    /**
     * Start notify.
     */
    public void onServerStart(String ip) {
        closeDialog();
        mBtnStart.setVisibility(View.GONE);
        mBtnStop.setVisibility(View.VISIBLE);
        mBtnBrowser.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(ip)) {
            List<String> addressList = new LinkedList<>();
            mRootUrl = "http://" + ip + ":8080/";
            addressList.add(mRootUrl);
            addressList.add("http://" + ip + ":8080/login.html");
            mTvMessage.setText(TextUtils.join("\n", addressList));
        } else {
            mRootUrl = null;
            mTvMessage.setText(R.string.server_ip_error);
        }
    }

    /**
     * Error notify.
     */
    public void onServerError(String message) {
        closeDialog();
        mRootUrl = null;
        mBtnStart.setVisibility(View.VISIBLE);
        mBtnStop.setVisibility(View.GONE);
        mBtnBrowser.setVisibility(View.GONE);
        mTvMessage.setText(message);
    }

    /**
     * Stop notify.
     */
    public void onServerStop() {
        closeDialog();
        mRootUrl = null;
        mBtnStart.setVisibility(View.VISIBLE);
        mBtnStop.setVisibility(View.GONE);
        mBtnBrowser.setVisibility(View.GONE);
        mTvMessage.setText(R.string.server_stop_succeed);
    }

    private void showDialog() {
        if (mDialog == null) mDialog = new LoadingDialog(this);
        if (!mDialog.isShowing()) mDialog.show();
    }

    private void closeDialog() {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusMainThread(EventWrapper eventWrap) {
        if (eventWrap == null) {
            return;
        }
        int code = eventWrap.getEventCode();
        switch (code) {
            case EventBusMessageId.MSG_LIVEHALL_GET_MESSAGE:
                String message = (String) eventWrap.getData();
                CopyHelper.copy(this, message);
                break;
            case EventBusMessageId.MSG_LIVEHALL_UPLOAD_FILE:
                Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();

                String url = (String) eventWrap.getData();
                File file = new File(url);
                File parentFlie = new File(file.getParent());
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(Uri.fromFile(parentFlie), "*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    copyToServerFolder(data);
                }
                break;
            case 521:
                // 尝试安装apk
                if (resultCode == RESULT_OK) {
                    String finalPath = parseDataFromResult(data);
                    File file = new File(finalPath);
                    if (file.getAbsolutePath().endsWith(".apk")) {
                         installApk(file);
                    } else if (file.getAbsolutePath().endsWith(".png")
                            || file.getAbsolutePath().endsWith(".jpg")
                            || file.getAbsolutePath().endsWith(".mp4")) {
                         openPhoto(file);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void copyToServerFolder(Intent data) {
        try {
            String finalPath = parseDataFromResult(data);
            File folder = new File(PathManager.getInstance().getWebDir());
            FileUtils.copyFileToDirectory(new File(finalPath), folder);
            if (mTvPath != null) {
                mTvPath.setText(String.format("文件: %s 已拷贝到: %s 目录中", finalPath, folder.getAbsolutePath()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String parseDataFromResult(Intent data) {
        // Get the path
        String finalPath = "";
        Uri uri = data.getData();
        if (uri == null) {
            return finalPath;
        }
        if (ContentResolver.SCHEME_CONTENT.equals(data.getScheme())) {
            finalPath = getRealPath(uri);
        } else {
            CustomFileProvider.PathStrategy pathStrategy = CustomFileProvider.getPathStrategy(this, getPackageName() + ".fileprovider");
            finalPath = pathStrategy.getFileForUri(uri).getAbsolutePath();
        }
        if (TextUtils.isEmpty(finalPath)) {
            Toast.makeText(this, "抱歉，解析的路径为空.", Toast.LENGTH_SHORT).show();
        }
        return finalPath;
    }

    private String getRealPath(Uri uri) {
        String realPath = "";
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            Cursor cursor = this.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        realPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return realPath;
    }

    private void openPhoto(File file) {
        try {
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = CustomFileProvider.getUriForFile(this, this.getPackageName() + ".fileprovider", file);
            } else {
                uri = Uri.fromFile(file);
            }

            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "image/*");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装apk
     */
    private void installApk(File newApkFile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String type = "application/vnd.android.package-archive";
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = CustomFileProvider.getUriForFile(this, this.getPackageName() + ".fileprovider", newApkFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                uri = Uri.fromFile(newApkFile);
            }
            intent.setDataAndType(uri, type);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}