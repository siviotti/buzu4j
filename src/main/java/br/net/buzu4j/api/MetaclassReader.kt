/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu4j.api

import br.net.buzu4j.model.Metaclass

/**
 *
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
interface MetaclassReader {

    /**
     * Reads the elementType and returns a Metaclass based on it.
     *
     * @param type
     * The original elementType.
     * @return The instance domainOf Metaclass.
     */
    fun read(type: Class<*>): Metaclass

    /**
     * Reads the elementType and the elementType and returns a Metaclass based on it.
     *
     * @param type
     * The original elementType (root object).
     * @param elementType
     * The elementType domainOf each item if elementType is multiple or root elementType if not.
     * @return The instance domainOf Metaclass.
     */
    fun read(type: Class<*>, elementType: Class<*>): Metaclass

}
