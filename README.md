Description :
1. Cache.java is the interface which can be implemented as required
2. ImageCache.java saves 10 most recently used images.
3. Get method searches for images in memory, if not found return null (cache miss). It does  not get images from the network as it has to be fast. Cache miss handling can be done at the application level as per the requirement of the application
4. This cache emphasises on high speed get requests. Also, it is assumed that server can cater to any number of requests and if the requests exceed the capacity of the server they might not be served
5. LoadTest.java can be run to test the working of this cache


Enhancements :
1. Some statsd profiller can be added to count the number of cache miss and check the performance of cache
2. Profilling around the time take by get and add methods
3. Currently it is assumed that the cache can serve all the requests. It might be possible that the requests reach a limit of server's capacity wherein a queue should be introduced so that no request is lost. This however, can make get requests slow. Therefore there is a tradeoff between speed and ensuring serving all requests. Current implementation chose speedy get requests over the other
4. Cache clusters can be used either for replication (wherein each cluster should be notified and kept updated using some messaging protocol) or using sharding. For sharding, we would need some kind of proxy and hashing mechanism for the proxy to decide which cluster the entry should go in cache.
. Size of inmemory(currently 10) can be made configurable
4. Cache miss can be minimised by either storing more images in a way that they can be accessed faster
