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
	 * ����Դ  	source.txt
	 * Ŀ��   		����̨
	 * 
	 * @param <U>
	 * @param args
	 */
	public static <U> void main(String[] args) {
		JavaSparkContext sc = null;
		SparkSession sc1 = null;
		try{
			
		
		//System.out.println(args[0]);
		//1.ֱ�Ӵ���spark context
		 sc =  new JavaSparkContext(
				new SparkConf().setAppName("mavenCount").setMaster("local[2]")
				);
		 
		 JavaRDD<String> rdd = sc.textFile("C://Users//pc0062//Desktop//spark-sample//src//main//java//spark//core//source.txt");
		//trans����
		 JavaRDD<String> mavenRdd = rdd.filter(line->line.contains("Maven"));
		 
		 // action����  �������maven����
		 mavenRdd.foreach(line->{
			 System.out.println(line);	 
		 });
		 
		 
		 
		 //trans����   
		 JavaPairRDD<String, Iterable<String>> groupByKey = mavenRdd.
				 flatMap(x->Arrays.asList(x.split(" ")).iterator())
				 .mapToPair(f->new Tuple2<>(f.split(" ")[0], "1"))
				 //.aggregateByKey(zeroValue, seqFunc, combFunc)
				 //.combineByKey(createCombiner, mergeValue, mergeCombiners)
				 .groupByKey();
		 
		 
		 //action���� ������е��� 
		 //���������ֱ�������ݵ����ݿ� groupByKey.saveas
		 groupByKey.foreach(f->{
			System.out.println(f._1);
			
		});		 
		 
		 
		 Thread.sleep(1000*60);
		 sc.stop();
		 
//		2.����sparksql����Context  Ҳ���Լ������ݣ�����session����һֱ��
		 sc1 = SparkSession.builder()
		        .appName("mavenCount")
		        .master("local[2]")//����ģʽ
		  //    .master("spark://192.168.81.128:7077")//����Ǽ�Ⱥģʽ����Ҫָ��master��ַ
				.getOrCreate();
		
		
		JavaRDD<String> rdd1 = sc1.read().textFile("C://Users//pc0062//Desktop//spark-sample//src//main//java//spark//core//source.txt").javaRDD();
//		���ϲ�ѯ  ���������ݻ�۵�һ��rdd...
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
