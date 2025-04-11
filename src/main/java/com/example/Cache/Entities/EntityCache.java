/**
 * Entity class representing the objects stored in cache.
 */

package com.example.Cache.Entities;





import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@jakarta.persistence.Table(name = "entity_data")
public class EntityCache {
	@Id
private String id;
private String data;
public EntityCache() {
}
public EntityCache(String id, String data) {
	this.id = id;
	this.data = data;
}
public String getId() {
	return id;
}

public String getData() {
	return data;
}
public void setData(String data) {
	this.data = data;
}





}
