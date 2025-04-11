package com.example.Cache.Controllers;



import java.util.logging.Logger;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Cache.Entities.EntityCache;
import com.example.Cache.Excaptions.CacheOperationException;
import com.example.Cache.Excaptions.EntityNotFoundException;
import com.example.Cache.Services.CacheService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/cache")
@Tag(name = "Cache API", description = "Operations related to Cache")
public class CacheController {
	private static final Logger logger = Logger.getLogger(CacheController.class.getName());
    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping("/add")
    @Operation(summary = "Add  Cache Entity ", description = "Adds an entity to the cache. Evicts least used if full")
    public String addEntity(@RequestBody EntityCache entity) {
       logger.info("id--> "+entity.getId() + "data"+ entity.getData());
    	try {
    	cacheService.add(entity);
        return "Entity added to cache!";
    	}catch (Exception e) {
    		throw new CacheOperationException("Error while adding entity to cache: " + e.getMessage());
		}
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get  by ID", description = "Retrieves an entity from the cache or loads from the database basis of id.")
    public EntityCache getEntity(@PathVariable String id) {
       try {
    	  logger.info("id->> "+id);
    	return cacheService.get(id);
       }catch (EntityNotFoundException e) {
    	   throw new EntityNotFoundException("Entity with id " + id + " not found in cache or database.");
	}catch (Exception e) {
		throw new CacheOperationException("Error retrieving entity with id " + id + ": " + e.getMessage());
	}
    }

    @DeleteMapping("/remove/{id}")
    @Operation(summary = "Remove by ID", description = "Removes an entity from both cache and database bases of id")
    public String removeEntity(@PathVariable String id) {
        logger.info("id--> " +id);
    	try {
    	cacheService.remove(id);
        return "Entity removed!";
    	}catch (EntityNotFoundException e) {
    		 throw new EntityNotFoundException("Entity with id " + id + " not found in cache or database.");
		}catch (Exception e) {
			throw new CacheOperationException("Error removing entity with id " + id + ": " + e.getMessage());
		}
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear Cache", description = "Clears the entire cache without affecting the database")
    public String clearCache() {
        try {
    	cacheService.clear();
        return "Cache cleared!";
        }catch (Exception e) {
        	 throw new CacheOperationException("Error clearing cache: " + e.getMessage());
		}
    }

    @DeleteMapping("/removeAll")
    @Operation(summary = "Remove cache and Database", description = "Removes all entities from both cache and database.")
    public String removeAll() {
       
    	try {
    	cacheService.removeAll();
        return "Cache and database cleared!";
    	}catch (Exception e) {
    		throw new CacheOperationException("Error clearing cache and database: " + e.getMessage());
		}
    }

}
