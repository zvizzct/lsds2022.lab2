package edu.upf;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import edu.upf.parser.SimplifiedTweet;
import java.util.Optional;

public class TwitterLanguageFilterApp {

    public static void main(String[] args){
        if (args.length < 3) {
            System.err.println("Usage: spark-submit --class edu.upf.TwitterLanguageFilterApp your.jar <language> <outputFolder> <inputFile/Folder>");
            System.exit(1);
        }

        String language = args[0];
        String outputFolder = args[1];
        String inputFile = args[2];

        SparkConf conf = new SparkConf().setAppName("Twitter Language Filter");
        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        long startTime = System.currentTimeMillis();

        try {
            // Load tweets from input
            JavaRDD<String> tweets = sparkContext.textFile(inputFile);

            // Filter tweets by language
            JavaRDD<String> filteredTweets = tweets
                .map(SimplifiedTweet::fromJson) // Convert each line into a SimplifiedTweet object
                .filter(Optional::isPresent) // Filter out any lines that couldn't be parsed
                .map(Optional::get) // Extract the SimplifiedTweet from the Optional
                .filter(tweet -> tweet.getLanguage().equals(language)) // Filter by language
                .map(SimplifiedTweet::toString); // Convert back to JSON string

            System.out.println("Total tweets in " + language + ": " + filteredTweets.count());
            
            // Save the filtered tweets to the specified output folder
            filteredTweets.saveAsTextFile(outputFolder);
        } finally {
            long endTime = System.currentTimeMillis();
            System.out.println("Total execution time: " + (endTime-startTime) + "ms"); 
            sparkContext.close(); 
        }
    }
}
