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
package br.net.buzu4j.parsing.complex;

import br.net.buzu4j.api.PayloadMapper;
import br.net.buzu.exception.PplParseException;
import br.net.buzu4j.model.Metaclass;
import br.net.buzu.model.StaticMetadata;

import java.util.Collections;
import java.util.List;

/**
 *
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class ComplexMapper extends AbstractComplexMapper {

	protected final List<PayloadMapper> children;

	public ComplexMapper(List<PayloadMapper> children) {
		super();
		this.children = Collections.unmodifiableList(children);
	}

	// ******************** PARSE ********************

	@Override
	protected Object doParse(StaticMetadata metadata, String text, Metaclass toClass) {
		Object[] array = createAndFillArray(toClass, metadata.info().getMaxOccurs());
		int beginIndex = 0;
		int endIndex = 0;
		Object parsed = null;
		Metaclass metaclassChild;
		StaticMetadata metadataChild;
		List<StaticMetadata> staticMetadataChildren = metadata.children();
		PayloadMapper parserChild;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < children.size(); j++) {
				metadataChild = staticMetadataChildren.get(j);
				parserChild = children.get(j);
				// Def types
				metaclassChild = getMetaclassChild(metadataChild, toClass);
				// Split and parseMetadata
				endIndex += metadataChild.serialMaxSize();
				parsed = parserChild.parse(metadataChild, text.substring(beginIndex, endIndex), metaclassChild);
				beginIndex += metadataChild.serialMaxSize();
				// set parsed into field
				if (metaclassChild.isPrimitive()) {
					if (parsed == null) {
						continue;
					}
					if (metaclassChild.elementType().equals(char.class)) {
						parsed = parsed.toString().charAt(0);
					}
				}
				callSet(array[i], metaclassChild, parsed);
			}
		}
		return fromArray(array, toClass);
	}

	// ******************** SERIALIZE ********************

	@Override
	protected String serializeNotNull(StaticMetadata metadata, Object obj, Metaclass fromClass) {
		StringBuilder sb = new StringBuilder();
		Object[] array = toMaxArray(obj, metadata.info().getMaxOccurs());
		StaticMetadata metadataChild;
		PayloadMapper parserChild;
		Metaclass metaclassChild;
		List<StaticMetadata> staticMetadataChildren = metadata.children();
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < children.size(); j++) {
				metadataChild = staticMetadataChildren.get(j);
				parserChild = children.get(j);
				metaclassChild = fromClass.getChildByName(metadataChild.info().getName());
				if (array[i] != null) {
					
					sb.append(parserChild.serialize(metadataChild, metaclassChild.get(array[i]),
							metaclassChild));
				} else {
					sb.append(serializeNull(metadataChild));
				}
			}
		}
		return sb.toString();
	}

	// ******************** COMMON ********************

	private Metaclass getMetaclassChild(StaticMetadata metadataChild, Metaclass metaclass) {
		Metaclass metaclassChild = metaclass.getChildByName(metadataChild.name());
		if (metaclassChild == null) {
			throw new PplParseException("Field '" + metadataChild.name() + "' is missing in the Metaclass of '"
					+ metaclass.elementType() + "':\n" + metaclass.toTree(0));
		}
		return metaclassChild;
	}

}
