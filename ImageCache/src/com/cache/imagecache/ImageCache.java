/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cache.imagecache;

import com.image.cache.entry.ImageCacheEntry;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 *
 * @author tejashree.aher
 * key is the imageURL, Value is the ImageCacheEntry in which image content as well as key (imageURL) are stored
 */
public class ImageCache implements Cache<String, ImageCacheEntry>{
    private static final int SIZE = 10;
    private static ImageCache cacheInstance;
    private final ReentrantLock lock = new ReentrantLock();
    private static final Logger LOGGER = Logger.getLogger(ImageCache.class.getCanonicalName());
     
    //This helps in retrieving in O(1) time as well as maintains order of the keys. Reads as threadsafe, need to take care of writes
    //This is a combination of Hashmap and doubly linked list
    private static volatile Map<String, ImageCacheEntry> map = Collections.synchronizedMap(new LinkedHashMap<String, ImageCacheEntry>(){
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            //This will remove oldest entries when size becomes greater than SIZE
            return size() > SIZE;
        }
    });
   
    //Singleton
    public static synchronized ImageCache getInstance(){
        if(cacheInstance == null){
            cacheInstance = new ImageCache();
        }
        return cacheInstance;
    }
    
    private ImageCache(){
        
    }
    
    @Override
    public boolean evictAll() {
        lock.lock();
        try{
            map.clear();
        }finally{
            lock.unlock();
        }
        return true;
    }

    @Override
    public boolean add(String key, ImageCacheEntry value, Boolean replaceIfPresent) {
        LOGGER.info("Add entry for key: "+ key +" started at time: " + System.currentTimeMillis());
        ImageCacheEntry cacheValue = map.get(key);
        if(cacheValue != null){
            if(!replaceIfPresent){
                //return as the key is already present and it is not to be replaced with new value
                return false;
            }
        }
        ///need to make concurrent operations thread safe
        lock.lock();
        try {
            map.remove(key);
            map.put(key, value);
            System.out.println("Map after adding "+ key +"now has : "+ map +" by thread : "+ Thread.currentThread().getId()); ///this is to verify for running the load test, can be removed
        } finally {
            lock.unlock();
        }
        LOGGER.info("Add entry for key: "+ key +" ended at time: "+ System.currentTimeMillis());
        return true;
    }

    //no lock needed in get as get gives the last updated and get should be very fast.
    @Override
    public ImageCacheEntry get(String key) {
        LOGGER.info("Get for key: "+ key +" started at " + System.currentTimeMillis());
        if(!map.containsKey(key)){
            System.out.println("Cache miss for key : "+ key +"by thread: "+Thread.currentThread().getId());///Some statsd counter can be used here to track number of cache miss for any interval of time
            return null;
        }
        ImageCacheEntry cacheValue;
        lock.lock();
        try {
            cacheValue = map.get(key);
            map.remove(key);
            map.put(key, cacheValue);
            System.out.println("Map now after getting "+ key +" has : "+ map +"by thread: "+ Thread.currentThread().getId()); ///this is to verify for running the load test, can be removed
        } finally {
            lock.unlock();
        }
        LOGGER.info("Get for key: "+ key +" finished at time: "+ System.currentTimeMillis()); ///this can be replaced by some profiller to record time to get from cache
        return cacheValue; //get is threadsafe
    }

    @Override
    //Returns the entry removed from the cache
    public ImageCacheEntry evict(String key) {
        if(!map.containsKey(key)){
            return null;
        }
        ImageCacheEntry cacheEntry = map.remove(key);
        return cacheEntry;
    }
    
}
