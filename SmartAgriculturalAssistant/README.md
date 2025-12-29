# 智能农苑助手 (Smart Agricultural Assistant)

## 项目概述

智能农苑助手是一款基于Android的移动应用，旨在帮助用户更好地管理和养护家中的花草植物。该应用提供植物查询、天气预报、定时提醒等功能，让种植变得简单有趣。

## 技术栈

- **开发语言**: Java
- **构建工具**: Gradle 8.13
- **最低SDK版本**: Android 7.0 (API 24)
- **目标SDK版本**: Android 14 (API 34)
- **编译SDK版本**: Android 14 (API 34)

## 主要功能

### 1. 植物查询
- 提供数十种珍稀植物的详细资料查询
- 使用ExpandableListView展示植物分类和详情
- 支持搜索功能，快速查找植物信息
- 包含植物的生长习性和养护技巧

### 2. 天气系统 ⭐ (核心网络通信功能)
- **WebService集成**: 使用ksoap2-android库调用天气WebService API
- **实时天气**: 获取选定城市的实时天气信息
- **天气预报**: 显示今日和明日的天气预报
- **多城市支持**: 支持全国主要城市的天气查询
- **异步加载**: 使用AsyncTask在后台线程获取数据，避免阻塞UI

#### WebService实现细节

天气系统是本项目的核心网络通信模块，完整实现了以下WebService调用步骤：

**步骤1：创建SoapObject对象**
```java
SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME);
```

**步骤2：设置调用方法的参数**
```java
soapObject.addProperty("theCityName", cityName);
```

**步骤3：生成SOAP请求信息**
```java
SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
envelope.bodyOut = soapObject;
envelope.dotNet = true; // 重要：针对.NET WebService
envelope.setOutputSoapObject(soapObject);
```

**步骤4：创建HttpTransportSE对象**
```java
HttpTransportSE httpTransport = new HttpTransportSE(URL);
```

**步骤5：调用WebService方法**
```java
httpTransport.call(SOAP_ACTION, envelope);
```

**步骤6：获取并解析响应**
```java
if (envelope.getResponse() != null) {
    SoapObject result = (SoapObject) envelope.bodyIn;
    SoapObject detail = (SoapObject) result.getProperty("getWeatherbyCityNameResult");
    // 解析天气数据
}
```

### 3. 主界面
- 实时时钟显示（使用TextClock组件）
- 天气信息展示
- 植物状态监控
- 三个快捷操作按钮：浇水、施肥、松土
- 状态指示器，根据养护情况显示不同颜色

### 4. 提醒服务
- **广播接收器**: 使用BroadcastReceiver实现提醒功能
- **定时提醒**: 支持浇水、施肥、松土的定时提醒
- **通知系统**: 使用NotificationCompat发送系统通知
- **智能设置**: 根据植物类型自动设置提醒间隔
- **手动设置**: 允许用户自定义提醒间隔

### 5. 数据存储
- **SharedPreferences**: 存储用户设置和植物状态
- **File存储**: 植物资料存储在本地文件中
- **数据库支持**: 预留SQLite数据库接口用于城市和用户数据

### 6. 界面设计
- **TabHost布局**: 三个标签页切换（植物查询、主界面、设置）
- **Material Design**: 使用Material组件库
- **响应式设计**: 适配不同屏幕尺寸
- **自定义控件**: DigitalClock时间显示

## 项目结构

```
SmartAgriculturalAssistant/
├── app/
│   ├── build.gradle                    # 应用级构建配置
│   ├── proguard-rules.pro              # 代码混淆规则
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml     # 应用清单文件
│           ├── java/com/example/smartagri/
│           │   ├── MainActivity.java              # 主Activity（TabHost）
│           │   ├── WeatherService.java            # ⭐ 天气服务（WebService调用）
│           │   ├── ReminderManager.java           # 提醒管理器
│           │   ├── ReminderReceiver.java          # 广播接收器
│           │   └── PlantExpandableListAdapter.java # 植物列表适配器
│           └── res/
│               ├── layout/
│               │   └── activity_main.xml          # 主界面布局
│               ├── values/
│               │   ├── strings.xml                # 字符串资源
│               │   ├── colors.xml                 # 颜色资源
│               │   └── themes.xml                 # 主题资源
│               ├── xml/
│               │   └── file_paths.xml             # 文件路径配置
│               └── mipmap-*/                      # 应用图标
├── build.gradle                         # 项目级构建配置
├── settings.gradle                      # 项目设置
├── gradle.properties                    # Gradle属性配置
└── gradle/
    └── wrapper/
        └── gradle-wrapper.properties    # Gradle Wrapper配置（版本8.13）
```

## 网络通信实现

### 依赖库
```gradle
// ksoap2-android from JitPack
implementation 'com.github.simpligility:ksoap2-android:3.6.4'
implementation 'com.squareup.okhttp3:okhttp:4.12.0'
```

### 仓库配置
在 `settings.gradle` 中需要添加 JitPack 仓库：
```gradle
repositories {
    google()
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

### 权限配置
AndroidManifest.xml中已配置以下网络权限：
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
```

### WebService调用示例
```java
// 异步调用
WeatherService.getWeatherByCityName("北京", new WeatherService.WeatherCallback() {
    @Override
    public void onWeatherLoaded(String weatherInfo) {
        // 处理天气信息
    }
    
    @Override
    public void onWeatherError(String error) {
        // 处理错误
    }
});
```

## 构建和运行

### 环境要求
- Android Studio 2022.3.1 或更高版本
- JDK 8 或更高版本
- Android SDK API 24-34
- Gradle 8.13

### 构建步骤

1. **克隆或下载项目**
```bash
cd SmartAgriculturalAssistant
```

2. **使用Gradle构建**
```bash
# Unix/Linux/Mac
./gradlew build

# Windows
gradlew.bat build
```

3. **安装到设备**
```bash
# Unix/Linux/Mac
./gradlew installDebug

# Windows
gradlew.bat installDebug
```

4. **生成签名APK**
- 在Android Studio中选择 Build → Generate Signed Bundle / APK
- 选择APK选项
- 创建或选择已有的密钥库
- 选择release构建类型
- 生成的APK位于 `app/release/app-release.apk`

### 使用Android Studio

1. 使用Android Studio打开项目根目录
2. 等待Gradle同步完成
3. 连接Android设备或启动模拟器
4. 点击运行按钮（绿色三角形）

## 功能使用说明

### 植物查询
1. 切换到"植物查询"标签页
2. 在搜索框中输入植物名称
3. 点击列表中的分类展开查看植物详情
4. 查看植物的养护建议

### 天气查询
1. 切换到"设置"标签页
2. 在"选择城市"下拉框中选择城市
3. 切换回"主界面"标签页
4. 系统会自动加载选定城市的天气信息
5. 天气信息包括当前天气和未来天气预报

### 设置提醒
1. 切换到"设置"标签页
2. 勾选需要的提醒类型（浇水、施肥、松土）
3. 选择植物类型（系统会根据植物类型自动设置提醒间隔）
4. 点击"保存设置"按钮
5. 系统会在设定的时间发送通知提醒

### 养护操作
1. 在"主界面"标签页
2. 点击"浇水"、"施肥"或"松土"按钮
3. 系统会记录操作时间并更新植物状态
4. 状态指示器会根据养护情况变化颜色

## 核心代码说明

### WeatherService.java
这是项目的核心网络通信模块，实现了完整的WebService调用流程：

- **异步设计**: 使用AsyncTask避免网络操作阻塞主线程
- **错误处理**: 完善的异常捕获和错误回调
- **数据解析**: 解析SOAP XML响应并提取天气信息
- **回调接口**: 提供WeatherCallback接口用于异步结果通知

### ReminderManager.java
提醒管理器，负责设置和取消各种提醒：

- 使用AlarmManager设置精确定时任务
- 支持Android 6.0+的Doze模式
- 使用PendingIntent触发广播
- 提供便捷的API用于管理提醒

### ReminderReceiver.java
广播接收器，处理定时提醒：

- 接收AlarmManager发出的广播
- 创建通知渠道（Android 8.0+）
- 发送系统通知
- 支持点击通知跳转到应用

### MainActivity.java
主Activity，使用TabHost实现多标签页：

- TabHost管理三个标签页
- SharedPreferences存储用户设置
- 集成WeatherService获取天气
- 管理植物状态和提醒设置

## 数据存储

### SharedPreferences存储的数据
- 浇水、施肥、松土的状态和时间
- 提醒开关状态
- 选择的植物类型和城市

### 文件存储
- 植物资料存储在assets目录
- 使用UTF-8编码的文本文件

## 网络安全

- 使用HTTPS连接（当API支持时）
- AndroidManifest.xml中配置了 `usesCleartextTraffic="true"` 用于支持HTTP WebService
- 建议在生产环境中使用HTTPS API

## 权限说明

### 必需权限
- **INTERNET**: 访问网络获取天气信息
- **ACCESS_NETWORK_STATE**: 检查网络连接状态
- **SCHEDULE_EXACT_ALARM**: 设置精确定时提醒（Android 12+）
- **POST_NOTIFICATIONS**: 发送通知（Android 13+）

### 可选权限
- **READ_EXTERNAL_STORAGE**: 读取植物数据文件
- **WRITE_EXTERNAL_STORAGE**: 存储应用数据

## 已知限制

1. **WebService依赖**: 天气功能依赖于WebXML提供的WebService，该服务可能需要注册和API密钥
2. **网络连接**: 天气功能需要稳定的网络连接
3. **定时任务**: Android 6.0+系统的Doze模式可能影响定时提醒的准确性
4. **图标资源**: 当前使用Android系统默认图标，建议替换为自定义图标

## 扩展功能建议

1. **数据库集成**: 完整实现SQLite数据库存储城市和植物数据
2. **用户账号**: 添加用户登录和数据同步功能
3. **图像识别**: 集成植物识别功能
4. **社交分享**: 支持分享养护心得到社交平台
5. **统计图表**: 添加养护历史统计和可视化
6. **离线模式**: 缓存天气数据支持离线查看
7. **多语言**: 添加英语等多语言支持

## 版本发布

### 生成签名文件
```bash
# 使用keytool生成密钥
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-alias
```

### 验证APK签名
```bash
jarsigner -verify -verbose -certs app-release.apk
```

### 发布渠道
- Google Play
- 华为应用市场
- 小米应用商店
- OPPO应用市场
- VIVO应用商店

## 作业完成情况

### ✅ 已完成功能

1. **Android项目结构** - 完整的Gradle 8.13项目配置
2. **网络通信模块** - 完整实现WebService调用天气API（核心功能）
   - 使用ksoap2-android库
   - 完整的6步WebService调用流程
   - 异步加载和错误处理
   - SOAP XML解析
3. **植物查询功能** - ExpandableListView实现植物分类查询
4. **天气系统** - 集成WebService天气API
5. **提醒设置** - BroadcastReceiver实现定时提醒
6. **UI界面** - TabHost三标签页布局
7. **数据存储** - SharedPreferences和文件存储
8. **权限配置** - 完整的AndroidManifest.xml配置

### 📝 项目文档
本README.md文件包含：
- 项目概述和技术栈
- 详细的功能说明
- 完整的构建和运行指南
- 网络通信实现细节
- 使用说明
- 代码结构说明

## 许可证

本项目仅用于学习和教育目的。

## 联系方式

如有问题或建议，请通过GitHub Issues反馈。

---

**开发时间**: 2025年12月
**作业要求**: Android移动应用开发 - 第10章 综合案例一：智能农苑助手
**核心功能**: 网络通信（WebService天气API调用）
