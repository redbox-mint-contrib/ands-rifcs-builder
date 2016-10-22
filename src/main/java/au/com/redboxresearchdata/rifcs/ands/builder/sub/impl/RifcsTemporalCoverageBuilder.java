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
import org.ands.rifcs.base.Coverage;
import org.ands.rifcs.base.RIFCSException;
import org.ands.rifcs.base.Temporal;

import java.util.Date;

/**
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 *         created on 13/05/16.
 */
public class RifcsTemporalCoverageBuilder extends RifcsGenericSubBuilder<Coverage> {

    public RifcsTemporalCoverageBuilder(RifcsBuilder ownerBuilder, Coverage coverage) throws RIFCSException {
        super(ownerBuilder, coverage);
    }

    public RifcsTemporalCoverageBuilder coverageDateFrom(String date) throws RIFCSException {
        return coverageDate(date, "dateFrom");
    }

    public RifcsTemporalCoverageBuilder coverageDateTo(String date) throws RIFCSException {
        return coverageDate(date, "dateTo");
    }

    public RifcsTemporalCoverageBuilder coverageDate(String date, String type) throws RIFCSException {
        Temporal temporal = delegate.newTemporal();
        delegate.addTemporal(temporal);
        delegate.addTemporalDate(date, type);
        return this;
    }

    public RifcsTemporalCoverageBuilder coveragePeriod(String period) throws RIFCSException {
        delegate.addTemporal(period);
        return this;
    }

}
