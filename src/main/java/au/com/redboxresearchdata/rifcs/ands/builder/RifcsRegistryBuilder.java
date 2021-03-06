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

package au.com.redboxresearchdata.rifcs.ands.builder;

import au.com.redboxresearchdata.rifcs.ands.builder.sub.impl.*;
import org.ands.rifcs.base.RIFCSException;

/**
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 *         created on 13/05/16.
 */
public interface RifcsRegistryBuilder extends RifcsBuilder {
    RifcsLocationBuilder locationBuilder() throws org.ands.rifcs.base.RIFCSException;

    RifcsTemporalCoverageBuilder temporalCoverageBuilder() throws RIFCSException;

    RifcsSpatialCoverageBuilder spatialCoverageBuilder() throws RIFCSException;

    RifcsRightsBuilder rightsBuilder() throws RIFCSException;

    RifcsRelatedInfoBuilder relatedInfoBuilder() throws RIFCSException;
}
