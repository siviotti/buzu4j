package br.net.buzu4j;

import br.net.buzu4j.api.JavaPplMapper;
import br.net.buzu4j.context.BasicContext;
import br.net.buzu4j.context.JavaContext;
import br.net.buzu4j.metaclass.AnnotationSkipStrategy;
import br.net.buzu4j.metaclass.BasicMetaclassReader;
import br.net.buzu4j.metaclass.BasicSkipStrategy;
import br.net.buzu4j.metadata.build.load.BasicMetadataLoader;
import br.net.buzu4j.metadata.build.parse.BasicMetadataParser;
import br.net.buzu4j.metadata.build.parse.Splitter;
import br.net.buzu.model.Dialect;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * BuzuBuilder Unit Test.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class BuzuBuilderTest {

	@Test
	public void test01DefaultsAfterSimpleCreate() {
		Buzu buzu = new BuzuBuilder().build();
		assertEquals(Buzu.class, buzu.getClass());
		assertEquals(BasicContext.class, buzu.context().getClass());
		assertEquals(BasicMetadataParser.class, buzu.parser().getClass());
		assertEquals(Splitter.class, ((BasicMetadataParser) buzu.parser()).getSplitter().getClass());
		assertEquals(BasicMetaclassReader.class, buzu.reader().getClass());
		assertEquals(BasicSkipStrategy.class, ((BasicMetaclassReader) buzu.reader()).skipStrategy().getClass());
		assertEquals(BasicMetadataLoader.class, buzu.loader().getClass());
		assertTrue(buzu instanceof JavaPplMapper);
	}

	@Test
	public void test02DefaultsAfterNulls() {
		Buzu buzu = new BuzuBuilder().context(null).splitter(null).metaclassReader(null)
				.metadataLoader(null).metadataParser(null).skipStrategy(null).dialect(null).build();
		assertEquals(Buzu.class, buzu.getClass());
		assertEquals(BasicContext.class, buzu.context().getClass());
		assertEquals(BasicMetadataParser.class, buzu.parser().getClass());
		assertEquals(Splitter.class, ((BasicMetadataParser) buzu.parser()).getSplitter().getClass());
		assertEquals(BasicMetaclassReader.class, buzu.reader().getClass());
		assertEquals(BasicSkipStrategy.class, ((BasicMetaclassReader) buzu.reader()).skipStrategy().getClass());
		assertEquals(BasicMetadataLoader.class, buzu.loader().getClass());
		assertTrue(buzu instanceof JavaPplMapper);
	}

	@Test
	public void test03Custom() {
		BuzuBuilder builder = new BuzuBuilder();
		Buzu buzu = builder.context(new CustomContext()).splitter(new CustomSplitter())
				.metaclassReader(new CustomReader()).metadataLoader(new CustomLoader())
				.metadataParser(new CustomParser()).skipStrategy(new CustomSkipStrategy()).dialect(Dialect.VERBOSE)
				.addIgnore(CustomIfgnore.class).addUse(CustomUse.class).build();
		assertEquals(Buzu.class, buzu.getClass());

		assertEquals(CustomContext.class, buzu.context().getClass());

		assertEquals(CustomParser.class, buzu.parser().getClass());
		assertEquals(Splitter.class, ((BasicMetadataParser) buzu.parser()).getSplitter().getClass());
		assertEquals(CustomSplitter.class, builder.getSplitter().getClass());
		assertEquals(CustomReader.class, buzu.reader().getClass());
		assertEquals(BasicSkipStrategy.class, ((BasicMetaclassReader) buzu.reader()).skipStrategy().getClass());
		assertEquals(AnnotationSkipStrategy.class, builder.getSkipStrategy().getClass());
		assertEquals(CustomLoader.class, buzu.loader().getClass());
		assertTrue(buzu instanceof JavaPplMapper);
	}

	@Test
	public void testContext() {
		JavaContext context = new BasicContext() {
		};
		BuzuBuilder builder = new BuzuBuilder().context(context);
		Buzu buzu = builder.build();
		assertEquals(Buzu.class, buzu.getClass());
		assertEquals(context, buzu.context());
		assertEquals(context, builder.getContext());
	}

	@Test
	public void testAnnotationStrategy() {

	}

}

class CustomContext extends BasicContext {

}

class CustomReader extends BasicMetaclassReader {

}

class CustomLoader extends BasicMetadataLoader {

}

class CustomParser extends BasicMetadataParser {

}

class CustomSkipStrategy extends BasicSkipStrategy {

}

class CustomSplitter extends Splitter {

}

@interface CustomIfgnore {

}

@interface CustomUse {

}