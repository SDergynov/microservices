package com.epam.dzerhunou.resourceservice.repository;

import static com.epam.dzerhunou.repository.Tables.RESOURCE_;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.max;

import java.util.List;
import java.util.Objects;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class ResourceRepository {
    private final DSLContext dsl;

    @Autowired
    public ResourceRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Integer create(byte[] fileData) {
        return Objects.requireNonNull(dsl.insertInto(RESOURCE_)
                        .set(RESOURCE_.ID, (
                                dsl.select(coalesce(max(RESOURCE_.ID), 0))
                                        .from(RESOURCE_)
                                        .fetchOneInto(Integer.class)) + 1)
                        .set(RESOURCE_.FILE_DATA, fileData)
                        .returningResult(RESOURCE_.ID)
                        .fetchOne())
                .getValue(RESOURCE_.ID);
    }

    public Resource download(Integer id) {
        byte[] fileData = dsl.select(RESOURCE_.FILE_DATA)
                .from(RESOURCE_)
                .where(RESOURCE_.ID.eq(id))
                .fetchOne(0, byte[].class);
        return fileData == null ? null : new ByteArrayResource(fileData);
    }

    public List<Integer> delete(List<Integer> ids) {
        List<Integer> existingIds = dsl.select(RESOURCE_.ID)
                .from(RESOURCE_)
                .where(RESOURCE_.ID.in(ids))
                .fetch(RESOURCE_.ID);

        if (!existingIds.isEmpty()) {
            dsl.delete(RESOURCE_)
                    .where(RESOURCE_.ID.in(existingIds))
                    .execute();
        }
        return existingIds;
    }

    public boolean isExists(Integer id) {
        return dsl.fetchExists(dsl.selectFrom(RESOURCE_)
                .where(RESOURCE_.ID.eq(id)));
    }
}
