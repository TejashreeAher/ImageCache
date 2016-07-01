package com.cache.imagecache;

/**
 *
 * @author tejashree.aher
 */
public class CacheFactory {
    public static Cache getCache(String type) {
//        if (("inMemoryImageCache").equalsIgnoreCase(type)) {
//            return ImageCache.getInstance();
//        }else{
//            return null;
//        }
        //in this case, there is only one type of cache
        return ImageCache.getInstance();
    }
}
