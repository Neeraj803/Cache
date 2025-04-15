/* Technologies Used:
	•	Java 17+ (or any recent Java version)
	•	Spring Boot (for easy API exposure, optional)
	•	ConcurrentHashMap & LinkedHashMap (for cache storage with LRU eviction)
	•	MySQL (for persistence)
	•	JUnit (for testing) 
	
(i)	Overview:
	The CacheController provides a RESTful API interface for managing entities in a cache system that also integrates               with a persistent database. It allows adding, retrieving, and deleting cached data, ensuring that the cache remains updated and consistent with the database when necessary.
	
(ii)	Feature:
	
	1. Acts as a controller layer in a typical Spring Boot application.

    2. Interacts with the CacheService to perform cache and database operations.

    3. Provides endpoints for common cache operations with robust exception handling.

    4. Uses Swagger annotations for API documentation.
	
(iii)	LRU Eviction: LRU (Least Recently Used) is a cache eviction strategy that removes the least recently accessed     item when the cache 
	reaches its capacity. This keeps the most recently used items in the cache.
	 
	 DB setup:- using MYSQL Database
	  spring.datasource.url=jdbc:mysql://localhost:3306/cache
      spring.datasource.username=root
      spring.datasource.password=Neeraj@123
      spring.jpa.hibernate.ddl-auto=update
      spring.jpa.show-sql=true	
      
(iv)  cache size details and how the cache is initiated: 

  The cache is implemented using a custom LRUCache class that extends LinkedHashMap with a fixed capacity.
  private static final int CACHE_CAPACITY = 5;
  
  This value determines the maximum number of entries allowed in the cache at any given time. Once the size exceeds this   limit, the least recently used (LRU) entry is automatically evicted.
  
  
  (V) Test Scenarios:
      1. Add Entity to Cache (POST /cache/add)
      2. Get Entity from Cache or DB (GET /cache/get/{id})
      3. Remove Entity by ID (DELETE /cache/remove/{id})
      4. Clear Entire Cache (DELETE /cache/clear)
      5. Remove All (Cache + DB) (DELETE /cache/removeAll)
      6. LRU Eviction Logic Validation
      
	1.In this API cache size is 5. if cache size full save eldest entery in Database.
	   a) http://localhost:9091/cache/add 
    body-> {"id":"7", "data":"Bank of america"}
	
	2. we can get data from cache and database basis of Id,
	 a)  get by id url-> http://localhost:9091/cache/get/1
	
	3. we can remove data from cache and DB basis of id.
	     http://localhost:9091/cache/remove/3
	
	4. we can clear cache without effecting db.
	  a)   http://localhost:9091/cache/clear
	
	5. we can delete complete data from cache and database using removeall api.
	   a http://localhost:9091/cache/removeAll
	
	6. swagger link
    springdoc.swagger-ui.path=/swagger-ui.html
    
   
	*
	*/