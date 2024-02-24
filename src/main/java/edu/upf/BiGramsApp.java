package edu.upf;

import edu.upf.parser.ExtendedSimplifiedTweet;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BiGramsApp {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println(
                    "Usage: spark-submit --class edu.upf.BiGramsApp your.jar <language> <outputFolder> <inputFile/Folder>");
            System.exit(1);
        }

        String language = args[0];
        String outputFolder = args[1];
        String inputFile = args[2];

        SparkConf conf = new SparkConf().setAppName("BiGramsApp");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> lines = sc.textFile(inputFile);
        JavaRDD<ExtendedSimplifiedTweet> tweets = lines.flatMap(line -> {
            Optional<ExtendedSimplifiedTweet> tweetOpt = ExtendedSimplifiedTweet.fromJson(line);
            return tweetOpt.map(List::of).orElseGet(List::of).iterator();
        });

        // Filter tweets by language
        JavaRDD<ExtendedSimplifiedTweet> filteredTweets = tweets.filter(tweet -> tweet.getLanguage().equals(language));

        // Generate bi-grams
        JavaPairRDD<String, Integer> biGrams = filteredTweets.flatMapToPair(tweet -> {
            List<Tuple2<String, Integer>> results = new ArrayList<>();
            String[] words = tweet.getText().toLowerCase().split("\\s+");
            for (int i = 0; i < words.length - 1; i++) {
                // Filter out empty pairs
                if (!words[i].isEmpty() && !words[i + 1].isEmpty()) {
                    String biGram = words[i] + " " + words[i + 1];
                    results.add(new Tuple2<>(biGram, 1));
                }
            }
            return results.iterator();
        });

        // Count and sort bi-grams
        JavaPairRDD<String, Integer> countedBiGrams = biGrams.reduceByKey(Integer::sum);
        JavaPairRDD<Integer, String> swappedBiGrams = countedBiGrams.mapToPair(Tuple2::swap);
        JavaPairRDD<Integer, String> sortedBiGrams = swappedBiGrams.sortByKey(false);

        // Take top 10
        List<Tuple2<Integer, String>> top10BiGrams = sortedBiGrams.take(10);

        // Save the top 10 bigrams to a file
        JavaRDD<Tuple2<Integer, String>> top10BiGramsRDD = sc.parallelize(top10BiGrams);
        JavaRDD<String> top10BiGramsString = top10BiGramsRDD.map(tuple -> tuple._2() + ": " + tuple._1());
        top10BiGramsString.saveAsTextFile(outputFolder + "/top10BiGrams");

        sc.close();
    }
}
