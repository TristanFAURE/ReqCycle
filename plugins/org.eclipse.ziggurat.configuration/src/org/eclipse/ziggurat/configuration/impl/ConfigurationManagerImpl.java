/*******************************************************************************
 * Copyright (c) 2013 Atos.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Atos - initial API and implementation
 ******************************************************************************/
package org.eclipse.ziggurat.configuration.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Singleton;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.ziggurat.configuration.Activator;
import org.eclipse.ziggurat.configuration.IConfigurationManager;
import org.eclipse.ziggurat.configuration.impl.EMFConfResourceFactory.EMFConfResource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Singleton
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ConfigurationManagerImpl implements IConfigurationManager {

	
	protected static final Map<?, ?> SAVE_OPTIONS = Collections.singletonMap(XMIResource.OPTION_SCHEMA_LOCATION, true);

	public static final String CONF_RESOURCE_EXTENSION = "emfconf";

	public RestrictedResourceSet rs = new RestrictedResourceSet();


	public void saveConfiguration(EObject conf, IResource context, Scope scope, String id, ResourceSet resourceSet) throws IOException {
		saveConfiguration(Collections.singleton(conf), context, scope, id, resourceSet);
	}
	
	public void saveConfiguration(Collection<? extends EObject> conf, IResource context, Scope scope, String id, ResourceSet resourceSet) throws IOException {
		if(context == null && Scope.PROJECT.equals(scope)) {
			throw new IOException("Context should not be null when using project scope");
		}

		if(scope == null || context == null) {
			scope = Scope.WORKSPACE;
		}

		URI confFileUri = getConfigurationFileUri(context, scope, id);
		if(resourceSet instanceof RestrictedResourceSet) {
			((RestrictedResourceSet)resourceSet).authorizedUris.add(confFileUri);
		}
		Resource r = resourceSet.getResource(confFileUri, false);
		if(r == null) {
			r = resourceSet.createResource(confFileUri);
		}

		r.getContents().clear();
		r.getContents().addAll(conf);

		if(r instanceof EMFConfResource) {
			((EMFConfResource)r).manualSave(SAVE_OPTIONS);
		} else {
			r.save(SAVE_OPTIONS);
		}
	}
	
	public Collection<EObject> getConfiguration(IResource context, Scope scope, String id, ResourceSet resourceSet, boolean reload) {
		URI confFileUri = getConfigurationFileUri(context, scope, id);
		if(resourceSet instanceof RestrictedResourceSet) {
			((RestrictedResourceSet)resourceSet).addAuthorizedUri(confFileUri);
		}
		try {
			Resource loadedResource = resourceSet.getResource(confFileUri, false);
			if (reload && loadedResource != null && loadedResource.isLoaded()) {
				loadedResource.unload();
				resourceSet.getResources().remove(loadedResource);
			}

			Resource r = resourceSet.getResource(confFileUri, true);

			if(r != null && !r.getContents().isEmpty()) {
				return r.getContents();
			}
		} catch (Throwable e) {
			//DO NOTHING
			e.printStackTrace();
		}

		return null;
	}

	public void saveConfiguration(EObject conf, IResource context, Scope scope, String id) throws IOException {
		if(!isSelfContained(conf)) {
			throw new IOException("The configuration object have reference(s) outside of itself and its children");
		}

		saveConfiguration(conf, context, scope, id, rs);
	}

	public Collection<EObject> getConfiguration(IResource context, Scope scope, String id, boolean reload) {
		return getConfiguration(context, scope, id, rs, reload);
	}

	protected boolean isSelfContained(EObject eObj) {
//		Set<EObject> containedObjs = Sets.newHashSet();
//		containedObjs.add(eObj);
//		TreeIterator<EObject> it = eObj.eAllContents();
//		while(it.hasNext()) {
//			containedObjs.add(it.next());
//		}
//
//		for(EObject o : Lists.newLinkedList(containedObjs)) {
//			if(hasOutsideReferences(o, containedObjs)) {
//				return false;
//			}
//		}

		return true;
	}

	protected boolean hasOutsideReferences(EObject obj, Set<EObject> containedObjs) {
		for(EReference ref : obj.eClass().getEAllReferences()) {
			if(obj.eIsSet(ref) && !ref.isContainment()) {
				if(ref.isMany()) {
					List values = (List)obj.eGet(ref);
					for(Object val : values) {
						if(val != null && !containedObjs.contains(val)) {
							return true;
						}
					}
				} else {
					Object val = obj.eGet(ref);
					if(val != null && !containedObjs.contains(val)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public URI getConfigurationFileUri(IResource context, Scope scope, String id) {
		return getConfigurationFileUri(context, scope, id, CONF_RESOURCE_EXTENSION);
	}

	protected URI getConfigurationFileUri(IResource context, Scope scope, String id, String extension) {
		IPath confFilePath = null;

		if(context != null && context.getProject() != null && (scope == null || Scope.PROJECT.equals(scope))) {
			confFilePath = context.getProject().getFullPath().append("/.settings/" + Activator.PLUGIN_ID + "/" + id + "." + extension);
			IFile confFile = ResourcesPlugin.getWorkspace().getRoot().getFile(confFilePath);

			if(!confFile.exists() && scope == null) {
				confFilePath = null;
			}
		}

		if(confFilePath == null && (scope == null || Scope.WORKSPACE.equals(scope))) {
			return URI.createURI("platform:/meta/" + Activator.PLUGIN_ID + "/" + id + "." + extension);
		} else {
			return URI.createPlatformResourceURI(confFilePath.toOSString(), true);
		}
		
	}

	public String getConfigurationResourceExtension() {
		return CONF_RESOURCE_EXTENSION;
	}

	public Map<String, Object> getSimpleConfiguration(IResource context, Scope scope, String id, boolean reload) {
		EObject confEObj = null;
		
		Collection<EObject> conf = getConfiguration(context, scope, id, reload);
		if(conf != null && !conf.isEmpty()) {
			confEObj = conf.iterator().next();
		}

		if(confEObj == null) {
			return null;
		}

		Map<String, Object> res = Maps.newHashMap();

		for(EAttribute att : confEObj.eClass().getEAllAttributes()) {
			if(att.isMany()) {
				List l = (List)confEObj.eGet(att);
				List newList = Lists.newArrayList();
				for(Object o : l) {
					newList.add(o);
				}
				res.put(att.getName(), newList);
			} else {
				res.put(att.getName(), confEObj.eGet(att));
			}
		}
		return res;
	}

	public void saveSimpleConfiguration(Map<String, Object> conf, IResource context, Scope scope, String id) throws IOException {
		EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName(CONF_RESOURCE_EXTENSION);
		ePackage.setNsPrefix(CONF_RESOURCE_EXTENSION);
		ePackage.setNsURI("http://www.agesys.org/" + CONF_RESOURCE_EXTENSION + "/" + EcoreUtil.generateUUID());

		EFactory eFactory = EcoreFactory.eINSTANCE.createEFactory();
		eFactory.setEPackage(ePackage);

		EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.setName("Conf");
		ePackage.getEClassifiers().add(eClass);

		for(Entry<String, Object> elem : conf.entrySet()) {
			EClassifier currentType = null;
		    int upperBound = 1;

			if(elem.getValue() instanceof Collection) {
			    upperBound = -1;
				// check collection consistency
				for(Object o : (Collection)elem.getValue()) {
					// allow null objects
					if(o != null) {
						EClassifier oType = getEType(o);
						if(oType == null) {
							throw new IOException("Unsupported value type");
						} else if(oType instanceof EDataType && !oType.equals(oType)) {
							throw new IOException("Incoherent collection (values of different types)");
						} else if(oType instanceof EClass) {
						    if (currentType == null) {
						        currentType = oType;
						    } else if(currentType instanceof EClass) {
						        if (!((EClass)currentType).isSuperTypeOf(eClass)) {
						            if (((EClass)oType).isSuperTypeOf((EClass) currentType)) {
						                currentType = oType;
						            } else {
						                throw new IOException("Incoherent collection (values of different types)");
						            }
						        }
						    } else {
						        throw new IOException("Incoherent collection (values of different types)");
						    }
						}
					}
				}
			} else {
			    currentType = getEType(elem.getValue());
			}
			
			EStructuralFeature feature = null;

			if (currentType instanceof EClass) {
			    feature = EcoreFactory.eINSTANCE.createEReference();
			} else if (currentType instanceof EDataType) {
			    feature = EcoreFactory.eINSTANCE.createEAttribute();
			}
			
			

			if(feature == null) {
				throw new IOException("Unsupported value type");
			}

			feature.setName(elem.getKey());
			feature.setUpperBound(upperBound);
			feature.setEType(currentType);

			eClass.getEStructuralFeatures().add(feature);
		}

		URI mmFileUri = getConfigurationFileUri(context, scope, id, "ecore");
		Resource mmResource = rs.createResource(mmFileUri);
		mmResource.getContents().add(ePackage);
		mmResource.save(null);

		EObject confEObj = eFactory.create(eClass);

		for(Entry<String, Object> elem : conf.entrySet()) {
		    Object value = elem.getValue();
		    if(value instanceof Collection) {
		        List l = (List)confEObj.eGet(eClass.getEStructuralFeature(elem.getKey()));
		        for (Object o : (Collection)value) {
		            if (o instanceof EObject && !isSelfContained((EObject) o)) {
		                throw new IOException("The configuration object have reference(s) outside of itself and its children");
		            }
		            l.add(o);
		        }


		    } else {
                if (value instanceof EObject && !isSelfContained((EObject) value)) {
                    throw new IOException("The configuration object have reference(s) outside of itself and its children");
                }
		        confEObj.eSet(eClass.getEStructuralFeature(elem.getKey()), value);
		    }
		}

		saveConfiguration(confEObj, context, scope, id);
	}

	protected EClassifier getEType(Object obj) {
	    if (obj instanceof EObject) {
	        return ((EObject) obj).eClass();
	    } else if(obj instanceof String) {
			return EcorePackage.Literals.ESTRING;
		} else if(obj instanceof Integer) {
			return EcorePackage.Literals.EINTEGER_OBJECT;
		} else if(obj instanceof Long) {
			return EcorePackage.Literals.ELONG_OBJECT;
		} else if(obj instanceof Boolean) {
			return EcorePackage.Literals.EBOOLEAN_OBJECT;
		} else if(obj instanceof Double) {
			return EcorePackage.Literals.EDOUBLE_OBJECT;
		} else if(obj instanceof Float) {
			return EcorePackage.Literals.EFLOAT_OBJECT;
		}
		return null;
	}

}
