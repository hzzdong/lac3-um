/*
 * jQuery Form Validation Engine 2.6.4，错误信息提示修改
 * 
 * hzzdong@qq.com 2015-06-21
 * 
 */
(function($) {
	$.fn.hideValidateErrorText = function() {
		var form = $(this);
		if (!form[0]) {
			return form;
		}
		var fields = form.find("input,select,textarea");
		// alert(JSON2.stringify(fields));
		if (fields && fields.length > 0) {
			for (var i = 0, l = fields.length; i < l; i++) {
				var field = $(fields[i]);
				var clss = field.attr('class');
				// console.log(clss);
				if (clss && clss.indexOf("i-group") != -1) {
					setFieldSuccessParentInputGroupCss(field);
				} else if (clss && clss.indexOf("f-group") != -1) {
					setGroupFieldSuccessParentCss(field);
				} else {
					switch (field.prop("type")) {
					case "text":
					case "password":
					case "textarea":
					case "file":
					case "select-one":
					case "select-multiple":
					default:
						setFieldSuccessCss(field);
						break;
					case "radio":
					case "checkbox":
						setFieldSuccessParentCss(field);
						break;
					}
				}
			}
		}
	}

	$.fn.bindValidationEngineHelper = function() {
		var form = $(this);
		if (!form[0]) {
			return form;
		}
		form.find("input,select,textarea").bind("jqv.field.result",
				function(event, field, errorFound, prompText) {
					var clss = field.attr('class');
					if (errorFound) {
						// console.log(event);
						if (clss && clss.indexOf("i-group") != -1) {
							setFieldErrorParentInputGroupCss(field, prompText);
						} else if (clss && clss.indexOf("f-group") != -1) {
							setGroupFieldErrorParentCss(field, prompText);
						} else {
							switch (field.prop("type")) {
							case "text":
							case "password":
							case "textarea":
							case "file":
							case "select-one":
							case "select-multiple":
							default:
								setFieldErrorCss(field, prompText);
								break;
							case "radio":
							case "checkbox":
								setFieldErrorParentCss(field, prompText);
								break;
							}
						}
					} else {
						if (clss && clss.indexOf("i-group") != -1) {
							setFieldSuccessParentInputGroupCss(field);
						} else if (clss && clss.indexOf("f-group") != -1) {
							setGroupFieldSuccessParentCss(field);
						} else {
							switch (field.prop("type")) {
							case "text":
							case "password":
							case "textarea":
							case "file":
							case "select-one":
							case "select-multiple":
							default:
								setFieldSuccessCss(field);
								break;
							case "radio":
							case "checkbox":
								setFieldSuccessParentCss(field);
								break;
							}
						}
					}
					errorFound = false;
				});
		return form;
	};

	function setFieldErrorCss(field, prompText) {
		var parent = field.parent();
		if (parent && !parent.hasClass("form-group"))
			parent = parent.parent();
		if (!parent)
			return;
		parent.addClass("has-error");
		var errorMessage = field.attr('errorMsg') ? field.attr('errorMsg')
				: prompText;
		if (0 < field.next(".help-block").length)
			field.next(".help-block").remove();
		if (0 == field.next(".help-block").length)
			field.after('<small class="help-block" >' + errorMessage
					+ '</small>');
	}

	function setFieldSuccessCss(field) {
		var parent = field.parent();
		if (parent && !parent.hasClass("form-group"))
			parent = parent.parent();
		if (!parent)
			return;
		parent.removeClass("has-error");
		if (0 < field.next(".help-block").length)
			field.next(".help-block").remove();
	}

	function setFieldErrorParentCss(field, prompText) {
		var parent = getParentWithClassName(field, "form-group");
		if (!parent)
			return;
		parent.addClass("has-error");
		var errorMessage = field.attr('errorMsg') ? field.attr('errorMsg')
				: prompText;
		if (0 < parent.find(".help-block").length)
			parent.find(".help-block").remove();
		if (0 == parent.find(".help-block").length)
			parent
					.append('<div class="col-xs-12 help-block"><small class="help-block">'
							+ errorMessage + '</small></div>');
		// parent.append('<small class="help-block" >'+errorMessage+'</small>');
	}

	function setFieldErrorParentInputGroupCss(field, prompText) {
		var formGroup = getParentWithClassName(field, "form-group");
		if (formGroup) {
			formGroup.addClass("has-error");
		}

		var inputGroup = getParentWithClassName(field, "input-group");
		if (inputGroup) {
			var parent = inputGroup.parent();
			if (parent) {
				var errorMessage = field.attr('errorMsg') ? field
						.attr('errorMsg') : prompText;
				if (0 < parent.find(".help-block").length)
					parent.find(".help-block").remove();
				if (0 == parent.find(".help-block").length)
					parent
							.append('<div class="col-xs-12 help-block"><small class="help-block">'
									+ errorMessage + '</small></div>');
			}
		}
	}

	function setFieldSuccessParentInputGroupCss(field) {
		var formGroup = getParentWithClassName(field, "form-group");
		if (formGroup) {
			formGroup.removeClass("has-error");
		}

		var inputGroup = getParentWithClassName(field, "input-group");
		if (inputGroup) {
			var parent = inputGroup.parent();
			if(parent){
				if (0 < parent.find(".help-block").length) {
					parent.find(".help-block").remove();
				}
			}
		}
	}

	function getParentWithClassName(field, className) {
		var parent = field.parent();
		if (parent && !parent.hasClass(className))
			parent = parent.parent();
		if (parent && !parent.hasClass(className))
			parent = parent.parent();
		if (parent && !parent.hasClass(className))
			parent = parent.parent();
		if (parent && !parent.hasClass(className))
			parent = parent.parent();
		return parent;
	}

	function setFieldSuccessParentCss(field) {
		var parent = getParentWithClassName(field, "form-group");
		if (!parent)
			return;
		parent.removeClass("has-error");
		if (0 < parent.find(".help-block").length)
			parent.find(".help-block").remove();
	}

	function setGroupFieldErrorParentCss(field, prompText) {
		var parent = field.parent();
		if (!parent) {
			return;
		}
		parent.addClass("errorBlock");
		var errorMessage = field.attr('errorMsg') ? field.attr('errorMsg')
				: prompText;
		if (0 < parent.find('#' + field.attr("id") + '-error').length) {
			parent.find('#' + field.attr("id") + '-error').remove();
		}
		if (0 == parent.find('#' + field.attr("id") + '-error').length) {
			parent.append('<div class="errorInf" id="' + field.attr("id")
					+ '-error">' + errorMessage + '</div>');
		}
	}

	function setGroupFieldSuccessParentCss(field) {
		var parent = field.parent();
		if (!parent) {
			return;
		}
		if (0 < parent.find('#' + field.attr("id") + '-error').length) {
			parent.find('#' + field.attr("id") + '-error').remove();
		}
		if (0 >= parent.find(".errorInf").length) {
			parent.removeClass("errorBlock");
		}
	}

})(jQuery);