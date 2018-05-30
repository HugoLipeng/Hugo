package com.hugo.upload;

/**
 * Created by hugo on 2018/5/30.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.utils.AsyncRun;
import com.qiniu.android.utils.StringUtils;
import com.qiniu.util.Auth;
import com.hugo.upload.utils.FileUtils;
import com.hugo.upload.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 8090;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private MainActivity context;
    private TextView uploadLogTextView;
    private LinearLayout uploadStatusLayout;
    private ProgressBar uploadProgressBar;
    private TextView uploadSpeedTextView;
    private TextView uploadFileLengthTextView;
    private TextView uploadPercentageTextView;
    private UploadManager uploadManager;
    private long uploadLastTimePoint;
    private long uploadLastOffset;
    private long uploadFileLength;
    private String uploadFilePath;
    public String TAG ="hugo";

    EditText editText;
    //...生成上传凭证，然后准备上传
    String accessKey = "abc";
    String secretKey = "adc";
    String bucket = "lipengfortest";

    //默认不指定key的情况下，以文件内容的hash值作为文件名
    String key = null;
    Auth auth = Auth.create(accessKey, secretKey);

    public MainActivity() {
        this.context = this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        this.uploadProgressBar = (ProgressBar) this
                .findViewById(R.id.simple_upload_without_key_upload_progressbar);
        this.uploadProgressBar.setMax(100);
        this.uploadStatusLayout = (LinearLayout) this
                .findViewById(R.id.simple_upload_without_key_status_layout);
        this.uploadSpeedTextView = (TextView) this
                .findViewById(R.id.simple_upload_without_key_upload_speed_textview);
        this.uploadFileLengthTextView = (TextView) this
                .findViewById(R.id.simple_upload_without_key_upload_file_length_textview);
        this.uploadPercentageTextView = (TextView) this
                .findViewById(R.id.simple_upload_without_key_upload_percentage_textview);
        this.uploadStatusLayout.setVisibility(LinearLayout.INVISIBLE);
        this.uploadLogTextView = (TextView) this
                .findViewById(R.id.simple_upload_without_key_log_textview);

        editText = (EditText) findViewById(R.id.et_uptoken);
    }

    public void selectUploadFile(View view) {
        Intent target = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(target,
                this.getString(R.string.choose_file));
        try {
            if (isPermissionOK())
                this.startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException ex) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        try {
                            // Get the file path from the URI

                            final String path = Tools.getPath(this, uri);


                            this.uploadFilePath = path;

                            this.clearLog();
                            this.writeLog(context
                                    .getString(R.string.qiniu_select_upload_file)
                                    + path);
                        } catch (Exception e) {
                            Toast.makeText(
                                    context,
                                    context.getString(R.string.qiniu_get_upload_file_failed),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void syncUploadFile(View view) {
        if (this.uploadFilePath == null) {
            return;
        }

        String token = auth.uploadToken(bucket);
        //String token = editText.getText().toString();
        Log.d(TAG, "syncUploadFile: token =" + token);
        if (StringUtils.isNullOrEmpty(token)) {
            Toast.makeText(this, "Need Upload Token", Toast.LENGTH_LONG).show();
            return;
        }

        syncUpload(token);
    }


    public void uploadFile(View view) {
        if (this.uploadFilePath == null) {
            return;
        }
        //从业务服务器获取上传凭证
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final OkHttpClient httpClient = new OkHttpClient();
//                Request req = new Request.Builder().url(QiniuLabConfig.makeUrl(
//                        QiniuLabConfig.REMOTE_SERVICE_SERVER,
//                        QiniuLabConfig.SIMPLE_UPLOAD_WITHOUT_KEY_PATH)).method("GET", null).build();
//                Response resp = null;
//                try {
//                    resp = httpClient.newCall(req).execute();
//                    JSONObject jsonObject = new JSONObject(resp.body().string());
//                    String uploadToken = jsonObject.getString("uptoken");
//                    writeLog(context
//                            .getString(R.string.qiniu_get_upload_token)
//                            + uploadToken);
//                    upload(uploadToken);
//                } catch (IOException e) {
//                    AsyncRun.runInBack(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(
//                                    context,
//                                    context.getString(R.string.qiniu_get_upload_token_failed),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    writeLog(context
//                            .getString(R.string.qiniu_get_upload_token_failed)
//                            + resp.toString());
//                } catch (JSONException e) {
//                    writeLog(context.getString(R.string.qiniu_get_upload_token_failed));
//                    writeLog("StatusCode:" + resp.code());
//                    if (resp != null) {
//                        writeLog("Response:" + resp.toString());
//                    }
//                    writeLog("Exception:" + e.getMessage());
//                } finally {
//                    if (resp != null) {
//                        resp.body().close();
//                    }
//                }
//            }
//        }).start();


//        String token = "BVclqUXJMzuWBm1vEyLZ1Jw9QVuhfXJj7U-shplx:SPy65ZLEmQU6FSr973WesD8S-Ik=:eyJzY29wZSI6Im5hc3RvcmFnZSIsImRlYWRsaW5lIjoxNDkzOTcyMzQ1fQo=";
//        String token = "PCcVvxAGRykxES1ZtFKG3xcZxZll1VAemHIAirMd:8vFmOQzB6uDBdFEg_0FElbEF_DU=:eyJzY29wZSI6Im5vdGhpbmciLCJkZWFkbGluZSI6NDYzNTkxNTgyOX0=";
        String token = auth.uploadToken(bucket);
        //String token = editText.getText().toString();

        if (StringUtils.isNullOrEmpty(token)) {
            Toast.makeText(this, "Need Upload Token", Toast.LENGTH_LONG).show();
            return;
        }

        upload(token);
    }


    private boolean isPermissionOK() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        } else {
            return checkPermission();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermission() {
        boolean ret = true;

        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionsNeeded.add("Write external storage");
        }

        if (permissionsNeeded.size() > 0) {
            // Need Rationale
            String message = "You need to grant access to " + permissionsNeeded.get(0);
            for (int i = 1; i < permissionsNeeded.size(); i++) {
                message = message + ", " + permissionsNeeded.get(i);
            }
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permissionsList.get(0))) {
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
            } else {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            ret = false;
        }

        return ret;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        boolean ret = true;
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            ret = false;
        }
        return ret;
    }

 /*   private static Zone createZone(String upHost, String upHostBackup, String upIp, String upIp2) {
        String[] upIps = {upIp, upIp2};
        ServiceAddress up = new ServiceAddress("http://" + upHost, upIps);
        ServiceAddress upBackup = new ServiceAddress("http://" + upHostBackup, upIps);
        return new FixedZone(up, upBackup);
    }*/

    private void upload(final String uploadToken) {
        Configuration config = new Configuration.Builder()
//                .zone(Zone.zone0)
//                .zone(createZone("uphost", "bkhost", "", ""))
                .build();
        if (this.uploadManager == null) {
            this.uploadManager = new UploadManager(config);
//            this.uploadManager = new UploadManager();
        }
        final File uploadFile = new File(this.uploadFilePath);
        final UploadOptions uploadOptions = new UploadOptions(null, null, false,
                new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                        Log.e("SSSSS", "percent==" + percent);
                        updateStatus(percent);
                    }
                }, null);
        final long startTime = System.currentTimeMillis();
        final long fileLength = uploadFile.length();
        this.uploadFileLength = fileLength;
        this.uploadLastTimePoint = startTime;
        this.uploadLastOffset = 0;

        AsyncRun.runInMain(new Runnable() {
            @Override
            public void run() {
                // prepare status
                uploadPercentageTextView.setText("0 %");
                uploadSpeedTextView.setText("0 KB/s");
                uploadFileLengthTextView.setText(Tools.formatSize(fileLength));
                uploadStatusLayout.setVisibility(LinearLayout.VISIBLE);
            }
        });

        writeLog(context.getString(R.string.qiniu_upload_file) + "...");

//        OkHttpClient okHttpClient2 = new OkHttpClient();
//
//        try {
//            Response response = okHttpClient2.newCall(new Request.Builder().url("http://www.baidu.com").build()).execute();
//            Log.e("SSSSS=22", response.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        因为是无key上传，所以key参数指定为null
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.e("SSSSSS", "start");
//                Log.e("SSSSSS", "response==" +
//                        uploadManager.syncPut(uploadFile, null, uploadToken, uploadOptions).toString());
//    }
//        });
//        thread.start();

        this.uploadManager.put(uploadFile, "tmp" + System.currentTimeMillis(), uploadToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo respInfo,
                                         JSONObject jsonData) {
                        AsyncRun.runInMain(new Runnable() {
                            @Override
                            public void run() {
                                // reset status
                                uploadStatusLayout
                                        .setVisibility(LinearLayout.INVISIBLE);
                                uploadProgressBar.setProgress(0);
                            }
                        });

                        long lastMillis = System.currentTimeMillis()
                                - startTime;

                        if (respInfo.isOK()) {
                            try {
                                String fileKey = jsonData.getString("key");
                                String fileHash = jsonData.getString("hash");
                                writeLog("File Size: "
                                        + Tools.formatSize(uploadFileLength));
                                writeLog("File Key: " + fileKey);
                                writeLog("File Hash: " + fileHash);
                                writeLog("Last Time: "
                                        + Tools.formatMilliSeconds(lastMillis));
                                writeLog("Average Speed: "
                                        + Tools.formatSpeed(fileLength,
                                        lastMillis));
                                writeLog("X-Reqid: " + respInfo.reqId);
                                writeLog("X-Via: " + respInfo.xvia);
                                writeLog("--------------------------------");
                            } catch (JSONException e) {
                                AsyncRun.runInMain(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(
                                                context,
                                                context.getString(R.string.qiniu_upload_file_response_parse_error),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                                writeLog(context
                                        .getString(R.string.qiniu_upload_file_response_parse_error));
                                if (jsonData != null) {
                                    writeLog(jsonData.toString());
                                }
                                writeLog("--------------------------------");
                            }
                            Log.e("SSSSS", "resposne==" + respInfo.toString());
                        } else {
                            AsyncRun.runInMain(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(
                                            context,
                                            context.getString(R.string.qiniu_upload_file_failed),
                                            Toast.LENGTH_LONG).show();
                                }
                            });

                            writeLog(respInfo.toString());
                            if (jsonData != null) {
                                writeLog(jsonData.toString());
                            }
                            writeLog("--------------------------------");
                        }
                    }

                }, uploadOptions);
    }

    private void syncUpload(final String uploadToken) {
        Configuration config = new Configuration.Builder()
//                .zone(createZone("upload-na0.qiniu.com", "up-na0.qiniu.com", "23.236.102.3", "23.236.102.2"))
//                .zone(Zone.zone0)
                .build();
        if (this.uploadManager == null) {
            this.uploadManager = new UploadManager(config);
//            this.uploadManager = new UploadManager();
        }
        final File uploadFile = new File(this.uploadFilePath);
        final UploadOptions uploadOptions = new UploadOptions(null, null, false,
                new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                        Log.e("SSSSS", "percent==" + percent);
                        updateStatus(percent);
                    }
                }, null);
        final long startTime = System.currentTimeMillis();
        final long fileLength = uploadFile.length();
        this.uploadFileLength = fileLength;
        this.uploadLastTimePoint = startTime;
        this.uploadLastOffset = 0;

        AsyncRun.runInMain(new Runnable() {
            @Override
            public void run() {
                // prepare status
                uploadPercentageTextView.setText("0 %");
                uploadSpeedTextView.setText("0 KB/s");
                uploadFileLengthTextView.setText(Tools.formatSize(fileLength));
                uploadStatusLayout.setVisibility(LinearLayout.VISIBLE);
            }
        });

        writeLog(context.getString(R.string.qiniu_upload_file) + "...");

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                writeLog("---------------begin sync upload-----------------");
//                uploadManager.syncPut(uploadFile, "syncFile", uploadToken, uploadOptions);
//                writeLog("---------------end sync upload-----------------");
//
//            }
//        }).start();

    }

    private void updateStatus(final double percentage) {
        long now = System.currentTimeMillis();
        long deltaTime = now - uploadLastTimePoint;
        long currentOffset = (long) (percentage * uploadFileLength);
        long deltaSize = currentOffset - uploadLastOffset;
        if (deltaTime <= 100) {
            return;
        }

        final String speed = Tools.formatSpeed(deltaSize, deltaTime);
        // update
        uploadLastTimePoint = now;
        uploadLastOffset = currentOffset;

        AsyncRun.runInMain(new Runnable() {
            @Override
            public void run() {
                int progress = (int) (percentage * 100);
                uploadProgressBar.setProgress(progress);
                uploadPercentageTextView.setText(progress + " %");
                uploadSpeedTextView.setText(speed);
            }
        });
    }

    private void clearLog() {
        this.uploadLogTextView.setText("");
    }

    private void writeLog(final String msg) {
        AsyncRun.runInMain(new Runnable() {
            @Override
            public void run() {
                uploadLogTextView.append(msg);
                uploadLogTextView.append("\r\n");
            }
        });
    }

}