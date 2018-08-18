package com.bugly.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task


public class PluginImpl implements Plugin<Project> {
    private Project project = null;

    // URL for uploading apk file
    private static final String APK_UPLOAD_URL = "https://api.bugly.qq.com/beta/apiv1/exp?app_key=";

    @Override
    void apply(Project project) {
        this.project = project
        // 接收外部参数
        project.extensions.create("beta", PluginExtension)

        // 取得外部参数
        project.task('testTask') << {
            println project.beta.appId + ":" + project.beta.appKey
        }

        if (project.android.hasProperty("applicationVariants")) { // For android application.
            project.android.applicationVariants.all { variant ->
                // Create task.
                createUploadTask(variant)
            }
        }
    }

    /**
     * 创建上传任务
     *
     * @param variant 编译参数
     * @return
     */
    private Task createUploadTask(Object variant) {
        String variantName = variant.name.capitalize()
        def betaTask = project.task("upload${variantName}BetaApkFile") << {
            // Check for execution
            if (false == project.beta.execute) {
                return
            }
            UploadInfo uploadInfo = new UploadInfo()
            uploadInfo.appId = project.beta.appId
            uploadInfo.appKey = project.beta.appKey
            uploadInfo.title = project.beta.title
            uploadInfo.description = project.beta.desc
            uploadInfo.sourceFile = project.beta.sourceFile
            uploadApk(uploadInfo)
        }
        return betaTask
    }

    /**
     *  上传apk
     * @param uploadInfo
     * @return
     */
    public boolean uploadApk(UploadInfo uploadInfo) {
        // 拼接url如：https://api.bugly.qq.com/beta/apiv1/exp?app_key=bQvYLRrBNiqUctfi
        String url = APK_UPLOAD_URL + uploadInfo.appKey
        project.logger.info(url)

        if (uploadInfo.sourceFile == null) {
            project.logger.error("Please set the sourceFile，eg: D:/BetaUpload.apk")
            return false
        }

        project.logger.info(uploadInfo.sourceFile)
        project.logger.info('Uploading the file: ' + uploadInfo.sourceFile)

        if (!post(url, uploadInfo.sourceFile, uploadInfo)) {
            project.logger.error("Failed to upload!")
            return false
        } else {
            project.logger.info("Successfully uploaded!")
            return true
        }
    }

    /**
     * 上传apk
     * @param url 地址
     * @param filePath 文件路径
     * @param uploadInfo 更新信息
     * @return
     */
    public boolean post(String url, String filePath, UploadInfo uploadInfo) {
        //要上传的文件名,如：d:\haha.doc.你要实现自己的业务。我这里就是一个空list.
        try {
            String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // 上传文件
            File file = new File(filePath);
            // 用于拼接表单
            StringBuilder sb = new StringBuilder();
            // 头
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n")

            // 内容处理 name为内容的字段
            sb.append("Content-Disposition: form-data; name=\"app_id\"");
            sb.append("\r\n\r\n");
            sb.append(uploadInfo.appId);
            sb.append("\r\n")

            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n")

            sb.append("Content-Disposition: form-data; name=\"pid\"");
            sb.append("\r\n\r\n");
            sb.append(uploadInfo.pid);
            sb.append("\r\n")

            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n")

            sb.append("Content-Disposition: form-data; name=\"title\"");
            sb.append("\r\n\r\n");
            sb.append(urlEncodeString(uploadInfo.title));
            sb.append("\r\n")

            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n")

            sb.append("Content-Disposition: form-data; name=\"description\"");
            sb.append("\r\n\r\n");
            sb.append(urlEncodeString(uploadInfo.description));
            sb.append("\r\n")

            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n")

            sb.append("Content-Disposition: form-data;name=\"file \";filename=\"" + file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            out.write(sb.toString().getBytes())

            DataInputStream inputStream = new DataInputStream(new FileInputStream(file))
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = inputStream.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            inputStream.close()
            byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
            out.write(end_data);
            out.flush();
            out.close();
            String result = readBufferFromStream(conn.getInputStream());
            if (result == null){
                return false;
            }
            return true;
            println("upload result:" + result)
        } catch (Exception e) {
            println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
    }

    /**
     * read buffer from inputstream
     * @param input
     * @return
     */
    public String readBufferFromStream(InputStream input) {
        if (input == null)
            return null;
        String charSet = "UTF-8";
        BufferedInputStream buffer = new BufferedInputStream(input);
        ByteArrayOutputStream baos = null;
        String str = null;
        try {
            baos = new ByteArrayOutputStream();

            byte[] byteChunk = new byte[1024 * 16];
            int len = -1;
            while ((len = buffer.read(byteChunk)) > -1) {
                baos.write(byteChunk, 0, len);
            }
            baos.flush();
            byte[] bytes = baos.toByteArray();
            str = new String(bytes, charSet);
            if (baos != null) {
                baos.close();
                baos = null;
            }
        } catch (IOException e) {
            println("readBufferFromStream error", e);
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                    baos = null;
                }
            } catch (Exception e) {
                println("readBufferFromStream error!", e);
            }
        }
        return str;
    }

    /**
     * Encode URL string.
     *
     * @param str URL string
     * @return encoded URL string
     */
    private String urlEncodeString(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8")
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace()
            return null
        }
    }

    private static class UploadInfo {
        // App ID of Bugly platform.
        public String appId = null
        // App Key of Bugly platform.
        public String appKey = null
        // platform id
        public String pid = "1"
        // Name of apk file to upload.
        public String sourceFile = null
        // app version title
        public String title = null
        // app version description [option]
        public String description = null
        // app secret level
        public int secret = 0
        // if open range was qq group set users to qq group num separate by ';' eg: 13244;23456;43843
        // if open range was qq num set users to qq num separate by ';' eg: 1000136; 10000148;1888432
        public String users = null
        // if open range was password you must set password
        public String password = null
        // download limit [option] default 10000
        public int download_limit = 10000
    }


}