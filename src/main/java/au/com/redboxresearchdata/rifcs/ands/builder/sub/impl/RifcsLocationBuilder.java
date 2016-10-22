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
import org.ands.rifcs.base.*;

/**
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 *         created on 13/05/16.
 */
public class RifcsLocationBuilder extends RifcsGenericSubBuilder<Location> {

    public RifcsLocationBuilder(RifcsBuilder ownerBuilder, Location location) throws RIFCSException {
        super(ownerBuilder, location);
    }

    public RifcsLocationBuilder physicalAddress(final String value) throws RIFCSException {
        return physicalAddress(value, "text");
    }

    public RifcsLocationBuilder physicalAddress(final String value, final String type) throws RIFCSException {
        Address address = createAddress();
        Physical physical = address.newPhysical();
        address.addPhysical(physical);
        AddressPart addressPart = physical.newAddressPart();
        physical.addAddressPart(addressPart);
        addressPart.setType(type);
        addressPart.setValue(value);
        return this;
    }

    public RifcsLocationBuilder urlElectronicAddress(final String value) throws RIFCSException {
        return electronicAddress(value, "url");
    }

    public RifcsLocationBuilder emailElectronicAddress(final String value) throws RIFCSException {
        return electronicAddress(value, "email");
    }

    public RifcsLocationBuilder electronicAddress(final String value, final String type) throws RIFCSException {
        Address address = createAddress();
        Electronic electronic = address.newElectronic();
        address.addElectronic(electronic);
        electronic.setType(type);
        electronic.setValue(value);
        return this;
    }

    private Address createAddress() throws RIFCSException {
        Address address = delegate.newAddress();
        delegate.addAddress(address);
        return address;
    }

}
