package com.qianliu.dao;

import com.qianliu.domain.CourseClickCount;
import com.qianliu.utils.HBaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
* 课程点击访问的DAO
* */
public class CourseClickDAO {

    public static List<CourseClickCount> query(String day) throws Exception{
        List<CourseClickCount> list = new ArrayList<>();

        //取hbase中获取day中的所有课程以及课程访问量
        Map<String,Long> map = HBaseUtils.getInstance().query("imooc_course_clickcount","20190309");
        for (Map.Entry<String,Long> entry: map.entrySet()){
            CourseClickCount courseClickCount = new CourseClickCount();
            courseClickCount.setName(entry.getKey());
            courseClickCount.setValue(entry.getValue());
            list.add(courseClickCount);
        }

        return list;
    }

    public static void main(String[] args) throws Exception {
        CourseClickDAO dao = new CourseClickDAO();
        List<CourseClickCount> list = dao.query("20171022");
        for(CourseClickCount model : list) {
            System.out.println(model.getName() + " : " + model.getValue());
        }
    }
}
