package models;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.ArrayList;
import java.util.List;

public class UserAnalysis implements java.io.Serializable {
    private static final JavaSparkContext sc =
            new JavaSparkContext(new SparkConf().setAppName("SparkJdbc").setMaster("local[*]"));
    private static final SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);

    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String MYSQL_CONNECTION_URL = "jdbc:mysql://localhost:3306/cnmooc?useUnicode=true&characterEncoding=GBK";
    private static final String MYSQL_USERNAME = "root";
    private static final String MYSQL_PWD = "1234";

    public List<List<String>> getData() {
        DataFrame df = sqlContext.jdbc("jdbc:mysql://localhost:3306/cnmooc?useUnicode=true&characterEncoding=GBK&user=root&password=1234",
                "user_basic");
        df.registerTempTable("user");
        DataFrame schoolDistribute = sqlContext.sql("SELECT count(*), province from user group by province");
        // Join first name and last name
        List<String> xList = schoolDistribute.javaRDD().map(new Function<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                return row.getString(1);
            }
        }).collect();
        List<String> yList = schoolDistribute.javaRDD().map(new Function<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                return row.get(0).toString();
            }
        }).collect();
        List<List<String>> result = new ArrayList<List<String>>();
        result.add(xList);
        result.add(yList);
        return result;
    }


    public static void main(String[] args) {
        UserAnalysis test = new UserAnalysis();
        List<List<String>> res = test.getData();
        for (int i = 0; i < res.size(); i++) {
            for (int j = 0; j < res.get(i).size(); j++) {
                System.out.println(res.get(i).get(j));
            }
        }
    }
}
