/**
 * LAC2 FieldTree 0.0.1
 * 
 * @Author zhoudong(hzzdong@qq.com) created at 2018-06-30
 * 
 */
(function($) {
	
	function initLac2Menu(content, menuTreeData){
		var navul = $('<ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false"></ul>').appendTo(content);
		if(menuTreeData && menuTreeData.length>0){
			for(var i=0; i<menuTreeData.length; i++){
				var itemData = menuTreeData[i];
				if(itemData.children && itemData.children.length>0){
					var li1 = $('<li class="nav-item has-treeview"></li>').appendTo(navul);
					var item1 = $('<a href="#" class="nav-link"> <i class="nav-icon fa fa-th-large"></i> <p> '+itemData.name+' <i class="right fa fa-angle-left"></i> </p> </a>').appendTo(li1);
					var ul = $('<ul class="nav nav-treeview"></ul>').appendTo(li1);
					for(var j=0; j<itemData.children.length; j++){
						var item2Data = itemData.children[j];
						var li2 = $('<li class="nav-item"></li>').appendTo(ul);
						var item2 = $('<a class="nav-link lac-tab" data-id="'+item2Data.attributes.resCode+'" data-title="'+item2Data.name+'" data-url="'+item2Data.attributes.url+'"> <i class="nav-icon fa fa-th-large"></i> <p> '+item2Data.name+' </p> </a>').appendTo(li2);
					
					}
				} else {
					var li1 = $('<li class="nav-item"></li>').appendTo(navul);
					var item1 = $('<a class="nav-link lac-tab" data-id="'+itemData.attributes.resCode+'" data-title="'+itemData.name+'" data-url="'+itemData.attributes.url+'"> <i class="nav-icon fa fa-th-large"></i> <p> '+itemData.name+' </p> </a>').appendTo(li1);
				}

			}
		}
	}

	$.fn.lac2Menu = function(url) {
		var g = this, me = $(this);
		LAC.ajax({
	        contentType: "application/x-www-form-urlencoded",
	        url: url,
	        type: "GET",
	        dataType: 'json',
	        success: function (data) {
	        	console.log(JSON2.stringify(data));
				initLac2Menu(me, data);
	        }
	    });
		return me;
	};

})(jQuery);