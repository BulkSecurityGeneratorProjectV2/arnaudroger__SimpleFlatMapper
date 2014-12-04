package org.sfm.reflect.asm;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.sfm.reflect.TypeHelper;

public final class ConstructorDefinition<T> {
	private final Constructor<T> constructor;
	private final ConstructorParameter[] parameters;
	public ConstructorDefinition(Constructor<T> constructor,
			ConstructorParameter[] parameters) {
		super();
		this.constructor = constructor;
		this.parameters = parameters;
	}
	public Constructor<T> getConstructor() {
		return constructor;
	}
	public ConstructorParameter[] getParameters() {
		return parameters;
	}

	public static <T> List<ConstructorDefinition<T>> extractConstructors(final Type target) throws IOException {
		final List<ConstructorDefinition<T>> constructors = new ArrayList<ConstructorDefinition<T>>();

		final Class<T> targetClass = TypeHelper.toClass(target);
		
		ClassLoader cl = targetClass.getClassLoader();
		if (cl == null) {
			cl = ClassLoader.getSystemClassLoader();
		}
		
		final InputStream is = cl.getResourceAsStream(targetClass.getName().replace('.', '/') + ".class");
		try {
			ClassReader classReader = new ClassReader(is);
			classReader.accept(new ClassVisitor(Opcodes.ASM5) {
				List<String> genericTypeNames;

				@Override
				public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
					if (signature != null) {
						genericTypeNames =  AsmUtils.extractGenericTypeNames(signature);
					} else {
						genericTypeNames = Collections.emptyList();
					}
					super.visit(version, access, name, signature, superName, interfaces);
				}

				@Override
				public MethodVisitor visitMethod(int access,
                        String name,
                        String desc,
                        String signature,
                        String[] exceptions) {
					if ("<init>".equals(name)) {
						return new MethodVisitor(Opcodes.ASM5) {
							final List<ConstructorParameter> parameters = new ArrayList<ConstructorParameter>();
							Label firstLabel;
							Label lastLabel;
							@Override
							public void visitLabel(Label label) {
								if (firstLabel == null) {
									firstLabel = label;
								}
								lastLabel = label;
							}

							@Override
							public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
								if (start.equals(firstLabel) && end.equals(lastLabel) && ! "this".equals(name)) {
									parameters.add(createParameter(name, signature == null ? desc : signature));
								}
							}

							private ConstructorParameter createParameter(String name,
									String desc) {
								try {
									Type paramType = AsmUtils.toGenericType(desc, genericTypeNames, target);
									Type type = paramType;
									if (desc.startsWith("T")) {
										type = Object.class;
									}
									return new ConstructorParameter(name, type, paramType);
								} catch (ClassNotFoundException e) {
									throw new Error("Unexpected error " + e, e);
								}
							}

							@Override
							public void visitEnd() {
								try {
									constructors.add(new ConstructorDefinition<T>(targetClass.getDeclaredConstructor(toTypeArray(parameters)), parameters.toArray(new ConstructorParameter[parameters.size()])));
								} catch(Exception e) {
									throw new Error("Unexpected error " + e, e);
								}
							}
							
							private Class<?>[] toTypeArray(List<ConstructorParameter> parameters) {
								Class<?>[] types = new Class<?>[parameters.size()];
								for(int i = 0; i < types.length; i++) {
									types[i] = TypeHelper.toClass(parameters.get(i).getType());
								}
								return types;
							}
						};
					} else {
						return null;
					}
				}

	
				
			}, 0);
		} finally {
			try { is.close(); } catch(Exception e) {};
		}
		
		return constructors;
	}
	public boolean hasParam(ConstructorParameter param) {
		for (ConstructorParameter p : parameters) {
			if (p.equals(param)) {
				return true;
			}
		}
		return false;
	}
}
