package models;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TestSubmit implements java.io.Serializable {
    private static final JavaSparkContext sc =
            new JavaSparkContext(new SparkConf().setAppName("SparkJdbc").setMaster("local[*]"));
    private static final SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String MYSQL_CONNECTION_URL = "jdbc:mysql://localhost:3306/cnmooc?useUnicode=true&characterEncoding=GBK";
    private static final String MYSQL_USERNAME = "root";
    private static final String MYSQL_PWD = "1234";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");

    public List<List<String>> getData(String course_name, String open, String name) {
        DataFrame df_submit = sqlContext.jdbc("jdbc:mysql://localhost:3306/cnmooc?useUnicode=true&characterEncoding=GBK&user=root&password=1234",
                "test_submit");
        df_submit.registerTempTable("test_submit");
        DataFrame df_basic = sqlContext.jdbc("jdbc:mysql://localhost:3306/cnmooc?useUnicode=true&characterEncoding=GBK&user=root&password=1234",
                "test_basic");
        df_basic.registerTempTable("test_basic");
        String sql = "select count(*), datediff(endTime, submitTime) from "
                + "(select submitTime, endTime from test_submit "
                + "left join test_basic on test_submit.test_id = test_basic.test_id "
                + "where test_basic.course_name='" + course_name + "' and test_basic.course_open_name='" +
                open + "' and test_name='" + name + "' ) t where datediff(endTime, submitTime)>=0 and datediff(endTime, submitTime)<1000  "
                + "group by datediff(endTime, submitTime)";
        DataFrame schoolDistribute = sqlContext.sql(sql);
        // Join first name and last name
        List<String> xList = schoolDistribute.javaRDD().map(new Function<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                ;
                return row.get(1).toString();
            }
        }).collect();
        List<String> yList = schoolDistribute.javaRDD().map(new Function<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                return row.get(0).toString();
            }
        }).collect();
        List<List<String>> result = new ArrayList<List<String>>();
//        for(int i=0;i<xList.size();i++) {
//        	if(Integer.parseInt(xList.get(i))<0)
//        		xList.remove(i);
//        	if(Integer.parseInt(xList.get(i))>1000)
//        		xList.remove(i);
//        	if(i==xList.size()-1)
//        		break;
//        	i--;
//        }
        result.add(xList);
        result.add(yList);
        return result;
    }


    public static void main(String[] args) {
        TestSubmit test = new TestSubmit();
        List<List<String>> res = test.getData("电路理论（上）", "2015春", "第五章 章节练习");
        for (int i = 0; i < res.get(0).size(); i++) {
            System.out.println(res.get(0).get(i) + " " + res.get(1).get(i));
        }
    }
}
