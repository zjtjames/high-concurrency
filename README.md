# high-concurrency
电商秒杀系统后台，高并发的瓶颈在于数据库，使用redis缓存，rabbitmq缓冲等手段解决高并发

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
        * UUIDUtil 使用UUID作为session的id
        * ValidatorUtil 验证手机号格式
    * vo model对象

* 秒杀步骤
    1. 减库存
    2. 生成普通订单（在order_info表中insert）
    3. 生成秒杀订单（在miaosha_order表中insert）
    4. 使用@Transactional注解，是方法具有事务性，全部成功或全部回滚
* 实现分布式session
    1. 使用UUID作为session的id
    2. 用fastjson将User对象序列化为字符串，保存session信息
    3. 将不同服务器上的session统一存到一台服务器的redis中
    4. 将sessionId写入cookie，redis中与cookie中过期时间设为相同
* 热点数据redis缓存
    1. 缓存商品列表、商品详情页面的html字符串，过期时间设为较短的60秒
    2. 系统初始化时，把商品库存数量加载到redis，在redis中减库存
    3. 生成秒杀订单后，将秒杀订单用redis缓存，永不过期，避免之后同一用户的请求判断重复秒杀时访问数据库
* rabbitmq异步下单
    1. 每次do_miaosha就是user要秒杀goodId，把这两个字段拼接成一个MiaoshaMessage对象
    2. 用fastjson把对象序列化成字符串，由sender发出，之后直接向用户返回“排队中”
    3. sender是消息的生产者，消息是字符串类型的
    4. receiver是消息的消费者，它通过@RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)来监听队列，从队列中消费消息
    5. 队列中有消息的时候，receiver方法会被自动调用，所以在receiver中做秒杀逻辑，减库存，下订单，写入秒杀订单
    6. 收到“排队中”后，客户端开始轮询，这是用前端代码实现的，实际上就是不停地请求/result接口，调用miaoshaResult方法去查询结果，前端根据miaoshaResult方法的返回值，向用户展示相应的页面 