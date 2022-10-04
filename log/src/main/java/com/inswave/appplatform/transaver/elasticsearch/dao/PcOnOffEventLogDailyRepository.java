package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import com.inswave.appplatform.transaver.elasticsearch.domain.PcOnOffEventLogDaily;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PcOnOffEventLogDailyRepository extends ElasticsearchRepository<PcOnOffEventLogDaily, String>, Document2Repository<PcOnOffEventLogDaily> {

    List<PcOnOffEventLogDaily> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(String from, String to);
    Page<Document2> findByTimeCurrentGreaterThanAndTimeCurrentLessThan(Long from, Long to, Pageable pageable);

}