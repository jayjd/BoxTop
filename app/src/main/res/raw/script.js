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


