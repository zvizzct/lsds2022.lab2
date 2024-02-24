package spark;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;
import scala.Tuple2;

import java.util.Arrays;

public class WordCount {

    public static void main(String[] args){
        String input = args[0];
        String outputDir = args[1];

        //Create a SparkContext to initialize
        SparkConf conf = new SparkConf().setAppName("Word Count");
        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        // Load input
        JavaRDD<String> sentences = sparkContext.textFile(input);

        JavaPairRDD<String, Integer> counts = sentences
            .flatMap(s -> Arrays.asList(s.split("[ ]")).iterator())
            .map(word -> normalise(word))
            .mapToPair(word -> new Tuple2<>(word, 1))
            .reduceByKey((a, b) -> a + b);
        System.out.println("Total words: " + counts.count());
        counts.saveAsTextFile(outputDir);
    }

    private static String normalise(String word) {
        return word.trim().toLowerCase();
    }
}
// spark-submit --class spark.WordCount --master local[2] target/spark-test-1.0-SNAPSHOT.jar ./shakespeare.txt ./_output-folder
