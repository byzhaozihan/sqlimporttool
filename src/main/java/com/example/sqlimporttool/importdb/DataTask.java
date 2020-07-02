package com.example.sqlimporttool.importdb;

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

/**
 * 执行sql构造及IO的线程
 *
 * @author zzh
 */
public class DataTask implements Runnable {

    private final static Integer TIMEOUT_QUEUE_OFFER = 5;

    private final TemplateSQLParser templateSqlParser;

    private final String action;

    private final LinkedBlockingQueue<List<SingleSqlInfo>> dataQueue;

    private final CountDownLatch countDown;

    private final Connection connection;

    private final Integer cycles;


    public DataTask(String action, LinkedBlockingQueue<List<SingleSqlInfo>> temData,
                    CountDownLatch countDown, Connection connection,
                    TemplateSQLParser templateSqlParser, Integer cycles) {
        this.action = action;
        this.dataQueue = temData;
        this.countDown = countDown;
        this.connection = connection;
        this.templateSqlParser = templateSqlParser;
        this.cycles = cycles;
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
        try {
            String path = System.getProperty("user.dir");
            File sqlFile = new File(path, "/sql/hers_onbrdfinish_delete.sql");
            templateSqlParser.parseDeleteSQLData(sqlFile.getPath(), this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDown.countDown();
        }
    }

    private void batchSet() {
        try {
            String path = System.getProperty("user.dir");
            File sqlFile = new File(path, "/sql/hers_onbrdfinish_insert.sql");
            templateSqlParser.parseInsertSQLData(sqlFile.getPath(), cycles, this);
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
            if (!dataQueue.offer(unitData, TIMEOUT_QUEUE_OFFER, TimeUnit.MINUTES)) {
                System.out.println("队列已满且5分钟都无法插入队列，需要排查IO线程问题");
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
            //构建线程是否罢工
            boolean threadInterupt = false;
            int timeout = 0;
            while (!threadInterupt) {
                List<SingleSqlInfo> billData;
                List<List<SingleSqlInfo>> allDataList = new ArrayList<>();
                if (timeout > 2) {
                    threadInterupt = true;
                }
                if (!threadInterupt) {
                    //尝试去一条看看有没有数据
                    billData = dataQueue.poll(2, TimeUnit.SECONDS);
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            countDown.countDown();
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
