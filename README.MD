新西兰梅西大学的作业，本人于2022年10月份接的单子，任务是实现一个log4j的Appender类，并使用Goggle的Velocity正则引擎来实现一个自定义的日志格式化布局Layout，然后对实现的类进行单元测试，采用的框架为Junit+Jacoco，Jacoco是一个生成测试覆盖率的mvn插件，在test结束后会生成一个html格式的覆盖率报告。

此外还通过对实现的Appender类添加注册JMX,使其能够被JVM的性能检测程序实监控。

assginment_2: Maven工程，用的IntelliJ IDEA写的，如果你有IDEA则可以直接用IDEA打开。
如果没有也没关系，用Maven运行就行了。（Maven3)

Coverage\Jacoco : Jacoco生成的覆盖率文件，里面是一个HTML页面，点击index.html
即可打开查看覆盖率
务必保证此文件夹里的所有文件都放在一起，否则会导致页面显示不全

perforamce-analysis：word写的性能分析报告
结尾贴上了用JConsole检测的截图。

运行方法：
进入此文件夹，然后执行 Mvn clean test
自动下载依赖，并执行所有的测试类

如果遇到Jacoco不生成覆盖报告的情况，检测工程路径是否有中文！