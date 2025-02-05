package com.epam.dzerhunou.songservice.service;

import static com.epam.dzerhunou.songservice.support.Constants.BAD_REQUEST_CSV_STRING_FORMAT_IS_INVALID_OR_EXCEEDS_LENGTH_RESTRICTIONS;
import static com.epam.dzerhunou.songservice.support.Constants.DURATION_MUST_BE_IN_THE_FORMAT_MM_SS;
import static com.epam.dzerhunou.songservice.support.Constants.METADATA_FOR_THIS_ID_ALREADY_EXISTS;
import static com.epam.dzerhunou.songservice.support.Constants.NOT_FOUND_RESOURCE_WITH_THE_SPECIFIED_ID_DOES_NOT_EXIST;
import static com.epam.dzerhunou.songservice.support.Constants.TIME_PATTERN;
import static com.epam.dzerhunou.songservice.support.Constants.YEAR_MUST_BE_IN_A_YYYY_FORMAT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.epam.dzerhunou.model.DeleteMetadata200Response;
import com.epam.dzerhunou.model.IdDto;
import com.epam.dzerhunou.model.MetadataDto;
import com.epam.dzerhunou.songservice.exception.ApplicationException;
import com.epam.dzerhunou.songservice.exception.ValidationException;
import com.epam.dzerhunou.songservice.model.Meta;
import com.epam.dzerhunou.songservice.repository.SongRepository;

@Service
public class SongService {


    public static final String VALIDATION_ERROR = "Validation error";
    private final SongRepository repository;


    @Autowired
    public SongService(SongRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<IdDto> createMetadata(Meta metadata) {
        validateMetadata(metadata);
        if (repository.isExists(metadata.getId())) {
            throw new ApplicationException(METADATA_FOR_THIS_ID_ALREADY_EXISTS, 409);
        }
        repository.create(metadata);
        return ResponseEntity.ok(new IdDto()
                .id(metadata.getId()));
    }

    public DeleteMetadata200Response delete(String idString) {
        if (idString.length() > 200) {
            throw new ApplicationException(BAD_REQUEST_CSV_STRING_FORMAT_IS_INVALID_OR_EXCEEDS_LENGTH_RESTRICTIONS, 400);
        }
        List<Integer> ids = parseStringToLongList(idString);
        List<Integer> deletedIds = repository.delete(ids);
        return new DeleteMetadata200Response()
                .ids(deletedIds);
    }

    public ResponseEntity<MetadataDto> getMetadata(Integer id) {
        if (!repository.isExists(id)) {
            throw new ApplicationException(NOT_FOUND_RESOURCE_WITH_THE_SPECIFIED_ID_DOES_NOT_EXIST, 404);
        }
        return ResponseEntity.ok(repository.getMetadata(id).dto());
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

    private void validateMetadata(Meta metadata) {
        List<String> missedFields = metadata.getEmptyField();
        if (!CollectionUtils.isEmpty(missedFields)){
            throw new ValidationException(VALIDATION_ERROR, 400,
                    Map.of("Missed fields", String.join(", ", missedFields)));
        }
        if (metadata.getYear() < 1900 && metadata.getYear() > 2099) {
            throw new ValidationException(VALIDATION_ERROR, 400,
                    Map.of("year", YEAR_MUST_BE_IN_A_YYYY_FORMAT,
                            "duration", ""));
        }
        if (!isValidTimeFormat(metadata.getDuration())) {
            throw new ValidationException(VALIDATION_ERROR, 400,
                    Map.of("year", "",
                            "duration", DURATION_MUST_BE_IN_THE_FORMAT_MM_SS));
        }
    }

    private boolean isValidTimeFormat(String time) {
        if (time == null) {
            return false;
        }
        Matcher matcher = TIME_PATTERN.matcher(time);
        return matcher.matches();
    }
}
