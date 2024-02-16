package src.code;

/*
 * Arquivo que descreve o modelo das entities do dataset usado.
 */

public class Model {
    private String video_id;
    private String trending_date; // Change to date type later?
    private String title;
    private String channel_title;
    private short category_id;   // From 0 to 44
    private String publish_time; // Change to date type later?
    private String tags;
    private long views;          // Change to int later?
    private long likes;          // Change to int later?
    private long dislikes;       // Change to int later?
    private int comment_count;
    private String thumbnail_link;
    private boolean comments_disabled;
    private boolean ratings_disabled;
    private boolean video_error_or_removed;
    private String description;

    public short getCategory_id() {
        return category_id;
    }
    public String getChannel_title() {
        return channel_title;
    }
    public int getComment_count() {
        return comment_count;
    }
    public String getDescription() {
        return description;
    }
    public long getDislikes() {
        return dislikes;
    }
    public long getLikes() {
        return likes;
    }
    public String getPublish_time() {
        return publish_time;
    }
    public String getTags() {
        return tags;
    }
    public String getThumbnail_link() {
        return thumbnail_link;
    }
    public String getTitle() {
        return title;
    }
    public String getTrending_date() {
        return trending_date;
    }
    public String getVideo_id() {
        return video_id;
    }
    public long getViews() {
        return views;
    }
    public boolean getComments_disabled()
    {
        return comments_disabled;
    }
    public boolean getRatings_disabled()
    {
        return ratings_disabled;
    }
    public boolean getVideo_error_or_removed()
    {
        return video_error_or_removed;
    }
    
    public void setCategory_id(short category_id) {
        this.category_id = category_id;
    }
    public void setChannel_title(String channel_title) {
        this.channel_title = channel_title;
    }
    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }
    public void setComments_disabled(boolean comments_disabled) {
        this.comments_disabled = comments_disabled;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }
    public void setLikes(long likes) {
        this.likes = likes;
    }
    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }
    public void setRatings_disabled(boolean ratings_disabled) {
        this.ratings_disabled = ratings_disabled;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public void setThumbnail_link(String thumbnail_link) {
        this.thumbnail_link = thumbnail_link;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setTrending_date(String trending_date) {
        this.trending_date = trending_date;
    }
    public void setVideo_error_or_removed(boolean video_error_or_removed) {
        this.video_error_or_removed = video_error_or_removed;
    }
    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
    public void setViews(long views) {
        this.views = views;
    }

}
