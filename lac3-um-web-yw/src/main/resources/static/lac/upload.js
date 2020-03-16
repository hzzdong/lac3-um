/*
 * options 参数 
 * 		list:".aTfileList" 用id ; 
 * 		filePicker:".aTfilePicker" 用id ;
 * 		upimgmax:5 数字 默认4张 ; 
 * 		serverPath："${ctx}/bp/attachment/uploadFile";
 * 		url:获取已上传的图片初始化展示用 
 * 		imgPath 
 * 		staticPath 
 * 		callback:{ 
 * 			afterUpload:afterUpload,
 * 			echo:echo
 * 		}
 * 
 */
(function($) {
	RC.upload = function(options) {
	    var ratio = window.devicePixelRatio || 1, thumbnailWidth = 120 * ratio, thumbnailHeight = 120 * ratio;
		var $ = jQuery, $list = $("#" + options.list), $filePicker = $("#" + options.filePicker), // 上传按钮
		$upimgmax = options.upimgmax || 4; // 支持上传最大个数
		var delFileBtn =options.delFileBtn;
		if(delFileBtn == undefined){
			delFileBtn = true; 
		}
		if(!options.accept){
			options.accept=	{
				title : 'Images',
				extensions: 'amr,jpg,jpeg,png,bmp,doc,docx,rar,pdf'
				//extensions : 'jpg,jpeg,png',
				//mimeTypes : 'image/jpg,image/jpeg,image/png' //修改这行
			}
		}
		// 初始化Web Uploader
		var uploader = WebUploader.create({
			// 自动上传。
			auto : false,
			// swf文件路径
			swf : options.staticPath + '/static/webuploader/Uploader.swf',
			// 文件接收服务端。
			server : options.serverPath,
			// 选择文件的按钮。可选。
			// 内部根据当前运行是创建，可能是input元素，也可能是flash
			pick : {
				id : $filePicker,
			// multiple: false
			},
			duplicate : true,
			fileSingleSizeLimit : options.fileSingleSizeLimit, //  单个文件大小
			fileNumLimit : $upimgmax, // 限制上传个数
			accept :options.accept ,
			fileVal : 'file',
			//sendAsBinary:true,  //指明使用二进制的方式上传文件
			thumb : {
				width : thumbnailWidth,
				height : thumbnailHeight,
				// 图片质量，只有type为`image/jpeg`的时候才有效。
				quality : 100,
				// 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
				allowMagnify : false,
				// 是否允许裁剪。
				crop : true,
				// 为空的话则保留原有图片格式。
				// 否则强制转换成指定的类型。
				type : 'image/jpeg'
			}
		});
		// 当有文件添加进来的时候
		uploader.on('fileQueued', function(file) {
			var dPanel = '<div id="' + file.id + '" class="file-item thumbnail"><img>';
			if(delFileBtn){
				dPanel = dPanel+'<div class = "file-panel" style = "height: 30px;"><span class = "cancel delimgbtns" title=""></span></div>' 
			}
			dPanel=dPanel+ '</div>';
			var $li = $(dPanel), $img = $li.find('img');
			$filePicker.before($li);
			//$list.prepend($li);
			registerRemovefiles(); // 文件删除
			if (file.source.url) {
				if(file.type.indexOf("audio/")!=-1 || file.type.indexOf("video/")!=-1){
					var vPath =file.source.url;
	        		if(file.type.indexOf("audio/amr")!=-1){
	        			vPath = file.source.url+".mp3";
	        		}
					var  vv = "<video controls preload='auto' width='120' height='120' ";
					vv = vv+"poster='"+options.staticPath+"/static/video6.2.5/audio.png' data-setup='{}'>";
					vv = vv+"<source src='"+vPath+"'></source>";
					vv = vv+"<p class='vjs-no-js'>不支持播放</p></video>";
					$img.replaceWith(vv);
				}else if(file.type.indexOf("image/")!=-1){
					uploader.makeUrlThumb(file.source.url, thumbnailWidth, thumbnailHeight, function(s, d) {
						$img.attr('src', s);
						if (typeof options.callback.afterUpload == "function") {
							options.callback.afterUpload(file.id, file.source.source.id);
						}
					})
				}
				
			} else {
				if(file.type.indexOf("audio/")!=-1 || file.type.indexOf("video/")!=-1 ||file.name.indexOf(".amr")!=-1 ){
					var objUrl = getObjectURL(file.source.source); 
					var  vv = "<video controls preload='auto' width='120' height='120' ";
					vv = vv+"poster='"+options.staticPath+"/static/video6.2.5/audio.png' data-setup='{}'>";
					vv = vv+"<source src='"+objUrl+"'></source>";
					vv = vv+"<p class='vjs-no-js'>不支持播放</p></video>";
					$img.replaceWith(vv);
				}else if(file.type.indexOf("image/")!=-1){
					uploader.makeThumb(file, function(error, src) {
						if (error) {
							$img.replaceWith('<span>不能预览</span>');
							return;  
						}
						$img.attr('src', src);
					}, thumbnailWidth, thumbnailHeight);
				}
			}

			var uploadfilesNum = uploader.getStats().queueNum; //  共选中几个图片
			// 最多支持 options.upimgmax张
			if (($("#" + options.list + " > div").size() - 1) >= $upimgmax) {
				$filePicker.hide();
				if (($("#" + options.list + " > div").size() - 1) >= ($upimgmax + 1)) {
					// 中断 取消 大于  options.upimgmax张图片的对象
					uploader.removeFile(file, true);
					//$('.file-item').last().remove();
				}
			} else {
				$filePicker.show();
			}

			if (!file.source.url) {
				uploader.upload(file);
			}
		});
		
        function getObjectURL(file) {  
            var url = null;  
            if (window.createObjectURL != undefined) { // basic  
                url = window.createObjectURL(file);  
            }  else if (window.webkitURL != undefined) { // webkit or chrome  
                url = window.webkitURL.createObjectURL(file);  
            } else if (window.URL != undefined) { // mozilla(firefox)  
                url = window.URL.createObjectURL(file);  
            } 
            return url;  
        }  
		
		// 文件上传过程中创建进度条实时显示。
		uploader.on('uploadProgress', function(file, percentage) {
			var $li = $('#' + file.id), $percent = $li.find('.progress span');
			// 避免重复创建
			if (!$percent.length) {
				$percent = $('<p class="progress"><span></span></p>').appendTo($li).find('span');
			}
			$percent.css('width', percentage * 100 + '%');
		});

		// 文件上传成功，给item添加成功class, 用样式标记上传成功。
		uploader.on('uploadSuccess', function(file, response) {
			if (response.error) {
				var $li = $('#' + file.id), $error = $li.find('div.error');
				// 避免重复创建
				if (!$error.length) {
					$error = $('<div class="error"></div>').appendTo($li);
				}
				$error.text('操作失败');
			} else {
				var $li = $('#' + file.id);
				$li.addClass('upload-state-done');

				if (typeof options.callback.afterUpload == "function") {
					options.callback.afterUpload(file.id, response.id);
				}
			}

		});

		// 文件上传失败，显示上传出错。
		uploader.on('uploadError', function(file) {
			if (!file.source.url) {
				var $li = $('#' + file.id), $error = $li.find('div.error');
				// 避免重复创建
				if (!$error.length) {
					$error = $('<div class="error"></div>').appendTo($li);
				}
				$error.text('操作失败');
			}
		});
		// 完成上传完了，成功或者失败，先删除进度条。
		uploader.on('uploadComplete', function(file) {
			$('#' + file.id).find('.progress').remove();
		});
		uploader.on('error', function(handler) {
			if (handler == "Q_EXCEED_NUM_LIMIT") {
				console.log("最多只能上传 " + $upimgmax + "张图片")
			}
		});
		// 删除按钮事件
		function registerRemovefiles() {
			// 删除本条数据
			$('.delimgbtns').unbind('click').click(function() {
				var id = $(this).parent().parent().attr("id");
				// 中断 取消 传图
				$('#sub_' + id).remove();
				uploader.removeFile(id, true);
				$('#' + id).remove();
				$filePicker.show(); // 上传按钮显示
			});
		}
		if (options.url != undefined) {
			var p2 = {
				url : options.url,
				data : '',
				success : function(data1) {
					if (typeof options.callback.echo == "function") {
						options.callback.echo(uploader, data1);
					}
				}
			};
			RC.ajax(p2);
		}
		if (options.callback && options.callback.customEcho) {
			options.callback.customEcho(uploader);
		}
	}
})(jQuery);