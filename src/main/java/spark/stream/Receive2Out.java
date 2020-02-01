package spark.stream;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;

public class Receive2Out {

	/**
	 * 数据源 tcp  kafka ...
	 * 目标 控制台    
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JavaSparkContext sc = null;
		JavaStreamingContext jsc = null;
		try {
			sc = new JavaSparkContext(new SparkConf().setAppName("mavenCount").setMaster("local[2]"));

			jsc = new JavaStreamingContext(sc, Durations.seconds(5));
			JavaReceiverInputDStream<String> socketTextStream = jsc.socketTextStream("127.0.0.1", 7777);
			
			//trans 算子
			JavaDStream<String> dsRdd = socketTextStream.flatMap(f -> Arrays.asList(f.split(",")).iterator());
			JavaPairDStream<String, Integer> reduceByKey = dsRdd
					.mapToPair(f -> new Tuple2<String, Integer>(f, 1))
					.reduceByKey(new Function2<Integer, Integer, Integer>() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						@Override
						public Integer call(Integer arg0, Integer arg1) throws Exception {
							return arg0 + arg1;
						}
					});

			//actions算子  
			reduceByKey.foreachRDD(rdd->{
				//在这一步可以连接别的数据源，录入trans后的数据
				rdd.foreach(f->{
					System.out.println(f._1+":"+f._2);
					
				});
			});
			jsc.start();
			jsc.awaitTermination();
			sc.stop();
		} catch (Exception e) {
			e.printStackTrace();
			if (sc != null)
				sc.close();
			if (jsc != null)
				jsc.close();
		}
	}
}
