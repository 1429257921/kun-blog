package com.kun.common.database.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * RefreshScope的作用是实现配置、实例热加载，也就是我们重写修改配置信息后，
 * spring会销毁当前类的实例，然后重新创建一个新的实例放到容器中，也是实现数据源配置实时更新的关键
 *
 * @author gzc
 * @since 2022-9-30 16:48
 */
@Configuration
//@RefreshScope
public class DruidDataSourceWrapper extends DruidDataSource implements InitializingBean {

	@Value("${spring.datasource.druid.url:#{null}}")
	private String url;
	@Value("${spring.datasource.druid.username:#{null}}")
	private String username;
	@Value("${spring.datasource.druid.password:#{null}}")
	private String password;
	@Value("${spring.datasource.druid.driver-class-name:#{null}}")
	private String driverClassName;
//	@Value("${spring.datasource.druid.connection-properties:#{null}}")
//	private String connectionProperties;
	@Value("${spring.datasource.druid.db-type:#{null}}")
	private String dbType;

	private String passwordCallbackClassName;

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	@Override
	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	@Override
	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

//	@Override
//	public void setConnectionProperties(String connectionProperties) {
//		this.connectionProperties = connectionProperties;
//	}

	@Override
	public void setPasswordCallbackClassName(String passwordCallbackClassName) {
		this.passwordCallbackClassName = passwordCallbackClassName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 如果未找到前缀“spring.datasource.druid”JDBC属性，将使用“Spring.DataSource”前缀JDBC属性。
		super.setUrl(url);
		super.setUsername(username);
		super.setPassword(password);
		super.setDriverClassName(driverClassName);
		super.setInitialSize(initialSize);
		super.setMinIdle(minIdle);
		super.setMaxActive(maxActive);
		super.setMaxWait(maxWait);
		super.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		super.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		super.setValidationQuery(validationQuery);
		super.setTestWhileIdle(testWhileIdle);
		super.setTestOnBorrow(testOnBorrow);
		super.setTestOnReturn(testOnReturn);
		super.setPoolPreparedStatements(poolPreparedStatements);
		super.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
//		super.setConnectionProperties(connectionProperties);
		super.setConnectionProperties(null);
		super.setDbType(dbType);
		super.setPasswordCallbackClassName(passwordCallbackClassName);
	}
}