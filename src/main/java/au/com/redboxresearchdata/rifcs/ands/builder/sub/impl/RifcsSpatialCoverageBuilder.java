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
import org.ands.rifcs.base.Spatial;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 *         created on 13/05/16.
 */
public class RifcsSpatialCoverageBuilder extends RifcsGenericSubBuilder<Coverage> {

    public RifcsSpatialCoverageBuilder(RifcsBuilder ownerBuilder, Coverage coverage) throws RIFCSException {
        super(ownerBuilder, coverage);
    }

    public RifcsSpatialCoverageBuilder spatial(List<Map<String, String>> listOfMaps) throws RIFCSException {
        for (Map<String, String> map : listOfMaps) {
            spatial(map);
        }
        return this;
    }

    public RifcsSpatialCoverageBuilder spatial(Map<String, String> map) throws RIFCSException {
        spatial(map.get("type"), map.get("value"));
        return this;
    }

    public RifcsSpatialCoverageBuilder spatial(String type, String value) throws RIFCSException {
        Spatial spatial = createSpatial();
        spatial.setLanguage("en");
        spatial.setType(type);
        spatial.setValue(value);
        return this;
    }

    private Spatial createSpatial() throws RIFCSException {
        Spatial spatial = delegate.newSpatial();
        delegate.addSpatial(spatial);
        return spatial;
    }

}
