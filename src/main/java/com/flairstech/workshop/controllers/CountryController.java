package com.flairstech.workshop.controllers;

import com.flairstech.workshop.repositories.CountryLanguageRepository;
import com.flairstech.workshop.repositories.CountryRepository;
import com.flairstech.workshop.entities.Country;
import com.flairstech.workshop.entities.CountryLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class CountryController {
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    CountryLanguageRepository countryLanguageRepository;

    private static final ResponseEntity<Object> INVALID_COUNTRY_CODE_JSON =
            new ResponseEntity<>(
                    new LinkedHashMap<String, Object>() {{
                        put("error message", "INVALID_COUNTRY_CODE");
                    }},
    HttpStatus.INTERNAL_SERVER_ERROR);

    @GetMapping(value = "/{code}")
    public @ResponseBody ResponseEntity<Object> getCountry(@PathVariable String code) {
        Country country = countryRepository.findByCode(code);
        CountryLanguage countryLanguage = countryLanguageRepository.findByCountryCode(code);

        Map<String, Object> JSON = convertToJSON(country, countryLanguage);
        if (JSON==null) {
            return INVALID_COUNTRY_CODE_JSON;
        }
        return new ResponseEntity<>(JSON, HttpStatus.OK);
    }

    private Map<String, Object> convertToJSON(Country country, CountryLanguage countryLanguage) {
        Map<String, Object> countryJSON = new LinkedHashMap<>();

        try {
            countryJSON.put("name", country.getName());
            countryJSON.put("continent", country.getContinent());
            countryJSON.put("population", country.getPopulation());
            countryJSON.put("life_expectancy", country.getLifeExpectancy());
            countryJSON.put("country_language", countryLanguage.getLanguage());
        } catch (NullPointerException e) {
            return null;
        }

        return countryJSON;
    }
}
