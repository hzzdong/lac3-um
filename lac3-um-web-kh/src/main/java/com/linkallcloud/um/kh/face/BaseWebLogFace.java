package com.linkallcloud.um.kh.face;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.laclog.WebBusiLog;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.manager.IWebBusiLogManager;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;

public abstract class BaseWebLogFace<T extends WebBusiLog, S extends IWebBusiLogManager<T>> {
	protected final Log log = Logs.get();

    protected Mirror<T> mirror;

    @SuppressWarnings("unchecked")
    public BaseWebLogFace() {
        super();
        try {
            mirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
        } catch (Throwable e) {
            if (log.isWarnEnabled()) {
                log.warn("!!!Fail to get TypeParams for self!", e);
            }
        }
    }

    public Class<T> getDomainClass() {
        return (null == mirror) ? null : mirror.getType();
    }

    protected abstract S manager();

    @Face(simple = true)
    @RequestMapping(value = "/fetch", method = RequestMethod.POST)
    public @ResponseBody
    Object fetchById(IdFaceRequest faceReq, Trace t, SessionUser su) {
        if (faceReq.getId() == null || Strings.isBlank(faceReq.getUuid())) {
            throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误");
        }
        T entity = doFetch(t, faceReq.getId(), faceReq.getUuid(), su);
        return convert(t, "fetch", faceReq, entity);
    }
    
    protected T doFetch(Trace t, Long id, String uuid, SessionUser su) {
        if (id == null || Strings.isBlank(uuid)) {
            throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误");
        }
        return manager().fetchByIdUuid(t, id, uuid);
    }
    
    @Face(simple = true)
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public @ResponseBody Object page(PageFaceRequest faceReq, Trace t, SessionUser su) {
        Page<T> page = new Page<>(faceReq);
        page.addRule(new Equal("orgId", su.companyId()));
        page.addRule(new Equal("orgType", su.getUserType()));
        page = doPage(t, page, su);
        return convert(t, "page", faceReq, page);
    }

    protected Page<T> doPage(Trace t, Page<T> page, SessionUser su) {
        return manager().findPage(t, page);
    }
    
    /**
     * 再次进行类型转换
     *
     * @param method
     * @param entity
     * @return
     */
    protected Object convert(Trace t, String method, FaceRequest fr, T entity) {
        return entity;
    }

    protected Object converts(Trace t, String method, FaceRequest fr, List<T> entities) {
        return entities;
    }

    protected Object convert(Trace t, String method, FaceRequest fr, Page<T> page) {
        return page;
    }

}
