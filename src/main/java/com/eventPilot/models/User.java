package com.eventPilot.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "app_user")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class User extends BaseEntity {
    private String fullName;
    private String emailAddress;
    private Map<String, List<Reference>> eventsByDate;
}
