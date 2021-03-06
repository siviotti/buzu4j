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
package br.net.buzu4j.context;

import br.net.buzu.ext.MetadataCoderResolver;
import br.net.buzu.ext.MetadataFactory;

import java.util.Objects;

/**
 * Context domainOf parsing/serialization with Factories used to create objects and
 * others extension points.
 * 
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class BasicContext implements JavaContext {

	private final SubtypeManager subtypeManager;
	private final MetadataFactory metadataFactory;
	private final MetadataCoderResolver metadataCoderResolver;
	private final ParserFactory parserFactory;

	public BasicContext() {
		this(BasicSubtypeManager.INSTANCE, BasicMetadataFactory.INSTANCE, BasicMetadataCoderResolver.INSTANCE,
				BasicParserFactory.INSTANCE);
	}

	public BasicContext(SubtypeManager subtypeManager, MetadataFactory metadataFactory,
                        MetadataCoderResolver metadataCoderResolver, ParserFactory parserFactory) {
		super();
		this.subtypeManager = Objects.requireNonNull(subtypeManager, "subtypeManager cannot be null");
		this.metadataFactory = Objects.requireNonNull(metadataFactory, "metadataFactory cannot be null");
		this.metadataCoderResolver = Objects.requireNonNull(metadataCoderResolver, "metadataCoderResolver cannot be null");
		this.parserFactory = Objects.requireNonNull(parserFactory, "parserFactory cannot be null");
	}

	// **************************************************
	// API
	// **************************************************

	@Override
	public SubtypeManager subtypeManager() {
		return subtypeManager;
	}

	@Override
	public MetadataFactory metadataFactory() {
		return metadataFactory;
	}

	@Override
	public MetadataCoderResolver coderManager() {
		return metadataCoderResolver;
	}

	@Override
	public ParserFactory parserFactory() {
		return parserFactory;
	}

}
