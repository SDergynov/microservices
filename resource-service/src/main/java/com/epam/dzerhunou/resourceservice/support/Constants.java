package com.epam.dzerhunou.resourceservice.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    // Base mapping
    public static final String BASE_ENDPOINT_MAPPING = "${openapi.resource.base-path:/v1}";
    public static final String BAD_REQUEST_CSV_STRING_FORMAT_IS_INVALID_OR_EXCEEDS_LENGTH_RESTRICTIONS = "CSV string format is invalid or exceeds length restrictions.";
    public static final String BAD_REQUEST_THE_PROVIDED_ID_IS_INVALID = "The provided ID is invalid (e.g., contains letters, decimals, is negative, or zero).";
    public static final String NOT_FOUND_RESOURCE_WITH_THE_SPECIFIED_ID_DOES_NOT_EXIST = "Resource with the specified ID does not exist.";
    public static final String INTERNAL_SERVER_ERROR_AN_ERROR_OCCURRED_ON_THE_SERVER = "An error occurred on the server.";
}
