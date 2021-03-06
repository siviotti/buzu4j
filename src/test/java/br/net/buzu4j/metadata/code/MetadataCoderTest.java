package br.net.buzu4j.metadata.code;

import br.net.buzu.lang.Syntax;
import br.net.buzu4j.metadata.BasicMetadata;
import br.net.buzu4j.metadata.ComplexMetadata;
import br.net.buzu4j.metadata.ComplexMetadataTest;
import br.net.buzu4j.metadata.SimpleMetadataTest;
import br.net.buzu.model.*;
import br.net.buzu4j.model.MetaInfoTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static br.net.buzu.lang.Syntax.ENTER;
import static org.junit.Assert.*;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class MetadataCoderTest {

	private static final BasicMetadata COLOR = SimpleMetadataTest.createSample("color", Subtype.STRING, 10, 0, 0, 1,
			MetaInfoTest.domain("black", "white", "red"), "red", "KIX");
	private static final BasicMetadata SIZE = SimpleMetadataTest.createSample("size", Subtype.INTEGER, 10, 0, 1, 1,
			Domain.Companion.getEMPTY(), Syntax.EMPTY, Syntax.EMPTY);
	private static final List<Metadata> CHILDREN = new ArrayList<>();

	static {
		CHILDREN.add(COLOR);
		CHILDREN.add(SIZE);
	}
	private static final ComplexMetadata BOOK = ComplexMetadataTest.createSample("book", Subtype.OBJ, 0, 0, 0, 0, Domain.Companion.getEMPTY(), Syntax.EMPTY, Syntax.EMPTY, CHILDREN);
	
	@Test
	public void testVerbose() {
		VerboseMetadataCoder coder = new VerboseMetadataCoder();
		assertEquals(Dialect.VERBOSE, coder.dialect());
		assertEquals(VerboseMetadataCoder.SPACE, coder.afterName());
		assertEquals(VerboseMetadataCoder.SPACE, coder.afterType());
		assertEquals(VerboseMetadataCoder.SPACE, coder.afterSize());
		assertEquals(VerboseMetadataCoder.TAB, coder.indentationElement());
		assertEquals(VerboseMetadataCoder.ENTER, coder.afterMetadataEnd());
		assertEquals(VerboseMetadataCoder.ENTER, coder.afterMetadataEnd());
		
		String code = coder.code(BOOK);
		
		assertTrue(code.contains(Syntax.DEFAULT_OCCURS));
		assertTrue(code.contains(""+ENTER));
		assertTrue(code.contains(""+Subtype.STRING.getId()));
		
	}

	@Test
	public void testNatural() {
		NaturalMetadataCoder coder = new NaturalMetadataCoder();
		assertEquals(Dialect.NATURAL, coder.dialect());
		assertEquals(VerboseMetadataCoder.SPACE, coder.afterName());
		assertEquals(VerboseMetadataCoder.SPACE, coder.afterType());
		assertEquals(VerboseMetadataCoder.SPACE, coder.afterSize());
		assertEquals(VerboseMetadataCoder.TAB, coder.indentationElement());
		assertEquals(VerboseMetadataCoder.ENTER, coder.afterMetadataEnd());
		
		String code = coder.code(BOOK);
		
		assertFalse(code.contains(Syntax.DEFAULT_OCCURS));
		assertTrue(code.contains(""+Syntax.ENTER));
		assertTrue(code.contains(""+Subtype.STRING.getId()));
	}

	@Test
	public void testShort() {
		ShortMetadataCoder coder = new ShortMetadataCoder();
		assertEquals(Dialect.SHORT, coder.dialect());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterName());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterType());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterSize());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.indentationElement());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterMetadataEnd());
		
		String code = coder.code(BOOK);
		
		assertFalse(code.contains(Syntax.DEFAULT_OCCURS));
		assertFalse(code.contains(""+Syntax.ENTER));
		assertTrue(code.contains(""+Subtype.STRING.getId()));
	}

	@Test
	public void testCompact() {
		CompactMetadataCoder coder = new CompactMetadataCoder();
		assertEquals(Dialect.COMPACT, coder.dialect());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterName());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterType());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterSize());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.indentationElement());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterMetadataEnd());
		
		String code = coder.code(BOOK);
		
		assertFalse(code.contains(Syntax.DEFAULT_OCCURS));
		assertFalse(code.contains(""+Syntax.ENTER));
		assertFalse(code.contains(""+Subtype.STRING.getId()));
	}

	@Test
	public void testStructural() {
		StructuralMetadataCoder coder = new StructuralMetadataCoder();
		assertEquals(Dialect.STRUCTURAL, coder.dialect());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterName());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterType());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterSize());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.indentationElement());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterMetadataEnd());
		
		String code = coder.code(BOOK);
		
		assertFalse(code.contains(Syntax.DEFAULT_OCCURS));
		assertFalse(code.contains(""+Syntax.ENTER));
	}

}
