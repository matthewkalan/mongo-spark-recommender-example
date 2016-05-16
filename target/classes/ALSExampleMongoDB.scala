

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.{SparkContext, SparkConf}

import com.mongodb.spark._
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.DataFrame
import com.mongodb.spark.sql._

object ALSExampleMongoDB {

  case class Rating(userId: Int, movieId: Int, rating: Float, timestamp: Long)
  object Rating {
    def parseRating(str: String): Rating = {
      val fields = str.split("::")
      assert(fields.size == 4)
      Rating(fields(0).toInt, fields(1).toInt, fields(2).toFloat, fields(3).toLong)
    }
  }

  def main(args: Array[String]) {
   
    Logger.getLogger("org").setLevel(Level.WARN)
 
    val conf = new SparkConf()
      .setMaster(args(2))
      .setAppName("ALSExampleMongoDB")
      .set("spark.mongodb.input.uri", args(1))
    val sc = SparkContext.getOrCreate(conf)
    val sqlContext = SQLContext.getOrCreate(sc)
    var ratings = sqlContext.emptyDataFrame    //because compiler needs definition if we exit early
                
    //read from given data source
    val dsName = args(0).toLowerCase()
    if (dsName == "mongodb") {
      var inputUri = args(1)                                       //pass MongoDB connection string from args[]
      println("inputUri = " + inputUri)
      //ratings = sqlContext.read.option("uri", inputUri).mongo()
      ratings = sqlContext.read.mongo()
      ratings.printSchema()
    } else if (dsName == "file" ) {
      import sqlContext.implicits._
      ratings = sc.textFile(args(1))
      .map(Rating.parseRating)
      .toDF()
    } else {
      println("Args: mongodb|file <file-location>")
      sys.exit(1)
    }

    val Array(training, test) = ratings.randomSplit(Array(0.8, 0.2))

    // Build the recommendation model using ALS on the training data
    val als = new ALS()
      .setMaxIter(5)
      .setRegParam(0.01)
      .setUserCol("userId")
      .setItemCol("movieId")
      .setRatingCol("rating")
    val model = als.fit(training)

    // Evaluate the model by computing the RMSE on the test data
    val predictions = model.transform(test)
      .withColumn("rating", col("rating").cast(DoubleType))
      .withColumn("prediction", col("prediction").cast(DoubleType))

    val evaluator = new RegressionEvaluator()
      .setMetricName("rmse")
      .setLabelCol("rating")
      .setPredictionCol("prediction")
    val rmse = evaluator.evaluate(predictions)
    println(s"Root-mean-square error = $rmse")
    
    // sc.stop()    //removed because using shared SC
  }
}
