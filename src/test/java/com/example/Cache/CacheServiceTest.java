package com.example.Cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CacheServiceTest {
	private CacheService cacheService;
    private DatabaseService databaseService;
     @BeforeEach
     void setup() {
    	 databaseService =Mockito.mock(DatabaseService.class);
    	 cacheService=new CacheService(databaseService);
    	 
     }
     @Test
     void testAddAndGet() {
    	 EntityCache ec=new EntityCache("1","JPMC");
    	 cacheService.add(ec);
    	 assertEquals("JPMC", cacheService.get("1").getData());
     }
     @Test
     void testEviction() {
         for (int i = 1; i <= 6; i++) {
             cacheService.add(new EntityCache(String.valueOf(i), "JPMC" + i));
         }
         
         assertNull(cacheService.get("1")); // Should be evicted
     }
}
