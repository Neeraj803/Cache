/**
 * Spring Data JPA repository for Entity persistence.
 */

package com.example.Cache.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Cache.Entities.EntityCache;

@Repository
public interface DatabaseService extends JpaRepository<EntityCache, String>{

}
