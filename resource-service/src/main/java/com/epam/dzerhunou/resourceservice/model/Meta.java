package com.epam.dzerhunou.resourceservice.model;

import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@NoArgsConstructor
public class Meta {
    @JsonProperty
    private Integer id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String artist;
    @JsonProperty
    private String album;
    @JsonProperty
    private String duration;
    @JsonProperty
    private String year;

    public Meta withId(Integer id) {
        this.id = id;
        return this;
    }
    public Meta withName(String name) {
        this.name = name;
        return this;
    }

    public Meta withArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public Meta withAlbum(String album) {
        this.album = album;
        return this;
    }

    public Meta withDuration(String duration) {
        this.duration = formatDuration(Double.parseDouble(duration));
        return this;
    }

    public Meta withYear(String year) {
        this.year = year;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getDuration() {
        return duration;
    }

    public String getYear() {
        return year;
    }

    private String formatDuration(double seconds) {
        // Use integer division to determine the full minutes
        int mins = (int) seconds / 60;

        // Use remainder to find out seconds
        int secs = (int) seconds % 60;

        // Format the string with leading zeroes using String.format
        return String.format("%02d:%02d", mins, secs);
    }
}
