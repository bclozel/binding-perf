package io.spring.sample.bindingperf;

import java.util.ArrayList;
import java.util.List;

import io.spring.sample.bindingperf.model.DependencyGroup;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "initializr")
public class InitializrProperties {

	private final List<DependencyGroup> dependencies = new ArrayList<>();

	public List<DependencyGroup> getDependencies() {
		return dependencies;
	}
}
