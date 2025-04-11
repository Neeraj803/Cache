/* Technologies Used:
	•	Java 17+ (or any recent Java version)
	•	Spring Boot (for easy API exposure, optional)
	•	ConcurrentHashMap & LinkedHashMap (for cache storage with LRU eviction)
	•	MySQL (for persistence)
	•	JUnit (for testing) 
	
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