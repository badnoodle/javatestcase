package com.hiahiahia.cases.defense.sqli;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class SqliCase4 {
    @RequestMapping("/sqli/case4")
    public void safeSql(@RequestParam(value="id") String id) throws Exception {
        if(!isSafeStr(id)){
            throw new Exception("Group ID invalid.");
        }
        String sql = String.format("select * from user where id='%s'", id);
        Connection con;
        try {
            con = DriverManager.getConnection("", "sunny","");
            Statement stmt = con.createStatement();
            stmt.executeQuery(sql);   // safe
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Boolean isSafeStr(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        /*
         *  防护方法：通过正则校验用户输入是否合法
         */
        // ͬ同时校验 group id 是否合法，避免 SQL 注入
        Pattern group_pattern = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher matcher = group_pattern.matcher(str);
        return matcher.find();
    }

    @RequestMapping("/sqli/case4_unsafe")
    public void unsafeSql(@RequestParam(value="id") String id) {
        String sql = String.format("select * from user where id='%s'", id);
        Connection con;
        try {
            con = DriverManager.getConnection("", "sunny","");
            Statement stmt = con.createStatement();
            stmt.executeQuery(sql);   // unsafe
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
