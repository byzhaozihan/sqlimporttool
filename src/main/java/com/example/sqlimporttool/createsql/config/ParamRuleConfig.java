package com.example.sqlimporttool.createsql.config;

import com.example.sqlimporttool.createsql.sqltemplate.paramrule.impl.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParamRuleConfig {

    @Bean
    @ConditionalOnProperty(value = "sqltemplate.conf", havingValue = "company")
    public CompanyValueConfigure companyValueConfigure() {
        return new CompanyValueConfigure();
    }

    @Bean
    @ConditionalOnProperty(value = "sqltemplate.conf", havingValue = "department")
    public DepartmentValueConfigure departmentValueConfigure() {
        return new DepartmentValueConfigure();
    }

    @Bean
    @ConditionalOnProperty(value = "sqltemplate.conf", havingValue = "position")
    public PositionValueConfigure positionValueConfigure() {
        return new PositionValueConfigure();
    }

    @Bean
    @ConditionalOnProperty(value = "sqltemplate.conf", havingValue = "onbrdfinish")
    public OnBrdFinishValueConfigure onBrdFinishValueConfigure() {
        return new OnBrdFinishValueConfigure();
    }

    @Bean
    @ConditionalOnProperty(value = "sqltemplate.conf", havingValue = "stdonbrd")
    public StdOnBrdValueConfigure stdOnBrdValueConfigure() {
        return new StdOnBrdValueConfigure();
    }

    @Bean
    @ConditionalOnProperty(value = "sqltemplate.conf", havingValue = "quit")
    public QuitValueConfigure quitValueConfigure(){
        return new QuitValueConfigure();
    }
}

