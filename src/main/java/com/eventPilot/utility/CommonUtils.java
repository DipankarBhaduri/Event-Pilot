package com.eventPilot.utility;

import com.eventPilot.models.DateTimeComponent;
import com.eventPilot.models.Reference;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class providing common methods for handling date-time and reference operations.
 */
@Component
public class CommonUtils {

    /**
     * Separates the date and time components from a LocalDateTime object.
     *
     * @param dateTime LocalDateTime object to separate.
     * @return DateTimeComponent object containing separate date and time.
     */
    public DateTimeComponent separateDateAndTime(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        return new DateTimeComponent(date, time);
    }

    /**
     * Creates a Reference object from provided _id and name values.
     *
     * @param _id  The _id value for the Reference.
     * @param name The name value for the Reference.
     * @return Reference object created from _id and name.
     */
    public Reference getReferenceFromObject(String _id, String name) {
        return Reference.builder()._id(_id).name(name).build();
    }

    /**
     * Extracts _id values from a list of Reference objects.
     *
     * @param referenceList List of Reference objects.
     * @return List of _id values extracted from Reference objects.
     */
    public List<String> getIdsFromReferences(List<Reference> referenceList) {
        if (referenceList == null || referenceList.size() == 0) return new ArrayList<>();
        return referenceList.stream().map(Reference::get_id).collect(Collectors.toList());
    }

    /**
     * Converts a list of objects into a Map with specified key extraction function.
     *
     * @param objectList   List of objects to be converted into a Map.
     * @param keyExtractor Function to extract keys from objects.
     * @param <T>          Type of objects in the list.
     * @return Map with keys extracted from objects and corresponding objects as values.
     */
    public <T> Map<String, T> getObjectMap(List<T> objectList, Function<T, String> keyExtractor) {
        if (objectList != null && !objectList.isEmpty()) {
            return objectList.stream()
                    .collect(Collectors.toMap(keyExtractor, Function.identity(), (oldValue, newValue) -> newValue));
        }
        return new HashMap<>();
    }
}
