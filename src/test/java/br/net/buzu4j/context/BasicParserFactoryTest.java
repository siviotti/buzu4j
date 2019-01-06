package br.net.buzu4j.context;

import br.net.buzu4j.api.PayloadMapper;
import br.net.buzu4j.context.BasicParserFactory;
import br.net.buzu4j.context.ParserFactory;
import br.net.buzu4j.metaclass.SimpleMetaclass;
import br.net.buzu.model.Kind;
import br.net.buzu.model.MetaInfo;
import br.net.buzu4j.model.Metaclass;
import br.net.buzu.model.Subtype;
import br.net.buzu4j.parsing.simple.text.StringMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class BasicParserFactoryTest {

	@Test
	public void test() {
		ParserFactory parserFactory = new BasicParserFactory();
		MetaInfo metaInfo = new MetaInfo(0, "testname", Subtype.STRING, 10,0, 0, 1);
		Metaclass metaclass = new SimpleMetaclass(null, String.class, String.class, Kind.ATOMIC, metaInfo, null);
		PayloadMapper parser = parserFactory.create(metaclass);
		assertEquals(StringMapper.class, parser.getClass());
	}

}
