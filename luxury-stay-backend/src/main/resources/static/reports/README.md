# LuxuryStay 报表模块设计说明书

## 1. 概述

### 1.1 项目背景
本报表模块是一个**独立的静态页面**，用于展示LuxuryStay平台的核心运营数据，无需后端API支持，数据直接在前端硬编码展示。

### 1.2 访问地址
```
http://localhost:8081/reports/index.html
```

## 2. 功能模块

### 2.1 平台概览
- 统计卡片：总用户数、总商家数、总酒店数、总订单数、总收入、平均评分
- 图表：收入趋势图、订单趋势图、用户增长图、目的地分布饼图
- 数据表格：月度运营数据明细

### 2.2 商家数据
- 统计卡片：活跃商家数、本月新增商家、商家平均酒店数、商家平均收入
- 图表：商家业绩排行TOP10、商家活跃度分布
- 数据表格：商家业绩排行榜

### 2.3 用户数据
- 统计卡片：活跃用户数、本月新增用户、用户平均订单数、用户平均消费
- 图表：用户增长趋势、用户消费分布
- 数据表格：月度用户数据明细

### 2.4 酒店数据
- 统计卡片：总酒店数、五星级酒店数、平均房价、平均入住率
- 图表：酒店星级分布、酒店价格分布、热门目的地TOP10
- 数据表格：目的地酒店数据排行

## 3. 技术栈

| 技术 | 版本 | 用途 |
| :--- | :--- | :--- |
| HTML5 | - | 页面结构 |
| CSS3 | - | 页面样式 |
| JavaScript | ES6+ | 页面交互 |
| ECharts | 5.4.3 | 数据可视化 |
| Bootstrap | 5.3.0 | 响应式布局 |
| Font Awesome | 6.4.0 | 图标库 |

## 4. 文件结构

```
reports/
├── index.html          # 主报表页面（单页应用）
└── README.md           # 设计说明书（本文件）
```

## 5. 设计规范

### 5.1 配色方案
```css
--primary-color: #1a1a2e;      /* 深蓝色 - 主色调 */
--secondary-color: #c9a962;    /* 金色 - 辅助色 */
--success-color: #16a34a;      /* 绿色 - 成功状态 */
--warning-color: #d97706;      /* 橙色 - 警告状态 */
--danger-color: #dc2626;       /* 红色 - 危险状态 */
--bg-color: #f8f9fa;           /* 浅灰背景 */
```

### 5.2 卡片样式
- 圆角：12px
- 阴影：0 2px 8px rgba(0,0,0,0.1)
- 悬停效果：上移4px，阴影增强

### 5.3 图表容器
- 高度：400px
- 响应式：支持窗口大小自动调整

## 6. 数据结构

### 6.1 平台概览数据
```javascript
overview: {
    stats: {
        totalUsers: 12580,
        totalMerchants: 156,
        totalHotels: 892,
        totalOrders: 45678,
        totalRevenue: 1258,      // 万元
        avgRating: 4.6
    },
    monthlyData: [{ month, orders, revenue, newUsers, newMerchants }],
    destinationData: [{ name, value }]
}
```

### 6.2 商家数据
```javascript
merchants: {
    stats: {
        activeMerchants: 142,
        newMerchantsThisMonth: 8,
        avgHotelsPerMerchant: 5.7,
        avgRevenuePerMerchant: 80641
    },
    topMerchants: [{ rank, name, company, hotels, orders, revenue, rating }]
}
```

### 6.3 用户数据
```javascript
users: {
    stats: {
        activeUsers: 8456,
        newUsersThisMonth: 1234,
        avgOrdersPerUser: 3.6,
        avgSpendingPerUser: 1000
    },
    monthlyData: [{ month, newUsers, activeUsers, orders, spending }]
}
```

### 6.4 酒店数据
```javascript
hotels: {
    stats: {
        totalHotels: 892,
        fiveStarHotels: 234,
        avgPrice: 880,
        avgOccupancyRate: 75
    },
    starDistribution: [{ name, value }],
    priceDistribution: [{ name, value }],
    topDestinations: [{ rank, name, hotels, fiveStar, fourStar, threeStar, avgPrice }]
}
```

## 7. 功能特点

- ✅ 纯静态页面，无需后端API
- ✅ 响应式设计，支持多端访问
- ✅ Tab切换，四个数据维度
- ✅ ECharts图表可视化
- ✅ 统计卡片直观展示
- ✅ 数据表格详细呈现
- ✅ 返回首页按钮
- ✅ 图表自适应窗口大小

## 8. 运行方式

1. 启动Spring Boot应用
2. 访问 http://localhost:8081/reports/index.html
3. 无需登录，直接查看报表数据

## 9. 更新说明

### 9.1 更新数据
如需更新报表数据，直接修改 `index.html` 文件中的 `reportData` 对象。

### 9.2 更新样式
修改 `<style>` 标签中的CSS样式。

### 9.3 更新图表
修改 `<script>` 标签中的ECharts配置。

---

**版本**: v1.0  
**创建日期**: 2024年6月  
**作者**: LuxuryStay开发团队