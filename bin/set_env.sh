#!/usr/bin/env bash

echo "Setting environment variables"

export SPARK_HOME="/Users/mkalan/dev/spark-1.6.1-bin-hadoop2.6"
export MAIN_JAR_HOME="/Users/mkalan/github/mongo-spark-recommender-example/target/reco-engine-example-0.0.2.jar"
export MAIN_CLASS_NAME="ALSExampleMongoDB"
export NUM_THREADS="2"
export PACKAGES="org.mongodb.spark:mongo-spark-connector_2.10:0.1"
export MONGODB_INPUT_CONN_STRING=mongodb://mkalan-dev-0.mkalan-dev.57052e45e4b0c1b45d557c13.mongodbdns.com/movielens.ratings
export MONGODB_OUTPUT_CONN_STRING=
export SPARK_URL=spark://10.214.237.198:7077

# if using file for data
export DATA_HOME="/Users/mkalan/github/mongo-spark-recommender-example/data/sample_movielens_ratings.txt"


