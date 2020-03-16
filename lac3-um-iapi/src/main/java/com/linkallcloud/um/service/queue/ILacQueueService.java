package com.linkallcloud.um.service.queue;

import com.linkallcloud.um.domain.queue.LacQueue;

import java.util.List;

public interface ILacQueueService {

    /**
     * 生成队列
     * 
     * @param traceId
     * @param type
     * @param jsonData
     * @return
     */
    public int product(String traceId, int type, String jsonData);

    /**
     * 业务处理
     */
    public void dealBusiness();

    /**
     * 批量获取 可以消费的消息 先使用一个时间戳将被消费的消息锁定，然后再使用这个时间戳去查询锁定的数据。
     * 
     * @param count
     * @return
     */
    public List<LacQueue> findActiveQueueNew(int count);

    /**
     * 验证队列modle 的合法性
     * 
     * @param model
     * @return boolean true，消息还可以消费。false,消息不允许消费。
     */
    public boolean validateQueue(final LacQueue model);

    /**
     * 消息处理完毕之后，根据消费结果修改数据库中的状态
     * 
     * @param queueId
     * @param isDelete
     * @param consumeMinTime
     * @param tradeNo
     * @param consumeCount
     */
    public void consume(Long queueId, boolean isDelete, Long consumeMinTime, String tradeNo, int consumeCount);

    /**
     * 批量获取,修复被锁定的消息
     * 
     * @param count
     * @return
     */
    public void repairQueueByStatus(int status);

}
