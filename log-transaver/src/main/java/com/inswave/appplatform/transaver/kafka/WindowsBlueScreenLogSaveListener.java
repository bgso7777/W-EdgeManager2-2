package com.inswave.appplatform.transaver.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.log.manager.ModuleMonitoringManager;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.monitor.KafkaListenerMonitor;
import com.inswave.appplatform.log.monitor.LoggerMonitor;
import com.inswave.appplatform.transaver.saver.WindowsBlueScreenLogger;
import com.inswave.appplatform.util.DateUtil;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public final class WindowsBlueScreenLogSaveListener extends KafkaListenerMonitor {

    @Autowired
    WindowsBlueScreenLogger windowsBlueScreenLogger;

    private static final String topicName = "WindowsBlueScreenLog";
    private static final String groupId = "WindowsBlueScreenLogSaverGroup1";

    @KafkaListener(topics=topicName,groupId=groupId,autoStartup="${kafka.listener.windowsbluescreenlogsavelistener.enabled}",containerFactory="kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> consumerRecord, Consumer<String, String> consumer) {

        super.begin();
        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"begin");

        try {
            super.setTimeCurrent(DateUtil.getCurrentDateElasticsearch(Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z));
            super.setYear(DateUtil.getYear());
            super.setMonth(DateUtil.getMonth());
            super.setDay(DateUtil.getDay());
            super.setDayOfWeek(DateUtil.getDayOfWeek());
            super.setHour(DateUtil.getHour());
            super.setMinutely10(DateUtil.getMinute10());

            super.setInstanceId(Config.getInstance().getLog().getInstanceId());
            super.setTopicName(topicName);
            super.setGroupId(groupId);
            super.setPartitionId(consumerRecord.partition());
            super.setOffset(consumerRecord.offset());

            windowsBlueScreenLogger.insert("WindowsBlueScreenLog", consumerRecord.value());

            super.setProcessByte((long) consumerRecord.value().getBytes().length);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            windowsBlueScreenLogger.setJsonProcessingException(true);
        } catch (Exception e) {
            e.printStackTrace();
            windowsBlueScreenLogger.setException(true);
        } finally {
            try {
                consumer.commitSync();
            } catch (Exception e) {
                e.printStackTrace();
                windowsBlueScreenLogger.setCommitSyncException(true);
            } finally {
            }
        }
        super.end();
        JavaMelodyMonitor.printInfo( this.getClass().getSimpleName(),"end runningTime-->"+super.getRunningTime()+ " processCount->"+super.getProcessCount()+" bps-->"+super.getBps()+" bpms-->"+super.getBpms()+" processByte->"+super.getProcessByte());

        if(Config.getInstance().getLog().getIsUseModuleMonitoring()) {
            try {
                LoggerMonitor loggerMonitor = windowsBlueScreenLogger.cloneLoggerMonitor();
                loggerMonitor.setInstanceId(Config.getInstance().getLog().getInstanceId());
                loggerMonitor.setTopicName(topicName);
                loggerMonitor.setGroupId(groupId);
                ModuleMonitoringManager.getInstance().addLoggerMonitor(loggerMonitor);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Map<TopicPartition, Long> endOffsets = consumer.endOffsets(consumer.assignment());
                Long tempEndOffsets = 0L;
                for (TopicPartition topicPartition : endOffsets.keySet())
                    tempEndOffsets += endOffsets.get(topicPartition);
                super.setEndOffsets(tempEndOffsets);
                ModuleMonitoringManager.getInstance().addKafkaListenerMonitor(super.cloneKafkaListenerMonitor());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
