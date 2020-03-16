/**
 * LAC2 DataTables 0.0.1
 * 
 * @Author zhoudong(hzzdong@qq.com) created at 2017-05-06
 * 
 */
(function($) {
	
	window['LACDT'] = {};
	
	LACDT.autoHideColumns = true;
	
	/**
	 * 美化datatable:处理datatable头上的行选中checkbox，处理自动隐藏显示datatable的列
	 * 
	 * @param dt
	 *            datatable
	 * @param autoColumns
	 *            自动隐藏显示的列的id数组
	 * @param showColumns
	 *            始终显示的列的id数组
	 */
	LACDT.niceDataTable = function(dt) {
		LACDT.niceDataTableCheckAll(dt);
		LACDT.niceDataTableCheckChildren(dt);
		// LACDT.niceDataTableColumns(dt);
	};
	
	/**
	 * 处理datatable头上的全选checkbox
	 * 
	 * @param dt
	 */
	LACDT.niceDataTableCheckAll = function(dt) {
		var dtId = dt.table().node().id;
		var dotCheckallClass = '#'+dtId +' .dtCheckall';
		$(dotCheckallClass).iCheck({
			checkboxClass : 'icheckbox_square-blue',
			radioClass : 'iradio_square-blue',
			increaseArea : '20%' // optional
		});
		
		$(dotCheckallClass).on('ifChecked', function(e) {
			e.preventDefault();
			dt.$('tr').addClass('selected');
			dt.$(".dtCheckchild").iCheck('check');
		});
		$(dotCheckallClass).on('ifUnchecked', function(e) {
			e.preventDefault();
			dt.$('tr.selected').removeClass('selected');
			dt.$(".dtCheckchild").iCheck('uncheck');
		});
	};
	
	/**
	 * 处理datatable的行checkbox
	 * 
	 * @param dt
	 */
	LACDT.niceDataTableCheckChildren = function(dt) {
		if(!dt){
			return;
		}
		
		dt.$(".dtCheckchild").iCheck({
			checkboxClass : 'icheckbox_square-blue',
			radioClass : 'iradio_square-blue',
			increaseArea : '20%' // optional
		});
		
		dt.$('.dtCheckchild').on('ifClicked', function(e) {
			e.preventDefault();
			if ($(this).is(':checked')) {
				$(this).parents("tr").removeClass('selected');
				$(this).iCheck('uncheck');
		    } else {
		    	$(this).parents("tr").addClass('selected');
		    	$(this).iCheck('check');
		    }
		});
		
		dt.$('tr').on('click', function() {
			if ($(this).hasClass('selected')) {
				$(this).removeClass('selected');
				$(this).find(".dtCheckchild").iCheck('uncheck');
			} else {
				dt.$('tr.selected').removeClass('selected');
				$(this).addClass('selected');
				dt.$('.dtCheckchild').iCheck('uncheck');
				$(this).find(".dtCheckchild").iCheck('check');
			}
		});
		
	};
	
	/**
	 * 自动隐藏显示datatable的列
	 * 
	 * @param dt
	 */
	LACDT.niceDataTableColumns = function(dt) {
		if(dt && dt.settings() && dt.settings().init()){
			var oInits = dt.settings().init();
			var autoColumns = oInits.autoColumns || [];
			var alwayShowColumns = oInits.showColumns || [];
			if(LACDT.autoHideColumns == true){
				if (alwayShowColumns && alwayShowColumns.length > 0) {
					for (var k = 0, l = alwayShowColumns.length; k < l; k++) {
						dt.column(alwayShowColumns[k]).visible(true);
					}
				}
				if (autoColumns && autoColumns.length > 0) {
					var winW = $(window).width();
					var displayMe = winW > 767 ? true : false;
					for (var k = 0, l = autoColumns.length; k < l; k++) {
						dt.column(autoColumns[k]).visible(displayMe);
					}
				}
			}
		}
	};
	
	LACDT.smartTableWidth = function(setting) {
		var tableId = setting.nTable.id;
		if(!setting || !setting.oInit || !tableId){
			return;
		}

		// 容器宽度
		var parentWitdh = $("#"+setting.nTable.id+"_wrapper").parent().width();
		if(!parentWitdh || parentWitdh<=0){// 不可见
			var modalDlg = $("#"+setting.nTable.id).parents('.modal-dialog');
			if(modalDlg && modalDlg.length==1){
				if(modalDlg.hasClass('modal-sm')){
					parentWitdh = 270;
				} else if(modalDlg.hasClass('modal-md')){
					parentWitdh = 570;
				} else if(modalDlg.hasClass('modal-lg')){
					parentWitdh = 870;
				} else {// fs
					parentWitdh = $("#content").width() - 50;
				}
			}else{// 不是model
				parentWitdh = $("#content").width() - 50;
			}
		} else {
			parentWitdh = parentWitdh - 30;
		}
		
		// 用户设置的期望宽度
		var expectCustomWidth = setting.oInit.smartTableWidth;
		var expectWidth = (!expectCustomWidth || expectCustomWidth=="") ? 0 : getWidthHeightInteger(expectCustomWidth);
		
		// 用户的列上设置的宽度和
		var customWitdh = 0;
		var columns = setting.oInit.columns;
		if(columns && columns.length>0){
			for(var i=0; i<columns.length; i++){
				var column = columns[i];
				if(column.width && column.width!=""){
					if(column.visible == false){
						//
					}else{
						var columnWidth = getWidthHeightInteger(column.width);
						if(columnWidth == 0){
							columnWidth = 100;
						}
						customWitdh = customWitdh + columnWidth;
					}
				}
			}
		}
		
		//console.log(setting.nTable.id+" > parentWitdh:"+parentWitdh+", expectWidth:"+expectWidth+", customWitdh:"+customWitdh);
		
		if(expectWidth <= 0 && customWitdh <=22){// 都没有设置，只能100%，自动宽度
			$("#"+tableId+"_wrapper").css({"position":"","clear":"","zoom":1,"overflow-x":"hidden"});
			$("#"+tableId).css({"table-layout":"","width":"100%"});
			return;
		}
		
		var perfectWidth = (expectWidth > customWitdh) ? expectWidth : customWitdh;
		if(parentWitdh > perfectWidth){
			// $("#"+tableId).css({"table-layout":"","width": parentWitdh+"px"});
			$("#"+tableId).css({"table-layout":"","width": "100%"});
			$("#"+tableId+"_wrapper").css({"position":"","clear":"","zoom":1,"overflow-x":"hidden"});
		}else{
			$("#"+tableId+"_wrapper table").css({"width": perfectWidth+"px"});
		}
		
	};
	
	LACDT.getDefaultPage = function(){
		return new Page(0,10,false);  
	};
	
	LACDT.getPage = function(start, length, forceCount){
		return new Page(start, length, forceCount);  
	};
	
	/**
	 * 分页查询page对象
	 * 
	 * @param start
	 * @param length
	 * @param forceCount
	 * @returns
	 */
	function Page(start, length, forceCount){
		this.start = start;
		this.length = length;
		this.recordsTotal = 0;
		this.recordsFiltered = 0;
		this.forceCount = forceCount;
		this.searchCount = true;
		this.pageNo = function(){
			return this.start / this.length + 1;
		};
		this.hasNextPage = function(){
			return ( Math.ceil( this.recordsTotal / this.length ) > ( this.start / this.length + 1) ) ? true : false;
		};
		this.next = function(){
			if(this.forceCount){
				this.searchCount = true;
			} else {
				this.searchCount = false;
			}
			if(this.hasNextPage()){
				this.start += this.length;
			}
		};
	}

	/*-
	 * datatable初始化
	 * 
	 * {
	 *		order : [ [ 5, "desc" ] ], 		//默认排序
	 *		url : "${ctx}/bp/user/page",	//ajax分页数据加载的url
	 *		autoCheckBox : true,			//表格的第一列是否自动显示checkbox
	 *		//注意：若autoCheckBox==true，则表格第一列自动会生成checkbox，所以你定义的列就是第一列，注意索引下标
	 *		autoColumns : [ 2, 3, 5 ],		//自动显示/隐藏的列
	 *		showColumns : [ 1, 4 ],			//始终显示的列
	 *		columns : [
	 *		 	{							//列的声明
	 *				"data" : "mobile",
	 *				"defaultContent" : "",
	 *				"render" : function(data, type, row) {
	 *					return '<a class="btn-user-edit" href="#">'
	 *												+ data + '</a>';
	 *				}
	 *			},
	 *			{},							//其它列的声明
	 *		],
	 *		initComplete : function(setting, json) {} //表格初始化完成后的回调事件注册，你也可以注册其它回调事件
	 * }
	 */
	$.fn.LacDTable = function(options) {
		var g = this;
		options = options || {};
		var defaultOptions = {
			processing : true,
			serverSide : true,
			searching : false,
			bLengthChange: false,
			rowId : "id",
			bRetrieve: true,
			ajax : function(data, callback, oSettings) {
				var pData = {
					mapUnderscoreToCamelCase: false,
					cnds : []
				};
				
				if (oSettings.oInit.mapUnderscoreToCamelCase == true) {
					pData.mapUnderscoreToCamelCase = true;
				}
				
				if (oSettings.oInit.cnds) {
					if ($.isFunction(oSettings.oInit.cnds)) {
						pData.cnds = oSettings.oInit.cnds();
					} else if (Object.prototype.toString.call(oSettings.oInit.cnds) === "[object String]") {
						pData.cnds = JSON.parse(oSettings.oInit.cnds);
					} else {
						pData.cnds = oSettings.oInit.cnds;
					}
				}
				
				$.extend(pData, data)
				for(var i=0; i<oSettings.oInit.columns.length; i++){
					var column = oSettings.oInit.columns[i];
					if(column.alias && column.alias!=''){
						pData.columns[i].alias = column.alias;
					}
				}
				
				LAC.ajax({
					url : oSettings.oInit.url,
					contentType : "application/json",
					dataType : 'json',
					type : "POST",
					cache : false,
					data : JSON2.stringify(pData),
					success : function(result) {
						callback(result);
					}
				});
			},
			/*
			initComplete : function(setting, json) {
				$("#"+setting.nTable.id+"_filter").children().remove();
				
				LACDT.smartTableWidth(setting);
				$("#"+setting.nTable.id+"_wrapper").resize(
						function() {
							LACDT.smartTableWidth(setting);
						});
			},
			*/
			initComplete : function(setting, json) {
				//
			},
			drawCallback : function(setting) {
				LACDT.niceDataTable(new $.fn.DataTable.Api(setting.nTable));
			},
			language : {
				processing : "正在加载数据...",
				search : "检索:",
				lengthMenu : "每页显示 _MENU_ 条记录",
				info : "当前第 _START_ - _END_ 条　共计 _TOTAL_ 条",
				infoEmpty : "没有数据",
				infoFiltered : "(从 _MAX_ 条记录中过滤)",
				infoPostFix : "",
				loadingRecords : "加载中...",
				zeroRecords : "没有数据",
				emptyTable : "没有数据",
				paginate : {
					first : "首页",
					previous : "前页",
					next : "后页",
					last : "尾页"
				},
				aria : {
					sortAscending : ": 以升序排列此列",
					sortDescending : ": 以降序排列此列"
				}
			}
		};
		// console.log(options);
		
		if(options.autoCheckBox){
			$(this).find("thead th:first-child").before($('<th><input type="checkbox" class="dtCheckall toggle-vis-dt" /></th>'));
			$(this).find("tfoot th:first-child").before($('<th><input type="checkbox" class="dtCheckall toggle-vis-dt" /></th>'));
			options.columns = [{
				"data" : null,
				"width" : "22px",
				"sClass" : "text-center",
				"orderable" : false,
				"searchable" : false,
				"render" : function(data, type, row) {
					return '<input type="checkbox"  class="dtCheckchild toggle-vis-dt"/>';
				}
			}].concat(options.columns);
		}
		
		if(options.autoSequence){
			$(this).find("thead th:first-child").before($('<th></th>'));
			$(this).find("tfoot th:first-child").before($('<th></th>'));
			options.columns = [{
	            "searchable": false,
	            "orderable": false,
	            "data" : null,
	            "sClass" : "text-center",
	            "width" : "10px"
	        }].concat(options.columns);
		}
		
		if (options.initComplete) {
			options.realInitComplete = new CompositeMethods();
			options.realInitComplete.addMethod(defaultOptions.initComplete,
					options.initComplete);
			options.initComplete = function(setting, json) {
				setting.oInit.realInitComplete.excute(setting, json);
			}
		}
		
		if (options.drawCallback) {
			options.realDrawCallback = new CompositeMethods();
			options.realDrawCallback.addMethod(defaultOptions.drawCallback,
					options.drawCallback);
			options.drawCallback = function(setting, json) {
				setting.oInit.realDrawCallback.excute(setting, json);
			}
		}
		
		// 用户设置的期望宽度
		if(options.smartTableWidth && options.smartTableWidth!=''){
			$(this).attr("width", options.smartTableWidth);
		}
		
		var dtable = g.DataTable($.extend(true, {}, defaultOptions, options));
		if(options.autoSequence){
			dtable.on('order.dt search.dt draw.dt', function() {
				var oTable = $(g).dataTable();
				var tableSetings=oTable.fnSettings(); 
				var page_start=tableSetings._iDisplayStart;
				//console.log(page_start);
				dtable.column(0, {
					search : 'applied',
					order : 'applied'
				}).nodes().each(function(cell, i) {
					cell.innerHTML = i + 1 + page_start;
				});
			}).draw();
		}
		return dtable;
	};

	$.fn.buildPager = function(options) {
		var g = this;
		options = options || {};
		var total = options.total;
		var limit = options.limit;
		var pageNo = options.pageNo;
		if (total > limit) {
			var pageTotal = Math.ceil(total / limit);
			var prev = '<li><a href="javascript:;">&laquo;</a></li>';
			if (pageNo > 1) {
				prev = '<li><a href="javascript:loadPage(' + (pageNo - 1)
						+ ');">&laquo;</a></li>';
			}
			var next = '<li><a href="javascript:;">&raquo;</a></li>';
			if (pageNo < pageTotal) {
				next = '<li><a href="javascript:loadPage(' + (pageNo + 1)
						+ ');">&raquo;</a></li>';
			}

			var html = '';
			if (pageTotal <= 3) {
				for (var i = 1; i <= pageTotal; i++) {
					if (i == pageNo) {
						html += '<li><a style="background-color:#eee;" href="javascript:;">'
								+ i + '</a></li>';
					} else {
						html += '<li><a href="javascript:loadPage(' + i
								+ ');">' + i + '</a></li>';
					}
				}
			} else {
				if (pageNo == 1) {
					html += '<li><a style="background-color:#eee;" href="javascript:;">1</a></li>';
					html += '<li><a href="javascript:loadPage(2);">2</a></li>';
					html += '<li><a href="javascript:loadPage(3);">3</a></li>';
				} else if (pageNo == pageTotal) {
					html += '<li><a href="javascript:loadPage('
							+ (pageTotal - 2) + ');">' + (pageTotal - 2)
							+ '</a></li>';
					html += '<li><a href="javascript:loadPage('
							+ (pageTotal - 1) + ');">' + (pageTotal - 1)
							+ '</a></li>';
					html += '<li><a style="background-color:#eee;" href="javascript:;">'
							+ pageTotal + '</a></li>';
				} else {
					html += '<li><a href="javascript:loadPage(' + (pageNo - 1)
							+ ');">' + (pageNo - 1) + '</a></li>';
					html += '<li><a style="background-color:#eee;" href="javascript:;">'
							+ pageNo + '</a></li>';
					html += '<li><a href="javascript:loadPage(' + (pageNo + 1)
							+ ');">' + (pageNo + 1) + '</a></li>';
				}
			}
			$(g).append(prev + html + next);
		}
	};

	function CompositeMethods() {
		this.methods = [];
		this.addMethod = function() {
			for (var i = 0, n = arguments.length; i < n; i++) {
				this.methods.push(arguments[i]);
			}
		}
		this.excute = function() {
			if (this.methods.length > 0) {
				for (var i = 0; i < this.methods.length; i++) {
					this.methods[i].apply(null, arguments);
				}

			}
		};
	}

})(jQuery);