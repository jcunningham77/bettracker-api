package com.bettracker.services;

import java.util.List;

import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.util.Log4jConfigurer;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2

public class Application extends SpringBootServletInitializer
{
  /*@Bean
  public Docket swaggerApi()
  {
    return 
      new Docket(DocumentationType.SWAGGER_2).select().paths(userOnlyEndpoints()).build()
      .globalResponseMessage(RequestMethod.GET, getDefaultResponseMessages()).apiInfo(apiInfo());
  }	private Predicate<String> userOnlyEndpoints() {
		return new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return true;
			}
		};
	}*/
  
	@Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().paths(userOnlyEndpoints()).build().globalResponseMessage(RequestMethod.GET, getDefaultResponseMessages())
				.apiInfo(apiInfo());
	}

	private Predicate<String> userOnlyEndpoints() {
		return new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return input.contains("rest");
			}
		};
	}
  

  
 
  
  private static List<ResponseMessage> getDefaultResponseMessages()
  {
    List<ResponseMessage> defaultResponseMessages = Lists.newArrayList();
    defaultResponseMessages.add(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), new ModelRef("Error")));
    defaultResponseMessages.add(new ResponseMessage(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), new ModelRef("Error")));
    defaultResponseMessages.add(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), new ModelRef("Error")));
    return defaultResponseMessages;
  }
  
  private static ApiInfo apiInfo()
  {
    return 
    
      new ApiInfoBuilder().title("bettracker-api").description("This is the API documentation of the bettracker-api service which provides methods to perform CRUD operations for bets.").contact("jcunningham77@gmail.com").version("v1").build();
  }
  
//  @Bean
//  public MethodInvokingFactoryBean log4jInitialization()
//  {
//    MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
//    bean.setTargetClass(Log4jConfigurer.class);
//    bean.setTargetMethod("initLogging");
//    bean.setArguments(new Object[] { "/Users/jeffreycunningham/Documents/workspaceMars/bettracker-api/src/main/resources/log4jconfig.xml" });
//    return bean;
//  }
  
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
      return application.sources(Application.class);
  }
  
  public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
  }
}
