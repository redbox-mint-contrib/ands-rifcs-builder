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

import java.util.Date;

/**
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 *         created on 13/05/16.
 */
public class RifcsTemporalCoverageBuilder extends RifcsGenericSubBuilder<Coverage> {

    public RifcsTemporalCoverageBuilder(RifcsBuilder ownerBuilder, Coverage coverage) throws RIFCSException {
        super(ownerBuilder, coverage);
    }

    public RifcsTemporalCoverageBuilder coverageDateFrom(Date date) throws RIFCSException {
        delegate.addTemporalDate(date, "dateFrom");
        return this;
    }

    public RifcsTemporalCoverageBuilder coverageDateTo(Date date) throws RIFCSException {
        delegate.addTemporalDate(date, "dateTo");
        return this;
    }

    public RifcsTemporalCoverageBuilder coveragePeriod(String period) throws RIFCSException {
        delegate.addTemporal(period);
        return this;
    }

}
