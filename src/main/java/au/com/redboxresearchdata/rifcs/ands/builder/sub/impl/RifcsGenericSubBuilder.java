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
import au.com.redboxresearchdata.rifcs.ands.builder.sub.RifcsSubBuilder;
import org.ands.rifcs.base.RIFCSElement;
import org.ands.rifcs.base.RIFCSException;

/**
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 *         created on 13/05/16.
 */
public abstract class RifcsGenericSubBuilder<T extends RIFCSElement> implements RifcsSubBuilder {
    protected final RifcsBuilder ownerBuilder;
    protected final T delegate;

    protected RifcsGenericSubBuilder(RifcsBuilder ownerBuilder, T delegate) throws RIFCSException {
        this.ownerBuilder = ownerBuilder;
        this.delegate = delegate;
    }

    @Override
    public RifcsBuilder build() {
        return ownerBuilder;
    }
}
