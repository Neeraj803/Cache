package com.example.Cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

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
     void testAddWithBlankId() {
         EntityCache entity = new EntityCache("","");
         Exception exception = assertThrows(CacheOperationException.class, () -> {
             cacheService.add(entity);
         });
         assertEquals("null id not allowed to add ", exception.getMessage());
     }
     @Test
     void testAddWithNullId() {
         EntityCache entity = new EntityCache();
         Exception exception = assertThrows(CacheOperationException.class, () -> {
             cacheService.add(entity);
         });
         assertEquals("null id not allowed to add ", exception.getMessage());
     }
     
     @Test
     void testGetWithBlankId() {
         Exception exception = assertThrows(EntityNotFoundException.class, () -> {
             cacheService.get("");
         });
         assertTrue(exception.getMessage().contains("null id not present in db and cache"));
     }
     
     @Test
     void testGetWithNullId() {
         Exception exception = assertThrows(EntityNotFoundException.class, () -> {
             cacheService.get(null);
         });
         assertTrue(exception.getMessage().contains("null id not present in db and cache"));
     }
     
     @Test
     void testGetDatabaseFetchFails() {
         String id = "123";
         when(databaseService.findById(id)).thenThrow(new RuntimeException("DB fetch failed"));

         Exception exception = assertThrows(RuntimeException.class, () -> {
             cacheService.get(id);
         });
         assertEquals("Database fetch failed for id: " + id, exception.getMessage());
     }
     
     @Test
     void testRemoveWithBlankId() {
         cacheService.remove("");
         assertFalse(cacheService.cache.containsKey(""));
     }
     
     @Test
     void testRemoveWithNullId() {
         cacheService.remove(null);

         // Verify that no removal happens, since the id is null
         assertFalse(cacheService.cache.containsKey(null));
     }
     
     @Test
     void testRemoveIdNotFoundInCache() {
         String id = "nonExistentId";
         doThrow(new CacheOperationException("id not present in cache")).when(databaseService).deleteById(id);

         Exception exception = assertThrows(CacheOperationException.class, () -> {
             cacheService.remove(id);
         });
         assertEquals("id not present in cache for id: " + id, exception.getMessage());
     }
     
     @Test
     void testClearFails() {
         doThrow(new RuntimeException("Error clearing cache")).when(cacheService.cache).clear();

         Exception exception = assertThrows(RuntimeException.class, () -> {
             cacheService.clear();
         });
         assertEquals("Error clearing the cache.", exception.getMessage());
     }
     
     @Test
     void testRemoveAllFails() {
         doThrow(new RuntimeException("Error removing all entities")).when(databaseService).deleteAll();

         Exception exception = assertThrows(RuntimeException.class, () -> {
             cacheService.removeAll();
         });
         assertEquals("Error removing all entities from cache and database.", exception.getMessage());
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
