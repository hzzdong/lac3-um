$(function() {
	var t = setTimeout(function() {
		addTabs({
			id : '000',
			title : '首页',
			url : '/yc/welcome',
			unClose : 'true'
		});
		handleOnResize();
		clearTimeout(t);
	}, 100);
	$('#tab-menu').on('click', 'a', function() {
		var id = $(this).attr('data-pageid');
		changeTabs(id);
	})
});

/* 判断tabs是否已经打开 */
function findTabs(pageId) {
	var $ele = null;
	$('#tab-menu a').each(function() {
		if ($(this).attr('data-pageid') == pageId) {
			$ele = true;
			return false;
		}
	})
	return $ele;
}
// 滚动到指定选项卡
var scrollToTab = function(element) {
	// element是tab(a标签),装有标题那个
	// div.content-tabs > div.page-tabs-content
	var marginLeftVal = calSumWidth($(element).prevAll()), // 前面所有tab的总宽度
	marginRightVal = calSumWidth($(element).nextAll());// 后面所有tab的总宽度
	// 一些按钮(向左,向右滑动)的总宽度
	var tabOuterWidth = calSumWidth($(".content-tabs").children().not(
			".menuTabs"));
	// tab(a标签)显示区域的总宽度
	var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
	// 将要滚动的长度
	var scrollVal = 0;
	if ($(".page-tabs-content").outerWidth() < visibleWidth) {
		// 所有的tab都可以显示的情况
		scrollVal = 0;
	} else if (marginRightVal <= (visibleWidth - $(element).outerWidth(true) - $(
			element).next().outerWidth(true))) {
		// 向右滚动
		// marginRightVal(后面所有tab的总宽度)小于可视区域-(当前tab和下一个tab的宽度)
		if ((visibleWidth - $(element).next().outerWidth(true)) > marginRightVal) {
			scrollVal = marginLeftVal;
			var tabElement = element;
			while ((scrollVal - $(tabElement).outerWidth()) > ($(
					".page-tabs-content").outerWidth() - visibleWidth)) {
				scrollVal -= $(tabElement).prev().outerWidth();
				tabElement = $(tabElement).prev();
			}
		}
	} else if (marginLeftVal > (visibleWidth - $(element).outerWidth(true) - $(
			element).prev().outerWidth(true))) {
		// 向左滚动
		scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
	}
	// 执行动画
	$('.page-tabs-content').animate({
		marginLeft : 0 - scrollVal + 'px'
	}, "fast");
};
/* 点击tabs */
function changeTabs(id) {
	$('#tab-menu a').removeClass('active');
	var _this = $('#tab-menu a[data-pageid=' + id + ']');
	_this.addClass('active');
	$('.tabs_box>div').removeClass('active');
	$('#iframe_' + id).parent().addClass('active');
	scrollToTab(_this);
}
/* 点击侧边栏 */
function addTabs(options) {
	var options = options || {
		id : '000',
		title : '首页',
		url : '/yc/Application/list',
		unClose : 'true'
	};
	if (findTabs(options.id)) {
		changeTabs(options.id);
		return;
	}
	var tabTmp;
	if (options.unClose) {
		tabTmp = `<a href="javascript:void(0);" data-pageid="${options.id}" class="menu_tab active"><span class="page_tab_title">${options.title}</span></a>`
		var $tabPanel = $('<div role="tabpanel" class="tab-pane active"></div>');

	} else {
		tabTmp = `<a href="javascript:void(0);" data-pageid="${options.id}" class="menu_tab active canClose"><span class="page_tab_title">${options.title}</span><i class="fa fa-remove page_tab_close" style="cursor: pointer" onclick="closeTab(this);" data-pageid="${options.id}"></i></a>`
		var $tabPanel = $('<div role="tabpanel" class="tab-pane active canClose"></div>');
	}
	$('#tab-menu>.page-tabs-content a').removeClass('active');
	$('#tab-menu>.page-tabs-content').append(tabTmp);

	var $iframe = $("<iframe></iframe>").attr("src", options.url).addClass("tab_iframe").css({width: '100%', border:0}).attr("id","iframe_" + options.id)
	$iframe.appendTo($tabPanel);
	$('.tabs_box>div.tab-pane').removeClass('active');
	$tabPanel.appendTo('.tabs_box');
	handleIframeContent()
	scrollToTab($('#tab-menu a.active'));
}
/* 关闭tabs */
function closeTab(obj) {
	var id = $(obj).attr('data-pageid');
	var parent = $(obj).parent('a');
	if ($(obj).parent().hasClass('active')) {
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
}
/* 刷新当前页 */
function refreshTab() {
	$('.tab-pane.active').find('iframe').attr('src',
			$('.tab-pane.active').find('iframe').attr('src'));
}

/* 全部关闭和除此之外全部关闭 */
function closeOtherTabs(flag) {
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
}
var calSumWidth = function(element) {
	var width = 0;
	$(element).each(function() {
		width += $(this).outerWidth(true);
	});
	return width;
};
// 滚动条滚动到左边
var scrollTabLeft = function() {
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
var scrollTabRight = function() {
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
var isFullScreen = false;
function handleFullScreen() {
	if (isFullScreen) {
		exitFull();
		isFullScreen = false;
	} else {
		requestFullScreen();
		isFullScreen = true;
	}
}
var requestFullScreen = function() {
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
var exitFull = function() {
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
// handle the layout reinitialization on window resize
var handleOnResize = function() {
	var resize;
	$(window).resize(function() {
		if (resize) {
			clearTimeout(resize);
		}
		resize = setTimeout(function() {
			handleIframeContent();
		}, 100); // wait 50ms until window resize finishes.
	});
};
function getViewPort() {
	var e = window, a = 'inner';
	if (!('innerWidth' in window)) {
		a = 'client';
		e = document.documentElement || document.body;
	}
	return {
		width : e[a + 'Width'],
		height : e[a + 'Height']
	};
}
var handleIframeContent = function() {
	var ht = $(window).height();// 获取浏览器窗口的整体高度；
	//var $footer = $(".main-footer");
	var $header = $(".main-header");
	var $tabs = $(".content-tabs");
	var height = getViewPort().height - $header.outerHeight();//- $footer.outerHeight()
	if ($tabs.is(":visible")) {
		height = height - $tabs.outerHeight();
	}
	$(".tab_iframe").css({
		height : height - 10,
		width : "100%"
	});
};