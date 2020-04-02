/**
 * 否是用移动设备浏览
 * 
 * @return {}
 */
function isMobile() {
	return navigator.userAgent.match(/mobile/i) ? true : false;
}

function diffTime(startDate, endDate) {
	var diff = endDate.getTime() - startDate.getTime();// 时间差的毫秒数
	// 计算出相差天数
	var days = Math.floor(diff / (24 * 3600 * 1000));
	// 计算出小时数
	var leave1 = diff % (24 * 3600 * 1000); // 计算天数后剩余的毫秒数
	var hours = Math.floor(leave1 / (3600 * 1000));
	// 计算相差分钟数
	var leave2 = leave1 % (3600 * 1000); // 计算小时数后剩余的毫秒数
	var minutes = Math.floor(leave2 / (60 * 1000));

	// 计算相差秒数
	var leave3 = leave2 % (60 * 1000); // 计算分钟数后剩余的毫秒数
	var seconds = Math.round(leave3 / 1000);

	var returnStr = seconds + "秒";
	if (minutes > 0) {
		returnStr = minutes + "分" + returnStr;
	}
	if (hours > 0) {
		returnStr = hours + "小时" + returnStr;
	}
	if (days > 0) {
		returnStr = days + "天" + returnStr;
	}
	return returnStr;
}

// 判断数组中是否包含某字符串
Array.prototype.contains = function(needle) {
	for (i in this) {
		if (this[i].indexOf(needle) > 0)
			return i;
	}
	return -1;
}

/**
 * 
 * @param {}
 *            str
 * @return {}
 */
function toUtf8(str) {
	var out, i, len, c;
	out = "";
	len = str.length;
	for (i = 0; i < len; i++) {
		c = str.charCodeAt(i);
		if ((c >= 0x0001) && (c <= 0x007F)) {
			out += str.charAt(i);
		} else if (c > 0x07FF) {
			out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
			out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
			out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
		} else {
			out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
			out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
		}
	}
	return out;
}

/**
 * 
 * @param file
 * @returns
 */
function getImageWidthHeight(file, callBack) {
	var imgWH = {
		width : 0,
		height : 0
	};
	if (!file) {
		return imgWH;
	}
	if (file.files && file.files[0]) {
		var fileData = file.files[0];

		var reader = new FileReader();
		reader.onload = function(e) {
			var data = e.target.result;
			// 加载图片获取图片真实宽度和高度
			var image = new Image();
			image.onload = function() {
				if (callBack) {
					callBack(image.width, image.height);
				}
			};
			image.src = data;
		};
		reader.readAsDataURL(fileData);
	} else {
		// IE下使用滤镜来处理图片尺寸控制
		// 文件name中IE下是完整的图片本地路径
		var input = file;
		// var input = uploader.get('target').all('input').getDOMNode();
		input.select();
		// 确保IE9下，不会出现因为安全问题导致无法访问
		input.blur();
		var src = document.selection.createRange().text;
		var img = $(
				'<img style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image);width:300px;visibility:hidden;"  />')
				.appendTo('body').getDOMNode();
		img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
		if (callBack) {
			callBack(img.offsetWidth, img.offsetHeight);
		}
	}
}

/**
 * 若url中包含端口信息返回url中端口之后的"/"开始的部分。
 * 
 * http://flb.cneroom.cn/shop/admin/shopuser/list?a=1 -->
 * /shop/admin/shopuser/list?a=1
 * http://flb.cneroom.cn:8088/shop/admin/shopuser/list -->
 * /shop/admin/shopuser/list
 * 
 * @param url
 * @returns
 */
function parseUrlAfterPort(url) {
	var tmpUrl = url;
	if (tmpUrl != null && tmpUrl.length > 8) {
		var tmpUrl = tmpUrl.substr(7);
		var idx = tmpUrl.indexOf("/");
		if (idx != -1) {
			tmpUrl = tmpUrl.substr(idx);
		}
	}
	return tmpUrl;
}

/**
 * 若url中包含端口信息返回url中端口之后的"/"开始"?"之前的部分。
 * 
 * http://flb.cneroom.cn/shop/admin/shopuser/list?a=1 -->
 * /shop/admin/shopuser/list http://flb.cneroom.cn:8088/shop/admin/shopuser/list
 * --> /shop/admin/shopuser/list
 * 
 * @param url
 * @returns
 */
function parseUriAfterPort(url) {
	var tmpUrl = url;
	if (tmpUrl != null && tmpUrl.length > 8) {
		tmpUrl = tmpUrl.substr(7);
		var idx = tmpUrl.indexOf("/");
		if (idx != -1) {
			tmpUrl = tmpUrl.substr(idx);
			var idx2 = tmpUrl.indexOf("?");
			if (idx2 != -1) {
				tmpUrl = tmpUrl.substring(0, idx2);
			}
		}
	}
	return tmpUrl;
}

/**
 * 左对齐:右补c字符直到width为止 cs 字符(串) width 长度 c append符
 */
function alignLeft(cs, width, c) {
	if (cs == null)
		return null;
	var len = cs.length;
	if (len >= width)
		return cs;
	else
		return cs + dup(c, width - len);
}

/**
 * 重复num个cs字符(串)
 */
function dup(cs, num) {
	if (!cs || cs.trim() == "" || num <= 0)
		return "";
	var sb = new Array(0);
	for (var i = num; i--;)
		sb.push(cs);
	return sb.join("");
}

/**
 * 浮点数加法运算
 */
function addFloat(arg1, arg2) {
	var r1, r2, m;
	try {
		r1 = arg1.toString().split(".")[1].length;
	} catch (e) {
		r1 = 0;
	}
	try {
		r2 = arg2.toString().split(".")[1].length;
	} catch (e) {
		r2 = 0;
	}
	m = Math.pow(10, Math.max(r1, r2));
	return (mulFloat(arg1, m) + mulFloat(arg2, m)) / m;
}

/**
 * 浮点数减法运算
 */
function subFloat(arg1, arg2) {
	var r1, r2, m, n;
	try {
		r1 = arg1.toString().split(".")[1].length;
	} catch (e) {
		r1 = 0;
	}
	try {
		r2 = arg2.toString().split(".")[1].length;
	} catch (e) {
		r2 = 0;
	}
	m = Math.pow(10, Math.max(r1, r2));
	// 动态控制精度长度
	n = (r1 >= r2) ? r1 : r2;
	return ((mulFloat(arg1, m) - mulFloat(arg2, m)) / m).toFixed(n);
}

/**
 * 浮点数乘法运算
 */
function mulFloat(arg1, arg2) {
	var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
	try {
		m += s1.split(".")[1].length;
	} catch (e) {
	}
	try {
		m += s2.split(".")[1].length;
	} catch (e) {
	}
	return Number(s1.replace(".", "")) * Number(s2.replace(".", ""))
			/ Math.pow(10, m);
}

/**
 * 浮点数除法运算
 */
function divFloat(arg1, arg2) {
	var t1 = 0, t2 = 0, r1, r2;
	try {
		t1 = arg1.toString().split(".")[1].length;
	} catch (e) {
	}
	try {
		t2 = arg2.toString().split(".")[1].length;
	} catch (e) {
	}
	with (Math) {
		r1 = Number(arg1.toString().replace(".", ""));
		r2 = Number(arg2.toString().replace(".", ""));
		return (r1 / r2) * pow(10, t2 - t1);
	}
}

String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.ltrim = function() {
	return this.replace(/(^\s*)/g, "");
}
String.prototype.rtrim = function() {
	return this.replace(/(\s*$)/g, "");
}

/**
 * 日期格式化
 */
Date.prototype.format = function(mask) {
	var d = this;
	var zeroize = function(value, length) {
		if (!length)
			length = 2;
		value = String(value);
		for (var i = 0, zeros = ''; i < (length - value.length); i++) {
			zeros += '0';
		}
		return zeros + value;
	};

	return mask
			.replace(
					/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g,
					function($0) {
						switch ($0) {
						case 'd':
							return d.getDate();
						case 'dd':
							return zeroize(d.getDate());
						case 'ddd':
							return [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri',
									'Sat' ][d.getDay()];
						case 'dddd':
							return [ 'Sunday', 'Monday', 'Tuesday',
									'Wednesday', 'Thursday', 'Friday',
									'Saturday' ][d.getDay()];
						case 'M':
							return d.getMonth() + 1;
						case 'MM':
							return zeroize(d.getMonth() + 1);
						case 'MMM':
							return [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
									'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ][d
									.getMonth()];
						case 'MMMM':
							return [ 'January', 'February', 'March', 'April',
									'May', 'June', 'July', 'August',
									'September', 'October', 'November',
									'December' ][d.getMonth()];
						case 'yy':
							return String(d.getFullYear()).substr(2);
						case 'yyyy':
							return d.getFullYear();
						case 'h':
							return d.getHours() % 12 || 12;
						case 'hh':
							return zeroize(d.getHours() % 12 || 12);
						case 'H':
							return d.getHours();
						case 'HH':
							return zeroize(d.getHours());
						case 'm':
							return d.getMinutes();
						case 'mm':
							return zeroize(d.getMinutes());
						case 's':
							return d.getSeconds();
						case 'ss':
							return zeroize(d.getSeconds());
						case 'l':
							return zeroize(d.getMilliseconds(), 3);
						case 'L':
							var m = d.getMilliseconds();
							if (m > 99)
								m = Math.round(m / 10);
							return zeroize(m);
						case 'tt':
							return d.getHours() < 12 ? 'am' : 'pm';
						case 'TT':
							return d.getHours() < 12 ? 'AM' : 'PM';
						case 'Z':
							return d.toUTCString().match(/[A-Z]+$/);
						default:
							return $0.substr(1, $0.length - 2);

						}
					});

};

/**
 * 格式化 '
 * <li class="style_{0}" >{1}</li>
 * '.format(name,value)
 */
String.prototype.format = function() {
	var args = arguments;
	return this.replace(/{(\d{1})}/g, function() {
		return args[arguments[1]];
	});
};

/**
 * 数字格式化
 * 
 * @param number
 * @param decimals
 * @param dec_point
 * @param thousands_sep
 * @returns
 */
function number_format(number, decimals, dec_point, thousands_sep) {
	number = (number + '').replace(/[^0-9+\-Ee.]/g, '');
	var n = !isFinite(+number) ? 0 : +number, prec = !isFinite(+decimals) ? 0
			: Math.abs(decimals), sep = (typeof thousands_sep === 'undefined') ? ','
			: thousands_sep, dec = (typeof dec_point === 'undefined') ? '.'
			: dec_point, s = '', toFixedFix = function(n, prec) {
		var k = Math.pow(10, prec);
		return '' + Math.round(n * k) / k;
	};
	// Fix for IE parseFloat(0.55).toFixed(0) = 0;
	s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
	if (s[0].length > 3) {
		s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
	}
	if ((s[1] || '').length < prec) {
		s[1] = s[1] || '';
		s[1] += new Array(prec - s[1].length + 1).join('0');
	}
	return s.join(dec);
}

function number_format_commas(nStr) {
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}

function bindDate(target, df) {
	$(target).datepicker({
		"dateFormat" : df || "yy-mm-d",
		// "changeMonth":true,
		"changeYear" : true,
		"showButtonPanel" : true
	});
}

function bindDate4Month(target) {
	bindDate(target, "yy-mm");
}

var dateTemplate = {
	width : 120,
	align : 'center',
	sorttype : 'date',
	// formatoptions: { newformat: 'm/d/Y' }, datefmt: 'm/d/Y',
	formatoptions : {
		srcformat : 'Y-m-d',
		newformat : 'Y-m-d'
	},
	editoptions : {
		date : true,
		dataInit : bindDate
	},
	searchoptions : {
		sopt : [ 'eq', 'ne', 'lt', 'le', 'gt', 'ge' ],
		dataInit : bindDate
	}
};

/**
 * 元->分
 * 
 * @param {Object}
 *            yuanVal : 1234.56，必须是无小数或者两位小数，其它的不支持。
 */
function yuan2Fen(yuanVal) {
	var yuan = yuanVal.toString();
	var index = yuan.indexOf('.');
	if (index == -1) {
		return yuanVal + "00";
	}
	var y = yuan.substring(0, index);
	var f = yuan.substring(index + 1, yuan.length);
	if (f.length != 2) {
		return -1;
	}
	return y + f;
}

/**
 * 分->元
 * 
 * @param {Object}
 *            fenVal : 123456，必须大于0。
 */
function fen2Yuan(fenVal) {
	var fen = fenVal.toString();
	var len = fen.length;
	if (len <= 0) {
		return -1;
	} else if (len == 1) {
		return "0.0" + fen;
	} else if (len == 2) {
		return "0." + fen;
	} else {
		return fen.substring(0, len - 2) + "." + fen.substring(len - 2, len);
	}
}

/**
 * @param {Function}
 *            func the callback function
 * @param {Object}
 *            opts an object literal with the following properties (all
 *            optional): scope: the object to bind the function to (what the
 *            "this" keyword will refer to) args: an array of arguments to pass
 *            to the function when it is called, these will be appended after
 *            any arguments passed by the caller suppressArgs: boolean, whether
 *            to supress the arguments passed by the caller. This default is
 *            false.
 */
function callback(func, opts) {
	var cb = function() {
		var args = opts.args ? opts.args : [];
		var scope = opts.scope ? opts.scope : this;
		var fargs = opts.supressArgs === true ? [] : argsToArray(arguments);
		func.apply(scope, fargs.concat(args));
	}
	return cb;
}

/* A utility function for callback() */
function argsToArray(arrayLike) {
	var arr = [];
	for (var i = 0; i < arrayLike.length; i++) {
		arr.push(arrayLike[i]);
	}
	return arr;
}

/**
 * 得到width或者height的整数
 * 
 * @param wh
 * @returns
 */
function getWidthHeightInteger(wh) {
	if (!wh) {
		return 0;
	}

	var index = wh.indexOf('%');
	if (index != -1) {
		return -10000;
	}

	index = wh.indexOf('px');
	try {
		if (index == -1) {
			return parseInt(wh);
		} else {
			var tempWH = wh.substring(0, index);
			return parseInt(tempWH);
		}
	} catch (e) {
		return 0;
	}

}

/*
 * 手机、身份证、名称等脱敏
 */
function makeMask(str, frontLen, endLen) {
	if (!str || str.length <= 1) {
		return "*";
	} else if (str.length - frontLen - endLen <= 0) {
		if (str.length < 4) {
			var xing = '';
			for (var i = 1; i < str.length; i++) {
				xing += '*';
			}
			return str.substring(0, 1) + xing;
		} else if (str.length < 8) {
			var xing = '';
			for (var i = 2; i < str.length; i++) {
				xing += '*';
			}
			return str.substring(0, 2) + xing + str.substring(str.length - 2);
		} else {
			var xing = '';
			for (var i = 3; i < str.length; i++) {
				xing += '*';
			}
			return str.substring(0, 3) + xing + str.substring(str.length - 3);
		}
	} else {
		var len = str.length - frontLen - endLen;
		var xing = '';
		for (var i = 0; i < len; i++) {
			xing += '*';
		}
		return str.substring(0, frontLen) + xing
				+ str.substring(str.length - endLen);
	}
}

/**
 * 统计subStr在str中出现的次数
 * 
 * @param str
 * @param subStr
 * @returns
 */
function countSubStr(str, subStr) {
	if(!str || !subStr){
		return 0;
	}
	var n = (str.split(subStr)).length-1;
	return n;
}

function getLastChar(str){
	if(!str || str.length<=0){
		return '';
	}
	return str.substr(str.length - 1);
}

function lastCharEquals(str, lastChar){
	if(!str || str.length<=0 || !lastChar){
		return false;
	}
	return  str.substr(str.length - 1) == lastChar;
}

/**
 * 判断浏览器
 * 
 * @returns
 */
function myBrowser(){
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isOpera = userAgent.indexOf("Opera") > -1;
    if (isOpera) {
        return "Opera"
    }; //判断是否Opera浏览器
    if (userAgent.indexOf("Firefox") > -1) {
        return "FF";
    } //判断是否Firefox浏览器
    if (userAgent.indexOf("Chrome") > -1){
	  return "Chrome";
	}
    if (userAgent.indexOf("Safari") > -1) {
        return "Safari";
    } //判断是否Safari浏览器
    if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
       if (userAgent.indexOf("MSIE 6.0") > -1) { return "IE6"; }
       if (userAgent.indexOf("MSIE 7.0") > -1) { return "IE7"; }
       if (userAgent.indexOf("MSIE 8.0") > -1) { return "IE8"; }
       if (userAgent.indexOf("MSIE 9.0") > -1) { return "IE9"; }
       if (userAgent.indexOf("MSIE 10.0") > -1) { return "IE10"; }
       return "IE";
    } //判断是否IE6-9浏览器
    if (userAgent.toLowerCase().indexOf("trident") > -1 && userAgent.indexOf("rv") > -1 && !isOpera) {
       if (userAgent.indexOf("rv:10.0") > -1) { return "IE10"; }
       if (userAgent.indexOf("rv:11.0") > -1) { return "IE11"; }
       return "IE11";
    } //判断是否IE10-11浏览器
    else
    {
       return userAgent;
    }
}
