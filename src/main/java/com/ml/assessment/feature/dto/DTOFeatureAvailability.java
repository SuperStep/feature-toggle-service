package com.ml.assessment.feature.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ml.assessment.feature.model.FeatureAvailability;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DTOFeatureAvailability{
    private String featureName;
    private String email;

    private Boolean enabled;

    public DTOFeatureAvailability(String email, String featureName, Boolean enabled) {
        this.email = email;
        this.featureName = featureName;
        this.enabled = enabled;
    }

    public DTOFeatureAvailability(FeatureAvailability availability) {
        this.email = availability.getEmail();
        this.featureName = availability.getFeatureName();
        this.enabled = availability.getEnabled();
    }
}
