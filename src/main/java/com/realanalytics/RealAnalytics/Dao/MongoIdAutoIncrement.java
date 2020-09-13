package com.realanalytics.RealAnalytics.Dao;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.realanalytics.RealAnalytics.Data.DocSequence;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;


@Service
public class MongoIdAutoIncrement {

    private MongoOperations mongoOperations;

    @Autowired
    public MongoIdAutoIncrement(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public long generateSequence(String seqName) {
        DocSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DocSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;

    }
}
