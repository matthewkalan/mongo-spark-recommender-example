#!/usr/bin/env bash

echo "Setting environment variables"

export SPARK_HOME="/Users/mkalan/dev/spark-1.6.1-bin-hadoop2.6"
export MAIN_JAR_HOME="/Users/mkalan/github/mongo-spark-recommender-example/target/reco-engine-example-1.0.0.jar"
export MAIN_CLASS_NAME="com.mongodb.spark.examples.ALSExampleMongoDB"
export NUM_THREADS="2"
export PACKAGES="org.mongodb.spark:mongo-spark-connector_2.10:1.0.0"
export MONGODB_INPUT_CONN_STRING=mongodb://localhost:27017/movielens.ratings_medium
export MONGODB_OUTPUT_CONN_STRING=mongodb://localhost:27017/movielens.predictions
export THRESHOLD=0

# if using file for data
export DATA_HOME="/github/mongo-spark-recommender-example/data/sample_movielens_ratings.txt"


