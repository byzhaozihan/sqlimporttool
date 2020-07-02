package com.example.sqlimporttool.importDb;

import com.example.sqlimporttool.createsql.sqltemplate.sqlparser.TemplateSQLParser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DataTask implements Runnable {

    private TemplateSQLParser templateSQLParser;

    private String action;

    private LinkedBlockingQueue<List<SingleSqlInfo>> dataQueue;

    private CountDownLatch countDown;

    private Connection connection;

    private Integer cycles;


    public DataTask(String action, LinkedBlockingQueue<List<SingleSqlInfo>> temData,
                    CountDownLatch countDown, Connection connection,
                    TemplateSQLParser templateSQLParser, Integer cycles) {
        this.action = action;
        this.dataQueue = temData;
        this.countDown = countDown;
        this.connection = connection;
        this.templateSQLParser = templateSQLParser;
        this.cycles = cycles;
    }

    public DataTask() {
    }

    @Override
    public void run() {
        switch (action) {
            case "creatData":
                batchSet();
                break;
            case "importData":
                importData(connection);
                break;
            case "deleteData":
                deleteData();
            default:
                break;
        }
    }

    private void deleteData() {
        String path = System.getProperty("user.dir");
        File sqlFile = new File(path, "hers_onbrdfinish_delete.sql");
        try {
            templateSQLParser.parseDeleteSQLData(sqlFile.getPath(), this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDown.countDown();
        }
    }

    private void batchSet() {
        String path = System.getProperty("user.dir");
        File sqlFile = new File(path, "hers_onbrdfinish_insert.sql");
        try {
            templateSQLParser.parseInsertSQLData(sqlFile.getPath(), cycles, this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDown.countDown();
        }

    }

    /**
     * 构建数据
     */
    public void createData(List<SingleSqlInfo> unitData) {
        try {
            if (!dataQueue.offer(unitData, 30, TimeUnit.MINUTES)) {
                System.out.println("队列已满且30分钟都无法插入队列，需要排查IO线程问题");
                countDown.countDown();
            }
        } catch (InterruptedException e) {
            countDown.countDown();
            e.printStackTrace();
        }
    }

    /**
     * DB的操作
     */
    private void importData(Connection connection) {
        System.out.println("开始导入数据，当前时间为：");
        System.out.println(new Date());

        try {
            boolean threadInterupt = false;//构建线程是否罢工
            int timeout = 0;
            while (!threadInterupt) {
                List<SingleSqlInfo> billData;
                List<List<SingleSqlInfo>> allDataList = new ArrayList<>();
                if (timeout > 2) {
                    threadInterupt = true;
                }
                if (!threadInterupt) {
                    billData = dataQueue.poll(2, TimeUnit.SECONDS);//尝试去一条看看有没有数据
                    if (billData == null) {
                        timeout++;
                        continue;
                    } else {
                        //一次性最多拿500条，不然会导致单一线程处理太多，其他线程没有用到
                        int size = dataQueue.size();
                        dataQueue.drainTo(allDataList, Math.min(size, 500));
                        allDataList.add(billData);
                    }
                    timeout = 0;
                    //开始进行io操作
                    executeDataImport(allDataList, connection);
                }
            }
            //要么构建线程出问题，要么已经没有解析的数据
            System.out.println("导入数据完成，当前时间为：");
            System.out.println(new Date());
            countDown.countDown();
        } catch (InterruptedException e) {
            countDown.countDown();
            e.printStackTrace();
        }
    }

    private void executeDataImport(List<List<SingleSqlInfo>> allDataList, Connection conn) {
        PreparedStatement preparedStatement;
        try {
            conn.setAutoCommit(false);
            for (List<SingleSqlInfo> oneTempData : allDataList) {
                try {
                    for (SingleSqlInfo sqlInfo : oneTempData) {
                        String insertSql = sqlInfo.getSql();
                        Object[] param = sqlInfo.getParam();
                        preparedStatement = conn.prepareStatement(insertSql);
                        for (int i = 0; i < param.length; i++) {
                            preparedStatement.setObject(i + 1, param[i]);
                        }
                        preparedStatement.execute();
                    }
                    conn.commit();
                } catch (SQLException throwable) {
                    conn.rollback();
                    System.out.println(throwable.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
