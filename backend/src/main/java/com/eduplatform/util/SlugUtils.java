package com.eduplatform.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Utility class for generating URL-friendly slugs.
 */
public final class SlugUtils {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGES_DASHES = Pattern.compile("(^-|-$)");
    private static final Pattern MULTIPLE_DASHES = Pattern.compile("-+");

    private SlugUtils() {
        // Prevent instantiation
    }

    /**
     * Generates a URL-friendly slug from a given input string.
     * @param input The string to convert.
     * @return A clean, lowercase, hyphenated slug.
     */
    public static String generateSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        // Normalize unicode characters to their basic Latin equivalents
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        
        // Replace whitespace with a single dash
        String slug = WHITESPACE.matcher(normalized).replaceAll("-");
        
        // Remove all non-word characters except for dashes
        slug = NON_LATIN.matcher(slug).replaceAll("");
        
        // Replace multiple consecutive dashes with a single dash
        slug = MULTIPLE_DASHES.matcher(slug).replaceAll("-");
        
        // Remove leading or trailing dashes
        slug = EDGES_DASHES.matcher(slug).replaceAll("");
        
        return slug.toLowerCase(Locale.ENGLISH);
    }

    /**
     * Generates a unique slug by appending a counter if the base slug already exists.
     * @param baseSlug The desired base slug.
     * @param existsChecker A function that checks if a given slug already exists (e.g., a repository method).
     * @return A unique slug.
     */
    public static String generateUniqueSlug(String baseSlug, Function<String, Boolean> existsChecker) {
        if (!existsChecker.apply(baseSlug)) {
            return baseSlug;
        }
        
        String slug;
        int counter = 2;
        do {
            slug = baseSlug + "-" + counter;
            counter++;
        } while (existsChecker.apply(slug));
        
        return slug;
    }
}