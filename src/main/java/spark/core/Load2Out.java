package spark.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

public class Load2Out {


	/**
	 * 数据源  	source.txt
	 * 目标   		控制台
	 * 
	 * @param <U>
	 * @param args
	 */
	public static <U> void main(String[] args) {
		JavaSparkContext sc = null;
		SparkSession sc1 = null;
		try{
			
		
		//System.out.println(args[0]);
		//1.直接创建spark context
		 sc =  new JavaSparkContext(
				new SparkConf().setAppName("mavenCount").setMaster("local[2]")
				);
		 
		 JavaRDD<String> rdd = sc.textFile("C://Users//pc0062//Desktop//spark-sample//src//main//java//spark//core//source.txt");
		//trans算子
		 JavaRDD<String> mavenRdd = rdd.filter(line->line.contains("Maven"));
		 
		 // action算子  输出包含maven的行
		 mavenRdd.foreach(line->{
			 System.out.println(line);	 
		 });
		 
		 
		 
		 //trans算子   
		 JavaPairRDD<String, Iterable<String>> groupByKey = mavenRdd.
				 flatMap(x->Arrays.asList(x.split(" ")).iterator())
				 .mapToPair(f->new Tuple2<>(f.split(" ")[0], "1"))
				 //.aggregateByKey(zeroValue, seqFunc, combFunc)
				 //.combineByKey(createCombiner, mergeValue, mergeCombiners)
				 .groupByKey();
		 
		 
		 //action算子 输出所有单词 
		 //在这里可以直接赛数据到数据库 groupByKey.saveas
		 groupByKey.foreach(f->{
			System.out.println(f._1);
			
		});		 
		 
		 
		 Thread.sleep(1000*60);
		 sc.stop();
		 
//		2.利用sparksql创建Context  也可以加载数据，并且session可以一直用
		 sc1 = SparkSession.builder()
		        .appName("mavenCount")
		        .master("local[2]")//本地模式
		  //    .master("spark://192.168.81.128:7077")//如果是集群模式，需要指定master地址
				.getOrCreate();
		
		
		JavaRDD<String> rdd1 = sc1.read().textFile("C://Users//pc0062//Desktop//spark-sample//src//main//java//spark//core//source.txt").javaRDD();
//		联合查询  把所有数据汇聚到一个rdd...
//		rdd.union(rdd1);
		
		
		}catch (Exception e) {
			e.printStackTrace();
			if(sc!=null){
				sc.close();
			}
			if(sc1 != null){
				sc1.close();
			}
		}
	}

}
