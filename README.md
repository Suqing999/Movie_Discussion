# Movie_Discussion
Base on SSM

### 操作记录

##### 2021/10/28      搭建SSM框架

##### 2021/10/29     用户注册和登录实现
今日遗留问题：Ajax 传输到后台的内容，如何自动装配到Controller的实体类参数中？
其余的还没有需要问题，都是比较基础的CRUD

##### 2021/10/30   （ 周六一整天 ）实现大部分前台&后台CRUD内容 存在的错误如下，需要巩固的内容如下
内容1：初始值1含义
Map<String, String> map=new HashMap<>(1);

```
/**
     * Constructs an empty <tt>HashMap</tt> with the specified initial
     * capacity and the default load factor (0.75).
     *
     * @param  initialCapacity the initial capacity.
     * @throws IllegalArgumentException if the initial capacity is negative.
     * 这个1被称为初始容量也叫加载因子|    |加载因子越高   空间利用率提高了  但是查询时间 和添加时间增加
     */
    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }
```

内容2：JSP 500错误
````
org.apache.jasper.JasperException: Unable to compile class for JSP: 

An error occurred at line: [16] in the generated java file:xxxxx
````
原因：pom中jsp的jar包冲突导致，也有可能是jsp引入java文件路径失败

内容3：RequestMapping中用了$符号 500错误（大意失荆州！！）
```$xslt
java.lang.IllegalStateException: Invalid mapping on handler class [com.suki.controller.AdminCotroller]: 
public org.springframework.web.servlet.ModelAndView com.suki.controller.AdminCotroller.editCategoryView(java.lang.Integer)
```

内容3：拦截器使用练习
```xml
<mvc:interceptors>
        <mvc:interceptor>
            <!--初始页面的拦截器   加载初始页面的所需资源-->
            <mvc:mapping path="/**/"/>
            <bean class="com.suki.interceptor.StartInterceptor"/>
        </mvc:interceptor>
</mvc:interceptors>
```

<span style='color:red'>明天周日完成前端修改，文件上传，分页操作</span>

<span style='color:red'>以及研究生阶段之前完成的软著推荐系统前端页面连入！</span>

<span style='color:blue'>以及研究生阶段之前完成的软著推荐系统前端页面</span>

#### 研究生阶段的算法后台，java实现推荐后台以及前台不上传代码，因为还没毕业，不公开

