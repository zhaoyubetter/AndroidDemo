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
4:增加每个demo的注解标记demo完成状态,以及难点.进度.方便持续改进.以及他人查看.及修正. --2015/6/22己初步完成<br>
5:ObjectAnimator一直运行导致cpu消耗过高的bug.(demo如自定义Wheel)等等.<br>
6:项目国际化<br>


###2015/6/22
为单个demo增加注解项目进度,因为现存30个demo,所以在原在基础上想附上任一东西,都显得很困难.且重复性高.这时候构思了一个想法.注解绑定状态.在自定义程序view初始化时,附加上一层状态显示布局.此处设计为一个简单的相对布局.<br>
采用的方式为:
```java
    /**
     * 初始化进度信息
     *
     * @param object
     * @param view
     */
    private static void initRate(Object object, View view) {
        //因主界面程序己固定,且布局等都己非常多了.所以默认并不改动每个布局去添加这个文件,
        // 而以动态生成布局方式添加.非RelativeLayout,则在外围添加一层RelativeLayout
        boolean bateInfo = PrefernceUtils.getRvsBoolean(ConfigName.BATE_INFO);
        RateInfo info = object.getClass().getAnnotation(RateInfo.class);
        if (bateInfo && null != info && null != view) {
            RelativeLayout rateContainer = null;
            Context context = view.getContext();
            View rateLayout = View.inflate(context, R.layout.rate_layout, null);
            TextView rateStatus = (TextView) rateLayout.findViewById(R.id.tv_rate_state);
            TextView rateInfo = (TextView) rateLayout.findViewById(R.id.tv_rate_info);
            rateStatus.setText(info.rate().toString());//设置demo完成进度状态
            rateInfo.setText(info.beteInfo());//设置demo进度备注信息
            if (view instanceof RelativeLayout) {
                //直接添加
                rateContainer = (RelativeLayout) view;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                LinkedList<View> childViews = new LinkedList<>();
                for (int i = 0; i < viewGroup.getChildCount(); ) {
                    View childView = viewGroup.getChildAt(i);
                    childViews.add(childView);
                    viewGroup.removeView(childView);
                }
                rateContainer = new RelativeLayout(context);
                viewGroup.addView(rateContainer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout container = new LinearLayout(context);
                container.setOrientation(LinearLayout.VERTICAL);
                int size = childViews.size();
                for (int i = 0; i < size; i++) {
                    container.addView(childViews.removeFirst());
                }
                rateContainer.addView(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
            if (null != rateContainer) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                rateContainer.addView(rateLayout, layoutParams);
            }
        }
    }
```
则成功为每个demo项自动附加上一层布局体.会自动根据类上面附加的进度注解,展示到界面.主要作用是明确这个demo大体信息.避免使用者出现问题.<br>


###2015/6/27
新增加了动画元素绘图组控件,将绘制的元素的抽象化为一个对象,如line/ranctangle/arc/circle,元素抽像成一个对象,进行针对性的绘图控制.如动画.绘制顺序控制.完成基础实现.


注意事项
================
特别提示,此demo为个人项目中收录demo集.并不全面,且demo本身有很多.仅供大家实现思路参考.<br>
以上demo大部分为自我实现,少量网上摘抄.如有问题,及时交流.<br>

