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

import org.ands.rifcs.base.RIFCSElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 *         created on 12/05/16.
 */
public class RifcsInvocationHandler<T extends RIFCSElement> implements InvocationHandler {
    private static Logger LOG = LoggerFactory.getLogger(RifcsInvocationHandler.class);
    private T delegate;

    public RifcsInvocationHandler(T delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LOG.debug("invoking au.com.redboxresearchdata.rifcs invocation handler...");
        Method delegateMethod = delegate.getClass().getMethod(method.getName(), getArgTypes(args));
        return delegateMethod.invoke(delegate, args);
    }

    private Class[] getArgTypes(Object[] args) {
        Class[] types = {};
        if (args instanceof Object[]) {
            types = new Class[args.length];
            int typeCounter = 0;
            for (Object arg : args) {
                types[typeCounter++] = arg.getClass();
            }
        }
        return types;
    }
}
