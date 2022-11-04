package com.ml.assessment.feature.repository;

import com.ml.assessment.feature.model.FeatureAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeatureRepository extends JpaRepository<FeatureAvailability, Long> {

    List<FeatureAvailability> findAllByEmailAndFeatureName(String email, String featureName);
}
