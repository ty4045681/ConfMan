package com.finale.ConferenceManagement.interfaces;

@FunctionalInterface
public interface SetStoredFileName<T> {
    T setStoredFileName(T entity, String storedFileName);
}
