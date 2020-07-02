package com.example.sqlimporttool;

import com.example.sqlimporttool.createsql.sqltemplate.sqlparser.TemplateSQLParser;
import com.example.sqlimporttool.importdb.AppService;
import com.example.sqlimporttool.importdb.DataTask;
import com.example.sqlimporttool.importdb.SingleSqlInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootApplication
public class SqlimporttoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(SqlimporttoolApplication.class, args);
    }


    @Value("${jdbc.jdbctype}")
    private String jdbctype;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.password}")
    private String password;

    @Value("${operation.conf}")
    private String operation;

    @Autowired
    AppService appService;

    @Autowired
    private TemplateSQLParser templateSqlParser;

    @PostConstruct
    public void boot() {
        try {
            // classLoader,加载对应驱动
            Class.forName(getDriver(jdbctype));

            Connection conn1 = DriverManager.getConnection(url, username, password);
            Connection conn2 = DriverManager.getConnection(url, username, password);
            Connection conn3 = DriverManager.getConnection(url, username, password);

            Assert.notNull(conn1, "connection1 is null !!!");
            Assert.notNull(conn2, "connection2 is null !!!");
            Assert.notNull(conn3, "connection3 is null !!!");

            if ("delete".equals(operation.toLowerCase())) {
                deleteData();
            } else {
                appService.run(conn1, conn2, conn3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deleteData() {
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            Connection conn = DriverManager.getConnection(url, username, password);
            CountDownLatch countDown = new CountDownLatch(2);

            //用来存储中间过程数据
            LinkedBlockingQueue<List<SingleSqlInfo>> temData = new LinkedBlockingQueue<>(1500);
            System.out.println("删除数据开始执行-----");
            System.out.println(new Date());
            executorService.execute(new DataTask("deleteData", temData, countDown, null, templateSqlParser, 1));
            executorService.execute(new DataTask("importData", temData, countDown, conn, null, null));
            countDown.await();
            System.out.println("删除数据完成");
            System.out.println(new Date());
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDriver(String jdbcType) {
        String driver = null;
        switch (jdbcType.toLowerCase()) {
            case "mysql":
                driver = "com.mysql.cj.jdbc.Driver";
                break;
            case "oracle":
                driver = "oracle.jdbc.OracleDriver";
                break;
            case "postgresql":
                driver = "org.postgresql.Driver";
                break;
            default:
                break;
        }
        return driver;
    }

}
