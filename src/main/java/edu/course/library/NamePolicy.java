package edu.course.library;

public class NamePolicy {
    public String normalize(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("Name is required");
        }
        String Name = raw.strip();
        if (Name.isEmpty() || Name.length() > 30) {
            throw new IllegalArgumentException("Name length must be between 1 and 30");
        }
        return Name;
    }
}