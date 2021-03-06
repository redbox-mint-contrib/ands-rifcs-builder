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

import org.ands.rifcs.base.Collection;
import org.ands.rifcs.base.Dates;
import org.ands.rifcs.base.RIFCSException;
import org.ands.rifcs.base.RIFCSWrapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 *         created on 12/05/16.
 */
public class RifcsCollectionBuilder extends RifcsGenericBuilder<Collection> {
    private static final String RIFDateTimeFormat = "W3CDTF";

    public RifcsCollectionBuilder(final String key, final String originatingSource, final String group, final String type) throws RIFCSException {
        super(key, originatingSource, group, type);
    }

    @Override
    protected Collection init(final String type) throws RIFCSException {
        Collection collection = coreBuilder.getRegistry().newCollection();
        collection.setType(type);
        return collection;
    }


    public RifcsCollectionBuilder dateAccessioned(final String date) {
        delegate.setDateAccessioned(date);
        return this;
    }

    public RifcsCollectionBuilder dateCreated(final String date) throws RIFCSException {
        Dates dates = delegate.newDates();
        dates.setType("dc.created");
        dates.addDate(date, "dateFrom", RIFDateTimeFormat);
        delegate.addDates(dates);
        return this;
    }


    @Override
    public RifcsCollectionBuilder identifier(final String identifier, final String identifierType) throws RIFCSException {
        coreBuilder.identifier(identifier, identifierType);
        return this;
    }

    @Override
    public RifcsCollectionBuilder identifier(final Map<String, String> data) throws RIFCSException {
        coreBuilder.identifier(data);
        return this;
    }

    @Override
    public RifcsCollectionBuilder primaryName(final String name) throws RIFCSException {
        coreBuilder.primaryName(name);
        return this;
    }

    @Override
    public RifcsCollectionBuilder dateModified(final Date date) {
        coreBuilder.dateModified(date);
        return this;
    }

    @Override
    public RifcsCollectionBuilder dateModified(final String date) {
        coreBuilder.dateModified(date);
        return this;
    }

    @Override
    public RifcsCollectionBuilder relatedObject(final Map<String, String> data) throws RIFCSException {
        coreBuilder.relatedObject(data);
        return this;
    }

    @Override
    public RifcsCollectionBuilder relatedObjects(final String key, final List<Map<String, String>> dataList) throws RIFCSException {
        coreBuilder.relatedObjects(key, dataList);
        return this;
    }

    @Override
    public RifcsCollectionBuilder subject(final Map<String, String> data) {
        coreBuilder.subject(data);
        return this;
    }

    @Override
    public RifcsCollectionBuilder subject(final String value, final String type) {
        coreBuilder.subject(value, type);
        return this;
    }

    @Override
    public RifcsCollectionBuilder description(final Map<String, String> data) {
        coreBuilder.description(data);
        return this;
    }

    @Override
    public RifcsCollectionBuilder description(final String type, final String value) {
        coreBuilder.description(type, value);
        return this;
    }

    @Override
    public RifcsCollectionBuilder relatedInfo(final Map<String, String> data) throws RIFCSException {
        coreBuilder.relatedInfo(data);
        return this;
    }

    @Override
    public RifcsCollectionBuilder fullCitation(final Map<String, String> data) throws RIFCSException {
        coreBuilder.fullCitation(data);
        return this;
    }

    @Override
    public RifcsCollectionBuilder fullCitation(final String citation, final String style) throws RIFCSException {
        coreBuilder.fullCitation(citation, style);
        return this;
    }

    @Override
    public RIFCSWrapper build() throws RIFCSException {
        coreBuilder.getRegistry().addCollection(delegate);
        return coreBuilder.build();
    }


}