package com.qianliu.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HBase操作工具类：Java工具类建议采用单例模式封装
 */
public class HBaseUtils {


    HBaseAdmin admin = null;
    Configuration configuration = null;


    /**
     * 私有改造方法
     */
    private HBaseUtils(){
        configuration = new Configuration();
        configuration.set("hbase.zookeeper.quorum", "192.168.48.138:2181");
        configuration.set("hbase.rootdir", "hdfs://192.168.48.138:8020/hbase");

        try {
            admin = new HBaseAdmin(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HBaseUtils instance = null;

    public  static synchronized HBaseUtils getInstance() {
        if(null == instance) {
            instance = new HBaseUtils();
        }
        return instance;
    }


    /**
     * 根据表名获取到HTable实例
     */
    public HTable getTable(String tableName) {

        HTable table = null;

        try {
            table = new HTable(configuration, tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return table;
    }

    /**
     * 添加一条记录到HBase表
     * @param tableName HBase表名
     * @param rowkey  HBase表的rowkey
     * @param cf HBase表的columnfamily
     * @param column HBase表的列
     * @param value  写入HBase表的值
     */
    public void put(String tableName, String rowkey, String cf, String column, String value) {
        HTable table = getTable(tableName);

        Put put = new Put(Bytes.toBytes(rowkey));
        put.add(Bytes.toBytes(cf), Bytes.toBytes(column), Bytes.toBytes(value));

        try {
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * @author qianliu on 2019/3/27 20:50
     * @param tablename 表名
     * @param rowkey 列名，此处是课程的编号
     * @return 返回列名的查询出来的结果，返回的是课程的浏览记录
     * @Discription:
     */
    public Map<String,Long> query(String tablename,String rowkey) throws IOException {
        Map<String,Long> map = new HashMap<String,Long>();
        HTable table = getTable(tablename);
        String cf = "info";//列族
        String qualifier = "click_count";//列族中的列名

        Scan scan = new Scan();

        //获取前缀过滤器:只需要是该前缀的rowkey都会被查询出来
        Filter filter = new PrefixFilter(Bytes.toBytes(rowkey));
        scan.setFilter(filter);

        //table中加入scan
        ResultScanner rs = table.getScanner(scan);


        //获取结果转化为java中的string和long类型
        for (Result result : rs){
            String row = Bytes.toString(result.getRow());
            Long count = Bytes.toLong(result.getValue(cf.getBytes(),qualifier.getBytes()));
            map.put(row,count);
        }
        return map;
    }

    public static void main(String[] args) throws IOException {

        //HTable table = HBaseUtils.getInstance().getTable("imooc_course_clickcount");
        //System.out.println(table.getName().getNameAsString());

        /*
        String tableName = "imooc_course_clickcount" ;
        String rowkey = "20171111_88";
        String cf = "info" ;
        String column = "click_count";
        String value = "2";

        HBaseUtils.getInstance().put(tableName, rowkey, cf, column, value);
        */
        //根据前缀查询结果
        Map<String,Long> map = HBaseUtils.getInstance().query("imooc_course_clickcount","20190309");
        for (Map.Entry<String,Long> entry: map.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }

}
