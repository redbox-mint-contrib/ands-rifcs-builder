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

package au.com.redboxresearchdata.rifcs.ands.builder.sub.impl;

import au.com.redboxresearchdata.rifcs.ands.builder.RifcsBuilder;
import org.ands.rifcs.base.RIFCSException;
import org.ands.rifcs.base.Rights;

import java.util.Map;

/**
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 *         created on 24/05/16.
 */
public class RifcsRightsBuilder extends RifcsGenericSubBuilder<Rights> {

    public RifcsRightsBuilder(RifcsBuilder ownerBuilder, Rights rights) throws RIFCSException {
        super(ownerBuilder, rights);
    }

    public RifcsRightsBuilder accessRights(final Map<String, String> data) throws RIFCSException {
        accessRights(data.get("value"), data.get("uri"));
        return this;
    }

    public RifcsRightsBuilder accessRights(final String value, final String uri) throws RIFCSException {
        delegate.setAccessRights(value, uri, null);
        return this;
    }

    public RifcsRightsBuilder licence(final Map<String, String> data) throws RIFCSException {
        licence(data.get("value"), data.get("uri"));
        return this;
    }

    public RifcsRightsBuilder licence(final String value, final String uri) throws RIFCSException {
        delegate.setLicence(value, uri, null);
        return this;
    }

    public RifcsRightsBuilder rightsStatement(final Map<String, String> data) throws RIFCSException {
        rightsStatement(data.get("value"), data.get("uri"));
        return this;
    }

    public RifcsRightsBuilder rightsStatement(final String value, final String uri) throws RIFCSException {
        delegate.setRightsStatement(value, uri);
        return this;
    }

}
