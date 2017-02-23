module simpleflatmapper.jdbc {
    requires transitive simpleflatmapper.map;

    requires transitive java.sql;

    exports org.simpleflatmapper.jdbc;
    exports org.simpleflatmapper.jdbc.named;
    exports org.simpleflatmapper.jdbc.property;
    exports org.simpleflatmapper.jdbc.property.time;

    provides org.simpleflatmapper.converter.ConverterFactoryProducer
        with org.simpleflatmapper.jdbc.converter.JdbcConverterFactoryProducer;
    provides org.simpleflatmapper.reflect.meta.AliasProviderProducer
        with org.simpleflatmapper.jdbc.impl.JpaAliasProviderFactory;
}