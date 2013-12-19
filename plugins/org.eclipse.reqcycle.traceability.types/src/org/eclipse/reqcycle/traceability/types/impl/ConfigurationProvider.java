package org.eclipse.reqcycle.traceability.types.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.reqcycle.traceability.types.ITypesConfigurationProvider;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.Configuration;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.CustomType;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.Entry;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.Type;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.TypeConfigContainer;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.TypeconfigurationFactory;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.util.ConfigUtils;
import org.eclipse.reqcycle.traceability.types.engine.impl.ConfigurationValueInjecter;
import org.eclipse.reqcycle.types.IInjectedTypeProvider;
import org.eclipse.reqcycle.types.IType;
import org.eclipse.reqcycle.types.IType.FieldDescriptor;
import org.eclipse.reqcycle.types.ITypesManager;
import org.eclipse.ziggurat.configuration.IConfigurationManager;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

@Singleton
public class ConfigurationProvider implements ITypesConfigurationProvider,
		IInjectedTypeProvider {
	@Inject
	IConfigurationManager confManager;
	@Inject
	ITypesManager typesManager;

	@Override
	public TypeConfigContainer getContainer() {
		return EcoreUtil.copy(doGetContainer());
	}

	@PostConstruct
	void init() {
		typesManager.addTypeProvider(this);
	}

	private TypeConfigContainer doGetContainer() {

		Collection<EObject> conf = confManager.getConfiguration(null, null, ITypesConfigurationProvider.CONF_PREF_ID, false);

		TypeConfigContainer configuration = null;
		if(conf != null && !conf.isEmpty()) {
			configuration = (TypeConfigContainer)conf.iterator().next();
		}

		if(configuration == null) {
			configuration = TypeconfigurationFactory.eINSTANCE.createTypeConfigContainer();
			saveContainer(configuration);
		}
		for (int i = 0; i < configuration.getTypes().size(); i++) {
			Type t = configuration.getTypes().get(0);
			if (t instanceof CustomType) {
				CustomType custom = (CustomType) t;
				if (custom.getSuperType() == null) {
					delete(custom);
				}
				IType typeJava = custom.getSuperType().getIType();
				if (typeJava == null) {
					delete(custom);
				}
				if (typeJava.isExtensible()) {
					Set<String> descriptorsName = Sets.newHashSet(Iterables
							.transform(typeJava.getDescriptors(),
									new Function<FieldDescriptor, String>() {
										public String apply(FieldDescriptor d) {
											return d.name;
										}
									}));
					if (custom.getEntries().size() != descriptorsName.size()) {
						delete(custom);
					} else {
						for (Entry e : custom.getEntries()) {
							if (!descriptorsName.contains(e.getName())) {
								delete(custom);
								break;
							}
						}
					}
				} else {
					delete(custom);
				}
			}
		}
		for (IType t : typesManager.getAllTypes()) {
			Type typeFromContainer = ConfigUtils.getType(configuration,
					t.getId());
			if (typeFromContainer == null) {
				typeFromContainer = TypeconfigurationFactory.eINSTANCE
						.createType();
				typeFromContainer.setTypeId(t.getId());
				configuration.getTypes().add(typeFromContainer);
			}
		}
		return configuration;
	}

	protected void delete(CustomType custom) {
		EcoreUtil.delete(custom, true);
	}

	@Override
	public Configuration getConfiguration(final String id) {
		Configuration conf = doGetConfiguration(id);
		if (conf == null) {
			return null;
		}
		return EcoreUtil.copy(conf);
	}

	private Configuration doGetConfiguration(final String id) {
		TypeConfigContainer container = doGetContainer();
		Configuration conf = Iterables.find(container.getConfigurations(),
				new Predicate<Configuration>() {
					public boolean apply(Configuration conf) {
						return Objects.equal(conf.getName(), id);
					}
				}, null);
		return conf;
	}

	@Override
	public Configuration getDefaultConfiguration() {
		TypeConfigContainer container = doGetContainer();
		if (container.getDefaultConfiguration() == null) {
			return null;
		}
		TypeConfigContainer copy = EcoreUtil.copy(container);
		return copy.getDefaultConfiguration();
	}

	@Override
	public void save(EObject eobject) {
		if (eobject instanceof TypeConfigContainer) {
			saveContainer((TypeConfigContainer) eobject);
		} else if (eobject instanceof Configuration) {
			saveConfiguration((Configuration) eobject);
		}
	}

	private void saveConfiguration(Configuration eobject) {
		TypeConfigContainer container = doGetContainer();
		Configuration conf = doGetConfiguration(eobject.getName());
		if (conf == null) {
			container.getConfigurations().add(conf);
		} else {
			EcoreUtil.replace(conf, eobject);
		}
	}

	private void saveContainer(TypeConfigContainer eobject) {
		try {
			confManager.saveConfiguration(eobject, null, null, CONF_PREF_ID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setDefaultConfiguration(Configuration conf) {
		saveConfiguration(conf);
		TypeConfigContainer container = doGetContainer();
		container.setDefaultConfiguration(conf);
	}

	@Override
	public Iterable<IType> getTypes() {
		Collection<EObject> conf = confManager
				.getConfiguration(null, null,
						ITypesConfigurationProvider.CONF_PREF_ID, false);
		
		TypeConfigContainer container = null;
		if(conf != null && !conf.isEmpty()) {
			container = (TypeConfigContainer) conf.iterator().next();
		}
		
		if (container == null) {
			return ImmutableList.of();
		}
		return Iterables.transform(
				Iterables.filter(container.getTypes(), CustomType.class),
				new Function<CustomType, IType>() {
					public IType apply(CustomType custom) {
						Type superType = custom.getSuperType();
						IType superTypeJava = superType.getIType();
						return typesManager.newInjectedType(custom.getTypeId(),
								superTypeJava, new ConfigurationValueInjecter(
										custom));
					}
				});

	}

}
