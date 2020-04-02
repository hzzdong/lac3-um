/**
 * LAC2 0.0.1
 * 
 * @Author zhoudong(hzzdong@qq.com) at 2017-05-06
 */
(function($) {
	// 全局系统对象
	window['LAC'] = {};

	LAC.GLOBAL_DIALOG_MODEL_ID = "g_dlg_model_id";
	LAC.GLOBAL_DIALOG_ALERT_ID = "g_dlg_alert_id";
	LAC.GLOBAL_DIALOG_TIP_ID = "g_dlg_tip_id";

	LAC.DIALOG_TYPES = {
		common : {
			modalCss : "",
			tipCss : "",
			btnCss : "btn-default"
		},
		confirm : {
			modalCss : "modal-warning",
			tipCss : "box-warning",
			btnCss : "btn-confirm"
		},
		primary : {
			modalCss : "modal-primary",
			tipCss : "box-primary",
			btnCss : "btn-primary"
		},
		success : {
			modalCss : "modal-success",
			tipCss : "box-success",
			btnCss : "btn-success"
		},
		info : {
			modalCss : "modal-info",
			tipCss : "box-info",
			btnCss : "btn-info"
		},
		warning : {
			modalCss : "modal-warning",
			tipCss : "box-warning",
			btnCss : "btn-warning"
		},
		error : {
			modalCss : "modal-danger",
			tipCss : "box-danger",
			btnCss : "btn-danger"
		}
	};

	LAC.DIALOG_Z_INDEX = 9000;
	LAC.getDialogZIndex = function() {
		var win = window.parent ? window.top : window;
		win['LAC'].DIALOG_Z_INDEX++;
		return win['LAC'].DIALOG_Z_INDEX;
	};
	
	function LacTipWin(tipId) {
		this.tipId = tipId;
		this.wintipInvokeCount = 1;
		this.state = 'show';
		
		this.getTipWin = function(){
			var mainBody = window.parent ? $(window.top.document.body) : $(window.document.body);
			return mainBody.find("#" + this.tipId);
		}
		
		this.next = function(){
			return ++ this.wintipInvokeCount;
		}
		this.setContent = function(message) {
			this.getTipWin().find(".box-body").html(message);
		}
		this.setType = function(type) {
			var typeCss = LAC.DIALOG_TYPES.info.tipCss;
			var thisTip = this.getTipWin();
			$.each(LAC.DIALOG_TYPES, function(k, v) {
				if (k == type) {
					typeCss = v.tipCss;
				}
				if (thisTip.hasClass(v.tipCss)) {
					thisTip.removeClass(v.tipCss);
				}
			});
			if (typeCss) {
				thisTip.addClass(typeCss);
			}
		}
		this.reset = function(content, type) {
			this.setContent(content);
			this.setType(type);
		};
		this.hide = function() {
			this.state = 'hide';
			this.getTipWin().hide();
		};
		this.show = function() {
			this.state = 'show';
			this.getTipWin().show();
		};
		this.bindColseEvent = function(){
			var tipWin = this.getTipWin();
			tipWin.find(".btn-box-tool").on("click", function(){
				tipWin.hide();
			});
		};
		this.autoClose = function(idx) {
			var myIndex = idx;
			var tipWin = this.getTipWin();
			setTimeout(function() {
				if (this.state=='show' && myIndex >= this.wintipInvokeCount) {
					tipWin.hide();
				}
			}, 3000);
		};
	}
	
	LAC.createTipWin = function(tipId){
		return new LacTipWin(tipId);
	}

	/**
	 * tip提醒框
	 * 
	 * @param message
	 *            消息内容
	 * @param type
	 *            tip框的类型：同Dialog
	 * 
	 */
	LAC.tip = function(message, type) {
		var win = window.parent ? window.top : window;
		var myIndex = 1;
		var wintip = win['LAC'].wintip;
		if (wintip) {
			myIndex = wintip.next();
			wintip.reset(message, type);
			if (wintip.state != 'show') {
				wintip.show();
			}
		} else {
			wintip = $.LacDialog.tip(message, type);
		}
		win['LAC'].autoCloseTip(myIndex);
		//wintip.autoClose(myIndex);
	};

	/**
	 * 自动关闭tip提醒框
	 * 
	 * @param idx
	 * 
	 */
	LAC.autoCloseTip = function(idx) {
		var myIndex = idx;
		var win = window.parent ? window.top : window;
		var wintip = win['LAC'].wintip;
		setTimeout(function() {
			if (wintip.state == 'show' && myIndex >= wintip.wintipInvokeCount) {
				wintip.hide();
			}
		}, 3000);
	};

	// 显示loading
	LAC.showLoading = function(message) {
		message = message || "正在加载中...";
		// console.log(parent);
		$(document).mask(message);
	};
	// 隐藏loading
	LAC.hideLoading = function(message) {
		$(document).unmask();
	}
	// 显示透明遮罩
	LAC.showMask = function() {
		$(document).mask();
	};
	// 隐藏透明遮罩
	LAC.hideMask = function() {
		$(document).unmask();
	}

	LAC.showModel = function(options) {
		return $.LacDialog.show(options);
	}

	LAC.getModel = function(dialogId) {
		return Modals.getTopModals().getModal(
				(dialogId && dialogId != "") ? dialogId
						: LAC.GLOBAL_DIALOG_MODEL_ID);
	};

	LAC.closeModel = function(dialogId) {
		LAC.getModel(dialogId).hide();
	};

	LAC.destoryModel = function(dialogId) {
		var id = (dialogId && dialogId != "") ? dialogId
				: LAC.GLOBAL_DIALOG_MODEL_ID;
		var mainBody = window.parent ? $(window.top.document.body)
				: $(window.document.body);
		var dlg = mainBody.find("#" + id).modal("hide");
		if (dlg && dlg.length > 0) {
			dlg.modal("hide");
			dlg.remove();
			Modals.getTopModals().deleteModal(id);
		}
	};

	LAC.closeAlert = function(dialogId) {
		if (dialogId && dialogId != "") {
			$("#" + dialogId).modal("hide");
		} else {
			$("#" + LAC.GLOBAL_DIALOG_ALERT_ID).modal("hide");
		}
	}

	LAC.destoryAlert = function(dialogId) {
		var dlg;
		if (dialogId && dialogId != "") {
			dlg = $("#" + dialogId);
		} else {
			dlg = $("#" + LAC.GLOBAL_DIALOG_ALERT_ID);
		}
		if (dlg && dlg.length > 0) {
			dlg.modal("hide");
			dlg.remove();
		}
	}

	LAC.showInfo = function(message, callback) {
		if (typeof (message) == "function" || arguments.length == 0) {
			callback = message;
			message = "操作成功!";
		}
		$.LacDialog.info(message, '提示信息', callback);
	};

	// 显示成功提示窗口
	LAC.showSuccess = function(message, callback) {
		if (typeof (message) == "function" || arguments.length == 0) {
			callback = message;
			message = "操作成功!";
		}
		$.LacDialog.success(message, '提示信息', callback);
	};
	// 显示失败提示窗口
	LAC.showError = function(message, callback) {
		if (typeof (message) == "function" || arguments.length == 0) {
			callback = message;
			message = "操作失败!";
		}
		$.LacDialog.error(message, '错误提示信息', callback);
	};
	// 显示警告提示窗口
	LAC.showWarn = function(message, callback) {
		if (typeof (message) == "function" || arguments.length == 0) {
			callback = message;
			message = "操作警告!";
		}
		$.LacDialog.warning(message, '警告提示信息', callback);
	};
	// 显示确认提示窗口
	LAC.confirm = function(message, callback) {
		if (typeof (message) == "function" || arguments.length == 0) {
			callback = message;
			message = "您确定要执行此操作吗？";
		}
		$.LacDialog.confirm(message, '警告：请确认提示信息', callback);
	};

	/*
	 * AJAX向服务器提交数据请求
	 * 
	 * @param {Object} options ：请求参数
	 */
	LAC.ajax = function(options) {
		var p = options || {};
		var dataType = p.dataType || "json";
		var contentType = p.contentType || "application/json; charset=utf-8";
		var type = p.type || 'post';
		// alert("LAC.ajax : " + JSON2.stringify(options));
		$.ajax({
					cache : false,
					async : true,
					dataType : dataType,
					contentType : contentType,
					type : type,
					url : p.url,
					data : p.data,
					beforeSend : function() {
						if (p.beforeSend)
							p.beforeSend();
						// else LAC.showLoading("正在加载中...");
					},
					complete : function() {
						if (p.complete)
							p.complete();
						// else LAC.hideLoading();
					},
					success : function(result) {
						// alert("success : " + JSON2.stringify(result) );
						if (result != null
								&& Object.prototype.toString.call(result) === "[object String]") {
							if (result != "" && result.length > 0) {
								result = result.trim();
								if (result.substring(0, 1) == "{") {
									result = JSON.parse(result);
								}
							}
						}

						if (result.redirectUrl) {
							window.top.location.href = result.redirectUrl;
						} else if (result.toUrl) {
							window.location.href = result.toUrl;
						} else if (result.error || (result.code && result.code != "0")) {
							if (result.nonce) {
								$("#nonce").val(result.nonce);
							}
							LAC.tip(result.message || "系统出错啦！！！", "error");
							if (p.error) {
								p.error(result);
							}
						} else {
							if (p.success)
								p.success(result);
						}
					},
					error : function(result, b) {
						// alert(JSON2.stringify(result));
						if (p.error)
							p.error(result);
						LAC.tip(result.message || "系统出错啦！！！", "error");
					}
				});
	};

	/**
	 * 从后台取回对象
	 */
	LAC.GET = function(url, callBack) {
		LAC.ajax({
			contentType : "application/x-www-form-urlencoded",
			// contentType :"application/json; charset=utf-8",
			url : url,
			type : "GET",
			dataType : 'json',
			data : "",
			success : function(data) {
				if (callBack)
					callBack(data);
			}
		});
	}

	/**
	 * POST到后台
	 */
	LAC.POST = function(url, postObject, callBack) {
		var p = {
			contentType : "application/x-www-form-urlencoded",
			url : url,
			type : "POST",
			data : $.param(postObject, false),
			dataType : 'json',
			success : function(data) {
				if (callBack)
					callBack(data);
			}
		};
		LAC.ajax(p);
	};

	LAC.initPermission = function(url, callBack) {
		var p = {
			contentType : "application/json; charset=utf-8",
			url : url,
			type : "GET",
			dataType : 'json',
			success : function(data) {
				//console.log(data);
				LAC.UPS = data;
				if (callBack) {
					callBack(data);
				}
			}
		};
		LAC.ajax(p);
	};

	/**
	 * 按钮等根据是否有权限禁用还是不可见的模式，暂时支持，mode=hide：隐藏；mode=disabled:禁用；
	 */
	$.fn.permissionEnabled = function(mode) {
		var me = $(this);
		if (!mode) {
			mode = "disabled";
		}

		if (me.length <= 0) {
			return;
		}

		for (var i = 0; i < me.length; i++) {
			var ele = me[i];
			
			var win = window.parent ? window.top : window;
			var reses = win["LAC"].UPS;
			if (!reses || reses.length <= 0) {//没有权限数据
				LAC.setElementNoPermissionState($(ele));
			}
			
			var res = $(ele).data("res");
			//console.log("RES:"+res);
			
			var btnRes;
			var logic = "and";
			if(res.indexOf("||") != -1){
				btnRes = res.split("||");
				logic = "or";
			} else if(res.indexOf("&&") != -1){
				btnRes = res.split("&&");
			} else {
				btnRes = [res];
			}
			//console.log("btnRes:"+btnRes);
			
			if(logic == "or"){
				var find = false;
				for(var k=0; k<btnRes.length; k++){
					if(LAC.findMe(reses, btnRes[k].trim())==true){
						find = true;
						break;
					}
				}
				if (find == false) {
					LAC.setElementNoPermissionState($(ele));
				}
			} else {
				var allow = true;
				for(var k=0; k<btnRes.length; k++){
					if(LAC.findMe(reses, btnRes[k].trim())==false){
						allow = false;
						break;
					}
				}
				if (allow == false) {
					LAC.setElementNoPermissionState($(ele));
				}
			}
		}
	};
	
	LAC.findMe = function(resources, me){
		if(!resources || resources.length<=0 || !me){
			return false;
		}
		
		for (var j = 0; j < resources.length; j++) {
			if(resources[j] == me){
				return true;
			}
		}
		return false;
	};

	LAC.setElementNoPermissionState = function(ele, mode) {
		if (!mode) {
			mode = "disabled";
		}
		if (mode == "hide") {
			ele.hide();
		} else if (mode == "disabled") {
			ele.attr("disabled", "disabled");
		}
	};

	/**
	 * 在进行新增和修改时，根据url后台返回的对象的值填充form中对应字段的值
	 * 
	 * @param String
	 *            url ： 从此url取得后台返回的对象
	 * 
	 * @param function
	 *            cb ： 执行成功后的回调函数
	 */
	$.fn.initLacForm = function(url, cb) {
		var me = this;
		LAC.GET(url, function(ret) {
			//console.log('initLacForm GET', ret);
			if(ret && ret.code=="0"){
				var data = ret.data;
				me.initLacFormData(data);
				if (cb) {
					cb(data);
				}
			} else {
        		LAC.tip(ret.message || "系统出错啦！！！", "error");
        	}
		});
	};

	/**
	 * 在进行新增和修改时，根据后台返回的对象的值填充form中对应字段的值
	 * 
	 * @param {Object}
	 *            data ： 后台返回的对象
	 */
	$.fn.initLacFormData = function(data) {
		if (this.length === 0 || !data) {
			return;
		}

		var dtObj = data;
		if (typeof obj === 'string') {
			dtObj = JSON.parse(data);
		}

		var properties = this.lacFormToArray();
		// alert("properties:"+JSON2.stringify(properties));
		for (var i = 0, l = properties.length; i < l; i++) {
			var o = properties[i];
			var odata;
			if (o.name.indexOf(".", 0) > 0) {
				var nameArray = o.name.split(".");
				var tmp;
				for (var j = 0; j < nameArray.length; j++) {
					if (j == 0) {
						if (dtObj) {
							tmp = dtObj[nameArray[j]];
						}
					} else {
						if (tmp) {
							tmp = tmp[nameArray[j]];
						}
					}
				}
				odata = tmp;
			} else if (o.name.indexOf("_", 0) > 0) {
				var nameArray = o.name.split("_");
				var tmp;
				for (var j = 0; j < nameArray.length; j++) {
					if (j == 0) {
						if (dtObj) {
							tmp = dtObj[nameArray[j]];
						}
					} else {
						if (tmp) {
							tmp = tmp[nameArray[j]];
						}
					}
				}
				odata = tmp;
			} else {
				odata = dtObj[o.name];
			}
			if (o.stype == 'D') {
				var d = new Date(odata);
				odata = d.getFullYear() + '-' + (d.getMonth() + 1) + '-'
						+ d.getDate();
			}

			var ele = $("[name='" + o.name + "']", this);
			if (ele) {
				if (odata) {
					if (o.type == 'select-one' || o.type == 'select-multiple') {
						ele.val(odata);// .trigger("change");
					} else {
						ele.val(odata);
					}
				} else if (odata == 0) {
					if (o.type == 'select-one' || o.type == 'select-multiple') {
						ele.val(0);// .trigger("change");
					} else {
						ele.val(0);
					}
				} else {
					ele.val('');
				}
			}

			// labelFromData==true时，根据后端数据设置label名称
			var oLabel;
			if (o.labelFromData) {
				if (o.label.indexOf(".", 0) > 0) {
					var nameArray = o.label.split(".");
					var tmp;
					for (var j = 0; j < nameArray.length; j++) {
						if (j == 0) {
							if (dtObj) {
								tmp = dtObj[nameArray[j]];
							}
						} else {
							if (tmp) {
								tmp = tmp[nameArray[j]];
							}
						}
					}
					oLabel = tmp;
				} else {
					oLabel = dtObj[o.label];
				}
				var ele = $("[name='L-" + o.label + "']", this);
				if (ele) {
					if (oLabel) {
						ele.innerText = oLabel;
						ele.html(oLabel);
					}
				}
			}
			var addon;
			if (o.addon) {
				if (o.addon.indexOf(".", 0) > 0) {
					var nameArray = o.addon.split(".");
					var tmp = "";
					for (var j = 0; j < nameArray.length; j++) {
						if (j == 0) {
							if (dtObj) {
								tmp = dtObj[nameArray[j]];
							}
						} else {
							if (tmp) {
								if (!tmp[nameArray[j]]) {
									break;
								}
								tmp = tmp[nameArray[j]];
							}
						}
					}
					addon = tmp;
				} else {
					addon = dtObj[o.addon];
				}
				var ele = $("[id='addon-" + o.addon + "']", this);
				if (ele) {
					if (addon) {
						ele.html(addon);
					}
				}
			}
		}
	}

	/**
	 * Returns the value of the field element.
	 */
	$.lacFieldValue = function(el, successful) {
		var n = el.name, t = el.type, tag = el.tagName.toLowerCase();

		var rCRLF = /\r?\n/g;

		if (typeof successful === 'undefined') {
			successful = true;
		}

		/* eslint-disable no-mixed-operators */
		if (successful
				&& (!n || t === 'reset' || t === 'button'
						|| (t === 'checkbox' || t === 'radio') && !el.checked
						|| (t === 'submit' || t === 'image') && el.form
						&& el.form.clk !== el || tag === 'select'
						&& el.selectedIndex === -1)) {//|| el.disabled 
			/* eslint-enable no-mixed-operators */
			return null;
		}

		if (tag === 'select') {
			var index = el.selectedIndex;

			if (index < 0) {
				return null;
			}

			var a = [], ops = el.options;
			var one = (t === 'select-one');
			var max = (one ? index + 1 : ops.length);

			for (var i = (one ? index : 0); i < max; i++) {
				var op = ops[i];

				if (op.selected && !op.disabled) {
					var v = op.value;

					if (!v) { // extra pain for IE...
						v = (op.attributes && op.attributes.value && !(op.attributes.value.specified)) ? op.text
								: op.value;
					}

					if (one) {
						return v;
					}

					a.push(v);
				}
			}

			return a;
		}

		return $(el).val().replace(rCRLF, '\r\n');
	};

	$.fn.lacFormToArray = function(semantic, elements, filtering) {
		var a = [];

		if (this.length === 0) {
			return a;
		}

		var form = this[0];
		var formId = this.attr('id');
		var els = (semantic || typeof form.elements === 'undefined') ? form
				.getElementsByTagName('*') : form.elements;
		var els2;

		if (els) {
			els = $.makeArray(els); // convert to standard array
		}

		// #386; account for inputs outside the form which use the 'form'
		// attribute
		// FinesseRus: in non-IE browsers outside fields are already included in
		// form.elements.
		if (formId
				&& (semantic || /(Edge|Trident)\//.test(navigator.userAgent))) {
			els2 = $(':input[form="' + formId + '"]').get(); // hat tip @thet
			if (els2.length) {
				els = (els || []).concat(els2);
			}
		}

		if (!els || !els.length) {
			return a;
		}

		if ($.isFunction(filtering)) {
			els = $.map(els, filtering);
		}

		var i, j, n, v, el, max, jmax;

		for (i = 0, max = els.length; i < max; i++) {
			el = els[i];
			n = el.name;
			if (!n) {// || el.disabled
				continue;
			}

			if (semantic && form.clk && el.type === 'image') {
				// handle image inputs on the fly when semantic == true
				if (form.clk === el) {
					a.push({
						name : n,
						value : $(el).val(),
						type : el.type
					});
					a.push({
						name : n + '.x',
						value : form.clk_x
					}, {
						name : n + '.y',
						value : form.clk_y
					});
				}
				continue;
			}

			v = $.lacFieldValue(el, true);
			if (v && v.constructor === Array) {
				if (elements) {
					elements.push(el);
				}
				// for (j = 0, jmax = v.length; j < jmax; j++) {
				// a.push({name: n, value: v[j]});
				// }
				a.push({
					name : n,
					value : v,
					op : el.getAttribute("oper"),
					stype : el.getAttribute("stype"),
					type : el.type
				});

			} else if (el.type === 'file') {// feature.fileapi &&
			// if (elements) {
			// elements.push(el);
			// }
			//
			// var files = el.files;
			//
			// if (files.length) {
			// for (j = 0; j < files.length; j++) {
			// //a.push({name: n, value: files[j], type: el.type});
			// a.push({name: n, value: files[j], op: el.getAttribute("oper"),
			// stype: el.getAttribute("stype"), type: el.type});
			// }
			// } else {
			// // #180
			// //a.push({name: n, value: '', type: el.type});
			// a.push({name: n, value: '', op: el.getAttribute("oper"), stype:
			// el.getAttribute("stype"), type: el.type});
			// }

			} else if (v !== null && typeof v !== 'undefined') {
				if (elements) {
					elements.push(el);
				}
				// a.push({name: n, value: v, type: el.type, required:
				// el.required});
				a.push({
					name : n,
					value : v,
					op : el.getAttribute("oper"),
					stype : el.getAttribute("stype"),
					type : el.type,
					required : el.required
				});
			}
		}

		if (!semantic && form.clk) {
			// input type=='image' are not found in elements array! handle it
			// here
			var $input = $(form.clk), input = $input[0];

			n = input.name;

			if (n && !input.disabled && input.type === 'image') {
				a.push({
					name : n,
					value : $input.val()
				});
				a.push({
					name : n + '.x',
					value : form.clk_x
				}, {
					name : n + '.y',
					value : form.clk_y
				});
			}
		}

		return a;
	};

	/**
	 * 把表单字段转换成QueryRuleGroup查询对象
	 * 
	 * @param {Object}
	 *            custRules ：自定义的rules
	 */
	$.fn.lacFormToCnds = function(custRules) {
		var rules = [];
		if (this.length === 0) {
			return rules;
		}

		var properties = this.lacFormToArray();
		// alert("properties:"+JSON2.stringify(properties));
		for (var i = 0, l = properties.length; i < l; i++) {
			var o = properties[i];
			if (o.name.charAt(0) == '=') {// 过滤以"="开头的字段
				continue;
			}
			if (o.value && o.value != "" && o.value != 'NaN-NaN-NaN') {// 过滤空值
				// var idName = o.id.replace(/_/g, "\.");
				var fieldName = o.name;
				if (o.name.charAt(0) == '^') {// 开头为^ 符号的不将 "_" 转换成
					// ".",并将^去掉
					fieldName = fieldName.replace(/\^/g, "");
				} else {
					fieldName = fieldName.replace(/_/g, "\.");
				}
				var rule = {
					id : o.id,
					field : fieldName,
					data : ($.trim(o.value)),
					op : o.op,
					type : o.stype
				};
				rules.push(rule);
			}
		}

		if (custRules) {
			if (typeof custRules === 'object' && Array == custRules.constructor) {
				var allRules = rules.concat(custRules);
				rules = allRules;
			} else {
				rules.push(custRules);
			}
		}
		return rules;
	}

	/*-
	 * 把表单字段转换成名称为objName的对象
	 *
	 * @param {Object}
	 *            mainform ：数据提交的form对象
	 * @param {string}
	 *            dataObjName ： action中数据对象的名称
	 *
	 * 调用示例：
	 * form.lacFormToObject("pet");
	 * 1)支持空值过滤；
	 * 2)过滤以"="开头的字段；
	 * 3)同名字段自动组装成Array；
	 * 4)input中设置toServerArray="true"可以把此字段强制设置成Array，但是必须设置此input的id值；
	 * 5)支持字段名称带"_"的特殊处理：{company_id : 5} -> {company : {id : 5}};
	 *
	 * org_id:1 -> {org:{id:1}},
	 * org_id:11 -> {org:{id:11}},
	 * org_name:'Abc' -> {org:{name:'Abc'}},
	 * user_org_id:'2' -> {user: {org:{id:2}}}
	 * user_org_id:'3' -> {user: {org:{id:3}}}
	 * user_org_name:'Def' -> {user: {org:{name:'Def'}}}
	 *
	 * { org : {id:[1,11], name:'Abc'}, user : { org : { id : [2,3], name:'Def'}} }
	 */
	$.fn.lacFormToObject = function(objName) {
		var obj = {};
		if (this.length === 0) {
			return obj;
		}

		var properties = this.lacFormToArray();
		for (var i = 0, l = properties.length; i < l; i++) {
			var o = properties[i];

			if (o.name.charAt(0) == '=') {// 过滤以"="开头的字段
				continue;
			}

			var toServerArray = false;
			if (o.id) {
				var ar = document.getElementById(o.id).getAttribute(
						"toServerArray");
				if (ar && (ar == true || ar.toLowerCase() == 'true')) {
					toServerArray = true;
				}
			}

			if (o.value && o.value != "" && o.value != 'NaN-NaN-NaN') {// 过滤空值
				var names = o.name.split("_");
				var thisVal = (o.type == "password") ? hex_md5(o.value)
						: o.value;
				if (names && names.length == 1) {// name : 'xiaoming'
					var existField = obj[names[0]];
					if (existField) {// 已经存在同名的field，需要转成数组
						if (typeof existField === 'object'
								&& Array == existField.constructor) {
							//
						} else {
							obj[names[0]] = [];
							obj[names[0]].push(existField);
						}
						obj[names[0]].push(thisVal);
					} else {
						if (toServerArray) {
							obj[names[0]] = [];
							obj[names[0]].push(thisVal);
						} else {
							obj[names[0]] = thisVal;
						}
					}
				} else if (names && names.length > 1) {
					/**
					 * org_id:1 -> {org:{id:1}}, org_id:11 -> {org:{id:11}},
					 * org_name:'Abc' -> {org:{name:'Abc'}}, user_org_id:'2' ->
					 * {user: {org:{id:2}}} user_org_id:'3' -> {user:
					 * {org:{id:3}}} user_org_name:'Def' -> {user:
					 * {org:{name:'Def'}}} { org : {id:[1,11], name:'Abc'}, user : {
					 * org : { id : [2,3], name:'Def'}} }
					 */
					var level1F = obj[names[0]];
					if (level1F) {// 已经存在同名的field，需要转成数组.
						var cuur = level1F;
						for (var j = 1, len = names.length - 1; j < len; j++) {
							if (!cuur[names[j]] && j < len - 1) {
								cuur[names[j]] = {};
							}
							cuur = cuur[names[j]];
						}
						var levelLastFV = cuur[names[names.length - 1]];
						if (!levelLastFV) {
							if (toServerArray) {
								cuur[names[names.length - 1]] = [];
								cuur[names[names.length - 1]].push(thisVal);
							} else {
								cuur[names[names.length - 1]] = thisVal;
							}
						} else if (levelLastFV
								&& typeof levelLastFV === 'object'
								&& Array == levelLastFV.constructor) {
							cuur[names[names.length - 1]].push(thisVal);
						} else {
							cuur[names[names.length - 1]] = [];
							cuur[names[names.length - 1]].push(levelLastFV);
							cuur[names[names.length - 1]].push(thisVal);
						}

					} else {
						var cuur = obj;
						for (var j = 0, len = names.length - 1; j < len; j++) {
							if (!cuur[names[j]]) {
								cuur[names[j]] = {};
							}
							cuur = cuur[names[j]];
						}
						if (toServerArray) {
							cuur[names[names.length - 1]] = [];
							cuur[names[names.length - 1]].push(thisVal);
						} else {
							cuur[names[names.length - 1]] = thisVal;
						}
					}
				}
			}
		}

		if (objName && objName != "") {
			var resultObj = {};
			if (obj.nonce) {
				resultObj.nonce = obj.nonce;
				obj.nonce = "";
			}
			resultObj[objName] = obj;
			return resultObj;
		} else {
			return obj;
		}
	};

	/**
	 * form提交：带验证
	 * 
	 * @param {Object}
	 *            appendObj ： 数据对象
	 * @param {function}
	 *            cb : 保存成功后的回调函数
	 * 
	 * 注意： 1)数据以stream的方式递交； 2)调用示例：form.submitForm(callBack);
	 */
	$.fn.save = function(appendObj, cb) {
		if (this.length === 0) {
			return;
		}
		if (this.validationEngine('validate')) {
			var obj = this.lacFormToObject();
			obj.isUpdate = (obj.id && obj.id > 0) ? true : false;
			if(appendObj){
				obj = $.extend(obj, appendObj);
			}
			LAC.ajax({
				type : "post",
				dataType : 'json',
				cache : false,
				contentType : "application/json",
				url : this.attr('action'),
				data : JSON2.stringify(obj),
				success : function(ret) {
					if(ret && ret.code=="0"){
		        		var data = ret.data;
		        		obj = $.extend(obj, data);
						if (cb) {
							cb(data);
						} else {
							LAC.tip('操作成功!', 'success');
						}
					} else{
						LAC.tip(ret.message || "系统出错啦！！！", "error");
					}
				}
			});
		}
	};

	/**
	 * form提交，并关闭
	 * 
	 * @param {Object}
	 *            appendObj ： 数据对象
	 * @param {function}
	 *            cb : 保存成功后的回调函数
	 * 
	 * 注意： 1)数据以stream的方式递交； 2)调用示例：form.submitForm(callBack);
	 */
	$.fn.saveClose = function(appendObj, cb) {
  		if (this.length === 0) {
  			return;
  		}
  		if (this.validationEngine('validate')) {
  			var obj = this.lacFormToObject();
  			obj.isUpdate = (obj.id && obj.id > 0) ? true : false;
  			if(appendObj){
  				obj = $.extend(obj, appendObj);
  			}
  			LAC.ajax({
  				type : "post",
  				dataType : 'json',
  				cache : false,
  				contentType : "application/json",
  				url : this.attr('action'),
  				data : JSON2.stringify(obj),
  				success : function(ret) {
  					if(ret && ret.code=="0"){
  		        		var data = ret.data;
  		        		obj = $.extend(obj, data);
  						if (cb) {
  							cb(data);
  						} else {
  							LAC.tip('操作成功!', 'success');
  							LacTab.closeThisTab(obj);
  						}
  					} else{
  						LAC.tip(ret.message || "系统出错啦！！！", "error");
  					}
  				}
  			});
  		}
	};
	
	$.fn.saveDlg = function(dlgId, appendObj, cb) {
  		if (this.length === 0) {
  			return;
  		}
  		if (this.validationEngine('validate')) {
  			var obj = this.lacFormToObject();
  			obj.isUpdate = (obj.id && obj.id > 0) ? true : false;
  			if(appendObj){
  				obj = $.extend(obj, appendObj);
  			}
  			LAC.ajax({
  				type : "post",
  				dataType : 'json',
  				cache : false,
  				contentType : "application/json",
  				url : this.attr('action'),
  				data : JSON2.stringify(obj),
  				success : function(ret) {
  					if(ret && ret.code=="0"){
  		        		var data = ret.data;
  		        		obj = $.extend(obj, data);
  						if (cb) {
  							cb(data);
  						} else {
  							LAC.tip('操作成功!', 'success');
  							LAC.getModel(dlgId).getCallback()(obj);
  						}
  					} else{
  						LAC.tip(ret.message || "系统出错啦！！！", "error");
  					}
  				}
  			});
  		}
	};

	/**
	 * 从datatable界面发起快速搜索，点击搜索按钮执行此方法
	 * 
	 * @param dataTable
	 */
	$.fn.search4DataTable = function(dataTable, rules) {
		var oInits = dataTable.settings().init();
		oInits.cnds = this.lacFormToCnds();
		if (rules) {
			oInits.cnds = oInits.cnds.concat(rules);
		}
		oInits.searchCnds = oInits.cnds;
		dataTable.draw();
	};

	/**
	 * 从datatable界面发起自定义搜索
	 */
	LAC.custSearch4DataTable = function(dataTable, rules) {
		var oInits = dataTable.settings().init();
		oInits.cnds = rules;
		oInits.searchCnds = oInits.cnds;
		dataTable.draw();
	};

	/**
     * 新增一行(TabPage)
     *
     * @param dataTable
     * @param url
     * @param tabId
     * @param tabTitle
     * @param cb
     *            callback
     */
    LAC.addRow4DataTable = function(dataTable, url, tabId, tabTitle, cb) {
        LacTab.addTab({
            id : tabId ? tabId : "default-tab-id",
            title : tabTitle ? tabTitle : '新增',
            url : url,
            autoLink : true,
            callback : function(data) {
                if (data) {
                    dataTable.row.add(data).draw();
                    if(cb){
                        cb(data);
                    } else {
                        $(".btn-permed").permissionEnabled();
                    }
                }
            }
        });
    };
    
    /**
     * 新增一行(Dialog)
     *
     * @param dataTable
     * @param url
     * @param tabId
     * @param tabTitle
     * @param cb
     *            callback
     */
    LAC.addRow4DataTable2Dlg = function(dataTable, url, dlgId, dlgTitle, cb) {
    	var dlg = LAC.showModel({
			id : dlgId ? dlgId : "default-tab-id",
			type : "common",// primary common
			sizeCss : "modal-lg",
			title : dlgTitle ? dlgTitle : '编辑',
			url :  url,
			method : "get",
			callback : function(data) {
				LAC.closeModel(dlgId ? dlgId : "default-tab-id");
				if(cb){
					cb(data);
				} else {
					if (data) {
						dataTable.row.add(data).draw();
					}
					$(".btn-permed").permissionEnabled();
				}
			}
		});
    };

    /**
     * 新增一行(返回)
     *
     * @param dataTable
     * @param url
     * @param tabId
     * @param tabTitle
     * @param cb
     *            callback
     */
    LAC.addRow4DataTable2 = function(dataTable, url, tabId, tabTitle, cb) {
        LacTab.addTab({
            id : tabId ? tabId : "default-tab-id",
            title : tabTitle ? tabTitle : '新增',
            url : url,
            autoLink : true,
            callback : function(data) {
                if (data) {
                    if(cb){
                        cb(data);
                    } else {
                        dataTable.row.add(data).draw();
                        $(".btn-permed").permissionEnabled();
                    }
                }
            }
        });
    };

	/**
	 * 点击DataTable上某行进行编辑（TabPage）
	 * 
	 * @param curRow
	 * @param url
	 * @param tabId
	 * @param tabTitle
	 * @param td0
	 * @param cb
	 *            callback
	 */
	LAC.editCurRow4DataTable = function(curRow, url, tabId, tabTitle, td0, cb) {
		var data = curRow.data();
		url = LAC.appendParam2URL(url, "id", data.id);
		url = LAC.appendParam2URL(url, "uuid", data.uuid);

		var idx = -1;
		if (td0) {
			if($.isFunction(td0)){
				cb = td0;
			} else {
				idx = td0.text();
			}
		}

		LacTab.addTab({
			id : tabId ? tabId : "default-tab-id",
			title : tabTitle ? tabTitle : '编辑',
			url : url,
			autoLink : true,
			callback : function(data) {
				if(cb){
					cb(data);
				} else {
					if (data) {
						curRow.data(data);// .draw(false);
					}
					$(".btn-permed").permissionEnabled();
				}
				if (td0 && !($.isFunction(td0))) {
					td0.text(idx);
				}
			}
		});
	};
	
	/**
	 * 点击DataTable上某行进行编辑(Dialog)
	 * 
	 * @param curRow
	 * @param url
	 * @param tabId
	 * @param tabTitle
	 * @param td0
	 * @param cb
	 *            callback
	 */
	LAC.editCurRow4DataTable2Dlg = function(curRow, url, dlgId, dlgTitle, td0, cb) {
		var data = curRow.data();
		url = LAC.appendParam2URL(url, "id", data.id);
		url = LAC.appendParam2URL(url, "uuid", data.uuid);

		var idx = -1;
		if (td0) {
			if($.isFunction(td0)){
				cb = td0;
			} else {
				idx = td0.text();
			}
		}
		
		var dlg = LAC.showModel({
			id : dlgId ? dlgId : "default-tab-id",
			type : "common",// primary common
			sizeCss : "modal-lg",
			title : dlgTitle ? dlgTitle : '编辑',
			url :  url,
			method : "get",
			callback : function(data) {
				LAC.closeModel(dlgId ? dlgId : "default-tab-id");
				if(cb){
					cb(data);
				} else {
					if (data) {
						curRow.data(data);// .draw(false);
					}
					$(".btn-permed").permissionEnabled();
				}
				if (td0 && !($.isFunction(td0))) {
					td0.text(idx);
				}
			}
		});

	};

	/**
	 * 删除一条记录
	 * 
	 * @param curRow
	 * @param url
	 * @param confirmMsg
	 * @param callBack
	 */
	LAC.delete4DataTable = function(curRow, url, confirmMsg, callBack) {
		var data = curRow.data();
		url = LAC.appendParam2URL(url, "id", data.id);
		url = LAC.appendParam2URL(url, "uuid", data.uuid);
		LAC.confirmExecute(url, {}, function(ret) {
			if (ret) {
				if (callBack) {
					callBack(ret);
				} else {
					curRow.remove().draw(false);
				}
			} else {
				LAC.tip("操作执行失败！！！", "error");
			}
		}, confirmMsg || "你确定要执行此操作吗？", true);
	};

	/**
	 * DataTable批量删除记录
	 * 
	 * @param datatable
	 * @param url
	 * @param confirmMsg
	 * @param callBack
	 */
	LAC.deletes4DataTable = function(datatable, url, confirmMsg, callBack) {
		var rows = datatable.rows('.selected').data();
		if (rows && rows.length < 1) {
			LAC.showWarn("请至少选择一行。");
		} else {
			var iduuids = {};
			for (var i = 0; i < rows.length; i++) {
				var rowData = rows[i];
				iduuids[rowData.uuid] = rowData.id;
			}

			LAC.confirmExecute(url, iduuids, function(ret) {
				if (ret) {
					if (callBack)
						callBack(ret);
					else
						datatable.draw();
				} else {
					LAC.tip("删除失败！！！", "error");
				}
			}, confirmMsg || "你确定要执行此操作吗？", true);
		}
	};
	
	/**
	 * 更新状态（一条记录）
	 * 
	 * @param curRow
	 * @param url
	 * @param status
	 * @param confirmMsg
	 * @param callBack
	 */
	LAC.status4DataTable = function(curRow, url, status, confirmMsg, callBack) {
		var data = curRow.data();
		url = LAC.appendParam2URL(url, "status", status);
		url = LAC.appendParam2URL(url, "id", data.id);
		url = LAC.appendParam2URL(url, "uuid", data.uuid);
		LAC.confirmExecute(url, {}, function(ret) {
			console.log(ret);
			if(ret && ret.code=="0"){
				if (callBack) {
					callBack(ret.data);
				} else {
					data.status = status;
					curRow.data(data).draw(false);
				}
			} else {
				LAC.tip(ret.message || "操作执行失败！！！", "error");
			}
		}, confirmMsg || "你确定要执行此操作吗？", true);
	};
	
	/**
	 * 更新状态(批量)
	 * 
	 * @param datatable
	 * @param url
	 * @param status
	 * @param confirmMsg
	 * @param callBack
	 */
	LAC.statuss4DataTable = function(datatable, url, status, confirmMsg, callBack) {
		var rows = datatable.rows('.selected').data();
		if (rows && rows.length < 1) {
			LAC.showWarn("请至少选择一行。");
		} else {
			var iduuids = {};
			for (var i = 0; i < rows.length; i++) {
				var rowData = rows[i];
				iduuids[rowData.uuid] = rowData.id;
			}

			url = LAC.appendParam2URL(url, "status", status);
			LAC.confirmExecute(url, iduuids, function(ret) {
				if (ret) {
					if (callBack)
						callBack(ret);
					else
						datatable.draw();
				} else {
					LAC.tip("删除失败！！！", "error");
				}
			}, confirmMsg || "你确定要执行此操作吗？", true);
		}
	};
	
	/**
	 * 新增一行(TabPage)
	 * 
	 * @param treeId
	 * @param url
	 * @param tabId
	 * @param tabTitle
	 */
	LAC.addRow4TreeTable = function(treeId, url, tabId, tabTitle) {
		LacTab.addTab({
			id : tabId ? tabId : "default-tab-id",
			title : tabTitle ? tabTitle : '新增',
			url : url,
			autoLink : true,
			callback : function(data) {
				if (data) {
					var zTree = $.fn.zTree.getZTreeObj(treeId);
					var nodes = zTree.getSelectedNodes();
					if(nodes != null && nodes.length > 0){
						var treeNode = nodes[0];
						zTree.addNodes(treeNode, data);
					} else {
						zTree.addNodes(null, data);
					}
				}
			}
		});
	};
	
	/**
	 * 新增一行(Dialog)
	 * 
	 * @param treeId
	 * @param url
	 * @param tabId
	 * @param tabTitle
	 * @param cb
	 */
	LAC.addRow4TreeTableDlg = function(treeId, url, dlgId, dlgTitle, cb) {
		var dlg = LAC.showModel({
			id : dlgId ? dlgId : "default-tab-id",
			type : "common",// primary common
			sizeCss : "modal-lg",
			title : dlgTitle ? dlgTitle : '编辑',
			url :  url,
			method : "get",
			callback : function(data) {
				LAC.closeModel(dlgId ? dlgId : "default-tab-id");
				if(cb){
					cb(data);
				} else {
					var zTree = $.fn.zTree.getZTreeObj(treeId);
					var nodes = zTree.getSelectedNodes();
					if(nodes != null && nodes.length > 0){
						var treeNode = nodes[0];
						zTree.addNodes(treeNode, data);
					} else {
						zTree.addNodes(null, data);
					}
				}
			}
		});
	};

	/**
	 * 点击TreeTable上某行进行编辑
	 * 
	 * @param treeId
	 * @param url
	 * @param tabId
	 * @param tabTitle
	 */
	LAC.editCurRow4TreeTable = function(treeId, url, tabId, tabTitle) {
		LacTab.addTab({
			id : tabId ? tabId : "default-tab-id",
			title : tabTitle ? tabTitle : '编辑',
			url : url,
			autoLink : true,
			callback : function(data) {
				//console.log('LAC.editCurRow4TreeTable', data);
				if (data) {
					var zTree = $.fn.zTree.getZTreeObj(treeId), nodes = zTree
							.getSelectedNodes(), treeNode = nodes[0];
					if (treeNode) {
						$.extend(treeNode, data);
						//console.log("treeNode", treeNode);
						zTree.updateNode(treeNode);
						LAC_TreeTable.updateTreeTableColumns(treeId, treeNode);
					}
				}
			}
		});
	};
	
	/**
	 * 点击TreeTable上某行进行编辑(Dialog)
	 * 
	 * @param treeId
	 * @param url
	 * @param tabId
	 * @param tabTitle
	 * @param cb
	 */
	LAC.editCurRow4TreeTableDlg = function(treeId, url, dlgId, dlgTitle, cb) {
		var dlg = LAC.showModel({
			id : dlgId ? dlgId : "default-tab-id",
			type : "common",// primary common
			sizeCss : "modal-lg",
			title : dlgTitle ? dlgTitle : '编辑',
			url :  url,
			method : "get",
			callback : function(data) {
				LAC.closeModel(dlgId ? dlgId : "default-tab-id");
				if(cb){
					cb(data);
				} else {
					var zTree = $.fn.zTree.getZTreeObj(treeId);
					var nodes = zTree.getSelectedNodes();
					if(nodes != null && nodes.length > 0){
						var treeNode = nodes[0];
						$.extend(treeNode, data);
						zTree.updateNode(treeNode);
						LAC_TreeTable.updateTreeTableColumns(treeId, treeNode);
					}
				}
			}
		});
	};

	/**
	 * 删除一条记录
	 * 
	 * @param treeId
	 * @param url
	 * @param confirmMsg
	 * @param callBack
	 */
	LAC.delete4TreeTable = function(treeId, url, confirmMsg, callBack) {
		LAC.confirmExecute(url, {}, function(ret) {
			//console.log('delete4TreeTable', ret);
			if(ret && ret.code=="0"){
				if (callBack) {
					callBack(ret.data);
				} else {
					var zTree = $.fn.zTree.getZTreeObj(treeId), nodes = zTree
							.getSelectedNodes(), treeNode = nodes[0];
					if (treeNode) {
						zTree.removeNode(treeNode);
					}
				}
			} else {
				LAC.tip(ret.message || "操作执行失败！！！", "error");
			}
		}, confirmMsg || "你确定要执行此操作吗？", true);
	};
	
	/**
	 * 更新节点状态
	 */
	LAC.changeStatus4TreeTable = function(status, treeId, url, skipChkChildren, confirmMsg, callBack) {
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		var nodes = zTree.getSelectedNodes();
		if (!nodes && nodes.length > 1) {
			LAC.showError("请选择一个节点!");
			return;
		}
		var treeNode = nodes[0];
		if (treeNode && !skipChkChildren) {
			if (treeNode.children && treeNode.children.length > 0) {
				LAC.showError("该节点下有子节点，不能操作!");
				return;
			}
		}
		
		LAC.confirmExecute(url, {}, function(ret) {
			//console.log('changeStatus4TreeTable', ret);
			if(ret && ret.code=="0"){
				if (callBack) {
					callBack(ret.data);
				} else {
					var zTree = $.fn.zTree.getZTreeObj(treeId);
					if(zTree){
						var nodes = zTree.getSelectedNodes();
						if(nodes){
							var treeNode = nodes[0];
							treeNode.status = status;
							zTree.updateNode(treeNode);
							LAC_TreeTable.updateTreeTableColumns(treeId, treeNode);
						}
					}
				}
			} else {
				LAC.tip(ret.message || "操作执行失败！！！", "error");
			}
		}, confirmMsg || "你确定要执行此操作吗？", true);
	};

	/**
	 * 弹出确认对话框确认后执行
	 * 
	 * @param url
	 * @param data
	 * @param callBack
	 * @param confirmMsg
	 * @param needHint
	 *            true:成功后要弹出确认框提醒,false:不需要提醒
	 */
	LAC.confirmExecute = function(url, data, callBack, confirmMsg, needHint) {
		LAC.confirm(confirmMsg || "您确定要进行此操作吗？", function(ok) {
			if (ok) {
				LAC.ajax({
					url : url,
					data : JSON2.stringify(data),
					dataType : 'json',
					contentType : "application/json; charset=utf-8",
					success : function(ret) {
						if (callBack) {
							callBack(ret);
						}else{
							if (ret && needHint) {
								LAC.tip('操作成功!', 'success');
							}
						}
					}
				});
			}
		});
	};

	/**
	 * 更新单节点状态
	 * 
	 * @param {Object}
	 *            zTree : zTree对象
	 * @param {string}
	 *            url ： 删除操作action地址
	 * @param {string}
	 *            title ： 提示警告文字
	 * @param {boolean}
	 *            skipChkChildren ： 跳过子结点检查
	 */
	LAC.deleteSelectedSingleTreeNodeState = function(zTree, url, title,
			skipChkChildren) {
		var nodes = zTree.getSelectedNodes();
		if (!nodes && nodes.length > 1) {
			LAC.showError("请选择一个节点!");
			return;
		}
		var treeNode = nodes[0];
		if (!skipChkChildren) {
			if (treeNode.children && treeNode.children.length > 0) {
				LAC.showError("该节点下有子节点，不能操作!");
				return;
			}
		}
		var url = url + "?id=" + treeNode.id + "&uuid=" + treeNode.uuid;
		title = title || '确认要执行此操作吗？';
		LAC.confirm(title, function(yes) {
			if (yes) {
				var p = {
					contentType : "application/json; charset=utf-8",
					url : url,
					data : null,
					dataType : "json",
					success : function(result) {
						if (result.exception) {
							LAC.tip(result.exception.message, "error");
						} else if (result.redirectUrl) {
							window.top.location.href = result.redirectUrl;
						} else {
							zTree.removeNode(treeNode);
							LAC.tip('操作成功!', 'success');
						}
					},
					error : function(message) {
						LAC.tip('数据操作失败!<BR>错误信息：' + message, "error");
					}
				};
				LAC.ajax(p);
			}
		});
	};

	/*
	 * 更新单节点状态 @param {Object} zTree : zTree对象 @param {string} url ：
	 * 删除操作action地址 @param {string} title ： 提示警告文字 @param {Object} state @param
	 * {boolean} skipChkChildren ： 跳过子结点检查
	 */
	LAC.changeSelectedSingleTreeNodeState = function(zTree, url, title, state,
			skipChkChildren) {
		var nodes = zTree.getSelectedNodes();
		if (!nodes && nodes.length > 1) {
			LAC.showError("请选择一个节点!");
			return;
		}
		var treeNode = nodes[0];
		if (!skipChkChildren) {
			if (treeNode.children && treeNode.children.length > 0) {
				LAC.showError("该节点下有子节点，不能操作!");
				return;
			}
		}
		var stateUrl = url + "?id=" + treeNode.id + "&uuid=" + treeNode.uuid;
		title = title || '确认要执行此操作吗？';
		LAC.confirm(title, function(yes) {
			if (yes) {
				var p = {
					contentType : "application/json; charset=utf-8",
					url : stateUrl,
					data : null,
					dataType : "json",
					success : function(result) {
						if (result.exception) {
							LAC.tip(result.exception.message, "error");
						} else if (result.redirectUrl) {
							window.top.location.href = result.redirectUrl;
						} else {
							if (typeof state == 'object') {
								treeNode = $.extend(treeNode, state);
							} else {
								treeNode.state = state;
							}
							zTree.updateNode(treeNode);
							LAC.tip('操作成功!', 'success');
						}
					},
					error : function(message) {
						LAC.tip('数据操作失败!<BR>错误信息：' + message, "error");
					}
				};
				LAC.ajax(p);
			}
		});
	}

	/*
	 * 更新节点状态 @param {Object} zTree : zTree对象 @param {string} url ： 删除操作action地址
	 * @param {string} title ： 提示警告文字 @param {Object} state @param {boolean}
	 * skipChkChildren ： 跳过子结点检查
	 */
	LAC.changeTreeNodeState = function(zTree, url, title, state,
			skipChkChildren) {
		var nodes = zTree.getSelectedNodes();
		var iduuids = {};
		if (!nodes && nodes.length > 1) {
			LAC.showError("请选择一个节点!");
			return;
		}
		if (!skipChkChildren) {
			for (var i = 0; i < nodes.length; i++) {
				var node = nodes[i];
				if (node.children && node.children.length > 0) {
					LAC.showError("该节点下有子节点，不能操作!");
					return;
				}
				iduuids[node.uuid] = node.id;
			}
		} else {
			for (var i = 0; i < nodes.length; i++) {
				var node = nodes[i]
				iduuids[node.uuid] = node.id;
			}
		}
		title = title || '确认要执行此操作吗？';
		LAC.confirm(title, function(yes) {
			if (yes) {
				var p = {
					contentType : "application/json; charset=utf-8",
					url : url,
					data : JSON2.stringify({
						idUuids : iduuids
					}),
					dataType : "json",
					success : function(result) {
						if (result.exception) {
							LAC.tip(result.exception.message, "error");
						} else if (result.redirectUrl) {
							window.top.location.href = result.redirectUrl;
						} else {
							for (var i = 0; i < nodes.length; i++) {
								var node = nodes[i];
								if (typeof state == 'object') {
									node = $.extend(node, state);
								} else {
									node.state = state;
								}
								zTree.updateNode(node);
							}
							LAC.tip('操作成功!', 'success');
						}
					},
					error : function(message) {
						LAC.tip('数据操作失败!<BR>错误信息：' + message, "error");
					}
				};
				LAC.ajax(p);
			}
		});
	}

	LAC.dealTreeNodeIcon = function(treeNodeList, iconBasePath) {
		if (treeNodeList && treeNodeList.length > 0 && iconBasePath
				&& iconBasePath.length > 0) {
			for (var i = 0; i < treeNodeList.length; i++) {
				var node = treeNodeList[i];
				if (node.attributes && node.attributes.alias) {
					node.icon = iconBasePath + node.attributes.alias + ".png";
				}
			}
		}
	}

	LAC.timestamp2URL = function(url) {
		url += (url.match(/\?/) ? "&" : "?") + "t=" + (new Date()).getTime();
		return url;
	};

	LAC.appendParam2URL = function(url, name, value) {
		url += (url.match(/\?/) ? "&" : "?") + name + "=" + value;
		return url;
	};

	LAC.randomId = function(name) {
		var id = name + (new Date()).getTime()
				+ parseInt(Math.random() * 100000);
		if ($("#" + id).length > 0) {
			return LAC.randomId(name);
		} else {
			return id;
		}
	}

	LAC.datetime2Str = function(dt, format) {
		if (dt && dt > 0) {
			var d = new Date(dt);
			var year = d.getFullYear();
			var month = d.getMonth() + 1;
			var day = d.getDate();
			var hour = d.getHours();
			var minute = d.getMinutes();
			var second = d.getSeconds();
			if (format == 'YYYY-MM-DD') {
				return year + '-' + (month < 10 ? ('0' + month) : month) + '-'
						+ (day < 10 ? ('0' + day) : day);
			}
			if (format == 'YYYY-MM-DD HH:MI') {
				return year + '-' + (month < 10 ? ('0' + month) : month) + '-'
						+ (day < 10 ? ('0' + day) : day) + ' '
						+ (hour < 10 ? ('0' + hour) : hour) + ':'
						+ (minute < 10 ? ('0' + minute) : minute);
			}
			if (format == 'MM-DD HH:MI:SS') {
				return (month < 10 ? ('0' + month) : month) + '-'
						+ (day < 10 ? ('0' + day) : day) + ' '
						+ (hour < 10 ? ('0' + hour) : hour) + ':'
						+ (minute < 10 ? ('0' + minute) : minute) + ':'
						+ (second < 10 ? ('0' + second) : second);
			}
			return year + '-' + (month < 10 ? ('0' + month) : month) + '-'
					+ (day < 10 ? ('0' + day) : day) + ' '
					+ (hour < 10 ? ('0' + hour) : hour) + ':'
					+ (minute < 10 ? ('0' + minute) : minute) + ':'
					+ (second < 10 ? ('0' + second) : second);
		}
		return '';
	}
	
	LAC.myBrowser = function (){
	    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	    var isOpera = userAgent.indexOf("Opera") > -1;
	    if (isOpera) {
	        return "Opera"
	    }; //判断是否Opera浏览器
	    if (userAgent.indexOf("Firefox") > -1) {
	        return "FF";
	    } //判断是否Firefox浏览器
	    if (userAgent.indexOf("Chrome") > -1){
		  return "Chrome";
		}
	    if (userAgent.indexOf("Safari") > -1) {
	        return "Safari";
	    } //判断是否Safari浏览器
	    if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
	       if (userAgent.indexOf("MSIE 6.0") > -1) { return "IE6"; }
	       if (userAgent.indexOf("MSIE 7.0") > -1) { return "IE7"; }
	       if (userAgent.indexOf("MSIE 8.0") > -1) { return "IE8"; }
	       if (userAgent.indexOf("MSIE 9.0") > -1) { return "IE9"; }
	       if (userAgent.indexOf("MSIE 10.0") > -1) { return "IE10"; }
	       return "IE";
	    } //判断是否IE6-9浏览器
	    if (userAgent.toLowerCase().indexOf("trident") > -1 && userAgent.indexOf("rv") > -1 && !isOpera) {
	       if (userAgent.indexOf("rv:10.0") > -1) { return "IE10"; }
	       if (userAgent.indexOf("rv:11.0") > -1) { return "IE11"; }
	       return "IE11";
	    } //判断是否IE10-11浏览器
	    else
	    {
	       return userAgent;
	    }
	}

	$.extend(
					$.fn,
					{
						mask : function(msg, maskDivClass) {
							this.unmask();
							// 参数
							var op = {
								opacity : 0.8,
								z : 10000,
								bgcolor : '#ccc'
							};
							if (!msg) {
								op.opacity = 0;
							}
							var original = $(document.body);
							var position = {
								top : 0,
								left : 0
							};
							if (this[0] && this[0] !== window.document) {
								original = this;
								position = original.position();
							}
							// 创建一个 Mask 层，追加到对象中
							var maskDiv = $('<div class="maskdivgen"> </div>');
							maskDiv.appendTo(original);
							/*
							 * var maskWidth=original.outerWidth();
							 * if(!maskWidth){ maskWidth=original.width(); } var
							 * maskHeight=original.outerHeight();
							 * if(!maskHeight){ maskHeight=original.height(); }
							 */
							var maskWidth = $(window).width(), maskHeight = $(
									window).height();
							maskDiv.css({
								position : 'absolute',
								top : position.top,
								left : position.left,
								'z-index' : op.z,
								width : maskWidth,
								height : maskHeight,
								'background-color' : op.bgcolor,
								opacity : 0
							});
							if (maskDivClass) {
								maskDiv.addClass(maskDivClass);
							}
							if (msg) {
								// var msgDiv=$('<div
								// style="position:absolute;border:#6593cf
								// 1px solid; padding:2px;background:#ccca"><div
								// style="line-height:24px;border:#a3bad9 1px
								// solid;background:white;padding:2px 10px 2px
								// 10px">'+msg+'</div></div>');
								var msgDiv = $('<div style="position:absolute; padding:2px;"><div class="loading-bg"></div><div style="background:white;float: left;height: 48px;line-height: 48px;padding: 0 10px;">'
										+ msg + '</div></div>');
								msgDiv.appendTo(maskDiv);
								var widthspace = (maskDiv.width() - msgDiv
										.width());
								var heightspace = (maskDiv.height() - msgDiv
										.height());
								msgDiv.css({
									cursor : 'wait',
									top : (heightspace / 2 - 2),
									left : (widthspace / 2 - 2)
								});
							}
							maskDiv.fadeIn('fast', function() {
								// 淡入淡出效果
								$(this).fadeTo('fast', op.opacity);
							})
							return maskDiv;
						},
						unmask : function() {
							var original = $(document.body);
							if (this[0] && this[0] !== window.document) {
								original = $(this[0]);
							}
							original.find("> div.maskdivgen").fadeOut('fast',
									0, function() {
										$(this).remove();
									});
						}
					});

})(jQuery);