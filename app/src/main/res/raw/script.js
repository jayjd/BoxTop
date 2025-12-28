$(function () {
    // 壁纸来源切换
    $('input[name="wallpaperSource"]').on('change', function () {
        if (this.value === 'local') {
            $('.wallpaper-local').show();
            $('.wallpaper-url').hide();
        } else {
            $('.wallpaper-local').hide();
            $('.wallpaper-url').show();
        }
    });

    // APK 来源切换
    $('input[name="apkSource"]').on('change', function () {
        if (this.value === 'local') {
            $('.apk-local').show();
            $('.apk-url').hide();
        } else {
            $('.apk-local').hide();
            $('.apk-url').show();
        }
    });

});
function onBetaPer(){
    fetch('/api/getDeviceId', {
        method: 'GET'
    })
    .then(res => res.text())
    .then(data => {
        // 创建可点击复制的元素
        const dataDiv = document.getElementById("betaData");
        dataDiv.innerHTML = ""; // 清空旧内容

        const copyDiv = document.createElement("div");
        copyDiv.className = "weui-cell input-field";
        copyDiv.style.wordBreak = "break-all";
        copyDiv.style.whiteSpace = "normal";
        copyDiv.textContent = `${data} （点击复制）`;

        copyDiv.addEventListener("click", () => {
            console.log(data);
            copyToClipboard(data);
        });

        dataDiv.appendChild(copyDiv);
        console.log(data);
    })
    .catch(err => {
        console.error(err);
        alert('获取数据异常');
    });

}
function copyToClipboard(text) {
    // 使用 Clipboard API 复制文本
    if(!navigator.clipboard){
        alert("浏览器不支持 Clipboard API");
        return;
    }
    navigator.clipboard.writeText(text).then(() => {
        alert("已复制到剪贴板,将设备码发送给开发者进行授权！");
    }).catch(err => {
        alert("复制失败：" + err);
    });
}

function onPushWallPaper() {
    const source = document.querySelector('input[name="wallpaperSource"]:checked').value;

    if (source === 'local') {
        const fileInput = document.querySelector('.wallpaper-local input[type="file"]');
        const files = fileInput.files;

        if (!files || files.length === 0) {
            alert('请选择至少一张壁纸');
            return;
        }

        const formData = new FormData();
        formData.append('type', 'wallpaper');
        formData.append('source', 'local');

        // 支持多文件
        for (let i = 0; i < files.length; i++) {
            formData.append('files', files[i]);
        }

        fetch('/api/push/wallpaper', {
            method: 'POST',
            body: formData
        })
        .then(res => res.text())
        .then(data => {
            alert(data);
            console.log(data);
        })
        .catch(err => {
            console.error(err);
            alert('壁纸推送失败');
        });

    } else {
        const urlInput = document.querySelector('.wallpaper-url input');
        const url = urlInput.value.trim();

        if (!url) {
            alert('请输入壁纸 URL');
            return;
        }
        if (!url.startsWith('http://') && !url.startsWith('https://')) {
          alert('URL 必须以 http:// 或 https:// 开头');
          return;
        }
        const cleanUrl = url.split('?')[0].split('#')[0];
        fetch('/api/push/wallpaper', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                type: 'wallpaper',
                source: 'url',
                url: cleanUrl
            })
        })
        .then(res => res.text())
        .then(data => {
            alert(data);
            console.log(data);
        })
        .catch(err => {
            console.error(err);
            alert('壁纸推送失败');
        });
    }
}
function onPushApp() {
    const source = document.querySelector('input[name="apkSource"]:checked').value;

    if (source === 'local') {
        const fileInput = document.querySelector('.apk-local input[type="file"]');
        const files = fileInput.files;

        if (!files || files.length === 0) {
            alert('请选择 APK 文件');
            return;
        }

        const formData = new FormData();
        formData.append('type', 'apk');
        formData.append('source', 'local');

        // 支持多个 APK
        for (let i = 0; i < files.length; i++) {
            formData.append('files', files[i]);
        }

        fetch('/api/push/app', {
            method: 'POST',
            body: formData
        })
        .then(res => res.text())
        .then(data => {
            alert(data);
            console.log(data);
        })
        .catch(err => {
            console.error(err);
            alert('软件推送失败');
        });

    } else {
        const urlInput = document.querySelector('.apk-url input');
        const url = urlInput.value.trim();

        if (!url) {
            alert('请输入 APK 下载地址');
            return;
        }
        if (!url.startsWith('http://') && !url.startsWith('https://')) {
          alert('URL 必须以 http:// 或 https:// 开头');
          return;
        }
        const cleanUrl = url.split('?')[0].split('#')[0];
        fetch('/api/push/app', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                type: 'apk',
                source: 'url',
                url: cleanUrl
            })
        })
        .then(res => res.text())
        .then(data => {
            alert(data);
            console.log(data);
        })
        .catch(err => {
            console.error(err);
            alert('软件推送失败');
        });
    }
}


