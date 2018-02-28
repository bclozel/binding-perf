# Repro case for binding perf issue

This issue can be reproduced with Spring Boot 2.0.0.RC2,
but not current 2.0.0.BUILD-SNAPSHOT.

## Reproducing the issue

Start the application and notice that quite some time
is taken binding `io.spring.sample.bindingperf.model.Dependency$Mapping`.

```
2018-02-28 16:48:00.074 DEBUG 5893 --- [           main] org.springframework.beans.BeanUtils      : No property editor [org.springframework.boot.autoconfigure.cache.CacheTypeEditor] found for type org.springframework.boot.autoconfigure.cache.CacheType according to 'Editor' suffix convention
2018-02-28 16:48:00.239 DEBUG 5893 --- [           main] org.springframework.beans.BeanUtils      : No property editor [io.spring.sample.bindingperf.model.DependencyGroupEditor] found for type io.spring.sample.bindingperf.model.DependencyGroup according to 'Editor' suffix convention
2018-02-28 16:48:00.259 DEBUG 5893 --- [           main] org.springframework.beans.BeanUtils      : No property editor [io.spring.sample.bindingperf.model.VersionRangeEditor] found for type io.spring.sample.bindingperf.model.VersionRange according to 'Editor' suffix convention
2018-02-28 16:48:00.263 DEBUG 5893 --- [           main] org.springframework.beans.BeanUtils      : No property editor [io.spring.sample.bindingperf.model.LinkEditor] found for type io.spring.sample.bindingperf.model.Link according to 'Editor' suffix convention
2018-02-28 16:48:00.269 DEBUG 5893 --- [           main] org.springframework.beans.BeanUtils      : No property editor [io.spring.sample.bindingperf.model.DependencyEditor] found for type io.spring.sample.bindingperf.model.Dependency according to 'Editor' suffix convention
2018-02-28 16:48:00.295 DEBUG 5893 --- [           main] org.springframework.beans.BeanUtils      : No property editor [io.spring.sample.bindingperf.model.Dependency$MappingEditor] found for type io.spring.sample.bindingperf.model.Dependency$Mapping according to 'Editor' suffix convention
2018-02-28 16:48:01.874 DEBUG 5893 --- [           main] org.springframework.beans.BeanUtils      : No property editor [io.spring.sample.bindingperf.InitializrPropertiesEditor] found for type io.spring.sample.bindingperf.InitializrProperties according to 'Editor' suffix convention
2018-02-28 16:48:01.920 DEBUG 5893 --- [           main] org.springframework.beans.BeanUtils      : No property editor [org.springframework.boot.autoconfigure.info.ProjectInfoPropertiesEditor] found for type org.springframework.boot.autoconfigure.info.ProjectInfoProperties according to 'Editor' suffix convention
2018-02-28 16:48:01.924 DEBUG 5893 --- [           main] org.springframework.beans.BeanUtils      : No property editor [org.springframework.boot.autoconfigure.security.SecurityPropertiesEditor] found for type org.springframework.boot.autoconfigure.security.SecurityProperties according to 'Editor' suffix convention
2018-02-28 16:48:01.930 DEBUG 5893 --- [           main] org.springframework.beans.BeanUtils      : No property editor [org.springframework.boot.autoconfigure.web.ServerPropertiesEditor] found for type org.springframework.boot.autoconfigure.web.ServerProperties according to 'Editor' suffix convention
```

One can argue that this sample app is quite particular, since it's using the configuration
properties to sort a lot of metadata with large/deep hierarchies.

## Analysis

Profiling that part of the startup process shows that a significant amount of time
is spent around, consistent with the pause seen in the logs:

* `org.springframework.boot.context.properties.source.ConfigurationPropertyName::elementEquals`
* `org.springframework.boot.context.properties.source.ConfigurationPropertyName::isIndexed`

This call stack seems to point to a large number of interations on configuration keys in
`org.springframework.boot.context.properties.source.SpringConfigurationPropertySource#find(org.springframework.boot.context.properties.source.PropertyMapping[], org.springframework.boot.context.properties.source.ConfigurationPropertyName)`
Setting a conditional breakpoint there with `name.startsWith("initializr.dependencies")`
should be a good starting point to see what's happening.
