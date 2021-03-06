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
package br.net.buzu4j;

import br.net.buzu4j.api.MetaclassReader;
import br.net.buzu4j.api.MetadataLoader;
import br.net.buzu4j.context.ContextBuilder;
import br.net.buzu4j.context.JavaContext;
import br.net.buzu.ext.MetadataParser;
import br.net.buzu.ext.SkipStrategy;
import br.net.buzu4j.metaclass.AnnotationSkipStrategy;
import br.net.buzu4j.metaclass.BasicMetaclassReader;
import br.net.buzu4j.metaclass.BasicSkipStrategy;
import br.net.buzu4j.metadata.build.load.BasicMetadataLoader;
import br.net.buzu4j.metadata.build.parse.BasicMetadataParser;
import br.net.buzu4j.metadata.build.parse.Splitter;
import br.net.buzu.model.Dialect;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Builder to specific PplSimpleMapper (Buzu).
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class BuzuBuilder {

	private JavaContext context;
	private Splitter splitter;
	private MetadataParser metadataParser;
	private SkipStrategy skipStrategy;
	private Set<Class<? extends Annotation>> ignoreAnnotations = new HashSet<>();
	private Set<Class<? extends Annotation>> useAnnotations = new HashSet<>();
	private MetaclassReader metaclassReader;
	private MetadataLoader metadataLoader;
	private Dialect dialect;
	private boolean serializaNulls;

	/**
	 * Build and returns a instance domainOf <code>Buzu</code>.
	 * 
	 * @return The instance domainOf <code>Buzu</code> created based on internal
	 *         objects.
	 */
	public Buzu build() {
		if (context == null) {
			context = new ContextBuilder().build();
		}
		if (splitter == null) {
			splitter = new Splitter();
		}
		if (metadataParser == null) {
			metadataParser = new BasicMetadataParser(context, splitter);
		}
		if (skipStrategy == null) {
			skipStrategy = new BasicSkipStrategy();
		}
		if (useCustomSkipStrategy()) {
			skipStrategy = new AnnotationSkipStrategy(ignoreAnnotations, useAnnotations, skipStrategy);
		}
		if (metaclassReader == null) {
			metaclassReader = new BasicMetaclassReader(context, skipStrategy);
		}
		if (metadataLoader == null) {
			metadataLoader = new BasicMetadataLoader(context);
		}
		if (dialect == null) {
			dialect = Buzu.DEFAULT_DIALECT;
		}
		return new Buzu(context, metadataParser, metaclassReader, metadataLoader, dialect, serializaNulls);
	}
	
	private boolean useCustomSkipStrategy(){
		return !ignoreAnnotations.isEmpty() || !useAnnotations.isEmpty();
	}


	public JavaContext getContext() {
		return context;
	}

	public BuzuBuilder context(JavaContext context) {
		this.context = context;
		return this;
	}

	public MetadataParser getMetadataParser() {
		return metadataParser;
	}

	public BuzuBuilder metadataParser(MetadataParser metadataParser) {
		this.metadataParser = metadataParser;
		return this;
	}

	public Splitter getSplitter() {
		return splitter;
	}

	public BuzuBuilder splitter(Splitter splitter) {
		this.splitter = splitter;
		return this;
	}

	public MetadataLoader getMetadataLoader() {
		return metadataLoader;
	}

	public BuzuBuilder metadataLoader(MetadataLoader metadataLoader) {
		this.metadataLoader = metadataLoader;
		return this;
	}

	public MetaclassReader getMetaclassReader() {
		return metaclassReader;
	}

	public BuzuBuilder metaclassReader(MetaclassReader metaclassLoader) {
		this.metaclassReader = metaclassLoader;
		return this;
	}

	public SkipStrategy getSkipStrategy() {
		return skipStrategy;
	}

	public BuzuBuilder skipStrategy(SkipStrategy skip) {
		this.skipStrategy = skip;
		return this;
	}

	public BuzuBuilder addIgnore(Class<? extends Annotation> annotation) {
		ignoreAnnotations.add(annotation);
		return this;
	}

	public BuzuBuilder addUse(Class<? extends Annotation> annotation) {
		useAnnotations.add(annotation);
		return this;
	}

	public Dialect getDialect() {
		return dialect;
	}

	public BuzuBuilder dialect(Dialect dialect) {
		this.dialect = dialect;
		return this;
	}

	public boolean isSerializaNulls() {
		return serializaNulls;
	}

	public BuzuBuilder serializaNulls(boolean serializaNulls) {
		this.serializaNulls = serializaNulls;
		return this;
	}

}
