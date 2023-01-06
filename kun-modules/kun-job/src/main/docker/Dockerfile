FROM java:8
MAINTAINER gzc
# 将jar包添加到容器中并更名为xx.jar
ADD kun-blog.jar kun-blog.jar
EXPOSE 9200
ENV KUN_BLOG_SERVER_IP=1
ENV KUN_BLOG_REDIS_PASSWORD=1
ENV KUN_BLOG_DATASOURCE_USERNAME=1
ENV KUN_BLOG_DATASOURCE_PASSWORD=1
ENV KUN_BLOG_FASTDFS_GROUP=1
ENV KUN_BLOG_FASTDFS_PORT=1

# 运行jar包
RUN bash -c 'touch /kun-blog.jar'
ENTRYPOINT ["java","-jar","-Dfile.encoding=utf-8","kun-blog.jar","--spring.profiles.active=prod","-Djava.awt.headless=false",""]