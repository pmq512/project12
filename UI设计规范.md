# UI 设计规范

## 1. 设计理念

### 1.1 设计关键词
- **优雅**：简洁的线条，精致的细节
- **高级**：深邃的色彩，质感的表现
- **通透**：大量留白，呼吸感强
- **沉浸**：大图视觉，柔和遮罩

### 1.2 设计原则
- **少即是多**：避免过度装饰，突出内容本身
- **一致性**：统一的视觉语言贯穿全站
- **层次分明**：通过色彩、大小、间距建立清晰的视觉层次
- **以图为主**：高质量图片是视觉核心

### 1.3 技术实现
- **后端框架**：Spring Boot 3.2.x
- **视图模板**：Thymeleaf
- **样式方案**：Bootstrap 5.3.x + CSS
- **响应式**：移动优先，渐进增强
- **动画效果**：CSS Transitions + JavaScript
- **图标系统**：Font Awesome / Emoji

### 1.4 架构模式
- **单体应用**：前后端一体化架构
- **服务端渲染**：Thymeleaf模板引擎服务端渲染
- **状态管理**：Session + JWT Token

---

## 2. 色彩系统

### 2.1 主色调
```scss
// 主色 - 深邃蓝黑（品牌色）
$primary-color: #1a1a2e;
$primary-light: #2d2d44;
$primary-dark: #0f0f1a;

// 辅助色 - 香槟金（强调色）
$secondary-color: #c9a962;
$secondary-light: #d4b87a;
$secondary-dark: #b8944d;

// 中性色 - 浅灰白（背景色）
$accent-color: #f8f8f8;
```

### 2.2 文字色
```scss
// 主文字
$text-primary: #2c2c2c;
// 次要文字
$text-secondary: #666666;
// 提示文字
$text-light: #999999;
// 反白文字
$text-white: #ffffff;
```

### 2.3 背景色
```scss
// 主背景
$bg-primary: #ffffff;
// 交替背景
$bg-secondary: #f8f8f8;
// 深色背景（页脚）
$bg-dark: #1a1a2e;
```

### 2.4 功能色
```scss
// 成功
$success: #52c41a;
// 警告
$warning: #faad14;
// 错误
$error: #ff4d4f;
// 信息
$info: #1890ff;
```

---

## 3. 字体系统

### 3.1 字体家族
```scss
// 英文标题字体
$font-heading: 'Playfair Display', Georgia, serif;
// 英文正文字体
$font-body: 'Lato', 'Helvetica Neue', Arial, sans-serif;
// 中文字体
$font-chinese: 'Noto Serif SC', 'Songti SC', 'SimSun', serif;
```

### 3.2 字号规范
```scss
// 超大标题（Hero Slogan）
$font-4xl: 64px;
$font-weight-4xl: 700;
$line-height-4xl: 1.2;

// 大标题（区块标题）
$font-3xl: 48px;
$font-weight-3xl: 700;
$line-height-3xl: 1.3;

// 中标题（卡片标题）
$font-2xl: 32px;
$font-weight-2xl: 600;
$line-height-2xl: 1.3;

// 小标题
$font-xl: 24px;
$font-weight-xl: 600;
$line-height-xl: 1.4;

// 大正文
$font-lg: 18px;
$font-weight-lg: 400;
$line-height-lg: 1.6;

// 正文
$font-base: 16px;
$font-weight-base: 400;
$line-height-base: 1.6;

// 小字
$font-sm: 14px;
$font-weight-sm: 400;
$line-height-sm: 1.5;

// 超小字
$font-xs: 12px;
$font-weight-xs: 400;
$line-height-xs: 1.5;
```

---

## 4. 间距系统

### 4.1 基础间距
```scss
$spacing-xs: 4px;
$spacing-sm: 8px;
$spacing-md: 16px;
$spacing-lg: 24px;
$spacing-xl: 32px;
$spacing-2xl: 48px;
$spacing-3xl: 64px;
$spacing-4xl: 96px;
$spacing-5xl: 128px;
```

### 4.2 区块间距
- 页面上下内边距：`$spacing-4xl` (96px)
- 区块之间间距：`$spacing-5xl` (128px)
- 卡片之间间距：`$spacing-lg` (24px)
- 卡片内边距：`$spacing-lg` (24px)

---

## 5. 圆角与阴影

### 5.1 圆角
```scss
$radius-sm: 4px;    // 小元素（按钮、标签）
$radius-md: 8px;    // 卡片
$radius-lg: 16px;   // 大卡片
$radius-xl: 24px;   // 特殊元素
$radius-full: 50%;  // 圆形（头像）
```

### 5.2 阴影
```scss
// 小阴影（卡片默认）
$shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.08);

// 中阴影（卡片悬停）
$shadow-md: 0 4px 16px rgba(0, 0, 0, 0.12);

// 大阴影（弹窗、下拉）
$shadow-lg: 0 8px 32px rgba(0, 0, 0, 0.16);

// 超大阴影（特殊效果）
$shadow-xl: 0 16px 64px rgba(0, 0, 0, 0.2);
```

---

## 6. 组件规范

### 6.1 按钮
```scss
// 主按钮
.btn-primary {
  background: $secondary-color;
  color: $text-white;
  padding: 16px 48px;
  border-radius: $radius-sm;
  font-size: $font-base;
  font-weight: 600;
  letter-spacing: 1px;
  transition: all 0.3s ease;
  
  &:hover {
    background: $secondary-dark;
    transform: translateY(-2px);
    box-shadow: $shadow-md;
  }
}

// 次按钮
.btn-secondary {
  background: transparent;
  color: $text-white;
  border: 2px solid $text-white;
  padding: 14px 46px;
  border-radius: $radius-sm;
  
  &:hover {
    background: $text-white;
    color: $primary-color;
  }
}
```

### 6.2 卡片
```scss
.card {
  background: $bg-primary;
  border-radius: $radius-md;
  overflow: hidden;
  box-shadow: $shadow-sm;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-8px);
    box-shadow: $shadow-md;
  }
  
  .card-image {
    width: 100%;
    height: 240px;
    object-fit: cover;
    transition: transform 0.5s ease;
  }
  
  &:hover .card-image {
    transform: scale(1.1);
  }
  
  .card-content {
    padding: $spacing-lg;
  }
}
```

### 6.3 输入框
```scss
.input {
  border: 1px solid #e0e0e0;
  border-radius: $radius-sm;
  padding: 12px 16px;
  font-size: $font-base;
  transition: border-color 0.3s ease;
  
  &:focus {
    border-color: $secondary-color;
    outline: none;
    box-shadow: 0 0 0 3px rgba(201, 169, 98, 0.1);
  }
}
```

---

## 7. 布局规范

### 7.1 容器宽度
```scss
// 最大宽度
$container-max-width: 1200px;
// 内边距
$container-padding: $spacing-lg;

.container {
  max-width: $container-max-width;
  margin: 0 auto;
  padding: 0 $container-padding;
}
```

### 7.2 网格系统
```scss
// 桌面端
.grid-3 {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: $spacing-lg;
}

.grid-4 {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: $spacing-lg;
}

// 平板端
@media (max-width: 1199px) {
  .grid-3, .grid-4 {
    grid-template-columns: repeat(2, 1fr);
  }
}

// 手机端
@media (max-width: 767px) {
  .grid-3, .grid-4 {
    grid-template-columns: 1fr;
  }
}
```

---

## 8. 动画规范

### 8.1 过渡时间
```scss
$transition-fast: 0.2s ease;
$transition-base: 0.3s ease;
$transition-slow: 0.5s ease;
```

### 8.2 常用动画
```scss
// 淡入
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

// 从下往上淡入
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 滚动显示
.scroll-reveal {
  opacity: 0;
  transform: translateY(30px);
  transition: all 0.6s ease;
  
  &.visible {
    opacity: 1;
    transform: translateY(0);
  }
}
```

---

## 9. 图片规范

### 9.1 图片尺寸
| 用途 | 尺寸 | 比例 |
|------|------|------|
| Hero 轮播图 | 1920x1080 | 16:9 |
| 酒店卡片图 | 800x600 | 4:3 |
| 城市卡片图 | 600x800 | 3:4 |
| 用户头像 | 100x100 | 1:1 |
| 服务图标 | 64x64 | 1:1 |

### 9.2 图片处理
- 格式：优先使用 WebP，降级使用 JPG/PNG
- 质量：80%（平衡质量与体积）
- 加载：懒加载（lazy loading）
- 占位：使用模糊占位图（LQIP）

---

## 10. 响应式断点

```scss
// 超大桌面
$breakpoint-xxl: 1400px;

// 桌面
$breakpoint-xl: 1200px;

// 平板横屏
$breakpoint-lg: 992px;

// 平板竖屏
$breakpoint-md: 768px;

// 手机
$breakpoint-sm: 576px;

// 超小手机
$breakpoint-xs: 400px;
```

---

## 11. 无障碍设计

### 11.1 色彩对比度
- 正文文字与背景对比度 >= 4.5:1
- 大标题与背景对比度 >= 3:1

### 11.2 可访问性
- 所有图片添加 alt 属性
- 表单元素关联 label
- 键盘可访问所有交互元素
- 焦点状态清晰可见

---

## 12. 品牌应用

### 12.1 Logo 使用规范
- 最小尺寸：宽度不小于 120px
- 安全区域：Logo 周围留白不小于 Logo 高度的 1/4
- 深色背景使用白色 Logo
- 浅色背景使用深色 Logo

### 12.2 品牌调性
- 高端、优雅、精致
- 避免过于鲜艳的颜色
- 保持大量留白
- 图片风格统一（暖色调、自然光）
