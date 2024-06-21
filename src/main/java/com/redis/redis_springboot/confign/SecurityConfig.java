
package com.redis.redis_springboot.confign;

import com.redis.redis_springboot.security.LogoutSuccessHandler;
import com.redis.redis_springboot.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        //JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
      //  tokenRepository.setDataSource(dataSource);
        // 如果token表不存在，使用下面语句可以初始化该表；若存在，请注释掉这条语句，否则会报错。
//        tokenRepository.setCreateTableOnStartup(true);
        //return tokenRepository;
        //使用In-Memory Token存储
        InMemoryTokenRepositoryImpl tokenRepository = new InMemoryTokenRepositoryImpl();
        return tokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedPage("/user/unauth");  //无权限跳转页面
        http
                .csrf().disable() // 启用 CSRF 保护
                .authorizeRequests()
                .antMatchers("/user/login","/user/testCsrf").permitAll() // 允许访问登录相关路径
                .antMatchers("/static/**", "/css/**", "/img/**", "/js-code/**", "/js-package/**", "/js-util/**").permitAll() // 允许静态资源无需认证访问
                .anyRequest().authenticated() // 其他所有路径都需要认证
                .and()
                .formLogin()
                .loginPage("/user/login") // 指定登录页面的路径
                .loginProcessingUrl("/user/login") // 指定提交登录表单的路径
                .defaultSuccessUrl("/user/list", true) // 登录成功后的默认跳转页面
                .permitAll() // 允许所有用户访问登录页面
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .deleteCookies("JSESSIONID")
                //自定义logoutSuccessHandler
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll()
                // 自动登录
               .and().rememberMe()
                .tokenRepository(persistentTokenRepository())
                // 有效时间：单位s
                .tokenValiditySeconds(120)
                .userDetailsService(userDetailsService);
    }
}