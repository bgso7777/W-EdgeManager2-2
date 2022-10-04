package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.ClientHWInfoResourceLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ClientHWInfoResourceLogRepository extends ElasticsearchRepository<ClientHWInfoResourceLog, String>,Document2Repository<ClientHWInfoResourceLog> {

    ClientHWInfoResourceLog findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);

}