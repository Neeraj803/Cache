package com.example.Cache;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.Cache.Entities.EntityCache;
import com.example.Cache.Excaptions.CacheOperationException;
import com.example.Cache.Excaptions.EntityNotFoundException;
import com.example.Cache.Repositories.DatabaseService;
import com.example.Cache.Services.CacheService;


public class CacheServiceTest {
	private CacheService cacheService;
    private DatabaseService databaseService;
    private Map<String, String> cache;
   
     @BeforeEach
     void setup() {
    	 databaseService =Mockito.mock(DatabaseService.class);
    	 cacheService=new CacheService(databaseService);
    	 cacheService.cache=spy(new LinkedHashMap<>());
    	 cache = new LinkedHashMap<>();
    	 
    	 
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
     void testGetWithBlankId() {
         Exception exception = assertThrows(EntityNotFoundException.class, () -> {
             cacheService.get("");
         });
         assertTrue(exception.getMessage().contains("null id not present in db and cache"));
     }
     
     @Test
     void testGetWithNullId() {
         Exception exception = assertThrows(EntityNotFoundException.class, () -> {
             cacheService.get("");
         });
         assertTrue(exception.getMessage().contains(""));
     }
     
     
     
     @Test
     void testRemoveWithBlankId() {
         cacheService.remove("");
         assertFalse(cacheService.cache.containsKey(""));
     }
     
     @Test
     void testRemoveWithNullId() {
         cacheService.remove("");

         // Verify that no removal happens, since the id is null
         assertFalse(cacheService.cache.containsKey(null));
     }
     
   
     
     
     
  
     
     
     
     @Test
     void testAddAndGet() {
    	 EntityCache ec=new EntityCache("1","JPMC");
    	 cacheService.add(ec);
    	 assertEquals("JPMC", cacheService.get("1").getData());
     }
   
     @Test
     void testRemoveAll_WhenDatabaseServiceFails_ShouldThrowCacheOperationException() {
         doThrow(new RuntimeException("DB error")).when(databaseService).deleteAll();

         CacheOperationException exception = assertThrows(
             CacheOperationException.class,
             () -> cacheService.removeAll()
         );

         
         assert(exception.getMessage().contains("not able remove all data"));
     }
     
     @Test
     void testRemoveAll_WhenCacheClearFails_ShouldThrowCacheOperationException() {
         doThrow(new RuntimeException("Cache error")).when(cacheService.cache).clear();

         CacheOperationException exception = assertThrows(
             CacheOperationException.class,
             () -> cacheService.removeAll()
         );

         assert(exception.getMessage().contains("not able remove all data"));
     }
  
     @Test
     void testRemove_DeleteByIdThrowsException_ThrowsEntityNotFoundException() {
         // Arrange
         String id = "789";
         cache.put(id, "value");
         doThrow(new RuntimeException("DB error")).when(databaseService).deleteById(id);

         // Act + Assert
         EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> cacheService.remove(id));
         assertEquals("id not present id for id : " + id, ex.getMessage());
     }

}
