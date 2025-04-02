/**
 * Spring Data JPA repository for Entity persistence.
 */

package com.example.Cache;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseService extends JpaRepository<EntityCache, String>{

}
