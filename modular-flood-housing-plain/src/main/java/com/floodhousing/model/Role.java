package com.floodhousing.model;

/** User roles defined in the system proposal. */
public enum Role {
    ADMIN("Admin"),
    STAFF("Barangay Staff"),
    RESIDENT("Resident");

    private final String label;

    Role(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
