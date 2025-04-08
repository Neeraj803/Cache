package com.example.Cache;

import java.util.LinkedHashMap;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
	private static final Logger logger = Logger.getLogger(CacheService.class.getName());
	private final int MAX_CACHE_SIZE;
	private final Map<String,EntityCache> cache;
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
    	if(entity ==null) {
    		logger.warn("Attempted to add a null entity to the cache.");
    		return;
    	}
    	try {
        cache.put(entity.getId(), entity);
    	}catch (Exception e) {
    		logger.error("Error adding entity to cache: " + entity.getId(), e);
		}
        
    }
    public EntityCache get(String id) {
    	logger.info("Id for fetching data"+id);
    	if(id == null) {
    		logger.warn("Attempted to get a null id from cache.");
    		return null;
    	}
    	try {
        return cache.computeIfAbsent(id, key ->{
        try { 
             return  databaseService.findById(key).orElse(null);
        }catch (Exception e) {
        	logger.error("Error fetching entity with  " + key + " from the database.", e);
        	return null;
		}
        
        });
        
    	}catch (Exception e) {
    		logger.error("Error getting entity from cache for id: " + id, e);
            return null;
		}
    }
    
    public void remove(String id) {
    	
    	 if (id == null) {
             logger.warn("Attempted to remove a null id from cache.");
             return;
         }
    	try {
        cache.remove(id);
        databaseService.deleteById(id);
    	}catch (Exception e) {
    		logger.error("Error removing entity with id " + id + " from cache and database.", e);
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
		}
    }

}
