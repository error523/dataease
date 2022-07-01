package io.dataease.config;

import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.pagehelper.PageInterceptor;
import io.dataease.commons.utils.CompressUtils;
import io.dataease.commons.utils.MybatisInterceptorConfig;
import io.dataease.interceptor.MybatisInterceptor;
import io.dataease.plugins.common.base.domain.AuthSource;
import io.dataease.plugins.common.base.domain.Datasource;
import io.dataease.plugins.common.base.domain.FileContent;
import io.dataease.service.datasource.DatasourceService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = {"io.dataease.ext", "io.dataease.plugins"}, sqlSessionFactoryRef = "sqlSessionFactory")
@EnableTransactionManagement
public class MybatisConfig {

    @Resource
    private Environment env;

    @Bean
    @Primary
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setDbType(DbType.mysql);
        datasource.setUrl(env.getProperty("spring.datasource.url"));
        datasource.setUsername(env.getProperty("spring.datasource.username"));
        datasource.setPassword(env.getProperty("spring.datasource.password"));
        return datasource;
    }

    @Bean
    @Primary
    public TransactionManager txManager(DataSource datasource) {
        return new DataSourceTransactionManager(datasource);
    }

    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(DataSource datasource) throws Exception {
        // 使用mybatis-plus的SqlSessionFactoryBean，代替原有mybatis包下的SqlSessionFactoryBean，以支持mybatis-plus的功能特性
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(datasource);
//        sqlSessionFactoryBean.setMapperLocations(
//                new PathMatchingResourcePatternResolver()
//                        .getResources("classpath:io/dataease/ext/*.xml;classpath:io/dataease/plugins/*.xml"));
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        // 默认值是JdbcType.Other，这里是为了解决操作数据库的时候传入null值，如果sql没有加入jdbcType会报错的问题
        mybatisConfiguration.setJdbcTypeForNull(JdbcType.NULL);
        sqlSessionFactoryBean.setConfiguration(mybatisConfiguration);
        // 设置拦截器
        sqlSessionFactoryBean.setPlugins(pageInterceptor(), dbInterceptor());
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    @ConditionalOnBean({SqlSessionFactory.class})
    @ConditionalOnMissingBean
    public PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("pageSizeZero", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }

    @Bean
    @ConditionalOnBean({SqlSessionFactory.class})
    @ConditionalOnMissingBean
    public MybatisInterceptor dbInterceptor() {
        MybatisInterceptor interceptor = new MybatisInterceptor();
        List<MybatisInterceptorConfig> configList = new ArrayList<>();
        configList.add(new MybatisInterceptorConfig(FileContent.class, "file", CompressUtils.class, "zip", "unzip"));
        configList.add(new MybatisInterceptorConfig(Datasource.class, "configuration"));
        configList.add(new MybatisInterceptorConfig(AuthSource.class, "configuration"));
        interceptor.setInterceptorConfigList(configList);
        return interceptor;
    }
}
