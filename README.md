# StepView
模仿小米手环App首页的展示计步数据的自定义View

使用一步到位：

```
setMaxProgress(6000);//设置每日目标步数
setProgress(13232);//设置手环传递过来的步数
setColor(Color.parseColor("#00ffff"));//设置颜色
setBigTextSize(90);//设置总步数的字体大小
setSmallTextSize(25);//设置下面的字体大小
setDotSize(10);//设置小圆点的大小
```

上面的方法调用后，界面都会重新绘制

 ![GIF](C:\Users\Gersy\Desktop\GIF.gif)