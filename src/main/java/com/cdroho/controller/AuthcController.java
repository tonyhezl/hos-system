package com.cdroho.controller;

import com.cdroho.modle.SysRole;
import com.cdroho.modle.User;
import com.cdroho.modle.dto.Tab;
import com.cdroho.modle.dto.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.Element;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试需要权限访问的地址
 * @author HZL
 * @date 2020-4-7
 */
@RestController
@RequestMapping("authc")
public class AuthcController {
    /**
     * 用户登陆后获取用户数据
     * @return
     */
    @GetMapping("info")
    public UserInfo info() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        UserInfo userInfo = new UserInfo();
        userInfo.setName(user.getUsername());
        String[] roles=new String[user.getRoles().size()];
        Iterator<SysRole> it = user.getRoles().iterator();
        int i=0;
        while (it.hasNext()){
            roles[i]=it.next().getRole();
            i++;
        }
        userInfo.setRoles(roles);
        return userInfo;
    }

    /**
     * 根据mapper逆向生成数据库结构sql
     * @return
     */
    @GetMapping("createsql")
    public Object createsql () {
        try {
            generateSql("G://mediaproject//MediaSystem//src//com//shine//mapper", "D://media.sql");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @GetMapping("admin")
    public Object admin() {
        return "Welcome Admin";
    }

    // delete
    @GetMapping("removable")
    public Object removable() {
        return "removable";
    }

    // insert & update
    @GetMapping("renewable")
    public Object renewable() {
        return "renewable";
    }

    private static void generateSql(String dirPath,String sqlFile) throws IOException {
        FileWriter fw = null;
        try {
            File dir = new File(dirPath);
            File sql = new File(sqlFile);
            if (sql.exists()){
                sql.delete();
            }
            sql.createNewFile();
            fw = new FileWriter(sql);

            if (dir.exists() && dir.isDirectory()){
                File[] files = dir.listFiles();
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".xml")){
                        System.out.println(file.getName());
                        fw.append("\r\n");
                        fw.append(getSql(file));
                        fw.flush();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if (fw != null){
                fw.close();
            }
        }
    }
    private static String getSql(File xmlfile) throws DocumentException {
        SAXReader saxReader= new SAXReader();
        Document document = saxReader.read(xmlfile);
        org.dom4j.Element root = document.getRootElement();
        Element resultMap = root.element("resultMap");
        Tab tab = new Tab();
        tab.setTableName(getTableName(root));
        tab.setColumns(getColumns(resultMap));
        return tab.toString();
    }
    private static Map<String,String> getColumns(Element resultMap){
        List<Element> elements = resultMap.elements();
        Map<String,String> map = new LinkedHashMap<String, String>();
        for (Element element : elements) {
            map.put(element.attribute("column").getValue(), element.attribute("jdbcType").getValue());
        }
        return map;
    }
    private static String getTableName(Element root){
        Element selectByPrimaryKey = root.element("select");
        String selectStr = selectByPrimaryKey.getTextTrim();
        String tableName = selectStr.split("from")[1].trim().split(" ")[0].trim();
        return tableName;
    }
}
