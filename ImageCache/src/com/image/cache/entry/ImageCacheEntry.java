package com.image.cache.entry;

/**
 *
 * @author tejashree.aher
 */
public class ImageCacheEntry extends CacheEntry{
    String imageURL; 
    byte[] imageContent;
    long timestamp;

    public ImageCacheEntry(){
        
    }
    public ImageCacheEntry(String imageURL, byte[] imageContent){
        this.imageURL = imageURL;
        this.imageContent = imageContent;
        this.timestamp = System.currentTimeMillis();
    }
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public byte[] getImageContent() {
        return imageContent;
    }

    public void setImageContent(byte[] imageContent) {
        this.imageContent = imageContent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
}
