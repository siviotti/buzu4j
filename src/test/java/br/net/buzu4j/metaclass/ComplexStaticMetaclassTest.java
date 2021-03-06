package br.net.buzu4j.metaclass;

import br.net.buzu.model.Kind;
import br.net.buzu.model.MetaInfo;
import br.net.buzu4j.model.Metaclass;
import br.net.buzu.model.Subtype;
import br.net.buzu4j.metaclass.ComplexStaticMetaclass;
import br.net.buzu4j.metaclass.SimpleStaticMetaclass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Douglas siviotti
 *
 */
public class ComplexStaticMetaclassTest {

	static MetaInfo metaInfo = new MetaInfo(31, "color", Subtype.STRING, 10, 0, 1, 2);
	static SimpleStaticMetaclass ssm1 = new SimpleStaticMetaclass(null, String.class, String.class, Kind.ATOMIC, metaInfo, null);
	static SimpleStaticMetaclass ssm2 = new SimpleStaticMetaclass(null, String.class, String.class, Kind.ATOMIC, metaInfo, null);
	static List<Metaclass> children = new ArrayList<>();
	static ComplexStaticMetaclass cmm1;
	static ComplexStaticMetaclass cmm2;
	
	static {
		children.add(ssm1);
		children.add(ssm2);
		
		cmm1 = new ComplexStaticMetaclass(null, List.class, String.class, Kind.RECORD, metaInfo, null, children);
		cmm2 = new ComplexStaticMetaclass(null, List.class, String.class, Kind.RECORD, metaInfo, null, children);
	}
	
	
	@Test
	public void testIsStatic() {
		assertTrue(cmm1.isStatic());
	}

	@Test
	public void testMaxSerialSize() {
		assertEquals(80, cmm1.serialMaxSize());
	}
	
	@Test
	public void testEquals() {
		assertEquals(cmm1, cmm2);
		assertEquals(cmm2, cmm1);
		assertEquals(cmm1, cmm1);
		assertFalse(cmm1.equals(null));
	}
	
	@Test
	public void testHascode() {
		assertEquals(cmm1.hashCode(), cmm2.hashCode());
	}

}
