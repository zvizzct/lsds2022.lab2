# Lab 2 Solution: Spark Batch Applications on ElasticMapReduce

## Objective

This lab was aimed at leveraging Apache Spark for batch processing tasks on AWS ElasticMapReduce (EMR). The tasks included implementing a Twitter Language Filter, analyzing most popular bi-grams in tweets by language, and identifying the most retweeted tweets for the most retweeted users. The goal was to enhance our understanding of Spark's capabilities in distributed data processing and gain hands-on experience with AWS EMR.

## Implemented Classes

1. **TwitterLanguageFilterApp**: Filters tweets by language and stores the results in S3.
2. **BiGramsApp**: Analyzes tweets to find the most popular bi-grams for a specified language.
3. **MostRetweetedApp**: Identifies the most retweeted tweet for the top-10 most retweeted users.

## Dependencies

- Apache Spark: For distributed data processing.
- AWS SDK: For accessing AWS services like S3 and EMR.
- Gson: For JSON processing.
- JUnit: For testing the applications.

## Additional Remarks

- The applications were designed to be modular and reusable.
- AWS credentials were configured properly to ensure access to S3 and EMR.
- The Spark configurations were optimized based on the dataset size and EMR cluster capacity.

## Solutions

### Benchmarking on EMR

The TwitterLanguageFilterApp was benchmarked on AWS EMR for different languages with the following results:

- Spanish (`es`): 3 minutes, 20 seconds
- Catalan (`ca`): 3 minutes, 6 seconds
- English (`en`): 3 minutes, 26 seconds

### Bigrams Output

The following table presents the top-10 most popular bi-grams from the tweets in Spanish (`es`), Catalan (`ca`), and English (`en`):

| Spanish (`es`)                      | Catalan (`ca`)                           | English (`en`)           |
| ----------------------------------- | ---------------------------------------- | ------------------------ |
| #eurovision #finaleurovision: 22545 | alexander rybak: 404                     | of the: 21279            |
| en el: 21448                        | es el: 369                               | in the: 13665            |
| de la: 19980                        | de noruega.: 347                         | for the: 11443           |
| en #eurovision: 16049               | #eurovision https://t.co/b091qrmq5l: 346 | this is: 11440           |
| que no: 15545                       | el jordi: 346                            | the uk: 9874             |
| en la: 13135                        | realmente es: 346                        | rt @eurovision:: 9747    |
| el año: 12610                       | noruega. #eurovision: 346                | rt @bbceurovision:: 9558 |
| lo que: 12465                       | jordi hurtado: 346                       | vote for: 9185           |
| a la: 11812                         | rybak realmente: 346                     | song contest: 8393       |
| que el: 11507                       | hurtado de: 346                          | is the: 8182             |

### Most Retweeted Output

The top-10 most retweeted tweets for the most retweeted users were as follows:

1. Tweet ID `995356756770467840`: 10809 retweets
2. Tweet ID `995435123351973890`: 5420 retweets
3. Tweet ID `995381560277979136`: 4643 retweets
4. Tweet ID `995406052190445568`: 4138 retweets
5. Tweet ID `995417089656672256`: 3944 retweets
6. Tweet ID `995384555719839744`: 3668 retweets
7. Tweet ID `995451073606406144`: 2991 retweets
8. Tweet ID `995374825353904128`: 2278 retweets
9. Tweet ID `995407527864041473`: 2127 retweets
10. Tweet ID `995388604045316097`: 2113 retweets
