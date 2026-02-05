Page({
  handleWxLogin() {
    wx.login({
      success: (res) => {
        if (!res.code) {
          wx.showToast({ title: "获取登录码失败", icon: "none" });
          return;
        }
        const app = getApp();
        wx.request({
          url: `${app.globalData.apiBaseUrl}/api/wxapp/auth/login`,
          method: "POST",
          data: { code: res.code },
          success: (resp) => {
            const { accessToken, refreshToken } = resp.data || {};
            if (accessToken && refreshToken) {
              wx.setStorageSync("accessToken", accessToken);
              wx.setStorageSync("refreshToken", refreshToken);
              wx.showToast({ title: "登录成功", icon: "success" });
            } else {
              wx.showToast({ title: "登录失败", icon: "none" });
            }
          },
          fail: () => {
            wx.showToast({ title: "网络异常", icon: "none" });
          }
        });
      },
      fail: () => {
        wx.showToast({ title: "微信登录失败", icon: "none" });
      }
    });
  }
});
