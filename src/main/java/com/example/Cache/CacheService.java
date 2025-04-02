package com.example.Cache;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CacheService {
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
                    databaseService.save(eldest.getValue()); // Persist least used entity
                    return true;
                }
                return false;
            }
        };
    }
    public void add(EntityCache entity) {
        cache.put(entity.getId(), entity);
        System.out.print(entity.getData());
    }
    public EntityCache get(String id) {
        return cache.computeIfAbsent(id, key -> databaseService.findById(key).orElse(null));
    }
    
    public void remove(String id) {
        cache.remove(id);
        databaseService.deleteById(id);
    }
    
    public void clear() {
        cache.clear();
    }
    public void removeAll() {
        cache.clear();
        databaseService.deleteAll();
    }

}
