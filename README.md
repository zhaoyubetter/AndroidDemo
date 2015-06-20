# AndroidDemo
简介
================
这个项目是一个大的项目demo收录集.其实包括很多一些项目中的小的模板.例:<br>
1:自定义的轮播图展示点.<br>
2:自定义的音乐播放与暂停的无缝切换的展示效果<br>
3:以及自定义的引导页.等等...<br>
![](https://github.com/momodae/AndroidDemo/blob/master/shutcat/pic1.png)&nbsp;
![](https://github.com/momodae/AndroidDemo/blob/master/shutcat/pic2.png)&nbsp;
![](https://github.com/momodae/AndroidDemo/blob/master/shutcat/pic3.png)

模板介绍
================
所有demo的维护依靠一个xml来装载,以实现多层级,各demo间的无藕合性.<br>
之所以采用这种设计,是因为想将这个demo集设计成一种无限扩展的收录demo的项目.不混乱,且容易维护.当然现在还远没有达到这个目的.<br>
节点设计为:<br>
type 起始的节点为一个大的分类.分类与分类之间的标记为id 如果想将这个type划为另一个type的子模板,如设计模式->装饰模式->...这种结构,直接加入parent_id="0"<br>
每个type 可以定义version字段,这种情况设计为一个预留设计.因为软件各级后,再新增条目,这时,version会标记哪些demo为新增添加的.为升级提示,以及更新作的准备<br>
```xml
      <type name="属性动画" id="0">
        <item name="任一控件drawable抖动" info="通过属性动画控制view内drawable执行抖动重绘" clazz="com.ldfs.demo.ui.anim.AnimShakeFragment"/>
        <item name="变动数值" info="通过属性动画让数值迭增方式增加" clazz="com.ldfs.demo.ui.anim.AnimValueFragment"/>
        <item name="颜色渐变" info="属性动画控制背景颜色变幻,控件颜色/drawable变幻" clazz="com.ldfs.demo.ui.anim.AnimColorFragment"/>
        <item name="引导页" info="viewPager配合属性动画引导页" clazz="com.ldfs.demo.ui.anim.AnimGuideFragment"/>
        <item name="ViewGroup动画" info="通过LayoutTransition设定ViewGroup动画" clazz="com.ldfs.demo.ui.anim.FragmentLayoutTransition"/>
        <item name="ViewTransilate组动画" info="根据各种模式执行组动画" clazz="com.ldfs.demo.ui.anim.FragmentViewTransilate"/>
        <item name="属性动画演示" info="属性动画各种操作演示" clazz="com.ldfs.demo.ui.anim.FragmentPropertiesAnimator"/>
    </type>
```
层级跳转模板代码为:
```java
  	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MyItem item = mAdapter.getItem(position);
		mTitleChangeCallbacks.onTitleChange(item.name);
		if (-1 != item.id) {
			FragmentUtils.addFragment(getActivity(), FragmentFunctionList.newInstance(item.id), R.id.view_content, true);
		} else {
			try {
				Class<?> clazz = Class.forName(item.clazz);
				Object instance = clazz.newInstance();
				if (null != instance && instance instanceof Fragment) {
					Fragment fragment = (Fragment) instance;
					FragmentUtils.addFragment(getActivity(), fragment, TextUtils.isEmpty(item.container) ? R.id.fragment_container : ResUtils.id(item.container), true);
				}
			} catch (Exception e) {
				App.toast(R.string.open_option_fail);
			}
		}
	}
```

改进计划
================
1:demo采用v7 actionbar重新设计结构.使风格更趋向于api demo<br>
2:ViewTranslation动画组己发现,会有局部失效的时候(原因不明)<br>
3:工具类,与设计模式类,考虑自定义edit编辑器,使之展示直接展示代码.因为此类经验,很难显示效果.<br>
4:增加每个demo的注解标记demo完成状态,以及难点.进度.方便持续改进.以及他人查看.及修正.<br>
5:ObjectAnimator一直运行导致cpu消耗过高的bug.(demo如自定义Wheel)等等.<br>


注意事项
================
特别提示,此demo为个人项目中收录demo集.并不全面,且demo本身有很多.仅供大家实现思路参考.<br>
以上demo大部分为自我实现,少量网上摘抄.如有问题,及时交流.<br>

