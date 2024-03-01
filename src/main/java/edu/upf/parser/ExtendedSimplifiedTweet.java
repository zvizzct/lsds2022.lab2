package edu.upf.parser;

import java.util.Optional;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.Serializable;

public class ExtendedSimplifiedTweet implements Serializable {
    private static final Gson gsonInstance = new Gson();

    private final long tweetId;
    private final String text;
    private final long userId;
    private final String userName;
    private final long followersCount;
    private final String language;
    private final boolean isRetweeted;
    private final Long retweetedUserId;
    private final Long retweetedTweetId;
    private final long timestampMs;

    public ExtendedSimplifiedTweet(long tweetId, String text, long userId, String userName, long followersCount,
            String language, boolean isRetweeted, Long retweetedUserId, Long retweetedTweetId, long timestampMs) {
        this.tweetId = tweetId;
        this.text = text;
        this.userId = userId;
        this.userName = userName;
        this.followersCount = followersCount;
        this.language = language;
        this.isRetweeted = isRetweeted;
        this.retweetedUserId = retweetedUserId;
        this.retweetedTweetId = retweetedTweetId;
        this.timestampMs = timestampMs;
    }

    /**
     * Returns a {@link ExtendedSimplifiedTweet} from a JSON String.
     * If parsing fails, for any reason, return an {@link Optional#empty()}
     *
     * @param jsonStr
     * @return an {@link Optional} of a {@link ExtendedSimplifiedTweet}
     */
    public static Optional<ExtendedSimplifiedTweet> fromJson(String jsonStr) {
        try {
            JsonObject json = gsonInstance.fromJson(jsonStr, JsonObject.class);
            long id = json.get("id").getAsLong();
            String text = json.get("text").getAsString();
            JsonObject user = json.getAsJsonObject("user");
            long userId = user.get("id").getAsLong();
            String userName = user.get("name").getAsString();
            long followersCount = user.get("followers_count").getAsLong();
            String language = json.get("lang").getAsString();
            boolean isRetweeted = json.has("retweeted_status");
            Long retweetedUserId = isRetweeted
                    ? json.getAsJsonObject("retweeted_status").getAsJsonObject("user").get("id").getAsLong()
                    : null;
            Long retweetedTweetId = isRetweeted ? json.getAsJsonObject("retweeted_status").get("id").getAsLong() : null;
            long timestamp = json.has("timestamp_ms") ? json.get("timestamp_ms").getAsLong() : 0;

            return Optional.of(new ExtendedSimplifiedTweet(id, text, userId, userName, followersCount, language,
                    isRetweeted, retweetedUserId, retweetedTweetId, timestamp));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public String getText() {
        return text;
    }

    public String getLanguage() {
        return language;
    }

    public static String[] normalise(String word) {
        return word.trim().toLowerCase().split("\\s+");
    }

    public long getRetweetedUserId() {
        return this.retweetedUserId;
    }

    public long getRetweetedTweetId() {
        return this.retweetedTweetId;
    }
}
