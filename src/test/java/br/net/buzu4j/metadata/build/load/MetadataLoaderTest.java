package br.net.buzu4j.metadata.build.load;

import br.net.buzu4j.conserpro.Empresa;
import br.net.buzu4j.conserpro.NaturezaJuridica;
import br.net.buzu4j.conserpro.Socio;
import br.net.buzu4j.conserpro.Telefone;
import br.net.buzu4j.context.BasicContext;
import br.net.buzu4j.metaclass.BasicMetaclassReader;
import br.net.buzu4j.model.Metaclass;
import br.net.buzu.model.Metadata;
import br.net.buzu.model.Subtype;
import br.net.buzu4j.sample.pojo.Person;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class MetadataLoaderTest {
	
	private BasicMetadataLoader loader;
	private BasicMetaclassReader reader;
	
	@Before
	public void before() {
		loader= new BasicMetadataLoader();
		reader = new BasicMetaclassReader();
	}

	public static Empresa createEmpresa() {
		String EMPRESA_NOME = "Empresa Teste";
		int NJ_CODIGO = 206;
		String EMPRESA_CNPJ = "99999999999999";
		String NJ_NOME = "Sociedade Empresaria Ltda";
		Date WW2 = new Date(new GregorianCalendar(1939, Calendar.SEPTEMBER, 1).getTimeInMillis());

		List<Telefone> telefonesSocio1 = new ArrayList<Telefone>();
		telefonesSocio1.add(new Telefone("99", "11111111"));
		telefonesSocio1.add(new Telefone("88", "22222222"));
		Socio socio1 = new Socio("33333333333", "Socio 01", 30.0, telefonesSocio1, "socio1@teste.com",
				"socio1@hotmail.com");

		List<Telefone> telefonesSocio2 = new ArrayList<Telefone>();
		telefonesSocio2.add(new Telefone("77", "33333333"));
		telefonesSocio2.add(new Telefone("66", "44444444"));
		telefonesSocio2.add(new Telefone("55", "55555555"));
		Socio socio2 = new Socio("22222222222", "Socio 02", 30.0, telefonesSocio2, "socio2@teste.com");

		List<Telefone> telefonesPresidente = new ArrayList<Telefone>();
		telefonesPresidente.add(new Telefone("44", "66666666"));
		telefonesPresidente.add(new Telefone("33", "77777777"));
		telefonesPresidente.add(new Telefone("22", "88888888"));
		Socio presidente = new Socio("11111111111", "President", 30.0, telefonesSocio2, "presidente@teste.com");

		Empresa empresa = new Empresa(EMPRESA_CNPJ, EMPRESA_NOME, WW2, true, new NaturezaJuridica(NJ_CODIGO, NJ_NOME),
				presidente, socio1, socio2);

		empresa.setCapital(new BigDecimal("5000.000"));
		empresa.setMicroEmpresa(false);
		empresa.setDtFechamento(new Date());
		return empresa;
	}

	@Test
	public void testEmpresa() {
		Metaclass metaClass = new BasicMetaclassReader().read(Empresa.class);
		Metadata meta = new BasicMetadataLoader(new BasicContext()).load(createEmpresa(), metaClass);
		assertEquals(Subtype.OBJ, meta.info(). getSubtype());
	}
	
	@Test
	public void testPerson() {
		Person person = new Person("Ladybug", 15, "Paris");
		Metaclass metaclass = reader.read(person.getClass());
		Metadata metadataLadybug = loader.load(person, metaclass);
		assertEquals(3, metadataLadybug.children().size());
	}

	//@Test TODO não gerar Metadados de campos NULL
	public void testPersonNulls() {
		Person person = new Person(null, 15, null);
		Metaclass metaclass = reader.read(person.getClass());
		Metadata metadataLadybug = loader.load(person, metaclass);
		assertEquals(1, metadataLadybug.children().size());
	}

}
