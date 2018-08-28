package org.simpleflatmapper.map.fieldmapper;

import org.simpleflatmapper.map.FieldMapper;
import org.simpleflatmapper.map.MappingContext;
import org.simpleflatmapper.reflect.primitive.ShortGetter;
import org.simpleflatmapper.reflect.primitive.ShortSetter;

public final class ShortFieldMapper<S, T> implements FieldMapper<S, T> {

	private final ShortFieldMapperGetter<? super S> getter;
	private final ShortSetter<? super T> setter;
	
 	public ShortFieldMapper(final ShortFieldMapperGetter<? super S> getter, final ShortSetter<? super T> setter) {
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void mapTo(final S source, final T target, final MappingContext<? super S> mappingContext) throws Exception {
        setter.setShort(target, getter.getShort(source, mappingContext));
	}

    @Override
    public String toString() {
        return "ShortFieldMapper{" +
                "getter=" + getter +
                ", setter=" + setter +
                '}';
    }
}
