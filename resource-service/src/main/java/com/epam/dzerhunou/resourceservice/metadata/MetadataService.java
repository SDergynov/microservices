package com.epam.dzerhunou.resourceservice.metadata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.epam.dzerhunou.model.DeleteResources200Response;
import com.epam.dzerhunou.model.IdDto;
import com.epam.dzerhunou.resourceservice.model.Meta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MetadataService {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public MetadataService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void createSongMetadata(Meta metadata) throws JsonProcessingException {
        String requestBodyAsJson = objectMapper.writeValueAsString(metadata);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBodyAsJson, getHttpHeaders());

        ResponseEntity<IdDto> response = restTemplate.exchange(
                "http://localhost:8072/api/v1/songs", HttpMethod.POST, httpEntity, IdDto.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException();
        }
    }

    public void deleteSongMetadata(List<Integer> ids){
        HttpEntity<Void> httpEntity = new HttpEntity<>(null, getHttpHeaders());

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString("http://localhost:8072/api/v1/songs")
                .queryParam("id", String.join(",", ids.stream().map(String::valueOf).toList()));

        ResponseEntity<DeleteResources200Response> response = restTemplate.exchange(
                builder.toUriString(), HttpMethod.DELETE, httpEntity, DeleteResources200Response.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException();
        }
    }

    public HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
