package com.epam.dzerhunou.resourceservice.controller;

import static com.epam.dzerhunou.resourceservice.support.Constants.BASE_ENDPOINT_MAPPING;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.epam.dzerhunou.controller.ResourcesApi;
import com.epam.dzerhunou.model.DeleteResources200Response;
import com.epam.dzerhunou.model.IdDto;
import com.epam.dzerhunou.resourceservice.service.ResourceService;

@Controller
@RequestMapping(BASE_ENDPOINT_MAPPING)
public class ResourceController implements ResourcesApi {
    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    public ResponseEntity<IdDto> uploadFile(Resource body) {
        return resourceService.upload(body);
    }

    @Override
    public ResponseEntity<DeleteResources200Response> deleteResources(String inputString) {
        return ResponseEntity.ok(resourceService.delete(inputString));
    }

    @Override
    public ResponseEntity<Resource> getResource(String id) {
        return resourceService.download(id);
    }
}
