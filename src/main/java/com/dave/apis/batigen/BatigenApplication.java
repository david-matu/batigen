package com.dave.apis.batigen;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.sql.DataSource;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.skife.jdbi.v2.DBI;

import com.dave.apis.batigen.health.AppHealthCheck;

import com.dave.apis.batigen.resources.CustomerResource;
import com.dave.apis.batigen.resources.ProductResource;
import com.dave.apis.batigen.resources.ProductCategoryResource;
import com.dave.apis.batigen.resources.StaffResource;

import com.dave.apis.batigen.service.AppService;
import com.dave.apis.batigen.service.CustomerService;
import com.dave.apis.batigen.service.ProductService;
import com.dave.apis.batigen.service.ProductCategoryService;
import com.dave.apis.batigen.service.StaffService;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class BatigenApplication extends Application<BatigenConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BatigenApplication().run(args);
    }

    @Override
    public String getName() {
        return "BatigenAPI";
    }

    @Override
    public void initialize(final Bootstrap<BatigenConfiguration> bootstrap) {
    	bootstrap.addBundle(new SwaggerBundle<BatigenConfiguration>() {
			@Override
			protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(BatigenConfiguration config) {
				return config.swaggerBundleConfiguration;
			}
        });
    }

    @Override
    public void run(final BatigenConfiguration config, final Environment env) {
        configureCORS(env);
    	
    	final DataSource dataSource = config.getDataSourceFactory().build(env.metrics(), getName());
        
        DBI dbi = new DBI(dataSource);
        
        AppHealthCheck healthCheck = new AppHealthCheck(dbi.onDemand(AppService.class));
        env.healthChecks().register(getName(), healthCheck);

		env.jersey().register(getConfigurationClass());
		
		env.jersey().register(new CustomerResource(dbi.onDemand(CustomerService.class)));
		env.jersey().register(new ProductResource(dbi.onDemand(ProductService.class)));
		env.jersey().register(new ProductCategoryResource(dbi.onDemand(ProductCategoryService.class)));
		env.jersey().register(new StaffResource(dbi.onDemand(StaffService.class)));
    }
    
    private void configureCORS(Environment env) {
    	final FilterRegistration.Dynamic cors = env.servlets().addFilter("CORS", CrossOriginFilter.class);
    	
    	cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    	cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
    	cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
    	cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
    	
    	cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
}

