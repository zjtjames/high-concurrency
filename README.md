# high-concurrency
电商秒杀系统后台

* 包结构
    * config 配置
        * UserArgumentResolver 解析出User对象 作为参数传给需要的Controller
        * WebConfig 将UserArgumentResolver加入argumentResolvers列表
    * controller 控制器层
    * dao 数据库访问层
    * exception 异常处理
    * pojo 数据库表对象
    * rabbitmq 封装rabbitmq
        * MiaoshaMessage 消息对象
        * MQConfig 定义Queue的Bean
        * MQReceiver 消息的消费者
        * MQSender 消息的生产者
    * redis 封装redis
        * BasePrefix 抽象类 定义redis的key前缀
        * RedisConfig redis配置
        * RedisPoolFactory redis连接池
        * RedisService redis的get, set, incr, decr等操作
    * result 高可复用服务响应对象
    * service 服务层
    * util 工具包
        * MD5Util MD5加密用户密码
        * UUIDUtil 使用UUID作为session的key
        * ValidatorUtil 验证手机号格式
    * vo model对象
        