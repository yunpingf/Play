package models

import java.util.Properties

import models.SparkCommons._

case class UserCountByProvince(count: Long, province: String)


//@Inject()(db: Database)
object UserCountByProvinceDao {
  def query() = {
    val df = sqlContext.read.jdbc(query_url, "user_basic", new Properties())
    val res = df.groupBy(df("province")).count().rdd.
      map(f => new UserCountByProvince(f.getAs("count"), f.getAs("province"))).collect().toList
    res
  }
}
