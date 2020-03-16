/**
 * LAC2 FieldTree 0.0.1
 * 
 * @Author zhoudong(hzzdong@qq.com) created at 2018-06-30
 * 
 */
(function($) {

	window['LAC2_FieldTree_CACHE'] = {};
	
	var defaultFieldTreeSetting = {
		setting : {
			data : {
				simpleData : {
					enable : true
				}
			}
		},
		expandAll : true,
		cacheEnabled : false
	};
	
	function initMyZTree(myTree, options, treeData) {
		var treeObj = $.fn.zTree.init(myTree, options.setting, treeData);
    	if(options.expandAll){
    		treeObj.expandAll(true);
    	}
    	if (options.callback) {
			options.callback(treeData);
		}
	};
	
	function hideMenu() {
		$(".div-field-tree-content").fadeOut("fast");
		$("body").unbind("mousedown", onBodyDown);
	}
	function onBodyDown(event) {
		var target = $(event.target);
		if (!(target.hasClass('btn-field-tree-select') || target.hasClass('div-field-tree-content') || target.parents(".div-field-tree-content").length>0)) {
			hideMenu();
		}
	}

	/*-
		{
			setting : {
				data: {
					simpleData: {
						enable: true
					}
				}
			},
			url : "${ctx}/bp/user/testTree",
			expandAll:true
			callback: function(){},
			cacheEnabled : false
		}
		为了提高用户体验，建议：(1)、input设置readonly，此时button可操作；(2)、禁用状态的时候，建议设置input的disabled，此时button不可操作。
	 */
	$.fn.lac2FieldTree = function(options) {
		var g = this, me = $(this);
		options = $.extend(true, {}, defaultFieldTreeSetting, options);

		var myId = me.attr("id");
		var parent = me.parent();
		
		var myIsDisabled = false;
		if(me.attr("disabled")==true || me.attr("disabled")=="disabled"){
			myIsDisabled = true;
		}
		
		var inputGroupAppend = $('<span class="input-group-append"></span>');
		var selectBtn = $('<button type="button" class="btn btn-info btn-flat btn-field-tree-select">选择</button>').appendTo(inputGroupAppend);
		me.after(inputGroupAppend);

		if(myIsDisabled){
			selectBtn.attr("disabled",true);
		}

		var myTreeId = myId + "-4-T_r_e_e";
		var treeContent = $('<div class="bg-gray color-palette div-field-tree-content" style="display:none; position: absolute;border: 1px solid #ced4da;"></div>').appendTo($("body"));
		var myTree = $('<ul id="'+myTreeId+'" class="ztree" style="margin-top:0; width:auto;"></ul>').appendTo(treeContent);
		treeContent.css('z-index', LAC.getDialogZIndex());
		
		if (options.url && options.url != "") {
			var initTreeOk = false;
			if (options.cacheEnabled) {
				var cachedData = LAC2_FieldTree_CACHE[options.url];
				if (cachedData) {
					setTimeout(function() {
						initMyZTree(myTree, options, cachedData);
					}, 30);
					initTreeOk = true;
				}
			}
			
			if(!initTreeOk){
				LAC.ajax({
			        contentType: "application/x-www-form-urlencoded",
			        url: options.url,
			        type: "GET",
			        dataType: 'json',
			        success: function (data) {
			        	if (options.cacheEnabled) {
			        		LAC2_FieldTree_CACHE[options.url] = data;
						}
						initMyZTree(myTree, options, data);
			        }
			    });
			}
		} else if (options.zNodes) {
			setTimeout(function() {
				initMyZTree(myTree, options, options.zNodes);
			}, 30);
		}
		
		selectBtn.on("click", function() {
			var myOffset = me.offset();
			treeContent.css({left:myOffset.left + "px", top:myOffset.top + me.outerHeight() + "px"}).slideDown("fast");
			$("body").bind("mousedown", onBodyDown);
		});

		return me;
	};

})(jQuery);