package com.linkallcloud.um.pc.face;

import java.util.Arrays;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.iapi.sys.IMenuManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.face.base.BaseTreeFace;

@Controller
@RequestMapping(value = "/face/Menu", method = RequestMethod.POST)
@Module(name = "菜单")
public class MenuFace extends BaseTreeFace<Menu, IMenuManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IMenuManager menuManager;

	@Override
	protected IMenuManager manager() {
		return menuManager;
	}

	@Face(simple = true)
	@RequestMapping(value = "/loadAppMenuTree", method = RequestMethod.POST)
	public @ResponseBody Object loadAppMenuTree(IdFaceRequest fr, Trace t, AppVisitor av) {
		Tree root = manager().getMenuTree(t, new Sid(fr.getId(), fr.getUuid()), false);
		return Arrays.asList(root);
	}

}
