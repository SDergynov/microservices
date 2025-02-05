package com.epam.dzerhunou.resourceservice.service;

import static com.epam.dzerhunou.resourceservice.support.Constants.BAD_REQUEST_CSV_STRING_FORMAT_IS_INVALID_OR_EXCEEDS_LENGTH_RESTRICTIONS;
import static com.epam.dzerhunou.resourceservice.support.Constants.BAD_REQUEST_THE_PROVIDED_ID_IS_INVALID;
import static com.epam.dzerhunou.resourceservice.support.Constants.INTERNAL_SERVER_ERROR_AN_ERROR_OCCURRED_ON_THE_SERVER;
import static com.epam.dzerhunou.resourceservice.support.Constants.NOT_FOUND_RESOURCE_WITH_THE_SPECIFIED_ID_DOES_NOT_EXIST;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.epam.dzerhunou.model.DeleteResources200Response;
import com.epam.dzerhunou.model.IdDto;
import com.epam.dzerhunou.resourceservice.exception.ApplicationException;
import com.epam.dzerhunou.resourceservice.metadata.MetadataService;
import com.epam.dzerhunou.resourceservice.model.Meta;
import com.epam.dzerhunou.resourceservice.repository.ResourceRepository;

@Service
public class ResourceService {
    private final ResourceRepository repository;
    private final MetadataService metadataService;

    @Autowired
    public ResourceService(ResourceRepository repository, MetadataService metadataService) {
        this.repository = repository;
        this.metadataService = metadataService;
    }

    public ResponseEntity<IdDto> upload(Resource body) {
        try {
            Meta meta = parse(body);
            Integer id = repository.create(body.getContentAsByteArray());
            metadataService.createSongMetadata(meta.withId(id));
            return ResponseEntity.ok(new IdDto()
                    .id(id));
        } catch (IOException e) {
            throw new ApplicationException(INTERNAL_SERVER_ERROR_AN_ERROR_OCCURRED_ON_THE_SERVER, 500);
        }
    }

    public ResponseEntity<Resource> download(String idString) {
        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new ApplicationException(BAD_REQUEST_THE_PROVIDED_ID_IS_INVALID, 400);
        }
        if (id <= 0) {
            throw new ApplicationException(BAD_REQUEST_THE_PROVIDED_ID_IS_INVALID, 400);
        }
        if (!repository.isExists(id)) {
            throw new ApplicationException(NOT_FOUND_RESOURCE_WITH_THE_SPECIFIED_ID_DOES_NOT_EXIST, 404);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(repository.download(id));
    }

    public DeleteResources200Response delete(String idString) {
        if (idString.length() > 200) {
            throw new ApplicationException(BAD_REQUEST_CSV_STRING_FORMAT_IS_INVALID_OR_EXCEEDS_LENGTH_RESTRICTIONS, 400);
        }
        List<Integer> ids = parseStringToLongList(idString);
        List<Integer> deletedIds = repository.delete(ids);
        if (!CollectionUtils.isEmpty(deletedIds)) {
            metadataService.deleteSongMetadata(deletedIds);
        }
        return new DeleteResources200Response()
                .ids(deletedIds);
    }

    private List<Integer> parseStringToLongList(String input) {
        List<Integer> result = new ArrayList<>();
        String[] items = input.split(",");

        for (String item : items) {
            try {
                Integer num = Integer.parseInt(item.trim());
                result.add(num);
            } catch (NumberFormatException e) {
                throw new ApplicationException(BAD_REQUEST_CSV_STRING_FORMAT_IS_INVALID_OR_EXCEEDS_LENGTH_RESTRICTIONS, 400);
            }
        }

        return result;
    }

    private Meta parse(Resource body) {
        try (InputStream inputstream = body.getInputStream()) {
            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            ParseContext pcontext = new ParseContext();

            Mp3Parser mp3Parser = new Mp3Parser();
            mp3Parser.parse(inputstream, handler, metadata, pcontext);

            return new Meta()
                    .withName(metadata.get("title"))
                    .withArtist(metadata.get("xmpDM:artist"))
                    .withAlbum(metadata.get("xmpDM:album"))
                    .withDuration(metadata.get("xmpDM:duration"))
                    .withYear(metadata.get("xmpDM:releaseDate"));

        } catch (IOException | SAXException | TikaException e) {
            e.printStackTrace();
            return null;
        }
    }
}
