

#HhtSystemUI
##这是一款侧边栏的应用
**功能**
                       1、支持点击打开侧边浮动条的侧边工具栏菜单
                       2、支持手指从屏幕右侧边缘向左滑动/从右侧边缘向左滑动，调出侧边栏
                       3、显示后台应用（左右滑动关闭一个后台应用/上下滑动查看更多）
                       4、点击垃圾箱可同时关闭所有后台应用，也可点击应用右上方垃圾箱关闭指定应用
                       5、显示无线网连接状态（连接时视觉上有突出）：短按开启/关闭；长按进入无线网设置
                       6、显示热点开启/关闭状态（开启时视觉上有突出）：短按开启/关闭；长按进入热点设置
                       7、媒体音量和屏幕亮度左右滑动调节大小，并显示数值
                       8、点按搜索栏，弹出输入键盘，点击键盘上的回车（或点击搜索栏的搜索按钮）跳转至网页浏览器
                       9、在设置里用户关闭底部工具栏后， 底部工具栏内容放在侧边栏里。 常用“书写板”“投屏演示”“远程会议”“更多”4个。增加“windows”。最多5个图标
                       10、侧边栏的收起：A、点击空白区域退出侧边工具栏。B、点按工具栏应用进入该应用，侧边栏收起（长按无线/热点进入网络设置，侧边栏收起）。C、点按搜索，进入浏览器搜索页面后侧边栏收起
                       11、当有新消息时：通知标签上显示红点，点击“通知”再转到通知栏
                       12、通知栏展示的消息包括：应用的消息推送、系统更新通知
**资源文件**



IstSystemUIApplication   启动加载应用

HongheNavigationBar       //启动时加载浮动条
    start() //默认开启
    SideBarBgView.getInstance().init(mContext);
    SideTipManager.getInstance().init(mContext).showView();     //打开侧边浮动条工具类
SideTipManager     侧边浮动条工具类
        getInstance()   实现单例模式
        addWindowView() 定义一个窗口显示
        init(final Context context) 初始化一些数据
        sendLeftPostToTvOs()        //发送左边坐标
        keyInjection  //遥控按键关闭操作
        打开右菜单栏
       mContext.startActivity(new Intent(mContext, MenuRightActiviy.class));
//              ((Activity)mContext).overridePendingTransition(R.anim.right_in, R.anim.temp_no);
                closeOtherSideMenu(MenuLeftActiviy.RECEIVER_ACTION_FINISH_Left,mContext);
                mRightImageView.performClick();
         打开左菜单栏
          mContext.startActivity(new Intent(mContext, MenuLeftActiviy.class));
       //                ((Activity)mContext).overridePendingTransition(R.anim.left_in, R.anim.temp_no);
                       closeOtherSideMenu(MenuRightActiviy.RECEIVER_ACTION_FINISH_Right,mContext);
                       mLeftImageView.performClick();//










