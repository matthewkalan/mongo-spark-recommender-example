#!/usr/bin/env bash

# Run recommendation engine with Movielens data set in MongoDB, with Spark ALS ML algo

# Set environment variables
source set_env.sh

echo "Spark home = $SPARK_HOME"

# Run recommendation engine example
exec $SPARK_HOME/bin/spark-submit \
	--class $MAIN_CLASS_NAME \
	--packages $PACKAGES \
	$MAIN_JAR_HOME \
	mongodb \
	$MONGODB_INPUT_CONN_STRING \
	$MONGODB_OUTPUT_CONN_STRING \
	$THRESHOLD 
	