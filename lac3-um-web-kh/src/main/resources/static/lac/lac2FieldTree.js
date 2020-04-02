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
	
	function initMyZTree(myTreeId, myTree, options, treeData) {
		var treeObj = $.fn.zTree.init(myTree, options.setting, treeData);
		//console.log(treeObj);
    	if(options.expandAll){
    		treeObj.expandAll(true);
    	}
    	if(options.searchable){
    		var myTreeId = myTree.attr("id");
			fuzzySearch(myTreeId, '#input_tree_search_'+myTreeId, false, true); //初始化模糊搜索方法
		}
    	if (options.callback) {
			options.callback(myTreeId, options, treeData);
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
		
		if(!parent.hasClass("input-group")){
			parent.addClass("input-group");
		}
		var inputGroupAppend = $('<span class="input-group-btn"></span>');
		var selectBtn = $('<button id="btn-select-'+myId+'" type="button" class="btn btn-info btn-flat btn-field-tree-select">选择</button>').appendTo(inputGroupAppend);
		me.after(inputGroupAppend);
		
		//selectBtn.height(me.height());

		if(myIsDisabled){
			selectBtn.attr("disabled",true);
		}

		var myTreeId = myId + "-4-T_r_e_e";
		options.myId = myId;
		options.myTreeId = myTreeId;
		var treeContent = $('<div class="bg-gray color-palette div-field-tree-content" style="display:none; position: absolute;border: 1px solid #ced4da;"></div>').appendTo($("body"));
		
		if(options.searchable){
			var searchInput = $('<input type="text" id="input_tree_search_'+myTreeId+'" class="form-control form-search" placeholder="节点名称快速查找">').appendTo(treeContent);
			//fuzzySearch(myTreeId,'#input_tree_search_'+myTreeId,null,true); //初始化模糊搜索方法
			/*
			searchInput.bind('input propertychange',function () {
				var me = $(this);
		        var summary=me.val();
		        console.log(summary, summary.length);
		        if(summary.length >= 2 ){
		        	var myTreeId = me.attr("id").substr("input_tree_search_".length);
		        	//console.log(myTreeId);
		        	//var treeObj = $.fn.zTree.getZTreeObj(myTreeId);
		        	//var nodes = treeObj.getNodes();
		        	//treeObj.hideNodes(nodes);
		        }
		    });
		    */
		}
		
		var myTree = $('<ul id="'+myTreeId+'" class="ztree" style="margin-top:0; width:auto;"></ul>').appendTo(treeContent);
		treeContent.css('z-index', LAC.getDialogZIndex());
		
		if (options.url && options.url != "") {
			var initTreeOk = false;
			if (options.cacheEnabled) {
				var cachedData = LAC2_FieldTree_CACHE[options.url];
				if (cachedData) {
					setTimeout(function() {
						initMyZTree(myTreeId, myTree, options, cachedData);
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
			        success: function (ret) {
			        	if(ret && ret.code=="0"){
			        		var data = ret.data;
			        		if (options.cacheEnabled) {
				        		LAC2_FieldTree_CACHE[options.url] = data;
							}
							initMyZTree(myTreeId, myTree, options, data);
			        	} else {
			        		LAC.tip(ret.message || "系统出错啦！！！", "error");
			        	}
			        }
			    });
			}
		} else if (options.zNodes) {
			setTimeout(function() {
				initMyZTree(myTreeId, myTree, options, options.zNodes);
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