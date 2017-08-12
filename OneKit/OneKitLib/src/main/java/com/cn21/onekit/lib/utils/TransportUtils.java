package com.cn21.onekit.lib.utils;

import android.text.TextUtils;

import com.cn21.onekit.core.utils.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2017/7/26.
 */
public class TransportUtils {

    public static String doDownloadFile(String url, String filePath) {
        HttpURLConnection conn = null;
        InputStream stream = null;
        OutputStream os = null;
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setUseCaches(false);
            conn.addRequestProperty("Accept-Charset", "UTF-8");
            conn.connect();

            int responseCode = conn.getResponseCode();
            LogUtil.d("doDownloadFile resp code : %d", responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                os = new BufferedOutputStream(new FileOutputStream(filePath));
                stream = new BufferedInputStream(conn.getInputStream());
                byte[] buf = new byte[4096];
                int r = -1;
                while ((r = stream.read(buf)) != -1) {
                    os.write(buf, 0, r);
                }
                os.flush();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            FileUtil.close(stream);
            FileUtil.close(os);
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static String uploadFile(String urlAddress, File file, Map<String, String> param) {
        final String CHARSET = "UTF-8";
        final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        final String PREFIX = "--", LINE_END = "\r\n";
        final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        String response = "";// 服务器端返回值
        HttpURLConnection connection = null;
        OutputStream out = null;
        InputStream ist = null;

        try {
            URL url = new URL(urlAddress);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            // connection.setInstanceFollowRedirects(false);// 不自动重定向
            connection.setRequestProperty("Content-Type", CONTENT_TYPE +
                                                          ";boundary=" + BOUNDARY);// 设置请求头
            connection.setRequestProperty("connection", "keep-alive");// 保持连接
            connection.setRequestProperty("Charset", CHARSET);
            connection.setConnectTimeout(15000);// 请求超时时间
            connection.connect();

            // 获取输出流
            out = new BufferedOutputStream(new DataOutputStream(connection.getOutputStream()));
            /***
             * 以下先是用于上传参数
             */
            if (param != null &&
                param.size() > 0) {
                StringBuffer buffer = new StringBuffer();
                Iterator<String> it = param.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = param.get(key);
                    buffer.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    buffer.append("Content-Disposition: form-data; name=\"")
                        .append(key).append("\"").append(LINE_END)
                        .append(LINE_END);
                    buffer.append(value).append(LINE_END);
                    String params = buffer.toString();
                    LogUtil.d("%s=%s##", key, params);
                    out.write(params.getBytes());
                    out.flush();
                }
            }

            // 参数上传完开始上传文件
            if (file != null) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(PREFIX);
                buffer.append(BOUNDARY);
                buffer.append(LINE_END);

//                buffer.append("Content-Disposition: form-data; name=\"")
//                    .append(fileKey)
//                    .append("\"; filename=\"").append(file.getName()).append("\"")
//                    .append(LINE_END);
                buffer.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                        .append(file.getName()).append("\"")
                        .append(LINE_END);

                buffer.append("Content-Type: application/octet-stream; charset=")
                    .append(CHARSET)
                    .append(LINE_END);
                buffer.append(LINE_END);

                out.write(buffer.toString().getBytes());
                ist = new BufferedInputStream(new FileInputStream(file));

                byte[] bytes = new byte[4096];
                int len = 0;
                while ((len = ist.read(bytes)) != -1) {
                    out.write(bytes, 0, len);
                }
                out.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX +
                                   BOUNDARY + PREFIX + LINE_END)
                    .getBytes();
                out.write(end_data);
                out.flush();
            }
            final int responseCode = connection.getResponseCode();
            LogUtil.d("upload file resp code %d, info : %s", responseCode, getResponseMsg(connection.getInputStream()));
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            FileUtil.close(ist);
            FileUtil.close(out);
        }
        if (!TextUtils.isEmpty(response)) {
            LogUtil.d("server response: %s", response);
        }
        return null;
    }

    private static String getResponseMsg(InputStream inputStream) {
        StringBuffer buffer = new StringBuffer(128);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                buffer.append(readLine).append("\n");
            }
            br.close();
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtil.close(inputStream);
            FileUtil.close(br);
        }
        return "";
    }
}
