package com.kerbart.checkpoint.spring;

import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

@Configuration
@EnableMetrics
public class MetricsConfiguration extends MetricsConfigurerAdapter {

    private MetricRegistry metricRegistry;

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
        // registerReporter allows the MetricsConfigurerAdapter to
        // shut down the reporter when the Spring context is closed
        // registerReporter(ConsoleReporter.forRegistry(metricRegistry).build()).start(30,TimeUnit.SECONDS);
        // registerReporter(JmxReporter.forRegistry(metricRegistry).build()).start();
    }

    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

    public void setMetricRegistry(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

}
