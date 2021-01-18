package com.linkallcloud.um.web.oapi.face.party;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.linkallcloud.um.face.account.ChangeGovCodeRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.ptj.PartTimeJob;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.face.account.SessionUserRequest;
import com.linkallcloud.um.face.account.UserAppRequest;
import com.linkallcloud.um.iapi.party.IUserManager;
import com.linkallcloud.um.iapi.ptj.IPartTimeJobManager;
import com.linkallcloud.web.face.annotation.Face;

public abstract class UserFace<T extends User, M extends IUserManager<T>, PT extends PartTimeJob, PTM extends IPartTimeJobManager<PT>> {

    protected abstract M userManager();

    protected abstract PTM ptManager();

    @Face(login = false)
    @RequestMapping(value = "/fetchById", method = RequestMethod.POST)
    public @ResponseBody Object fetchById(IdFaceRequest faceReq, Trace t) {
        if (faceReq.getId() == null) {
            return null;
        }
        T user = userManager().fetchById(t, faceReq.getId());
        user.desensitization();
        return user;
    }
    @Face(login = false)
    @RequestMapping(value = "/changeUserInfo", method = RequestMethod.POST)
    public @ResponseBody Object changeUserInfo(ObjectFaceRequest<User> req, Trace t) {
        if (req != null) {
            User user = req.getData();
            if ( user.getId()!=null && !Strings.isBlank(user.getUuid())) {
                boolean b = userManager().changeUserInfo(t, user);
                return b;
            }
        }
        return false;
    }

    @Face(login = false)
    @RequestMapping(value = "/fetchByGovCode", method = RequestMethod.POST)
    public @ResponseBody Object fetchByGovCode(ObjectFaceRequest<String> faceReq, Trace t) {
        String govCode = faceReq.getData();
        if (!Strings.isBlank(govCode)) {
            T user = userManager().fetchByGovCode(t, govCode);
            user.desensitization();
            return user;
        }
        return null;
    }

    @Face(login = false)
    @RequestMapping(value = "/changeGovCode", method = RequestMethod.POST)
    public @ResponseBody Object changeGovCode(ChangeGovCodeRequest req, Trace t) {
        if (req != null && !Strings.isBlank(req.getGovCode()) && !Strings.isBlank(req.getUuid())) {
            boolean b = userManager().changeGovCode(t, req.getUuid(), req.getGovCode());
            return b;
        }
        return false;
    }

    @Face(login = false)
    @RequestMapping(value = "/fecthByAccount", method = RequestMethod.POST)
    public @ResponseBody Object fecthByAccount(ObjectFaceRequest<String> faceReq, Trace t) {
        String a = faceReq.getData();
        if (!Strings.isBlank(a)) {
            T user = userManager().fecthByAccount(t, a);
            user.desensitization();
            return user;
        }
        return null;
    }

    @Face(login = false)
    @RequestMapping(value = "/findByMobile", method = RequestMethod.POST)
    public @ResponseBody Object findByMobile(ObjectFaceRequest<String> faceReq, Trace t) {
        String a = faceReq.getData();
        if (!Strings.isBlank(a)) {
            List<T> users = userManager().findByMobile(t, a);
            desensitization(users);
            return users;
        }
        return null;
    }

    @Face(login = false)
    @RequestMapping(value = "/assembleSessionUser", method = RequestMethod.POST)
    public @ResponseBody Object assembleSessionUser(SessionUserRequest faceReq, Trace t) {
        if (Strings.isBlank(faceReq.getAccount()) || Strings.isBlank(faceReq.getAppCode())) {
            throw new ArgException("Arg", "Account和AppCode都不能为空");
        }

        Sid loginOrg = null;
        if (faceReq.getOrgId() != null && !Strings.isBlank(faceReq.getOrgType())) {
            loginOrg = new Sid(faceReq.getOrgId(), null, faceReq.getOrgType(), null);
        }
        return userManager().assembleSessionUser(t, faceReq.getAccount(), faceReq.getAppCode(), loginOrg);
    }

    @Face(login = false)
    @RequestMapping(value = "/getUserAppMenu", method = RequestMethod.POST)
    public @ResponseBody Object getUserAppMenu(UserAppRequest faceReq, Trace t) {
        if (faceReq.getUserId() != null && faceReq.getAppId() != null) {
            Tree root =
                    userManager().getUserAppMenu(t, faceReq.getCompanyId(), faceReq.getUserId(), faceReq.getAppId());
            return root == null ? null : root.getChildren();
        }
        return null;
    }

    @Face(login = false)
    @RequestMapping(value = "/getUserAppMenuRes", method = RequestMethod.POST)
    public @ResponseBody Object getUserAppPermissions4Menu(UserAppRequest faceReq, Trace t) {
        if (faceReq.getUserId() != null && faceReq.getAppId() != null) {
            String[] perms =
                    userManager().getUserAppMenus(t, faceReq.getCompanyId(), faceReq.getUserId(), faceReq.getAppId());
            return perms;
        }
        return null;
    }

    @Face(login = false)
    @RequestMapping(value = "/getUserAppOrgIds", method = RequestMethod.POST)
    public @ResponseBody Object getUserAppOrgIds(UserAppRequest faceReq, Trace t) {
        if (faceReq.getUserId() != null && faceReq.getAppId() != null) {
            return userManager().getUserAppOrgs(t, faceReq.getCompanyId(), faceReq.getUserId(), faceReq.getAppId());
        }
        return null;
    }

    @Face(login = false)
    @RequestMapping(value = "/getUserAppAreaIds", method = RequestMethod.POST)
    public @ResponseBody Object getUserAppAreaIds(UserAppRequest faceReq, Trace t) {
        if (faceReq.getUserId() != null && faceReq.getAppId() != null) {
            return userManager().getUserAppAreas(t, faceReq.getCompanyId(), faceReq.getUserId(), faceReq.getAppId());
        }
        return null;
    }

    @Face(login = false)
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public @ResponseBody Object find(ListFaceRequest faceReq, Trace t) {
        WebQuery wq = faceReq.getQuery();
        if (wq != null) {
            List<T> users = userManager().find(t, wq.toQuery());
            desensitization(users);
            return users;
        }
        return null;
    }

    @Face(login = false)
    @RequestMapping(value = "/findAndPtj", method = RequestMethod.POST)
    public @ResponseBody Object findAndPtj(ListFaceRequest faceReq, Trace t) {
        WebQuery wq = faceReq.getQuery();
        if (wq != null) {
            List<T> users = userManager().find(t, wq.toQuery());
            Set<Long> ids = new HashSet<Long>();
            List<Long> fids = new ArrayList<Long>();

            if (users != null && users.size() > 0) {
                for (T user : users) {
                    ids.add(user.getId());
                }
            }
            try {
                List<PT> ptjs = ptManager().find(t, wq.toQuery());

                if (ptjs != null && ptjs.size() > 0) {
                    for (PT job : ptjs) {
                        if (!ids.contains(job.getUserId())) {
                            fids.add(job.getUserId());
                        }
                    }

                }
            } catch (Exception e) {
            }
            if (fids.size() > 0) {
                List<T> users1 = userManager().findByIds(t, fids);
                if (users != null) {
                    users.addAll(users1);
                } else {
                    users = users1;
                }
            }

            desensitization(users);
            return users;
        }
        return null;
    }

    @Face(login = false)
    @RequestMapping(value = "/findPtjPage", method = RequestMethod.POST)
    public @ResponseBody Object findPtjPage(PageFaceRequest faceReq, Trace t) {
        if (faceReq.getQuery() == null || !faceReq.getQuery().hasRule4Field("destOrgId")
                || !faceReq.getQuery().hasRule4Field("destOrgClass")) {
            throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "destOrgId,destOrgClass参数错误。");
        }

        Page<PT> page = new Page<>(faceReq);
        page = ptManager().findPage(t, page);
        return page;
    }

    @Face(login = false)
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    public @ResponseBody Object findPage(PageFaceRequest faceReq, Trace t) {
        Page<T> page = new Page<>(faceReq);
        page = userManager().findPage(t, page);
        desensitization(page.getData());
        return page;
    }

    @Face(login = false)
    @RequestMapping(value = "/findPage4Select", method = RequestMethod.POST)
    public @ResponseBody Object findPage4Select(PageFaceRequest faceReq, Trace t) {
        Page<T> page = new Page<>(faceReq);
        page = userManager().findPage4Select(t, page);
        desensitization(page.getData());
        return page;
    }

    protected void desensitization(List<T> users) {
        if (users != null && users.size() > 0) {
            for (T user : users) {
                user.desensitization();
            }
        }
    }

    // @Face(login = false)
    // @RequestMapping(value = "/findByRoleCompany", method = RequestMethod.POST)
    // public @ResponseBody Object findByRoleCompany(CompanyEntityRequest faceReq, Trace t) throws Exception {
    // if (faceReq.getCompanyId() == null || faceReq.getEntityId() == null) {
    // return null;
    // }
    // return userManager().findByRoleCompany(t, faceReq.getCompanyId(), faceReq.getEntityId());
    // }

}
