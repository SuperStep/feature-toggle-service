package com.ml.assessment.feature.controller;

import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.ml.assessment.feature.dto.DTOFeatureAvailability;
import com.ml.assessment.feature.model.FeatureAvailability;
import com.ml.assessment.feature.repository.FeatureRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class FeatureToggleController {
    private final FeatureRepository featureRepository;
    private final ObjectMapper mapper;

    public FeatureToggleController(FeatureRepository featureRepository, ObjectMapper mapper) {
        this.featureRepository = featureRepository;
        this.mapper = mapper;
    }


    @GetMapping("/feature")
    public ResponseEntity<String> checkFeature(@RequestAttribute String email, @RequestAttribute String featureName) throws JsonProcessingException {

        List<FeatureAvailability> featureAvailabilities
                = featureRepository.findAllByEmailAndFeatureName(email, featureName);

        if(featureAvailabilities.size() > 0){
            DTOFeatureAvailability availability = new DTOFeatureAvailability( featureAvailabilities.get(0));

            String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    new HashMap<String, Object>(){{put("canAccess", availability.getEnabled());}});

            return new ResponseEntity<>(jsonResult, HttpStatus.OK);
        }
        else{
            String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    new HashMap<String, Object>(){{put("canAccess", false);}});
            return new ResponseEntity<>(jsonResult, HttpStatus.OK);
        }

    }

    @PostMapping("/feature")
    public ResponseEntity<String> toggleFeature(@RequestBody DTOFeatureAvailability availability){

        List<FeatureAvailability> featureAvailabilities
                = featureRepository.findAllByEmailAndFeatureName(availability.getEmail(), availability.getFeatureName());

        if(featureAvailabilities.isEmpty()){

            FeatureAvailability availabilityRecord = new FeatureAvailability(availability);
            featureRepository.save(availabilityRecord);

            return new ResponseEntity<>(HttpStatus.OK);

        } else {

            FeatureAvailability currentAvailabilityRecord = featureAvailabilities.get(0);
            if(currentAvailabilityRecord.getEnabled() == availability.getEnabled())
                return new ResponseEntity<>(HttpStatus.FOUND);
            else{
                featureRepository.save(new FeatureAvailability(availability));
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
    }
}
