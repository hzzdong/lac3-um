/**
 * RC Components 0.0.1
 * 
 * @Author zhoudong(hzzdong@qq.com) created at 2017-05-12
 * 
 */
(function($) {

	window['RC_COMPONENTS_CACHE'] = {};

	function initMyZTree(myInput, zTreeId, options, treeData) {
		$.fn.zTree.init($("#" + zTreeId), options.setting, treeData);
		if(options.setting.callback && options.setting.callback.complete){
			options.setting.callback.complete(zTreeId);
		}
		var parent = myInput.parent();
		var fieldW = parent.width();
		var popover = parent.find(".popover");
		var popoverW = popover.width();
		if (fieldW > popoverW) {
			popover.width(fieldW);
		}
		popover.css("left", "0px");
	};
	
	function showTreeNodeWithParents(treeObj, node){
		if(node && treeObj){
			treeObj.showNode(node);
			var parentNode = node.getParentNode();
			if(parentNode){
				showTreeNodeWithParents(treeObj, parentNode);
			}
		}
	}
	
	function search4ztree(treeId){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var nodes = treeObj.transformToArray(treeObj.getNodes());
		if(nodes && nodes.length>0){
			var searchValue = $("#"+treeId+"_search_input").val();
			if(searchValue &&　searchValue != ''){
				treeObj.hideNodes(nodes);
				
				var searchNodeName = treeObj.setting.searchNodeName || "name";
				var snodes = treeObj.getNodesByParamFuzzy(searchNodeName, searchValue);
				if(snodes && snodes.length>0){
					for (var i=0; i<snodes.length; i++){
						var snode = snodes[i];
						for (var j=0; j<nodes.length; j++){
							var node = nodes[j];
							if(node.name==snode.name){
								showTreeNodeWithParents(treeObj, node);
							}
						}
					}
				}
				treeObj.expandAll(true);
			} else {
				treeObj.showNodes(nodes);
			}
		}
	}

	/*-
		{
			setting : {
				view : {
					dblClickExpand : false
				},
				data : {
					simpleData : {
						enable : true
					}
				},
				callback : {
					
				},
				searchNodeName : "name" //ztree中要搜索的字段名称，默认：name
			},
			url : "${ctx}/bp/user/testTree",
			urlDataCached : false
		}
		为了提高用户体验，建议：(1)、input设置readonly，此时button可操作；(2)、禁用状态的时候，建议设置input的disabled，此时button不可操作。
	 */
	$.fn.tree4Input = function(options) {
		var g = this, my = $(this);
		options = options || {};

		var myId = my.attr("id");
		var parent = my.parent();
		
		var myIsDisabled = false;
		if(my.attr("disabled")==true || my.attr("disabled")=="disabled"){
			myIsDisabled = true;
		}

		my.remove();
		var inputGroup = $('<div class="input-group"></div>').appendTo(parent);
		my.appendTo(inputGroup);
		var inputGroupBtn = $('<span class="input-group-btn"></div>').appendTo(
				inputGroup);
		var btn = $(
				'<button type="button" class="btn btn-info btn-flat">选择</button>')
				.appendTo(inputGroupBtn);
		if(myIsDisabled){
			btn.attr("disabled",true);
		}

		if (!my.hasClass("myPopover")) {
			my.addClass("myPopover");
		}
		if (!btn.hasClass("myPopoverBtn")) {
			btn.addClass("myPopoverBtn");
		}

		var myTreeId = myId + "-4-T_r_e_e";
		my
				.popover({
					trigger : 'manual',
					placement : 'bottom', // top, bottom, left or right
					html : 'true',
					// trigger : 'click',
					title : function(){
						return '<div class="input-group"><input type="text" class="form-control i-group" id="'+myTreeId+'_search_input" placeholder="节点名称模糊匹配" ><span class="input-group-btn"><a href="#" class="btn btn-info btn-flat" id="'+myTreeId+'_search_button"><i class="fa fa-search"></i></a></span></div>';
					},
					content : function() {
						return '<ul id="'
								+ myTreeId
								+ '" class="ztree" style="margin-top:0; width:100%;"></ul>';
					}
				});

		my.on('inserted.bs.popover', function() {
			$("#"+myTreeId+"_search_button").on("click",function(event){
				search4ztree(myTreeId);
			});
			$("#"+myTreeId+"_search_input").bind("propertychange",function(event){
				search4ztree(myTreeId);
			}).bind("input", function(){
				search4ztree(myTreeId);
			});
			
			if (options.url && options.url != "") {
				if (options.urlDataCached) {
					var cd = RC_COMPONENTS_CACHE[options.url];
					if (cd) {
						setTimeout(function() {
							initMyZTree(my, myTreeId, options, cd);
						}, 30);
						return;
					}
				}
				
				RC.ajax({
					url : options.url,
					type : options.type ? options.type : "GET",
					success : function(data) {
						//alert(JSON2.stringify(data));
						if (options.urlDataCached) {
							RC_COMPONENTS_CACHE[options.url] = data;
						}
						initMyZTree(my, myTreeId, options, data);
					}
				});
			} else if (options.zNodes) {
				setTimeout(function() {
					initMyZTree(my, myTreeId, options, options.zNodes);
				}, 30);
			}

		});

		my.on("click", function(event) {
			if($(this).attr("disabled")==true || $(this).attr("disabled")=="disabled"){
				//
			} else {
				// $(".popover").popover('hide');
				$(".myPopover").popover('hide');
				$(this).popover('toggle');
			}
		});
		
		btn.on("click", function() {
			var myInput = $(this).parent().parent().find(".myPopover");
			if(myInput){
				if(myInput.attr("disabled")==true || myInput.attr("disabled")=="disabled"){
					//
				} else {
					$(".myPopover").popover('hide');
					myInput.popover('toggle');
				}
			}
		});

		$('body').on('click',function(event) {
							var target = $(event.target);
					if (!target.hasClass('popover')
							&& !target.hasClass('myPopover')
							&& !target.hasClass('myPopoverBtn')
							&& !target.hasClass('popover-content')
							&& !target.hasClass('popover-title')
							&& !target.hasClass('arrow')
							&& target.parents('.popover-content').length === 0
							&& target.parents('.popover-title').length === 0) {
						// $(".popover").popover('hide');
						$(".myPopover").popover('hide');
						// my.validationEngine('validate');
					}
				});
		
//		console.log($("#"+myTreeId+"_search_button"));
		
//		$("#"+myTreeId+"_search_button").on("click",function(event){
//			alert(111);
//			var treeObj = $.fn.zTree.getZTreeObj(myTreeId);
//			var searchValue = $("#"+myTreeId+"_search_input").val();
//			var nodes = treeObj.getNodesByParamFuzzy("name", searchValue, null);
//			alert(JSON2.stringify(nodes));
//		});
		
		if (options.callback) {
			options.callback(my);
		}

		return my;
	};

})(jQuery);