/*
 *
 *  Copyright (C) 2016 Queensland Cyber Infrastructure Foundation (http://www.qcif.edu.au/)
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License along
 *    with this program; if not, write to the Free Software Foundation, Inc.,
 *    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * /
 */

package au.com.redboxresearchdata.rifcs.ands.builder.impl;

import au.com.redboxresearchdata.rifcs.ands.builder.RifcsBuilder;
import au.com.redboxresearchdata.rifcs.ands.builder.sub.impl.RifcsRelatedInfoBuilder;
import au.com.redboxresearchdata.rifcs.ands.proxy.RifcsInvocationHandler;
import au.com.redboxresearchdata.rifcs.ands.proxy.RifcsProxy;
import org.ands.rifcs.base.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 *         created on 12/05/16.
 */
public class RifcsCoreBuilder<T extends RIFCSElement> implements RifcsBuilder {
    private static Logger LOG = LoggerFactory.getLogger(RifcsCoreBuilder.class);
    private final RIFCSWrapper wrapper;
    private final RIFCS rifcs;
    private final RegistryObject registry;
    private RifcsProxy proxy;

    protected RifcsCoreBuilder(final String key, final String originatingSource,
                               final String group
    ) throws RIFCSException {
        wrapper = new RIFCSWrapper();
        rifcs = wrapper.getRIFCSObject();
        registry = rifcs.newRegistryObject();
        registry.setKey(key);
        registry.setOriginatingSource(originatingSource);
        registry.setGroup(group);
    }

    protected RegistryObject getRegistry() {
        return registry;
    }

    protected void setDelegate(final T delegate) {
        InvocationHandler handler = new RifcsInvocationHandler<>(delegate);
        proxy = (RifcsProxy) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{RifcsProxy.class}, handler);
    }

    @Override
    public RifcsCoreBuilder identifier(final String identifier, final String identifierType) throws RIFCSException {
        proxy.addIdentifier(identifier, identifierType);
        return this;
    }

    @Override
    public RifcsCoreBuilder identifier(final Map<String, String> data) throws RIFCSException {
        return identifier(data.get("value"), data.get("type"));
    }

    @Override
    public RifcsCoreBuilder primaryName(final String name) throws RIFCSException {
        Name primaryName = proxy.newName();
        primaryName.setType("primary");
        primaryName.setLanguage("en");
        NamePart namePart = primaryName.newNamePart();
        namePart.setValue(name);
        primaryName.addNamePart(namePart);
        proxy.addName(primaryName);
        return this;
    }

    @Override
    public RifcsCoreBuilder dateModified(final Date date) {
        proxy.setDateModified(date);
        return this;
    }

    @Override
    public RifcsCoreBuilder relatedObject(final Map<String, String> data) throws RIFCSException {
        RelatedObject relatedObject = proxy.newRelatedObject();
        proxy.addRelatedObject(relatedObject);
        relatedObject.setKey(data.get("key"));
        relatedObject.addRelation(data.get("type"), data.get("url"), data.get("description"), data.get("descriptionLanguage"));
        return this;
    }

    @Override
    public RifcsCoreBuilder relatedObjects(final String key, final List<Map<String, String>> dataList) throws RIFCSException {
        RelatedObject relatedObject = proxy.newRelatedObject();
        proxy.addRelatedObject(relatedObject);
        relatedObject.setKey(key);
        for (Map<String, String> data : dataList) {
            relatedObject.addRelation(data.get("type"), data.get("url"), data.get("description"), data.get("descriptionLanguage"));
        }
        return this;
    }

    @Override
    public RifcsCoreBuilder subject(final Map<String, String> data) {
        return subject(data.get("type"), data.get("value"));
    }

    @Override
    public RifcsCoreBuilder subject(final String type, final String value) {
        proxy.addSubject(value, type, "en");
        return this;
    }

    @Override
    public RifcsCoreBuilder description(final Map<String, String> data) {
        return description(data.get("type"), data.get("value"));
    }

    @Override
    public RifcsCoreBuilder description(final String type, final String value) {
        proxy.addDescription(value, type, "en");
        return this;
    }

    @Override
    public RifcsBuilder relatedInfo(final Map<String, String> data) throws RIFCSException {
        RifcsRelatedInfoBuilder builder = new RifcsRelatedInfoBuilder(this, createRelatedInfo());
        if (data.get("title") instanceof String) {
            builder = builder.title(data.get("title"));
        }
        if (data.get("notes") instanceof String) {
            builder = builder.notes(data.get("notes"));
        }
        return builder
                .type(data.get("type"))
                .identifier(data.get("identifier"), data.get("identifierType"))
                .build();
    }

    @Override
    public RifcsCoreBuilder fullCitation(final Map<String, String> data) throws RIFCSException {
        return fullCitation(data.get("citation"), data.get("style"));
    }

    @Override
    public RifcsCoreBuilder fullCitation(final String citation, final String style) throws RIFCSException {
        CitationInfo citationInfo = proxy.newCitationInfo();
        proxy.addCitationInfo(citationInfo);
        citationInfo.setCitation(citation, style);
        return this;
    }

    protected Coverage createCoverage() throws RIFCSException {
        Coverage coverage = proxy.newCoverage();
        proxy.addCoverage(coverage);
        return coverage;
    }

    protected Location createLocation() throws RIFCSException {
        Location location = proxy.newLocation();
        proxy.addLocation(location);
        return location;
    }

    protected Rights createRights() throws RIFCSException {
        Rights rights = proxy.newRights();
        proxy.addRights(rights);
        return rights;
    }

    protected RelatedInfo createRelatedInfo() throws RIFCSException {
        RelatedInfo relatedInfo = proxy.newRelatedInfo();
        proxy.addRelatedInfo(relatedInfo);
        return relatedInfo;
    }

    @Override
    public RIFCSWrapper build() throws RIFCSException {
        register();
        return wrapper;
    }

    private void register() throws RIFCSException {
        if (StringUtils.isBlank(registry.getObjectClassName())) {
            throw new IllegalStateException("Must register either an activity, collection, party or service.");
        }
        rifcs.addRegistryObject(registry);
    }

}
