package application;

import application.config.DataServiceConfig;
import application.entities.Singer;
import application.services.SingerService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        GenericApplicationContext ctx = new AnnotationConfigApplicationContext(DataServiceConfig.class);
        SingerService bean = ctx.getBean(SingerService.class);
        List<Singer> list = bean.findAll();
        list.forEach(System.out::println);
    }
}
