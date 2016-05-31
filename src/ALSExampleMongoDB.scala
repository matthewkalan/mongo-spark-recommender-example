package com.mongodb.spark.examples

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

import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date

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
 
    // get current time at beginning and end to calculate job latency
    val formatter = new SimpleDateFormat("hh:mm:ss")
    val startTime = Calendar.getInstance().getTime()
    println("Start time: " + formatter.format(startTime))
    
    //this conf should only be used when run locally because sc.getOrCreate() is called below
    val conf = new SparkConf()      
      .setMaster("local")
      .setAppName("ALSExampleMongoDB")
    val sc = SparkContext.getOrCreate(conf)
    val sqlContext = SQLContext.getOrCreate(sc)
    var ratings = sqlContext.emptyDataFrame    //because compiler needs definition if we exit early
    var validInput = false
    
    //confirm 2 arguments of proper values
    if (args.length == 0) {
      println("# of args: " + args.length + "\nArgs expected: mongodb|file <mongodb connection string>|<file-location>")
    } else if (args(0).toLowerCase() == "mongodb") {
      var inputUri = args(1)                                       //pass MongoDB connection string from args
      println("inputUri = " + inputUri)
      ratings = sqlContext.read.option("uri", inputUri).mongo()
      ratings.printSchema()
      validInput = true
    } else if (args(0).toLowerCase() == "file" ) {
      import sqlContext.implicits._
      ratings = sc.textFile(args(1))
        .map(Rating.parseRating)
        .toDF()
      validInput = true
    }

    if (validInput) {
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
      
      val endTime = Calendar.getInstance().getTime()
      var elapsedTime = (endTime.getTime() - startTime.getTime()) / 1000
      println("End time:" + formatter.format(endTime) + ", Total time: " + elapsedTime + " seconds")
    } 
  }
}
