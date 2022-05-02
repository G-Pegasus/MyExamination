# 星球APP
## 一、APP主要页面及功能介绍
### 1.闪屏页
由闪屏页进入APP主页面，其中，闪屏页包括标题以及有动画效果的图片。动画效果通过Animation实现，具体有旋转效果RotateAnimation，缩放效果ScaleAnimation以及渐变效果AlphaAnimation
具体呈现如下图所示  
![image](https://github.com/liutongji/MyExamination/blob/master/splash.gif)  
### 2.APP整体架构
由三个Fragment组成，分别是主页面（计时器页面），宇宙页面和我的页面，三个Fragment嵌套在MainActivity上，点击下方标签栏可以切换页面。在宇宙（universe）界面里，有两个子Fragment，
由TabLayout控制切换，并且实现了ViewPager，左滑右滑可以切换两个子Fragment的界面。
### 3.计时器页面主要逻辑
#### （1）点击星球
点击中间的星球可以跳转到待点亮界面，该界面从屏幕下方滑出，按返回键可以再滑动消失，也是由动画实现。其中，该页面主要有一个RecyclerView用来展示当前所有未点亮星球，点击创建星球可以跳转到新增星球页面。点击其他星球图标可以在下方展示该星球信息。除此之外，还有一个切换按钮，点击该按钮可以使计时器页面的星球切换到选中星球，相应信息也会改变。**当开始计时后，计时器页面的星球图标不会产生点击事件**。  
![image](https://github.com/liutongji/MyExamination/blob/master/add.gif)  
#### （2）点击星球图标外侧的计时圈
点击计时圈会弹出一个自定义Dialog，可以输入专注时间（分钟），点击确定后开始计时，同时最上方的TextView开始变化，即实现倒计时功能。TextView的变化由Timer + Handler消息传递实现，
在倒计时结束后，会启动Service服务，在服务中创建通知，推送到手机前台的状态栏。计时圈是一个自定义View，主要由最外层的圆，圆上面进度的弧度和进度上的小圆组成。小圆倒计时的转动
通过数学计算（圆弧度的计算公式）以及动画Animation得到。并在自定义View中定义一个接口，用于实现倒计时结束后的逻辑。
![image](https://github.com/liutongji/MyExamination/blob/master/timer.gif)  
![image](https://github.com/liutongji/MyExamination/blob/master/service.gif)  
### 4.新增星球页面主要逻辑
#### （1）外观
外观是一个RecyclerView，有4种外观可选，点击会弹出Toast，提示您选择了第几个。
#### （2）三个文本与按钮
名称和备注是两个EditText，预计点亮时间是一个TextView，点击会弹出一个日历Dialog（DatePickerDialog）可进行选择年月日。按下新增星球按钮后会将当前星球保存到SQLite数据库。  
![image](https://github.com/liutongji/MyExamination/blob/master/add2.gif)  
### 5.universe页面主要逻辑
该页面显示已点亮的星球，排布按照RecyclerView中的瀑布流，但是由于星球大小差距不大，效果没有体现出来，点击星球会跳转到显示星球信息的页面，该页面的信息也是从数据库中获取。同时，
该页面实现了下拉刷新功能，当新增了点亮了的星球，下拉一下就会显示在页面中。  
![image](https://github.com/liutongji/MyExamination/blob/master/lightedinfo.gif)  
### 6.待点亮界面主要逻辑
该页面显示待点亮的星球，也可在此页面新增星球（新增星球后下拉一下就会显示到页面中）。点击星球会跳转到信息显示页面，该页面可实现修改信息与点亮，点击修改信息，会跳转到修改信息
页面，点击点亮会弹出一个自定义Dialog，点击确认，该星球就会被点亮，即在待点亮界面消失，出现在universe界面（都需要下拉刷新显示）。  
![image](https://github.com/liutongji/MyExamination/blob/master/lighted.gif)  
### 7.修改信息页面主要逻辑
该页面可以修改星球的信息，也可以删除该星球，相应的数据库也会进行更新。点击修改信息会弹出一个自定义Dialog，点击确认即可修改。  
![image](https://github.com/liutongji/MyExamination/blob/master/update.gif)   
![image](https://github.com/liutongji/MyExamination/blob/master/delete.gif)  
### 8.我的界面
![image](https://github.com/liutongji/MyExamination/blob/master/mine.jpg)  
## 二、SQLite的具体操作
### 1.isLighted属性
新建数据库时，添加isLighted来判断该星球是否被点亮。在待点亮界面以及universe界面就通过该属性查询。
### 2.使用的框架
我使用的是GreenDao，通过get与set来操作数据库，极大地节省了时间。
## 三、可能的技术亮点
### 1.自定义View模块
我实现自定义View模块时，主要倒计时和进度上的小球不好处理，但一番查阅资料后，让当前角度等于<animation.getAnimatedValue();>即可。除此之外，还有动画监听以及接口的处理。
### 2.自定义Dialog
我的自定义Dialog继承自Dialog，写好了基本的模板后，可以在上面进行随意的修改达到自己想要的效果。
### 3.SQLite数据库的操作
在各个页面实现数据的存储与获取，都需要在创建数据库时写好逻辑才能在查询的时候方便很多。
### 4.下拉刷新模块
在universe和待点亮两个Fragment实现了下拉刷新功能，对List集合的更新实现了监听，主要通过Handler和Runable实现刷新功能。
## 四、心得体会
这次考核是我大学以来，最努力也是最充实的一次，这三天，我每天看着自己实现的一个又一个功能而感到有成就感，我也学到了很多自己之前没接触到的知识，当然也遇到了无数的Bug，但我都没有放弃，而是坚持将产品做到最好。这几天，我充分感受到了身为程序员将来的生活，或许有趣，或许枯燥，但这都是生活的一部分，只要努力对待，终会有好的结果等着你。同时，我也意识到自己有很多的不足，有很多技术自己掌握的不熟练，需要到处查阅资料，这是很痛苦的，只有将自身的技术夯实了，在做项目时才能得心应手，在这之后，我会总结这次实践，找出自己的不足之处，多加提升。我也意识到，实践才是检验自己学习成果最有效的方法，只有多实践才能检验出自身的不足之处，也好找准学习的方向。最后，我想，在IT行业的每个方向都有无尽的知识，我们选好了方向就要一往无前的在这个方向上深耕，不断学习技术才能使自己跟得上时代的脚步。
## 五、需要提升的地方
### 1.代码方面
我这次的考核是用的java语言，由于我前几周才开始学习Kotlin，用的不是很熟练，如果用在考核中，可能会完不成进度，所以我选择了自己更为熟悉的Java一语言，在这之后，我会尽快掌握Kotlin的基本知识，将这门语言运用到实际项目中。
### 2.自定义View方面
我在实现自定义View的过程中，可以说遇到了很大的困难，由于之前练习的少，所以这方面的不足也体现在了这次考核中，在实现的过程中浪费了大量的时间。在这次考核过后，我会将自定义View模块列入自己的学习列表中，尽快熟悉自定义View更多的操作。
### 3.Service方面
我本来想在前台服务的通知栏里显示剩余时间的进度条，但由于时间原因以及自己的不熟练导致没有实现，只实现了定时通知的功能。我对Service只掌握了基础的部分，更多的功能还需要勤加练习，多进行实践。  **除此之外，还有许多待提升的地方就不一一赘述了，我会在这次考核之后，巩固已有知识的同时，去学习更多新的技术！**
