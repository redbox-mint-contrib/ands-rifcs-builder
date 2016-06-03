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
import org.ands.rifcs.base.RelatedInfo;

/**
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 *         created on 25/05/16.
 */
public class RifcsRelatedInfoBuilder extends RifcsGenericSubBuilder<RelatedInfo> {

    public RifcsRelatedInfoBuilder(RifcsBuilder ownerBuilder, RelatedInfo delegate) throws RIFCSException {
        super(ownerBuilder, delegate);
    }

    public RifcsRelatedInfoBuilder type(final String type) {
        delegate.setType(type);
        return this;
    }

    public RifcsRelatedInfoBuilder identifier(final String identifier, final String identifierType) throws RIFCSException {
        delegate.addIdentifier(identifier, identifierType);
        return this;
    }

    public RifcsRelatedInfoBuilder title(final String title) throws RIFCSException {
        delegate.setTitle(title);
        return this;
    }

    public RifcsRelatedInfoBuilder notes(final String notes) throws RIFCSException {
        delegate.setNotes(notes);
        return this;
    }
}
