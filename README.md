# 基于JavaSE实现的简易版MySQL数据库
## 该项目为个人项目，因为时间问题，目前只实现了部分功能，后续会不断的完善。
### 1、核心功能实现
  #### 1.1、SQL解析器
  可以对Create、Select、Insert、Update、Delete的SQL语句进行解析，其中嵌套子查询、复杂关系SQL查询语句解析都已实现。
  #### 1.2、物理存储结构
  通过MySQL数据页存储原理，实现了简易版的数据页的物理存储的设计及对应的IO模块，可以实现随机读取数据页、更新数据页的功能。
  #### 1.3、SQL执行器
  目前已实现建表语句（create table ...）、插入语句(insert into ...)、查询语句(select ... from ...)、和简单条件查询(select ... from ... where ...)
### 2、项目截图
#### 右边是MySQL数据库
  ![演示图片](https://user-images.githubusercontent.com/62747607/198819500-35ea0288-5c18-4f45-a168-fae469a429ec.jpg)

**感觉有帮助的，留个star再走吧~**
