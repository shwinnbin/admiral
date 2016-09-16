/*
 * Copyright (c) 2016 VMware, Inc. All Rights Reserved.
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with separate copyright notices
 * and license terms. Your use of these subcomponents is subject to the terms and
 * conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package com.vmware.admiral.compute.content;

import static com.vmware.admiral.common.util.AssertUtil.assertNotNull;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import com.vmware.admiral.compute.ResourceType;
import com.vmware.admiral.compute.container.CompositeComponentRegistry;
import com.vmware.photon.controller.model.resources.ComputeDescriptionService;

public class ComponentTemplateDeserializer extends StdDeserializer<ComponentTemplate<?>> {

    private static final long serialVersionUID = 1L;

    public ComponentTemplateDeserializer() {
        this(ComponentTemplate.class);
    }

    protected ComponentTemplateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ComponentTemplate<?> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        Map<?, ?> templateMap = p.readValueAs(Map.class);
        String type = convertType(templateMap.get("type"));
        ComponentTemplate<?> template = createTemplate(type, templateMap.get("data"));
        template.dependsOn = convertDepdensOn(templateMap.get("dependsOn"));
        return template;
    }

    private static String convertType(Object type) {
        assertNotNull(type, "type");
        return new ObjectMapper().convertValue(type, String.class);
    }

    @SuppressWarnings("unchecked")
    private static <T> ComponentTemplate<T> createTemplate(String type, Object data) {
        assertNotNull(data, "data");
        ComponentTemplate<T> template = new ComponentTemplate<>();
        template.type = type;
        template.data = (T) new ObjectMapper().convertValue(data, getDescriptionClass(type));
        return template;
    }

    private static String[] convertDepdensOn(Object dependsOn) {
        if (dependsOn == null) {
            return null;
        }
        return new ObjectMapper().convertValue(dependsOn, String[].class);
    }

    public static Class<?> getDescriptionClass(String type) {
        ResourceType resourceType = ResourceType.fromContentType(type);
        Class<?> clazz = CompositeComponentRegistry
                .metaByType(resourceType.getName()).descriptionClass;
        if (ComputeDescriptionService.ComputeDescription.class.equals(clazz)) {
            return ComputeDescription.class;
        }
        return clazz;
    }

}
