package com.cdroho.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 诊前提醒
 * @author HZL
 */
public class AsmwebService {
	private static Logger log = LoggerFactory.getLogger(AsmwebService.class);

	public static String deliver(String msg) throws DocumentException {
		BufferedReader br = null;
		InputStream is=null;
		// 返回结果字符串
		String result = null;
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost
		HttpPost httppost = new HttpPost("http://192.168.100.12:8090/index.php?g=CTFS&m=CTFS&a=beforeRemind");
		try {
			StringEntity postingString = new StringEntity(msg);
			httppost.setEntity(postingString);
			System.out.println("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {

					is = entity.getContent();
					br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					// 存放数据
					StringBuffer sbf = new StringBuffer();
					String temp = null;
					while ((temp = br.readLine()) != null) {
						sbf.append(temp);
						sbf.append("\r\n");
						// String filePath = "D:\\sbf.txt";
						//HttpTool.WriteStringToFile5(filePath,sbf.toString());
					}
					result = sbf.toString();
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			log.info("调用失败！");
		} catch (UnsupportedEncodingException e1) {
			log.info("调用失败！");
		} catch (IOException e) {
			log.info("调用失败！");
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 关闭资源
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return result;
	}
}
