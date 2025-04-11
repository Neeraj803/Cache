package com.example.Cache.Excaptions;

public class EntityNotFoundException extends RuntimeException{
	public EntityNotFoundException(String message) {
        super(message);
}
}