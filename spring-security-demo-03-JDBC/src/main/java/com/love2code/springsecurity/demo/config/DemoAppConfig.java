package com.love2code.springsecurity.demo.config;

import java.beans.PropertyVetoException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="com.love2code.springsecurity.demo")
// reads property files from src/main/resources
@PropertySource("classpath:persistance-mysql.properties")
public class DemoAppConfig {
	
	@Autowired
//	will hold data read from properties file
	private Environment env;
//	logger for diagnostics
	private Logger logger = Logger.getLogger(getClass().getName());

//	define a bean for VireResolver
	
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/view");
		viewResolver.setSuffix(".jsp");
		
		return viewResolver;
	}
	
//	need to define data source object
	@Bean
	public DataSource securityDataSource() {
		
//		create connection pool - create DB connetion for connecting to db
		ComboPooledDataSource securityDataSource = new ComboPooledDataSource();
//		set jdbc driver class
//		loads jdbc diver class 
		try {
			securityDataSource.setDriverClass(env.getProperty("jdbc.driver")); 
		}catch(PropertyVetoException exe){
			throw new RuntimeException (exe);
		}
		
//		log connection properties
		
		logger.info(">>> jdbc URL<<<"+env.getProperty("jdbc.url"));
		
		logger.info(">>> jdbc User<<<"+env.getProperty("jdbc.user"));
//		set database connection props - reads prop for m database file 
		securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		securityDataSource.setUser(env.getProperty("jdbc.user"));
		securityDataSource.setPassword(env.getProperty("jdbc.password"));
		
		securityDataSource.setInitialPoolSize(getIntProperty("Connrction.pool.initialPoolSize"));
		securityDataSource.setMinPoolSize(getIntProperty("Connrction.pool.minPoolSize"));
		securityDataSource.setMaxPoolSize(getIntProperty("Connrction.pool.minPoolSize"));
		securityDataSource.setMaxIdleTime(getIntProperty("Connrction.pool.maxIdleTime"));
		return securityDataSource;
	}
	
//	need helper method
//	read environment property and convert to int
	
	private int getIntProperty(String propName) {
		
		String propval = env.getProperty(propName);
		
//		now convert to int
		
		int intpropval = Integer.parseInt(propval);
		
		return intpropval;
	}
	
	
}
