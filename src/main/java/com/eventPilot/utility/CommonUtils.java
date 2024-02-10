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

@Component
public class CommonUtils {
    public DateTimeComponent separateDateAndTime(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        return new DateTimeComponent(date, time);
    }

    public Reference getReferenceFromObject(String _id, String name) {
        return Reference.builder()._id(_id).name(name).build();
    }

    public List<String> getIdsFromReferences(List<Reference> referenceList) {
        if (referenceList == null || referenceList.size() == 0) return new ArrayList<>();
        return referenceList.stream().map(Reference::get_id).collect(Collectors.toList());
    }

    public <T> Map<String, T> getObjectMap(List<T> objectList, Function<T, String> keyExtractor) {
        if (objectList != null && !objectList.isEmpty()) {
            return objectList.stream()
                    .collect(Collectors.toMap(keyExtractor, Function.identity(), (oldValue, newValue) -> newValue));
        }
        return new HashMap<>();
    }
}
