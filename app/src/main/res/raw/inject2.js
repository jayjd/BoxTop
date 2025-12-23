(function () {
    console.log('注入全屏JS代码');
    
    // 检测视频元素并尝试设置自动全屏
    function enableAutoFullscreen() {
        console.log('开始检测视频元素...');
        
        // 查找所有视频元素
        const videoElements = document.querySelectorAll('video');
        console.log('找到 ' + videoElements.length + ' 个视频元素');
        
        videoElements.forEach(video => {
            // 监听视频播放事件
            video.addEventListener('play', function() {
                console.log('视频开始播放，尝试进入全屏');
                try {
                    // 尝试让视频进入全屏
                    if (video.requestFullscreen) {
                        video.requestFullscreen();
                    } else if (video.webkitRequestFullscreen) {
                        video.webkitRequestFullscreen(); // iOS Safari
                    } else if (video.msRequestFullscreen) {
                        video.msRequestFullscreen(); // IE11
                    } else if (video.webkitEnterFullscreen) {
                        video.webkitEnterFullscreen(); // 某些Android WebView
                    } else if (video.mozRequestFullScreen) {
                        video.mozRequestFullScreen(); // Firefox
                    }
                    console.log('全屏请求已发送');
                } catch (error) {
                    console.error('全屏请求失败:', error);
                }
            });
            
            // 尝试自动播放视频（某些视频需要手动触发）
            try {
                video.play().then(() => {
                    console.log('视频自动播放成功');
                }).catch(err => {
                    console.log('自动播放被阻止，等待用户交互:', err);
                });
            } catch (e) {
                console.log('无法尝试自动播放视频');
            }
        });
    }
    
    // 页面加载完成后执行
    if (document.readyState === 'complete') {
        enableAutoFullscreen();
    } else {
        window.addEventListener('load', enableAutoFullscreen);
    }
    
    // 监听DOM变化，处理动态加载的视频元素
    const observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            if (mutation.addedNodes && mutation.addedNodes.length) {
                mutation.addedNodes.forEach(node => {
                    if (node.nodeName === 'VIDEO') {
                        console.log('检测到新添加的视频元素');
                        enableAutoFullscreen();
                    }
                });
            }
        });
    });
    
    observer.observe(document.body, {
        childList: true,
        subtree: true
    });
    
    console.log('自动全屏脚本初始化完成');
})();