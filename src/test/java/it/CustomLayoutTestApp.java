package it;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.thymeleaf.spring.support.Layout;
import org.thymeleaf.spring.support.ThymeleafLayoutInterceptor;

@SpringBootApplication
public class CustomLayoutTestApp {
    @Configuration
    public static class ThymeleafLayoutInterceptorConfig extends WebMvcConfigurationSupport {
        @Override
        protected void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new ThymeleafLayoutInterceptor());
        }

        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/vc").setViewName("viewController");
        }
    }

    @Controller
    @Layout(value = "layouts/default")
    public static class LayoutController {

        @RequestMapping({"/", "index"})
        public String defaultLayout() {
            return "index";
        }

        @RequestMapping("simple")
        @Layout("layouts/simple")
        public String simpleLayout() {
            return "simple";
        }

        @RequestMapping("no-layout")
        @Layout(Layout.NONE)
        public String noLayout() {
            return "noLayout";
        }
    }
}
