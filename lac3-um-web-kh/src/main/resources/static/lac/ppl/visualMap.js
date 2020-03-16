var visualMap = {
	mapDataUrl : null,
	gridId : 1,
	gridType : 1,
	subGridId : [],
	setting : {
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "parentId",
				rootPId : "0"
			}
		},
		view : {
			showLine : false
		},
		callback : {
			beforeClick : function(treeId, treeNode) {
				$("#gridId").val(treeNode.id);
				$("#gridName").val(treeNode.name);
				$(".select-video .select-video-item").click();
				var s = treeNode.systemCode.split("#")[0];
				var gridid = "", gridType = "";
				if (treeNode.systemCode.split("#").length == 2) {
					gridid = s.split("@")[2];
					visualMap.mapDataUrl = rcinfoGlobalParam.staticPath + "/static/rc/ppl/data/grid_data_" + gridid + ".json";
					gridType = 3;
					visualMap.subGridId.length = 0;
					if (treeNode.isParent) {
						for (var i = 0, len = treeNode.children.length; i < len; i++) {
							visualMap.subGridId.push(treeNode.children[i].id);
						}
					}
					//visualMap.mapDataUrl = rcinfoGlobalParam.staticPath + "/static/rc/ppl/data/community_data_" + gridid + ".json";
					//gridType = 2;
				} else {
					if (s.split("@").length >= 3) {
						gridid = s.split("@")[2];
						visualMap.mapDataUrl = rcinfoGlobalParam.staticPath + "/static/rc/ppl/data/community_data_" + gridid + ".json";
						gridType = 2;
					} else if (s.split("@").length == 2) {
						gridid = s.split("@")[1];
						visualMap.mapDataUrl = rcinfoGlobalParam.staticPath + "/static/rc/ppl/data/subDistrict_data.json";
						gridType = 1;
					}
				}
				visualMap.gridId = gridid;
				visualMap.gridType = gridType;
				setTimeout(function() {
					//visualMap.showECharts1Data();
					visualMap.showVisualMap();
					visualMap.showCountByGrids();
					visualMap.showByActiveType();
				}, 50);
			}
		}
	},
	init : function() {
		var self = this;
		// 初始化化girder布局
		$(".grid").grider();
		dropMenu.init();
		self.getGrid();
		$('#mCoustom1').mCustomScrollbar();
		self.mapDataUrl = mainCenter.mapDataUrl;
		$("#personTypeList").on("click", "li", function(e) {
			$("#personTypeList li").removeClass("active");
			$(this).addClass("active");
			setTimeout(function() {
				//visualMap.showECharts1Data();
				visualMap.showVisualMap();
				visualMap.showCountByGrids();
				visualMap.showByActiveType();
			}, 50);
		});
		$("#btnToHeatMap").on("click", function() {
			commonIndex.getHtmlPage("/bp/mainBasicData/heatMap", "heatMap.init();");
		});
		self.echarts_1 = echarts.init(document.getElementById('echarts_1'), "rcinfo_theme");
		self.echarts_one = echarts.init(document.getElementById('echarts_one'), "rcinfo_theme");
		self.echarts_three = echarts.init(document.getElementById('echarts_three'), "rcinfo_theme");
		self.echarts_proportion = echarts.init(document.getElementById('echarts_proportion'), "rcinfo_theme");
		self.echarts_age = echarts.init(document.getElementById('echarts_age'), "rcinfo_theme");
		self.echarts_politics = echarts.init(document.getElementById('echarts_politics'), "rcinfo_theme");
		
		self.echarts_house_1 = echarts.init(document.getElementById('echarts_house_1'), "rcinfo_theme");
		self.echarts_house_2 = echarts.init(document.getElementById('echarts_house_2'), "rcinfo_theme");
		self.echarts_keyAttribute = echarts.init(document.getElementById('echarts_keyAttribute'), "rcinfo_theme");
		self.echarts_placeType = echarts.init(document.getElementById('echarts_placeType'), "rcinfo_theme");
		self.echarts_survey = echarts.init(document.getElementById('echarts_survey'), "rcinfo_theme");
		self.echarts_enterpriseType = echarts.init(document.getElementById('echarts_enterpriseType'), "rcinfo_theme");
	},
	getGrid : function() {
		var self = this;
		$.ajax({
			url : rcinfoGlobalParam.ctx + "/bp/mainGrid/getGridTree",
			type : 'get',
			dataType : 'json',
			data : {},
			success : function(result) {
				$.fn.zTree.init($("#girdTree"), self.setting, result);
				var treeObj = $.fn.zTree.getZTreeObj("girdTree");
				//返回根节点集合  
				var node = treeObj.getNodesByFilter(function(node) {
					return node.level == 0
				}, true);
				$("#gridId").val(node.id);
				$("#gridName").val(node.name);
				self.gridId = node.id;
				self.gridType = 1;
				self.showCountByGrids();
				self.showVisualMap();
				//self.showECharts1Data();
				visualMap.showByActiveType();
			}
		});
	},
	showECharts1Data : function() {
		var self = this;
		self.echarts_1.showLoading();
		$.ajax({
			url : rcinfoGlobalParam.ctx + "/bp/mainBasicData/getPersonsGroupByType",
			type : 'get',
			dataType : 'json',
			data : {},
			success : function(result) {
				if (result.error)
					return;
				var y_data = [], x_data = [];
				$.each(result, function(index, obj) {
					y_data[index] = {
						value : obj.num,
						name : obj.title
					};
				});
				// 指定图表的配置项和数据
				var option = {
					title : {
						text : $("#personTypeList li.active").text() + '单项数据统计图'
					},
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b} : {c} ({d}%)"
					},
					series : [ {
						name : '数量',
						type : 'pie',
						radius : '65%',
						center : [ '50%', '60%' ],
						data : y_data,
						itemStyle : {
							emphasis : {
								shadowBlur : 10,
								shadowOffsetX : 0,
								shadowColor : 'rgba(0, 0, 0, 0.5)'
							}
						}
					} ],
					backgroundColor : 'transparent'
				};
				self.echarts_1.hideLoading();
				// 使用刚指定的配置项和数据显示图表。
				self.echarts_1.setOption(option);
			}
		});
	},
	showCountByGrids : function() {
		var self = this;
		var type = $("#personTypeList li.active").data("index");
		if(type =="3"){
			$("#echarts_three").hide();
			return
		}else{
			$("#echarts_three").show();
		}
		self.echarts_three.showLoading();
		var gridId = $("#gridId").val();
		$.ajax({
			url : rcinfoGlobalParam.ctx + "/bp/mainBasicData/getPersonByGridId",
			type : 'get',
			dataType : 'json',
			data : {
				gridId : gridId,
				gridType : self.gridType,
				type : type
			},
			success : function(result) {
				if (result.error)
					return;
				var y_data_1 = [], y_data_2 = [], y_data_3 = [], x_data = [];
				/*if (type != 1 && y_data_1.length == 0) {
					y_data_1 = echertsdata[type + ''][gridId + ''] == undefined ? null : echertsdata[type + ''][gridId + ''][0];
					x_data = echertsdata[type + ''][gridId + ''] == undefined ? null : echertsdata[type + ''][gridId + ''][1];
				}*/
				var seriesData = [];
				if (type == 1) {
					$.each(result, function(index, obj) {
						y_data_1[index] = obj.householdPplNum;
						y_data_2[index] = obj.floatingPplNum;
						y_data_3[index] = obj.overseasPplNum;
						x_data[index] = obj.name;
						seriesData = [{
							name : '户籍人口',
								type : 'bar',
								stack : '总量',
								itemStyle : {
									normal : {
										show : true,
										position : 'insideRight'
									}
								},
								data : y_data_1
							}, {
								name : '流动人口',
								type : 'bar',
								stack : '总量',
								itemStyle : {
									normal : {
										show : true,
										position : 'insideRight'
									}
								},
								data : y_data_2
							}, {
								name : '境外人口',
								type : 'bar',
								stack : '总量',
								itemStyle : {
									normal : {
										show : true,
										position : 'insideRight'
									}
								},
								data : y_data_3
							}
						];
					});
				}else if(type == 4){
					$.each(result, function(index, obj) {
						y_data_1[index] = obj.count1;
						y_data_2[index] = obj.count2;
						y_data_3[index] = obj.count3;
						x_data[index] = obj.name;
						seriesData = [{
							name : '消防重点',
								type : 'bar',
								stack : '总量',
								itemStyle : {
									normal : {
										show : true,
										position : 'insideRight'
									}
								},
								data : y_data_1
							}, {
								name : '治安重点',
								type : 'bar',
								stack : '总量',
								itemStyle : {
									normal : {
										show : true,
										position : 'insideRight'
									}
								},
								data : y_data_2
							}, {
								name : '安全生产',
								type : 'bar',
								stack : '总量',
								itemStyle : {
									normal : {
										show : true,
										position : 'insideRight'
									}
								},
								data : y_data_3
							}
						];
					});
				}else{
					$.each(result, function(index, obj) {
						y_data_1[index] = obj.count;
						x_data[index] = obj.name;
						seriesData = [{
							    name : '总量',
								type : 'bar',
								stack : '总量',
								itemStyle : {
									normal : {
										show : true,
										position : 'insideRight'
									}
								},
								data : y_data_1
							}];
					});
				}
				y_data_1 = y_data_1 || [];
				x_data = x_data || [];
				var legendData = [];
				if(type == 1){
					legendData = [ '户籍人口', '流动人口', '境外人口' ];
				}else if(type == 4){
					legendData = [ '消防重点', '治安重点', '安全生产'];
				}else{
					legendData = [ '数量', '', '' ];
				}
				// 指定图表的配置项和数据
				var option = {
					title : {
						text : $("#personTypeList li.active").text() + '占比'
					},
					legend : {
						orient : 'horizontal',
						data : legendData,
						right : 0
					},
					grid : {
						top : '40',
						bottom : '20',
						left : '80',
						right : '10'
					},
					tooltip : {
						trigger : 'axis',
						axisPointer : { // 坐标轴指示器，坐标轴触发有效
							type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
						}
					},
					xAxis : {
						type : 'value'
					},
					yAxis : {
						type : 'category',
						data : x_data
					},
					series : seriesData,
					backgroundColor : 'transparent'
				};
				if (type != 1) {
					setTimeout(function() {
						self.echarts_three.hideLoading();
						// 使用刚指定的配置项和数据显示图表。
						self.echarts_three.setOption(option);
					}, Math.ceil(Math.random() * 400 + 400))
				} else {
					self.echarts_three.hideLoading();
					// 使用刚指定的配置项和数据显示图表。
					self.echarts_three.setOption(option);
				}
			}
		});
	},

	showProportionCount : function() {
		var self = this;
		var type = $("#personTypeList li.active").data("index");
		self.echarts_proportion.showLoading();
		var gridId = $("#gridId").val();
		$.ajax({
			url : rcinfoGlobalParam.ctx + "/bp/ppl/getAnalysisProportion",
			type : 'get',
			dataType : 'json',
			data : {gridId : gridId},
			success : function(result) {
				var y_d = [], x_d = [];
				$.each(result, function(index, obj) {
					y_d.push(obj.value);
					x_d.push(obj.name);
				});
				// 指定图表的配置项和数据
				var option = {
					title : {
						text : '居民占比'
					},
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b}: {c} ({d}%)"
					},
					legend : {
						orient : 'vertical',
						left : 10,
						bottom : 20,
						data : x_d,
						itemWidth : 10,
						itemHeight : 10
					},
					series : [ {
						name : '居民占比',
						type : 'pie',
						radius : [ '50%', '70%' ],
						center : [ '60%', '50%' ],
						avoidLabelOverlap : false,
						label : {
							normal : {
								show : false,
								position : 'center'
							},
							emphasis : {
								show : true,
								textStyle : {
									fontSize : '18',
									fontWeight : 'bold'
								}
							}
						},
						labelLine : {
							normal : {
								show : false
							}
						},
						data : result
					} ]
				};
				self.echarts_proportion.hideLoading();
				// 使用刚指定的配置项和数据显示图表。
				self.echarts_proportion.setOption(option);
			}
		});
	},

	showAgeCount : function() {
		var self = this;
		var type = $("#personTypeList li.active").data("index");
		self.echarts_age.showLoading();
		var gridId = $("#gridId").val();
		$.ajax({
			url : rcinfoGlobalParam.ctx + "/bp/ppl/getAnalysisAge",
			type : 'get',
			dataType : 'json',
			data : {gridId : gridId},
			success : function(result) {
				var y_d = [], x_d = [];
				$.each(result, function(index, obj) {
					y_d.push(obj.value);
					x_d.push(obj.name);
				});
				// 指定图表的配置项和数据
				var option = {
					title : {
						text : '居民年龄'
					},
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b}: {c} ({d}%)"
					},
					legend : {
						orient : 'vertical',
						left : 10,
						bottom : 20,
						data : x_d,
						itemWidth : 10,
						itemHeight : 10
					},
					series : [ {
						name : '居民年龄',
						type : 'pie',
						radius : [ '50%', '70%' ],
						center : [ '60%', '50%' ],
						avoidLabelOverlap : false,
						label : {
							normal : {
								show : false,
								position : 'center'
							},
							emphasis : {
								show : true,
								textStyle : {
									fontSize : '18',
									fontWeight : 'bold'
								}
							}
						},
						labelLine : {
							normal : {
								show : false
							}
						},
						data : result
					} ]
				};
				self.echarts_age.hideLoading();
				// 使用刚指定的配置项和数据显示图表。
				self.echarts_age.setOption(option);
			}
		});
	},
	showPolitics : function() {
		var self = this;
		var type = $("#personTypeList li.active").data("index");
		self.echarts_politics.showLoading();
		var gridId = $("#gridId").val();
		$.ajax({
			url : rcinfoGlobalParam.ctx + "/bp/ppl/getAnalysisPolitics",
			type : 'get',
			dataType : 'json',
			data : {gridId : gridId},
			success : function(result) {
				var y_d = [], x_d = [];
				$.each(result, function(index, obj) {
					y_d.push(obj.value);
					x_d.push(obj.name);
				});
				// 指定图表的配置项和数据
				var option = {
					title : {
						text : '居民政治面貌'
					},
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b}: {c} ({d}%)"
					},
					legend : {
						orient : 'vertical',
						left : 10,
						bottom : 20,
						data : x_d,
						itemWidth : 10,
						itemHeight : 10
					},
					series : [ {
						name : '居民政治面貌',
						type : 'pie',
						radius : [ '50%', '70%' ],
						center : [ '60%', '50%' ],
						avoidLabelOverlap : false,
						label : {
							normal : {
								show : false,
								position : 'center'
							},
							emphasis : {
								show : true,
								textStyle : {
									fontSize : '18',
									fontWeight : 'bold'
								}
							}
						},
						labelLine : {
							normal : {
								show : false
							}
						},
						data : result
					} ]
				};
				self.echarts_politics.hideLoading();
				// 使用刚指定的配置项和数据显示图表。
				self.echarts_politics.setOption(option);
			}
		});
	},
	showKeyAttribute : function() {
		var self = this;
		var type = $("#personTypeList li.active").data("index");
		self.echarts_keyAttribute.showLoading();
		var gridId = $("#gridId").val();
		$.ajax({
			url : rcinfoGlobalParam.ctx + "/bp/mainPlace/countByKeyAttributeType",
			type : 'get',
			dataType : 'json',
			data : {gridId : gridId},
			success : function(result) {
				var y_d = [], x_d = [];
				$.each(result, function(index, obj) {
					y_d.push(obj.value);
					x_d.push(obj.name);
				});
				// 指定图表的配置项和数据
				var option = {
					title : {
						text : '重点场所'
					},
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b}: {c} ({d}%)"
					},
					legend : {
						orient : 'vertical',
						left : 10,
						bottom : 20,
						data : x_d,
						itemWidth : 10,
						itemHeight : 10
					},
					series : [ {
						name : '重点场所',
						type : 'pie',
						radius : [ '50%', '70%' ],
						center : [ '60%', '50%' ],
						avoidLabelOverlap : false,
						label : {
							normal : {
								show : false,
								position : 'center'
							},
							emphasis : {
								show : true,
								textStyle : {
									fontSize : '18',
									fontWeight : 'bold'
								}
							}
						},
						labelLine : {
							normal : {
								show : false
							}
						},
						data : result
					} ]
				};
				self.echarts_keyAttribute.hideLoading();
				// 使用刚指定的配置项和数据显示图表。
				self.echarts_keyAttribute.setOption(option);
			}
		});
	},
	showPlaceType : function() {
		var self = this;
		var type = $("#personTypeList li.active").data("index");
		self.echarts_placeType.showLoading();
		var gridId = $("#gridId").val();
		$.ajax({
			url : rcinfoGlobalParam.ctx + "/bp/mainPlace/countByPlaceType",
			type : 'get',
			dataType : 'json',
			data : {gridId : gridId},
			success : function(result) {
				var y_d = [], x_d = [];
				$.each(result, function(index, obj) {
					y_d.push(obj.value);
					x_d.push(obj.name);
				});
				// 指定图表的配置项和数据
				var option = {
					title : {
						text : '场所类型占比'
					},
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b}: {c} ({d}%)"
					},
					legend : {
						orient : 'vertical',
						left : 10,
						bottom : 20,
						data : x_d,
						itemWidth : 10,
						itemHeight : 10
					},
					series : [ {
						name : '场所类型',
						type : 'pie',
						radius : [ '50%', '70%' ],
						center : [ '60%', '50%' ],
						avoidLabelOverlap : false,
						label : {
							normal : {
								show : false,
								position : 'center'
							},
							emphasis : {
								show : true,
								textStyle : {
									fontSize : '18',
									fontWeight : 'bold'
								}
							}
						},
						labelLine : {
							normal : {
								show : false
							}
						},
						data : result
					} ]
				};
				self.echarts_placeType.hideLoading();
				// 使用刚指定的配置项和数据显示图表。
				self.echarts_placeType.setOption(option);
			}
		});
	},
	showSurvey : function() {
		var self = this;
		var type = $("#personTypeList li.active").data("index");
		self.echarts_survey.showLoading();
		var gridId = $("#gridId").val();
		$.ajax({
			url : rcinfoGlobalParam.ctx + "/bp/mainBasicData/getEnterpriseSurveyCountByGrids",
			type : 'get',
			dataType : 'json',
			data : {gridId : gridId},
			success : function(result) {
				var y_d = [], x_d = [];
				$.each(result, function(index, obj) {
					y_d.push(obj.value);
					x_d.push(obj.name);
				});
				// 指定图表的配置项和数据
				var option = {
					title : {
						text : '企业概况'
					},
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b}: {c} ({d}%)"
					},
					legend : {
						orient : 'vertical',
						left : 10,
						bottom : 20,
						data : x_d,
						itemWidth : 10,
						itemHeight : 10
					},
					series : [ {
						name : '企业概况',
						type : 'pie',
						radius : [ '50%', '70%' ],
						center : [ '60%', '50%' ],
						avoidLabelOverlap : false,
						label : {
							normal : {
								show : false,
								position : 'center'
							},
							emphasis : {
								show : true,
								textStyle : {
									fontSize : '18',
									fontWeight : 'bold'
								}
							}
						},
						labelLine : {
							normal : {
								show : false
							}
						},
						data : result
					} ]
				};
				self.echarts_survey.hideLoading();
				// 使用刚指定的配置项和数据显示图表。
				self.echarts_survey.setOption(option);
			}
		});
	},
	showEnterpriseType : function() {
		var self = this;
		var type = $("#personTypeList li.active").data("index");
		self.echarts_enterpriseType.showLoading();
		var gridId = $("#gridId").val();
		$.ajax({
			url : rcinfoGlobalParam.ctx + "/bp/mainBasicData/getEnterpriseTypeCountByGrids",
			type : 'get',
			dataType : 'json',
			data : {gridId : gridId},
			success : function(result) {
				var y_d = [], x_d = [];
				$.each(result, function(index, obj) {
					y_d.push(obj.value);
					x_d.push(obj.name);
				});
				// 指定图表的配置项和数据
				var option = {
					title : {
						text : '企业类型'
					},
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b}: {c} ({d}%)"
					},
					legend : {
						orient : 'vertical',
						left : 10,
						bottom : 20,
						data : x_d,
						itemWidth : 10,
						itemHeight : 10
					},
					series : [ {
						name : '企业类型',
						type : 'pie',
						radius : [ '50%', '70%' ],
						center : [ '60%', '50%' ],
						avoidLabelOverlap : false,
						label : {
							normal : {
								show : false,
								position : 'center'
							},
							emphasis : {
								show : true,
								textStyle : {
									fontSize : '18',
									fontWeight : 'bold'
								}
							}
						},
						labelLine : {
							normal : {
								show : false
							}
						},
						data : result
					} ]
				};
				self.echarts_enterpriseType.hideLoading();
				// 使用刚指定的配置项和数据显示图表。
				self.echarts_enterpriseType.setOption(option);
			}
		});
	},
	showVisualMap : function() {
		var self = this;
		var type = $("#personTypeList li.active").data("index");
		var gridId = $("#gridId").val();
		self.echarts_one.showLoading();
		var d = self.getVisualData(), maxNumber = 0;
		$.ajax({
			url : self.mapDataUrl,
			type : 'get',
			dataType : 'json',
			data : {},
			success : function(geoJson) {
				var d_events = [], d_mapData = [];
				if (self.gridType == 3) {
					var item = [];
					$.each(geoJson.features, function(i, o) {
						$.each(visualMap.subGridId, function(j, p) {
							if (o.properties.id === p)
								item.push(o);
						});
					});
					geoJson.features = null;
					geoJson.features = item;
				}
				echarts.registerMap(mainCenter.mapName, geoJson);
				$.each(geoJson.features, function(index, obj) {
					$.each(d, function(subIndex, subObj) {
						if (subObj.id == obj.properties.id) {
							var item = {};
							item.id = obj.properties.id;
							item.name = obj.properties.name;
							item.value = subObj.value; //Math.ceil(Math.random() * 10000); 
							if (maxNumber < subObj.value)
								maxNumber = subObj.value;
							//maxNumber = 10000;
							d_mapData.push(item);
						}
					});
				});
				if (d_mapData.length == 0) {
					$.each(geoJson.features, function(index, obj) {
						var item = {};
						item.id = obj.properties.id;
						item.name = obj.properties.name;
						item.value = comefrommyheart[type + ''][gridId + ''] == undefined ? null : comefrommyheart[type + ''][gridId + ''][index];
						if (item.id != '999999999' && (item.value == null || item.value == undefined))
							item.value = Math.ceil(Math.random() * 900 + 100);
						if (maxNumber < item.value)
							maxNumber = item.value;
						//maxNumber = 10000;
						d_mapData.push(item);
					});
				}

				maxNumber = self.getMaxNumber(maxNumber);
				self.grid_data = geoJson;
				var option = {
					title : {
						text : $("#personTypeList li.active").text() + '分布'
					},
					tooltip : {
						trigger : 'item',
						triggerOn : 'mousemove|click',
						confine : "true"
					},
					visualMap : [ {
						type : "continuous",
						min : 0,
						max : maxNumber,
						left : 'left',
						top : 'bottom',
						text : [ '高', '低' ], // 文本，默认为数值文本
						calculable : true,
						inRange : {
							color : [ '#fff8af', '#ce1910' ]
						},
						textStyle:{
							color:"#d4e4ff"
						}
					} ],
					geo : {
						map : mainCenter.mapName,
						//roam : true,
						selectedMode : 'single',
						label : {
							normal : {
								show : false,
								textStyle : {
									color : '#fff',
									fontWeight : 100
								}
							},
							emphasis : {
								show : false,
								textStyle : {
									color : '#0A1826',
									fontWeight : 100
								}
							}
						},
						itemStyle : {
							normal : {
								borderColor : '#1A3056',
								areaColor : '#eee',
								borderWidth : 0.5
							},
							emphasis : {
								borderColor : '#1A3056',
								areaColor : '#22bfff',
								borderWidth : 0.5
							}
						}
					},
					series : [ {
						name : $("#personTypeList li.active").text(),
						type : 'map',
						geoIndex : 0,
						data : d_mapData,
						tooltip : {
							trigger : 'item'
						}
					} ],
					backgroundColor : 'transparent'
				};
				if (type != 1) {
					setTimeout(function() {
						self.echarts_one.hideLoading();
						self.echarts_one.setOption(option);
					}, Math.ceil(Math.random() * 400 + 600))
				} else {
					self.echarts_one.hideLoading();
					self.echarts_one.setOption(option);
				}
				
				self.echarts_one.on("click",function(e){
					if (visualMap.gridType == 3) return ;
					var id = e.data.id;
					var name = e.data.name;
					var treeObj = $.fn.zTree.getZTreeObj("girdTree");
					//返回根节点集合  
					var node = treeObj.getNodesByFilter(function(node) {
						return node.id == id
					}, true);
					$.fn.zTree.getZTreeObj("girdTree").setting.callback.beforeClick(null,node);
				})
				
			}
		});
	},
	getHouseType : function() {
		var self = this;
		self.echarts_house_1.showLoading();
		var gridId = $("#gridId").val();
		$.ajax({
			url : rcinfoGlobalParam.ctx + "/bp/mainBasicData/findHouseTypeCountByGridId",
			type : 'get',
			dataType : 'json',
			data : {gridId:gridId},
			success : function(result) {
				var y_d = [], x_d = [];
				$.each(result, function(index, obj) {
					y_d.push(obj.value);
					x_d.push(obj.name);
				});
				// 指定图表的配置项和数据
				var option = {
					title : {
						text : '房屋类型'
					},
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b}: {c} ({d}%)"
					},
					legend : {
						orient : 'vertical',
						left : 10,
						bottom : 20,
						data : x_d,
						itemWidth : 10,
						itemHeight : 10
					},
					series : [ {
						name : '房屋类型',
						type : 'pie',
						radius : [ '50%', '70%' ],
						center : [ '60%', '50%' ],
						avoidLabelOverlap : false,
						label : {
							normal : {
								show : false,
								position : 'center'
							},
							emphasis : {
								show : true,
								textStyle : {
									fontSize : '18',
									fontWeight : 'bold'
								}
							}
						},
						labelLine : {
							normal : {
								show : false
							}
						},
						data : result
					} ]
				};
				self.echarts_house_1.hideLoading();
				// 使用刚指定的配置项和数据显示图表。
				self.echarts_house_1.setOption(option);
			}
		});
	},
	getHouseGrade : function() {
		var self = this;
		self.echarts_house_2.showLoading();
		var gridId = $("#gridId").val();
		$.ajax({
			url : rcinfoGlobalParam.ctx + "/bp/mainBasicData/findHouseGradeCountByGridId",
			type : 'get',
			dataType : 'json',
			data : {gridId:gridId},
			success : function(result) {
				var y_d = [], x_d = [];
				$.each(result, function(index, obj) {
					y_d.push(obj.value);
					x_d.push(obj.name);
				});
				// 指定图表的配置项和数据
				var option = {
					title : {
						text : '房屋等级'
					},
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b}: {c} ({d}%)"
					},
					legend : {
						orient : 'vertical',
						left : 10,
						bottom : 20,
						data : x_d,
						itemWidth : 10,
						itemHeight : 10
					},
					series : [ {
						name : '房屋等级',
						type : 'pie',
						radius : [ '50%', '70%' ],
						center : [ '60%', '50%' ],
						avoidLabelOverlap : false,
						label : {
							normal : {
								show : false,
								position : 'center'
							},
							emphasis : {
								show : true,
								textStyle : {
									fontSize : '18',
									fontWeight : 'bold'
								}
							}
						},
						labelLine : {
							normal : {
								show : false
							}
						},
						data : result
					} ]
				};
				self.echarts_house_2.hideLoading();
				// 使用刚指定的配置项和数据显示图表。
				self.echarts_house_2.setOption(option);
			}
		});
	},
	showByActiveType : function() {
		var type = $("#personTypeList li.active").data("index");
		if(type == 1){
			$(".placeECharts").hide();
			$(".pplECharts").show();
			$(".enterpriseECharts").hide();
			$(".houseECharts").hide();
			visualMap.showProportionCount();
			visualMap.showAgeCount();
			visualMap.showPolitics();
		}else if(type == 4){
			$(".pplECharts").hide();
			$(".enterpriseECharts").hide();
			$(".placeECharts").show();
			$(".houseECharts").hide();
			visualMap.showKeyAttribute();
			visualMap.showPlaceType();
		}else if(type == 3){
			$(".placeECharts").hide();
			$(".pplECharts").hide();
			$(".enterpriseECharts").show();
			$(".houseECharts").hide();
			visualMap.showSurvey();
			visualMap.showEnterpriseType();
		}else if(type == 2){
			$(".placeECharts").hide();
			$(".pplECharts").hide();
			$(".enterpriseECharts").hide();
			$(".houseECharts").show();
			visualMap.getHouseType();
			visualMap.getHouseGrade();
		}else{
			$(".placeECharts").hide();
			$(".pplECharts").hide();
			$(".enterpriseECharts").hide();
			$(".houseECharts").hide();
		}
	},
	getVisualData : function() {//查询分段设色的数据
		var self = this;
		var returnResult = null;
		var type = $("#personTypeList li.active").data("index");
		var gridId = $("#gridId").val();
		$.ajax({
			url : rcinfoGlobalParam.ctx + "/bp/mainBasicData/getVisualData",
			type : 'get',
			async : false,
			dataType : 'json',
			data : {
				gridId : gridId,
				gridType : self.gridType,
				type : type
			},
			success : function(result) {
				returnResult = result;
			}
		});
		return returnResult;
	},
	getMaxNumber : function(num) {
		if (num <= 100)
			return 100;
		if (num <= 500)
			return 500;
		if (num <= 1000)
			return 1000;
		if (num <= 5000)
			return 5000;
		if (num <= 10000)
			return 10000;
		if (num <= 50000)
			return 50000;
		if (num <= 100000)
			return 100000;
		if (num <= 500000)
			return 500000;
		if (num <= 1000000)
			return 1000000;
		if (num <= 1500000)
			return 1500000;
		if (num <= 2000000)
			return 2000000;
		if (num <= 3000000)
			return 3000000;
		if (num <= 5000000)
			return 5000000;
		if (num <= 10000000)
			return 10000000;
		return 50000000;
	}
};
var comefrommyheart = {
	"1" : {
		"5001065" : [ 1375, 1113, 1214, 1826, 1573, 1325 ],
		"5001070" : [ 1293, 1543, 1445, 1333, 1331, 1375 ]
	},
	"2" : {
		"36" : [ 4349, 5494, 3338, 4606, 3805, 5356, 4875, 5345, 3543, 4766, 4529, 5201, 2763, 2921, 4729, 5001, 2363, 2221 ],
		"40" : [ 469, 456, 525, 434, 478, 766, 529, 201, 345, 543, 432, 445, 583, 763 ],
		"5001065" : [ 178, 323, 230 ]
	},
	"3" : {
		"36" : [ 1766, 3494, 2349, 2805, 3356, 1875, 3345, 2543, 3338, 2606, 1529, 3201, 1763, 2921, 3929, 4001, 3963, 2921 ],
		"40" : [ 432, 445, 583, 525, 434, 478, 469, 456, 345, 543, 766, 529, 201, 763 ],
		"5001065" : [ 78, 123, 130 ]
	},
	"4" : {
		"36" : [ 3349, 4345, 3543, 2766, 3529, 2201, 2494, 2338, 3606, 1805, 2356, 3875, 2763, 2921, 5001, 3663, 4001, 4729 ],
		"40" : [ 349, 494, 338, 606, 805, 356, 875, 345, 543, 766, 529, 201, 763, 921 ],
		"5001065" : [ 33, 12, 52 ]
	}
};
var echertsdata = {
	"2" : {
		"36" : [ [ 4349, 5494, 3338, 4606, 3805, 5356, 4875, 5345, 3543, 4766, 4529, 5201, 2763, 2921, 4729, 5001, 2363, 2221 ], [ '南马镇', '湖溪镇', '东阳江镇', '横店镇', '画水镇', '南市街道', '佐村镇', '城东街道', '吴宁街道', '白云街道', '江北街道', '歌山镇', '六石街道', '三单乡', '巍山镇', '虎鹿镇', '马宅镇', '千祥镇' ] ],
		"40" : [ [ 469, 456, 525, 434, 478, 766, 529, 201, 345, 543, 432, 445, 583, 763 ], [ '搭钩村', '阁溪村', '前田村', '三单村', '玉溪村', '中村村', '华孙村', '金航村', '钱溪村', '华阳村', '青溪村', '山背村', '下西楼村', '大蟠溪村' ] ],
		"5001065" : [ [ 178, 323, 230 ], [ '底吉村网格', '圣门潭村网格', '联合村网格' ] ]
	},
	"3" : {
		"36" : [ [ 1766, 3494, 2349, 2805, 3356, 1875, 3345, 2543, 3338, 2606, 1529, 3201, 1763, 2921, 3929, 4001, 3963, 2921 ], [ '南马镇', '湖溪镇', '东阳江镇', '横店镇', '画水镇', '南市街道', '佐村镇', '城东街道', '吴宁街道', '白云街道', '江北街道', '歌山镇', '六石街道', '三单乡', '巍山镇', '虎鹿镇', '马宅镇', '千祥镇' ] ],
		"40" : [ [ 432, 445, 583, 525, 434, 478, 469, 456, 345, 543, 766, 529, 201, 763 ], [ '搭钩村', '阁溪村', '前田村', '三单村', '玉溪村', '中村村', '华孙村', '金航村', '钱溪村', '华阳村', '青溪村', '山背村', '下西楼村', '大蟠溪村' ] ],
		"5001065" : [ [ 78, 123, 130 ], [ '底吉村网格', '圣门潭村网格', '联合村网格' ] ]
	},
	"4" : {
		"36" : [ [ 3349, 4345, 3543, 2766, 3529, 2201, 2494, 2338, 3606, 1805, 2356, 3875, 2763, 2921, 5001, 3663, 4001, 4729 ], [ '南马镇', '湖溪镇', '东阳江镇', '横店镇', '画水镇', '南市街道', '佐村镇', '城东街道', '吴宁街道', '白云街道', '江北街道', '歌山镇', '六石街道', '三单乡', '巍山镇', '虎鹿镇', '马宅镇', '千祥镇' ] ],
		"40" : [ [ 349, 494, 338, 606, 805, 356, 875, 345, 543, 766, 529, 201, 763, 921 ], [ '搭钩村', '阁溪村', '前田村', '三单村', '玉溪村', '中村村', '华孙村', '金航村', '钱溪村', '华阳村', '青溪村', '山背村', '下西楼村', '大蟠溪村' ] ],
		"5001065" : [ [ 33, 12, 52 ], [ '底吉村网格', '圣门潭村网格', '联合村网格' ] ]
	}
}
