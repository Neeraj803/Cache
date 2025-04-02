package com.example.Cache;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/cache")
@Tag(name = "Cache API", description = "Operations related to Cache")
public class CacheController {
	
    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping("/add")
    @Operation(summary = "Add  Cache Entity ", description = "Adds an entity to the cache. Evicts least used if full")
    public String addEntity(@RequestBody EntityCache entity) {
        cacheService.add(entity);
        return "Entity added to cache!";
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get  by ID", description = "Retrieves an entity from the cache or loads from the database basis of id.")
    public EntityCache getEntity(@PathVariable String id) {
        return cacheService.get(id);
    }

    @DeleteMapping("/remove/{id}")
    @Operation(summary = "Remove by ID", description = "Removes an entity from both cache and database bases of id")
    public String removeEntity(@PathVariable String id) {
        cacheService.remove(id);
        return "Entity removed!";
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear Cache", description = "Clears the entire cache without affecting the database")
    public String clearCache() {
        cacheService.clear();
        return "Cache cleared!";
    }

    @DeleteMapping("/removeAll")
    @Operation(summary = "Remove cache and Database", description = "Removes all entities from both cache and database.")
    public String removeAll() {
        cacheService.removeAll();
        return "Cache and database cleared!";
    }

}
