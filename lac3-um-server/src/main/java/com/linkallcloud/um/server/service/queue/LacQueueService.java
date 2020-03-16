package com.linkallcloud.um.server.service.queue;

import java.util.List;

import com.linkallcloud.um.service.queue.ILacQueueService;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.queue.LacQueue;
import com.linkallcloud.um.domain.queue.LacQueues;
import com.linkallcloud.um.server.dao.queue.ILacQueueDao;
import com.linkallcloud.core.util.Utils;

public abstract class LacQueueService implements ILacQueueService {
    private static Log log = Logs.get();

    protected abstract ILacQueueDao queueDao();

    public abstract boolean doDealBusiness(Trace t, int type, String businessJsonData);

    @Override
    public int product(String traceId, int type, String jsonData) {
        LacQueue queue = new LacQueue(traceId, type, jsonData);
        return queueDao().insert(queue);
    }

    @Override
    public void dealBusiness() {
        List<LacQueue> list = findActiveQueueNew(5);
        if (list != null && list.size() > 0) {
            list.forEach(lacQueue -> {
                Trace t = new Trace(true);
                if (validateQueue(lacQueue)) {
                    // 处理业务逻辑
                    boolean success = doDealBusiness(t, lacQueue.getType(), lacQueue.getJsonData());
                    if (success) {
                        consume(lacQueue.getId(), true, null, t.getTid(), lacQueue.getConsumeCount() + 1);
                    } else {
                        consume(lacQueue.getId(), false, null, t.getTid(), lacQueue.getConsumeCount() + 1);
                    }
                } else {
                    consume(lacQueue.getId(), false, null, t.getTid(), lacQueue.getConsumeCount() + 1);
                }
            });
        } else {
            log.info("***** 当前队列中没有可处理的任务");
        }
    }

    @Transactional(readOnly = false)
    @Override
    public List<LacQueue> findActiveQueueNew(int count) {
        // 先去更新数据
        String locker = String.valueOf(System.currentTimeMillis()) + Utils.getNumericRandomID(10);
        int lockCount = 0;
        try {
            // 将status为1的更新为3，设置locker，先锁定消息
            lockCount = queueDao().updateActiveQueue(LacQueues.QUEUE_STATUS_LOCKED, LacQueues.QUEUE_STATUS_ACTIVE,
                    count, locker);
        } catch (Exception e) {
            log.error("QueueDomainRepository.findActiveQueueNew error occured!" + e.getMessage(), e);
            throw new BaseRuntimeException(LacQueues.SERVICE_DATABASE_FALIURE,
                    "QueueDomainRepository.findActiveQueue error occured!", e);
        }

        // 如果锁定的数量为0，则无需再去查询
        if (lockCount == 0) {
            return null;
        }

        // 休息一会在再询，防止数据已经被更改
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            log.error("QueueDomainRepository.findActiveQueue error sleep occured!" + e.getMessage(), e);
        }
        List<LacQueue> activeList = null;
        try {
            activeList = queueDao().getByLocker(locker);
        } catch (Exception e) {
            log.error("QueueDomainRepository.findActiveQueue error occured!" + e.getMessage(), e);
            throw new BaseRuntimeException(LacQueues.SERVICE_DATABASE_FALIURE,
                    "QueueDomainRepository.findActiveQueue error occured!", e);
        }
        return activeList;
    }

    @Override
    public boolean validateQueue(LacQueue model) {
        int consumeCount = model.getConsumeCount();
        if (consumeCount >= LacQueues.QUEUE_MAX_CONSUME_COUNT) {
            // 消费次数超过了最大次数
            return false;
        }
        int consumeStatus = model.getConsumeStatus();
        if (consumeStatus == LacQueues.QUEUE_STATUS_CONSUMER_SUCCESS) {
            // 消息已经被成功消费
            return false;
        }
        if (model.getStatus() != LacQueues.QUEUE_STATUS_LOCKED) {
            // 消息状态不正确
            return false;
        }
        String jsonData = model.getJsonData();
        if (Strings.isBlank(jsonData)) {
            // 消息体为空
            return false;
        }
        return true;
    }

    @Transactional(readOnly = false)
    @Override
    public void consume(Long queueId, boolean isDelete, Long consumeMinTime, String traceId, int consumeCount) {
        LacQueue queue = new LacQueue();
        if (!isDelete) {
            // 已经到了最大消费次数，消息作废 不再处理
            if (consumeCount >= LacQueues.QUEUE_MAX_CONSUME_COUNT) {
                // 达到最大消费次数的也设置为消费成功
                queue.setConsumeStatus(LacQueues.QUEUE_STATUS_CONSUMER_SUCCESS);
                queue.setStatus(LacQueues.QUEUE_STATUS_CANCEL);
            } else {
                queue.setConsumeStatus(LacQueues.QUEUE_STATUS_CONSUMER_FAILED);
                // 设置为可用状态等待下次继续发送
                queue.setStatus(LacQueues.QUEUE_STATUS_ACTIVE);
            }
        } else {
            // 第三方消费成功
            queue.setConsumeStatus(LacQueues.QUEUE_STATUS_CONSUMER_SUCCESS);
            queue.setStatus(LacQueues.QUEUE_STATUS_DELETED);
        }
        queue.setNextConsumeTime(consumeMinTime == null ? LacQueues.getNextConsumeTime(consumeCount) : consumeMinTime);
        if (!Strings.isBlank(traceId)) {
            queue.setTraceId(traceId);
        }
        long now = System.currentTimeMillis();
        queue.setUpdateTime(now);
        queue.setLastConsumeTime(now);
        queue.setConsumeCount(consumeCount);
        queue.setId(queueId);
        queueDao().update(queue);
    }

    @Transactional(readOnly = false)
    @Override
    public void repairQueueByStatus(int status) {
        List<LacQueue> activeList = null;
        try {
            Query query = new Query();
            query.addRule(new Equal("status", status));
            // 下次消费时间在当前时间3小时以内的消息
            query.addRule(new Equal("nextConsumeTime", System.currentTimeMillis() + 3 * 60 * 1000));
            activeList = queueDao().find(query);
        } catch (Exception e) {
            log.error("QueueDomainRepository.repairQueueByStatus find error occured!" + e.getMessage(), e);
            throw new BaseRuntimeException(LacQueues.SERVICE_DATABASE_FALIURE,
                    "QueueDomainRepository.findQueueByStatus error occured!", e);
        }
        if (activeList == null || activeList.size() == 0) {
            return;
        }
        for (LacQueue temp : activeList) {
            try {
                // status=1，可被消费
                queueDao().updateQueueStatus(temp.getId(), LacQueues.QUEUE_STATUS_ACTIVE);
            } catch (Exception e) {
                log.error("QueueDomainRepository.repairQueueByStatus  update error occured!" + e.getMessage(), e);
                throw new BaseRuntimeException(LacQueues.SERVICE_DATABASE_FALIURE,
                        "QueueDomainRepository.repairQueueByStatus update error occured!", e);
            }

        }
    }

}
