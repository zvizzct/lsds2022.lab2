package edu.upf;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import edu.upf.parser.ExtendedSimplifiedTweet;
import scala.Tuple2;

public class MostRetweetedApp {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        List<String> argsList = Arrays.asList(args);
        String output_folder = argsList.get(0);
        String input_file_folder = argsList.get(1);
        

        SparkConf conf = new SparkConf().setAppName("Most Retweeted");
        JavaSparkContext sc = new JavaSparkContext(conf);

        try { 
            JavaRDD<String> tweets = sc.textFile(input_file_folder);
            JavaRDD<ExtendedSimplifiedTweet> extendedTweets = tweets
                .map(ExtendedSimplifiedTweet::fromJson)
                .filter(Optional::isPresent)
                .map(Optional::get);

            // ------ Top10 most retweeted users ------
            JavaPairRDD<Long, Integer> userRetweets = extendedTweets
                .filter(tweet -> tweet.isRetweeted())
                .mapToPair(tweet -> new Tuple2<>(tweet.getRetweetedUserId(), 1))
                .reduceByKey(Integer::sum);
            
            JavaPairRDD<Integer, Long> sortedRetweets = userRetweets
                .mapToPair(Tuple2::swap)
                .sortByKey(false);
            
            List<Tuple2<Integer, Long>> top10users = sortedRetweets
                .take(10);
            
            JavaRDD<Tuple2<Integer, Long>> top10usersRDD = sc.parallelize(top10users);
            JavaRDD<String> top10usersFile = top10usersRDD.map(user_id -> user_id._2() + ": " + user_id._1());
            
            top10usersFile.saveAsTextFile(output_folder + "_users");

            // -----------------------------------------
            
            // ------ Top10 most retweeted tweets ------
            JavaPairRDD<Long, Integer> tweetRetweets = extendedTweets
                .filter(tweet -> tweet.isRetweeted())
                .mapToPair(tweet -> new Tuple2<>(tweet.getRetweetedTweetId(), 1))
                .reduceByKey(Integer::sum);
            
            JavaPairRDD<Integer, Long> sortedTweetRetweets = tweetRetweets
                .mapToPair(Tuple2::swap)
                .sortByKey(false);
            
            List<Tuple2<Integer, Long>> top10tweets = sortedTweetRetweets
                .take(10);

            JavaRDD<Tuple2<Integer, Long>> top10tweetsRDD = sc.parallelize(top10tweets);
            JavaRDD<String> top10tweetsFile = top10tweetsRDD.map(tweet_id -> tweet_id._2() + ": " + tweet_id._1());

            top10tweetsFile.saveAsTextFile(output_folder + "_tweets");
            // -----------------------------------------

        } catch (Exception e) {
            System.out.println(e);
        }

        sc.close();

        long end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start) + "ms");
    }
}