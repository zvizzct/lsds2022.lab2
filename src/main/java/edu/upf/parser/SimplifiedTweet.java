package edu.upf.parser;

import java.util.Optional;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SimplifiedTweet {

  private static final Gson gsonInstance = new Gson();

  private final long tweetId;              //  ID of the tweet
  private final String message;            //  content of the tweet
  private final long userId;               //  ID of the user
  private final String username;           //  users name
  private final String lang;               //  language of the tweet
  private final long createdAt;            //  milliseconds 
  
  public SimplifiedTweet(long tweetId, String message, long userId, String username, String lang, long createdAt) {
    this.tweetId = tweetId;
    this.message = message;
    this.userId = userId;
    this.username = username;
    this.lang = lang;
    this.createdAt = createdAt;
  }

  /**
   * Creates a SimplifiedTweet from a JSON string.
   * Returns an empty Optional if parsing fails for any reason.
   *
   * @param jsonStr The JSON string to parse.
   * @return An Optional of SimplifiedTweet.
   */
  public static Optional<SimplifiedTweet> fromJson(String jsonStr) {
    try {
      JsonObject json = gsonInstance.fromJson(jsonStr, JsonObject.class);
      long id = json.get("id").getAsLong();
      String text = json.get("text").getAsString();
      JsonObject user = json.getAsJsonObject("user");
      long userId = user.get("id").getAsLong();
      String userName = user.get("name").getAsString();
      String language = json.get("lang").getAsString();
      long timestamp = json.has("timestamp_ms") ? json.get("timestamp_ms").getAsLong() : 0;

      return Optional.of(new SimplifiedTweet(id, text, userId, userName, language, timestamp));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public String toString() {
    return gsonInstance.toJson(this);
  }

  public String getLanguage() {
    return lang;
  }
}
