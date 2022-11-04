package com.ml.assessment.feature.model;

import com.ml.assessment.feature.dto.DTOFeatureAvailability;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;

@Entity
@NoArgsConstructor
public class FeatureAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String featureName;
    @Email
    private String email;
    private Boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public FeatureAvailability(DTOFeatureAvailability availability){
        this.email = availability.getEmail();
        this.featureName = availability.getFeatureName();
        this.enabled = availability.getEnabled();
    }


}
