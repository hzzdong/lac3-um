/**
 * LAC TreeTable 0.0.1
 * 
 * @Author zhoudong(hzzdong@qq.com) created at 2018-06-27
 * 
 */
(function($) {
	
	window['LAC_TreeTable'] = {};
	

	function parseObjectValue(obj, fieldName) {
		if (!obj || !fieldName) {
			return '&nbsp;';
		}
		if (fieldName.indexOf('.') != -1) {
			var fields = fieldName.split(".");
			if (fields && fields.length > 0) {
				var ret = obj;
				for (var i = 0; i < fields.length; i++) {
					ret = ret[fields[i]];
					if (ret === undefined) {
						return '&nbsp;';
					}
				}
				return (ret === undefined) ? '&nbsp;' : ret;
			}
		} else {
			var ret = obj[fieldName];
			return (ret === undefined) ? '&nbsp;' : ret;
		}

	}
	
	function lacTreeTableHeader(options){
		options = options || {};
		if(!options.columns || options.columns.length<=0){
			return;
		}
		
		var li_head = '<li class="head"><a>';
		if(options.defaultColumnWidth){
			li_head += '<div class="diy" style="width:'+options.defaultColumnWidth+'">'+options.defaultColumnName+'</div>';
		} else {
			li_head += '<div class="diy">'+options.defaultColumnName+'</div>';
		}
		for(var i=0; i<options.columns.length; i++){
			var column = options.columns[i];
			if(column.width){
				li_head += '<div class="diy" style="width:'+column.width+'">'+column.fcName+'</div>';
				//li_head += '<div class="diy">'+column.fcName+'</div>';
			}else{
				li_head += '<div class="diy">'+column.fcName+'</div>';
			}
		}
		li_head += '</a></li>';
		return li_head;
	}
	
	LAC_TreeTable.updateTreeTableColumns = function (treeId, treeNode){
		var options = LAC_TreeTable[treeId] || {};
		if(options.columns && options.columns.length>0){
        	for(var i=0; i<options.columns.length; i++){
    			var column = options.columns[i];
    			var fv = '';
    			if(column.render){
    				fv = column.render(treeNode);
    			} else {
    				fv = parseObjectValue(treeNode, column.feName);
    			}
    			
    			var columnDivId=$('#LTT_'+treeNode.tId+'_'+treeNode.id+'_'+column.feName);
    			columnDivId.html(fv);
        	}
        }
	}
	
	var defaultOptions = {
			defaultColumnWidth: '20%',
			defaultColumnName: '名称',
			setting : {
			    view: {
			        showLine: false,
			        showIcon: true,
			        selectedMulti: false,
			        addDiyDom: function (treeId, treeNode) {
			            var spaceWidth = 15;
			            var liObj = $("#" + treeNode.tId);
			            var aObj = $("#" + treeNode.tId + "_a");
			            var switchObj = $("#" + treeNode.tId + "_switch");
			            var icoObj = $("#" + treeNode.tId + "_ico");
			            var spanObj = $("#" + treeNode.tId + "_span");
			            aObj.attr('title', treeNode.name);
			            var options = LAC_TreeTable[treeId] || {};
			            if(options.defaultColumnWidth){
			    			aObj.append('<div class="diy swich" style="width:'+options.defaultColumnWidth+'"></div>');
			    		} else {
			    			aObj.append('<div class="diy swich"></div>');
			    		}
			            var div = $(liObj).find('div').eq(0);
			            switchObj.remove();
			            spanObj.remove();
			            icoObj.remove();
			            div.append(switchObj);
			            div.append(icoObj);
			            div.append(spanObj)
			            var spaceStr = "<span style='height:1px;display: inline-block;width:" + (spaceWidth * treeNode.level) + "px'></span>";
			            switchObj.before(spaceStr);
			            
			            var editStr = '';
			            if(options.columns && options.columns.length>0){
			            	for(var i=0; i<options.columns.length; i++){
			        			var column = options.columns[i];
			        			var fv = '';
			        			if(column.render){
			        				fv = column.render(treeNode);
			        			} else {
			        				fv = parseObjectValue(treeNode, column.feName);
			        			}
			        			if(column.width){
			        				editStr += '<div id="LTT_'+treeNode.tId+'_'+treeNode.id+'_'+column.feName+'" class="diy" style="width:'+column.width+'">'+ fv +'</div>';
			        			}else{
			        				editStr += '<div id="LTT_'+treeNode.tId+'_'+treeNode.id+'" class="diy">'+ fv +'</div>';
			        			}
			            	}
			            }
			            aObj.append(editStr);
			        }
			    },
			    data: {
			        simpleData: {
			            enable: true
			        }
			    }
			}
	};

	/*-
	 * url,
	 * setting,
	 * defaultColumnWidth: '20%',
	 * expandAll: false,
	 * callback : function(){},
	 * columns : [
	 * 		{
	 * 			"feName" : "name",
	 * 			"fcName" : "名称",
	 * 			"width" : "20%",
	 * 			"render" : function(treeNode) {
	 * 				return '<i class="text-info btn-app-edit" style="cursor:pointer">' + data + '</i>';
	 * 			}
	 * 		}
	 * ]
	 */
	$.fn.treeTable = function(options) {
		var g = this, my = $(this);
		options = $.extend(true, {}, defaultOptions, options);
		LAC_TreeTable[my.attr("id")] = options;

		LAC.ajax({
	        contentType: "application/x-www-form-urlencoded",
	        url: options.url,
	        type: "GET",
	        dataType: 'json',
	        success: function (ret) {
	        	//console.log(ret);
	        	if(ret && ret.code=="0"){
	        		var data = ret.data;
	        		if(options.iconBasePath && options.iconBasePath.length>0){
		        		LAC.dealTreeNodeIcon(data, options.iconBasePath);
		        	}
		        	var treeObj = $.fn.zTree.init(my, options.setting, data);
		            var li_head = lacTreeTableHeader(options);
		            var rows = my.find('li');
		            if (rows.length > 0) {
		                rows.eq(0).before(li_head)
		            } else {
		            	my.append(li_head);
		            	my.append('<li ><div style="text-align: center;line-height: 30px;" >无符合条件数据</div></li>')
		            }
		            
		            if(options.expandAll){
		            	treeObj.expandAll(true);
		            }
		            if(options.callback){
		            	options.callback(data);
		            }
	        	} else {
	        		LAC.tip(ret.message || "系统出错啦！！！", "error");
	        	}
	        }
	    });

		return my;
	};

})(jQuery);