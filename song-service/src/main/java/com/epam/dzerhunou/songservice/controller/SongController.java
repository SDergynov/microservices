package com.epam.dzerhunou.songservice.controller;

import static com.epam.dzerhunou.songservice.support.Constants.BASE_ENDPOINT_MAPPING;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.epam.dzerhunou.controller.SongsApi;
import com.epam.dzerhunou.model.DeleteMetadata200Response;
import com.epam.dzerhunou.model.IdDto;
import com.epam.dzerhunou.model.MetadataDto;
import com.epam.dzerhunou.songservice.model.Meta;
import com.epam.dzerhunou.songservice.service.SongService;


@Controller
@RequestMapping(BASE_ENDPOINT_MAPPING)
public class SongController implements SongsApi {
    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @Override
    public ResponseEntity<IdDto> createMetadata(MetadataDto metadataDto) {
        return songService.createMetadata(new Meta(metadataDto));
    }

    @Override
    public ResponseEntity<DeleteMetadata200Response> deleteMetadata(String inputString) {
        return ResponseEntity.ok(songService.delete(inputString));
    }

    @Override
    public ResponseEntity<MetadataDto> getMetadata(Integer id) {
        return songService.getMetadata(id);
    }
}
