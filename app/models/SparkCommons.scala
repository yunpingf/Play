package models

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object SparkCommons {
  lazy val conf = {
    new SparkConf()
      .setMaster("local[*]")
      .setAppName("MOOC Analysis")
  }

  lazy val sc = SparkContext.getOrCreate(conf)
  lazy val sqlContext = new SQLContext(sc)
  val query_url = "jdbc:mysql://localhost:3306/cnmooc?useUnicode=true&characterEncoding=GBK&user=root&password=1234"
}
