# wordbreak

The project named wordbreak is designed to break the sentence in individual dictionary words. It's conditions are as follows: 

1、JDK 1.8 or later.

2、Both Windows and Linux is OK.

3、4G or higher memory.

# How to run this project

1、install JDK as well as Maven.

2、mvn clean package -DskipTests.
Or you can double click the file r.cmd.

3、these files in need are as follows:
    wordbreak.jar: the compiled program running on JDK.
    log4j.properties: the file contains configurations of initializing log4j.
    startApplication.cmd: start a window. By the way, you can test the program with an UI.
    startCommand.cmd: As is known to us, command line contains parameters.
    system.properties: the file contains mapping between digits and letters and other parameters.

4、if you get the jar, you can run it using the ways:
    script: java -Xms1024m -Xmx2048m -XX:PermSize=1024m -XX:MaxPermSize=1536m -XX:MaxNewSize=1536m -jar wordbreak.jar
            
5、
Although I have tried my best to avoid bugs, I still strongly look forward to your feedback if you find bugs.
And my email is 1024639401@qq.com. Thank you all.




