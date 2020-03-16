package com.linkallcloud.um.server.dao.queue;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.query.Query;
import com.linkallcloud.um.domain.queue.LacQueue;

public interface ILacQueueDao {
    

    public int insert(@Param("entity") LacQueue queue);

    public int update(@Param("entity") LacQueue queue);

    /**
     * 将status为srcStatus的更新为destStatus，设置locker，先锁定消息
     * 
     * @param destStatus
     * @param srcStatus
     * @param count
     * @param locker
     * @return
     */
    public int updateActiveQueue(@Param("destStatus") int destStatus, @Param("srcStatus") int srcStatus,
            @Param("count") int count, @Param("locker") String locker);
    
    public void updateQueueStatus(@Param("id") Long id, @Param("status") int status);

    public List<LacQueue> getByLocker(@Param("locker") String locker);

    public List<LacQueue> find(@Param("query") Query query);

    

}
