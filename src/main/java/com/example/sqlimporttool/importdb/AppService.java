package com.example.sqlimporttool.importdb;


import com.example.sqlimporttool.createsql.sqltemplate.sqlparser.TemplateSQLParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class AppService {


    @Autowired
    private TemplateSQLParser templateSqlParser;

    @Value("${template.cycles}")
    private Integer cycles;

    public void run(Connection... connection) {

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch countDown = new CountDownLatch(4);

        //用来存储中间过程数据
        LinkedBlockingQueue<List<SingleSqlInfo>> temData = new LinkedBlockingQueue<>(3000);

        //链接用一个程序结束再关闭
        try {
            System.out.println("start ---" + System.nanoTime());
            executorService.execute(new DataTask("creatData", temData, countDown, null, templateSqlParser, cycles));
            executorService.execute(new DataTask("importData", temData, countDown, connection[0], null, null));
            executorService.execute(new DataTask("importData", temData, countDown, connection[1], null, null));
            executorService.execute(new DataTask("importData", temData, countDown, connection[2], null, null));
            countDown.await();
            System.out.println("数据导入完成");
            System.out.println("end ---" + System.nanoTime());
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
