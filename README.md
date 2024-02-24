# Lab 2: Spark Batch Applications on ElasticMapReduce

### Objective

This lab focuses on leveraging Apache Spark for batch processing tasks, specifically implementing a TwitterLanguageFilter application that filters tweets by language. The application is designed to run on AWS ElasticMapReduce (EMR) and includes enhancements to the SimplifiedTweet class for accessing additional tweet fields, enabling more complex data processing tasks with Spark.

### How to Use

1. Prepare your Spark application by packaging it with Maven. Ensure your main class is named `TwitterLanguageFilterApp` and located within the `edu.upf` package.

2. Upload your compiled jar to an S3 bucket structured as follows: `lsds2024.lab2.output.<USER-ID>/jars/your-jar-file(s)`.

3. Load your input data onto S3 in the `input` directory of the same bucket.

4. Create an EMR cluster with Spark support through the AWS Web Console. Refer to the provided Lab 2 slides for detailed instructions.

5. Add a Spark step in the AWS console to execute your application, specifying the jar location on S3 and the main class to run.

6. Use the following format for running your Spark job:
   `spark-submit --master <YOUR MASTER> --class edu.upf.TwitterLanguageFilterApp your.jar <language> <output> <inputFile/Folder>`

### Benchmarking on EMR

To benchmark the performance of your Spark-based TwitterFilter application on EMR, follow these steps:

1. Run the application for different languages (e.g., Spanish `es`, English `en`, and Catalan `ca`), and store the results in their respective directories within the `benchmark` folder on S3.

2. Document the elapsed time for each run as observed in the AWS console.

3. Note that you can experiment with running Spark on different numbers of cores, limited by your EMR cluster's configuration, to observe the impact on processing time.

### Additional Remarks

- Ensure your AWS credentials are properly set up for access to S3 and EMR.
- Remember to terminate your EMR cluster after your experiments to avoid unnecessary AWS charges.
- The input files must be in JSON format, containing tweets from the specified event or dataset. The output will be stored in a designated directory on S3.

### Dependencies

This project requires the following dependencies:

- Apache Spark for distributed data processing.
- AWS SDK for integration with AWS services like S3 and EMR.
- Gson for JSON processing.
- JUnit for testing.

### Important Notes

- Adjust your Spark configurations (e.g., number of executors, core and memory settings) based on the size of your dataset and the capacity of your EMR cluster to optimize performance.
- Monitor the AWS EMR console for logs and metrics that can help troubleshoot any issues or further tune your application's performance.

### Benchmark

Running in 8 cluster local: Total execution time: 293655ms
