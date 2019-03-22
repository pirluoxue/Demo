package com.example.demo.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements ApplicationContextAware {

	private static ApplicationContext springContext;
	
	private static boolean isSpringEnv = false;

	public static ApplicationContext getContext() {
		return springContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<?> clazz) {
		return (T) springContext.getBean(clazz);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name)
	{
		return (T) springContext.getBean(name);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		springContext = applicationContext;
		isSpringEnv = true;
	}
	
	public static boolean isSpringEnv()
	{
		return isSpringEnv;
	}

	public static void printAllBeans() {
		String[] beans = springContext.getBeanDefinitionNames();
		for (String beanName : beans) {
			if(!beanName.contains("com.mbalib")) {
				continue;
			}
			System.out.println("===============================");
			System.out.println("BeanName:" + beanName);
			System.out.println("Bean：" + springContext.getBean(beanName));
		}
	}

}
