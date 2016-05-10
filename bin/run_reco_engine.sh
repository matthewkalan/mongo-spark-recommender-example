#!/usr/bin/env bash

# Run recommendation engine with Movielens data set in MongoDB, with Spark ALS ML algo

# Set environment variables
source set_env.sh

echo "Spark home = $SPARK_HOME"

# Run recommendation engine example
exec $SPARK_HOME/bin/spark-submit \
	--class $MAIN_CLASS_NAME \
	--master local[2] \
	--packages $PACKAGES \
	--conf "spark.mongodb.input.uri=$MONGODB_INPUT_CONN_STRING" \
	$MAIN_JAR_HOME \
	mongodb #\
	#$DATA_HOME 
