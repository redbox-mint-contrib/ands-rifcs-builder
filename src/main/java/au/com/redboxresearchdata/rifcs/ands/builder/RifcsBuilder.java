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

import org.ands.rifcs.base.RIFCSException;
import org.ands.rifcs.base.RIFCSWrapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 *         created on 12/05/16.
 */
public interface RifcsBuilder {
    RifcsBuilder identifierType(final String identifier, final String identifierType) throws RIFCSException;

    RifcsBuilder primaryName(final String name) throws RIFCSException;

    RifcsBuilder dateModified(final Date date);

    RifcsBuilder relatedObject(final Map<String, String> relationData) throws RIFCSException;

    RifcsBuilder relatedObjects(final String key, final List<Map<String, String>> dataList) throws RIFCSException;

    RifcsBuilder subject(final String value, final String type);

    RifcsBuilder subject(final Map<String, String> data);

    RifcsBuilder description(final String type, final String value);

    RifcsBuilder description(final Map<String, String> data);

    RifcsBuilder relatedInfo(final Map<String, String> data) throws RIFCSException;

    RifcsBuilder fullCitation(final Map<String, String> data) throws RIFCSException;

    RifcsBuilder fullCitation(final String citation, final String style) throws RIFCSException;

    RIFCSWrapper build() throws RIFCSException;
}
