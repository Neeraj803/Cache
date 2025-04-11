package com.example.Cache.Services;



import java.util.LinkedHashMap;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.example.Cache.Entities.EntityCache;
import com.example.Cache.Excaptions.CacheOperationException;
import com.example.Cache.Excaptions.DuplicateEntryException;
import com.example.Cache.Excaptions.EntityNotFoundException;
import com.example.Cache.Repositories.DatabaseService;

@Service
public class CacheService {
	private static final Logger logger = Logger.getLogger(CacheService.class.getName());
	private final int MAX_CACHE_SIZE;
	public Map<String,EntityCache> cache;
	private final DatabaseService  databaseService;
    public CacheService(DatabaseService databaseService) {
    	
        this.MAX_CACHE_SIZE = 5; // Can be configured dynamically
        this.databaseService = databaseService;
        this.cache = new LinkedHashMap<>(MAX_CACHE_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, EntityCache> eldest) {
                if (size() > MAX_CACHE_SIZE) {
                	try {
                    databaseService.save(eldest.getValue()); // Persist least used entity
                    return true;
                	}catch (Exception e) {
                		logger.error("Error saving least used entity to database: " + eldest.getValue(), e);
					}
                }
                return false;
            }
        };
    }
    public void add(EntityCache entity) {
    	logger.info("entity -> "+entity);
    	if(entity.getId().isBlank()) {
    		logger.warn("Attempted to add a null entity to the cache.");
    		throw new CacheOperationException("null id not allowed to add ");
    		
    	}
    	try {
    		if(cache.containsKey(entity.getId())) {
    			throw new DuplicateEntryException("duplicate entry not allowed");
    		}
        cache.put(entity.getId(), entity);
    	}catch (Exception e) {
    		logger.error("Error adding entity to cache: " + entity.getId(), e);
    		
		}
        
    }
    public EntityCache get(String id) {
    	logger.info("Id for fetching data"+id);
    	if(id.isBlank()) {
    		logger.warn("Attempted to get a null id from cache.");
    		throw new EntityNotFoundException("null id not present in db and cache" + id);
    	}
    	try {
        return cache.computeIfAbsent(id, key ->{
        try { 
        	return databaseService.findById(key).orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + key));
        }catch (Exception e) {
        	logger.error("Error fetching entity with  " + key + " from the database.", e);
        	 throw new EntityNotFoundException("Database fetch failed for id: " + id);
        
		}
        
        });
        
    	}catch (Exception e) {
    		logger.error("Error getting entity from cache for id: " + id, e);
    		throw new CacheOperationException("Cache lookup failed for id: " + id);
		}
    }
    

    
    public void clear() {
    	try {
        cache.clear();
    	}catch (Exception e) {
    		logger.error("Error clearing the cache.", e);
    		
		}
    }
    public void removeAll() {
    	try {
        cache.clear();
        databaseService.deleteAll();
    	}catch (Exception e) {
    		logger.error("Error removing all entities from cache and database.", e);
    		throw new CacheOperationException("not able remove all data from cache and database");
    		
		}
    }
    public void remove(String id) {
    	
   	 if (id.isBlank()) {
            logger.warn("Attempted to remove a null id from cache.");
            return;
        }
   	try {
   		if(cache.containsKey(id)) {
       cache.remove(id);
   		}else {
   			logger.info("ID not found in cache: " + id);
   			throw new CacheOperationException("id not present in cache for id: "+id);
   		}
       databaseService.deleteById(id);
   	}catch (Exception e) {
   		logger.error("Error removing entity with id " + id + " from cache and database.", e);
   		throw new EntityNotFoundException("id not present id for id : "+id);
   		
		}
   }
	
}
