# Run on a Spark standalone cluster in client deploy mode
./bin/spark-submit \
  --class  spark.core.Load2Out  \
  --master spark://*.*.*:7077 \
  --executor-memory 20G \
  --total-executor-cores 100 \
  /spark-sample-1.0.0-SNAPSHOT.jar \
  1