package com.finale.ConferenceManagement.interfaces;

import java.nio.file.Path;

@FunctionalInterface
public interface SetTargetLocation {
    Path setTargetLocation(Path fileStorageLocation, String fileName);
}
