package com.collectibles.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for JSON operations.
 * Provides methods for reading JSON files from resources and
 * converting JSON strings to Java objects using Gson.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class JsonUtil {

    // Single Gson instance for the entire application (thread-safe)
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * Gets the shared Gson instance.
     *
     * @return Configured Gson instance
     */
    public static Gson getGson() {
        return gson;
    }

    /**
     * Reads a JSON file from the resources folder and converts it to a list of objects.
     *
     * @param <T> The type of objects in the list
     * @param resourcePath Path to the JSON file in resources (e.g., "data/items.json")
     * @param typeToken TypeToken for the list type
     * @return List of objects parsed from the JSON file
     * @throws RuntimeException if the file cannot be read or parsed
     */
    public static <T> List<T> loadListFromJson(String resourcePath, TypeToken<List<T>> typeToken) {
        try {
            // Get the file from resources as an input stream
            InputStream inputStream = JsonUtil.class.getClassLoader()
                    .getResourceAsStream(resourcePath);

            // Check if file exists
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }

            // Create a reader with UTF-8 encoding
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

            // Parse JSON to List using TypeToken
            Type type = typeToken.getType();
            List<T> list = gson.fromJson(reader, type);

            // Close the reader
            reader.close();

            // Return the parsed list or empty list if null
            return list != null ? list : new ArrayList<>();

        } catch (Exception e) {
            throw new RuntimeException("Error loading JSON from " + resourcePath + ": " + e.getMessage(), e);
        }
    }

    /**
     * Converts a Java object to a JSON string.
     *
     * @param object The object to convert
     * @return JSON string representation of the object
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * Converts a JSON string to a Java object.
     *
     * @param <T> The type of object to create
     * @param json The JSON string
     * @param classOfT The class of the object to create
     * @return Object parsed from the JSON string
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}