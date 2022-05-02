package com.laffuste.ordo.properties;

import com.google.common.base.Stopwatch;
import com.laffuste.ordo.properties.loaders.PropertiesLoader;
import com.laffuste.ordo.properties.domain.TypedProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkState;

/**
 * Builder for properties resolution and mapping to a properties domain model.
 */
@Slf4j
public class PropertyLoaderBuilder<T> {
    private final List<PropertiesLoader> propertyLoaders = new ArrayList<>();

    private Function<TypedProperties, T> mapper;

    public static <T> PropertyLoaderBuilder<T> builder() {
        return new PropertyLoaderBuilder<>();
    }

    public PropertyLoaderBuilder<T> add(PropertiesLoader loader) {
        propertyLoaders.add(loader);
        return this;
    }

    public PropertyLoaderBuilder<T> mapper(Function<TypedProperties, T> mapper) {
        this.mapper = mapper;
        return this;
    }

    public T load() {
        checkState(mapper != null, "Mapper cannot be null");
        Stopwatch watch = Stopwatch.createStarted();
        log.info("Loading properties from {} loader...", propertyLoaders.size());
        Properties p = propertyLoaders.stream()
                .peek((a) -> log.info("Loading from {}", a.getClass().getSimpleName()))
                .map(PropertiesLoader::load)
                .reduce(new Properties(), (prevProps, props) -> {
                    prevProps.putAll(props); // merge all properties, last one wins
                    return prevProps;
                });
        T domainProps = mapper.apply(TypedProperties.ofProperties(p));
        log.info("Loaded {} properties in {}", p.size(), watch);
        return domainProps;
    }

}
