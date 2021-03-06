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
package br.net.buzu4j.metadata.build.parse;

import br.net.buzu4j.context.BasicContext;
import br.net.buzu4j.context.JavaContext;
import br.net.buzu.exception.PplParseException;
import br.net.buzu.ext.MetadataParser;
import br.net.buzu.lang.Token;
import br.net.buzu.model.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.net.buzu.lang.Syntax.*;
import static br.net.buzu.model.Domains.createDomain;
import static br.net.buzu.model.Domains.toDomainItemList;

/**
 * Metadata parser to transform <code>Text</code> to <code>Metadata</code>.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class BasicMetadataParser implements MetadataParser {

	static final String UNTERMINATED_STRING = " (Unterminated String)";
	static final String SUBTYPE_NOT_FOUND = "Subtype not found:";
	static final String INVALID_DOMAIN = "Invalid domain:";
	static final String INVALID_DEFAULTVALUE = "Invalid default value:";

	private static final Domain EMPTY_DOMAIN =Domain.Companion.getEMPTY();

	private final Splitter splitter;
	private final JavaContext context;

	private int count;

	/**
	 * Default constructor.
	 */
	public BasicMetadataParser() {
		this(new BasicContext(), new Splitter());
	}

	/**
	 * Default constructor.
	 */
	public BasicMetadataParser(JavaContext context) {
		this(context, new Splitter());
	}

	/**
	 * Complete comstructor.
	 * 
	 * @param splitter
	 * @param context
	 */
	public BasicMetadataParser(JavaContext context, Splitter splitter) {
		super();
		if (splitter == null) {
			throw new IllegalArgumentException("Splitter cannot be null!");
		}
		this.splitter = splitter;
		if (context == null) {
			throw new IllegalArgumentException("Context cannot be null!");
		}
		this.context = context;
	}

	// **************************************************
	// API
	// **************************************************

	@Override
	public Metadata parse(PplString pplString) {
		try {
			List<ParseNode> nodes = splitter.splitLayout(pplString.getPplMetadata());
			if (nodes.size() > 1) {
				ParseNode root = new ParseNode();
				Map<String, String> varMap = createRoot(root, nodes);
				return parse(EMPTY, root, 0);
			} else {
				return parse(EMPTY, nodes.get(0), 0);
			}
		} catch (ParseException e) {
			throw new PplParseException("Parsing error on text:\n" + pplString, e);
		}
	}

	protected Map<String, String> createRoot(ParseNode root, List<ParseNode> nodes) {
		Map<String, String> map = new HashMap<>();
		root.children = new ArrayList<>();
		for (ParseNode child : nodes) {
			if (child.isVar()) {
				map.put(child.name.substring(1), child.getDefaultValue());
			} else {
				root.children.add(child);
			}
		}
		if (map.containsKey(VAR_ROOT)) {
			root.name = map.get(VAR_ROOT);
		}
		return map;
	}

	protected Metadata parse(String parentId, ParseNode node, int index) {
		String name = parseName(node);
		Subtype subtype = parseSubtype(node);
		int size = parseSize(node, subtype);
		int scale = parseScale(node, subtype);
		int minOccurs = parseMinOccurs(node);
		int maxOccurs = parseMaxOccurs(node);
		Domain domain = parseDomain(node);
		MetaInfo metaInfo = new MetaInfo(index, name, subtype, size, scale, minOccurs, maxOccurs, domain,
				node.defaultValue, node.tags);
		return context.metadataFactory().create(metaInfo, parseChildren("", node));

	}

	protected List<Metadata> parseChildren(String parentId, ParseNode node) {
		if (!node.isComplex()) {
			return null;
		}
		List<Metadata> metas = new ArrayList<>();
		for (int i = 0; i < node.children.size(); i++) {
			metas.add(parse(parentId, node.children.get(i), i));
		}
		return metas;
	}

	protected String parseName(ParseNode node) {
		String name = (node.hasName()) ? node.getName() : NO_NAME_START + count++;
		if (!pplIsValidMetaName(name)) {
			throw new MetadataParseException("Invalid Metadata name:" + name, node);
		}
		return name;
	}

	protected Subtype parseSubtype(ParseNode node) {
		Subtype subtype = context.subtypeManager().fromText(node.getType(), node.isComplex());
		if (subtype == null) {
			throw new MetadataParseException(SUBTYPE_NOT_FOUND + node.getType(), node);
		}
		return subtype;
	}

	protected int parseSize(ParseNode node, Subtype subtype) {
		if (node.hasSize()) {
			if (subtype.getDataType().getSizeType().equals(SizeType.FIXED)) {
				throw new MetadataParseException(subtype + " do not support custom size." + subtype, node);
			}
			return extractSize(node);
		}
		return subtype.fixedSize();
	}

	protected int parseScale(ParseNode node, Subtype subtype) {
		if (node.hasSize()) {
			if (subtype.getDataType().getSizeType().equals(SizeType.FIXED)) {
				throw new MetadataParseException(subtype + " do not support custom scale." + subtype, node);
			}
			return extractScale(node);
		}
		return 0;
	}

	protected int extractSize(ParseNode node) {
		String size = node.getSize();
		int index = size.indexOf(Token.DECIMAL_SEP);
		if (index > 0) {
			size = size.substring(0, index);
		}
		return Integer.parseInt(size);
	}

	protected int extractScale(ParseNode node) {
		String scale = node.getSize();
		int index = scale.indexOf(Token.DECIMAL_SEP);
		if (index > 0) {
			scale = scale.substring(index + 1);
			return scale.length() > 0 ? Integer.parseInt(scale) : 0;
		}
		return 0;
	}

	protected int parseMinOccurs(ParseNode node) {
		return (node.hasOccurs()) ? extractMinOccurs(node.getOccurs()) : 0;
	}

	protected int extractMinOccurs(String occurs) {
		int index = occurs.indexOf(Token.OCCURS_RANGE);
		return (index < 0) ? DEFAULT_MIN_OCCURS : Integer.parseInt(occurs.substring(0, index));
	}

	protected int parseMaxOccurs(ParseNode node) {
		return (node.hasOccurs()) ? extractMaxOccurs(node.getOccurs()) : 1;
	}

	protected int extractMaxOccurs(String occurs) {
		int index = occurs.indexOf(Token.OCCURS_RANGE);
		return (index < -1) ? Integer.parseInt(occurs) : Integer.parseInt(occurs.substring(index + 1));
	}

	protected Domain parseDomain(ParseNode node) {
		String domainStr = node.getDomain();
		if (domainStr == null || domainStr.length() < 3) {
			return EMPTY_DOMAIN;
		}
		if (domainStr.charAt(0) != Token.DOMAIN_BEGIN || domainStr.charAt(domainStr.length() - 1) != Token.DOMAIN_END) {
			throw new MetadataParseException(INVALID_DOMAIN + domainStr, node);
		}
		domainStr = domainStr.substring(1, domainStr.length() - 1);
		if (domainStr.trim().isEmpty()) {
			return EMPTY_DOMAIN;
		}
		List<String> list = new ArrayList<>();
		char c;
		int beginIndex = 0;
		int endIndex = domainStr.length();
		for (int i = 0; i < domainStr.length(); i++) {
			c = domainStr.charAt(i);
			if (c == Token.PLIC || c == Token.QUOTE) {
				try {
					i = pplNextStringDelimiter(domainStr, i, c);
				} catch (ParseException e) {
					throw new MetadataParseException(INVALID_DOMAIN + domainStr, node, e);
				}
				continue;
			}
			if (c == Token.DOMAIN_SEPARATOR) {
				endIndex = i;
				list.add(extractItem(domainStr, beginIndex, endIndex));
				beginIndex = endIndex + 1;
			}

		}
		list.add(extractItem(domainStr, beginIndex, domainStr.length()));

		return createDomain(node.name + Token.PATH_SEP + "domain", toDomainItemList(list));
	}

	private String extractItem(final String domain, int beginIndex, int endIndex) {
		String s = domain.substring(beginIndex, endIndex);
		char firstChar = s.charAt(0);
		if (firstChar == Token.QUOTE || firstChar == Token.PLIC) {
			char lastChar = s.charAt(s.length() - 1);
			if (firstChar == lastChar) {
				return s.substring(1, s.length() - 1);
			}
		}
		return s;
	}

	// **************************************************
	// get / set
	// **************************************************

	public Splitter getSplitter() {
		return splitter;
	}

	public JavaContext getContext() {
		return context;
	}

}
