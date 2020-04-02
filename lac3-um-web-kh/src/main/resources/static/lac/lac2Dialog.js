/*-
 * LAC2 Dialog 0.0.1
 * 
 * @Author zhoudong[ hzzdong@qq.com ] at  2018-02-18
 * 
 * 示例 ：var options = {
 * 			id : "1234567890",
 * 			cached : false,
 *			type : "common",
 *			sizeCss : "modal-lg",
 *          overflow : "scroll",
 *			title : "客户高级搜索",
 *			url : "xxxxxxxxxxxxxxxx",
 *			data : {},
 *			method : "get",
 *			modalArgs : [],
 *			onload : function(){
 *				XXXXX;
 *			},
 *			dialogEvents : [
 *				{
 *					name :  'hidden.bs.modal', event : function (e){ return false; }
 *				}
 *			],
 *			callback : function(){}
 *		};
 * 
 * 若是alert，全局创建一个dialog对象（id=LAC.GLOBAL_DIALOG_ALERT_ID）。每次调用的时候都找出全局alert对象，然后重新设置标题、内容、事件绑定等；
 * 若是model：
 * 		1）用户指定了id，且设置了dialogCached属性为true，则创建一个缓存的model，创建成功以后，每次调用直接找出缓存的model即可，不用重新设置标题、内容、事件绑定等；
 * 		2）用户没有指定id，全局创建一个model（id=LAC.GLOBAL_DIALOG_MODEL_ID），每次调用的时候都找出全局对象，然后重新设置标题、内容、事件绑定等；
 */
(function($) {

	window['Modals'] = new LacModals();

	Modals.template = '<div class="modal fade" id="{0}" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="Lable-{1}">'
			+ '<div class="modal-dialog" role="document">'
			+ '<div class="modal-content">'
			+ '<div class="modal-header">'
			+ '<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>'
			+ '<h4 class="modal-title" id="Lable-{2}">提示窗口</h4>'
			+ '</div>'
			+ '<div class="modal-body"></div>'
			+ '<div class="modal-footer"></div>'
			+ '</div>'
			+ '</div>'
			+ '</div>';

	function LacModals() {
		this.modals = {};
		this.addModal = function(modalId, modal, options) {
			this.modals[modalId] = new LacModal(modalId, modal, options);
		};
		this.getModal = function(id) {
			var modalId = (id && id != "") ? id : LAC.GLOBAL_DIALOG_MODEL_ID;
			return this.modals[modalId];
		};
		this.deleteModal = function(modalId) {
			var modal = this.modals[modalId];
			if (modal) {
				delete this.modals[modalId];
			}
		};
	}

	function LacModal(id, dlg, options) {
		this.id = id;
		this.nModal = dlg;
		this.options = options;
		this.show = function() {
			this.nModal.modal('show');
		};
		this.hide = function() {
			this.nModal.modal('hide');
		};
		this.getModalArgs = function() {
			return this.options.modalArgs;
		};
		this.getCallback = function() {
			return this.options.callback;
		};
	}
	
	Modals.getTopModals = function(){
		return window.parent ? window.top['Modals'] : window['Modals'];
	};

	$.LacDialog = function(options) {
		options = options || {};
		if (!options.id || options.id == "") {
			options.id = LAC.GLOBAL_DIALOG_MODEL_ID;// LAC.randomId("dlg");
		}

		var mainBody = window.parent ? $(window.top.document.body) : $(window.document.body);
		//var dlgFade = $("#" + options.id);
		var dlgFade = mainBody.find("#" + options.id);
		if (!dlgFade || dlgFade.length == 0) {
			dlgFadeTemplate = Modals.template.format(options.id, options.id, options.id);
			
			dlgFade = $(dlgFadeTemplate).appendTo(mainBody);
			// if (options.id == LAC.GLOBAL_DIALOG_ALERT_ID) {
			// dlgFade.css('z-index', 9999);
			// }
			dlgFade.css('z-index', LAC.getDialogZIndex());
			dlgFade.find(".modal-dialog").draggable({
				cursor : "move",
				handle : '.modal-header'
			});
			dlgFade.css("overflow", options.overflow ? options.overflow
					: "scroll");
		} else {
			dlgFade.css('z-index', LAC.getDialogZIndex());
			if (options.cached && options.id != LAC.GLOBAL_DIALOG_MODEL_ID
					&& options.id != LAC.GLOBAL_DIALOG_ALERT_ID) {
				Modals.getTopModals().getModal(options.id).options = options;

				// var oldZIdx = parseInt(dlgFade.css('z-index'));
				// oldZIdx++;
				// dlgFade.css('z-index', oldZIdx);

				return dlgFade;
			}
		}

		//var dlg = $("#" + options.id + " .modal-dialog");
		var dlg = dlgFade.find(".modal-dialog");
		/* 处理title参数设置 */
		//$("#Lable-" + options.id).html((options.title && options.title != '') ? options.title : "提示窗口");
		dlgFade.find("#Lable-" + options.id).html((options.title && options.title != '') ? options.title : "提示窗口");

		/* 处理content */
		//var dlgBody = $("#" + options.id + " .modal-body");
		var dlgBody = dlgFade.find(".modal-body");
		dlgBody.children().remove();
		if (options.content && options.content != "") {
			dlgBody.html(options.content);
			//var dlgFooter = $("#" + options.id + " .modal-footer");
			var dlgFooter = dlgFade.find(".modal-footer");
			dlgFooter.children().remove();
		} else if (options.url && options.url != "#") {
			LAC.ajax({
				url : options.url,
				type : (options.method && options.method == 'post') ? "post" : "get",
				data : options.data?JSON2.stringify(options.data):'',
				contentType : "application/json",
				dataType : "html",
				success : function(result) {
					dlgBody.html(result);

					//var dlgFooter = $("#" + options.id + " .modal-footer");
					var dlgFooter = dlgFade.find(".modal-footer");
					dlgFooter.children().remove();

					var boxFooter = dlgBody.find(".box-footer");
					if (boxFooter && boxFooter.length == 1) {
						boxFooter.children().appendTo(dlgFooter);
						boxFooter.remove();
					}
				}
			});
		}

		/* 处理 Alert buttons */
		if (options.id == LAC.GLOBAL_DIALOG_ALERT_ID) {
			//var dlgFooter = $("#" + options.id + " .modal-footer");
			var dlgFooter = dlgFade.find(".modal-footer");
			dlgFooter.children().remove();

			var dlgBtnClose = $('<button type="button" class="btn btn-outline pull-left btn-close" data-dismiss="modal">关闭</button>').appendTo(dlgFooter);
			if (options.callback) {
				dlgBtnClose.unbind();
				dlgBtnClose.on('click', function() {
					options.callback(false);
				});
			}

			if (options.type && options.type == "confirm") {// comfirn按钮
				var btnConfirm = $('<button type="button" class="btn btn-outline btn-primary" data-dismiss="modal">确定</button>').appendTo(dlgFooter);
				if (options.callback) {
					btnConfirm.unbind();
					btnConfirm.on('click', function() {
						options.callback(true);
					});
				}
			}

			if (options.type && options.type == 'common') {
				dlgFooter.find('button').removeClass('btn-outline');
			}
		}

		/* 处理type参数设置 */
		var typeCss;
		$.each(LAC.DIALOG_TYPES, function(k, v) {
			if (k == options.type) {
				typeCss = v.modalCss;
			}
			if (dlg.hasClass(v.modalCss)) {
				dlg.removeClass(v.modalCss);
			}
		});
		if (typeCss) {
			dlg.addClass(typeCss);
		}

		if (!options.sizeCss
				|| options.sizeCss == ""
				|| (options.sizeCss != "modal-sm"
						&& options.sizeCss != "modal-md"
						&& options.sizeCss != "modal-lg" && options.sizeCss != "modal-fs")) {
			options.sizeCss = "modal-sm";
		}

		if (options.sizeCss == "modal-sm") {
			if (dlg.hasClass("modal-md"))
				dlg.removeClass("modal-md");
			if (dlg.hasClass("modal-lg"))
				dlg.removeClass("modal-lg");
			if (dlg.hasClass("modal-fs"))
				dlg.removeClass("modal-fs");
			if (!dlg.hasClass("modal-sm"))
				dlg.addClass("modal-sm");
		} else if (options.sizeCss == "modal-md") {
			if (dlg.hasClass("modal-sm"))
				dlg.removeClass("modal-sm");
			if (dlg.hasClass("modal-lg"))
				dlg.removeClass("modal-lg");
			if (dlg.hasClass("modal-fs"))
				dlg.removeClass("modal-fs");
			if (!dlg.hasClass("modal-md"))
				dlg.addClass("modal-md");
		} else if (options.sizeCss == "modal-lg") {
			if (dlg.hasClass("modal-sm"))
				dlg.removeClass("modal-sm");
			if (dlg.hasClass("modal-md"))
				dlg.removeClass("modal-md");
			if (dlg.hasClass("modal-fs"))
				dlg.removeClass("modal-fs");
			if (!dlg.hasClass("modal-lg"))
				dlg.addClass("modal-lg");
		} else {
			if (dlg.hasClass("modal-sm"))
				dlg.removeClass("modal-sm");
			if (dlg.hasClass("modal-md"))
				dlg.removeClass("modal-md");
			if (dlg.hasClass("modal-lg"))
				dlg.removeClass("modal-lg");
			if (!dlg.hasClass("modal-fs"))
				dlg.addClass("modal-fs");
		}

		// console.log(LAC.DIALOG_TYPES.confirm.btnCss);
		dlgFade.unbind();
		if (options.dialogEvents) {
			options.dialogEvents.forEach(function(dlgEvent) {
				dlgFade.on(dlgEvent.name, function(e) {
					dlgEvent.event(e);
				});
			});
		}

		if (options.onload) {
			options.onload(options);
		}

		// dlgFade.options = options;
		// dlgFade.attr("options", options);
		//Modals.addModal(options.id, options);
		Modals.getTopModals().addModal(options.id, dlgFade, options);
		return dlgFade;
	};

	$.LacDialog.show = function(options) {
		var def = {
			id : LAC.GLOBAL_DIALOG_MODEL_ID,
			type : "common",
			sizeCss : "modal-lg"
		};
		return $.LacDialog($.extend(true, {}, def, options)).modal('show');
	};

	$.LacDialog.alert = function(content, title, type, callback) {
		content = content || "";
		if (typeof (title) == "function") {
			callback = title;
			type = null;
		} else if (typeof (type) == "function") {
			callback = type;
		}
		title = (typeof (title) == "string" && title != "") ? title : "提示信息";
		type = (typeof (type) == "string" && type != "") ? type : 'info';

		var dlg = $.LacDialog({
			id : LAC.GLOBAL_DIALOG_ALERT_ID,
			type : type,
			title : title,
			content : content,
			callback : callback
		});
		return dlg.modal('show');
	};

	$.LacDialog.confirm = function(content, title, onBtnClick) {
		return $.LacDialog.alert(content, title, 'confirm', onBtnClick);
	};

	$.LacDialog.success = function(content, title, onBtnClick) {
		return $.LacDialog.alert(content, title, 'success', onBtnClick);
	};

	$.LacDialog.info = function(content, title, onBtnClick) {
		return $.LacDialog.alert(content, title, 'info', onBtnClick);
	};

	$.LacDialog.warning = function(content, title, onBtnClick) {
		return $.LacDialog.alert(content, title, 'warning', onBtnClick);
	};

	$.LacDialog.error = function(content, title, onBtnClick) {
		return $.LacDialog.alert(content, title, 'error', onBtnClick);
	};

	$.LacDialog.tip = function(content, type) {
		var tipId = LAC.GLOBAL_DIALOG_TIP_ID;
		// console.log(options);
		var mainBody = window.parent ? $(window.top.document.body) : $(window.document.body);
		var box = mainBody.find("#" + tipId);
		if (!box || box.length == 0) {
			box = $('<div id="' + tipId + '" style="position:fixed; z-index:9999; width:240px; height:120px; bottom:2px; right:2px;" class="box box-warning box-solid"></div>').appendTo(mainBody);
			var boxHeader = $('<div class="box-header with-border"></div>').appendTo(box);
			var boxHeaderTitle = $('<h3 class="box-title">提示信息</h3>').appendTo(boxHeader);
			var boxHeaderTools = $('<div class="box-tools pull-right"></div>').appendTo(boxHeader);
			var boxHeaderToolsColse = $('<button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>').appendTo(boxHeaderTools);
			var boxBody = $('<div class="box-body"></div>').appendTo(box);
		}

		/*
		box.find(".btn-box-tool>i.fa-times").on("click", function(){
			//alert("hide");
			box.hide();
		});
		*/
		
		if (content && content != '') {
			box.find(".box-body").html(content);
		} else {
			box.find(".box-body").html(" ");
		}

		var typeCss = LAC.DIALOG_TYPES.info.tipCss;
		$.each(LAC.DIALOG_TYPES, function(k, v) {
			if (k == type) {
				typeCss = v.tipCss;
			}
			if (box.hasClass(v.tipCss)) {
				box.removeClass(v.tipCss);
			}
		});
		if (typeCss) {
			box.addClass(typeCss);
		}
		
		//box.hide();
		box.show();
		
		var win = window.parent ? window.top : window;
		var wintip = win['LAC'].createTipWin(tipId);
		wintip.bindColseEvent();
		win['LAC'].wintip = wintip;
/*
		box.setDialogContent = function(message) {
			box.find(".box-body").html(message);
		};
		box.setType = function(type) {
			var typeCss = LAC.DIALOG_TYPES.info.tipCss;
			$.each(LAC.DIALOG_TYPES, function(k, v) {
				if (k == type) {
					typeCss = v.tipCss;
				}
				if (box.hasClass(v.tipCss)) {
					box.removeClass(v.tipCss);
				}
			});
			if (typeCss) {
				box.addClass(typeCss);
			}
		};
		box.reset = function(content, type) {
			$(this).setDialogContent(content);
			$(this).setType(type);
		};
		*/
		return wintip;
	};
	
})(jQuery);