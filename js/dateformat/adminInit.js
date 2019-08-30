// 防止在自动登录之后出现双重窗口
try {
    if (window.self.name == window.parent.name && window.self.name == "main") {
        window.parent.location.href = window.self.location.href;
    }
} catch (e) {
    console.error(e);
}

$(document).ready(function () {
    adminInitTime();
    adminInitImg();
    adminInitPrice();
    adminInitImgs();
    adminInitSort();
    // 单图片预览
    $("[up72-showImg]").click(function () {
        var _imgPath = $(this).attr('src');
        if (_imgPath.length > 1) {
            layer.ready(function () {
                layer.photos({
                    photos: {
                        "title": "图片预览", "id": 1, "start": 0,
                        "data": [{"alt": "图片预览", "pid": 1, "src": _imgPath, "thumb": _imgPath}]
                    }
                    , shift: 5
                });
            });
        }
    });
});

// 时间处理
var adminInitTime = function(){
    $('[up72-time]').each(function () {
        var _this = $(this);
        if(_this.attr('up72-finish') != 1){
            var _time = _this.text().trim();
            if (/^[0-9]{17}$/.test(_time)) {
                var _format = _this.attr('up72-time');
                if (_format == "") {
                    // _format = "YYYY/MM/DD HH:mm";
                    _format = "YYYY-MM-DD HH:mm:ss";
                }
                _this.text(moment(_time, 'YYYYMMDDHHmmssSSS').format(_format))
            }
            _this.attr('up72-finish', 1);
        }
    });
}

// 单张图片小图展示
var adminInitImg = function(){
    $('[up72-img]').each(function () {
        var _this = $(this);
        if(_this.attr('up72-finish') != 1){
            var width = 30;
            var height = 30;
            var _imgPath = _this.attr('up72-img');
            var _width = _this.attr('up72-img-w');
            var _height = _this.attr('up72-img-h');
            if(_width > 0){
                width = _width;
            }
            if(_height > 0){
                height = _height;
            }
            $("<img src='' up72-showImg='' style='cursor: pointer' />").attr('src', _imgPath).width(width).height(height).appendTo(_this);
            _this.attr('up72-finish', 1);
        }
    });
}

// 金额由“分”转为“元”
var adminInitPrice = function(){
    $('[up72-fen2yuan]').each(function () {
        var obj = $(this);
        if(obj.attr('up72-finish') != 1){
            var fen = obj.text().trim();
            if (isBlank(fen) || isNaN(fen)) {
                return;
            }
            var yuan = (Number(fen) / 100).toFixed(2);
            obj.text(yuan).val(yuan);
            obj.attr('up72-finish', 1);
        }
    });
}

var adminInitImgs = function(){
    layer.config({
        extend: 'extend/layer.ext.js'
    });

    // 多张图片小图展示
    $('[up72-imgs]').each(function () {
        var _this = $(this);
        if(_this.attr('up72-finish') != 1){
            var imgs = _this.attr("up72-imgs");
            if (isBlank(imgs)) {
                return;
            }
            if (isBlank(_this.attr("id"))) {
                _this.attr("id", "imgs" + new Date().getTime() + "" + randomNumber(1, 10000));
            }
            var width = 30;
            var height = 30;
            var _width = _this.attr('up72-img-w');
            var _height = _this.attr('up72-img-h');
            if(_width > 0){
                width = _width;
            }
            if(_height > 0){
                height = _height;
            }
            var imgArr = imgs.split(",");
            for (var i = 0; i < imgArr.length; i++) {
                var img = imgArr[i];
                if(img != ''){
                    $("<img src='' style='margin-right: 3px;cursor: pointer;' idx='" + i + "' />").attr('src', img).width(width).height(height).appendTo(_this);
                }
            }
            _this.find("img").click(function () {
                var img = $(this);
                var obj = $(this).parent(); // 一般是td元素
                var imgs = obj.attr("up72-imgs");
                if (isBlank(imgs)) {
                    return;
                }
                layer.photos({
                    photos: "#" + obj.attr("id")
                    , shift: 5
                });
                window.setTimeout(function () {
                    if ($(".layui-layer").length == 0) {
                        img.click();
                    }
                }, 100)
            });
            _this.attr('up72-finish', 1);
        }
        $(document).on("click",".layui-layer-shade",function(){
            $(".layui-layer-shade").remove();
            $(".layui-layer").remove();
        })
    });
}


//图片加载失败
function errorImgUrl(ImgD, url) {
    ImgD.onerror = null;
    ImgD.src = ctx + url;
}

//图片加载失败
function errorImg(ImgD) {
    ImgD.onerror = null;
    ImgD.src = ctx + '/adminStyles/images/default.png';
}

//图片加载失败
function errorHeadImg(ImgD) {
    ImgD.onerror = null;
    ImgD.src = ctx + '/jsp/app/images/headimg.png';
}

//图片加载失败
function errorImgHide(ImgD) {
    ImgD.onerror = null;
    ImgD.src = '';
    ImgD.style.display="none";
    $(ImgD).hide();
}

var sortInputHtml = '<input type="text" value="[sort]" db_value="[sort]" name="sort" size="5" data-id="[id]" data-url="[url]" style="text-align: right; padding: 0 2px;">';
function adminInitSort(){
    $('[up72-sort]').each(function () {
        var _this = $(this);
        if(_this.attr('up72-finish') != 1){
            var url = _this.attr('up72-sort');
            var sort = _this.text();
            var id = _this.attr('data-id');
            _this.html(sortInputHtml.replace(/\[sort\]/ig, sort).replace("[id]", id).replace("[url]", url));
        }
    });

    $('input[name=sort]').on('change', function(){
        var _this = $(this);
        var sort = Number(_this.val());
        if(sort >=0 && sort < 99999){
            var id = _this.attr('data-id');
            var url = _this.attr('data-url');
            $.getJson(url, {"id": id, "sort": sort}, function () {
                successMsg("修改成功");
                location.reload();
            });
        }else{
            var db_value = _this.attr("db_value");
            _this.val(db_value);
            return showMsg("排序值不合法", {icon: 2, time: 3000});
        }
    })
}