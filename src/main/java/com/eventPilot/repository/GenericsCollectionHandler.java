package com.eventPilot.repository;

import com.mongodb.client.result.DeleteResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * A utility class for performing common MongoDB operations on collections using Spring Data MongoDB.
 * This class provides generic methods for inserting, retrieving, and querying documents in a MongoDB collection.
 */
@Component
public class GenericsCollectionHandler {

    private MongoTemplate mongoTemplate;

    /**
     * Constructs a new GenericsCollectionHandler with the provided MongoTemplate.
     * @param mongoTemplate The MongoTemplate instance to be used for database interactions.
     */
    public GenericsCollectionHandler (MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Inserts a document into the MongoDB collection.
     *
     * @param objectToSave The object to be saved in the collection.
     * @param <T>          The type of the object.
     * @return The saved object.
     */
    public <T> T insertData(T objectToSave) {
        return mongoTemplate.save(objectToSave);
    }

    /**
     * Retrieves all documents of a specific type from the MongoDB collection.
     *
     * @param clazz The class type of the documents.
     * @param <T>   The type of the documents.
     * @return A list of all documents of the specified type.
     */
    public <T> List<T> findAllDocument(Class<T> clazz) {
        return mongoTemplate.findAll(clazz);
    }

    /**
     * Retrieves documents from the MongoDB collection based on a specific field and its value.
     *
     * @param clazz     The class type of the documents.
     * @param fieldName The name of the field to query.
     * @param value     The value to match in the specified field.
     * @param <T>       The type of the documents.
     * @return A list of documents matching the specified field and value.
     */
    public <T> List<T> findByField(Class<T> clazz, String fieldName, Object value) {
        Query query = new Query(Criteria.where(fieldName).is(value));
        return mongoTemplate.find(query, clazz);
    }

    /**
     * Retrieves documents from the MongoDB collection based on a specific field and a list of values.
     *
     * @param clazz     The class type of the documents.
     * @param fieldName The name of the field to query.
     * @param values    The list of values to match in the specified field.
     * @param <T>       The type of the documents.
     * @return A list of documents matching the specified field and list of values.
     */
    public <T> List<T> findByFields(Class<T> clazz, String fieldName, List<String> values) {
        Query query = new Query(Criteria.where(fieldName).in(values));
        return mongoTemplate.find(query, clazz);
    }

    /**
     * Removes documents from the MongoDB collection based on a specific field and its value.
     *
     * @param clazz     The class type of the documents.
     * @param fieldName The name of the field to query.
     * @param value     The value to match in the specified field.
     * @param <T>       The type of the documents.
     */
    public <T> DeleteResult removeByField(Class<T> clazz, String fieldName, Object value) {
        Query query = new Query(Criteria.where(fieldName).is(value));
        return mongoTemplate.remove(query, clazz);
    }
}
