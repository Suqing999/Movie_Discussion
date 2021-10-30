# Movie_Discussion
Base on SSM

### 操作记录

##### 2021/10/28      搭建SSM框架

##### 2021/10/29     用户注册和登录实现
今日遗留问题：Ajax 传输到后台的内容，如何自动装配到Controller的实体类参数中？
其余的还没有需要问题，都是比较基础的CRUD

##### 2021/10/30    
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



