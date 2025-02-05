package com.epam.dzerhunou.songservice.repository;

import static com.epam.dzerhunou.repository.Tables.METADATA;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.dzerhunou.songservice.model.Meta;

@Repository
public class SongRepository {
    private final DSLContext dsl;

    @Autowired
    public SongRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void create(Meta metadata) {
        dsl.insertInto(METADATA)
                .set(METADATA.ID, metadata.getId())
                .set(METADATA.NAME, metadata.getName())
                .set(METADATA.ARTIST, metadata.getArtist())
                .set(METADATA.ALBUM, metadata.getAlbum())
                .set(METADATA.DURATION, metadata.getDuration())
                .set(METADATA.YEAR, metadata.getYear())
                .execute();
    }

    public Meta getMetadata(Integer id) {
        return dsl.selectFrom(METADATA)
                .where(METADATA.ID.eq(id))
                .fetchOne(r -> new Meta()
                        .withId(r.getId())
                        .withName(r.getName())
                        .withArtist(r.getArtist())
                        .withAlbum(r.getAlbum())
                        .withDuration(r.getDuration())
                        .withYear(r.getYear()));
    }

    public List<Integer> delete(List<Integer> ids) {
        List<Integer> existingIds = dsl.select(METADATA.ID)
                .from(METADATA)
                .where(METADATA.ID.in(ids))
                .fetch(METADATA.ID);

        if (!existingIds.isEmpty()) {
            dsl.delete(METADATA)
                    .where(METADATA.ID.in(existingIds))
                    .execute();
        }
        return existingIds;
    }

    public boolean isExists(Integer id) {
        return dsl.fetchExists(dsl.selectFrom(METADATA)
                .where(METADATA.ID.eq(id)));
    }
}
