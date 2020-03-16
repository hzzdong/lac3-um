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
/*点击侧边栏*/
function addTabs(options) {
    var options = options ||{
        id:'000',
        title:'首页',
        url:'./pages/Ui/buttons-ifrmae.html',
        unClose:'true'
    }
    if(findTabs(options.id)){
        changeTabs(options.id);
        return;
    }
    var tabTmp;
    if(options.unClose){
        tabTmp = `<a href="javascript:void(0);" data-pageid="${options.id}" class="menu_tab active"><span class="page_tab_title">${options.title}</span></a>`
        var $tabPanel = $('<div role="tabpanel" class="tab-pane active"></div>');

    }else{
        tabTmp = `<a href="javascript:void(0);" data-pageid="${options.id}" class="menu_tab active canClose"><span class="page_tab_title">${options.title}</span><i class="fa fa-remove page_tab_close" style="cursor: pointer" onclick="closeTab(this);" data-pageid="${options.id}"></i></a>`
        var $tabPanel = $('<div role="tabpanel" class="tab-pane active canClose"></div>');
    }
    findEL('#tab-menu>.page-tabs-content a').removeClass('active');
    findEL("#tab-menu>.page-tabs-content" ).append(tabTmp);

    var $iframe = $("<iframe></iframe>").attr("src", options.url).addClass("tab_iframe").css({width: '100%', border:0}).attr("id", "iframe_" + options.id).attr('data-link',options.link)
    $iframe.appendTo($tabPanel);
    findEL('.tabs_box>div.tab-pane').removeClass('active');
    $tabPanel.appendTo(findEL('.tabs_box'));
    handleIframeContent()
    scrollToTab(findEL('#tab-menu a.active'));
    
    $tabPanel.on("click", function() {
    	alert("click");
    });
    
    $tabPanel.on("closeLacTab", function() {
    	alert("closeLacTab");
    });

}



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
var handleIframeContent = function () {
    var ht = $(window.parent).height();//获取浏览器窗口的整体高度；
    var $footer = findEL(".main-footer");
    var $header = findEL(".main-header");
    var $tabs = findEL(".content-tabs");
    var height = getViewPort().height - $header.outerHeight();
    if ($tabs.is(":visible")) {
        height = height - $tabs.outerHeight();
    }
    findEL(".tab_iframe").css({
        height: height - 10,
        width: "100%"
    });

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
/*每个页面的关闭按钮
* 如果父页的tab没关，跳刀父页，否则向前或向后跳一个
* */
function closeThisPage () {
    var prod = $(window.parent.document.querySelector('body'));
    var fid= self.frameElement.getAttribute('data-link');
    var currTab = prod.find('.page-tabs-content a[data-pageid="'+fid+'"]');
    var prepareRemoveTab = prod.find('.tab-pane.active.canClose');
    prepareRemoveTab.trigger("click");
    prepareRemoveTab.triggerHandler("click");
    prepareRemoveTab.click();
    prepareRemoveTab.triggerHandler("closeLacTab");
    prepareRemoveTab.trigger("closeLacTab");
    prepareRemoveTab.closeLacTab();
    /*
    if(currTab.length>0){
    	prod.find('.active.canClose').remove();
        currTab.addClass('active');
        prod.find('#iframe_'+fid).parent().addClass('active');
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
    */
}