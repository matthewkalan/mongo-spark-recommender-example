#!/usr/bin/env bash

echo "Setting environment variables"

export SPARK_HOME="/Users/mkalan/dev/spark-1.6.1-bin-hadoop2.6"
export MAIN_JAR_HOME="/Users/mkalan/github/mongo-spark-recommender-example/target/reco-engine-example-0.0.1.jar"
export MAIN_CLASS_NAME="ALSExampleMongoDB"
export NUM_THREADS="2"
export PACKAGES="org.mongodb.spark:mongo-spark-connector_2.10:0.1"
export MONGODB_INPUT_CONN_STRING=mongodb://127.0.0.1/movielens.ratings?readPreference=primaryPreferred
export MONGODB_OUTPUT_CONN_STRING=

# if using file for data
export DATA_HOME="/Users/mkalan/github/mongo-spark-recommender-example/data/sample_movielens_ratings.txt"


