package com.linkallcloud.um.domain.queue;

import java.io.Serializable;
import java.util.UUID;

public class LacQueue implements Serializable {
    private static final long serialVersionUID = 4202251724532453809L;

    private Long id;
    private String uuid;

    private long createTime;
    private long updateTime;

    private String traceId;

    private int type;// 业务类型

    protected int status; // 处理状态位 :1:有效可处理（active） 3：临时被占用 （locked） 4:作废  5：处理完毕 标记删除（deleted）
    private int consumeStatus;// 消费状态：1:未消费 2:消费成功 3：消费失败，等待下次消费 4：作废

    private String locker;// 占用标签

    private long lastConsumeTime;// 最后一次消费时间
    private long nextConsumeTime;// 可消费开始时间
    private int consumeCount;// 消费次数

    private String jsonData;// 数据信息 json格式

    public LacQueue() {
        super();
    }

    public LacQueue(String traceId, int type, String jsonData) {
        super();
        this.uuid = UUID.randomUUID().toString().replace("-", "");
        this.createTime = System.currentTimeMillis();
        this.status = LacQueues.QUEUE_STATUS_ACTIVE;
        this.consumeStatus = LacQueues.QUEUE_STATUS_CONSUMER_NONE;
        this.nextConsumeTime = this.createTime;

        this.traceId = traceId;
        this.type = type;
        this.jsonData = jsonData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getConsumeStatus() {
        return consumeStatus;
    }

    public void setConsumeStatus(int consumeStatus) {
        this.consumeStatus = consumeStatus;
    }

    public String getLocker() {
        return locker;
    }

    public void setLocker(String locker) {
        this.locker = locker;
    }

    public long getLastConsumeTime() {
        return lastConsumeTime;
    }

    public void setLastConsumeTime(long lastConsumeTime) {
        this.lastConsumeTime = lastConsumeTime;
    }

    public long getNextConsumeTime() {
        return nextConsumeTime;
    }

    public void setNextConsumeTime(long nextConsumeTime) {
        this.nextConsumeTime = nextConsumeTime;
    }

    public int getConsumeCount() {
        return consumeCount;
    }

    public void setConsumeCount(int consumeCount) {
        this.consumeCount = consumeCount;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

}
