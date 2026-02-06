Page({
  data: {
    question: "",
    result: ""
  },
  handleInput(e) {
    this.setData({ question: e.detail.value });
  },
  handleAsk() {
    const question = this.data.question.trim();
    if (!question) {
      wx.showToast({ title: "请输入问题", icon: "none" });
      return;
    }
    const app = getApp();
    const token = wx.getStorageSync("accessToken");
    wx.request({
      url: `${app.globalData.apiBaseUrl}/api/guard/decision`,
      method: "POST",
      header: {
        Authorization: token ? `Bearer ${token}` : ""
      },
      data: { question },
      success: (resp) => {
        const { allowed, reason, answer } = resp.data || {};
        if (allowed) {
          this.setData({ result: answer || "已通过护栏，等待响应" });
        } else {
          this.setData({ result: reason || "请求被拒绝" });
        }
      },
      fail: () => {
        wx.showToast({ title: "请求失败", icon: "none" });
      }
    });
  }
});
