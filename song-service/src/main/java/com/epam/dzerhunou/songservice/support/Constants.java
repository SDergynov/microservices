package com.epam.dzerhunou.songservice.support;

import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    // Base mapping
    public static final String BASE_ENDPOINT_MAPPING = "${openapi.song.base-path:/v1}";

    public static final Pattern TIME_PATTERN = Pattern.compile("\\b([0-5]?[0-9]):([0-5][0-9])\\b");

    public static final String BAD_REQUEST_CSV_STRING_FORMAT_IS_INVALID_OR_EXCEEDS_LENGTH_RESTRICTIONS = "CSV string format is invalid or exceeds length restrictions.";
    public static final String BAD_REQUEST_THE_PROVIDED_ID_IS_INVALID = "The provided ID is invalid (e.g., contains letters, decimals, is negative, or zero).";
    public static final String NOT_FOUND_RESOURCE_WITH_THE_SPECIFIED_ID_DOES_NOT_EXIST = "Resource with the specified ID does not exist.";
    public static final String INTERNAL_SERVER_ERROR_AN_ERROR_OCCURRED_ON_THE_SERVER = "An error occurred on the server.";
    public static final String DURATION_MUST_BE_IN_THE_FORMAT_MM_SS = "Duration must be in the format MM:SS";
    public static final String YEAR_MUST_BE_IN_A_YYYY_FORMAT = "Year must be in a YYYY format";
    public static final String METADATA_FOR_THIS_ID_ALREADY_EXISTS = "Metadata for this ID already exists";

}
