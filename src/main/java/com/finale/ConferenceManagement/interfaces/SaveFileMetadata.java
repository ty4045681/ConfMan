package com.finale.ConferenceManagement.interfaces;

@FunctionalInterface
public interface SaveFileMetadata<T> {
    T save(T entity);
}
