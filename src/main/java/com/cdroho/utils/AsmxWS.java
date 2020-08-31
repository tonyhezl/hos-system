package com.cdroho.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.dom4j.DocumentException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 语音发声
 * @author HZL
 * @date 2020-6-23
 */
public class AsmxWS {

    private static Logger log = LoggerFactory.getLogger(AsmxWS.class);

    public static String voice(String msg,String ip) throws DocumentException {
        BufferedReader br = null;
        InputStream is=null;
        // 返回结果字符串
        String result = null;
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost("http://192.168.100.32:18080/soap/Ietyy_data");
        String json = "<?xml version=\"1.0\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"><SOAP-ENV:Body xmlns:NS1=\"urn:etyy_dataIntf-Ietyy_data\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><NS1:getCmd><s xsi:type=\"xsd:string\">{\"cmd\":1001,\"data\":{\"host\":\"192.168.100.32\",\"params\":{\"context\":\"" +
                chinaToUnicode(msg) +
                "\",\"num\":1,\"APKVIDEO\":{\"isPercent\":1,\"ip\":\"127.0.0.1\",\"s\":0.4,\"v\":0.9,\"toip\":\"" +
                ip+
                "\"}}}}</s></NS1:getCmd></SOAP-ENV:Body></SOAP-ENV:Envelope>";
        try {
            StringEntity postingString = new StringEntity(json);
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



    public static Map<String, Object> createMapByXml(String xml) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (doc == null) {
            return map;
        }
        Element rootElement = doc.getRootElement();
        elementTomap(rootElement, map);
        return map;
    }



    public static Map<String, Object> elementTomap(Element outele,Map<String, Object> outmap) {
        List<Element> list = outele.elements();
        int size = list.size();
        if (size == 0) {
            outmap.put(outele.getName(), outele.getTextTrim());
        } else {
            Map<String, Object> innermap = new HashMap<String, Object>();
            for (Element ele1 : list) {
                String eleName = ele1.getName();
                Object obj = innermap.get(eleName);
                if (obj == null) {
                    elementTomap(ele1, innermap);
                } else {
                    if (obj instanceof java.util.Map) {
                        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                        list1.add((Map<String, Object>) innermap.remove(eleName));
                        elementTomap(ele1, innermap);
                        list1.add((Map<String, Object>) innermap.remove(eleName));
                        innermap.put(eleName, list1);
                    } else {
                        elementTomap(ele1, innermap);
                        ((List<Map<String, Object>>) obj).add(innermap);
                    }
                }
            }
            outmap.put(outele.getName(), innermap);
        }
        return outmap;
    }


    public static String chinaToUnicode(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            int chr1 = (char) str.charAt(i);
            // 汉字范围 \u4e00-\u9fa5 (中文)
            if (chr1 >= 19968 && chr1 <= 171941) {
                result += "\\u" + Integer.toHexString(chr1);
            } else {
                result += str.charAt(i);
            }
        }
        return result;
    }
}
