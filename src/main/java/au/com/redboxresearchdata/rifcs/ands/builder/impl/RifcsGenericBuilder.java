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

import au.com.redboxresearchdata.rifcs.ands.builder.RifcsRegistryBuilder;
import au.com.redboxresearchdata.rifcs.ands.builder.sub.impl.*;
import org.ands.rifcs.base.RIFCSElement;
import org.ands.rifcs.base.RIFCSException;

/**
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 *         created on 12/05/16.
 */

public abstract class RifcsGenericBuilder<T extends RIFCSElement> implements RifcsRegistryBuilder {
    protected final RifcsCoreBuilder<T> coreBuilder;
    protected final T delegate;

    public RifcsGenericBuilder(final String key, final String originatingSource, final String group, final String type) throws RIFCSException {
        coreBuilder = new RifcsCoreBuilder<>(key, originatingSource, group);
        delegate = init(type);
        coreBuilder.setDelegate(delegate);
    }

    abstract protected T init(final String type) throws RIFCSException;

    @Override
    public RifcsLocationBuilder locationBuilder() throws RIFCSException {
        return new RifcsLocationBuilder(this, coreBuilder.createLocation());
    }

    @Override
    public RifcsTemporalCoverageBuilder temporalCoverageBuilder() throws RIFCSException {
        return new RifcsTemporalCoverageBuilder(this, coreBuilder.createCoverage());
    }

    @Override
    public RifcsSpatialCoverageBuilder spatialCoverageBuilder() throws RIFCSException {
        return new RifcsSpatialCoverageBuilder(this, coreBuilder.createCoverage());
    }

    @Override
    public RifcsRightsBuilder rightsBuilder() throws RIFCSException {
        return new RifcsRightsBuilder(this, coreBuilder.createRights());
    }

    @Override
    public RifcsRelatedInfoBuilder relatedInfoBuilder() throws RIFCSException {
        return new RifcsRelatedInfoBuilder(this, coreBuilder.createRelatedInfo());
    }

}
