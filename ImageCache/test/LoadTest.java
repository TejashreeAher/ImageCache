
import com.cache.imagecache.Cache;
import com.cache.imagecache.CacheFactory;
import com.image.cache.entry.CacheEntry;
import com.image.cache.entry.ImageCacheEntry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tejashree.aher
 */
public class LoadTest {
    private static final int NUMBER_OF_PROCESSING_THREADS = 10;
    private static LinkedBlockingQueue<ImageCacheEntry> cacheEntryList = new LinkedBlockingQueue<ImageCacheEntry>();
    private static LinkedBlockingQueue<String> keyListToGet = new LinkedBlockingQueue<String>();
    
    private static ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_PROCESSING_THREADS);
    private static ExecutorService executorService2 = Executors.newFixedThreadPool(NUMBER_OF_PROCESSING_THREADS);
    
    private static class CacheAddingTask implements Runnable{

        @Override
        public void run() {
            while(cacheEntryList != null && cacheEntryList.size() > 0){
                ImageCacheEntry entry = cacheEntryList.poll();
                Cache imageCache = CacheFactory.getCache("imageCache");
                imageCache.add(entry.getImageURL(), entry, Boolean.TRUE);
                keyListToGet.add(entry.getImageURL());
                System.out.println("Added "+ entry.getImageURL()+" to image cache by Thread: "+ Thread.currentThread().getId());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoadTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           
        }
        
    }
    
     private static class CacheRetrievalTask implements Runnable{

        @Override
        public void run() {
            while(keyListToGet != null && keyListToGet.size() > 0){
                String key = keyListToGet.poll();
                Cache imageCache = CacheFactory.getCache("imageCache");
                CacheEntry entryFromCache = imageCache.get(key);
            }
        }
        
    }
    
    ///This starts two independent executorservice one for get and the other for add and checks the cache at each condition. Last accessed gets added at the end of the map (most recently accesed)
     
    public static void main(String[] args){
        for(int i=0; i<20; i++){
            ImageCacheEntry entry = new ImageCacheEntry("http://example.com/"+i+".jpg", null);
            cacheEntryList.add(entry);
        }
        for(int i=0; i<20; i++){
            keyListToGet.add("http://example.com/"+i+".jpg");
            CacheFactory.getCache("imagecache").add("http://example.com/"+i+".jpg", null, Boolean.TRUE); //add all to cache
        }
        
        CacheAddingTask addTask = new CacheAddingTask();
        executorService.submit(addTask);
        
        CacheRetrievalTask getTask = new CacheRetrievalTask();
        executorService2.submit(getTask);
    }


    
}
