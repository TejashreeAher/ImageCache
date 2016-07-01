/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cache.imagecache;

import com.image.cache.entry.CacheEntry;

/**
 *
 * @author tejashree.aher
 * @param <T>
 * @param <E>
 */
public interface Cache<T extends Object, E extends CacheEntry> {
    
    public abstract boolean add(T key, E entry, Boolean replaceIfNotPresent);
    
    public abstract E get(T key);
    
    public abstract CacheEntry evict(T key);
    
    public abstract boolean evictAll();
    
}
