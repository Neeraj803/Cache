package com.example.Cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* Technologies Used:
	•	Java 17+ (or any recent Java version)
	•	Spring Boot (for easy API exposure, optional)
	•	ConcurrentHashMap & LinkedHashMap (for cache storage with LRU eviction)
	•	MySQL (for persistence)
	•	JUnit (for testing) 
	springdoc.api-docs.path=/api-docs
    springdoc.swagger-ui.path=/swagger-ui.html
    1. url for save-> http://localhost:9091/cache/add 
    body-> {"id":"7", "data":"Bank of america"}
    
    2. get by id url-> http://localhost:9091/cache/get/1
    
    3. remove by id-> http://localhost:9091/cache/remove/3
    4. clear cache without effecting db url-> http://localhost:9091/cache/clear
    
	*
	*/
@SpringBootApplication
public class CacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacheApplication.class, args);
	}

}
