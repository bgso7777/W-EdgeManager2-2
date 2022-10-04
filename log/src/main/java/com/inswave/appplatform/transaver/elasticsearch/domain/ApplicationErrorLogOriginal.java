package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

import java.util.List;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@Getter 
@Setter
public class ApplicationErrorLogOriginal extends Document {

    private String indexName;
    public void setIndexName(@Param("indexName") String indexName) {
        if (indexName.equals("")) {
            indexName = this.getClass().getSimpleName().toLowerCase();
            this.indexName = this.getClass().getSimpleName().toLowerCase();
        } else {
            this.indexName = indexName;
        }
        super.setIndexName(indexName);
    }
    public String getIndexName() {
        return this.indexName;
    }

    // applicationError :  {
    // columns : ["channel", "level", "id", "activityId", "keywords", "levelDisplayName", "logName", "machineName", "opcode", "opcodeDisplayName", "processId", "providerId", "providerName", "qualifiers", "recordId", "relatedActivityId", "task", "taskDisplayName", "threadId", "timeCreated", "userId", "version", "message"],

    @Field(type = FieldType.Nested, name = "applicationErrorData")
    private List<ApplicationErrorLogData> applicationErrorData;

}