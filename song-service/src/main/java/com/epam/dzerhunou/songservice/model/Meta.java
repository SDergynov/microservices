package com.epam.dzerhunou.songservice.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.epam.dzerhunou.model.MetadataDto;

public class Meta {
    private Integer id;
    private String name;
    private String artist;
    private String album;
    private String duration;
    private Integer year;

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
        this.duration = duration;
        return this;
    }

    public Meta withYear(Integer year) {
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

    public Meta() {
    }

    public String getAlbum() {
        return album;
    }

    public String getDuration() {
        return duration;
    }

    public Integer getYear() {
        return year;
    }

    public Meta(MetadataDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.artist = dto.getArtist();
        this.album = dto.getAlbum();
        this.duration = dto.getDuration();
        this.year = dto.getYear();
    }

    public MetadataDto dto() {
        return new MetadataDto()
                .id(id)
                .name(name)
                .artist(artist)
                .album(album)
                .duration(duration)
                .year(year);
    }

    public List<String> getEmptyField() {
        List<String> missedFields = new ArrayList<>();
        if (StringUtils.isEmpty(name)) {
            missedFields.add("name");
        }
        if (StringUtils.isEmpty(album)) {
            missedFields.add("album");
        }
        if (StringUtils.isEmpty(artist)) {
            missedFields.add("artist");
        }
        if (StringUtils.isEmpty(duration)) {
            missedFields.add("duration");
        }
        if (year == null) {
            missedFields.add("year");
        }
        return missedFields;
    }
}
