/**
 * Lac2 tab 0.0.1
 * 
 * @Author zhoudong(hzzdong@qq.com) created at 2018-06-17
 * 
 */
(function($) {

	window['LacTab'] = {};
	window['LacTabs'] = {};
	
	$(function() {
		$('#tab-menu').on('click', 'a', function() {
			var id = $(this).attr('data-pageid');
			changeTabs(id);
		})
	});

	/*判断tabs是否已经打开*/
	function findTabs(pageId) {
	    var $ele = null;
	    findEL('#tab-menu a').each(function () {
	        if($(this).attr('data-pageid') == pageId){
	            $ele = true;
	            return false;
	        }
	    })
	    return $ele;
	}
	//滚动到指定选项卡
	var scrollToTab = function (element) {
	    //element是tab(a标签),装有标题那个
	    //div.content-tabs > div.page-tabs-content
	    var marginLeftVal = calSumWidth($(element).prevAll()),//前面所有tab的总宽度
	        marginRightVal = calSumWidth($(element).nextAll());//后面所有tab的总宽度
	    //一些按钮(向左,向右滑动)的总宽度
	    var tabOuterWidth = calSumWidth(findEL(".content-tabs").children().not(".menuTabs"));
	    // tab(a标签)显示区域的总宽度
	    var visibleWidth = findEL(".content-tabs").outerWidth(true) - tabOuterWidth;
	    //将要滚动的长度
	    var scrollVal = 0;
	    if (findEL(".page-tabs-content").outerWidth() < visibleWidth) {
	        //所有的tab都可以显示的情况
	        scrollVal = 0;
	    } else if (marginRightVal <= (visibleWidth - $(element).outerWidth(true) - $(element).next().outerWidth(true))) {
	        //向右滚动
	        //marginRightVal(后面所有tab的总宽度)小于可视区域-(当前tab和下一个tab的宽度)
	        if ((visibleWidth - $(element).next().outerWidth(true)) > marginRightVal) {
	            scrollVal = marginLeftVal;
	            var tabElement = element;
	            while ((scrollVal - findEL(tabElement).outerWidth()) > (findEL(".page-tabs-content").outerWidth() - visibleWidth)) {
	                scrollVal -= findEL(tabElement).prev().outerWidth();
	                tabElement = findEL(tabElement).prev();
	            }
	        }
	    } else if (marginLeftVal > (visibleWidth - $(element).outerWidth(true) - $(element).prev().outerWidth(true))) {
	        //向左滚动
	        scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
	    }
	    //执行动画
	    findEL('.page-tabs-content').animate({
	        marginLeft: 0 - scrollVal + 'px'
	    }, "fast");
	};
	/*点击tabs*/
	function changeTabs(id){
	    findEL('#tab-menu a').removeClass('active');
	    var _this =  findEL('#tab-menu a[data-pageid='+id+']');
	    _this.addClass('active');
	    findEL('.tabs_box>div').removeClass('active');
	    findEL('#iframe_'+id).parent().addClass('active');
	    scrollToTab(_this);
	}
	
	function changeAndRefreshTab(options){
	    findEL('#tab-menu a').removeClass('active');
	    var _this =  findEL('#tab-menu a[data-pageid='+options.id+']');
	    _this.addClass('active');
	    findEL('.tabs_box>div').removeClass('active');
	    var thisIframe = findEL('#iframe_'+ options.id);
	    thisIframe.parent().addClass('active');
	    thisIframe.attr("src", options.url);
	    scrollToTab(_this);
	}
	
	LacTab.addTab = function(options){
		var options = $.extend(true, {}, { canClose:true }, options)
		if(findTabs(options.id)){
	        //changeTabs(options.id);
			changeAndRefreshTab(options);
	        return;
	    }
	    var tabTmp;
	    if(options.canClose){
	        tabTmp = '<a href="javascript:void(0);" data-pageid="'+options.id+'" class="menu_tab active canClose"><span class="page_tab_title">'+options.title+'</span><i class="fa fa-remove page_tab_close" style="cursor: pointer" onclick="LacTab.closeTab(this);" data-pageid="'+options.id+'"></i></a>';
	    		    
	        var $tabPanel = $('<div role="tabpanel" class="tab-pane active canClose"></div>');
	    }else{
	    	tabTmp = '<a href="javascript:void(0);" data-pageid="'+options.id+'" class="menu_tab active"><span class="page_tab_title">'+options.title+'</span></a>';
		    var $tabPanel = $('<div role="tabpanel" class="tab-pane active"></div>');
	    }
	    findEL('#tab-menu>.page-tabs-content a').removeClass('active');
	    findEL("#tab-menu>.page-tabs-content" ).append(tabTmp);

	    var $iframe = $("<iframe></iframe>").attr("src", options.url).addClass("tab_iframe").css({width: '100%', border:0}).attr("id", "iframe_" + options.id).attr('data-tabId',options.id);
	    
	    if(options.autoLink){
	    	$iframe.attr('data-link', LacTab.getTabId());
	    } else if(options.link){
	    	$iframe.attr('data-link', options.link);
	    }
	    $iframe.appendTo($tabPanel);
	    findEL('.tabs_box>div.tab-pane').removeClass('active');
	    $tabPanel.appendTo(findEL('.tabs_box'));
	    handleIframeContent()
	    scrollToTab(findEL('#tab-menu a.active'));
	    
	    getLacTabs()[options.id] = options;
	};

	var calSumWidth = function (element) {
	    var width = 0;
	    $(element).each(function () {
	        width += $(this).outerWidth(true);
	    });
	    return width;
	};


	// handle the layout reinitialization on window resize
	var handleOnResize = function () {
	    var resize;
	    $(window).resize(function () {
	        if (resize) {
	            clearTimeout(resize);
	        }
	        resize = setTimeout(function () {
	            handleIframeContent();
	        }, 100); // wait 50ms until window resize finishes.
	    });
	};
	function getViewPort() {
	    var e = window.parent,
	        a = 'inner';
	    if (!('innerWidth' in window.parent)) {
	        a = 'client';
	        e = window.parent.document.documentElement || window.parent.document.body;
	    }
	    return {
	        width: e[a + 'Width'],
	        height: e[a + 'Height']
	    };
	}

	/*获取顶层元素*/
	function findEL (el){
	    if($(el).length<1){
	        var body = $(window.parent.document.querySelector('body'));
	        return body.find(el)
	    } else {
	        return $(el)
	    }
	}
	
	LacTab.removeTab = function(tabId){
		var me = window['LacTab'];
		if(window.parent){
			me =  window.top['LacTab'];
		}
		me.removeTabFromCache(tabId);
	}
	
	LacTab.removeTabFromCache = function(tabId){
		var tab = getLacTabs()[tabId];
		if(tab){
			delete tab;
		}
	}
	
	LacTab.closeTab = function(ele){
		var id = $(ele).attr('data-pageid');
		var parent = $(ele).parent('a');
		if ($(ele).parent().hasClass('active')) {
			if (parent.next().length > 0) {
				parent.next().addClass('active');
				$("#iframe_" + id).parent().next().addClass('active');
			} else {
				parent.prev().addClass('active');
				$("#iframe_" + id).parent().prev().addClass('active');
			}
		}
		$("#iframe_" + id).parent().remove();
		parent.remove();
	};
	
	LacTab.callback = function(data){
		var tabId = self.frameElement.getAttribute('data-tabId');
	    var thisTab = getLacTab(tabId);
	    if(thisTab && thisTab.callback){
	    	thisTab.callback(data);
	    }
	};
	
	LacTab.closeThisTab = function(data){
		var prod = $(window.parent.document.querySelector('body'));
	    var fid= self.frameElement.getAttribute('data-link');
	    var tabId = self.frameElement.getAttribute('data-tabId');
	    var thisTab = getLacTab(tabId);
	    if(data && thisTab && thisTab.callback){
	    	thisTab.callback(data);
	    }
	    
	    if(thisTab){
	    	LacTab.removeTab(tabId);
	    }
	    
	    var currTab = prod.find('.page-tabs-content a[data-pageid="'+fid+'"]');
	    if(currTab.length>0){
	    	var thiss = prod.find('.active.canClose');
	        $(currTab).addClass('active');
	        prod.find('#iframe_'+fid).parent().addClass('active');
	        if(thiss){
	        	thiss.remove();
	        }
	    }else {
	        var prevTab = prod.find('.menu_tab.active.canClose').prev();
	        var nextTab = prod.find('.menu_tab.active.canClose').next();
	        var prevTabPane = prod.find('.tab-pane.active.canClose').prev();
	        var nextTabPane = prod.find('.tab-pane.active.canClose').next();
	        prod.find('.active.canClose').remove();
	        if(nextTab.length>0){
	            nextTab.addClass('active');
	            nextTabPane.addClass('active');
	        }else{
	            prevTab.addClass('active');
	            prevTabPane.addClass('active');
	        }
	    }
	};
	
	var getLacTab = function(tabId){
		var tabs = getLacTabs();
		if(tabs){
			return tabs[tabId];
		}
	};
	
	var getLacTabs = function(){
		if(window.parent){
			return window.top['LacTabs'];
		}else{
			return window['LacTabs'];
		}
	};
	
	LacTab.getTabId = function(){
		return self.frameElement.getAttribute('data-tabId');
	};

	LacTab.resize = function() {
		var resize;
		$(window).resize(function() {
			if (resize) {
				clearTimeout(resize);
			}
			resize = setTimeout(function() {
				handleIframeContent();
			}, 100);
		});
	};

	var handleIframeContent = function () {
	    var ht = $(window.parent).height();//获取浏览器窗口的整体高度；
	    var $header = findEL(".main-header");
	    var $tabs = findEL(".content-tabs");
	    var height = getViewPort().height - $header.outerHeight();
	    if ($tabs.is(":visible")) {
	        height = height - $tabs.outerHeight();
	    }
	    findEL(".tab_iframe").css({
	        height: height - 16,
	        width: "100%"
	    });
	};
	
	/* 刷新当前页 */
	LacTab.refreshTab = function() {
		$('.tab-pane.active').find('iframe').attr('src', $('.tab-pane.active').find('iframe').attr('src'));
	};
	
	/* 全部关闭和除此之外全部关闭 */
	LacTab.closeOtherTabs = function(flag) {
		if (flag) {
			$('.canClose').remove();
			$('#tab-menu a').addClass('active');
			$('.tabs_box>.tab-pane').addClass('active');
			$('.page-tabs-content').animate({
				marginLeft : 0 + 'px'
			}, "fast");
		} else {
			$('.canClose').not('.active').each(function() {
				$(this).remove();
			})
		}
	};
	
	var calSumWidth = function(element) {
		var width = 0;
		$(element).each(function() {
			width += $(this).outerWidth(true);
		});
		return width;
	};
	// 滚动条滚动到左边
	LacTab.scrollTabLeft = function() {
		var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css(
				'margin-left')));
		var tabOuterWidth = calSumWidth($(".content-tabs").children().not(
				".menuTabs"));
		var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
		var scrollVal = 0;
		if ($(".page-tabs-content").width() < visibleWidth) {
			return false;
		} else {
			var tabElement = $(".menu_tab:first");
			var offsetVal = 0;
			while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {
				offsetVal += $(tabElement).outerWidth(true);
				tabElement = $(tabElement).next();
			}
			offsetVal = 0;
			if (calSumWidth($(tabElement).prevAll()) > visibleWidth) {
				while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth)
						&& tabElement.length > 0) {
					offsetVal += $(tabElement).outerWidth(true);
					tabElement = $(tabElement).prev();
				}
				scrollVal = calSumWidth($(tabElement).prevAll());
			}
		}
		$('.page-tabs-content').animate({
			marginLeft : 0 - scrollVal + 'px'
		}, "fast");
	};
	// 滚动条滚动到右边
	LacTab.scrollTabRight = function() {
		var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css(
				'margin-left')));
		var tabOuterWidth = calSumWidth($(".content-tabs").children().not(
				".menuTabs"));
		var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
		var scrollVal = 0;
		if ($(".page-tabs-content").width() < visibleWidth) {
			return false;
		} else {
			var tabElement = $(".menu_tab:first");
			var offsetVal = 0;
			while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {
				offsetVal += $(tabElement).outerWidth(true);
				tabElement = $(tabElement).next();
			}
			offsetVal = 0;
			while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth)
					&& tabElement.length > 0) {
				offsetVal += $(tabElement).outerWidth(true);
				tabElement = $(tabElement).next();
			}
			scrollVal = calSumWidth($(tabElement).prevAll());
			if (scrollVal > 0) {
				$('.page-tabs-content').animate({
					marginLeft : 0 - scrollVal + 'px'
				}, "fast");
			}
		}
	};
	
	LacTab.isFullScreen = false;
	LacTab.handleFullScreen = function() {
		if (LacTab.isFullScreen) {
			LacTab.exitFull();
			LacTab.isFullScreen = false;
		} else {
			LacTab.requestFullScreen();
			LacTab.isFullScreen = true;
		}
	};
	
	LacTab.requestFullScreen = function() {
		var de = document.documentElement;
		if (de.requestFullscreen) {
			de.requestFullscreen();
		} else if (de.mozRequestFullScreen) {
			de.mozRequestFullScreen();
		} else if (de.webkitRequestFullScreen) {
			de.webkitRequestFullScreen();
		} else {
			alert({
				message : "该浏览器不支持全屏！",
				type : "danger"
			});
		}
	};
	
	// 退出全屏 判断浏览器种类
	LacTab.exitFull = function() {
		// 判断各种浏览器，找到正确的方法
		var exitMethod = document.exitFullscreen || // W3C
		document.mozCancelFullScreen || // Chrome等
		document.webkitExitFullscreen || // FireFox
		document.webkitExitFullscreen; // IE11
		if (exitMethod) {
			exitMethod.call(document);
		} else if (typeof window.ActiveXObject !== "undefined") {// for Internet
																	// Explorer
			var wscript = new ActiveXObject("WScript.Shell");
			if (wscript !== null) {
				wscript.SendKeys("{F11}");
			}
		}
	};

})(jQuery);