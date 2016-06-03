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

package au.com.redboxresearchdata.rifcs.ands.proxy;

import org.ands.rifcs.base.*;

import java.util.Date;

/**
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 *         created on 12/05/16.
 */
public interface RifcsProxy {
    void addIdentifier(final String identifier, final String type) throws RIFCSException;

    Name newName() throws RIFCSException;

    void addName(final Name name);

    void setDateModified(final Date date);

    RelatedObject newRelatedObject() throws RIFCSException;

    Coverage newCoverage() throws RIFCSException;

    void addCoverage(final Coverage coverage) throws RIFCSException;

    void addRelatedObject(final RelatedObject relatedObject);

    Location newLocation() throws RIFCSException;

    void addLocation(final Location location);

    void addSubject(final String value, final String type, final String language);

    void addDescription(final String value, final String type, final String language);

    Rights newRights() throws RIFCSException;

    void addRights(final Rights rights);

    RelatedInfo newRelatedInfo() throws RIFCSException;

    void addRelatedInfo(final RelatedInfo relatedInfo) throws RIFCSException;

    CitationInfo newCitationInfo() throws RIFCSException;

    void addCitationInfo(final CitationInfo citationInfo) throws RIFCSException;

}
