package com.linkallcloud.um.server.service.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.um.server.dao.queue.ILacQueueDao;

@Service
@Transactional(readOnly = true)
public class ScgcLacUqeueService extends LacQueueService {
    private static Log log = Logs.get();

    @Autowired
    private ILacQueueDao lacQueueDao;

    @Override
    protected ILacQueueDao queueDao() {
        return lacQueueDao;
    }

    @Transactional(readOnly = false)
    @Override
    public boolean doDealBusiness(Trace t, int type, String businessJsonData) {
        // if (type < 100) {
        // return bpLa.deal(t, type, businessJsonData);
        // } else if (type < 200) {
        // return businessLa.deal(t, type, businessJsonData);
        // } else if (type < 300) {
        // return busiQrcodeLa.deal(t, type, businessJsonData);
        // } else if (type < 400) {
        // return busiGcLa.deal(t, type, businessJsonData);
        // }

        log.infof("业务处理失败：Trace(%s),type(%d),Data(%s)", t.getTid(), type, businessJsonData);
        return false;
    }

}
