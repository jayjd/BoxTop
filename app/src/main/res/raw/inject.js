(function() {
    let retryCount = 0;
    const maxRetries = 15;

    function findAllVideosInIframes(doc, depth = 0) {
        if (depth > 5) return [];

        let videos = [];

        try {
            let docVideos = doc.querySelectorAll('video');
            videos = [...docVideos];

            let iframes = doc.querySelectorAll('iframe');

            iframes.forEach(iframe => {
                try {
                    let iframeDoc = iframe.contentDocument || iframe.contentWindow.document;
                    if (iframeDoc) {
                        let iframeVideos = findAllVideosInIframes(iframeDoc, depth + 1);
                        videos = videos.concat(iframeVideos);
                    }
                } catch (e) {
                    console.log('无法访问 iframe 内容:', e.message);
                }
            });
        } catch (e) {
            console.log('查找视频时出错:', e.message);
        }

        return videos;
    }

    function findAllVideos() {
        let allVideos = [];

        try {
            let mainVideos = document.querySelectorAll('video');
            allVideos = [...mainVideos];

            let objectElements = document.querySelectorAll('object[type*="video"], embed[type*="video"]');
            allVideos = allVideos.concat([...objectElements]);

            let iframes = document.querySelectorAll('iframe');
            iframes.forEach(iframe => {
                try {
                    let iframeDoc = iframe.contentDocument || iframe.contentWindow.document;
                    if (iframeDoc) {
                        let iframeVideos = findAllVideosInIframes(iframeDoc, 1);
                        allVideos = allVideos.concat(iframeVideos);
                    }
                } catch (e) {
                    console.log('无法访问 iframe 内容:', e.message);
                }
            });
        } catch (e) {
            console.log('查找视频时出错:', e.message);
        }

        return allVideos;
    }

    function autoFullscreen() {
        console.log('开始查找视频元素...');

        let videos = findAllVideos();

        if (videos.length === 0) {
            let playerElements = document.querySelectorAll('.MacPlayer, .player, .video-player, .media-player, .dplayer-video-wrap, [id*="player"], [class*="player"]');
            console.log('尝试查找播放器容器:', playerElements.length);

            playerElements.forEach(element => {
                if (element.classList.contains('MacPlayer')) {
                    console.log('发现 MacPlayer 播放器');
                    element.classList.add('embed-responsive', 'embed-responsive-16by9');
                    element.style.cssText += 'z-index: 2147483647; width: 100vw !important; height: 100vh !important; position: fixed !important; top: 0 !important; left: 0 !important; background: black;';

                    if (element.parentElement) {
                        element.parentElement.style.cssText += 'z-index: 2147483647; width: 100vw !important; height: 100vh !important; position: fixed !important; top: 0 !important; left: 0 !important; z-index: 999999; background: black;';
                    }
                    try {
                        let overlayElements = document.querySelectorAll('header, nav, .header, .navbar, .top-bar, .menu, .floatNav.ispcbox, .overlay, .modal, .popup, .ad, .advertisement');
                        overlayElements.forEach(el => {
                            console.log('隐藏遮挡元素:', el);
                            el.style.cssText += 'z-index: 1 !important;';
                        });

                        let hideElements = document.querySelectorAll('.ad, .floatNav.ispcbox, .advertisement, .popup, .modal, .overlay, .fixedbar-fixed-bar, .vod-bottom, .ewave-player-full-toggle');
                        hideElements.forEach(el => {
                            console.log('隐藏元素:', el);
                            el.style.display = 'none';
                        });
                    } catch (e) {
                        console.log('处理遮挡元素时出错:', e.message);
                    }
                }
                let playButtons = element.querySelectorAll('[play], .play, .play-btn, button');
                playButtons.forEach(button => {
                    try {
                        button.click();
                        console.log('点击播放按钮:', button);
                    } catch (e) {
                        console.log('点击播放按钮失败:', e.message);
                    }
                });
            });
        }

        if (videos.length === 0) {
            videos = findAllVideos();
        }

        if (videos.length > 0) {
            let overlayElements = document.querySelectorAll('header, nav, .m-topheader, .header, .header_nav, .nav_wrapper_bg, .navbar, .top-bar, .menu, .overlay, .modal, .popup, .ad, .advertisement');
            overlayElements.forEach(el => {
                console.log('隐藏元素:', el);
                el.style.display = 'none';
            });
            console.log('找到视频元素数量:', videos.length);

            videos.forEach((video, index) => {
                try {
                    console.log(`播放第 ${index + 1} 个视频`, video);
                    video.play();

                    var aft = video.offsetWidth;

                    video.style.cssText += 'position: fixed !important;top: 0 !important;left: 0 !important;width: 100vw !important;height: 100vh !important;z-index: 2147483647 !important;background: black !important;margin: 0 !important;padding: 0 !important;border: none !important;outline: none !important;';
                    video.style.objectFit = 'cover';
                    video.style.setProperty('z-index', '2147483647', 'important');
                    video.controls = false;
                    var ber = video.offsetWidth;
                    console.log('offsetWidth 变化:', aft, '->', ber);

                    if (aft == ber) {
                        console.log('调整视频尺寸以适应全屏');
                        if (video.requestFullscreen) {
                            video.requestFullscreen();
                            console.log('全屏成功');
                        } else if (video.webkitRequestFullscreen) {
                            video.webkitRequestFullscreen();
                            console.log('全屏成功 (webkit)');
                        } else if (video.mozRequestFullScreen) {
                            video.mozRequestFullScreen();
                            console.log('全屏成功 (moz)');
                        } else if (video.msRequestFullscreen) {
                            video.msRequestFullscreen();
                            console.log('全屏成功 (ms)');
                        }
                    }else{
                        console.log('视频宽度已经足够大，无需请求全屏');
                    }

                    console.log('通过CSS样式设置全屏成功');
                } catch (e) {
                    console.log(`播放第 ${index + 1} 个视频时出错:`, e.message);
                }
            });

            return;
        } else {
            retryCount++;
            if (retryCount < maxRetries) {
                console.log("未找到 video，继续等待... 重试次数:" + retryCount);
                setTimeout(autoFullscreen, 2000);
            } else {
                console.log("已达到重试上限，停止重试");

                console.log('最后尝试查找页面所有元素中的视频相关元素...');
                let allElements = document.querySelectorAll('*');
                allElements.forEach(el => {
                    if (el.tagName.toLowerCase().includes('video') ||
                        (el.className && typeof el.className === 'string' && el.className.includes('video')) ||
                        (el.id && typeof el.id === 'string' && el.id.includes('video'))) {
                        console.log('可能的视频元素:', el);
                    }
                });
            }
        }
    }
    autoFullscreen();
})();