
## DISCLAIMER

Please note: all tools/ scripts in this repo are released for use "AS IS" without any warranties of any kind, including, but not limited to their installation, use, or performance. We disclaim any and all warranties, either express or implied, including but not limited to any warranty of noninfringement, merchantability, and/ or fitness for a particular purpose. We do not warrant that the technology will meet your requirements, that the operation thereof will be uninterrupted or error-free, or that any errors will be corrected. Any use of these scripts and tools is at your own risk. There is no guarantee that they have been through thorough testing in a comparable environment and we are not responsible for any damage or data loss incurred with their use. You are responsible for reviewing and testing any scripts you run thoroughly before use in any non-testing environment.

## Background

This is an example referenced in the MongoDB World 2016 talk, Architecting Wide-Ranging Analytics with MongoDB.

The primary value of this project is to show a working example of a recommendation engine based on an Alternative Least Squares (ALS) machine learning (ML) algorithm with Spark using data in MongoDB.  

The only source file is src/ALSExampleMongoDB.scala, based on an example found at the address below, but using MongoDB as the source of user ratings data and for writing the predictions.    
https://benfradet.github.io/blog/2016/02/15/Alernating-least-squares-and-collaborative-filtering-in-spark.ml

## Dependencies for Running the Example

You will need to [install Spark](http://spark.apache.org/downloads.html) or otherwise run it from a hosting service.  It is set up for best practices to run with [Databricks](http://databricks.com).  

Also you will need to have a MongoDB database running on a hostname and port.  There are multiple options to [running MongoDB](https://www.mongodb.com/download-center), either with the cloud service Atlas, or downloading and installing.  

You should decompress the data from /data/ratings.csv.zip and import it into MongoDB using [mongoimport]() with a command similar to:
mongodb-osx-x86_64-3.2.7-ent/bin/mongoimport --type csv -d movielens -c ratings --port 27017 --fields userId,movieId,rating,timestamp ratings.csv

You will also need Maven or the ability to otherwise create a JAR from the source files.  I used the Maven plug-in in Eclipse with the pom.xml in the root directory of this project.  The Instructions below assume you have built the JAR.  

To optionally build the project in Eclipse (with the Perspective for Scala), I downloaded JARS from the Central Maven repository for the [Java Driver](http://search.maven.org/remotecontent?filepath=org/mongodb/mongo-java-driver/3.2.2/mongo-java-driver-3.2.2.jar) and [MongoDB Spark Connector](http://search.maven.org/remotecontent?filepath=org/mongodb/spark/mongo-spark-connector_2.10/1.0.0/mongo-spark-connector_2.10-1.0.0.jar).  I would have thought the Maven plug-in in Eclipse would take care of those dependencies but it didn't for me so I referenced those JARS directly in my build path.  You should change the build path accordingly in the Eclipse project.  

## Instructions For Running the Example

* Clone this project from Github or otherwise download it
git clone https://github.com/matthewkalan/mongo-spark-recommender-example.git

* Make sure the dependencies above are addressed

* If you have run the example before, drop the predictions table so that it can be generated again during the run.  

* Edit the script in bin/set_env.sh specific to your environment

* Run bin/run_reco_engine.sh.  You will see output in the console with the Root mean square error of the predicted user ratings and the Total running time.  

